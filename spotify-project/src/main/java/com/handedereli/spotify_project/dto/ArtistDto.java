package com.handedereli.spotify_project.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public record ArtistDto(
        String id,
        String name,
        int popularity,
        Followers followers,
        List<String> genres,
        List<AlbumDto> albums
) {
    public record Followers(long total) {}

    // Serviste albümleri enjekte etmek için helper
    public ArtistDto withAlbums(List<AlbumDto> albums) {
        return new ArtistDto(id, name, popularity, followers, genres, albums);
    }
}