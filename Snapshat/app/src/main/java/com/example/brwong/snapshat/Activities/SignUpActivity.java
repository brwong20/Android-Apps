package com.example.brwong.snapshat.Activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.brwong.snapshat.R;
import com.example.brwong.snapshat.Utilities.SnapshatClass;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class SignUpActivity extends ActionBarActivity {

    @InjectView (R.id.user_name_prompt) EditText mUsername;
    @InjectView(R.id.password_prompt) EditText mPassword;
    @InjectView(R.id.email_prompt) EditText mEmail;
    @InjectView(R.id.signUpButton) Button mSignUpButton;
    @InjectView(R.id.signup_progress)ProgressBar mSignupProgress;
    @InjectView(R.id.cancelButton) Button mCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        ButterKnife.inject(this);


        mCancelButton.setOnClickListener(new View.OnClickListener() {//Exits the Sign Up with finish()
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mSignupProgress.setVisibility(View.INVISIBLE);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                String email = mEmail.getText().toString();//Converts text to string

                if(username.isEmpty() || password.isEmpty() || email.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setMessage(R.string.signup_error_message)
                            .setTitle(R.string.signup_error_title)
                            .setPositiveButton(android.R.string.ok, null);//Dismisses the dialog with an OK
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    mSignupProgress.setVisibility(View.VISIBLE);
                    ParseUser user = new ParseUser();
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setEmail(email);
                    user.signUpInBackground(new SignUpCallback() {//Signs up in a background thread
                        @Override
                        public void done(ParseException e) {
                            mSignupProgress.setVisibility(View.INVISIBLE);
                            if(e==null){//If there was no error thrown

                                SnapshatClass.updateParseInstallation(ParseUser.getCurrentUser());//Gets user's Id that just signed up
                                //to store in ParseInstallation for Push Notifications

                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                builder.setMessage(R.string.signup_error_message)
                                        .setTitle(R.string.signup_error_title)
                                        .setPositiveButton(android.R.string.ok, null);//Dismisses the dialog with an OK
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }
}
