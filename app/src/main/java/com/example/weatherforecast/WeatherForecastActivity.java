package com.example.weatherforecast;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherforecast.customviews.SwipeRefreshLayoutFix;
import com.example.weatherforecast.database.WeatherForecastDatabase;
import com.example.weatherforecast.service.WeatherForecastService;
import com.example.weatherforecast.service.WeatherForecastServiceEvent;
import com.example.weatherforecast.utils.NetUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherForecastActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.forecast_listview) ListView listView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayoutFix swipeRefreshLayout;
    @BindView(R.id.emptyview) TextView emptyView;

    private String forecastUrl = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22amsterdam%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

    private WeatherForecastDatabase weatherForecastDatabase;
    private SQLiteDatabase database;
    private WeatherForecastAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        ButterKnife.bind(this);

        listView.setEmptyView(emptyView);

        swipeRefreshLayout.setOnRefreshListener(this);

        weatherForecastDatabase = new WeatherForecastDatabase(this);

        database = weatherForecastDatabase.getReadableDatabase();

        Cursor c = weatherForecastDatabase.getForecast(database, 0);
        adapter = new WeatherForecastAdapter(this, c);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Cursor c = weatherForecastDatabase.getForecast(database, 0);
        adapter.changeCursor(c);

        EventBus.getDefault().register(this);

        Intent intent = new Intent(this, WeatherForecastService.class);
        intent.putExtra(WeatherForecastService.BUNDLE_KEY_FORECAST_URL, forecastUrl);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh() {
        if (! NetUtils.isConnected(this)) {
            Toast.makeText(this, "Enable your internet connection to retrieve the forecast", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
            return;
        }

        Intent intent = new Intent(this, WeatherForecastService.class);
        intent.putExtra(WeatherForecastService.BUNDLE_KEY_FORECAST_URL, forecastUrl);
        startService(intent);

        swipeRefreshLayout.setRefreshing(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBusEvent(WeatherForecastServiceEvent event) {
        switch (event.getEvent()) {
            case SYNC_COMPLETE:
                Cursor c = weatherForecastDatabase.getForecast(database, 0);
                adapter.changeCursor(c);
                break;
            case HTTP_ERROR:
            case SQL_ERROR:
                Toast.makeText(this, event.getMessage(), Toast.LENGTH_LONG).show();
                break;
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Added for testing purposes
     * @param forecastUrl
     */
    public void setForecastUrl(String forecastUrl) {
        this.forecastUrl = forecastUrl;
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WeatherForecastService weatherForecastService = ((WeatherForecastService.LocalBinder) service).getService();
            if( weatherForecastService.isSyncing() )
                swipeRefreshLayout.setRefreshing(true);
            unbindService(this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
