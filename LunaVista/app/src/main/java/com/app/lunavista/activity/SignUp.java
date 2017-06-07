package com.app.lunavista.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.lunavista.AppUtils.AppUtils;
import com.app.lunavista.R;
import com.app.lunavista.asyntask.AsyncPostDataResponse;
import com.app.lunavista.interfaces.ListenerPostData;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Logiguyz on 7/10/2016.
 */
public class SignUp extends AppCompatActivity implements ListenerPostData {

    Context context;
    EditText edtUsername, edtPassword, edtRePassword, edtEmail, edtMobile, edtLocation;
    RelativeLayout rlSignUp;
    String social_type;
    boolean isSocial = false;
    View viewcnfrmpassword, viewpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        context = this;
        init();
        Intent in = getIntent();
        if (in.hasExtra("social_type")) {

            isSocial = true;
            Log.e("name", "**" + in.getExtras().getString("name"));
            edtUsername.setText(in.getExtras().getString("name"));
            edtEmail.setText(in.getExtras().getString("email"));
            social_type = in.getExtras().getString("social_type");
            edtPassword.setText(in.getExtras().getString("password"));
            edtRePassword.setText(in.getExtras().getString("password"));
            edtPassword.setVisibility(View.GONE);
            viewpassword.setVisibility(View.GONE);
            viewcnfrmpassword.setVisibility(View.GONE);
            edtRePassword.setVisibility(View.GONE);

        }
        setListener();
    }

    private void init() {
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtRePassword = (EditText) findViewById(R.id.edtRePassword);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtMobile = (EditText) findViewById(R.id.edtMobile);
        edtLocation = (EditText) findViewById(R.id.edtLocation);
        viewpassword = findViewById(R.id.viewpassword);
        viewcnfrmpassword = findViewById(R.id.viewcnfrmpassword);
        rlSignUp = (RelativeLayout) findViewById(R.id.rlSignUp);


    }

    private void setListener() {

        rlSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = "", password = "", email = "", repassword = "", location = "", mobile = "";
                username = edtUsername.getText().toString();
                password = edtPassword.getText().toString();
                repassword = edtRePassword.getText().toString();
                email = edtEmail.getText().toString();
                mobile = edtMobile.getText().toString();
                location = edtLocation.getText().toString();

                if (username.length() > 0 && password.length() > 0 && repassword.length() > 0 && mobile.length() > 0 && location.length() > 0 && email.length() > 0) {

                    if (!AppUtils.isEmailValid(email)) {
                        Toast.makeText(context, "Please input valid email.", Toast.LENGTH_LONG).show();
                    } else if (!password.equalsIgnoreCase(repassword)) {
                        Toast.makeText(context, "Password and confirm password are not same.", Toast.LENGTH_LONG).show();
                    } else {


                        if (isSocial) {

                            ArrayList<NameValuePair> requestData = new ArrayList<NameValuePair>(2);
                            requestData.add(new BasicNameValuePair(
                                    "name", username));
                            requestData.add(new BasicNameValuePair(
                                    "mobile", mobile));
                            requestData.add(new BasicNameValuePair(
                                    "location", location));
                            requestData.add(new BasicNameValuePair(
                                    "social_id", social_type));
                            requestData.add(new BasicNameValuePair(
                                    "email", email));
                            requestData.add(new BasicNameValuePair(
                                    "password", password));
                            requestData.add(new BasicNameValuePair(
                                    "latitude", "28.21"));
                            requestData.add(new BasicNameValuePair(
                                    "longitude", "78.54"));
                            requestData.add(new BasicNameValuePair(
                                    "device_type", "Android"));
                            requestData.add(new BasicNameValuePair(
                                    "device_token", "nnxsnsknksxkmxskmxsksxk"));
                            new AsyncPostDataResponse(context, 2, requestData, getString(R.string.url_base)
                                    + getString(R.string.url_registerSocial));


                        } else {
                            ArrayList<NameValuePair> requestData = new ArrayList<NameValuePair>(2);
                            requestData.add(new BasicNameValuePair(
                                    "name", username));
                            requestData.add(new BasicNameValuePair(
                                    "mobile", mobile));
                            requestData.add(new BasicNameValuePair(
                                    "location", location));

                            requestData.add(new BasicNameValuePair(
                                    "email", email));
                            requestData.add(new BasicNameValuePair(
                                    "password", password));
                            requestData.add(new BasicNameValuePair(
                                    "latitude", "28.21"));
                            requestData.add(new BasicNameValuePair(
                                    "longitude", "78.54"));
                            requestData.add(new BasicNameValuePair(
                                    "device_type", "Android"));
                            requestData.add(new BasicNameValuePair(
                                    "device_token", "nnxsnsknksxkmxskmxsksxk"));
                            new AsyncPostDataResponse(context, 1, requestData, getString(R.string.url_base) + getString(R.string.url_register));

                        }


                    }
                } else {
                    Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_LONG).show();
                }


            }
        });


    }

    @Override
    public void onPostRequestSucess(int position, String response) {

        if (position == 1) {
            try {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    JSONObject data = commandResult.getJSONObject("data");
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (position == 2) {
            try {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    JSONObject data = commandResult.getJSONObject("data");
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onPostRequestFailed(int position, String response) {
        Toast.makeText(context, "Problem to connect server, please try again.", Toast.LENGTH_LONG).show();
    }
}
