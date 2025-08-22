package com.handedereli.spotify_project.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AlbumDto {

    private String id;
    private String name;
    private String release_date;
    private Integer total_tracks;
    private List<TrackDto> tracks;

}