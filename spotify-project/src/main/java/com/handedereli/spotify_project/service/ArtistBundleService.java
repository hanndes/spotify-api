package com.handedereli.spotify_project.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.handedereli.spotify_project.dto.AlbumDto;
import com.handedereli.spotify_project.dto.ArtistBundleDto;
import com.handedereli.spotify_project.dto.ArtistDto;
import com.handedereli.spotify_project.dto.TrackDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistBundleService {

    private final SpotifyClient client;
    private final TrackService trackService;
    private final AlbumService albumService;

    /**
     * Sanatçı ID'si ile tüm bilgileri getir
     */
    public ArtistBundleDto getArtistBundle(String artistId, String market) {
        // Sanatçı bilgilerini getir
        ArtistDto artist = client.getArtist(artistId);

        // Popüler parçalarını getir
        List<TrackDto> tracks = trackService.getTopTracks(artistId, market);

        // Albümlerini getir
        List<AlbumDto> albums = albumService.getArtistAlbums(artistId, market);

        return new ArtistBundleDto(artist, albums);
    }

    /**
     * Sanatçı ismi ile arama yapıp tüm bilgileri getir
     */
    public ArtistBundleDto getArtistBundleByName(String artistName, String market) {
        // Önce sanatçıyı ara
        JsonNode searchResult = client.searchArtist(artistName);

        // İlk sonucu al
        JsonNode artists = searchResult.path("artists").path("items");
        if (!artists.isArray() || artists.isEmpty()) {
            throw new RuntimeException("Sanatçı bulunamadı: " + artistName);
        }

        String artistId = artists.get(0).path("id").asText();
        if (artistId.isEmpty()) {
            throw new RuntimeException("Sanatçı ID'si alınamadı: " + artistName);
        }

        // Sanatçı ID'si ile bundle'ı getir
        return getArtistBundle(artistId, market);
    }


}