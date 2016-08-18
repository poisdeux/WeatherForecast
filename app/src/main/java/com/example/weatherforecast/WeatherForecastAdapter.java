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

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.weatherforecast.database.WeatherForecastContract;
import com.example.weatherforecast.utils.DateUtils;

/**
 * Created by martijn on 01/08/16.
 */
public class WeatherForecastAdapter extends CursorAdapter {

    public WeatherForecastAdapter(Context context, Cursor c) {
        super(context, c, false);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.listview_item_forecast,
                                                         parent, false);

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.date = (TextView) view.findViewById(R.id.forecast_date);
        viewHolder.temperatureLow = (TextView) view.findViewById(R.id.temperature_low);
        viewHolder.temperatureHigh = (TextView) view.findViewById(R.id.temperature_high);

        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder)view.getTag();

        int epochIndex =
                cursor.getColumnIndex(WeatherForecastContract.Forecast.COLUMN_NAME_DATE_EPOCH_MILLIS);
        int tempLowIndex =
                cursor.getColumnIndex(WeatherForecastContract.Forecast.COLUMN_NAME_TEMP_LOW);
        int tempHighIndex =
                cursor.getColumnIndex(WeatherForecastContract.Forecast.COLUMN_NAME_TEMP_HIGH);

        viewHolder.date.setText(DateUtils.toDateString(cursor.getLong(epochIndex)));
        viewHolder.temperatureLow.setText(cursor.getString(tempLowIndex));
        viewHolder.temperatureHigh.setText(cursor.getString(tempHighIndex));
    }

    public class ViewHolder {
        TextView date;
        TextView temperatureLow;
        TextView temperatureHigh;
    }
}