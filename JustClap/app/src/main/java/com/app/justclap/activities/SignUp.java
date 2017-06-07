package com.app.justclap.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.app.justclap.utils.GPSTracker;

public class SignUp extends Activity implements ListenerPostData {

    private BroadcastReceiver broadcastReceiver;
    EditText edit_emailaddress, edit_password, edit_first_name, edit_mobileno;
    Context context;
    CheckBox checkbox_terms;
    Spinner select_gender;
    TextView text_new_account, text_terms;
    RelativeLayout rl_signIn;
    RelativeLayout rl_main_layout, rl_network;
    String[] spinner_item = {"Select Gender", "Male", "Female"};
    ArrayAdapter<String> spinner_adapter;
    GPSTracker gTraker;
    String social_type = "0", password = "";
    double mLat, mLong;
    TextInputLayout input_layout_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_sign_up);

        context = this;
        init();
        Intent in = getIntent();
        if (in.hasExtra("social_type")) {

            Log.e("name", "**" + in.getExtras().getString("name"));
            edit_first_name.setText(in.getExtras().getString("name"));
            edit_emailaddress.setText(in.getExtras().getString("email"));
            social_type = in.getExtras().getString("social_type");
            password = in.getExtras().getString("password");
            edit_password.setText(in.getExtras().getString("password"));
            edit_password.setVisibility(View.GONE);
            input_layout_password.setVisibility(View.GONE);

        }
        if (gTraker.canGetLocation) {

            mLat = gTraker.getLatitude();
            mLong = gTraker.getLongitude();

        }

        clickableText();
        setListener();

        spinner_adapter = new ArrayAdapter<String>(context, R.layout.row_customspinner_new, R.id.text_spinner, spinner_item);
        select_gender.setAdapter(spinner_adapter);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }

    private void clickableText() {
        SpannableString ss = new SpannableString("I agree with terms and condition and privacy policy");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                // startActivity(new Intent(SignUp.this, Login.class));

                Intent in = new Intent(SignUp.this, TermsAndconditions.class);
                startActivity(in);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent in = new Intent(SignUp.this, PrivacyPolicy.class);
                startActivity(in);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };

        ss.setSpan(clickableSpan, 13, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpan1, 37, 51, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text_terms.setText(ss);
        text_terms.setMovementMethod(LinkMovementMethod.getInstance());
        text_terms.setHighlightColor(Color.TRANSPARENT);

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
        gTraker = new GPSTracker(context);
        checkbox_terms = (CheckBox) findViewById(R.id.checkbox_terms);
        edit_emailaddress = (EditText) findViewById(R.id.text_emailaddress);
        edit_password = (EditText) findViewById(R.id.text_password);
        edit_first_name = (EditText) findViewById(R.id.text_name);
        edit_mobileno = (EditText) findViewById(R.id.text_mobile);
        select_gender = (Spinner) findViewById(R.id.text_gender);
        rl_signIn = (RelativeLayout) findViewById(R.id.rl_signIn);
        text_new_account = (TextView) findViewById(R.id.text_new_account);
        text_terms = (TextView) findViewById(R.id.text_terms);
        input_layout_password = (TextInputLayout) findViewById(R.id.input_layout_password);
    }

    private void setListener() {

        rl_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidLoginDetails()) {

                    if (AppUtils.isNetworkAvailable(context)) {
                        try {
                            rl_main_layout.setVisibility(View.VISIBLE);
                            rl_network.setVisibility(View.GONE);

                            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                                    2);

                           /* nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "t","4fbf76c28cc7e510eff27d01b511ac78"));
                            nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "ts", "ad12787c58fa9ab1ea06ad1a45de0798"));*/

                            nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "email", edit_emailaddress
                                            .getText()
                                            .toString()));

                            nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "name", edit_first_name
                                            .getText()
                                            .toString()));

                            nameValuePairs.add(new BasicNameValuePair("socialType", social_type));

                            nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "mobile", edit_mobileno
                                            .getText()
                                            .toString()));


                            nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "password", edit_password
                                            .getText()
                                            .toString()));

                            nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "gender", select_gender.getSelectedItem().toString()));

                            nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "deviceType", "1"));


                            String token = "";
                            if (AppUtils.getGcmRegistrationKey(context).equalsIgnoreCase("")) {
                                token = "abcd";
                            } else {
                                token = AppUtils.getGcmRegistrationKey(context);
                            }

                            nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "deviceToken", token));
                            nameValuePairs.add(new BasicNameValuePair(
                                    "latitude", "" + mLat));

                            nameValuePairs.add(new BasicNameValuePair(
                                    "longitude", "" + mLong));


                            new AsyncPostDataResponse(
                                    SignUp.this,
                                    1,
                                    nameValuePairs,
                                    getString(R.string.url_base_new)
                                            + getString(R.string.signUpUser));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        rl_main_layout.setVisibility(View.GONE);
                        rl_network.setVisibility(View.VISIBLE);
                      /*  Toast.makeText(context,
                                getString(R.string.message_network_problem),
                                Toast.LENGTH_SHORT).show();*/
                    }

                }
            }
        });

        text_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(SignUp.this, Login.class);
                startActivity(in);

            }
        });
    }

    private boolean isValidLoginDetails() {
        boolean isValidLoginDetails = true;

        String emailaddress = edit_emailaddress.getText().toString();
        String password = edit_password.getText().toString();
        String first_name = edit_first_name.getText().toString();
        String gender = select_gender.getSelectedItem().toString();
        String mobileno = edit_mobileno.getText().toString();


        if (!emailaddress.equalsIgnoreCase("") && !first_name.equalsIgnoreCase("")
                && !mobileno.equalsIgnoreCase("") && !password.equalsIgnoreCase("")
                && !gender.equalsIgnoreCase("") && checkbox_terms.isChecked()) {

            if (!AppUtils.isEmailValid(emailaddress.trim())) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.validEmail, Toast.LENGTH_SHORT).show();
            } else if (mobileno.length() < 10) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.mobileno_Length, Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.passwordLength, Toast.LENGTH_SHORT).show();
            } else if (select_gender.getSelectedItemPosition() == 0) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), "Select your gender", Toast.LENGTH_SHORT).show();
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
            } else if (password.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.enterPassword, Toast.LENGTH_SHORT).show();
            } else if (gender.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.entergender, Toast.LENGTH_SHORT).show();
            } else if (!checkbox_terms.isChecked()) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), "Agree to our terms and conditions.", Toast.LENGTH_SHORT).show();
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
                    AppUtils.setOtpUserId(context, data.getString("userID"));
                    AppUtils.setUserName(context, data.getString("name"));
                    AppUtils.setUserMobile(context, data.getString("mobile"));
                    AppUtils.setUseremail(context, data.getString("email"));
                    if (!data.getString("profileImage").equalsIgnoreCase("")) {
                        AppUtils.setUserImage(context, getResources().getString(R.string.img_url) + data.getString("profileImage"));
                    } else {
                        AppUtils.setUserImage(context, "");

                    }
                    Log.e("customer_id", AppUtils.getUserId(context));
                    //  setCartdata();
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                    sendBroadcast(broadcastIntent);

                    Intent in = new Intent(SignUp.this, OtpVerificationActivity.class);
                    in.putExtra("mobileno", data.getString("mobile"));
                    startActivity(in);
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
        AppUtils.showCustomAlert(SignUp.this, getResources().getString(R.string.problem_server));
    }
}
