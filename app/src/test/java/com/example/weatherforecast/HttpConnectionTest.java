package com.example.weatherforecast;

import android.os.Handler;

import org.junit.Before;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertEquals;

/**
 * Created by martijn on 24/06/16.
 */
public class HttpConnectionTest {

    @Test
    public void yahooQueryTest() throws Exception {
        HttpConnection httpConnection = new HttpConnection();
        URL url = new URL("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20%28select%20woeid%20from%20geo.places%281%29%20where%20text%3D%22amsterdam%22%29&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys");
        httpConnection.execute(url, new HttpConnection.HttpConnectionCallback() {
            @Override
            public void onSuccess(String response) {

            }

            @Override
            public void onError(int errorCode, String message) {

            }
        }, new Handler());
    }
}