package com.example.brwong.snapshat.Utilities;

import android.app.Application;

import com.example.brwong.snapshat.Activities.MainActivity;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.PushService;

/**
 * Created by Brwong on 5/22/15.
 */
public class SnapshatClass extends Application {//Tests to see if we are hooked up to Parse



    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "juYR9QXWJeKfOWeePiNQwKqpmLmeZOLAwyZEyz7p", "Bhu43us2BErXw7e4SUQF5my8z6sgJuzHw0UGs8wr");

        //Test to see successful SDK install
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");//Sets bar as the value of foo
        testObject.saveInBackground();//Saves data in background thread to Parse database.
        ParseInstallation.getCurrentInstallation().saveInBackground();//Tests for Parse Push Notifications
    }

    public static void updateParseInstallation(ParseUser user){
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put(ParseConstants.KEY_USER_ID, user.getObjectId());
        installation.saveInBackground();
    }

}
