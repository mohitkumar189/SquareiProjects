package com.app.anmolenterprise.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.anmolenterprise.R;
import com.app.anmolenterprise.aynctask.CommonAsyncTaskHashmap;
import com.app.anmolenterprise.interfaces.ApiResponse;
import com.app.anmolenterprise.utils.AppUtils;

import org.json.JSONObject;

public class Feedback extends AppCompatActivity implements ApiResponse {


    private Context context;
    private EditText edt_name, edt_email, edt_address, edt_mobileno;
    private Toolbar toolbar;
    private Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        context = this;

        init();
        setListener();

    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Feedback");
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

    public void updateProfile() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                String params = "?mobile=" + AppUtils.getUserMobile(context) + "&name=" + edt_name.getText().toString() + "&comment=" + edt_address.getText().toString()
                        + "&email=" + edt_email.getText().toString() + "&user_id=" + AppUtils.getUserId(context);

                String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.sendFeedback);
                new CommonAsyncTaskHashmap(1, context, this).getquery(url + params);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean isValidLoginDetails() {
        boolean isValidLoginDetails = true;

        if (!edt_email.getText().toString().equalsIgnoreCase("")
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
                Toast.makeText(context, "Please enter comment", Toast.LENGTH_SHORT).show();
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostFail(int method, String response) {

    }
}
