package com.app.justclap.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.asyntask.AsyncPostDataResponse;
import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.utils.AppUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Logiguyz on 1/13/2016.
 */
public class ActivityRating extends AppCompatActivity implements ListenerPostData {
    ImageView image_smile;
    TextView text_ratingtype;
    RatingBar ratingbar;
    EditText edtAddress_edtcomment;
    Button submit;
    String service_id = "", vendor_id = "", searchId = "";
    Toolbar toolbar;
    ConnectionDetector cd;
    Context context;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_commentvendor);

        context = this;
        overridePendingTransition(R.anim.enter,
                R.anim.exit);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive", "Logout in progress");
                //At this point you should start the login activity and finish this one
                finish();
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rating & Review");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent in = getIntent();
        service_id = in.getExtras().getString("service_id");
        vendor_id = in.getExtras().getString("vendor_id");
        searchId = in.getExtras().getString("searchId");
        setupui();
        ratingbar.setRating(5);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }

    private void setupui() {
        submit = (Button) findViewById(R.id.submit);
        text_ratingtype = (TextView) findViewById(R.id.text_ratingtype);
        image_smile = (ImageView) findViewById(R.id.image_smile);
        ratingbar = (RatingBar) findViewById(R.id.ratingbar);
        edtAddress_edtcomment = (EditText) findViewById(R.id.edtAddress_edtcomment);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtAddress_edtcomment.getText().toString().equalsIgnoreCase("")) {

                    AppUtils.showCustomAlert(ActivityRating.this, "Please give your review");

                } else {
                    submitRating();

                }
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO bind to drawer with... injection?
                finish();
                overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);

            }

        });

        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                Log.e("rating", "*" + rating);
                Log.e("Math.floor(rating)", "*" + Math.floor(rating));
                if (rating == 1 || rating < 1) {
                    image_smile.setImageResource(R.drawable.very_bad);
                    text_ratingtype.setText("Very Dissatisfied");
                }
                if (rating > 1 || rating == 2) {
                    image_smile.setImageResource(R.drawable.bad);
                    text_ratingtype.setText("Dissatisfied");
                }
                if (rating > 2 || rating == 3) {
                    image_smile.setImageResource(R.drawable.okay);
                    text_ratingtype.setText("Neutral");
                }
                if (rating > 3 || rating == 4) {
                    image_smile.setImageResource(R.drawable.good_job);
                    text_ratingtype.setText("Satisfied");
                }
                if (rating > 4 || rating == 5) {
                    image_smile.setImageResource(R.drawable.excellent_job);
                    text_ratingtype.setText("Very Satisfied");
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);
    }

    private void submitRating() {

        try {

            // for gcm condition check
            // =================================================

            cd = new com.app.justclap.interfaces.ConnectionDetector(context);
            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present

                AppUtils.showCustomAlert(ActivityRating.this, getResources().getString(R.string.message_network_problem));

                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "userID", AppUtils.getUserId(context)));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "vendorID", vendor_id));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "serviceID", service_id));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "searchId", searchId));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "reviewRating", ratingbar.getRating() + ""));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "reviewComment", edtAddress_edtcomment.getText().toString()));


                new AsyncPostDataResponse(ActivityRating.this, 1, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.setReview));
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

                    Intent iintent = new Intent(ActivityRating.this, ActivityRatingReviewSuccess.class);
                    startActivity(iintent);
                    finish();

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onPostRequestFailed(int position, String response) {
        AppUtils.showCustomAlert(ActivityRating.this, getResources().getString(R.string.problem_server));
    }
}
