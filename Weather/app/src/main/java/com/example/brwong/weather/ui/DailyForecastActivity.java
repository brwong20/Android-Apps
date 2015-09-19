package com.example.brwong.weather.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brwong.weather.R;
import com.example.brwong.weather.adapters.DailyForecastAdapter;
import com.example.brwong.weather.weather_data.Day;

import org.w3c.dom.Text;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DailyForecastActivity extends ListActivity {

    private Day[] mDays;
    @InjectView(R.id.daily_location_name) TextView mDailyLocationName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST); //Stores array data into Parcelable[] type first
        mDays = Arrays.copyOf(parcelables, parcelables.length, Day[].class);//Converts Parcelable array into a Day array
        DailyForecastAdapter adapter = new DailyForecastAdapter(this, mDays);
        setListAdapter(adapter);//Don't forget to finally ADAPT your data to set as a list

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String dayOfTheWeek = mDays[position].getDayOfTheWeek();
        String conditions = mDays[position].getSummary();
        String maxTemp = mDays[position].getMaxTemperature()+"";

        String message = String.format("On %s, the high will be %s, and it will be %s", dayOfTheWeek, maxTemp, conditions);

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
