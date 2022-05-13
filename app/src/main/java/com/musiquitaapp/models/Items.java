package com.musiquitaapp.models;

public class Items {
    private String kind;
    private Id id;
    private Snippet snippet;

    public Items(String kind, Id id, Snippet snippet) {
        this.kind = kind;
        this.id = id;
        this.snippet = snippet;
    }

    public Items() {
    }

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

}
