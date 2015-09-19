package com.example.brwong.snapshat.Adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.brwong.snapshat.R;
import com.example.brwong.snapshat.Utilities.MD5Util;
import com.example.brwong.snapshat.Utilities.ParseConstants;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

/**
 * Created by Brwong on 5/31/15.
 */
public class UserAdapter extends ArrayAdapter<ParseUser> {
//Getting the users for each box in the GridView - does EXACTLY the same thing as the MessageAdapter, but for names and Images instead

    protected Context mContext;
    protected List<ParseUser> mUsers;

    public UserAdapter(Context context, List<ParseUser> users) {
        super(context, R.layout.user_item, users);
        mContext = context;
        mUsers = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {//VERY IMPORTANT: Implemented ourselves to create a View for every item

        ViewHolder holder;

        if (convertView == null) {//If convertView isn't created yet, make it
            convertView = LayoutInflater.from(mContext).inflate(R.layout.user_item, null);
            holder = new ViewHolder();
            holder.mUserImageView = (ImageView) convertView.findViewById(R.id.user_avatar_image);//Uses convertView b/c ONLY IT EXTENDS ACTIVITY IN HERE
            holder.mSenderLabel = (TextView) convertView.findViewById(R.id.nameLabel);
            holder.mCheckImageView = (ImageView) convertView.findViewById(R.id.user_checkmark);
            convertView.setTag(holder);//Saves our setup
        }
        else {//If it is, let's reuse it
            holder = (ViewHolder) convertView.getTag();//Reuses our setup
        }

        //Sets data in each view(for each position in the list)
        ParseUser user = mUsers.get(position);

        String email = user.getEmail().toLowerCase();//For Gravatar - needs ALL lowercase

        if(email.equals("")){
            holder.mUserImageView.setImageResource(R.drawable.avatar_empty);//If no email, set to default empty image
        }else{
            String hash = MD5Util.md5Hex(email);//Creates a hash value with pre-written MD5 class
            String gravatarUrl = "http://www.gravatar.com/avatar/HASH" + hash + "?s=204" + "&d=404";
            //Creates the gravatar request Url with a specific size and 404 code if no email is tied to an image
            Picasso.with(mContext)
                    .load(gravatarUrl)
                    .placeholder(R.drawable.avatar_empty)//If 404 code is retrieved
                    .into(holder.mUserImageView);//Loads gravatar image into View
        }

        holder.mSenderLabel.setText(user.getUsername());//Using the easier getUserName method provided for ParseUser class

        GridView gridView = (GridView)parent;//References whatever View type is passed in (GridView in this case)
        if(gridView.isItemChecked(position)){
            holder.mCheckImageView.setVisibility(View.VISIBLE);//Set check mark drawable over image if checked
        }else{
            holder.mCheckImageView.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private static class ViewHolder{//Holds the View elements for each item
        ImageView mUserImageView;
        ImageView mCheckImageView;
        TextView mSenderLabel;
    }

    public void refill(List<ParseUser> users){
        mUsers.clear();
        mUsers.addAll(users);
        notifyDataSetChanged();
    }
}
