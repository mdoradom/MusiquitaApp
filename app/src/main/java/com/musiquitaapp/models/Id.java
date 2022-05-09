package com.musiquitaapp.models;

public class Id {
    String kind;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public Id(String kind, String videoId) {
        this.kind = kind;
        this.videoId = videoId;
    }

    public Id() {
    }

    public String videoId;
}
