package com.musiquitaapp.models;

public class Items {
    String kind;
    Id id;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public Snippet getSnippet() {
        return snippet;
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
    }

    public Items(String kind, Id id, Snippet snippet) {
        this.kind = kind;
        this.id = id;
        this.snippet = snippet;
    }

    public Items() {
    }

    Snippet snippet;
}
