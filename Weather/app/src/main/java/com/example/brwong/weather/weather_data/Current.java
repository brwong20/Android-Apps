package com.example.brwong.weather.weather_data;

import com.example.brwong.weather.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Brwong on 5/8/15.
 */
public class Current {
    private String mIcon;
    private Long mTime;
    private Double mTemperature;
    private Double mHumidity;
    private Double mPrecipChance;
    private String mSummary;
    private String mTimeZone;
    private int mWindSpeed;
    private String mLocation;

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public void setWindSpeed(int windSpeed) {
        mWindSpeed = windSpeed;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public String getIcon() {
        return mIcon;
    }

    public int getIconId(){
        return Forecast.getIconId(mIcon); //Since we refactored this code in Forecast, we can just pass in this class' property, mIcon to give the same
                                            //results with the function we had before(now implemented in the overall Forecast class)
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public Long getTime() {
        return mTime;
    }

    public String getFormattedTime(){//Converts UNIX date to just the time
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");//"h:mm a" is the time format we want the formatter to output
        Date dateTime = new Date(mTime * 1000);//This converts our date/time in SECONDS TO MILLISECONDS
        formatter.setTimeZone(TimeZone.getTimeZone(mTimeZone));//Can set timezone here to the getter method for timezone or the member variable - both have the same value
        String timeString = formatter.format(dateTime);//We converted to millseconds because this .format() only takes MILLISECONDS as a parameter

        return timeString;
    }

    public void setTime(Long time) {
        mTime = time;
    }

    public int getTemperature() {
        return (int) Math.round(mTemperature);//Rounds the temperature to nearest whole integer
    }

    public void setTemperature(Double temperature) {
        mTemperature = temperature;
    }

    public Double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(Double humidity) {
        mHumidity = humidity;
    }

    public int getPrecipChance() {
        double precipPercentage = mPrecipChance *100;
        return (int) Math.round(precipPercentage);
    }

    public void setPrecipChance(Double precipChance) {
        mPrecipChance = precipChance;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public int getWindSpeed() {
        return mWindSpeed;
    }

    public void setWind(int wind) {
        mWindSpeed = wind;
    }
}
