package com.handedereli.spotify_project.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.handedereli.spotify_project.dto.ArtistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistService {

    private final SpotifyClient spotifyClient;

    public ArtistDto getArtist(String artistId) {
        JsonNode artistRoot = spotifyClient.getArtist(artistId);

        // genres dönüştürme
        List<String> genres = new ArrayList<>();
        JsonNode genresNode = artistRoot.path("genres");
        if (genresNode.isArray()) {
            genresNode.forEach(n -> genres.add(n.asText()));
        }

        return new ArtistDto(
                artistRoot.path("id").asText(),
                artistRoot.path("name").asText(),
                artistRoot.path("popularity").asInt(),
                new ArtistDto.Followers(artistRoot.path("followers").path("total").asLong()),
                genres,
                null // albümler burada doldurulmaz
        );
    }
}
