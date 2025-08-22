package com.handedereli.spotify_project.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;


public record ArtistDto(
        String name,
        int popularity,
        long followers,
        List<String> genres
) {
    @JsonCreator
    public ArtistDto(
            String name,
            int popularity,
            @JsonProperty("followers") Map<String, Object> followers,
            List<String> genres
    ) {
        this(name,
                popularity,
                followers != null && followers.get("total") instanceof Number n ? n.longValue() : 0,
                genres);
    }
}

/*
@Data
public class ArtistDto {
    private String name;
    private int popularity;
    // istersen tut  private String type;
 //   private long followers;  // followers.total buraya gelecek
    @JsonAlias("followers.total")
    private long followers;
    private List<String> genres;  // ["pop", "dance pop", ...]

   /* Spotify "followers": {"href": null, "total": 67848049}
    @JsonProperty("followers")
    private void unpackFollowers(Map<String, Object> followersObj) {
        if (followersObj != null) {
            Object total = followersObj.get("total");
            if (total instanceof Number n) {
                this.followers = n.longValue();
            }
        }
    }
}*/
