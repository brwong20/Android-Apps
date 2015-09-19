package com.example.brwong.weather.weather_data;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Brwong on 5/14/15.
 */
public class Day implements Parcelable{
    private long mTime;
    private String mSummary;
    private String mIcon;
    private String mTimezone;
    private double mMaxTemperature;

    public Day() {//We need this DEFAULT constructor to work for the MainActivity's getDailyDetails() since we declared a private Day constructor down there that isn't compatible
        //We don't need any parameters or set anything in here since we DIRECTLY set the mVars in MainActivity.
        //This is a constructor that allows MainActivity to access the whole Day class
    }


    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public int getMaxTemperature() {
        return (int) Math.round(mMaxTemperature);
    }

    public void setMaxTemperature(double maxTemperature) {
        mMaxTemperature = maxTemperature;
    }

    public String getTimezone() {

        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }

    public int getIconId(){
        return Forecast.getIconId(mIcon);
    }

    public String getDayOfTheWeek(){
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");//Instantiating a new format (gives us the whole day name)
        formatter.setTimeZone(TimeZone.getTimeZone(mTimezone));//Finds the timezone
        Date dateTime = new Date(mTime *1000);//Sets the day name based on milliseconds
        return formatter.format(dateTime);

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) { //Writing to parcel
        dest.writeString(mSummary);
        dest.writeLong(mTime);
        dest.writeString(mTimezone);
        dest.writeDouble(mMaxTemperature);
        dest.writeString(mIcon);
    }

    private Day(Parcel in){//Reading from created parcel - ORDER MATTERS WHEN WE READ IN THE DATA FROM ABOVE
        mSummary = in.readString();
        mTime = in.readLong();
        mTimezone = in.readString();
        mMaxTemperature = in.readDouble();
        mIcon = in.readString();

    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel source) {
            return new Day(source); //Constructs a new object based on our parcel's data
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];//Always like this
        }
    };


}
