package com.slicepay.slicepayassignment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.slicepay.slicepayassignment.R;
import com.slicepay.slicepayassignment.app.MyApplication;
import com.slicepay.slicepayassignment.database.DataBaseHandler;
import com.slicepay.slicepayassignment.database.PictureAsyncTask;
import com.slicepay.slicepayassignment.model.FlickrResponse;
import com.slicepay.slicepayassignment.model.Photo;
import com.slicepay.slicepayassignment.restclient.SOSInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class is splash screen of the app and it load the data from API and set into database
 */
public class SplashScreen extends AppCompatActivity {
    //Rest client interface class
    private SOSInterface mService;

    //Database handler for SQLite open helper
    private DataBaseHandler db;

    //Loading percentage of the API
    private TextView txtLoadingPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        txtLoadingPercentage = findViewById(R.id.loadingPercentage);
        TextView txtLoading = findViewById(R.id.loading);
        mService = MyApplication.getSOService();
        db = new DataBaseHandler(this);
        db.deleteRecord();
        //to remove "information bar" above the action bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //to remove the action bar (title bar)
        getSupportActionBar().hide();
        if (MyApplication.isNetworkAvailable(this)) {
            loadFlickrApi();
        } else {
            txtLoading.setText(getResources().getString(R.string.check_internet_connection));
        }
    }

    /**
     * This method  is used to get the request and response from the api and stored in database
     */
    private void loadFlickrApi() {
        mService.getPhotos().enqueue(new Callback<FlickrResponse>() {
            @Override
            public void onResponse(Call<FlickrResponse> call, Response<FlickrResponse> response) {
                txtLoadingPercentage.setText(getResources().getString(R.string.thirty_percentage));
                List<Photo> photo = response.body().getPhotos().getPhoto();
                if (!photo.isEmpty()) {
                    for (int i = 0; i < photo.size(); i++)
                        db.insertPhoto(photo.get(i));
                    txtLoadingPercentage.setText(getResources().getString(R.string.sixty_percentage));

                    List<Photo> listPhotos = db.getAllDetails();
                    for (int j = 0; listPhotos.size() > j; j++) {
                        Photo flickrPhoto = listPhotos.get(j);
                        PictureAsyncTask task = new PictureAsyncTask(db);
                        task.execute(flickrPhoto);
                    }
                    txtLoadingPercentage.setText(getResources().getString(R.string.hundread_percentage));
                    finish();
                    Intent a = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(a);
                } else {
                    Toast.makeText(SplashScreen.this, "Error loading from API", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<FlickrResponse> call, Throwable t) {
                Toast.makeText(SplashScreen.this, "Error loading from API", Toast.LENGTH_LONG).show();
            }
        });
    }
}
