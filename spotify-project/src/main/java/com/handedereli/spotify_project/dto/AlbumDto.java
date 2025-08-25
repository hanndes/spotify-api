package com.handedereli.spotify_project.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AlbumDto(
        String name,
        String release_date,
        Integer total_tracks,
        List<TrackDto> tracks
) { }
