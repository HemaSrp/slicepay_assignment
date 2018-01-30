package com.slicepay.slicepayassignment.restclient;

import com.slicepay.slicepayassignment.model.Flickr;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by hema on 27/1/18.
 */

public interface SOSInterface {

    @GET("/services/rest/?method=flickr.interestingness.getList&api_key=9f89151d82e427401680cd48dd2d5cf5&per_page=30&page=1&format=json&nojsoncallback=1")
    Call<Flickr> getAnswers();
}
