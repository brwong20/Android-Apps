package com.example.brwong.weather.weather_data;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Brwong on 5/14/15.
 */
public class Hour implements Parcelable{
    private String mIcon;
    private Long mTime;
    private Double mTemperature;
    private String mSummary;
    private String mTimezone;

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public int getIconId(){
        return Forecast.getIconId(mIcon);
    }

    public Long getTime() {
        return mTime;
    }

    public void setTime(Long time) {
        mTime = time;
    }

    public int getTemperature() {
        return (int) Math.round(mTemperature);
    }

    public void setTemperature(Double temperature) {
        mTemperature = temperature;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }

    public String getHourOfDay(){
        SimpleDateFormat formatter = new SimpleDateFormat("h a");//Instantiating a new format (gives us the whole day name)
        formatter.setTimeZone(TimeZone.getTimeZone(mTimezone));//Finds the timezone
        Date dateTime = new Date(mTime *1000);//Sets the day name based on milliseconds
        return formatter.format(dateTime);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mIcon);
        dest.writeLong(mTime);
        dest.writeDouble(mTemperature);
        dest.writeString(mSummary);
        dest.writeString(mTimezone);
    }

    private Hour(Parcel in){
        mIcon = in.readString();
        mTime= in.readLong();
        mTemperature = in.readDouble();
        mSummary = in.readString();
        mTimezone = in.readString();
    }

    public static final Creator<Hour> CREATOR = new Creator<Hour>() {
        @Override
        public Hour createFromParcel(Parcel source) {
            return new Hour(source);
        }

        @Override
        public Hour[] newArray(int size) {
            return new Hour[size];
        }
    };

    public Hour() { }
}
