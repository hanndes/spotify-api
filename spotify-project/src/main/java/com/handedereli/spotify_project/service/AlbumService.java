package com.handedereli.spotify_project.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.handedereli.spotify_project.dto.AlbumDto;
import com.handedereli.spotify_project.dto.ArtistBundleDto;
import com.handedereli.spotify_project.dto.ArtistDto;
import com.handedereli.spotify_project.dto.TrackDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final SpotifyClient spotifyClient;


    public List<AlbumDto> getArtistAlbums(String artistId, String market) {
        JsonNode root = spotifyClient.getArtistAlbums(artistId, market);
        List<AlbumDto> albums = new ArrayList<>();
        // Spotify API -> { "items": [ {album1}, {album2}, ... ] }
        for (JsonNode album : root.path("items")) {
            albums.add(new AlbumDto(
                    album.path("id").asText(),
                    album.path("name").asText(),
                    album.path("release_date").asText(),
                    album.path("total_tracks").asInt(),null
            ));
        }
        return albums;
    }
    public List<TrackDto> getAlbumTracks(String albumId, String market) {
        JsonNode root = spotifyClient.getAlbumWithTracks(albumId, market);

        JsonNode items = root.has("items")
                ? root.path("items")
                : root.path("tracks").path("items");

        List<TrackDto> result = new ArrayList<>();
        items.forEach(t -> result.add(new TrackDto(
                t.path("id").asText(),
                t.path("name").asText(),
                t.path("duration_ms").asInt()
        )));
        return result;
    }

    // AlbumService.java
    public ArtistBundleDto getArtistAlbumsWithTracks(String artistId, String market) {
        // 1) Artist bilgisi
        ArtistDto artist = spotifyClient.getArtist(artistId);

        // 2) Sanatçının albümleri
        JsonNode albumsRoot = spotifyClient.getArtistAlbums(artistId, market);
        List<AlbumDto> albums = new ArrayList<>();

        for (JsonNode albumNode : albumsRoot.path("items")) {
            String albumId     = albumNode.path("id").asText();
            String albumName   = albumNode.path("name").asText();
            String releaseDate = albumNode.path("release_date").asText();
            int totalTracks    = albumNode.path("total_tracks").asInt();

            // 3) Albümün track’leri (DOĞRU PATH: tracks.items)
            JsonNode albumWithTracks = spotifyClient.getAlbumWithTracks(albumId, market);
            List<TrackDto> tracks = new ArrayList<>();
            JsonNode trackItems = albumWithTracks.path("tracks").path("items");
            if (trackItems.isArray()) {
                for (JsonNode t : trackItems) {
                    tracks.add(new TrackDto(
                            t.path("id").asText(),
                            t.path("name").asText(),
                            t.path("duration_ms").asInt()
                    ));
                }
            }

            albums.add(new AlbumDto(
                    albumId,
                    albumName,
                    releaseDate,
                    totalTracks,
                    tracks
            ));
        }

        List<TrackDto> topTracks = new ArrayList<>();
        JsonNode topRoot = spotifyClient.getArtistTopTracks(artistId, market); // /artists/{id}/top-tracks
        JsonNode topItems = topRoot.path("tracks");
        if (topItems.isArray()) {
            for (JsonNode t : topItems) {
                topTracks.add(new TrackDto(
                        t.path("id").asText(),
                        t.path("name").asText(),
                        t.path("duration_ms").asInt()
                ));
            }
        }

        // 5) Hepsini tek pakette döndür
        return new ArtistBundleDto(artist, albums);
    }


}