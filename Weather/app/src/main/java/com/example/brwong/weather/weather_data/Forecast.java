package com.example.brwong.weather.weather_data;

import com.example.brwong.weather.R;
import com.example.brwong.weather.weather_data.Current;
import com.example.brwong.weather.weather_data.Day;
import com.example.brwong.weather.weather_data.Hour;

/**
 * Created by Brwong on 5/14/15.
 */
public class Forecast {

    private Current mCurrent;
    private Hour[] mHour;
    private Day[] mDay;

    public Current getCurrent() {
        return mCurrent;
    }

    public void setCurrent(Current current) {
        mCurrent = current;
    }

    public Hour[] getHour() {
        return mHour;
    }

    public void setHour(Hour[] hour) {
        mHour = hour;
    }

    public Day[] getDay() {
        return mDay;
    }

    public void setDay(Day[] day) {
        mDay = day;
    }

    public static int getIconId(String iconString){//We're making this static so all our other forecast related classes can use it
        {
            //This method is to return an int mipmap(icon) based on the iconID's value from the retrieved data
            int iconId = R.mipmap.clear_day;//Sets a default icon in case new icons are added or something goes wrong

            if (iconString.equals("clear-day")) {//"If iconString equals this value, set the appropriate icon"
                iconId = R.mipmap.clear_day;
            }
            else if (iconString.equals("clear-night")) {
                iconId = R.mipmap.clear_night;
            }
            else if (iconString.equals("rain")) {
                iconId = R.mipmap.rain;
            }
            else if (iconString.equals("snow")) {
                iconId = R.mipmap.snow;
            }
            else if (iconString.equals("sleet")) {
                iconId = R.mipmap.sleet;
            }
            else if (iconString.equals("wind")) {
                iconId = R.mipmap.wind;
            }
            else if (iconString.equals("fog")) {
                iconId = R.mipmap.fog;
            }
            else if (iconString.equals("cloudy")) {
                iconId = R.mipmap.cloudy;
            }
            else if (iconString.equals("partly-cloudy-day")) {
                iconId = R.mipmap.partly_cloudy;
            }
            else if (iconString.equals("partly-cloudy-night")) {
                iconId = R.mipmap.cloudy_night;
            }

            return iconId;
        }

    }

}
