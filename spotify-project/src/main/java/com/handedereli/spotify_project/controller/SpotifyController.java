package com.handedereli.spotify_project.controller;

import com.handedereli.spotify_project.dto.ArtistBundleDto;
import com.handedereli.spotify_project.dto.ArtistDto;
import com.handedereli.spotify_project.service.SpotifyClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SpotifyController {

    private final SpotifyClient spotifyClient;

    @GetMapping("/ping")
    public String ping() { return "ok"; }

    @GetMapping("/artist-summary")
    public ResponseEntity<ArtistBundleDto> getSummary(
            @RequestParam String name,
            @RequestParam(defaultValue = "TR") String market
    ) {
        return ResponseEntity.ok(aggregator.getArtistSummaryByName(name, market));
    }


    @GetMapping("/artists/{id}")
    public ArtistDto getArtist(@PathVariable String id) {
        return spotifyClient.getArtist(id);
    }

    @GetMapping("/rihanna/top-tracks")
    public ResponseEntity<String> rihannaTopTracks(@RequestParam(defaultValue = "TR") String market) {
        String body = spotifyClient.getArtistTopTracks("5pKCCKE2ajJHZ9KAiaK11H", market);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/albums/{id}/tracks")
    public ResponseEntity<String> albumTracks(@PathVariable String id,
                                              @RequestParam(defaultValue = "TR") String market,
                                              @RequestParam(defaultValue = "50") int limit,
                                              @RequestParam(defaultValue = "0") int offset) {
        String body = spotifyClient.getAlbumTracks(id, market, limit, offset);
        return ResponseEntity.ok(body);
    }

}
