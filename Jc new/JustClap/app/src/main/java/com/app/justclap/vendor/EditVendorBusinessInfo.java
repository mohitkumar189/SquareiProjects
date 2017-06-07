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


public class EditVendorBusinessInfo extends AppCompatActivity {

    private Context context;
    private Toolbar toolbar;
    private Button btn_submit;
    private EditText edt_businessname, edt_service_offered, edt_distance, edt_business_location, edt_business_desc, edt_websiteurl, edt_facebookurl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vendor_business_info);

        context = this;
        init();
        setListener();

    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Business Info");
        btn_submit = (Button) findViewById(R.id.btn_submit);
        edt_businessname = (EditText) findViewById(R.id.edt_businessname);
        edt_service_offered = (EditText) findViewById(R.id.edt_service_offered);
        edt_distance = (EditText) findViewById(R.id.edt_distance);
        edt_business_location = (EditText) findViewById(R.id.edt_business_location);
        edt_business_desc = (EditText) findViewById(R.id.edt_business_desc);
        edt_websiteurl = (EditText) findViewById(R.id.edt_websiteurl);
        edt_facebookurl = (EditText) findViewById(R.id.edt_facebookurl);

    }


    private void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValidLoginDetails()) {

                }

            }
        });
    }

    private boolean isValidLoginDetails() {
        boolean isValidLoginDetails = true;

        String emailaddress = edt_service_offered.getText().toString();
        String first_name = edt_businessname.getText().toString();
        String mobile = edt_distance.getText().toString();
        String professional_exp = edt_business_location.getText().toString();
        String relevant_exp = edt_business_desc.getText().toString();
        String about = edt_websiteurl.getText().toString();

        if (!emailaddress.equalsIgnoreCase("") && !first_name.equalsIgnoreCase("")
                && !professional_exp.equalsIgnoreCase("") && !relevant_exp.equalsIgnoreCase("") && !mobile.equalsIgnoreCase("")) {

            if (!AppUtils.isEmailValid(emailaddress.trim())) {
                isValidLoginDetails = false;
                edt_service_offered.setError(getString(R.string.error_invalid_email));
                edt_service_offered.requestFocus();
            } else {
                isValidLoginDetails = true;
            }

        } else {
            if (first_name.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                edt_businessname.setError(getString(R.string.enterBusinessName));
                edt_businessname.requestFocus();
            } else if (emailaddress.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                edt_service_offered.setError(getString(R.string.enter_serviceOffer));
                edt_service_offered.requestFocus();
            } else if (mobile.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                edt_distance.setError(getString(R.string.enterdistance));
                edt_distance.requestFocus();
            } else if (professional_exp.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                edt_business_location.setError(getString(R.string.enterBusinesslocation));
                edt_business_location.requestFocus();
            } else if (relevant_exp.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                edt_business_desc.setError(getString(R.string.enterBusinessdesc));
                edt_business_desc.requestFocus();
            }
        }

        return isValidLoginDetails;
    }

}
