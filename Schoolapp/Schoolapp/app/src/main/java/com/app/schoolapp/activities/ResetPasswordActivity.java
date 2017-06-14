package com.app.schoolapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class ResetPasswordActivity extends AppCompatActivity implements ApiResponse {
    private Context context;
    private Toolbar toolbar;
    private EditText resetCode, password, passwordConfirm;
    private Button reset_password_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        context = this;
        init();
        setListener();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Reset Password");
        resetCode = (EditText) findViewById(R.id.resetCode);
        password = (EditText) findViewById(R.id.password);
        passwordConfirm = (EditText) findViewById(R.id.passwordConfirm);
        reset_password_button = (Button) findViewById(R.id.reset_password_button);
    }

    private void setListener() {
        reset_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidNumber(resetCode.getText().toString())) {
                    resetCode.setError("enter 4 digit pin");
                    resetCode.requestFocus();
                    return;
                } else if (!isValidNumber(password.getText().toString())) {
                    password.setError("New password must be 4 digit long");
                    password.requestFocus();
                    return;
                } else if (!isValidNumber(passwordConfirm.getText().toString())) {
                    passwordConfirm.setError("New password must be 4 digit long");
                    passwordConfirm.requestFocus();
                    return;
                } else {
                    if (password.getText().toString().trim().equals(passwordConfirm.getText().toString().trim())) {
                        onResetPassword();
                    } else {
                        Toast.makeText(context, "Password must be same", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void onResetPassword() {
        Toast.makeText(context, "On reset password", Toast.LENGTH_SHORT).show();
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                HashMap<String, String> hm = new HashMap<>();
                hm.put("email", AppUtils.getUserEmail(context));
                hm.put("reset_code", resetCode.getText().toString());
                hm.put("password", passwordConfirm.getText().toString());
                String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.verify);
                new CommonAsyncTaskHashmap(1, context, this).getqueryJson(url, 2, hm);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidNumber(String num) {
        if (num.length() >= 4) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {

        try {
            int success = response.getInt("success");
            int error = response.getInt("error");
            String message = response.getString("message");
            if (success == 1) {
                if (error == 1) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(context, LoginActivity.class));
                    finish();
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }


            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostFail(int method, String response) {
        Toast.makeText(context, "Password must be same", Toast.LENGTH_SHORT).show();
    }
}
