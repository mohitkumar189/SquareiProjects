package com.app.justclap.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.justclap.R;
import com.app.justclap.asyntask.AsyncPostDataResponse;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.utils.AppUtils;

public class AboutUs extends AppCompatActivity implements ListenerPostData {


    TextView text_terms;
    Context context;
    Toolbar toolbar;
    ConnectionDetector cd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        context = this;
        init();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("About Us");
        //    getTerms();

        text_terms.setText("JustClap is a fastest growing mobile based service marketplace startup in India. We help customers hire trusted professional into different business segments. Home Services, Naukriz, Travel, Healthcare, Personal and Matrimonial are the major mobility solution JustClap introduce on single platform. We are a single solution application design for all your needs.");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);
                finish();



            }
        });

    }

    private void init() {
        overridePendingTransition(R.anim.enter,
                R.anim.exit);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        text_terms = (TextView) findViewById(R.id.text_terms);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);
    }
    private void getTerms() {

        try {


            cd = new ConnectionDetector(context);

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present


                AppUtils.showCustomAlert(AboutUs.this, getResources().getString(R.string.message_network_problem));

                // stop executing code by return
                return;

            } else {


                new AsyncPostDataResponse(AboutUs.this, 1, null,
                        getString(R.string.url_base_new)
                                + getString(R.string.getTermsCond));
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

                    JSONObject data = commandResult.getJSONObject("data");

                    text_terms.setText(Html.fromHtml(data.getString("termCond")));

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onPostRequestFailed(int position, String response) {

    }
}
