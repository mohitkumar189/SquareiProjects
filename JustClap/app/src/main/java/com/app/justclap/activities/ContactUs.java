package com.app.justclap.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.justclap.R;
import com.app.justclap.asyntask.AsyncPostDataResponse;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.utils.AppUtils;

public class ContactUs extends AppCompatActivity implements ListenerPostData {


    TextView text_terms;
    Context context;
    Toolbar toolbar;
    ConnectionDetector cd;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);

        context = this;
        init();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Contact Us");
        //    getTerms();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);
                finish();

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);
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
        text_terms = (TextView) findViewById(R.id.text_terms);

    }

    private void getTerms() {

        try {


            cd = new ConnectionDetector(context);

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present


                AppUtils.showCustomAlert(ContactUs.this, getResources().getString(R.string.message_network_problem));

                // stop executing code by return
                return;

            } else {


                new AsyncPostDataResponse(ContactUs.this, 1, null,
                        getString(R.string.url_base_new)
                                + getString(R.string.getTermsCond));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


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

                    text_terms.setText(Html.fromHtml(data.getString("termCond")));

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onPostRequestFailed(int position, String response) {

    }
}
