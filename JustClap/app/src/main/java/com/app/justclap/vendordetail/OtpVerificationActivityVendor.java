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
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.R;
import com.app.justclap.activities.DashBoardActivity;
import com.app.justclap.activities.OtpVerificationActivity;
import com.app.justclap.asyntask.AsyncPostDataResponse;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.utils.AppUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OtpVerificationActivityVendor extends AppCompatActivity implements ListenerPostData {

    Toolbar toolbar;
    EditText edit_otp;
    Context context;
    private BroadcastReceiver broadcastReceiver;
    TextView text_resendsms, text_msg, text_verify_otp;
    String mobileno = "", isregisterPage = "", selecredService = "", selectedCategory = "";
    BroadcastReceiver broadcastReceiver1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        context = this;

        init();
        Intent in = getIntent();
        mobileno = in.getExtras().getString("mobileno");
        selecredService = in.getExtras().getString("selecredService");
        selectedCategory = in.getExtras().getString("selectedCategory");

      /* isregisterPage = in.getExtras().getString("isregisterPage");*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Verify " + mobileno);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        text_msg.setText("(Enter the otp below in case if we fail to detect the SMS automatically)");
        setListener();
        //requestOtp();

    }

    private void registerSmsReciever() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.f2r.otprecieved");

        broadcastReceiver1 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive", "Sms recieved in progress");

                edit_otp.setText(intent.getStringExtra("otp"));
                edit_otp.setSelection(edit_otp.getText().length());

            }
        };

        registerReceiver(broadcastReceiver1, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerSmsReciever();
    }

    private void requestOtp()

    {
        if (AppUtils.isNetworkAvailable(context)) {
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "user_id", AppUtils.getOtpUserId(context)));

                new AsyncPostDataResponse(
                        context,
                        1,
                        nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.resendOTP));


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {


            Toast.makeText(context,
                    getString(R.string.message_network_problem),
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void verifyOtp() {

        if (AppUtils.isNetworkAvailable(context)) {
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);


                nameValuePairs
                        .add(new BasicNameValuePair(
                                "user_id", AppUtils.getOtpUserId(context)));


                nameValuePairs
                        .add(new BasicNameValuePair(
                                "otp_code", edit_otp.getText().toString()));


                nameValuePairs
                        .add(new BasicNameValuePair(
                                "deviceType", "1"));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "deviceToken", AppUtils.getGcmRegistrationKey(context)));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "services", selecredService));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "categories", selectedCategory));

                new AsyncPostDataResponse(
                        context,
                        2,
                        nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.verifyOTP));


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {


            Toast.makeText(context,
                    getString(R.string.message_network_problem),
                    Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    protected void onStop() {
        super.onStop();
        if (broadcastReceiver1 != null) {
            unregisterReceiver(broadcastReceiver1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }

    private void init() {

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

        text_verify_otp = (TextView) findViewById(R.id.text_verify_otp);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        edit_otp = (EditText) findViewById(R.id.edit_otp);
        text_resendsms = (TextView) findViewById(R.id.text_resendsms);
        text_msg = (TextView) findViewById(R.id.text_msg);
    }

    private void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        text_verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit_otp.getText().toString().length() > 3) {
                    verifyOtp();
                } else {
                    Toast.makeText(context, "Please input valid code", Toast.LENGTH_LONG).show();
                }
            }
        });

        text_resendsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestOtp();
            }
        });
    }

    @Override
    public void onPostRequestSucess(int position, String response) {

        try {

            if (position == 2) {

                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    JSONObject data = commandResult.getJSONObject("data");

                    AppUtils.setvendorname(context, data.getString("name"));
                    AppUtils.setvendorId(context, data.getString("vendorID"));
                    if (!data.getString("profileImage").equalsIgnoreCase("")) {
                        AppUtils.setVendorImage(context, getResources().getString(R.string.img_url) + data.getString("profileImage"));
                    } else {
                        AppUtils.setVendorImage(context, "");
                    }
                    AppUtils.setvendormobile(context, data.getString("mobile"));
                    AppUtils.setvendorEmail(context, data.getString("email"));
                    AppUtils.setvendorWallet(context, data.getString("wallet"));
                    AppUtils.setisVip(context, data.getString("isVIP"));
                    JSONArray servicesArray = data.getJSONArray("services");
                    JSONObject services = servicesArray.getJSONObject(0);

                    AppUtils.setvendorServiceId(context, services.getString("serviceID"));
                    AppUtils.setisunidirectional(context, services.getString("is_uni_directional"));
                    AppUtils.setOtpUserId(context, "");
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                    sendBroadcast(broadcastIntent);

                    Intent in = new Intent(OtpVerificationActivityVendor.this, Vendor_homePage.class);
                    setResult(512, in);
                    //   in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                    finish();

                }

            } else if (position == 1) {

                if (position == 1) {
                    JSONObject jObject = new JSONObject(response);
                    JSONObject commandResult = jObject
                            .getJSONObject("commandResult");
                    if (commandResult.getString("success").equalsIgnoreCase("1")) {
                        //   JSONObject data = commandResult.getJSONObject("data");

                    }
                }

            }

        } catch (JSONException e)

        {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostRequestFailed(int position, String response) {

    }

}
