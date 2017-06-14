package com.app.schoolapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.schoolapp.R;
import com.app.schoolapp.utils.AppUtils;


public class ForgotPassword extends AppCompatActivity {

    private Context context;
    private Toolbar toolbar;
    private EditText email;
    private Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        context = this;
        init();
        setListener();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Forgot Password");
        email = (EditText) findViewById(R.id.email);
        btn_submit = (Button) findViewById(R.id.btn_submit);
    }

    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppUtils.isEmailValid(email.getText().toString())) {
                    email.setError(getString(R.string.error_invalid_email));
                    email.requestFocus();
                } else {
                    Toast.makeText(context, "Link for reset password will be sent on your email id.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }


}
