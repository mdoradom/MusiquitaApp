package com.musiquitaapp.models;

public class Status {
    String privacyStatus;
    boolean isLinked;

    public Status(String privacyStatus, boolean isLinked) {
        this.privacyStatus = privacyStatus;
        this.isLinked = isLinked;
    }
}
