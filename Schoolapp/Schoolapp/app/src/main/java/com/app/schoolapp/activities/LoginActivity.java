package com.app.schoolapp.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.schoolapp.R;
import com.app.schoolapp.asynctask.CommonAsyncTaskHashmap;
import com.app.schoolapp.interfaces.ApiResponse;
import com.app.schoolapp.parent.ParentDashboard;
import com.app.schoolapp.student.StudentDashboard;
import com.app.schoolapp.teacher.TeacherDashboard;
import com.app.schoolapp.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements ApiResponse {


    private EditText mEmailView;
    private EditText mPasswordView;
    private Context context;
    private TextView text_signup, text_forgot_password;
    String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    int PERMISSION_ALL = 1;
    private Button mEmailSignInButton;
    ImageView imag_checkbox;
    boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        context = this;
        init();
        setListener();
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Login");

        text_signup = (TextView) findViewById(R.id.text_signup);
        text_forgot_password = (TextView) findViewById(R.id.text_forgot_password);
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        imag_checkbox = (ImageView) findViewById(R.id.imag_checkbox);
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

    }

    private void setListener() {
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
/*               Intent intent3 = new Intent(context, TeacherDashboard.class);
                intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent3);*/
            }
        });
        imag_checkbox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChecked) {
                    isChecked = false;
                    imag_checkbox.setImageResource(R.drawable.unchecked);
                } else {
                    isChecked = true;
                    imag_checkbox.setImageResource(R.drawable.checked);
                }
            }
        });

        text_forgot_password.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, ForgotPassword.class);
                startActivity(in);
            }
        });

        text_signup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(context, SignUp.class);
                startActivity(in);
            }
        });

    }


    private void loginUser() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                HashMap<String, String> hm = new HashMap<>();
                hm.put("username", mEmailView.getText().toString());
                hm.put("password", mPasswordView.getText().toString());
                hm.put("appname", "school");
                //       http://squarei.in/api/v2/login?email=anderson.soyug@gmail.com&password=4105
                String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.loginuser);
                new CommonAsyncTaskHashmap(1, context, this).getqueryJson(url, hm);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            loginUser();

            // service for login
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {
        try {
            if (method == 1) {
                int success = response.getInt("success");
                int error = response.getInt("error");
                String message = response.getString("message");
             /*   JSONObject commandResult = response
                        .getJSONObject("commandResult");*/
                if (success == 1 && error == 0) {
                    String client_id = response.getJSONObject("data").getString("client_id");
                    String clientType = response.getJSONObject("data").getString("category");

                    Log.e("Login result: ", client_id);

                    if (client_id != "" || client_id != "null" || client_id != null) {
                        AppUtils.setUserId(context, client_id);
                        AppUtils.setUserType(context, clientType);
                    }
                    switch (clientType) {
                        case "Student":
                         //   AppUtils.setWardId(context, client_id);
                            Intent intent = new Intent(context, StudentDashboard.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            break;
                        case "Parent":
                            Intent intent2 = new Intent(context, ParentDashboard.class);
                            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent2);
                            break;
                        case "Teacher":
                            Intent intent3 = new Intent(context, TeacherDashboard.class);
                            intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent3);
                            break;
                        default:
                            break;
                    }
                    Toast.makeText(context, "Login Successfully", Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    @Override
    public void onPostFail(int method, String response) {
        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
    }

}

