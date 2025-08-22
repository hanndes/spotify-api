package com.handedereli.spotify_project.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.handedereli.spotify_project.dto.AlbumDto;
import com.handedereli.spotify_project.dto.ArtistBundleDto;
import com.handedereli.spotify_project.dto.ArtistDto;
import com.handedereli.spotify_project.dto.TrackDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistBundleService {
    private final WebClient spotifyWebClient;
    private final TokenService tokenService;
    private final ObjectMapper mapper;

 /*   public ArtistBundleDto getBundleByName(String name, String market) {
        // 1) önce artistId bul (search endpoint)
        JsonNode searchJson = call("/search?type=artist&limit=1&q=" + name);
        String artistId = searchJson.path("artists").path("items").get(0).path("id").asText();

        // 2) sonra 3 istek: artist, top-tracks, albums
        JsonNode artistJson = call("/artists/" + artistId);
        JsonNode tracksJson = call("/artists/" + artistId + "/top-tracks?market=" + market);
        JsonNode albumsJson = call("/artists/" + artistId + "/albums?include_groups=album,single&limit=20");

        // map → ArtistDto
        ArtistDto artist = new ArtistDto(
                artistJson.path("name").asText(),artistJson.path("popularity").asInt(),
                artistJson.path("followers").path("total").asLong(),
                mapper.convertValue(artistJson.path("genres"), new TypeReference<List<String>>() {})
        );

        // map → TrackDto
        List<TrackDto> tracks = new ArrayList<>();
        tracksJson.path("tracks").forEach(t -> tracks.add(new TrackDto(
                t.path("id").asText(),
                t.path("name").asText(),
                t.path("album").path("name").asText(),
                t.path("preview_url").isMissingNode() ? null : t.path("preview_url").asText(),
                t.path("popularity").asInt()
        )));

        // map → AlbumDto
        List<AlbumDto> albums = new ArrayList<>();
        albumsJson.path("items").forEach(al -> albums.add(new AlbumDto(
                al.path("id").asText(),
                al.path("name").asText(),
                al.path("release_date").asText(),
                al.path("total_tracks").asInt()
        )));

        return new ArtistBundleDto(artist, tracks, albums);
    }

    private JsonNode call(String path) {
        String json = spotifyWebClient.get()
                .uri(path)
                .header("Authorization", "Bearer " + tokenService.getAccessToken())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        try {
            return mapper.readTree(json);
        } catch (Exception e) {
            throw new RuntimeException("JSON parse error", e);
        }
    }

  */
}
