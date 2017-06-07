package com.app.justclap.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.R;
import com.app.justclap.aynctask.CommonAsyncTaskHashmap;
import com.app.justclap.interfaces.ApiResponse;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.utils.GPSTracker;
import com.app.justclap.utils.MyConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ActivateMobileNumber extends AppCompatActivity implements ApiResponse {

    private Context context;
    private EditText edt_mobileno;
    private ImageView img_next;
    private TextView get_otp, text_activate_phone_number, text_resendotp;
    boolean isotpScreen = false;
    private double mLat, mLong;
    private GPSTracker gTraker;
    private String userId = "";
    private String mobileno = "";
    private int PERMISSION_ALL = 1;
    private String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS, Manifest.permission.CALL_PHONE};
    private BroadcastReceiver broadcastReceiver1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_mobile_number);
        context = this;
        init();
        setListener();
    }

    private void registerSmsReciever() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.jc.otprecieved");

        broadcastReceiver1 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive", "Sms recieved in progress");

                edt_mobileno.setText(intent.getStringExtra("otp"));
                edt_mobileno.setSelection(edt_mobileno.getText().length());
            }
        };
        registerReceiver(broadcastReceiver1, intentFilter);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (broadcastReceiver1 != null) {
            unregisterReceiver(broadcastReceiver1);
        }
    }

 /*   @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver1 != null) {
            try {
                unregisterReceiver(broadcastReceiver1);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        registerSmsReciever();
        gTraker = new GPSTracker(context);

        if (gTraker.canGetLocation()) {

            mLat = gTraker.getLatitude();
            mLong = gTraker.getLongitude();

            Log.e("mLat", "" + mLat);
            Log.e("mLong", "" + mLong);

        } else {
            showSettingsAlert();
            // getTrainingList();
        }

    }

    public void showSettingsAlert() {
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

            // Setting Dialog Title
            alertDialog.setTitle("GPS is settings");

            // Setting Dialog Message
            alertDialog
                    .setMessage("GPS is not enabled. Do you want to go to settings menu?");

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);

                        }
                    });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    });

            // Showing Alert Message
            alertDialog.show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void setListener() {

        img_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isotpScreen) {
                    if (!edt_mobileno.getText().toString().equalsIgnoreCase("")) {
                        verifyOtp();
                    } else {
                        Toast.makeText(context, "Please enter Otp.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (!edt_mobileno.getText().toString().equalsIgnoreCase("")) {
                        mobileno = edt_mobileno.getText().toString();
                        loginUser();
                    } else {
                        Toast.makeText(context, "Please enter mobile number.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        text_resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendOtp();
            }
        });
    }

    /*
      * for Login user
      */
    private void loginUser() {
        try {

            if (AppUtils.isNetworkAvailable(context)) {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("mobile", edt_mobileno.getText().toString());
                hm.put("latitude", mLat + "");
                hm.put("longitude", mLong + "");
                hm.put("deviceType", MyConstant.DEVICETYPE);
                hm.put("deviceToken", AppUtils.getGcmRegistrationKey(context));

                String url = getResources().getString(R.string.url_base_new) + getResources().getString(R.string.loginuser);
                new CommonAsyncTaskHashmap(1, context, this).getqueryJson(url, hm);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }

        } catch (
                Exception e)

        {
            e.printStackTrace();
        }

    }

    /*
    * Resend user
    */
    private void resendOtp() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                HashMap<String, String> hm = new HashMap<>();
                hm.put("mobile", mobileno);

                String url = getResources().getString(R.string.url_base_new) + getResources().getString(R.string.resendOtp);
                new CommonAsyncTaskHashmap(3, context, this).getqueryJson(url, hm);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
   * verify user
   */
    private void verifyOtp() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                HashMap<String, String> hm = new HashMap<>();
                hm.put("userId", userId);
                hm.put("otpCode", edt_mobileno.getText().toString());
                hm.put("deviceType", MyConstant.DEVICETYPE);
                hm.put("deviceToken", AppUtils.getGcmRegistrationKey(context));
                hm.put("mobile", mobileno);
                String url = getResources().getString(R.string.url_base_new) + getResources().getString(R.string.verifyOtp);
                new CommonAsyncTaskHashmap(2, context, this).getqueryJson(url, hm);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }

        } catch (
                Exception e)

        {
            e.printStackTrace();
        }

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    private void init() {
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        edt_mobileno = (EditText) findViewById(R.id.edt_mobileno);
        img_next = (ImageView) findViewById(R.id.img_next);
        get_otp = (TextView) findViewById(R.id.get_otp);
        text_resendotp = (TextView) findViewById(R.id.text_resendotp);
        text_activate_phone_number = (TextView) findViewById(R.id.text_activate_phone_number);
    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {
        try {
            try {
                if (method == 1) {

                    JSONObject commandResult = response
                            .getJSONObject("commandResult");
                    if (commandResult.getString("success").equalsIgnoreCase("1")) {
                        JSONObject maindata = commandResult.getJSONObject("data");
                        JSONObject userDetails = maindata.getJSONObject("UserDetails");
                        isotpScreen = true;
                        text_activate_phone_number.setText("Enter otp");
                        get_otp.setText("");

                        text_resendotp.setVisibility(View.VISIBLE);
                        edt_mobileno.setText("");
                        edt_mobileno.setHint(". . . . . .");
                        userId = userDetails.getString("Id");
                  /*
                    "UserDetails":{
                        "UserId":"1119",
                                "OtpCode":"024871"
                    }*/

                      /*  Intent in = new Intent(context, UserDashboard.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(in);
                        finish();*/
                    } else {
                        Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();

                    }
                } else if (method == 2) {
                    JSONObject commandResult = response
                            .getJSONObject("commandResult");
                    if (commandResult.getString("success").equalsIgnoreCase("1")) {
                        JSONObject maindata = commandResult.getJSONObject("data");
                        JSONObject userDetails = maindata.getJSONObject("UserDetails");
                        AppUtils.setUserId(context, userDetails.getString("Id"));
                        AppUtils.setUserName(context, userDetails.getString("Name"));
                        AppUtils.setUserMobile(context, userDetails.getString("Mobile"));
                        AppUtils.setUseremail(context, userDetails.getString("Email"));
                        AppUtils.setOtherData(context, userDetails.getString("Gender"), MyConstant.GENDER);
                        AppUtils.setOtherData(context, userDetails.getString("IsSubscribed"), MyConstant.ISSUBSCRIBED);
                        AppUtils.setOtherData(context, userDetails.getString("IsPremium"), MyConstant.ISPREMIUM);
                        AppUtils.setOtherData(context, userDetails.getString("IsRegistered"), MyConstant.ISREGISTERED);
                        AppUtils.setOtherData(context, userDetails.getString("LoggedAs"), MyConstant.LOGGEDAS);
                        AppUtils.setOtherData(context, userDetails.getString("DeviceId"), MyConstant.DEVICEID);
                        AppUtils.setOtherData(context, userDetails.getString("UserUniqueCode"), MyConstant.UNIQUECODE);
                        AppUtils.setOtherData(context, userDetails.getString("ProfileImage"), MyConstant.PROFILEIMAGE);

                        Intent in = new Intent(context, UserDashboard.class);
                    //    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(in);
                        finish();
                    }else {
                        Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();

                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        } catch (
                Exception e)

        {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostFail(int method, String response) {
        Toast.makeText(context, getResources().getString(R.string.problem_server), Toast.LENGTH_SHORT).show();
    }
}
