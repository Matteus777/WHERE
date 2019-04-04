package com.example.whereapplication.Object;

import android.media.Image;

import java.util.Date;
import java.util.UUID;

public class Event {
    private String local;
    private String id;
    private Price[] price;
    private String title;
    private Image photo;
    private String date;
    private String desc;

    public Price[] getPrice() {
        return price;
    }

    public void setPrice(Price[] price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private boolean tagopen;
    private boolean tagfree;
    private boolean tagshow;

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    private boolean tagfesta;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
