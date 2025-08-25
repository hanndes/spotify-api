package com.handedereli.spotify_project.dto;

import java.util.List;

public record ArtistBundleDto(
        ArtistDto artist,
        List<TrackDto> topTracks,
        List<AlbumDto> albums
) {}