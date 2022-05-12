package com.musiquitaapp.models;

public class RelatedPlaylists {
    String likes;
    String favorites;
    String uploads;
    String watchHistory;
    String watchLater;

    public RelatedPlaylists(String likes, String favorites, String uploads, String watchHistory, String watchLater) {
        this.likes = likes;
        this.favorites = favorites;
        this.uploads = uploads;
        this.watchHistory = watchHistory;
        this.watchLater = watchLater;
    }
}
