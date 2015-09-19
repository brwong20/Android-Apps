package com.example.brwong.interactivestory.Model;

/**
 * Created by Brwong on 5/5/15.
 */
public class Choice {
    private String mText;
    private int mNextPage; // Chooses a page from our array of pages based on the user's choice

    public Choice(String text, int nextPage){
    //Constructs a choice for us
        mText = text;
        mNextPage = nextPage;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public int getNextPage() {
        return mNextPage;
    }

    public void setNextPage(int nextPage) {
        mNextPage = nextPage;
    }
}
