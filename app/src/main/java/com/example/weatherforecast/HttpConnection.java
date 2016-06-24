package com.example.weatherforecast;

import android.os.Handler;
import android.os.Process;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by martijn on 24/06/16.
 */
public class HttpConnection {
    private OkHttpClient httpClient;
    private ExecutorService executorService;

    public interface HttpConnectionCallback {
        void onSuccess(String response);
        void onError(int errorCode, String message);
    }

    public HttpConnection() {
        httpClient = new OkHttpClient();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void execute(final URL url, final HttpConnectionCallback callback,
                        final Handler handler) {

        Runnable command = new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                get(url, callback, handler);
            }
        };

        executorService.execute(command);
    }

    private void get(URL url, final HttpConnectionCallback callback, final Handler handler) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            final Response response = httpClient.newCall(request).execute();
            final String result = handleResponse(response);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onSuccess(result);
                }
            });
        } catch (final IOException e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onError(HttpException.IO_EXCEPTION, e.getMessage());
                }
            });
        } catch (final HttpException e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onError(e.errorCode, e.getMessage());
                }
            });
        }
    }

    private String handleResponse(Response response) throws HttpException {
        try {
            switch (response.code()) {
                case 200:
                    return response.body().string();
                default:
                    throw new HttpException(HttpException.HTTP_RESPONSE_CODE_UNKNOWN,
                                           "Server response: " + response);
            }
        } catch (IOException e) {
            throw new HttpException(HttpException.IO_EXCEPTION, e.getMessage());
        }
    }

    private class HttpException extends Exception {
        public static final int IO_EXCEPTION = 0;
        public static final int HTTP_RESPONSE_CODE_UNKNOWN = 1;
        private int errorCode;

        public HttpException(int errorCode, String message) {
            super(message);
            this.errorCode = errorCode;
        }
    }
}
