package com.musiquitaapp.models;

public class Playlist {
    String id;
    Snippet snippet;
    PlaylistType playlistType;
    int lenght;

    public Playlist(String id, Snippet snippet, PlaylistType playlistType, int lenght) {
        this.id = id;
        this.snippet = snippet;
        this.playlistType = playlistType;
        this.lenght = lenght;
    }
}
