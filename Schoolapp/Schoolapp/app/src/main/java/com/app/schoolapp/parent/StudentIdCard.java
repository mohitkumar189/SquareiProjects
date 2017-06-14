package com.app.schoolapp.parent;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.schoolapp.R;
import com.app.schoolapp.asynctask.CommonAsyncTaskHashmap;
import com.app.schoolapp.interfaces.ApiResponse;
import com.app.schoolapp.utils.AppUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class StudentIdCard extends AppCompatActivity implements ApiResponse {

    private Context context;
    private ImageView image_student;
    private TextView student_name, student_class, student_year, edt_studentid, edt_fathername, edt_dob, edt_address, edt_phoneno,
            edt_blood_grp, text_school_address, text_quote, text_school_email, text_school_phoneno;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_id_card);

        context = this;
        init();
        setListener();
        getProfileData();
    }

    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        image_student = (ImageView) findViewById(R.id.image_student);

        student_name = (TextView) findViewById(R.id.student_name);
        student_class = (TextView) findViewById(R.id.student_class);
        student_year = (TextView) findViewById(R.id.student_year);
        edt_studentid = (TextView) findViewById(R.id.edt_studentid);
        edt_fathername = (TextView) findViewById(R.id.edt_fathername);
        edt_dob = (TextView) findViewById(R.id.edt_dob);
        edt_address = (TextView) findViewById(R.id.edt_address);
        edt_phoneno = (TextView) findViewById(R.id.edt_phoneno);
        edt_blood_grp = (TextView) findViewById(R.id.edt_blood_grp);
        text_school_address = (TextView) findViewById(R.id.text_school_address);
        text_quote = (TextView) findViewById(R.id.text_quote);
        text_school_email = (TextView) findViewById(R.id.text_school_email);
        text_school_phoneno = (TextView) findViewById(R.id.text_school_phoneno);
    }

    private void getProfileData() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.user_profile) + "?client_id=" + AppUtils.getWardId(context);
                new CommonAsyncTaskHashmap(1, context, this).getquery(url);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {
        try {
            int success = response.getInt("success");
            int error = response.getInt("error");
            String message = response.getString("message");
            JSONObject data = response.getJSONObject("data");
            if (success == 1 && error == 0) {
                if (data != null) {
                    student_name.setText(data.getString("name"));
                    student_class.setText(data.getString("sclass"));
                    student_year.setText(data.getString("currentsession"));
                    edt_studentid.setText(data.getString("rollno"));
                    edt_fathername.setText(data.getString("fathername"));
                    edt_dob.setText(data.getString("dob"));
                    edt_address.setText(data.getString("address"));
                    edt_phoneno.setText(data.getString("phone"));
                    edt_blood_grp.setText(data.getString("bloodgroup"));
                    String url = data.getString("pic");
                    Picasso.with(this).load(url).into(image_student);
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
}
