package com.example.brwong.snapshat.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.brwong.snapshat.Activities.ViewImageActivity;
import com.example.brwong.snapshat.Adapters.MessageAdapter;
import com.example.brwong.snapshat.Utilities.ParseConstants;
import com.example.brwong.snapshat.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brwong on 5/26/15.
 */
public class InboxFragment extends ListFragment {

    protected List<ParseObject> mMessages;
    SwipeRefreshLayout mSwipeRefreshLayout;


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.inbox_fragment, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mSwipeRefreshLayout.setColorSchemeColors(R.color.swipe_color_1,
                R.color.swipe_color_2,
                R.color.swipe_color_3,
                R.color.swipe_color_4);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        retrieveMessages();
    }

    private void retrieveMessages() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_MESSAGES);
        query.whereEqualTo(ParseConstants.KEY_RECIPIENT_ID, ParseUser.getCurrentUser().getObjectId());
        query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);//Sorts our list below based on creation date.

        query.findInBackground(new FindCallback<ParseObject>() {//Uses conditions above to create a list
            @Override
            public void done(List<ParseObject> messages, ParseException e) {

                if(mSwipeRefreshLayout.isRefreshing()){//If the SwipeRefreshLayout is STILL refreshing after our done method has finished, STOP refreshing
                    mSwipeRefreshLayout.setRefreshing(false);
                }

                if(e == null) {//Messages found!
                    mMessages=messages;

                    /*String[] usernames = new String[mMessage.size()];
                    int i = 0;
                    for (ParseObject message : mMessages) {
                        usernames[i] = message.getString(ParseConstants.KEY_SENDER_NAME);//Gets sender name from the name we PUT into the message when sending
                        i++;*///Don't need this since our custom adapter does all this for us
                    if(getListView().getAdapter() == null) {
                        MessageAdapter adapter = new MessageAdapter(getListView().getContext(), mMessages);
                        setListAdapter(adapter);
                    }else{
                        ((MessageAdapter)getListView().getAdapter()).refill(mMessages);
                    }
                }
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ParseObject message = mMessages.get(position);//Gets message at current position
        String messageType = message.getString(ParseConstants.KEY_FILE_TYPE);
        ParseFile file = message.getParseFile(ParseConstants.KEY_FILE);
        Uri fileUri = Uri.parse(file.getUrl());
        if(messageType.equals(ParseConstants.TYPE_IMAGE)) {//If it's a photo
            //View the image
            Intent photoIntent = new Intent(getActivity(), ViewImageActivity.class);
            photoIntent.setData(fileUri);
            startActivity(photoIntent);
        }else{
            //View the video
            Intent videoIntent = new Intent(Intent.ACTION_VIEW, fileUri);
            videoIntent.setDataAndType(fileUri, "video/*");//Not always required, used here to be specific
            startActivity(videoIntent);
        }

        //Delete right after, even if only viewed for a second
        List<String> ids = message.getList(ParseConstants.KEY_RECIPIENT_ID);//Object Ids are stored as Strings on parse, we're getting all of them with this

        if(ids.size() == 1){
            //If there is only one more recipient left, delete the message after they view
            message.deleteInBackground();
        }else{

            //Remove the recipient and save the message
            ids.remove(ParseUser.getCurrentUser().getObjectId()); //Removes id locally

            ArrayList<String> idToRemove = new ArrayList<String>();//Need an ArrayList because it's a COLLECTION of values (parameter removeAll needs)

            idToRemove.add(ParseUser.getCurrentUser().getObjectId()); //Adds the current user's Id to the array list

            message.removeAll(ParseConstants.KEY_RECIPIENT_ID, idToRemove);//Removes the id set above from the Key on Parse.

            message.saveInBackground();//Saves deletion

        }
    }

    protected SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            retrieveMessages();
        }
    };
}
