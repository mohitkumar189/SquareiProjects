package com.app.lunavista.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.app.lunavista.R;
import com.app.lunavista.interfaces.ListenerPostData;

import org.json.JSONObject;

public class VipMembership extends AppCompatActivity implements ListenerPostData {


    Context context;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vip_membership);
        context = this;
        initialize();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setListener();


    }

    public void initialize() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);


    }

    private void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


    }


    @Override
    public void onPostRequestSucess(int position, String response) {
        try {
            if (position == 1) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
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
    public void onPostRequestFailed(int position, String response) {

    }
}
