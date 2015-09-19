package com.example.brwong.weather.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brwong.weather.R;
import com.example.brwong.weather.weather_data.Hour;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Brwong on 5/17/15.
 */
public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.HourViewHolder> {


    public Context mContext;//Added because the Toast in our ViewHolder doesn't know the context while it's in the ViewHolder class
    public Hour[] mHours;

    public HourlyForecastAdapter(Context context, Hour[] hours){//Change the constructor for this adapter in the activity too!
        mContext = context;
        mHours = hours;
    }

    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)//Get context from viewGroup always
                .inflate(R.layout.hourly_list_item, viewGroup, false);//Params: (your created layout file, viewGroup, false)
        HourViewHolder viewholder = new HourViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(HourViewHolder hourViewHolder, int i) {
        hourViewHolder.bindHour(mHours[i]);//Gets the position of each Hour element in the array (each hour) and
                                            // binds the each one's data to our Views as created down below
    }

    @Override
    public int getItemCount() {
        return mHours.length;
    }



    public class HourViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView mTimeLabel;//
        public TextView mTemperatureLabel;
        public TextView mSummaryLabel;
        public ImageView mIconImageView;



        public HourViewHolder(View itemView) {//Hold these views
            super(itemView);

            mTimeLabel = (TextView) itemView.findViewById(R.id.hourly_time_label);
            mTemperatureLabel = (TextView) itemView.findViewById(R.id.temp_label);
            mSummaryLabel = (TextView) itemView.findViewById(R.id.summary_label);
            mIconImageView = (ImageView) itemView.findViewById(R.id.icon_image_view);

            itemView.setOnClickListener(this); //Sets an OnClickListener for the OnClick method implemented below
                                        //Passing in this because only this class implement the OnClickListener
        }

        public void bindHour(Hour hour){//Bind them to this data
            mTimeLabel.setText(hour.getHourOfDay());
            mTemperatureLabel.setText(hour.getTemperature()+"");
            mSummaryLabel.setText(hour.getSummary());
            mIconImageView.setImageResource(hour.getIconId());

        }

        @Override
        public void onClick(View v) {
            String time = mTimeLabel.getText().toString();
            String temperature = mTemperatureLabel.getText().toString();
            String summary = mSummaryLabel.getText().toString();
            String message = String.format("At %s, it will be %s and %s.", time, temperature, summary);

            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();//Need a context variable!
        }

    }

}
