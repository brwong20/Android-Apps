package com.example.brwong.snapshat.Activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.brwong.snapshat.R;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Brwong on 5/26/15.
 */
public class ForgotPassword extends ActionBarActivity{

    @InjectView(R.id.forgotPWButton) Button mForgotButton;
    @InjectView(R.id.forgot_password_box) EditText mResetEmail;
    @InjectView(R.id.cancel_forgot_Button) Button mCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        ButterKnife.inject(this);

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mForgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resetEmail = mResetEmail.getText().toString();
                if(resetEmail.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
                    builder.setMessage(R.string.forgot_empty_error)
                            .setTitle(R.string.signup_error_title)
                            .setPositiveButton(android.R.string.ok, null);//Dismisses the dialog with an OK
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    ParseUser.requestPasswordResetInBackground(resetEmail, new RequestPasswordResetCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            if (e == null) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
                                builder.setMessage(R.string.resest_success_message)
                                        .setTitle(R.string.reset_success_title)
                                        .setPositiveButton(android.R.string.ok, null);//Dismisses the dialog with an OK
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
                                builder.setMessage(R.string.forgot_password_error)
                                        .setTitle(R.string.forgot_password_title)
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
}

