package com.musiquitaapp.models;

import com.musiquitaapp.models.YouTubeVideo;

import java.util.List;

public class MySession {
    public String sessionName;
    public String ownerName;
    public int hour;
    public int minute;
    public int second;
    public boolean isPlaying;
    public boolean recentlyJoined;
    public int currentSongIndex;
    public YouTubeVideo currentSong;
    public List<YouTubeVideo> songsInQueue;
    public int connectedUsers;
    public List<Boolean> isUserReady;


    public MySession() {
    }

    public MySession(String sessionName, String ownerName, int hour, int minute, int second,
                     boolean isPlaying, boolean recentlyJoined, int currentSongIndex, YouTubeVideo currentSong,
                     List<YouTubeVideo> songsInQueue, int connectedUsers)
    {
        this.sessionName = sessionName;
        this.ownerName = ownerName;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.isPlaying = isPlaying;
        this.recentlyJoined = recentlyJoined;
        this.currentSongIndex = currentSongIndex;
        this.currentSong = currentSong;
        this.songsInQueue = songsInQueue;
        this.connectedUsers = connectedUsers;
    }
}