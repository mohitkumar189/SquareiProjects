package com.app.anmolenterprise.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.anmolenterprise.R;
import com.app.anmolenterprise.aynctask.CommonAsyncTaskHashmap;
import com.app.anmolenterprise.interfaces.ApiResponse;
import com.app.anmolenterprise.utils.AppUtils;
import com.app.anmolenterprise.utils.Constant;
import com.app.anmolenterprise.utils.GPSTracker;

import org.json.JSONObject;

public class Login extends AppCompatActivity implements ApiResponse {

    private Context context;
    private EditText edt_mobileno;
    private Button btn_login;
    private TextView text_login;
    private Toolbar toolbar;
    GPSTracker gTraker;
    double mLat, mLong;
    String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, android.Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    int PERMISSION_ALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        init();
        setListener();

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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        edt_mobileno = (EditText) findViewById(R.id.edt_mobileno);
        btn_login = (Button) findViewById(R.id.btn_login);
        text_login = (TextView) findViewById(R.id.text_login);
    }

    /*
        * manage click listener of all views
        */
    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edt_mobileno.getText().toString().equalsIgnoreCase("") && edt_mobileno.getText().toString().length() >= 10) {
                    loginUser();
                } else {

                    Toast.makeText(context, "Please enter valid mobile no.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        gTraker = new GPSTracker(context);

        if (gTraker.canGetLocation()) {

            mLat = gTraker.getLatitude();
            mLong = gTraker.getLongitude();

            Log.e("mLat", "" + mLat);
            Log.e("mLong", "" + mLong);

        } else {
            showSettingsAlert();
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


    public void loginUser() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                String params = "?mobile=" + edt_mobileno.getText().toString() + "&latitude=" + mLat + "&longitude=" + mLong
                        + "&deviceType=" + Constant.DEVICETYPE + "&deviceToken=" + "add";

                String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.loginRegister);
                new CommonAsyncTaskHashmap(1, context, this).getquery(url + params);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {

        try {
            if (method == 1) {
                JSONObject commandResult = response.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    AppUtils.setUserId(context, data.getString("UserId"));
                    AppUtils.setUserMobile(context, data.getString("UserPhone"));
                    AppUtils.setUserRole(context, data.getString("UserRole"));
                    if (data.has("Service")) {
                        AppUtils.setService(context, data.getString("Service"));
                    }
                    if (AppUtils.getUserRole(context).equalsIgnoreCase("2")) {
                        Intent in = new Intent(getApplicationContext(), UserDashboard.class);
                        startActivity(in);
                        finish();
                    } else {
                        Intent in = new Intent(getApplicationContext(), VendorDashboard.class);
                        startActivity(in);
                        finish();
                    }

                } else {

                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostFail(int method, String response) {

    }
}
