package com.app.justclap.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.asyntask.AsyncPostDataFragment;
import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserProfile extends AppCompatActivity implements ListenerPostData {

    Toolbar toolbar;
    ImageView image_user, img_email, img_call, img_chat;
    TextView text_name, text_email, textmobile;
    Context context;
    String id = "", email = "", mobile = "", searchId = "", imageurl = "";
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_user_profile);

        context = this;
        init();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent in = getIntent();
        id = in.getStringExtra("user_id");
        if (in.hasExtra("searchId")) {
            searchId = in.getExtras().getString("searchId");
        }
        text_name.setText(in.getStringExtra("name"));
        text_email.setText(in.getStringExtra("email"));
        textmobile.setText("Contact: " + in.getStringExtra("mobile"));
        email = in.getStringExtra("email");
        mobile = in.getStringExtra("mobile");
        try {
            if (!in.getStringExtra("image").equalsIgnoreCase("")) {
                Picasso.with(context).load(in.getStringExtra("image"))
                        .transform(new CircleTransform())
                        .into(image_user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setListener();

        getuserprofile();

    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        image_user = (ImageView) findViewById(R.id.image_user);
        img_email = (ImageView) findViewById(R.id.img_email);
        img_call = (ImageView) findViewById(R.id.img_call);
        img_chat = (ImageView) findViewById(R.id.img_chat);
        text_name = (TextView) findViewById(R.id.text_name);
        text_email = (TextView) findViewById(R.id.text_email);
        textmobile = (TextView) findViewById(R.id.textmobile);
    }

    private void getuserprofile() {

        try {

            // for gcm condition check
            // =================================================

            cd = new com.app.justclap.interfaces.ConnectionDetector(context);

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present

                AppUtils.showCustomAlert(UserProfile.this, "Internet Connection Error, Please connect to working Internet connection");
                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "userID", id));


                new AsyncPostDataFragment(UserProfile.this, (ListenerPostData) this, 1, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.getCustomerProfile));

            }
        } catch (Exception e) {
            e.printStackTrace();

        }


    }


    private void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onBackPressed", "onBackPressed");
                setResult(21);
                finish();

            }
        });

        img_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    String uri = "tel:" + mobile.trim();
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        });

        img_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, email);
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        img_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(context, ActivityChat.class);
                in.putExtra("reciever_id", id);
                in.putExtra("name", text_name.getText().toString());
                in.putExtra("image", getResources().getString(R.string.img_url) + imageurl);
                in.putExtra("searchID", searchId);
                startActivity(in);
            }
        });

    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Log.e("onBackPressed", "onBackPressed");
            setResult(21);
            finish();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onPostRequestSucess(int position, String response) {

        try {

            if (position == 1) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {


                    JSONObject jo = commandResult.getJSONObject("data");
                    text_name.setText(jo.getString("userName"));
                    text_email.setText(jo.getString("email"));
                    textmobile.setText("Contact: " + jo.getString("phoneNo"));
                    email = jo.getString("email");
                    mobile = jo.getString("phoneNo");
                    imageurl = jo.getString("profileImage");
                    Picasso.with(context).load(getResources().getString(R.string.img_url) + jo.getString("profileImage"))
                            .transform(new CircleTransform())
                            .into(image_user);

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
