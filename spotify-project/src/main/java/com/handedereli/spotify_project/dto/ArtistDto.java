package com.handedereli.spotify_project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public record ArtistDto(
        String name,
        int popularity,
        @JsonProperty("followers") Followers followers,
        List<String> genres
) {
    public record Followers(@JsonProperty("total") long total) {}
}


