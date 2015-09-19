package com.example.brwong.funfacts;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;


public class FunFactsActivity extends Activity {

    private FactBook mFactBook = new FactBook(); //Creating a FactBook object to use to always get our facts ONLY in this activity
    private ColorWheel mColorWheel = new ColorWheel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fun_facts);

        final TextView factLabel = (TextView) findViewById(R.id.fact_TextView);
        final Button showFact = (Button) findViewById(R.id.another_fact_button);
        final RelativeLayout factPageColor = (RelativeLayout) findViewById(R.id.relativeLayout);

        View.OnClickListener listenforthis = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newFact = mFactBook.getFact();
                factLabel.setText(newFact); //This sets factLabel to FINAL(final) and doesn't allow it to be reassigned a function since only this button can change the text to something else
                int newColor = mColorWheel.getColor();
                factPageColor.setBackgroundColor(newColor);// newColor has to be an int!! Parse used to change - refer to ColorWheel class
                showFact.setTextColor(newColor);//Sets the buttons text color the same as the page's color
            }
        };

        showFact.setOnClickListener(listenforthis);


    }
}
