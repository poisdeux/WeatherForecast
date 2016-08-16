package com.example.weatherforecast;

import android.os.Handler;
import android.os.Process;
import android.util.Log;

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
    private static OkHttpClient httpClient;
    private ExecutorService executorService;

    public interface HttpConnectionCallback {
        void onSuccess(String response);
        void onError(ErrorCode errorCode, String message);
    }

    public static enum ErrorCode {
        IO_EXCEPTION,
        HTTP_RESPONSE_CODE_UNKNOWN
    }

    public HttpConnection() {
        executorService = Executors.newSingleThreadExecutor();
    }

    public OkHttpClient getHttpClient() {
        if ( httpClient == null ) {
            httpClient = new OkHttpClient();
        }
        return httpClient;
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

    public void get(URL url, final HttpConnectionCallback callback, final Handler handler) {

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            final Response response = getHttpClient().newCall(request).execute();
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
                    callback.onError(ErrorCode.IO_EXCEPTION, e.getMessage());
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
                    throw new HttpException(ErrorCode.HTTP_RESPONSE_CODE_UNKNOWN,
                                           "Server response: " + response);
            }
        } catch (IOException e) {
            throw new HttpException(ErrorCode.IO_EXCEPTION, e.getMessage());
        }
    }

    public class HttpException extends Exception {
        private ErrorCode errorCode;

        public HttpException(ErrorCode errorCode, String message) {
            super(message);
            this.errorCode = errorCode;
        }

        public ErrorCode getErrorCode() {
            return errorCode;
        }
    }
}
