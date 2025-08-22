package com.handedereli.spotify_project.dto;

public record TrackDto(
        String id,
        String name,
        Integer duration_ms
) {
}

