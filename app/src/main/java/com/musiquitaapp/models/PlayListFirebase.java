package com.musiquitaapp.models;

import java.util.List;
import java.util.UUID;

public class PlayListFirebase {
    public String authorName;
    public String authorID;
    public String playlistID;
    public String playlistDescrip;
    public String playlistTitle;
    public String thumbnail;
    public List<YouTubeVideo> songs;

    public PlayListFirebase() {
    }

    public PlayListFirebase(String authorName, String authorID, String playlistDescrip, String playlistTitle, String thumbnail, List<YouTubeVideo> songs) {
        this.authorName = authorName;
        this.authorID = authorID;
        this.playlistID = UUID.randomUUID().toString();
        this.playlistDescrip = playlistDescrip;
        this.playlistTitle = playlistTitle;
        this.thumbnail = thumbnail;
        this.songs = songs;
    }

    @Override
    public String toString() {
        return "PlayListFirebase{" +
                "authorName='" + authorName + '\'' +
                ", authorID='" + authorID + '\'' +
                ", playlistID='" + playlistID + '\'' +
                ", playlistDescrip='" + playlistDescrip + '\'' +
                ", playlistTitle='" + playlistTitle + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", songs=" + songs +
                '}';
    }
}
