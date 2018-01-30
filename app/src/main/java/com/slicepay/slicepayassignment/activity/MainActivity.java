package com.slicepay.slicepayassignment.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.slicepay.slicepayassignment.R;
import com.slicepay.slicepayassignment.adapter.FlickrAdapter;
import com.slicepay.slicepayassignment.app.SeperatorDecoration;
import com.slicepay.slicepayassignment.database.DataBaseHandler;
import com.slicepay.slicepayassignment.database.PhotoFetchListner;
import com.slicepay.slicepayassignment.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to display the flickr live photos from database and set it in recycler view.
 */

public class MainActivity extends AppCompatActivity implements PhotoFetchListner {

    //Database handler
    private DataBaseHandler db;

    //Recyclerview
    private RecyclerView recyclerView;

    //Recyclerview adapter
    private FlickrAdapter mAdapter;

    //Searchview
    private SearchView searchView;

    //Photos list
    private List<Photo> allPhotos;

    //GridLayout manager
    private GridLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        db = new DataBaseHandler(this);
        if (savedInstanceState != null) {
            allPhotos = savedInstanceState.getParcelableArrayList("photosList");
            setRecyclerView(allPhotos);
        } else {
            getFeedFromDatabase();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("photosList", (ArrayList<? extends Parcelable>) allPhotos);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        allPhotos = savedInstanceState.getParcelableArrayList("photosList");
    }
    @Override
    public void onDeliverAllPhotos(List<Photo> photos) {
        allPhotos = photos;
        setRecyclerView(allPhotos);
    }

    /**
     * This method is used to display the data from the table in adapter
     *
     * @param photos photoslist
     */
    private void setRecyclerView(List<Photo> photos) {
        mAdapter = new FlickrAdapter(this, photos);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        } else {
            mLayoutManager = new GridLayoutManager(getApplicationContext(),3);
        }
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SeperatorDecoration(this, ContextCompat.getColor(this, R.color.white), 10.0f));
        recyclerView.setAdapter(mAdapter);
    }



    @Override
    public void onHideDialog() {
        //Hide dialog
    }

    /**
     * THis method is used to fetch the photos
     */
    private void getFeedFromDatabase() {
        db.fetchPhotos(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

}
