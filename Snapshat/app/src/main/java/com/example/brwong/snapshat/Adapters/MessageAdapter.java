package com.example.brwong.snapshat.Adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.brwong.snapshat.Utilities.ParseConstants;
import com.example.brwong.snapshat.R;
import com.parse.ParseObject;

import java.util.Date;
import java.util.List;

/**
 * Created by Brwong on 5/31/15.
 */
public class MessageAdapter extends ArrayAdapter<ParseObject> {

    protected Context mContext;
    protected List<ParseObject> mMessages;

    public MessageAdapter(Context context, List<ParseObject> messages) {
        super(context, R.layout.message_item, messages);
        mContext = context;
        mMessages = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {//VERY IMPORTANT: Implemented ourselves to create a View for every item

        ViewHolder holder;

        if (convertView == null) {//If convertView isn't created yet, make it
            convertView = LayoutInflater.from(mContext).inflate(R.layout.message_item, null);
            holder = new ViewHolder();
            holder.mIconImageView = (ImageView) convertView.findViewById(R.id.message_icon);//Uses convertView b/c ONLY IT EXTENDS ACTIVITY IN HERE
            holder.mSenderLabel = (TextView) convertView.findViewById(R.id.sender_label);
            holder.mTimeLabel = (TextView) convertView.findViewById(R.id.time_label);
            convertView.setTag(holder);//Saves our setup
        }
        else {//If it is, let's reuse it
            holder = (ViewHolder) convertView.getTag();//Reuses our setup
        }

        //Sets data in each view(for each position in the list)
        ParseObject message = mMessages.get(position);

        Date createdAt = message.getCreatedAt();//Gets created Date
        long now = new Date().getTime();//Gets current time
        String convertedDate = DateUtils.getRelativeTimeSpanString(createdAt.getTime(),
                now,
                DateUtils.SECOND_IN_MILLIS).toString();//Converts created Date to long and uses time now and time created to give relative time

        holder.mTimeLabel.setText(convertedDate);

        if(message.getString(ParseConstants.KEY_FILE_TYPE).equals(ParseConstants.TYPE_IMAGE)) {
            holder.mIconImageView.setImageResource(R.drawable.ic_picture);
        }else{
            holder.mIconImageView.setImageResource(R.drawable.ic_video);
        }

        holder.mSenderLabel.setText(message.getString(ParseConstants.KEY_SENDER_NAME));
        return convertView;
    }

    private static class ViewHolder{//Holds the View elements for each item
        ImageView mIconImageView;
        TextView mSenderLabel;
        TextView mTimeLabel;
    }

    public void refill(List<ParseObject> messages){
        mMessages.clear();
        mMessages.addAll(messages);
        notifyDataSetChanged();
    }
}
