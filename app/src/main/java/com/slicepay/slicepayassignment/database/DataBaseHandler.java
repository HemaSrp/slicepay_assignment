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
 * This class is used to create the database,table,delete,update,insert the record.
 */

public class DataBaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Flickrmanager";

    // Table name
    private static final String PHOTO_DETAILS = "PhotoDetails";

    // Photo id
    private static final String PHOTO_ID = "PhotoId";

    // Owner id
    private static final String OWNER_ID = "OwnerId";

    //Secret id
    private static final String SECRET_ID = "SecretId";

    //Server id
    private static final String SERVER_ID = "ServerId";

    // Farm
    private static final String FARM = "Farm";

    // Title
    private static final String TITLE = "Title";

    // Photo
    private static final String PHOTO = "Photo";

    //Photo url
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

    /**
     * This method is used to insert the photo in table.
     *
     * @param photo object
     */

    public void insertPhoto(Photo photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OWNER_ID, photo.getOwner());
        values.put(PHOTO_ID, photo.getId());
        values.put(SERVER_ID, photo.getServer());
        values.put(SECRET_ID, photo.getSecret());
        values.put(PHOTO, "");
        values.put(FARM, photo.getFarm());
        values.put(TITLE, photo.getTitle());
        values.put(PHOTO_URL, "https://farm" + photo.getFarm() + ".staticflickr.com/" + photo.getServer() + "/" + photo.getId() + "_" + photo.getSecret() + "_t.jpg");
        db.insert(PHOTO_DETAILS, null, values);
        db.close(); // Closing database connection
    }

    /**
     * This method is used to get all photos from the table
     *
     * @return
     */
    public List<Photo> getAllDetails() {
        List<Photo> photosList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + PHOTO_DETAILS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Photo photo = new Photo();
                photo.setId(cursor.getString(0));
                photo.setOwner(cursor.getString(1));
                photo.setSecret(cursor.getString(2));
                photo.setServer(cursor.getString(3));
                photo.setFarm(cursor.getString(4));
                photo.setTitle(cursor.getString(5));
                photo.setPhotoURL(cursor.getString(6));
                photo.setPhotoImg(cursor.getBlob(7));
                photosList.add(photo);
            } while (cursor.moveToNext());
        }

        // return photo list
        return photosList;
    }

    /**
     * This method is used to update the bitmap image in photo id
     *
     * @param photo
     * @return 1
     */
    public int updateContact(Photo photo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PHOTO, photo.getPhotoImg());

        // updating row
        return db.update(PHOTO_DETAILS, values, PHOTO_ID + " = ?",
                new String[]{String.valueOf(photo.getId())});
    }

    /**
     * This method is used to fetch the photos from the table
     *
     * @param listener
     */
    public void fetchPhotos(PhotoFetchListner listener) {
        PhotoFetcher fetcher = new PhotoFetcher(listener, this.getWritableDatabase());
        fetcher.start();
    }

    /**
     * This method is used to delete the record from db
     */
    public void deleteRecord() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PHOTO_DETAILS, null, null);
    }

    /**
     * This method is used to fetch the details from the table
     */
    public class PhotoFetcher extends Thread {

        final String selectQuery = "SELECT  * FROM " + PHOTO_DETAILS ;
        private final PhotoFetchListner mListener;
        private final SQLiteDatabase mDb;


        public PhotoFetcher(PhotoFetchListner listener, SQLiteDatabase db) {
            mListener = listener;
            mDb = db;
        }

        @Override
        public void run() {
            Cursor cursor = mDb.rawQuery(selectQuery, null);

            final List<Photo> photoList = new ArrayList<>();

            if (cursor.getCount() > 0) {

                if (cursor.moveToFirst()) {
                    do {
                        Photo photo = new Photo();
                        photo.setId(cursor.getString(0));
                        photo.setOwner(cursor.getString(1));
                        photo.setSecret(cursor.getString(2));
                        photo.setServer(cursor.getString(3));
                        photo.setFarm(cursor.getString(4));
                        photo.setTitle(cursor.getString(5));
                        photo.setPhotoURL(cursor.getString(6));
                        photo.setPhotoImg(cursor.getBlob(7));
                        photoList.add(photo);

                    } while (cursor.moveToNext());
                }
            }

            //To interact with UI thread
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onDeliverAllPhotos(photoList);
                    mListener.onHideDialog();
                }
            });
        }
    }
}
