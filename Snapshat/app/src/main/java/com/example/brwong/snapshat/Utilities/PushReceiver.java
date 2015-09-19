package com.example.brwong.snapshat.Utilities;

import android.content.Context;
import android.content.Intent;

import com.example.brwong.snapshat.Activities.MainActivity;
import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by Brwong on 6/22/15.
 */
public class PushReceiver extends ParsePushBroadcastReceiver {//Used to receive push notification and instruct it to open up MainActivity

    @Override
    public void onPushOpen(Context context, Intent intent) {
        Intent i = new Intent(context, MainActivity.class);
        i.putExtras(intent.getExtras());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

}
