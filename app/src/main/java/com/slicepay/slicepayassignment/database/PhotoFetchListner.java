package com.slicepay.slicepayassignment.database;

import com.slicepay.slicepayassignment.model.Photo;

import java.util.List;

/**
 * This interface is used interact with background thread to UI thread.
 */
public interface PhotoFetchListner {

    void onDeliverAllPhotos(List<Photo> flowers);

    void onHideDialog();
}

