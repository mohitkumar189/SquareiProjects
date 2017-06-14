package com.app.schoolapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.app.schoolapp.R;
import com.app.schoolapp.student.StudentDashboard;
import com.app.schoolapp.utils.AppUtils;


public class SignUp extends AppCompatActivity {

    private Context context;
    private Toolbar toolbar;
    private EditText edt_email, edt_name, edt_password, edt_mobile,confirmpassword;
    private Button btn_submit;
    private TextView text_add_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        context = this;
        init();
        setListener();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Forgot Password");
        edt_password = (EditText) findViewById(R.id.password);
        confirmpassword = (EditText) findViewById(R.id.confirmpassword);
        edt_email = (EditText) findViewById(R.id.email);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_mobile = (EditText) findViewById(R.id.edt_mobile);
        btn_submit = (Button) findViewById(R.id.btn_signup);
        text_add_photo = (TextView) findViewById(R.id.text_add_photo);
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
                    Toast.makeText(context, "Signup sucessfully", Toast.LENGTH_SHORT).show();
                    AppUtils.setUserId(context, "21");
                    Intent intent = new Intent(context, StudentDashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private boolean isValidLoginDetails() {
        boolean isValidLoginDetails = true;

        String emailaddress = edt_email.getText().toString();
        String password = edt_password.getText().toString();
        String conpassword = confirmpassword.getText().toString();
        String first_name = edt_name.getText().toString();
        String mobileno = edt_mobile.getText().toString();

        if (!emailaddress.equalsIgnoreCase("") && !first_name.equalsIgnoreCase("")
                && !mobileno.equalsIgnoreCase("") && !password.equalsIgnoreCase("") && !conpassword.equalsIgnoreCase("")) {

            if (!AppUtils.isEmailValid(emailaddress.trim())) {
                isValidLoginDetails = false;
                edt_email.setError(getString(R.string.error_invalid_email));
                edt_email.requestFocus();
            } else if (mobileno.length() < 10) {
                isValidLoginDetails = false;
                edt_mobile.setError(getString(R.string.mobileno_Length));
                edt_mobile.requestFocus();
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
            } else if (password.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                edt_password.setError(getString(R.string.enterPassword));
                edt_password.requestFocus();
            } else if (conpassword.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                edt_password.setError(getString(R.string.enterconfirmPassword));
                edt_password.requestFocus();
            }  else if (mobileno.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                edt_mobile.setError(getString(R.string.enterMobileno));
                edt_mobile.requestFocus();
            }
        }

        return isValidLoginDetails;
    }

}
