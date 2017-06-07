package com.app.anmolenterprise.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.anmolenterprise.R;
import com.app.anmolenterprise.utils.AppUtils;

public class VendorDashboard extends AppCompatActivity {

    private Context context;
    private Toolbar toolbar;
    private RelativeLayout rl_comletedleads, rl_feedback, rl_newleads;
    private ImageView image_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_dashboard);
        context = this;

        init();
        setListener();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        rl_newleads = (RelativeLayout) findViewById(R.id.rl_newleads);
        image_logout = (ImageView) findViewById(R.id.image_logout);
        rl_comletedleads = (RelativeLayout) findViewById(R.id.rl_comletedleads);
        rl_feedback = (RelativeLayout) findViewById(R.id.rl_feedback);
    }

    private void setListener() {
        rl_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, Feedback.class);
                startActivity(in);
            }
        });
        rl_comletedleads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(context, CompletedLeads.class);
                startActivity(in);
            }
        });
        rl_newleads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(context, NewLeads.class);
                startActivity(in);
            }
        });

        image_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showLogoutBox();
            }
        });
    }

    private void showLogoutBox() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                VendorDashboard.this);

        alertDialog.setTitle("LOG OUT !");

        alertDialog.setMessage("Are you sure you want to Logout?");

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        AppUtils.setUserId(context, "");
                        Intent intent = new Intent(context, Login.class);
                        startActivity(intent);
                        finish();
                    }

                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

        alertDialog.show();


    }

}
