package com.slicepay.slicepayassignment.app;

import com.slicepay.slicepayassignment.restclient.RestClient;
import com.slicepay.slicepayassignment.restclient.SOSInterface;

/**
 * Created by hema on 27/1/18.
 */

public class ApiUtils {

    public static final String BASE_URL = "https://api.flickr.com";

    public static SOSInterface getSOService() {
        return RestClient.getClient(BASE_URL).create(SOSInterface.class);
    }
}
