package com.handedereli.spotify_project.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.handedereli.spotify_project.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistBundleService {

    private final SpotifyClient spotifyClient;

    public ArtistBundleDto getArtistBundleByName(String name, String market) {
        // 1) İsimden arama → ilk eşleşme
        JsonNode search = spotifyClient.searchArtistByName(name, market);
        JsonNode items = search.path("artists").path("items");
        if (!items.isArray() || items.size() == 0) {
            return new ArtistBundleDto(null, List.of(), List.of());
        }

        JsonNode first = items.get(0);
        String artistId = first.path("id").asText(null);
        if (artistId == null || artistId.isBlank()) {
            return new ArtistBundleDto(null, List.of(), List.of());
        }

        // 2) Artist temel bilgiler (normalize)
        List<String> genres = new ArrayList<>();
        JsonNode genresNode = first.path("genres"); // search sonucunda varsa kullan; yoksa getArtist'tan da çekebilirsin
        if (genresNode.isArray()) {
            genresNode.forEach(g -> genres.add(g.asText()));
        }

        long followersTotal = first.path("followers").path("total").asLong(0);
        int popularity = first.path("popularity").asInt(0);
        String artistName = first.path("name").asText("");

        // Bazı arama sonuçlarında followers/genres/popularity olmayabilir; garanti için detay çağrısı yapılabilir:
        if (followersTotal == 0 || genres.isEmpty() || popularity == 0) {
            JsonNode artistDetails = spotifyClient.getArtist(artistId);
            if (artistDetails.path("genres").isArray()) {
                genres.clear();
                artistDetails.path("genres").forEach(g -> genres.add(g.asText()));
            }
            followersTotal = artistDetails.path("followers").path("total").asLong(followersTotal);
            popularity = artistDetails.path("popularity").asInt(popularity);
            if (artistName == null || artistName.isBlank()) {
                artistName = artistDetails.path("name").asText("");
            }
        }

        ArtistDto artist = new ArtistDto(
                artistName,
                popularity,
                new ArtistDto.Followers(followersTotal),
                genres,
                null // albums'ü sonra set etmiyoruz; bundle'da ayrı alan
        );

        // 3) Top Tracks
        List<TrackDto> topTracks = new ArrayList<>();
        JsonNode tt = spotifyClient.getArtistTopTracks(artistId, market);
        JsonNode ttTracks = tt.path("tracks");
        if (ttTracks.isArray()) {
            for (JsonNode t : ttTracks) {
                topTracks.add(new TrackDto(
                        t.path("name").asText(""),
                        t.path("duration_ms").asInt(0)
                ));
            }
        }

        // 4) Albümler (+ her bir albümün track’leri)
        List<AlbumDto> albums = new ArrayList<>();
        JsonNode ar = spotifyClient.getArtistAlbums(artistId, market); // client'ta limit=50 gönder
        JsonNode albumItems = ar.path("items");
        if (albumItems.isArray()) {
            for (JsonNode alb : albumItems) {
                String albumId = alb.path("id").asText(null);
                if (albumId == null || albumId.isBlank()) continue;

                String albumName   = alb.path("name").asText("");
                String releaseDate = alb.path("release_date").asText("");
                int totalTracks    = alb.path("total_tracks").asInt(0);

                // Albüm + embedded tracks
                JsonNode albumFull = spotifyClient.getAlbumWithTracks(albumId, market);
                JsonNode trackItems = albumFull.path("tracks").path("items");

                List<TrackDto> tracks = new ArrayList<>();
                if (trackItems.isArray()) {
                    for (JsonNode tr : trackItems) {
                        tracks.add(new TrackDto(
                                tr.path("name").asText(""),
                                tr.path("duration_ms").asInt(0)
                        ));
                    }
                }

                albums.add(new AlbumDto(
                        albumName,
                        releaseDate,
                        totalTracks,
                        tracks
                ));
            }
        }

        return new ArtistBundleDto(artist, topTracks, albums);
    }

}