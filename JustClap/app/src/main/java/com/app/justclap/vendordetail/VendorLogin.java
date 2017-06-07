package com.app.justclap.vendordetail;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.activities.ForgotPassword;
import com.app.justclap.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.app.justclap.asyntask.AsyncPostDataResponse;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.utils.AppUtils;

public class VendorLogin extends Activity implements ListenerPostData {


    TextView text_new_account, text_forgot_password;
    Context context;
    EditText edit_email, edit_password;
    RelativeLayout rl_signIn;
    private BroadcastReceiver broadcastReceiver;
    RelativeLayout rl_main_layout, rl_network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_login);

        context = this;
        init();
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
        edit_email = (EditText) findViewById(R.id.emailaddress);
        edit_password = (EditText) findViewById(R.id.password);
        rl_signIn = (RelativeLayout) findViewById(R.id.rl_signIn);
        rl_main_layout = (RelativeLayout) findViewById(R.id.rl_main_layout);
        rl_network = (RelativeLayout) findViewById(R.id.rl_network);


        text_forgot_password = (TextView) findViewById(R.id.text_forgot_password);
        text_new_account = (TextView) findViewById(R.id.text_new_account);

    }


    private void setListener() {

        text_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(VendorLogin.this, ForgotPassword.class);
                startActivity(intent);

            }
        });

        text_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(VendorLogin.this, VendorSignUp.class);
                startActivity(intent);

            }
        });

        rl_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidLoginDetails()) {
                    if (AppUtils.isNetworkAvailable(context)) {
                        try {

                            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                                    2);

                            nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "email", edit_email
                                            .getText()
                                            .toString()));

                            nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "password", edit_password
                                            .getText()
                                            .toString()));

                            nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "deviceType", "1"));

                            nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "deviceToken", AppUtils.getGcmRegistrationKey(context)));

                            new AsyncPostDataResponse(
                                    VendorLogin.this,
                                    1,
                                    nameValuePairs,
                                    getString(R.string.url_base_new)
                                            + getString(R.string.loginServiceProvider));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        rl_main_layout.setVisibility(View.GONE);
                        rl_network.setVisibility(View.VISIBLE);

                        /*Toast.makeText(context,
                                getString(R.string.message_network_problem),
                                Toast.LENGTH_SHORT).show();*/
                    }
                }
            }
        });

    }

    private boolean isValidLoginDetails() {
        boolean isValidLoginDetails = true;

        if (!edit_email.getText().toString().equalsIgnoreCase("") && !edit_password.getText().toString().equalsIgnoreCase("")) {

            if (!AppUtils.isEmailValid(edit_email.getText().toString().trim())) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.validEmail, Toast.LENGTH_SHORT).show();
            } else if (edit_password.getText().toString().length() < 6) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.passwordLength, Toast.LENGTH_SHORT).show();
            } else {
                isValidLoginDetails = true;
            }

        } else {
            if (edit_email.getText().toString().equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.enterEmail, Toast.LENGTH_SHORT).show();
            } else if (edit_password.getText().toString().equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.enterPassword, Toast.LENGTH_SHORT).show();
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
    public void onPostRequestSucess(int method, String response) {

        try {

            if (method == 1) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {


                    JSONObject data = commandResult.getJSONObject("data");
                    AppUtils.setvendoruserId(context, data.getString("userID"));

                    AppUtils.setvendorname(context, data.getString("name"));
                    AppUtils.setvendorId(context, data.getString("vendorID"));
                    AppUtils.setvendormobile(context, data.getString("mobile"));
                    AppUtils.setisVip(context, data.getString("isVIP"));
                    AppUtils.setvendorEmail(context, data.getString("email"));
                    AppUtils.setWalletBalance(context, data.getString("wallet"));

                    JSONArray servicesArray = data.getJSONArray("services");
                    JSONObject services = servicesArray.getJSONObject(0);

                    AppUtils.setvendorServiceId(context, services.getString("serviceID"));
                    AppUtils.setisunidirectional(context, services.getString("is_uni_directional"));

                    if (!data.getString("profileImage").equalsIgnoreCase("")) {
                        AppUtils.setVendorImage(context, getResources().getString(R.string.img_url) + data.getString("profileImage"));
                    } else {
                        AppUtils.setVendorImage(context, "");
                    }

                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                    sendBroadcast(broadcastIntent);
                    if (AppUtils.getisunidirectional(context).equalsIgnoreCase("2")) {

                        Intent in = new Intent(VendorLogin.this, Vendor_Lead_dashboardBidirectional.class);
                        setResult(512, in);
                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(in);
                        finish();
                    } else if (AppUtils.getisunidirectional(context).equalsIgnoreCase("1")) {
                        Intent in = new Intent(VendorLogin.this, Vendor_Lead_dashboard.class);
                        setResult(512, in);
                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(in);
                        finish();
                    }




                } else {
                    Toast.makeText(context, commandResult.getString("message"),
                            Toast.LENGTH_SHORT).show();

                }

                if (commandResult.getString("success").equalsIgnoreCase("0")) {

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
        AppUtils.showCustomAlert(VendorLogin.this, getResources().getString(R.string.problem_server));
    }
}
