package com.app.justclap.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.app.justclap.R;
import com.app.justclap.asyntask.AsyncPostDataResponse;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.utils.AppUtils;

public class ForgotPassword extends AppCompatActivity implements ListenerPostData {
    EditText edittext_forgot;
    Button btn_forgot;
    ConnectionDetector cd;
    Toolbar toolbar;
    private BroadcastReceiver broadcastReceiver;
    RelativeLayout rl_main_layout, rl_network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        overridePendingTransition(R.anim.enter,
                R.anim.exit);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive", "Logout in progress");
                //At this point you should start the login activity and finish this one
                finish();
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
        edittext_forgot = (EditText) findViewById(R.id.edittext_forgot);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btn_forgot = (Button) findViewById(R.id.btn_forgot);
        cd = new ConnectionDetector(getApplicationContext());
        rl_main_layout = (RelativeLayout) findViewById(R.id.rl_main_layout);
        rl_network = (RelativeLayout) findViewById(R.id.rl_network);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Forgot Password");


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO bind to drawer with... injection?
                finish();
                overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);

            }

        });


        btn_forgot.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (cd.isConnectingToInternet()) {

                    if (true) {
                        // TODO Auto-generated method stub
                        if (!edittext_forgot.getText().toString()
                                .equalsIgnoreCase("")) {

                            try {

                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("email",
                                        edittext_forgot.getText()
                                                .toString()));
                                String url = getString(R.string.url_base_new)
                                        + getString(R.string.forgotPasswordVendor);
                                new AsyncPostDataResponse(
                                        ForgotPassword.this, 1, params, url);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {

							/*
                             * Toast.makeText(ForgotPassword.this,
							 * "Please input email.", Toast.LENGTH_SHORT)
							 * .show();
							 */
                            AppUtils.showCustomAlert(ForgotPassword.this,
                                    "Please enter the registered email id");
                        }
                    }
                } else {
                    /*
					 * Toast.makeText(getApplicationContext(),
					 * "No network connection", Toast.LENGTH_SHORT).show();
					 */
                    rl_main_layout.setVisibility(View.GONE);
                    rl_network.setVisibility(View.VISIBLE);


                    //AppUtils.showCustomAlert(ForgotPassword.this, getResources().getString(R.string.message_network_problem));

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }

    @Override
    public void onPostRequestSucess(int method, String response) {
        // {"input":{"email":"nitinsri511@gmail.com"},"commandResult":{"success":"1","message":
        // "Password sent to your registered email","data":{}}}
        if (method == 1) {
            try {
                Log.e("jobj", response.toString());

                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
				/*	Toast.makeText(ForgotPassword.this,
							commandResult.getString("message"),
							Toast.LENGTH_SHORT).show();*/

                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);

                    AppUtils.showCustomAlertLong(ForgotPassword.this, commandResult.getString("message"));

                    finish();
                } else {
			/*		Toast.makeText(ForgotPassword.this,
							commandResult.getString("message"),
							Toast.LENGTH_SHORT).show();*/
                    AppUtils.showCustomAlertLong(ForgotPassword.this, commandResult.getString("message"));
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        // TODO Auto-generated method stub

    }

    @Override
    public void onPostRequestFailed(int method, String response) {
        AppUtils.showCustomAlert(ForgotPassword.this, getResources().getString(R.string.problem_server));
        // TODO Auto-generated method stub

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);
    }
}
