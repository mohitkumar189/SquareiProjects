package com.app.justclap.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.app.justclap.R;

/**
 * Created by Logiguyz on 1/13/2016.
 */
public class ActivityRatingReviewSuccess extends AppCompatActivity {

    Button btn_discover;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_reviewsuccess);

        overridePendingTransition(R.anim.enter,
                R.anim.exit);

        btn_discover = (Button) findViewById(R.id.btn_discover);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rating & Review");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        btn_discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                sendBroadcast(broadcastIntent);
                Intent iintent = new Intent(ActivityRatingReviewSuccess.this, DashBoardActivity.class);
                startActivity(iintent);
                overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);
                finish();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                sendBroadcast(broadcastIntent);
                Intent iintent = new Intent(ActivityRatingReviewSuccess.this, DashBoardActivity.class);
                startActivity(iintent);
                overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);
                finish();


            }
        });

    }

    @Override
    public void onBackPressed() {

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.package.ACTION_LOGOUT");
        sendBroadcast(broadcastIntent);
        Intent iintent = new Intent(ActivityRatingReviewSuccess.this, DashBoardActivity.class);
        startActivity(iintent);
        overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);
        finish();

    }
}
