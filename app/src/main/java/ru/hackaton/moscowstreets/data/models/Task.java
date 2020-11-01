package ru.hackaton.moscowstreets.data.models;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class Task {
    private int id;
    private String name;
    private int category;
    private Status status;
    private String commentary;
    private int rating = 0;
    private List<Uri> photoUris = new ArrayList<>();

    public Task(int id, String name, int category, Status status) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.status = status;
    }

    public Task(int id, String name, int category, Status status, String commentary, int rating) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.status = status;
        this.commentary = commentary;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getCategory() {
        return category;
    }

    public Status getStatus() {
        return status;
    }

    public String getCommentary() {
        return commentary;
    }

    public int getRating() {
        return rating;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void addPhotoUri(Uri uri) {
        this.photoUris.add(uri);
    }

    public List<Uri> getPhotoUris() {
        return photoUris;
    }

    public enum Status {
        IN_PROGRESS("выполняется"),
        DONE("выполнен");

        public final String description;

        Status(String description) {
            this.description = description;
        }
    }
}
