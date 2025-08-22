package com.handedereli.spotify_project.dto;
import java.util.List;

public record TrackDto(
        String id,
        String name,
        Integer popularity,
        Integer duration_ms,
        List<ArtistDto> artistDtos
) {
}

