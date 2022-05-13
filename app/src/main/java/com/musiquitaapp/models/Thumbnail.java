package com.musiquitaapp.models;

public class Thumbnail {

    private Medium medium;
    private High high;

    public Thumbnail(Medium medium, High high) {
        this.medium = medium;
        this.high = high;
    }

    public Thumbnail() {
    }

    public Medium getMedium() {
        return medium;
    }

    public void setMedium(Medium medium) {
        this.medium = medium;
    }

    public High getHigh() {
        return high;
    }

    public void setHigh(High high) {
        this.high = high;
    }

}
