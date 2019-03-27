package com.example.whereapplication.Object;

import android.media.Image;

import java.util.Date;
import java.util.UUID;

public class Event {
    private String id;
    private double price;
    private String title;
    private Image photo;
    private Date date;
    private boolean tagopen;
    private boolean tagfree;
    private boolean tagshow;
    private boolean tagfesta;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Image getPhoto() {
        return photo;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isTagopen() {
        return tagopen;
    }

    public void setTagopen(boolean tagopen) {
        this.tagopen = tagopen;
    }

    public boolean isTagfree() {
        return tagfree;
    }

    public void setTagfree(boolean tagfree) {
        this.tagfree = tagfree;
    }

    public boolean isTagshow() {
        return tagshow;
    }

    public void setTagshow(boolean tagshow) {
        this.tagshow = tagshow;
    }

    public boolean isTagfesta() {
        return tagfesta;
    }

    public void setTagfesta(boolean tagfesta) {
        this.tagfesta = tagfesta;
    }
}
