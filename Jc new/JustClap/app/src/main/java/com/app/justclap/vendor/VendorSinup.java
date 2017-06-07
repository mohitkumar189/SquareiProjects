package com.app.justclap.vendor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.R;
import com.app.justclap.activities.PickLocation;
import com.app.justclap.activities.PrivacyPolicy;
import com.app.justclap.activities.SelectServiceList;
import com.app.justclap.activities.TermsAndconditions;
import com.app.justclap.aynctask.CommonAsyncTaskAquery;
import com.app.justclap.interfaces.ApiResponse;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.model.ModelService;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.utils.GPSTracker;
import com.app.justclap.utils.MyConstant;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

public class VendorSinup extends AppCompatActivity implements OnCustomItemClicListener, ApiResponse {

    Context context;
    Button btnLocation;
    TextView text_seekbarprogress, text_terms, text_select_category, text_login;
    ModelService serviceDetail;
    ArrayList<String> services_array = new ArrayList<>();
    ArrayList<String> service_id = new ArrayList<>();
    ArrayList<String> is_uni_directional = new ArrayList<>();
    SeekBar seekBar;
    ArrayList<String> service_name = new ArrayList<>();
    ArrayList<String> product_adapter = new ArrayList<>();
    CheckBox checkbox_terms;
    boolean checkCity = false;
    EditText edit_emailaddress, edit_password, edit_name, edit_mobileno,
            edit_business_name;
    String user_type = "1", latitude = "0.0", longitude = "0.0", selecredService = "", selectedCategory = "";
    String lat = "0.0", lng = "0.0";
    private BroadcastReceiver broadcastReceiver;
    private Button btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_sinup);
        context = this;
        init();
        // setServices();

        clickableText();
        setListener();
        GPSTracker gps = new GPSTracker(context);
        if (gps.canGetLocation) {
            latitude = "" + gps.getLatitude();
            longitude = "" + gps.getLongitude();
        }

        setCurrentLocation();
        text_seekbarprogress.setText(seekBar.getProgress() + " Km");

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
        text_terms = (TextView) findViewById(R.id.text_terms);
        text_select_category = (TextView) findViewById(R.id.text_select_category);
        text_seekbarprogress = (TextView) findViewById(R.id.text_seekbarprogress);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        btnLocation = (Button) findViewById(R.id.btn_pickLocation);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        checkbox_terms = (CheckBox) findViewById(R.id.checkbox_terms);
        edit_emailaddress = (EditText) findViewById(R.id.text_emailaddress);
        edit_password = (EditText) findViewById(R.id.text_password);
        edit_name = (EditText) findViewById(R.id.text_name);
        edit_mobileno = (EditText) findViewById(R.id.text_mobile);
        edit_business_name = (EditText) findViewById(R.id.text_business_name);
        text_login = (TextView) findViewById(R.id.text_login);

        AppUtils.fontGotham_Medium(text_login, context);
        AppUtils.fontGotham_edit(edit_emailaddress, context);
        AppUtils.fontGotham_edit(edit_password, context);
        AppUtils.fontGotham_edit(edit_mobileno, context);
        AppUtils.fontGotham_edit(edit_name, context);
        AppUtils.fontGotham_edit(edit_business_name, context);

    }


/*
    private void setServices() {

        try {
            if (AppUtils.isNetworkAvailable(context)) {
                new AsyncPostDataResponse(
                        VendorSignUp.this,
                        2,
                        null,
                        getString(R.string.url_base_new)
                                + getString(R.string.vendorSignupData));
            } else {

                rl_main_layout.setVisibility(View.GONE);
                rl_network.setVisibility(View.VISIBLE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
*/

    private void setListener() {
        btnLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                GPSTracker gps = new GPSTracker(context);

                if (gps.isGPSEnabled) {

                    Intent i = new Intent(context, PickLocation.class);
                    i.putExtra("lat", latitude);
                    i.putExtra("lng", longitude);
                    startActivityForResult(i, 511);

                } else {
                    gps.showSettingsAlert();
                }
            }
        });

        text_select_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, SelectServiceList.class);

                startActivityForResult(in, 21);
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValidLoginDetails()) {
                    if (AppUtils.isNetworkAvailable(context)) {
                        try {

                            HashMap<String, Object> hm = new HashMap<>();
                            hm.put("userId", AppUtils.getUserId(context));
                            hm.put("name", edit_name.getText().toString());
                            hm.put("email", edit_emailaddress.getText().toString());
                            hm.put("businessName", edit_business_name.getText().toString());
                            hm.put("mobile", edit_mobileno.getText().toString());
                            hm.put("isVendor", "1");
                            hm.put("deviceType", MyConstant.DEVICETYPE);
                            hm.put("deviceToken", AppUtils.getGcmRegistrationKey(context));
                            hm.put("latitude", latitude);
                            hm.put("longitude", longitude);
                            hm.put("address", btnLocation.getText().toString());
                            hm.put("distanceCovered", text_seekbarprogress.getText().toString());


                            String url = getResources().getString(R.string.url_base_new) + getResources().getString(R.string.updateProfile);
                            new CommonAsyncTaskAquery(1, context, VendorSinup.this).getquery(url, hm);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {


                        Toast.makeText(context,
                                getString(R.string.message_network_problem),
                                Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                text_seekbarprogress.setText(progress + " Km");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    private void clickableText() {
        SpannableString ss = new SpannableString("I agree with terms and condition and privacy policy");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent in = new Intent(VendorSinup.this, TermsAndconditions.class);
                startActivity(in);
                // startActivity(new Intent(VendorSignUp.this, Login.class));
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
                Intent in = new Intent(VendorSinup.this, PrivacyPolicy.class);
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

    private boolean isValidLoginDetails() {
        boolean isValidLoginDetails = true;

        String emailaddress = edit_emailaddress.getText().toString();
        String password = edit_password.getText().toString();
        String first_name = edit_name.getText().toString();

        String mobileno = edit_mobileno.getText().toString();
        String businessname = edit_business_name.getText().toString();


        if (!first_name.equalsIgnoreCase("") && !emailaddress.equalsIgnoreCase("")
                && !mobileno.equalsIgnoreCase("") && !password.equalsIgnoreCase("")
                && !businessname.equalsIgnoreCase("") && checkbox_terms.isChecked()) {

            if (!AppUtils.isEmailValid(emailaddress.trim())) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.validEmail, Toast.LENGTH_SHORT).show();
            } else if (mobileno.length() < 10) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.mobileno_Length, Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.passwordLength, Toast.LENGTH_SHORT).show();
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
            } else if (businessname.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.enter_business, Toast.LENGTH_SHORT).show();
            } else if (!checkbox_terms.isChecked()) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), "Agree to our terms and conditions.", Toast.LENGTH_SHORT).show();
            }
        }

        return isValidLoginDetails;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            Log.e("resultCode", "**" + resultCode);
            if (requestCode == 511 && resultCode == 512) {

                btnLocation.setText(data.getStringExtra("location"));
                latitude = data.getStringExtra("latitude");
                longitude = data.getStringExtra("longitude");
            } else if (requestCode == 21 && resultCode == 511) {

                Log.e("Biselectedservies", AppUtils.getBiSelectedServices(context));
            } else if (resultCode == 512) {
                Log.e("Uniselectedservies", AppUtils.getUniSelectedServices(context));

            }
        } catch (Exception e) {
            // Toast.makeText(getBaseContext(), "Exception "+e.getMessage(),
            // 5000).show();
            e.printStackTrace();
        }
    }

    private void setCurrentLocation() {

        // TODO Auto-generated method stub
        GPSTracker gps = new GPSTracker(context);
        if (gps.canGetLocation) {
            lat = "" + gps.getLatitude();
            lng = "" + gps.getLongitude();

            GetAddressFromURLTask1 task1 = new GetAddressFromURLTask1();
            task1.execute(new String[]{lat, lng});

        } else {
            /*Toast.makeText(context, "Could not found lat long",
                    Toast.LENGTH_LONG).show();*/
        }

    }

    @Override
    public void onItemClickListener(int position, int flag) {

    }

    @Override
    public void onPostSuccess(int method, JSONObject jObject) {

        try {

            if (method == 1) {

                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    Toast.makeText(context, commandResult.getString("message"),
                            Toast.LENGTH_SHORT).show();

                    JSONObject data = commandResult.getJSONObject("data");
                    AppUtils.setvendorname(context, data.getString("name"));
                    AppUtils.setOtpUserId(context, data.getString("vendorID"));
                    if (!data.getString("profileImage").equalsIgnoreCase("")) {
                        AppUtils.setVendorImage(context, getResources().getString(R.string.img_url) + data.getString("profileImage"));
                    } else {
                        AppUtils.setVendorImage(context, "");
                    }
                    AppUtils.setvendormobile(context, data.getString("mobile"));
                    AppUtils.setvendorEmail(context, data.getString("email"));
                    AppUtils.setvendorWallet(context, data.getString("wallet"));
                    AppUtils.setisVip(context, data.getString("isVIP"));
                   /* JSONArray servicesArray = data.getJSONArray("services");
                    JSONObject services = servicesArray.getJSONObject(0);

                    AppUtils.setvendorServiceId(context, services.getString("serviceID"));
                    AppUtils.setisunidirectional(context, services.getString("is_uni_directional"));
*/
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                    sendBroadcast(broadcastIntent);

                 /*   Intent in = new Intent(VendorSinup.this, OtpVerificationActivityVendor.class);
                    in.putExtra("mobileno", data.getString("mobile"));
                    in.putExtra("selecredService", selecredService);
                    in.putExtra("selectedCategory", selectedCategory);
                    startActivity(in);
                    finish();*/


                } else {
                    Toast.makeText(context, commandResult.getString("message"),
                            Toast.LENGTH_SHORT).show();

                }

                if (commandResult.getString("success").equalsIgnoreCase("0")) {

                    Toast.makeText(context, commandResult.getString("message"),
                            Toast.LENGTH_SHORT).show();

                }

            }

            if (method == 2) {


                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {


                    JSONObject data = commandResult.getJSONObject("data");

                    JSONArray services = data.getJSONArray("services");

                    for (int i = 0; i < services.length(); i++) {

                        JSONObject ob = services.getJSONObject(i);

                        product_adapter.add(ob.getString("serviceName"));
                        service_id.add(ob.getString("serviceID"));
                        is_uni_directional.add(ob.getString("is_uni_directional"));

                    }
                    product_adapter.add("Select Service");

                }


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostFail(int method, String response) {

    }


    private class GetAddressFromURLTask1 extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... urls) {

            String response = "";
            HttpResponse response2 = null;
            StringBuilder stringBuilder = new StringBuilder();

            try {

                HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/geocode/json?latlng=" + urls[0] + "," + urls[1] + "&ln=en");

                HttpClient client = new DefaultHttpClient();
                Log.e("Url ", "http://maps.google.com/maps/api/geocode/json?ln=en&latlng=" + urls[0] + "," + urls[1]);


                try {
                    response2 = client.execute(httpGet);

                    HttpEntity entity = response2.getEntity();

                    char[] buffer = new char[2048];
                    Reader reader = new InputStreamReader(entity.getContent(), "UTF-8");

                    while (true) {
                        int n = reader.read(buffer);
                        if (n < 0) {
                            break;
                        }
                        stringBuilder.append(buffer, 0, n);
                    }

                    Log.e("Url response1", stringBuilder.toString());

                } catch (ClientProtocolException e) {
                } catch (IOException e) {
                }

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject = new JSONObject(stringBuilder.toString());

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (Exception e) {

                e.printStackTrace();
                Log.e("Error 2 :>>", "error in doINBackground OUTER");
                //infowindow.setText("Error in connecting to Google Server... try again later");
            }
            return stringBuilder.toString();
            //return jsonObject;
        }


        protected void onPostExecute(String result) {

            try {
                if (result != null) {
                    //result=	Html.fromHtml(result).toString();
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray resultsObject = jsonObject.getJSONArray("results");
                    JSONObject formattedAddress = (JSONObject) resultsObject.get(0);
                    String formatted_address = formattedAddress.getString("formatted_address");

                    Log.e("formatted Adss from>>", formatted_address);
                    btnLocation.setText(formatted_address);

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
