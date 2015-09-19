package com.example.brwong.snapshat.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.brwong.snapshat.Utilities.ParseConstants;
import com.example.brwong.snapshat.R;
import com.example.brwong.snapshat.Adapters.SectionsPagerAdapter;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener{

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final int TAKE_PHOTO_REQUEST = 0;
    public static final int TAKE_VIDEO_REQUEST = 1;
    public static final int CHOOSE_PHOTO_REQUEST = 2;
    public static final int CHOOSE_VIDEO_REQUEST = 3;

    public static final int MEDIA_TYPE_IMAGE = 4;
    public static final int MEDIA_TYPE_VIDEO =5;

    public static final int FILE_SIZE_LIMIT = 1024*1024*10; //10 MEGABYTES

    protected Uri mMediaUri;


    protected DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener() {//Defining Dialog listener up here for more organized/concise code
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch(which){
                case 0: //Take pic
                    takePhoto();
                    break;
                case 1: //Take vid
                    takeVideo();
                    break;
                case 2: //Choose photo
                    Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    choosePhotoIntent.setType("image/*");//Filters only IMAGES for us to select
                    startActivityForResult(choosePhotoIntent, CHOOSE_PHOTO_REQUEST);
                    break;
                case 3: //Choose video
                    Intent chooseVideoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    chooseVideoIntent.setType("video/*");//Filters only VIDEOS for us to select
                    Toast.makeText(MainActivity.this, R.string.video_file_size_warning, Toast.LENGTH_LONG).show();
                    startActivityForResult(chooseVideoIntent, CHOOSE_VIDEO_REQUEST);
                    break;
                default:
                    break;
            }
        }

        private void takeVideo() {
            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
            if(mMediaUri == null){
                Toast.makeText(MainActivity.this, R.string.external_storage_error, Toast.LENGTH_LONG).show();
            }else {
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);//Puts taken image or video to MediaStore to be saved
                takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);//Sets the limit of time a video can be
                takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0); //0 == lowest quality - Parse doesn't allow HQ vids to be sent
                startActivityForResult(takeVideoIntent, TAKE_VIDEO_REQUEST);
            }
        }

        private void takePhoto() {
            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//Looks for a camera app to use

            mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);//Makes our photo file as a Uri
            if(mMediaUri == null){
                Toast.makeText(MainActivity.this, R.string.external_storage_error, Toast.LENGTH_LONG).show();
            }else {
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);//Puts taken image or video to MediaStore to be saved
                startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
            }
        }

        private Uri getOutputMediaFileUri(int mediaType) {//RETRIEVES THE PUT Uri and converts into an image or video file


            // To be safe, you should check that the EXTERNAL STORAGE/SD card is mounted
            // using Environment.getExternalStorageState() before doing this.
            if(isExternalStorageAvailable()){
                //Get the Uri

                //1. CREATE A FILE TO STORE PICS/VIDS FROM OUR APP(this is why appName is used)
                String appName = MainActivity.this.getString(R.string.app_name);
                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        appName);

                //2. Create our directory from above if it doesn't exist
                if(!mediaStorageDir.exists()){
                    if(!mediaStorageDir.mkdirs()){
                        Log.e(TAG, "Failed to create director");
                        return null;
                    }
                }

                //3 & 4. Crate a file and its name
                File mediaFile;
                Date now = new Date();
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(now);
                String path = mediaStorageDir.getPath() + File.separator;
                if(mediaType == MEDIA_TYPE_IMAGE) {
                    mediaFile = new File(path + "IMG_" + timestamp + ".jpg");
                }else if(mediaType == MEDIA_TYPE_VIDEO){
                    mediaFile = new File(path+ "VID_" + timestamp + ".mp4");
                }else{
                    return null;
                }

                Log.d(TAG, "File:" + Uri.fromFile(mediaFile));

                //5. Return the file's Uri
                return Uri.fromFile(mediaFile);

            }else{
                return null;
            }
        }
        private boolean isExternalStorageAvailable(){
            String state = Environment.getExternalStorageState();//Tells us what state external storage is
            if(state.equals(Environment.MEDIA_MOUNTED)){//If state is mounted(external storage is ok)
                return true;
            }else{
                return false;
            }
        }
    };


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseUser currentUser = ParseUser.getCurrentUser();//Gets the current user's cached data if entered properly(KEEPS THEM LOGGED IN)
        if(currentUser==null) {//Takes user back to log in screen if they didn't log in correctly
            navigateToLogin();
        }else{
            Log.i(TAG, currentUser.getUsername());
        }

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());//Passed in this b/c we changed the constructor in
                                                                                            //the SectionsPagerAdapter class

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setIcon(mSectionsPagerAdapter.getIcon(i))
                            .setTabListener(this));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {//Save to gallery, result was ok
            if (requestCode == CHOOSE_PHOTO_REQUEST || requestCode == CHOOSE_VIDEO_REQUEST) {//Checks to see if we performed the right request
                if (data == null) {
                    Toast.makeText(MainActivity.this, R.string.camera_general_error, Toast.LENGTH_LONG).show();
                } else {
                    mMediaUri = data.getData();//GETS the data we chose (photo data or video data)
                }
                if (requestCode == CHOOSE_VIDEO_REQUEST) {//Filters out photos
                    int fileSize = 0;

                    InputStream inputStream = null;//Declared out here to give us access in the finally block

                    try {
                        inputStream = getContentResolver().openInputStream(mMediaUri);
                        fileSize = inputStream.available();
                    } catch (FileNotFoundException fnfe) {
                        Toast.makeText(MainActivity.this, R.string.file_not_found_error, Toast.LENGTH_LONG).show();
                        return;
                    } catch (IOException ioe) {
                        Toast.makeText(MainActivity.this, R.string.file_not_found_error, Toast.LENGTH_LONG).show();
                        return;
                    } finally {
                        try {
                            inputStream.close();
                        } catch (IOException e) {/* */}//Left blank intentionally b/c we just want to close the stream}
                    }
                    if (fileSize >= FILE_SIZE_LIMIT) {//If video is greater than or equal 10MB
                        Toast.makeText(MainActivity.this, R.string.file_too_big_warning, Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            } else {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);//Scans file to see if its good for gallery

                mediaScanIntent.setData(mMediaUri);

                sendBroadcast(mediaScanIntent);//Sends to scanned/registered data(images only)
            }


            Intent recipientsIntent = new Intent(this, ChooseRecipientsActivity.class);
            recipientsIntent.setData(mMediaUri);//Passing in the media uri file we created above

            String fileType;
            if (requestCode == TAKE_PHOTO_REQUEST || requestCode == CHOOSE_PHOTO_REQUEST) {//If the request was to for photos(image)
                fileType = ParseConstants.TYPE_IMAGE;//"image"
            } else {
                fileType = ParseConstants.TYPE_VIDEO;//"video"
            }
            recipientsIntent.putExtra(ParseConstants.KEY_FILE_TYPE, fileType);
            startActivity(recipientsIntent);
        } else if (resultCode != RESULT_CANCELED) {//If activity is cancelled, not when camera is closed, or back button is pressed
            Toast.makeText(MainActivity.this, R.string.camera_general_error, Toast.LENGTH_LONG).show();

        }
    }


    private void navigateToLogin() {//REFACTORED(Extracted method) code to bring an activity back to the log in screen
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//Makes the LoginActivity a new task
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//Clears this task from the "Activity stack"
        startActivity(loginIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.logout_action: //If our logout option item is pressed
                ParseUser.logOut();//Logs user out
                navigateToLogin();//Brings user back to log in screen
                break;
            case R.id.edit_friends_action:
                Intent editIntent = new Intent(this, EditFriendsActivity.class);
                startActivity(editIntent);
                break;
            case R.id.camera_action:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setItems(R.array.camera_choices, mDialogListener);
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

}
