package com.musiquitaapp.models;

public class Medium {

    private String url;
    private int width;
    private int height;

    public Medium(String url, int width, int height) {
        this.url = url;
        this.width = width;
        this.height = height;
    }

    public Medium() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
