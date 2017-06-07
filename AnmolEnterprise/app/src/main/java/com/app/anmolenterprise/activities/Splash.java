package com.app.anmolenterprise.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.app.anmolenterprise.R;
import com.app.anmolenterprise.utils.AppUtils;

public class Splash extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        context = this;
        Log.e("gcmkey", AppUtils.getGcmRegistrationKey(context));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AppUtils.getUserId(context).equalsIgnoreCase("")) {
                    Intent in = new Intent(getApplicationContext(), Login.class);
                    startActivity(in);
                    finish();
                } else {
                    if (AppUtils.getUserRole(context).equalsIgnoreCase("2")) {
                        Intent in = new Intent(getApplicationContext(), UserDashboard.class);
                        startActivity(in);
                        finish();
                    } else {
                        Intent in = new Intent(getApplicationContext(), VendorDashboard.class);
                        startActivity(in);
                        finish();
                    }
                }
            }
        }, 1000);
    }
}
