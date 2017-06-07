package com.app.justclap.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.app.justclap.R;
import com.app.justclap.aynctask.CommonAsyncTaskHashmap;
import com.app.justclap.interfaces.ApiResponse;
import com.app.justclap.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class Splash extends AppCompatActivity implements ApiResponse {

    private Context context;
    private static int SPLASH_TIME_OUT = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        context = this;
        getData();
    }

    private void getData() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

             /*   HashMap<String, Object> hm = new HashMap<>();*/
                String url = getResources().getString(R.string.url_base_new) + getResources().getString(R.string.homeData);
                new CommonAsyncTaskHashmap(1, context, this).getqueryNoProgress(url);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostSuccess(int method, JSONObject jObject) {
        try {
            if (method == 1) {

                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    JSONObject maindata = commandResult.getJSONObject("data");
                    AppUtils.setCategoryJsondata(context, maindata.toString());
                    if (AppUtils.getUserId(context).equalsIgnoreCase("")) {
                        Intent intent = new Intent(Splash.this, ActivateMobileNumber.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(Splash.this, UserDashboard.class);
                        startActivity(intent);
                        finish();
                    }
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            SharedPreferences pref = getSharedPreferences("new", MODE_PRIVATE);
                            boolean isDeactivated = pref.getBoolean("Deactivate", false);
                            if (isDeactivated) {
                            /*    if (!AppUtils.getUserId(context).equalsIgnoreCase("")) {
                                    Intent i1 = new Intent(Splash.this, DashBoardActivity.class);
                                    Log.e("flag", flag);
                                    i1.putExtra("flag", flag);
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
                                }*/

                            } else {

                            /*    Intent i1 = new Intent(Splash.this, ActivityHome.class);
                                startActivity(i1);
                                finish();
*/
                            }
                        }
                    }, SPLASH_TIME_OUT);
                } else {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    @Override
    public void onPostFail(int method, String response) {
        Log.e("onPostFail", "*" + response);
        Toast.makeText(context, getResources().getString(R.string.problem_server), Toast.LENGTH_SHORT).show();
        finish();

    }
}
