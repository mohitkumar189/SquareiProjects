package com.app.anmolenterprise.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.anmolenterprise.R;
import com.app.anmolenterprise.aynctask.CommonAsyncTaskHashmap;
import com.app.anmolenterprise.interfaces.ApiResponse;
import com.app.anmolenterprise.utils.AppUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class UpdateProfile extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ApiResponse {


    private Context context;
    private EditText edt_name, edt_email, edt_address, edt_mobileno;
    private Toolbar toolbar;
    private Button btn_submit;
    private String lat = "0.0", lng = "0.0";
    private String formatted_address = "";
    private Location mLastLocation;
    private static final int PERMISSIONS_REQUEST_LOCATION = 100;
    private LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;
    private double mLat = 0.0;
    private double mLong = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        context = this;

        init();
        setListener();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        googleapi();
        edt_mobileno.setText(AppUtils.getUserMobile(context));
        getProfile();

    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Update Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_address = (EditText) findViewById(R.id.edt_address);
        edt_mobileno = (EditText) findViewById(R.id.edt_mobileno);
        btn_submit = (Button) findViewById(R.id.btn_submit);
    }

    private void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidLoginDetails()) {
                    updateProfile();
                }
            }
        });

    }

    public void getProfile() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                String params = "?user_id=" + AppUtils.getUserId(context);
                String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.getProfile);
                new CommonAsyncTaskHashmap(2, context, this).getquery(url + params);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateProfile() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                String params = "?mobile=" + edt_mobileno.getText().toString() + "&name=" + edt_name.getText().toString() + "&address=" + edt_address.getText().toString()
                        + "&email=" + edt_email.getText().toString() + "&user_id=" + AppUtils.getUserId(context);

                params = params.replace(" ", "%20");
                String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.updateProfile);
                new CommonAsyncTaskHashmap(1, context, this).getquery(url + params);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void googleapi() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }


    @SuppressLint("NewApi")
    private void connection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
            } else {
                con();
            }
        } else
            con();
    }

    private void con() {
        Log.e("", "");
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.e("", "");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            // startLocationUpdate();
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            Log.e("mLastLocation", "**" + mLastLocation);
            if (mLastLocation != null) {
                mLat = mLastLocation.getLatitude();
                mLong = mLastLocation.getLongitude();

                Log.e("latitude", "**" + mLat + "*" + mLong);

                address(mLat, mLong);
                if (String.valueOf(mLat) != null) {
                    //     startLocationUpdate();

                } else {
                    connection();

                }
            }
        } else {
            showGPSDisabledAlertToUser();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        con();
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    connection();
                } else {
                    Toast.makeText(this, "Sorry!!! Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        connection();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void address(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses.size() > 0) {
                String address;
                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                if (address.contains("Unnamed")) {
                    address = addresses.get(1).getAddressLine(0);
                }
                String city = addresses.get(0).getLocality();
                String sublocality = addresses.get(0).getSubLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();

                String newaddress = "";
                if (address == null)
                    newaddress = city + ", " + state;
                else if (city == null)
                    newaddress = address + ", " + state;
                else if (state == null)
                    newaddress = address + ", " + city;
                else if (address == null && state == null)
                    newaddress = city;
                else if (city == null && state == null)
                    newaddress = address;
                else
                    newaddress = knownName + ", " + sublocality + ", " + city;

                //newaddress = knownName + ", "+sublocality + ", " + city + ", " + state;
                String noSpaces = newaddress.replaceAll("\0", " ");
                formatted_address = noSpaces;
                edt_address.setText(formatted_address);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidLoginDetails() {
        boolean isValidLoginDetails = true;

        if (!edt_mobileno.getText().toString().equalsIgnoreCase("")
                && !edt_email.getText().toString().equalsIgnoreCase("")
                && !edt_address.getText().toString().equalsIgnoreCase("") && !edt_name.getText().toString().equalsIgnoreCase("")) {


            isValidLoginDetails = true;
        } else {
            if (edt_name.getText().toString().equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(context, getResources().getString(R.string.enter_name), Toast.LENGTH_SHORT).show();

            } else if (edt_email.getText().toString().equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(context, getResources().getString(R.string.enter_email), Toast.LENGTH_SHORT).show();

            } else if (edt_address.getText().toString().equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(context, getResources().getString(R.string.enter_address), Toast.LENGTH_SHORT).show();

            } else if (edt_mobileno.getText().toString().equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(context, getResources().getString(R.string.enter_mobile), Toast.LENGTH_SHORT).show();

            }
        }

        return isValidLoginDetails;
    }


    @Override
    public void onPostSuccess(int method, JSONObject response) {
        try {
            if (method == 1) {
                JSONObject commandResult = response.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");

                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                    finish();
                } else {

                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }

            } else if (method == 2) {
                JSONObject commandResult = response.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    JSONObject User = data.getJSONObject("User");

                    edt_address.setText(User.getString("Address"));
                    edt_name.setText(User.getString("FullName"));
                    edt_email.setText(User.getString("Email"));
                    edt_mobileno.setText(User.getString("Mobile"));
                    AppUtils.setUserName(context, User.getString("FullName"));
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
