package com.app.lunavista.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.app.lunavista.R;

/**
 * Created by Logiguyz on 7/10/2016.
 */
public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences pref = getSharedPreferences("new", MODE_PRIVATE);
                boolean isDeactivated = pref.getBoolean("Deactivate", false);
                if (isDeactivated) {

                    Intent i1 = new Intent(Splash.this, Home.class);
                    startActivity(i1);
                    finish();


                } else {

                    Intent i1 = new Intent(Splash.this, Login.class);
                    startActivity(i1);
                    finish();

                }
            }
        }, 1500);
    }
}
