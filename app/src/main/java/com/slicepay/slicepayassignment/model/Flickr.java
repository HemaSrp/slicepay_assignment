package com.slicepay.slicepayassignment.model;

/**
 * Created by hema on 27/1/18.
 */

public class Flickr {

    private Photos photos;

    private String stat;

    public Photos getPhotos() {
        return photos;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    @Override
    public String toString() {
        return "ClassPojo [photos = " + photos + ", stat = " + stat + "]";
    }

}
