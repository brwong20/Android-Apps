package com.example.brwong.weather.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.brwong.weather.R;
import com.example.brwong.weather.adapters.HourlyForecastAdapter;
import com.example.brwong.weather.weather_data.Hour;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HourlyForecastActivity extends ActionBarActivity {

    private Hour[] mHours;

    @InjectView(R.id.recycler_view) RecyclerView mRecyclerView;//We need this because we're going to use this w/ the adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forecast);
        ButterKnife.inject(this);
        Intent intent = getIntent();

        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.HOURLY_FORECAST);
        mHours = Arrays.copyOf(parcelables, parcelables.length, Hour[].class);

        HourlyForecastAdapter hourAdapter = new HourlyForecastAdapter(this, mHours);//Adapting parceled data
        mRecyclerView.setAdapter(hourAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);//This sets up a LinearLayoutManager which manages our list of linear items
                                                                                //Helps optimize how the layout is setup by determining what can be used and reused
        mRecyclerView.setLayoutManager(layoutManager);//Sets our manager in the ReyclerView

        mRecyclerView.setHasFixedSize(true);//Further optimizes the layout b/c we are telling the manager we have a fixed size (24 hrs are never going to change)

    }


}
