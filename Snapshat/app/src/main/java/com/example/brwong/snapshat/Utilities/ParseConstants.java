package com.example.brwong.snapshat.Utilities;

/**
 * Created by Brwong on 5/26/15.
 */
public final class ParseConstants { //Class will never change - class used to declare our parse parameters/objects so we don't have to over and over
    //Class names
    public static final String CLASS_MESSAGES = "Messages"; //Allows us to use their Messages class to store key value message objects(ParseObjects)

    //Field names
    public static final String KEY_USERNAME = "username";
    public static final String KEY_FRIENDS_RELATION = "friendsRelation";

    //Keys for a message(fields in our CLASS_MESSAGES on Parse)
    public static final String KEY_RECIPIENT_ID = "recipientIds";
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_FILE = "file";
    public static final String KEY_FILE_TYPE = "fileType";
    public static final String KEY_CREATED_AT = "createdAt";//Used to sort inbox messages by date

    //Used to differentiate between an image and video
    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_VIDEO = "video";

    //To store recipients of push notifications
    public static final String KEY_USER_ID = "userId";


}
