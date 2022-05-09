package com.musiquitaapp.models;

import java.io.Serializable;

public class YouTubeVideo implements Serializable {
    private String id;
    private String title;
    private String thumbnailURL;
    private String duration;
    private String viewCount;

    public YouTubeVideo() {
        this.id = "";
        this.title = "";
        this.thumbnailURL = "";
        this.duration = "";
        this.viewCount = "";
    }

    public YouTubeVideo(YouTubeVideo newVideo) {
        this.id = newVideo.id;
        this.title = newVideo.title;
        this.thumbnailURL = newVideo.thumbnailURL;
        this.duration = newVideo.duration;
        this.viewCount = newVideo.viewCount;
    }

    public YouTubeVideo(String id, String title, String thumbnailURL, String duration, String viewCount) {
        this.id = id;
        this.title = title;
        this.thumbnailURL = thumbnailURL;
        this.duration = duration;
        this.viewCount = viewCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnail) {
        this.thumbnailURL = thumbnail;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    @Override
    public String toString() {
        return "YouTubeVideo {" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
