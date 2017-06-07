package com.app.anmolenterprise.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.app.anmolenterprise.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LeadDetail extends AppCompatActivity {

    private String detail = "";
    private TextView text_name, text_email, text_mobileno, text_alternateno, text_pincode, text_address, text_product, text_companyname,
            text_purchase_date, text_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        detail = getIntent().getStringExtra("detail");
        init();
        setData();

    }

    private void setData() {

        if (!detail.equalsIgnoreCase("")) {

            try {
                JSONObject jsonObject = new JSONObject(detail);
                text_name.setText(jsonObject.getString("full_name"));
                text_email.setText(jsonObject.getString("email"));
                text_mobileno.setText(jsonObject.getString("mobile"));
                text_alternateno.setText(jsonObject.getString("alternative_no"));
                text_pincode.setText(jsonObject.getString("pincode"));
                text_address.setText(jsonObject.getString("address"));
                text_product.setText(jsonObject.getString("product"));
                text_companyname.setText(jsonObject.getString("company_name"));
                text_purchase_date.setText(jsonObject.getString("purchase_date"));
                text_comment.setText(jsonObject.getString("comment"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void init() {

        text_name = (TextView) findViewById(R.id.text_name);
        text_email = (TextView) findViewById(R.id.text_email);
        text_mobileno = (TextView) findViewById(R.id.text_mobileno);
        text_alternateno = (TextView) findViewById(R.id.text_alternateno);
        text_pincode = (TextView) findViewById(R.id.text_pincode);
        text_address = (TextView) findViewById(R.id.text_address);
        text_comment = (TextView) findViewById(R.id.text_comment);
        text_purchase_date = (TextView) findViewById(R.id.text_purchase_date);
        text_product = (TextView) findViewById(R.id.text_product);
        text_companyname = (TextView) findViewById(R.id.text_companyname);


    }

}
