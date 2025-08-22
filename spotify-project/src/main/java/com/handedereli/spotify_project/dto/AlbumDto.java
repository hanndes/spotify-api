package com.handedereli.spotify_project.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AlbumDto {
    String id;
    String name;
    String release_date;
    Integer total_tracks;
    List<TrackDto> tracks;
}