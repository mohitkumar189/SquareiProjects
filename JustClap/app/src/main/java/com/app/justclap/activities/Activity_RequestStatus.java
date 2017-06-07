package com.app.justclap.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.justclap.R;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.app.justclap.asyntask.AsyncPostDataResponse;
import com.app.justclap.interfaces.*;
import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.utils.AppUtils;

public class Activity_RequestStatus extends AppCompatActivity implements ListenerPostData, OnCustomItemClicListener {


    com.app.justclap.interfaces.ConnectionDetector cd;
    Context context;
    TextView text_service_name, text_service, text_scheduled_time, text_requestId,text_scheduled, text_viewDetail, text_cancelrequest;
    ImageView img_back, image_background;
    String service_id = "", service_name = "", pageFlag = "0", time = "",requestId="", reason = "", searchId = "";
    private BroadcastReceiver broadcastReceiver;
    RelativeLayout rl_main_layout, rl_network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activity__request_status);

        context = this;
        init();

        Intent in = getIntent();
        if (in.hasExtra("service_id")) {
            service_id = in.getExtras().getString("service_id");
            service_name = in.getExtras().getString("service_name");
            time = in.getExtras().getString("time");

        }

        requestId = in.getExtras().getString("requestId");
        Log.e("requestId","*"+requestId);
        if (in.hasExtra("pageFlag")) {
            pageFlag = in.getExtras().getString("pageFlag");
        }

        if (in.hasExtra("searchId")) {

            searchId = in.getStringExtra("searchId");
        }
        if (in.hasExtra("service_image")) {

            Picasso.with(context).load(in.getExtras().getString("service_image"))
                    .into(image_background);
        }
        setListener();
        text_requestId.setText(requestId);
        text_service_name.setText(service_name);
        text_service.setText("Your " + service_name + " request has been confirmed. You will get responses very soon.");
        text_scheduled_time.setText(time + "");
        Log.e("timeslot", time + "*" + text_scheduled_time.getText().toString());


        if (text_scheduled_time.getText().toString().equalsIgnoreCase("") || time.equalsIgnoreCase("") || text_scheduled_time.getText().toString().equalsIgnoreCase("null")) {
            text_scheduled.setVisibility(View.GONE);
        } else {
            text_scheduled.setVisibility(View.VISIBLE);
        }

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

        rl_main_layout = (RelativeLayout) findViewById(R.id.rl_main_layout);
        rl_network = (RelativeLayout) findViewById(R.id.rl_network);

        image_background = (ImageView) findViewById(R.id.image_background);
        img_back = (ImageView) findViewById(R.id.img_back);
        text_service_name = (TextView) findViewById(R.id.text_service_name);
        text_service = (TextView) findViewById(R.id.text_service);
        text_scheduled_time = (TextView) findViewById(R.id.text_scheduled_time);
        text_scheduled = (TextView) findViewById(R.id.text_scheduled);
        text_requestId = (TextView) findViewById(R.id.text_requestId);
        text_viewDetail = (TextView) findViewById(R.id.text_viewDetail);
        text_cancelrequest = (TextView) findViewById(R.id.text_cancelrequest);


    }

    private void setListener() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO bind to drawer with... injection?

                if (pageFlag.equalsIgnoreCase("1")) {
                    Intent in = new Intent(Activity_RequestStatus.this, DashBoardActivity.class);
                    startActivity(in);
                    finish();
                    overridePendingTransition(R.anim.left_to_right,
                            R.anim.right_to_left);
                } else {

                    finish();
                    overridePendingTransition(R.anim.left_to_right,
                            R.anim.right_to_left);
                }


            }

        });

        text_viewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(Activity_RequestStatus.this, ViewDetailAnswers.class);
                in.putExtra("service_id", service_id);
                in.putExtra("service_name", service_name);
                in.putExtra("searchId", searchId);
                startActivity(in);
            }
        });

        text_cancelrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showConfirmtion();
            }
        });


    }


    private void showConfirmtion() {

        final CharSequence[] items = {"Due to my personal reason", "I don't like responses", "Submit by mistake"
        };
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);

        alertDialog.setTitle("Cancel Request");

        // alertDialog.setMessage("Are you sure you want to cancel this request?");

        alertDialog.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Due to my personal reason")) {

                    reason = "0";

                } else if (items[item].equals("I don't like responses")) {

                    reason = "1";

                } else if (items[item].equals("Submit by mistake")) {

                    reason = "2";
                }


            }
        });


        alertDialog.setPositiveButton("CONFIRM",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (reason.equalsIgnoreCase("")) {
                            AppUtils.showCustomAlert(Activity_RequestStatus.this, "Please select reason for cancel request");
                        } else {
                            deleteService();
                            dialog.cancel();
                        }

                        Log.e("clickedPosiion", which + "");

                    }

                });

        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

        alertDialog.show();


    }

    private void deleteService() {

        try {

            // for gcm condition check
            // =================================================

            cd = new ConnectionDetector(context);

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present
                //  AppUtils.showCustomAlert(Activity_RequestStatus.this, getResources().getString(R.string.message_network_problem));
                rl_main_layout.setVisibility(View.GONE);
                rl_network.setVisibility(View.VISIBLE);
                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "userID", AppUtils.getUserId(context)));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "serviceID", service_id));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "reason", reason));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "searchId", searchId));


                new AsyncPostDataResponse(Activity_RequestStatus.this, 3, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.deleteCustomerService));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e("pageFlag", "*" + pageFlag);
        if (pageFlag.equalsIgnoreCase("1")) {
            Intent in = new Intent(Activity_RequestStatus.this, DashBoardActivity.class);
            startActivity(in);
            finish();
            overridePendingTransition(R.anim.left_to_right,
                    R.anim.right_to_left);
        } else {

            finish();
            overridePendingTransition(R.anim.left_to_right,
                    R.anim.right_to_left);
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
                    AppUtils.showCustomAlert(Activity_RequestStatus.this, commandResult.getString("message"));

                } else {

                    AppUtils.showCustomAlert(Activity_RequestStatus.this, commandResult.getString("message"));

                }


            } else if (position == 3) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    AppUtils.showCustomAlert(Activity_RequestStatus.this, commandResult.getString("message"));
                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);

                    Intent in = new Intent(context, DashBoardActivity.class);
                    startActivity(in);
                    finish();

                } else {
                    AppUtils.showCustomAlert(Activity_RequestStatus.this, commandResult.getString("message"));
                }


            }
        } catch (JSONException e) {

            e.printStackTrace();
        }


    }

    @Override
    public void onPostRequestFailed(int position, String response) {

        AppUtils.showCustomAlert(Activity_RequestStatus.this, getResources().getString(R.string.problem_server));
    }

    @Override
    public void onItemClickListener(int position, int flag) {


    }
}
