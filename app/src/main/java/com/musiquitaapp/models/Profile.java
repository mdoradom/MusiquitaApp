package com.musiquitaapp.models;

public class Profile {
    public String backgroundImage;
    public String description;

    public Profile() {
    }

    public Profile(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public Profile(String backgroundImage, String description) {
        this.backgroundImage = backgroundImage;
        this.description = description;
    }


}
