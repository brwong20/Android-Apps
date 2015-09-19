package com.example.brwong.snapshat.Fragments;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.brwong.snapshat.Adapters.UserAdapter;
import com.example.brwong.snapshat.Utilities.ParseConstants;
import com.example.brwong.snapshat.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Brwong on 5/26/15.
 */
public class FriendsFragment extends Fragment {

    public static final String TAG = FriendsFragment.class.getSimpleName();

    protected List<ParseUser> mFriends;//List of friends(created from EditFriendsActivity);
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;
    protected GridView mGridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.user_grid, container, false);
            mGridView = (GridView)rootView.findViewById(R.id.friendsGrid);

            TextView emptyGrid = (TextView)rootView.findViewById(android.R.id.empty);
            mGridView.setEmptyView(emptyGrid);//Manually sets the text for an empty list
            return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

        ParseQuery<ParseUser> friendQuery = mFriendsRelation.getQuery();
        friendQuery.addAscendingOrder(ParseConstants.KEY_USERNAME);
        friendQuery.findInBackground(new FindCallback<ParseUser>() {//Gets a query in the background
            // of checked friends(since we can't use mFriendsRelation directly)
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                if (e == null) {
                    mFriends = friends;

                    String[] usernames = new String[mFriends.size()];
                    int i = 0;
                    for (ParseUser friend : mFriends) {
                        usernames[i] = friend.getUsername();
                        i++;
                    }
                    if(mGridView.getAdapter() == null) {//Creates the adapter and grid of users if no adapter has been created
                        UserAdapter adapter = new UserAdapter(getActivity(), mFriends);//Initializing a new custom UserAdapter we made
                        mGridView.setAdapter(adapter);
                    }else{
                        ((UserAdapter)mGridView.getAdapter()).refill(mFriends);//Refills the data so we can just refresh the users instead of recreating the adapter and all of its elements
                    }
                } else {

                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(e.getMessage())
                            .setTitle(R.string.signup_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

    }
}
