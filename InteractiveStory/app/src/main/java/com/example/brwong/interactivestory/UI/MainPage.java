package com.example.brwong.interactivestory.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.brwong.interactivestory.R;


public class MainPage extends Activity{

    private EditText mNameField;
    private Button mStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        mNameField = (EditText) findViewById(R.id.startEditText);
        mStartButton = (Button) findViewById(R.id.startButton);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNameField.getText().toString();
                //Toast.makeText(MainPage.this, name, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainPage.this, StoryActivity.class);
                intent.putExtra(getString(R.string.key_name), name);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mNameField.setText("");//Allows you to resume your main activity's life cycle AND SETS THE EDIT TEXT TO A BLANK FOR A NEW INPUT
    }
}

