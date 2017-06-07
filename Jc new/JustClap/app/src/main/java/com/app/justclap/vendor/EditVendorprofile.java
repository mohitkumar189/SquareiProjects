package com.app.justclap.vendor;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.app.justclap.R;
import com.app.justclap.utils.AppUtils;


public class EditVendorprofile extends AppCompatActivity {


    private Context context;
    private Toolbar toolbar;
    private Button btn_submit;
    private EditText edt_name, edt_email, edt_phone, edt_professional_exp, edt_relevant_exp, edt_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vendorprofile);

        context = this;
        init();
        setListener();

    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");
        btn_submit = (Button) findViewById(R.id.btn_submit);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_phone = (EditText) findViewById(R.id.edt_phone);
        edt_professional_exp = (EditText) findViewById(R.id.edt_professional_exp);
        edt_relevant_exp = (EditText) findViewById(R.id.edt_relevant_exp);
        edt_about = (EditText) findViewById(R.id.edt_about);

    }


    private void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean isValidLoginDetails() {
        boolean isValidLoginDetails = true;

        String emailaddress = edt_email.getText().toString();
        String first_name = edt_name.getText().toString();
        String mobile = edt_phone.getText().toString();
        String professional_exp = edt_professional_exp.getText().toString();
        String relevant_exp = edt_relevant_exp.getText().toString();
        String about = edt_about.getText().toString();

        if (!emailaddress.equalsIgnoreCase("") && !first_name.equalsIgnoreCase("")
                && !professional_exp.equalsIgnoreCase("") && !relevant_exp.equalsIgnoreCase("") && !mobile.equalsIgnoreCase("") && !about.equalsIgnoreCase("")) {

            if (!AppUtils.isEmailValid(emailaddress.trim())) {
                isValidLoginDetails = false;
                edt_email.setError(getString(R.string.error_invalid_email));
                edt_email.requestFocus();
            } else {
                isValidLoginDetails = true;
            }

        } else {
            if (first_name.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                edt_name.setError(getString(R.string.enterFirstName));
                edt_name.requestFocus();
            } else if (emailaddress.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                edt_email.setError(getString(R.string.enter_email));
                edt_email.requestFocus();
            } else if (mobile.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                edt_phone.setError(getString(R.string.enterPhone));
                edt_phone.requestFocus();
            } else if (professional_exp.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                edt_professional_exp.setError(getString(R.string.enterProfessional_exp));
                edt_professional_exp.requestFocus();
            } else if (relevant_exp.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                edt_relevant_exp.setError(getString(R.string.enterrelevant_exp));
                edt_relevant_exp.requestFocus();
            } else if (about.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                edt_about.setError(getString(R.string.enterAbout));
                edt_about.requestFocus();
            }
        }

        return isValidLoginDetails;
    }


}
