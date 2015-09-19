package com.example.brwong.interactivestory.Model;

/**
 * Created by Brwong on 5/5/15.
 */
public class Page {
    private int mImageId;
    private String mText;
    private Choice mChoice1;
    private Choice mChoice2;// Value of the two choice buttons that are clicked to go to the next page
    private boolean mIsFinal = false;

    public Page(int imageId, String text, Choice choice1, Choice choice2){
    // CUSTOM CONSTRUCTOR : ALLOWS US TO PASS IN A PAGE'S PROPERTIES ALL TOGETHER
        mImageId = imageId;
        mText = text;
        mChoice1 = choice1;
        mChoice2 = choice2;// NEED CUSTOM CONSTRUCTOR FOR THIS TOO - GO TO PAGE CLASS
    }

    public Page(int imageId, String text){ //This constructor is for either of our last pages which have NO CHOICES (null)
        mImageId = imageId;
        mText = text;
        mChoice1 = null;
        mChoice2 = null;
        mIsFinal = true; //Sets to true indicating this is a FINAL page.
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public Choice getChoice1() {
        return mChoice1;
    }

    public void setChoice1(Choice choice1) {
        mChoice1 = choice1;
    }

    public Choice getChoice2() {
        return mChoice2;
    }

    public void setChoice2(Choice choice2) {
        mChoice2 = choice2;
    }

    public int getImageId() {

        return mImageId;
    }

    public void setImageId(int imageId) {
        mImageId = imageId;
    }

    public boolean isFinal() {
        return mIsFinal;
    }

    public void setIsFinal(boolean isFinal) {
        mIsFinal = isFinal;
    }
}
