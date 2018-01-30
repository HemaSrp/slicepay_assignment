package com.slicepay.slicepayassignment.model;

/**
 * This class is used to set and get the response details
 */

public class FlickrResponse {

    //Photos
    private Photos photos;

    /**
     * This class is sued to get the photos
     *
     * @return photos
     */
    public Photos getPhotos() {
        return photos;
    }

    @Override
    public String toString() {
        return "FlickrResponse{" +
                "photos=" + photos +
                '}';
    }
}
