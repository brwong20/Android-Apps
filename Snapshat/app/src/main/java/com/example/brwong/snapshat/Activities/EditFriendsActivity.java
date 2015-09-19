package com.example.brwong.snapshat.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.brwong.snapshat.Adapters.UserAdapter;
import com.example.brwong.snapshat.Utilities.ParseConstants;
import com.example.brwong.snapshat.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;


public class EditFriendsActivity extends Activity {

    public static final String TAG = EditFriendsActivity.class.getSimpleName();

    protected List<ParseUser> mUsers;//List of users
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;
    protected GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_grid);
        mGridView = (GridView) findViewById(R.id.friendsGrid);
        mGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);//Allows us to check and uncheck items
        mGridView.setOnItemClickListener(mOnItemClickListener);//We create a custom listener below

        TextView emptyGrid = (TextView) findViewById(android.R.id.empty);
        mGridView.setEmptyView(emptyGrid);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

        ParseQuery<ParseUser> friendQuery = ParseUser.getQuery();//Creates a query of users
        friendQuery.orderByAscending(ParseConstants.KEY_USERNAME);
        friendQuery.setLimit(1000);//Limits to a thousand users
        friendQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null) {
                    mUsers = users;
                    String[] usernames = new String[mUsers.size()];//Creating an array of username strings from our List object that holds all the usernames
                    int i = 0;
                    for (ParseUser user : mUsers) {//For each loop: "For each user in mUsers"
                        usernames[i] = user.getUsername();
                        i++;
                    }
                    if (mGridView.getAdapter() == null) {//Creates the adapter and grid of users if no adapter has been created
                        UserAdapter adapter = new UserAdapter(EditFriendsActivity.this, mUsers);//Initializing a new custom UserAdapter we made
                        mGridView.setAdapter(adapter);
                    } else {
                        ((UserAdapter) mGridView.getAdapter()).refill(mUsers);//Refills the data so we can just refresh the users instead of recreating the adapter and all of its elements
                    }
                    addFriendCheckMarks();

                } else {
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditFriendsActivity.this);
                    builder.setMessage(e.getMessage())
                            .setTitle(R.string.signup_error_title)
                            .setPositiveButton(android.R.string.ok, null);//Dismisses the dialog with an OK
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }


    private void addFriendCheckMarks() {//Called AFTER the list is adapted so the ASYNCHRONOUS TASKS RUN IN SEQUENCE, NOT TOGETHER
        //Reason: this method is dependent on the adapted list of friends
        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {//Gets a query in the background
            // of checked friends(since we can't use mFriendsRelation directly)
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                if (e == null) {//List was successfully returned/adapter
                    for (int i = 0; i < mUsers.size(); i++) {//Loops through all the users

                        ParseUser friendUser = mUsers.get(i);//Converts a user in the list to a single ParseUser

                        for (ParseUser friend : friends) {// FOR EACH friend(PARSE USER) you find in friends(RETURNED LIST OF FRIENDS/ParseUser)

                            if (friend.getObjectId().equals(friendUser.getObjectId())) {//IF their id in the mFriendsRelation query/database
                                // MATCHES with a user in the list

                                mGridView.setItemChecked(i, true);//Sets check mark at position of that friend
                                //ALSO CALLS THE ADAPTER's getView METHOD EACH TIME THIS METHOD RUNS - THIS IS WHY WE CAN
                                //HAVE addFriendCheckMarks() come AFTER where the adapter is called.
                            }
                        }
                    }
                } else {
                    Log.e(TAG, e.getMessage());
                }
            }
        });

    }

    protected AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ImageView checkImageView = (ImageView)view.findViewById(R.id.user_checkmark);

            if (mGridView.isItemChecked(position)) {//If item is checked, add the friend
                mFriendsRelation.add(mUsers.get(position));
                checkImageView.setVisibility(View.VISIBLE);
            } else {//If not, delete the friend
                mFriendsRelation.remove(mUsers.get(position));
                checkImageView.setVisibility(View.INVISIBLE);
            }
            mCurrentUser.saveInBackground(new SaveCallback() {//Saves friend relation data
                @Override
                public void done(ParseException e) {
                    if (e != null) {//Don't need any other code because user can just delete and re add a friend
                        Log.e(TAG, e.getMessage());
                    }
                }
            });

        }
    };
}
