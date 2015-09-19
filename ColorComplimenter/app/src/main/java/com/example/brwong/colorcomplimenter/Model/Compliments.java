package com.example.brwong.colorcomplimenter.Model;

/**
 * Created by Brwong on 5/7/15.
 */
public class Compliments {
    private String mCompliment;
    private String mColor;

    public String getCompliment() {
        return mCompliment;
    }

    public void setCompliment(String compliment) {
        mCompliment = compliment;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public Compliments(String color, String compliment){
        mColor = color;
        mCompliment =  compliment;
    }
}
