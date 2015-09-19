package com.example.brwong.snapshat.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brwong.snapshat.Adapters.UserAdapter;
import com.example.brwong.snapshat.Utilities.FileHelper;
import com.example.brwong.snapshat.Utilities.ParseConstants;
import com.example.brwong.snapshat.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ChooseRecipientsActivity extends ActionBarActivity {

    public static final String TAG = ChooseRecipientsActivity.class.getSimpleName();

    protected List<ParseUser> mFriends;//List of friends(created from EditFriendsActivity);
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;
    protected MenuItem mSendMenuItem;//Used to retrieve the send button item
    protected Uri mMediaUri;
    protected String mFileType;//For put data from MainActivity

    @InjectView(R.id.friend_list_progress_fragment) ProgressBar mProgressBar;
    @InjectView(R.id.friendsGrid) GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_grid);
        ButterKnife.inject(this);
        mGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        mGridView.setOnItemClickListener(mOnItemClickListener);
        TextView emptyGrid = (TextView)findViewById(android.R.id.empty);
        mGridView.setEmptyView(emptyGrid);
        mMediaUri = getIntent().getData();//Gets data from MainActivity
        mFileType = getIntent().getExtras().getString(ParseConstants.KEY_FILE_TYPE);//Gets the file type value passed by MainActivity
    }

    @Override
    public void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
        mProgressBar.setVisibility(View.VISIBLE);

        ParseQuery<ParseUser> friendQuery = mFriendsRelation.getQuery();
        friendQuery.addAscendingOrder(ParseConstants.KEY_USERNAME);
        friendQuery.findInBackground(new FindCallback<ParseUser>() {//Gets a query in the background
            // of checked friends(since we can't use mFriendsRelation directly)
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                mProgressBar.setVisibility(View.INVISIBLE);
                if (e == null) {
                    mFriends = friends;

                    String[] usernames = new String[mFriends.size()];
                    int i = 0;
                    for (ParseUser friend : mFriends) {
                        usernames[i] = friend.getUsername();
                        i++;
                    }

                    if(mGridView.getAdapter() == null) {//Creates the adapter and grid of users if no adapter has been created
                        UserAdapter adapter = new UserAdapter(ChooseRecipientsActivity.this, mFriends);//Initializing a new custom UserAdapter we made
                        mGridView.setAdapter(adapter);
                    }else{
                        ((UserAdapter)mGridView.getAdapter()).refill(mFriends);//Refills the data so we can just refresh the users instead of recreating the adapter and all of its elements
                    }

                } else {

                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChooseRecipientsActivity.this);
                    builder.setMessage(e.getMessage())
                            .setTitle(R.string.signup_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_recipients, menu);
        mSendMenuItem = menu.getItem(0);//Only gets one item in our layout and it's the first one(Send)
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_send) {//To give function when send button is clicked
            ParseObject message = createMessage();
            if(message==null){
                //Error
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.error_selecting_file)
                        .setTitle(R.string.error_selecting_file_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                send(message);
                finish();//Goes back to MainActivity
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void send(ParseObject message) {//Saves file (key-value) to the back-end so recipient can retrieve.
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    //Successful send
                    Toast.makeText(ChooseRecipientsActivity.this, R.string.success_message, Toast.LENGTH_LONG).show();
                    //Use a TOAST because the activity is going to FINISH right after it "sends" - toasts move between activities
                    sendPushNotification();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChooseRecipientsActivity.this);
                    builder.setMessage(R.string.error_sending_message)
                            .setTitle(R.string.error_selecting_file_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    private ParseObject createMessage() {
        ParseObject message = new ParseObject(ParseConstants.CLASS_MESSAGES);
        message.put(ParseConstants.KEY_SENDER_ID, ParseUser.getCurrentUser().getObjectId());
        message.put(ParseConstants.KEY_SENDER_NAME, ParseUser.getCurrentUser().getUsername());
        message.put(ParseConstants.KEY_RECIPIENT_ID, getRecipientIds());
        message.put(ParseConstants.KEY_FILE_TYPE, mFileType);

        byte[] fileBytes = FileHelper.getByteArrayFromFile(this, mMediaUri);//Gets the byte array from a file
        if(fileBytes == null){
            return null;
        }else{
            if(mFileType.equals(ParseConstants.TYPE_IMAGE)){//For images only

                fileBytes=FileHelper.reduceImageForUpload(fileBytes);//Resizes to make less than 10 MB if we need to
            }
            String fileName = FileHelper.getFileName(this, mMediaUri, mFileType);//Generates a name for the file

            ParseFile file = new ParseFile(fileName, fileBytes);//Creates a new ParseFile

            message.put(ParseConstants.KEY_FILE, file);//Gives a value to our KEY_FILE in message
            return message;

        }
    }

    private ArrayList<String> getRecipientIds(){
        ArrayList<String> recipientIds = new ArrayList<String>();
        for(int i=0; i<mGridView.getCount(); i++){
            if(mGridView.isItemChecked(i)){//For every friend in our list, it it's checked...
                recipientIds.add(mFriends.get(i).getObjectId());//..add them to our ArrayList by getting their
                                                               // id from the query/list on friends on the backend
            }
        }
        return recipientIds;
    }

    protected AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            ImageView checkImageView = (ImageView)view.findViewById(R.id.user_checkmark);

            //From previous OnListItemClick method

            if (mGridView.getCheckedItemCount() > 0) {
                mSendMenuItem.setVisible(true);
            } else {
                mSendMenuItem.setVisible(false);
            }

            //From EditFriendsActivity, but without the friend adding/deleting functions

            if (mGridView.isItemChecked(position)) {//If item is checked, show checkmark
                checkImageView.setVisibility(View.VISIBLE);
            } else {//If not, delete the checkmark
                checkImageView.setVisibility(View.INVISIBLE);
            }
        }
    };

    protected void sendPushNotification(){
        ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
        query.whereContainedIn(ParseConstants.KEY_USER_ID, getRecipientIds());//Validates that recipient should be receiving a Push Notification
        //Looks through the ParseInstallation query we defined using a key from ParseConstants

        //Creating and sending the Push Notification
        ParsePush push = new ParsePush();
        push.setQuery(query);
        push.setMessage(getString(R.string.push_message, ParseUser.getCurrentUser().getUsername()));//Uses placeholder in string and getUsername instead of +
        push.sendInBackground();

    }
}
