package com.handedereli.spotify_project.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.handedereli.spotify_project.dto.ArtistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class SpotifyClient {

    private final WebClient spotifyWebClient;  // baseUrl set, auth header yok
    private final TokenService tokenService;

    public ArtistDto getArtist(String artistId) {
        try {
            return spotifyWebClient.get()
                    .uri("/artists/{id}", artistId)
                    .header("Authorization", "Bearer " + tokenService.getAccessToken())
                    .retrieve()
                    .bodyToMono(ArtistDto.class)
                    .block(); // Sync çağrı için
        } catch (Exception e) {
            throw new RuntimeException("Sanatçı bilgisi alınamadı: " + e.getMessage(), e);
        }
    }
    public JsonNode getArtistTopTracks(String id, String market) {

        return spotifyWebClient.get()
                .uri(uri -> uri.path("/artists/{id}/top-tracks")
                        .queryParam("market", market)
                        .build(id))
                .header("Authorization", "Bearer " + tokenService.getAccessToken())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }

    // YENİ: Sanatçının albümlerini getir
    public JsonNode getArtistAlbums(String artistId, String market) {
        return spotifyWebClient.get()
                .uri(uri -> uri.path("/artists/{id}/albums")
                        .queryParam("market", market)
                        .queryParam("limit", "20")
                        .queryParam("include_groups", "album,single")
                        .build(artistId))
                .header("Authorization", "Bearer " + tokenService.getAccessToken())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }

}

