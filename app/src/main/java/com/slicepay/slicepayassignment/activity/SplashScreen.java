package com.slicepay.slicepayassignment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.slicepay.slicepayassignment.R;
import com.slicepay.slicepayassignment.app.ApiUtils;
import com.slicepay.slicepayassignment.app.MyApplication;
import com.slicepay.slicepayassignment.database.DataBaseHandler;
import com.slicepay.slicepayassignment.database.PictureAsyncTask;
import com.slicepay.slicepayassignment.model.Flickr;
import com.slicepay.slicepayassignment.model.Photo;
import com.slicepay.slicepayassignment.restclient.SOSInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {
    private SOSInterface mService;
    private DataBaseHandler db;
    private TextView txtLoadingPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        txtLoadingPercentage = findViewById(R.id.loadingPercentage);
        TextView txtLoading = findViewById(R.id.loading);
        mService = ApiUtils.getSOService();
        db = new DataBaseHandler(this);
        db.deleteRecord();
        //to remove "information bar" above the action bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //to remove the action bar (title bar)
        getSupportActionBar().hide();
        if (MyApplication.isNetworkAvailable(this)) {
            loadAnswers();
        } else {
            txtLoading.setText(getResources().getString(R.string.check_internet_connection));
        }
    }

    private void loadAnswers() {
        mService.getAnswers().enqueue(new Callback<Flickr>() {
            @Override
            public void onResponse(Call<Flickr> call, Response<Flickr> response) {
                txtLoadingPercentage.setText(getResources().getString(R.string.thirty_percentage));
                List<Photo> photo = response.body().getPhotos().getPhoto();
                for (int i = 0; i < photo.size(); i++)
                    db.addPhoto(photo.get(i));
                txtLoadingPercentage.setText(getResources().getString(R.string.sixty_percentage));

                List<Photo> listPhotos = db.getAllContacts();
                for (int j = 0; listPhotos.size() > j; j++) {
                    Photo flickrPhoto = listPhotos.get(j);
                    PictureAsyncTask task = new PictureAsyncTask(db);
                    task.execute(flickrPhoto);
                }
                txtLoadingPercentage.setText(getResources().getString(R.string.hundread_percentage));
                finish();
                Intent a = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(a);
            }

            @Override
            public void onFailure(Call<Flickr> call, Throwable t) {
                Log.e("MainActivity", "error loading from API");
            }
        });
    }
}
