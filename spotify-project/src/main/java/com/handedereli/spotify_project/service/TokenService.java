package com.handedereli.spotify_project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;

/**
 * Spotify Accounts API (Client Credentials Flow) token yöneticisi.
 * - access_token'ı alır, cache'ler ve süresi yaklaşınca yeniler.
 * - 401 gibi durumlarda invalidate() ile sıfırlanabilir.
 */
@Service
@RequiredArgsConstructor
public class TokenService {

    /** Auth header'ı olmayan, plain WebClient. Base URL şart değildir (abs. URL kullanıyoruz). */
    private final WebClient webClient;

    @Value("${spotify.accounts.base-url}") private String accountsBaseUrl; // https://accounts.spotify.com
    @Value("${spotify.client-id}")         private String clientId;
    @Value("${spotify.client-secret}")     private String clientSecret;

    private volatile String  cachedToken;
    private volatile Instant expiresAt = Instant.EPOCH;

    /**
     * Dışarıdan hep bunu çağırın.
     * Geçerliyse cache'den döner; değilse refresh eder.
     */
    public String getAccessToken() {
        // token hâlâ geçerliyse (30 sn tamponla) cache'den dön
        if (cachedToken != null && Instant.now().isBefore(expiresAt.minusSeconds(30))) {
            return cachedToken;
        }
        // çift çağrılarda tek sefer yenilemek için senkron blok
        synchronized (this) {
            if (cachedToken != null && Instant.now().isBefore(expiresAt.minusSeconds(30))) {
                return cachedToken;
            }
            refreshToken();
            return cachedToken;
        }
    }

    /**
     * 401 aldığınızda çağırın. Sonraki getAccessToken() otomatik yeniler.
     */
    public void invalidate() {
        cachedToken = null;
        expiresAt = Instant.EPOCH;
    }

    /* -------------------- private -------------------- */

    private void refreshToken() {
        String basic = Base64.getEncoder()
                .encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));

        Map<String, Object> resp;
        try {
            resp = webClient.post()
                    .uri(accountsBaseUrl + "/api/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .header("Authorization", "Basic " + basic)
                    .bodyValue("grant_type=client_credentials")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new IllegalStateException("Spotify token isteği başarısız: " + e.getStatusCode() + " " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new IllegalStateException("Spotify token isteği başarısız: " + e.getMessage(), e);
        }

        if (resp == null || resp.get("access_token") == null) {
            throw new IllegalStateException("Spotify token alınamadı (response null ya da access_token yok).");
        }

        String token = (String) resp.get("access_token");
        Number expiresIn = (Number) resp.getOrDefault("expires_in", 3600);

        cachedToken = token;
        expiresAt   = Instant.now().plusSeconds(expiresIn.longValue());
    }
}
