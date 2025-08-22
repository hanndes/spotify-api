package com.handedereli.spotify_project.dto;


import java.util.List;

public record AlbumDto(
        String id,
        String name,
        String release_date,   // Spotify JSON alanıyla aynı
        Integer total_tracks,
        List<TrackDto> tracks
) { }
