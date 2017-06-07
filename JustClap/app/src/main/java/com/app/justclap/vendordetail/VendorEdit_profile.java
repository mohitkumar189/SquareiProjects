package com.app.justclap.vendordetail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.justclap.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.app.justclap.asyntask.AsyncPostDataResponse;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.utils.AppUtils;

public class VendorEdit_profile extends AppCompatActivity implements ListenerPostData {


    RelativeLayout rl_bottom;
    Context context;
    EditText edit_emailaddress, text_About, edit_name, edit_mobileno, text_relevant, text_professional;
    Toolbar toolbar;
    private BroadcastReceiver broadcastReceiver;
    RelativeLayout rl_main_layout, rl_network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_edit_profile);


        context = this;
        init();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");
        Intent in = getIntent();
        edit_name.setText(in.getExtras().getString("name"));
        edit_emailaddress.setText(in.getExtras().getString("email"));
        text_About.setText(in.getExtras().getString("about"));
        edit_mobileno.setText(in.getExtras().getString("phone"));
        text_relevant.setText(in.getExtras().getString("relavent_experience"));
        text_professional.setText(in.getExtras().getString("professional_experience"));

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
        rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        text_relevant = (EditText) findViewById(R.id.text_relevant);
        text_professional = (EditText) findViewById(R.id.text_professional);
        edit_emailaddress = (EditText) findViewById(R.id.text_emailaddress);
        text_About = (EditText) findViewById(R.id.text_About);
        edit_name = (EditText) findViewById(R.id.text_name);
        edit_mobileno = (EditText) findViewById(R.id.text_mobile);

        rl_main_layout = (RelativeLayout) findViewById(R.id.rl_main_layout);
        rl_network = (RelativeLayout) findViewById(R.id.rl_network);

    }

    @Override
    protected void onPause() {
        super.onPause();

        edit_name.setText(edit_name.getText().toString());
        edit_emailaddress.setText(edit_emailaddress.getText().toString());
        text_About.setText(text_About.getText().toString());
        edit_mobileno.setText(edit_mobileno.getText().toString());
        text_relevant.setText(text_relevant.getText().toString());
        text_professional.setText(text_professional.getText().toString());


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

        rl_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isValidLoginDetails()) {
                    if (AppUtils.isNetworkAvailable(context)) {

                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                                2);


                        nameValuePairs
                                .add(new BasicNameValuePair(
                                        "name", edit_name.getText().toString()));
                        nameValuePairs
                                .add(new BasicNameValuePair(
                                        "email", edit_emailaddress.getText().toString()));

                        nameValuePairs
                                .add(new BasicNameValuePair(
                                        "phone", edit_mobileno.getText().toString()));

                        nameValuePairs
                                .add(new BasicNameValuePair(
                                        "about", text_About.getText().toString()));

                        nameValuePairs
                                .add(new BasicNameValuePair(
                                        "experience", text_relevant.getText().toString()));

                        nameValuePairs
                                .add(new BasicNameValuePair(
                                        "professional", text_professional.getText().toString()));

                        nameValuePairs
                                .add(new BasicNameValuePair(
                                        "vendorID", AppUtils.getvendorId(context)));

                        new AsyncPostDataResponse(VendorEdit_profile.this, 1, nameValuePairs,
                                getString(R.string.url_base_new)
                                        + getString(R.string.updateVendorPersonalDetails));


                    } else {

                        rl_main_layout.setVisibility(View.GONE);
                        rl_network.setVisibility(View.VISIBLE);

                        //   Toast.makeText(getApplicationContext(), R.string.message_network_problem, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }



    private boolean isValidLoginDetails() {
        boolean isValidLoginDetails = true;

        String emailaddress = edit_emailaddress.getText().toString();
        String about = text_About.getText().toString();
        String first_name = edit_name.getText().toString();

        String mobileno = edit_mobileno.getText().toString();


        if (!first_name.equalsIgnoreCase("") && !emailaddress.equalsIgnoreCase("")
                && !mobileno.equalsIgnoreCase("")
                ) {

            if (!AppUtils.isEmailValid(emailaddress.trim())) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.validEmail, Toast.LENGTH_SHORT).show();
            } else if (mobileno.length() < 10) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.mobileno_Length, Toast.LENGTH_SHORT).show();
            } else {
                isValidLoginDetails = true;
            }

        } else {
            if (first_name.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.enterFirstName, Toast.LENGTH_SHORT).show();
            } else if (emailaddress.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.enterEmail, Toast.LENGTH_SHORT).show();
            } else if (mobileno.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.enterMobileno, Toast.LENGTH_SHORT).show();
            }
        }

        return isValidLoginDetails;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);
    }

    @Override
    public void onPostRequestSucess(int position, String response) {

        try {

            if (position == 1) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {


                    JSONObject data = commandResult.getJSONObject("data");
                    Toast.makeText(context, commandResult.getString("message"),
                            Toast.LENGTH_SHORT).show();
                    Intent in = new Intent();
                    in.putExtra("name", edit_name.getText().toString());
                    in.putExtra("email", edit_emailaddress.getText().toString());
                    in.putExtra("phone", edit_mobileno.getText().toString());
                    in.putExtra("about", text_About.getText().toString());
                    in.putExtra("professional_experience", text_professional.getText().toString());
                    in.putExtra("relavent_experience", text_relevant.getText().toString());
                    setResult(513, in);
                    finish();
                } else {
                    Toast.makeText(context, commandResult.getString("message"),
                            Toast.LENGTH_SHORT).show();

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostRequestFailed(int position, String response) {
        AppUtils.showCustomAlert(VendorEdit_profile.this, getResources().getString(R.string.problem_server));
    }
}
