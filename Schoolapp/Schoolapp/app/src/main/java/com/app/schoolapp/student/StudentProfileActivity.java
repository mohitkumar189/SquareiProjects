package com.app.schoolapp.student;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.schoolapp.R;
import com.app.schoolapp.asynctask.CommonAsyncTaskHashmap;
import com.app.schoolapp.interfaces.ApiResponse;
import com.app.schoolapp.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class StudentProfileActivity extends AppCompatActivity implements ApiResponse, View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private Context context;
    private String fname;
    private String lname;
    private String email;
    private String mobile;
    private String altNo;
    private String address;
    private String dob;
    private String gender;
    private String currentsession;
    private String bloodgroup;
    private String fathername;
    private String stusection;
    private String stuclass;
    private String rollno;
    private String pic;
    private String fblink;
    private String twitterlink;
    private String googlelink;


    private EditText edt_name, last_name, edt_email, edt_mobilenumber, edt_alter_number, edt_adress,
            edt_dob, edt_education, edt_spous_name, edt_spouse_qual, edt_facebook, edt_twitter, edt_google;
    private Button btnSubmit;
    private RadioGroup radiogrp;
    private RadioButton radio_male, radio_female;
    private TextView tvClass;
    private ImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        context = this;
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {

                initViews();
            }
        });
        thread1.start();
        getUserProfile();

    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edt_name = (EditText) findViewById(R.id.edt_name);
        last_name = (EditText) findViewById(R.id.last_name);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_mobilenumber = (EditText) findViewById(R.id.edt_mobilenumber);
        edt_alter_number = (EditText) findViewById(R.id.edt_alter_number);
        edt_adress = (EditText) findViewById(R.id.edt_adress);
        edt_dob = (EditText) findViewById(R.id.edt_dob);
        edt_education = (EditText) findViewById(R.id.edt_education);
        edt_spous_name = (EditText) findViewById(R.id.edt_spous_name);
        edt_spouse_qual = (EditText) findViewById(R.id.edt_spouse_qual);
        edt_facebook = (EditText) findViewById(R.id.edt_facebook);
        edt_twitter = (EditText) findViewById(R.id.edt_twitter);
        edt_google = (EditText) findViewById(R.id.edt_google);
        radiogrp = (RadioGroup) findViewById(R.id.radiogrp);
        radio_male = (RadioButton) findViewById(R.id.radio_male);
        radio_female = (RadioButton) findViewById(R.id.radio_female);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        tvClass = (TextView) findViewById(R.id.tvClass);
        profile_image = (ImageView) findViewById(R.id.profile_image);
        btnSubmit.setOnClickListener(this);
        radiogrp.setOnCheckedChangeListener(this);
    }

    private void getUserProfile() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.get_profile) + "?client_id=" + AppUtils.getUserId(context);
                new CommonAsyncTaskHashmap(1, context, this).getquery(url);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setData() {
        edt_name.setText(fname);
        last_name.setText(lname);
        edt_email.setText(email);
        edt_mobilenumber.setText(mobile);
        edt_alter_number.setText(altNo);
        edt_dob.setText(dob);
        edt_adress.setText(address);

        switch (gender) {
            case "Male":
                radiogrp.check(R.id.radio_male);
                break;
            case "Female":
                radiogrp.check(R.id.radio_female);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSubmit)
            sendProfile();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (checkedId == R.id.radio_male) {
            gender = "Male";
        } else if (checkedId == R.id.radio_female) {
            gender = "Female";
        }
    }

    @Override
    public void onPostSuccess(int method, final JSONObject response) {

        try {
            int success = response.getInt("success");
            int error = response.getInt("error");
            String message = response.getString("message");
            if (success == 1 && error == 0) {
                JSONObject data = response.getJSONObject("data");
                if (data != null && data.length() > 0) {
                    fname = data.getString("firstname");
                    lname = data.getString("lastname");
                    email = data.getString("email");
                    mobile = data.getString("phone");
                    stuclass = data.getString("stuclass");
                    stusection = data.getString("stusection");
                    rollno = data.getString("rollno");
                    fathername = data.getString("fathername");
                    address = data.getString("address");
                    bloodgroup = data.getString("bloodgroup");
                    currentsession = data.getString("currentsession");
                    pic = data.getString("pic");
                    dob = data.getString("dob");
                    gender = data.getString("gender");
                    //fblink=data.getString("social_link");

                    setData();
                }
            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostFail(int method, String response) {

    }

    private void sendProfile() {

        fname = edt_name.getText().toString();
        lname = last_name.getText().toString();
        email = edt_email.getText().toString();
        mobile = edt_mobilenumber.getText().toString();
        altNo = edt_alter_number.getText().toString();
        address = edt_adress.getText().toString();
        dob = edt_dob.getText().toString();
        fblink = edt_facebook.getText().toString();
        twitterlink = edt_twitter.getText().toString();
        googlelink = edt_google.getText().toString();

        try {
            if (AppUtils.isNetworkAvailable(context)) {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("FirstName", fname);
                hm.put("LastName", lname);
                hm.put("Email", email);
                hm.put("Phone", mobile);
                hm.put("AlternetPhone", altNo);
                //    hm.put("AadharNo", altNo);
                //     hm.put("Photo", altNo);
                hm.put("Address", address);
                hm.put("DOB", dob);
                hm.put("Gender", gender);
                hm.put("SocialLinks", "public");
                hm.put("client_id", AppUtils.getUserId(context));
                // hm.put("appname", "school");
                //       http://squarei.in/api/v2/login?email=anderson.soyug@gmail.com&password=4105
                String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.get_profile);
                new CommonAsyncTaskHashmap(1, context, this).getqueryJson(url, 1, hm);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
