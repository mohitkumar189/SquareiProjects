package com.app.justclap.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ImageView;

import com.app.justclap.CommonUtilities;
import com.app.justclap.R;
import com.app.justclap.WakeLocker;
import com.app.justclap.vendordetail.Vendor_Lead_dashboardBidirectional;
import com.google.android.gcm.GCMRegistrar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.app.justclap.asyntask.AsyncPostDataResponseNoProgress;
import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.vendordetail.Vendor_Lead_dashboard;

public class Splash extends Activity implements ListenerPostData {

    Context context;
    AlertDialog alert;
    private static int SPLASH_TIME_OUT = 1000;
    ConnectionDetector cd;
    ImageView img_logo;


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_splash);

        context = this;
        img_logo = (ImageView) findViewById(R.id.img_logo);

//        TranslateAnimation animation3 = new TranslateAnimation(0.0f, 0.0f,
//                400.0f, 0.0f);
//        animation3.setDuration(2000);
//        animation3.setRepeatCount(0);
//        animation3.setRepeatMode(0);
//        animation3.setFillAfter(true);
//        img_logo.startAnimation(animation3);



        getData();

        //============================================ Gcm Code for UDID generation


        try {

            // for gcm condition check
            // =================================================

            cd = new ConnectionDetector(getApplicationContext());

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                // Internet Connection is not present
            /*
             * alert.showAlertDialog(Home.this, "Internet Connection Error",
			 * "Please connect to working Internet connection", false);
			 */
                // stop executing code by return
                return;
            }

            // Check if GCM configuration is set
            if (CommonUtilities.SERVER_URL == null
                    || CommonUtilities.SENDER_ID == null
                    || CommonUtilities.SERVER_URL.length() == 0
                    || CommonUtilities.SENDER_ID.length() == 0) {
                // GCM sernder id / server url is missing
            /*
             * alert.showAlertDialog(Home.this, "Configuration Error!",
			 * "Please set your Server URL and GCM Sender ID", false);
			 */
                // stop executing code by return
                return;
            }

            // ==================================================================================================

            // Check if user filled the form
            if ("sdjhjs".trim().length() > 0 && "sdfsd".trim().length() > 0) {

                // Registering user on our server
                // Sending registraiton details to MainActivity

                // Make sure the device has the proper dependencies.
                GCMRegistrar.checkDevice(this);

                // Make sure the manifest was properly set - comment out this line
                // while developing the app, then uncomment it when it's ready.
                GCMRegistrar.checkManifest(this);

                // lblMessage = (TextView) findViewById(R.id.textView1);

                registerReceiver(mHandleMessageReceiver, new IntentFilter(
                        CommonUtilities.DISPLAY_MESSAGE_ACTION));

                // Get GCM registration id
                final String regId = GCMRegistrar.getRegistrationId(this);

                Log.e("regId", regId);

                // Check if regid already presents
                if (regId.equals("")) {
                    // Registration is not present, register now with GCM
                    GCMRegistrar.register(this, CommonUtilities.SENDER_ID);
                } else {
                    // Device is already registered on GCM
                    if (GCMRegistrar.isRegisteredOnServer(this)) {
                        // Skips registration.

					/*
                     * Toast.makeText(getApplicationContext(),
					 * "Already registered with GCM", Toast.LENGTH_LONG)
					 * .show();
					 */
                    }
                }

            } else {
                // user doen't filled that data
                // ask him to fill the form
            /*
             * alert.showAlertDialog(Home.this, "Registration Error!",
			 * "Please enter your details", false);
			 */
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }


        //=======================================End of GCM Key generation.....=========================


    }


    private void getData() {

        try {

            // for gcm condition check
            // =================================================

            cd = new ConnectionDetector(getApplicationContext());

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present

                AppUtils.showCustomAlert(Splash.this, getResources().getString(R.string.message_network_problem));

                // stop executing code by return
                return;
            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);

               /* nameValuePairs
                        .add(new BasicNameValuePair(
                                "cityID", "67"));
*/
                new AsyncPostDataResponseNoProgress(Splash.this, 1, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.homeData));
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
                    JSONObject maindata = commandResult.getJSONObject("data");

                    //   JSONArray cities = maindata.getJSONArray("cities");
                    JSONArray categories = maindata.getJSONArray("categories");
                    JSONArray newoffer = maindata.getJSONArray("newoffer");
                    AppUtils.setcategories(context, categories.toString());
                    AppUtils.setNewOffers(context, newoffer.toString());
                    AppUtils.setSkills(context, maindata.getJSONArray("skills").toString());
                    //   AppUtils.setcities(context, cities.toString());

                    AppUtils.setCategoryJsondata(context, maindata.toString());


                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            SharedPreferences pref = getSharedPreferences("new", MODE_PRIVATE);
                            boolean isDeactivated = pref.getBoolean("Deactivate", false);
                            if (isDeactivated) {

                                if (!AppUtils.getUserId(context).equalsIgnoreCase("")) {
                                    Intent i1 = new Intent(Splash.this, DashBoardActivity.class);
                                    startActivity(i1);
                                    finish();
                                } else if (!AppUtils.getvendorId(context).equalsIgnoreCase("")) {
                                    if (AppUtils.getisunidirectional(context).equalsIgnoreCase("2")) {

                                        Intent intent = new Intent(Splash.this, Vendor_Lead_dashboardBidirectional.class);
                                        startActivity(intent);
                                        finish();

                                    } else if (AppUtils.getisunidirectional(context).equalsIgnoreCase("1")) {
                                        Intent intent = new Intent(Splash.this, Vendor_Lead_dashboard.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                } else {

                                    Intent i1 = new Intent(Splash.this, DashBoardActivity.class);
                                    startActivity(i1);
                                    finish();
                                }

                            } else {

                                Intent i1 = new Intent(Splash.this, ActivityHome.class);
                                startActivity(i1);
                                finish();

                            }
                        }
                    }, SPLASH_TIME_OUT);


                } else {

                    AppUtils.showCustomAlert(Splash.this,
                            "Data not Found");
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }




    @Override
    public void onPostRequestFailed(int position, String response) {

        AppUtils.showCustomAlert(Splash.this, getResources().getString(R.string.problem_server));
        finish();

    }


    /**
     * Receiving push messages
     */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(
                    CommonUtilities.EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());

            /**
             * Take appropriate action on this message depending upon your app
             * requirement For now i am just displaying it on the screen
             * */

            // Showing received message
            // lblMessage.append(newMessage + "\n");
            /*Toast.makeText(getApplicationContext(),
                    "New Message: " + newMessage, Toast.LENGTH_LONG).show();*/

            // Releasing wake lock
            WakeLocker.release();
        }
    };

    @Override
    protected void onDestroy() {

        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegis Receiver Error", "> " + e.getMessage());
        }

        super.onDestroy();
    }
}
