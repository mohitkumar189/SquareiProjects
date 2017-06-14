package com.app.schoolapp.parent;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.schoolapp.R;

public class StudentIdCard extends AppCompatActivity {

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

}
