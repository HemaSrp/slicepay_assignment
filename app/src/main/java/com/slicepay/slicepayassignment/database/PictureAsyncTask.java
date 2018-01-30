package com.slicepay.slicepayassignment.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.slicepay.slicepayassignment.app.MyApplication;
import com.slicepay.slicepayassignment.model.Photo;

import java.io.InputStream;
import java.net.URL;


/**
 * This class is used to convert the bitmap image into byte array and update in table
 */
public class PictureAsyncTask extends AsyncTask<Photo, Void, Void> {

    //Databasehandler
    private final DataBaseHandler db;

    public PictureAsyncTask(DataBaseHandler db) {
        this.db = db;
    }

    // automatically done on worker thread (separate from UI thread)
    @Override
    protected Void doInBackground(Photo... params) {
        Photo dataModel = params[0];
        try {
            InputStream inputStream = new URL(dataModel.getPhotoURL()).openStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            //set bitmap value to Picture
            dataModel.setPhotoImg(MyApplication.getPictureByteOfArray(bitmap));
            //add data to database (shows in next step)
            db.updateContact(dataModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
