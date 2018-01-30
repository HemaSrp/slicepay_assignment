package com.slicepay.slicepayassignment.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class is used to set and get the response details
 */

public class Photo implements Parcelable {

    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
    private String isfamily;
    private String farm;
    private String id;
    private String title;
    private String ispublic;
    private String owner;
    private String secret;
    private String server;
    private String isfriend;
    private String photoURL;
    private byte[] photoImg;

    public Photo() {

    }

    private Photo(Parcel in) {
        //retrieve
        this.id = in.readString();
        this.title = in.readString();
        this.ispublic = in.readString();
        this.owner = in.readString();
        this.secret = in.readString();
        this.server = in.readString();
        this.isfriend = in.readString();
        this.photoURL = in.readString();
        this.photoImg = new byte[in.readInt()];
        in.readByteArray(photoImg);
    }


    public String getFarm() {
        return farm;
    }

    public void setFarm(String farm) {
        this.farm = farm;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }


    @Override
    public String toString() {
        return "ClassPojo [isfamily = " + isfamily + ", farm = " + farm + ", id = " + id + ", title = " + title + ", ispublic = " + ispublic + ", owner = " + owner + ", secret = " + secret + ", server = " + server + ", isfriend = " + isfriend + "]";
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public byte[] getPhotoImg() {
        return photoImg;
    }

    public void setPhotoImg(byte[] photoImg) {
        this.photoImg = photoImg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //write
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.ispublic);
        dest.writeString(this.owner);
        dest.writeString(this.secret);
        dest.writeString(this.server);
        dest.writeString(this.isfriend);
        dest.writeString(this.photoURL);
        dest.writeInt(photoImg.length);
        dest.writeByteArray(photoImg);
    }

}
