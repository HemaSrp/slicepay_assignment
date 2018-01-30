package com.slicepay.slicepayassignment.model;

/**
 * Created by hema on 27/1/18.
 */

public class Flickr {

    private Photos photos;

    public Photos getPhotos() {
        return photos;
    }

    @Override
    public String toString() {
        return "Flickr{" +
                "photos=" + photos +
                '}';
    }
}
