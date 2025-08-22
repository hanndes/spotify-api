package com.handedereli.spotify_project.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ArtistDto(
        String name,
        int popularity,
        Followers followers,
        List<String> genres,
        List<AlbumDto> albums
) {
    public record Followers(long total) {}

    // Serviste albümleri enjekte etmek için helper
    public ArtistDto withAlbums(List<AlbumDto> albums) {
        return new ArtistDto(name, popularity, followers, genres, albums);
    }
}