package com.example.brwong.weather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.brwong.weather.R;
import com.example.brwong.weather.weather_data.Day;

/**
 * Created by Brwong on 5/16/15.
 */
public class DailyForecastAdapter extends BaseAdapter {

    private Context mContext;
    private Day[] mDays;

    public DailyForecastAdapter(Context context, Day[] days){//Allows us to construct our adapter and use it in the class we pass data to
        mContext = context;
        mDays = days;
    }


    @Override
    public int getCount() {//This COUNTS how many elements/items we have in our array (Day[])
        return mDays.length;
    }

    @Override
    public Object getItem(int position) {//Gets the element/item at the position passed in
        return mDays[position];
    }

    @Override
    public long getItemId(int position) {//Not used: Used to tag items for easy reference.
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.daily_forecast_list, null);//Inflating the layout we created for the list

            holder = new ViewHolder();//Initializing our ViewHolder class

            holder.day_name_label = (TextView) convertView.findViewById(R.id.day_name_label);//Setting our ViewHolder's views once by finding their ids in our inflated layout
            holder.list_weather_icon = (ImageView) convertView.findViewById(R.id.list_weather_icon);
            holder.temperature_label = (TextView) convertView.findViewById(R.id.temperature_label);

            convertView.setTag(holder);//Creates a ViewHolder tag (setup) so it can be reused
        }else{
            holder = (ViewHolder) convertView.getTag();//After we have created our convertView, we can now RECYCLE and RESET the layout's Views based on the Views ViewHolder holds!
        }

        Day day = mDays[position];

        holder.list_weather_icon.setImageResource(day.getIconId());
        holder.temperature_label.setText(day.getMaxTemperature() + "");


        if(position == 0){//0 = today b/c the API has 8 elements in its daily array
            holder.day_name_label.setText("Today");//Sets a View for today since the first position of the array will give us today's forecast
        }else {
            holder.day_name_label.setText(day.getDayOfTheWeek());
        }



        return convertView; //We return our convertView since it is the View being set AND reset each time data is passed in
    }

    private static class ViewHolder{
        ImageView list_weather_icon;
        TextView temperature_label;
        TextView day_name_label;
    }
}
