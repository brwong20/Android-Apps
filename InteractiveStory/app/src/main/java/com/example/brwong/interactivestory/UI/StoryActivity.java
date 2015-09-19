package com.example.brwong.interactivestory.UI;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.brwong.interactivestory.Model.Page;
import com.example.brwong.interactivestory.Model.Story;
import com.example.brwong.interactivestory.R;


public class StoryActivity extends Activity {

    private Story mStory = new Story();
    private ImageView mImageView;
    private TextView mTextView;
    private Button mChoice1;
    private Button mChoice2;
    private String mName; //Used to integrate user's name INTO the page - CAN DO THIS OR PASS INTO loadPage() method as a PARAMETER
    private Page mCurrentPage; //Declared here so we can use it in our OnClickListener, instead of using final because this DESCRIBES our page (property).


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        Intent intent = getIntent();
        mName = intent.getStringExtra(getString(R.string.key_name));
        if(mName == null) { // Checks to see if name was successfully passed
            mName = "You fucked up";
        }

        mImageView = (ImageView) findViewById(R.id.pageImage);
        mTextView = (TextView) findViewById(R.id.story_text);
        mChoice1 = (Button) findViewById(R.id.choiceButton1);
        mChoice2 = (Button) findViewById(R.id.choiceButton2); //THESE ARE THE VARIABLES WE WANT TO MANIPULATE IN ORDER TO CHANGE THE SCREEN
                                                                // WITHOUT USING A NEW CLASS! (Dynamic)
        loadPage(0);//Starts story at page 0

        }

    public void loadPage(int choice){

        mCurrentPage = mStory.getPage(choice);
        Drawable drawable = getResources().getDrawable(mCurrentPage.getImageId()); // Chains 3 methods: .getImageId gets THE ID FROM THE SPECIFIED PAGE , .getDrawable & .getResources work together to convert this into a DRAWABLE
        mImageView.setImageDrawable(drawable);//Dynamically Sets the image based on the page

        String pageText = mCurrentPage.getText();//Retrieves default page text

        pageText=String.format(pageText, mName); //Replaces placeholder in page's string with mName

        mTextView.setText(pageText);//Dynamically sets text based on the page used AND name given

        if(mCurrentPage.isFinal()){
            mChoice1.setVisibility(View.INVISIBLE);
            mChoice2.setText("Play again.");
            mChoice2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {

            mChoice1.setText(mCurrentPage.getChoice1().getText());// Have to put this AFTER the if statement b/c there are no choices anymore, just a PLAY AGAIN finish() button.
            mChoice2.setText(mCurrentPage.getChoice2().getText());

            mChoice1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int nextPage = mCurrentPage.getChoice1().getNextPage(); //Either declare page FINAL or make it a member variable because the SCOPE of page doesn't reach in this
                    // class as an instance
                    loadPage(nextPage); //Utilizes the loadPage to do the same as how we started in main activity (loadPage(0))
                }
            });//When clicking choice 1's BUTTON, this will RETURN choice 1's code and RETURN the NEXT PAGES ID(getNextPage) from choice1

            mChoice2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int nextPage = mCurrentPage.getChoice2().getNextPage();
                    loadPage(nextPage);
                }
            });
        }
    }//Method to get a page from the Story class using mStory as the instance of the class AND declaring a getPage() method in
     //the Story class
}

