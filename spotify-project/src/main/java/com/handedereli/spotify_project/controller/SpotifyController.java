package com.handedereli.spotify_project.controller;

import com.handedereli.spotify_project.dto.AlbumDto;
import com.handedereli.spotify_project.dto.ArtistDto;
import com.handedereli.spotify_project.dto.TrackDto;
import com.handedereli.spotify_project.service.AlbumService;
import com.handedereli.spotify_project.service.SpotifyClient;
import com.handedereli.spotify_project.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SpotifyController {

    private final SpotifyClient spotifyWebClient;
    private final TrackService trackService;
    private final AlbumService albumService;

    @GetMapping("/ping")
    public String ping() { return "ok"; }



    @GetMapping("/artists/{id}")
   public ArtistDto getArtist(@PathVariable String id) {
        return spotifyWebClient.getArtist(id);
    }

    @GetMapping("/artist/{id}/top-tracks")
    public ResponseEntity<List<TrackDto>> getArtistTopTracks(
            @PathVariable String id,
            @RequestParam(defaultValue = "TR") String market) {
        return ResponseEntity.ok(trackService.getTopTracks(id, market));
    }
    // YENİ: Sanatçının albümleri
    @GetMapping("/artist/{id}/albums")
    public ResponseEntity<List<AlbumDto>> getArtistAlbums(
            @PathVariable String id,
            @RequestParam(defaultValue = "TR") String market) {
        return ResponseEntity.ok(albumService.getArtistAlbums(id, market));
    }


}
