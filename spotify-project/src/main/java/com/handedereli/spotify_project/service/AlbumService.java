package com.handedereli.spotify_project.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.handedereli.spotify_project.dto.AlbumDto;
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

        for (JsonNode album : root.path("items")) {
            albums.add(new AlbumDto(
                    album.path("id").asText(),
                    album.path("name").asText(),
                    album.path("release_date").asText(),
                    album.path("total_tracks").asInt(),
                    null
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

    public ArtistDto getArtistAlbumsWithTracks(String artistId, String market) {
        // 1) Artist bilgisi (JsonNode)
        JsonNode artistRoot = spotifyClient.getArtist(artistId);

        // 2) Genres'ı direkt list'e dönüştür
        List<String> genres = new ArrayList<>();
        JsonNode genresNode = artistRoot.path("genres");
        if (genresNode.isArray()) {
            genresNode.forEach(n -> genres.add(n.asText()));
        }

        // 3) ArtistDto'yu oluştur
        ArtistDto base = new ArtistDto(
                artistRoot.path("id").asText(),
                artistRoot.path("name").asText(),
                artistRoot.path("popularity").asInt(),
                new ArtistDto.Followers(artistRoot.path("followers").path("total").asLong()),
                genres,
                null // albümleri sonra ekleyeceğiz
        );

        // 4) Sanatçının albümleri
        JsonNode albumsRoot = spotifyClient.getArtistAlbums(artistId, market);
        List<AlbumDto> albums = new ArrayList<>();

        for (JsonNode a : albumsRoot.path("items")) {
            String albumId     = a.path("id").asText();
            String albumName   = a.path("name").asText();
            String releaseDate = a.path("release_date").asText();
            int totalTracks    = a.path("total_tracks").asInt();

            // Albümün track'leri
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

        // 5) ArtistDto'ya albümleri ekle
        return base.withAlbums(albums);
    }

}