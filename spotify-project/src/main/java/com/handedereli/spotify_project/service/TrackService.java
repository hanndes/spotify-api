package com.handedereli.spotify_project.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.handedereli.spotify_project.dto.TrackDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackService {
    private final SpotifyClient client;

    public List<TrackDto> getTopTracks(String artistId, String market) {
        JsonNode root = client.getArtistTopTracks(artistId, market);
        List<TrackDto> tracks = new ArrayList<>();

        // Spotify API -> { "tracks": [ {track1}, {track2}, ... ] }
        for (JsonNode t : root.path("tracks")) {
            tracks.add(new TrackDto(
                    t.path("id").asText(),
                    t.path("name").asText(),
                    t.path("duration_ms").asInt()
            ));
        }

        return tracks;
    }
}
