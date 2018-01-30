package com.slicepay.slicepayassignment.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Looper;

import com.slicepay.slicepayassignment.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hema on 27/1/18.
 */

public class DataBaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Flickrmanager";

    // Flickr table name
    private static final String PHOTO_DETAILS = "PhotoDetails";

    // Flickr table name
    private static final String PHOTO_ID = "PhotoId";
    // Flickr table name
    private static final String OWNER_ID = "OwnerId";
    // Flickr table name
    private static final String SECRET_ID = "SecretId";
    // Flickr table name
    private static final String SERVER_ID = "ServerId";
    // Flickr table name
    private static final String FARM = "Farm";
    // Flickr table name
    private static final String TITLE = "Title";
    // Flickr table name
    private static final String PHOTO = "Photo";

    private static final String PHOTO_URL = "photo_url";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_TABLE_QUERY = "CREATE TABLE " + PHOTO_DETAILS + "" +
                "(" +
                PHOTO_ID + " TEXT not null,"
                + OWNER_ID + " TEXT not null,"
                + SECRET_ID + " TEXT not null," +
                SERVER_ID + " TEXT not null," +
                FARM + " TEXT not null," +
                TITLE + " TEXT ," +
                PHOTO_URL + " TEXT ," +
                PHOTO + " blob not null)";
        sqLiteDatabase.execSQL(CREATE_TABLE_QUERY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PHOTO_DETAILS);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public void addPhoto(Photo photo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OWNER_ID, photo.getOwner());
        values.put(PHOTO_ID, photo.getId()); // Contact Name
        values.put(SERVER_ID, photo.getServer()); // Contact Phone Number
        values.put(SECRET_ID, photo.getSecret()); // Contact Phone Number
        values.put(PHOTO, "");
        values.put(FARM, photo.getFarm());
        values.put(TITLE, photo.getTitle());
        values.put(PHOTO_URL, "https://farm" + photo.getFarm() + ".staticflickr.com/" + photo.getServer() + "/" + photo.getId() + "_" + photo.getSecret() + "_t.jpg");
        db.insert(PHOTO_DETAILS, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Contacts
    public List<Photo> getAllContacts() {
        List<Photo> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + PHOTO_DETAILS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Photo contact = new Photo();
                contact.setId(cursor.getString(0));
                contact.setOwner(cursor.getString(1));
                contact.setSecret(cursor.getString(2));
                contact.setServer(cursor.getString(3));
                contact.setFarm(cursor.getString(4));
                contact.setTitle(cursor.getString(5));
                contact.setPhotoURL(cursor.getString(6));
                contact.setPhotoImg(cursor.getBlob(7));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public int updateContact(Photo contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PHOTO, contact.getPhotoImg());

        // updating row
        return db.update(PHOTO_DETAILS, values, PHOTO_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});
    }


    public void fetchPhotos(PhotoFetchListner listener) {
        FlowerFetcher fetcher = new FlowerFetcher(listener, this.getWritableDatabase());
        fetcher.start();
    }

    public void deleteRecord() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PHOTO_DETAILS, null, null);
    }

    public class FlowerFetcher extends Thread {

        final String selectQuery = "SELECT  * FROM " + PHOTO_DETAILS + " ORDER BY TITLE";
        private final PhotoFetchListner mListener;
        private final SQLiteDatabase mDb;


        public FlowerFetcher(PhotoFetchListner listener, SQLiteDatabase db) {
            mListener = listener;
            mDb = db;
        }

        @Override
        public void run() {
            Cursor cursor = mDb.rawQuery(selectQuery, null);

            final List<Photo> flowerList = new ArrayList<>();

            if (cursor.getCount() > 0) {

                if (cursor.moveToFirst()) {
                    do {
                        Photo contact = new Photo();
                        contact.setId(cursor.getString(0));
                        contact.setOwner(cursor.getString(1));
                        contact.setSecret(cursor.getString(2));
                        contact.setServer(cursor.getString(3));
                        contact.setFarm(cursor.getString(4));
                        contact.setTitle(cursor.getString(5));
                        contact.setPhotoURL(cursor.getString(6));
                        contact.setPhotoImg(cursor.getBlob(7));
                        flowerList.add(contact);

                    } while (cursor.moveToNext());
                }
            }
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onDeliverAllPhotos(flowerList);
                    mListener.onHideDialog();
                }
            });
        }
    }
}
