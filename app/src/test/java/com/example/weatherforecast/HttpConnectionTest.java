/*
 * Copyright 2016 Martijn Brekhof. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.weatherforecast;

import android.os.Handler;

import com.example.weatherforecast.service.HttpConnection;
import com.example.weatherforecast.utils.TestData;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by martijn on 24/06/16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class HttpConnectionTest {

    @Test
    public void executeTest() throws Exception {
        HttpConnection httpConnection = new HttpConnectionMock();
        URL url = new URL("http://example.com/");
        httpConnection.execute(url, new HttpConnectionMock.HttpConnectionCallback() {
            @Override
            public void onSuccess(String response) {
                assertTrue("Response not equal", response.contentEquals(TestData.getJsonResponse()));
            }

            @Override
            public void onError(HttpConnection.ErrorCode errorCode, String message) {
                fail("Got error "+errorCode.name()+": "+message);
            }
        }, new Handler());
    }

    @Test
    public void getTest() throws Exception {
        HttpConnection httpConnection = new HttpConnectionMock();
        URL url = new URL("http://example.com/");
        httpConnection.get(url, new HttpConnectionMock.HttpConnectionCallback() {
            @Override
            public void onSuccess(String response) {
                assertTrue("Response not equal", response.contentEquals(TestData.getJsonResponse()));
            }

            @Override
            public void onError(HttpConnection.ErrorCode errorCode, String message) {
                fail("Got error "+errorCode.name()+": "+message);
            }
        }, new Handler());
    }

    private class HttpConnectionMock extends HttpConnection {
        @Override
        public OkHttpClient getHttpClient() {
            final Request request = new Request.Builder()
                    .url("http://example.com/")
                    .build();

            final Response response = new Response.Builder()
                    .protocol(Protocol.HTTP_1_1)
                    .request(request)
                    .code(200)
                    .body(ResponseBody.create(MediaType.parse("text/html"),
                                              TestData.getJsonResponse()))
                    .build();

            return new OkHttpClient() {
                @Override
                public Call newCall(Request request) {
                    return new Call(this, request) {
                        @Override public Response execute() throws IOException {
                            return response;
                        }
                    };
                }
            };
        }
    }
}