package com.app.schoolapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.schoolapp.R;
import com.app.schoolapp.asynctask.CommonAsyncTaskHashmap;
import com.app.schoolapp.interfaces.ApiResponse;
import com.app.schoolapp.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class ForgotPassword extends AppCompatActivity implements ApiResponse {

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
                    onForgotPassword();
                    // Toast.makeText(context, "Link for reset password will be sent on your email id.", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void onForgotPassword() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                HashMap<String, String> hm = new HashMap<>();
                hm.put("email", email.getText().toString());
                String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.forgotpassword);
                new CommonAsyncTaskHashmap(1, context, this).getqueryJson(url, 2, hm);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {
        Log.e("response:", response.toString());
        try {
            int success = response.getInt("success");
            if (success == 1) {
                Toast.makeText(context, "Link for reset password will be sent on your email id.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ForgotPassword.this, ResetPasswordActivity.class));
                AppUtils.setUserEmail(context, email.getText().toString().trim());
                finish();
            } else {
                Toast.makeText(context, "Something went wrong !", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostFail(int method, String response) {
        Toast.makeText(context, "Something went wrong !", Toast.LENGTH_LONG).show();
    }
}
