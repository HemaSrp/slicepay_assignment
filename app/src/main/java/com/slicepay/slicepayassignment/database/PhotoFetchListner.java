package com.slicepay.slicepayassignment.database;

import com.slicepay.slicepayassignment.model.Photo;

import java.util.List;

/**
 * Created by hema on 28/1/18.
 */

public interface PhotoFetchListner {

    void onDeliverAllPhotos(List<Photo> flowers);

    void onDeliverPhoto(Photo flower);

    void onHideDialog();
}

