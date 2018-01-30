package com.slicepay.slicepayassignment.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.slicepay.slicepayassignment.app.MyApplication;
import com.slicepay.slicepayassignment.model.Photo;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by hema on 28/1/18.
 */

public class PictureAsyncTask extends AsyncTask<Photo, Void, Void> {
    DataBaseHandler db;

    public PictureAsyncTask(DataBaseHandler db) {
        this.db = db;
    }

    // can use UI thread here
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    // automatically done on worker thread (separate from UI thread)
    @Override
    protected Void doInBackground(Photo... params) {
        Photo dataModel = params[0];
        try {
            Log.e("length", dataModel.getPhotoURL() + "");
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
