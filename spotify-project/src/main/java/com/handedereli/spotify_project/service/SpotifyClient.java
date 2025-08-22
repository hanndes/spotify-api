package com.handedereli.spotify_project.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.handedereli.spotify_project.dto.ArtistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
public class SpotifyClient {

    private final WebClient spotifyWebClient;  // baseUrl set, auth header yok
    private final TokenService tokenService;
    private final ObjectMapper mapper; // Spring otomatik bean sağlar

    public ArtistDto getArtist(String artistId) {
        String json =  spotifyWebClient.get()
                .uri("/artists/{id}", artistId)
                .header("Authorization", "Bearer " + tokenService.getAccessToken())
                .retrieve()
                .bodyToMono(String.class) // JSON -> direkt tek sınıf
                .block();
        try {
            // JSON String → ArtistDto
            return mapper.readValue(json, ArtistDto.class);
        } catch (Exception e) {
            throw new RuntimeException("JSON parse edilemedi", e);
        }
    }

    // ---- EXISTING ----
    public String getArtistTopTracks(String artistId, String market) {
        return with401Retry(() ->
                spotifyWebClient.get()
                        .uri(u -> u.path("/artists/{id}/top-tracks")
                                .queryParam("market", market)
                                .build(artistId))
                        .header("Authorization", "Bearer " + tokenService.getAccessToken())
                        .retrieve()
                        .bodyToMono(String.class)
                        .block()
        );
    }

    public String getAlbumTracks(String albumId, String market, int limit, int offset) {
        return with401Retry(() ->
                spotifyWebClient.get()
                        .uri(u -> u.path("/albums/{id}/tracks")
                                .queryParam("market", market)
                                .queryParam("limit", limit)
                                .queryParam("offset", offset)
                                .build(albumId))
                        .header("Authorization", "Bearer " + tokenService.getAccessToken())
                        .retrieve()
                        .bodyToMono(String.class)
                        .block()
        );
    }

    // ---- NEW: sanatçı ara (id bulmak için) ----
    public String searchArtist(String name, int limit) {
        return with401Retry(() ->
                spotifyWebClient.get()
                        .uri(u -> u.path("/search")
                                .queryParam("type", "artist")
                                .queryParam("q", name)
                                .queryParam("limit", limit)
                                .build())
                        .header("Authorization", "Bearer " + tokenService.getAccessToken())
                        .retrieve()
                        .bodyToMono(String.class)
                        .block()
        );
    }

    // ---- NEW: sanatçının albümleri ----
    public String getArtistAlbums(String artistId, String market, int limit) {
        return with401Retry(() ->
                spotifyWebClient.get()
                        .uri(u -> u.path("/artists/{id}/albums")
                                .queryParam("include_groups", "album,single,appears_on,compilation")
                                .queryParam("market", market)
                                .queryParam("limit", limit)
                                .build(artistId))
                        .header("Authorization", "Bearer " + tokenService.getAccessToken())
                        .retrieve()
                        .bodyToMono(String.class)
                        .block()
        );
    }

    // 401 gelirse token'ı invalid edip 1 kez tekrar dener
    private String with401Retry(Caller c) {
        try { return c.call(); }
        catch (WebClientResponseException.Unauthorized e) {
            tokenService.invalidate();
            return c.call();
        }
    }
    @FunctionalInterface interface Caller { String call(); }
}

