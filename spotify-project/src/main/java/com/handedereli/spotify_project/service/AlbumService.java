package com.handedereli.spotify_project.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.handedereli.spotify_project.dto.AlbumDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final SpotifyClient client;

    public List<AlbumDto> getArtistAlbums(String artistId, String market) {
        JsonNode root = client.getArtistAlbums(artistId, market);
        List<AlbumDto> albums = new ArrayList<>();
        // Spotify API -> { "items": [ {album1}, {album2}, ... ] }
        for (JsonNode album : root.path("items")) {
            albums.add(new AlbumDto(
                    album.path("id").asText(),
                    album.path("name").asText(),
                    album.path("release_date").asText(),
                    album.path("total_tracks").asInt()
            ));
        }

        return albums;
    }
}