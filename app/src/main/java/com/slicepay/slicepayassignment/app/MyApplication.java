package com.slicepay.slicepayassignment.app;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.slicepay.slicepayassignment.restclient.RestClient;
import com.slicepay.slicepayassignment.restclient.SOSInterface;

import java.io.ByteArrayOutputStream;

/**
 * This is the application class of the app contains static methods.
 */

public class MyApplication extends Application {

    //Base url of the app
    private static final String BASE_URL = "https://api.flickr.com";

    /**
     * This method is used to create SOSinterface
     *
     * @return client
     */
    public static SOSInterface getSOService() {
        return RestClient.getClient(BASE_URL).create(SOSInterface.class);
    }

    /**
     * This method is used to check the internet connection
     *
     * @param context context of the activity
     * @return boolean
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    /**
     * CThis method converts the bitmap into byte array
     *
     * @param bitmap bitmap image
     * @return byte array
     */
    public static byte[] getPictureByteOfArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
