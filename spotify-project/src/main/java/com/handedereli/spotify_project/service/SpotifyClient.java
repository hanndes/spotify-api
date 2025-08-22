package com.handedereli.spotify_project.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.handedereli.spotify_project.dto.ArtistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class SpotifyClient {

    private final WebClient spotifyWebClient;  // baseUrl: https://api.spotify.com/v1
    private final TokenService tokenService;


    public JsonNode getArtist(String artistId) {
        try {
            return spotifyWebClient.get()
                    .uri("/artists/{id}", artistId)
                    .header("Authorization", "Bearer " + tokenService.getAccessToken())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
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

    public JsonNode getAlbumWithTracks(String albumId, String market) {
        return spotifyWebClient.get()
                .uri(uri -> uri.path("/albums/{id}")
                        .queryParam("market", market)
                        .build(albumId))
                .header("Authorization", "Bearer " + tokenService.getAccessToken())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }

    public JsonNode searchArtist(String artistName) {
        return spotifyWebClient.get()
                .uri(uri -> uri.path("/search")
                        .queryParam("q", artistName)
                        .queryParam("type", "artist")
                        .queryParam("limit", "1")
                        .build())
                .header("Authorization", "Bearer " + tokenService.getAccessToken())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }

    public JsonNode searchArtistByName(String name, String market) {
        String q = URLEncoder.encode(name, StandardCharsets.UTF_8);
        JsonNode root = spotifyWebClient.get()
                .uri(uri -> uri.path("/search")
                        .queryParam("q", q)
                        .queryParam("type", "artist")
                        .queryParam("market", market)
                        .queryParam("limit", 1)
                        .build())
                .header("Authorization", "Bearer " + tokenService.getAccessToken())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        JsonNode items = root.path("artists").path("items");
        if (items.isArray() && items.size() > 0) return items.get(0);
        throw new IllegalStateException("Artist not found: " + name);
    }
}