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
import android.widget.Toast;

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

public class ChangePassword extends AppCompatActivity implements ListenerPostData {

    EditText old_password, new_password, confirm_password;
    Context context;
    Button Change_password;
    ConnectionDetector cd;
    Toolbar toolbar;
    private BroadcastReceiver broadcastReceiver;
    RelativeLayout rl_main_layout, rl_network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        context = this;
        init();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Change Password");
        cd = new ConnectionDetector(getApplicationContext());

        setListener();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }

    private void init() {
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        old_password = (EditText) findViewById(R.id.old_password);
        new_password = (EditText) findViewById(R.id.new_password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        Change_password = (Button) findViewById(R.id.btn_changePassword);
        rl_main_layout = (RelativeLayout) findViewById(R.id.rl_main_layout);
        rl_network = (RelativeLayout) findViewById(R.id.rl_network);

    }

    private void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO bind to drawer with... injection?
                finish();
                overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);

            }

        });


        Change_password.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (cd.isConnectingToInternet()) {

                    if (isValidLoginDetails()) {
                        try {

                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("oldPassword",
                                    old_password.getText().toString()));
                            params.add(new BasicNameValuePair("password",
                                    new_password.getText().toString()));
                            params.add(new BasicNameValuePair("email",
                                    AppUtils.getUseremail(context)));

                            String url = getString(R.string.url_base_new)
                                    + getString(R.string.changePassword);
                            new AsyncPostDataResponse(context, 1, params, url);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
//
                } else {
                    rl_main_layout.setVisibility(View.GONE);
                    rl_network.setVisibility(View.VISIBLE);


                  /*  AppUtils.showCustomAlert(ChangePassword.this,
                            getResources().getString(R.string.message_network_problem));*/
                }

            }

        });


       /* showPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub

                if (isChecked) {
                    old_password.setInputType(InputType.TYPE_CLASS_TEXT);
                    new_password.setInputType(InputType.TYPE_CLASS_TEXT);
                    confirm_password.setInputType(InputType.TYPE_CLASS_TEXT);

                } else {
                    old_password.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    new_password.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    confirm_password.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }

            }
        });*/

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);
    }

    private boolean isValidLoginDetails() {
        boolean isValidLoginDetails = true;

        if (!old_password.getText().toString().equalsIgnoreCase("")
                && !new_password.getText().toString().equalsIgnoreCase("")
                && !confirm_password.getText().toString().equalsIgnoreCase("")) {

            if (!new_password.getText().toString()
                    .equals(confirm_password.getText().toString())) {
                isValidLoginDetails = false;
                // Toast.makeText(getApplicationContext(),
                // "Password should not match", Toast.LENGTH_SHORT).show();

                AppUtils.showCustomAlert(ChangePassword.this,
                        "Password mismatch!!");
            } else {
                isValidLoginDetails = true;
            }

        } else {
            if (old_password.getText().toString().equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                /*
                 * Toast.makeText(getApplicationContext(),
				 * "Please enter old password.", Toast.LENGTH_SHORT) .show();
				 */
                AppUtils.showCustomAlert(ChangePassword.this,
                        "Please enter old password.");

            } else if (new_password.getText().toString().equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                /*
                 * Toast.makeText(getApplicationContext(),
				 * "Please enter new password.", Toast.LENGTH_SHORT) .show();
				 */

                AppUtils.showCustomAlert(ChangePassword.this,
                        "Please enter new password.");
            } else if (confirm_password.getText().toString()
                    .equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(),
                        "Please enter password again.", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        return isValidLoginDetails;
    }

    @Override
    public void onPostRequestSucess(int method, String response) {

        try {

            if (method == 1) {
                Log.e("resp", response);

                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {


                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);
                    AppUtils.showCustomAlert(ChangePassword.this,
                            commandResult.getString("message"));
                    finish();

                } else {

                    AppUtils.showCustomAlert(ChangePassword.this,
                            commandResult.getString("message"));
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onPostRequestFailed(int method, String message) {
        // TODO Auto-generated method stub
        Log.e("Error", "***" + message);

        AppUtils.showCustomAlert(ChangePassword.this, getResources().getString(R.string.problem_server));
    }

}
