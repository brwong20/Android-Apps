package com.example.brwong.colorcomplimenter.UI;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.brwong.colorcomplimenter.R;


public class MainActivity extends ActionBarActivity {
    private TextView mColorField;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mColorField = (EditText) findViewById(R.id.prompt_color);
        mButton = (Button) findViewById(R.id.color_button);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String favColor = mColorField.getText().toString();
                Intent intent = new Intent(MainActivity.this, Complimenter.class);
                intent.putExtra(getString(R.string.color_key), favColor);
                startActivity(intent);

            }
        });


    }

}
