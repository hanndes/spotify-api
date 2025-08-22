package com.handedereli.spotify_project.controller;

import com.handedereli.spotify_project.dto.AlbumDto;
import com.handedereli.spotify_project.dto.ArtistDto;
import com.handedereli.spotify_project.dto.TrackDto;
import com.handedereli.spotify_project.service.AlbumService;
import com.handedereli.spotify_project.service.ArtistService;
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
    private final ArtistService artistService;
    private final TrackService trackService;
    private final AlbumService albumService;

    @GetMapping("/ping")
    public String ping() { return "ok"; }
    @GetMapping("/artists/{id}")
    public ResponseEntity<ArtistDto> getArtist(@PathVariable String id) {
        ArtistDto artist = artistService.getArtist(id);
        return ResponseEntity.ok(artist);
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

    // 🔹 YENİ: Albüm içindeki track’ler
    // GET /api/albums/{albumId}/tracks?market=TR&limit=20&offset=0
    @GetMapping("/albums/{albumId}/tracks")
    public ResponseEntity<List<TrackDto>> getAlbumTracks(
            @PathVariable String albumId,
            @RequestParam(defaultValue = "TR") String market) {
        return ResponseEntity.ok(albumService.getAlbumTracks(albumId, market));
    }
    @GetMapping("/albums/{id}")
    public ResponseEntity<List<TrackDto>> getAlbumWithTracks(
            @PathVariable String id,
            @RequestParam(defaultValue = "TR") String market) {
        return ResponseEntity.ok(albumService.getAlbumTracks(id, market));
    }

    // 2. Albüm + trackler
    @GetMapping("/{id}/albums-with-tracks")
    public ResponseEntity<ArtistDto> getArtistAlbumsWithTracks(
            @PathVariable String id,
            @RequestParam(defaultValue = "TR") String market) {
        return ResponseEntity.ok(albumService.getArtistAlbumsWithTracks(id, market));
    }

}
