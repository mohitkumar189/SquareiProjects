package com.app.schoolapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.schoolapp.R;
import com.app.schoolapp.asynctask.CommonAsyncTaskHashmap;
import com.app.schoolapp.interfaces.ApiResponse;
import com.app.schoolapp.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SignUp extends AppCompatActivity implements ApiResponse, AdapterView.OnItemSelectedListener {

    private Context context;
    private Toolbar toolbar;
    private EditText edt_email, edt_name, edt_password, edt_mobile, confirmpassword;
    private Button btn_submit;
    private TextView text_add_photo;
    private static final String TAG = "SignUp";
    private Spinner spinnerUserType;
    private String userType = null;

    // Spinner Drop down elements
    private List<String> userTypes = new ArrayList<String>();

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
        spinnerUserType = (Spinner) findViewById(R.id.spinnerUserType);
        text_add_photo = (TextView) findViewById(R.id.text_add_photo);

        userTypes.add("--Select User Type--");
        userTypes.add("Student");
        userTypes.add("Parent");
        userTypes.add("Teacher");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, userTypes);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Dropdown Layout
        spinnerUserType.setAdapter(dataAdapter);
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
                if (userType != null) {
                    if (isValidLoginDetails()) {
             /*       Toast.makeText(context, "Signup sucessfully", Toast.LENGTH_SHORT).show();
                    AppUtils.setUserId(context, "21");
                    Intent intent = new Intent(context, StudentDashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();*/
                        signupUser();
                    }
                } else {
                    Toast.makeText(context, getResources().getString(R.string.select_user_type), Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinnerUserType.setOnItemSelectedListener(this);
    }

    private void signupUser() {
        //  String fullname=edt_name.getText().toString().trim();
        // String[] splitStr = fullname.split("\\s+");
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                HashMap<String, String> hm = new HashMap<>();
                hm.put("name", edt_name.getText().toString());
                hm.put("email", edt_email.getText().toString());
                hm.put("password", edt_password.getText().toString());
                hm.put("mobile", edt_mobile.getText().toString());
                hm.put("category", userType);
                hm.put("appname", "school");
                //       http://squarei.in/api/v2/login?email=anderson.soyug@gmail.com&password=4105
                String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.register);
                new CommonAsyncTaskHashmap(1, context, this).getqueryJson(url, hm);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            } else if (mobileno.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                edt_mobile.setError(getString(R.string.enterMobileno));
                edt_mobile.requestFocus();
            }
        }

        return isValidLoginDetails;
    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {
        try {
            int success = response.getInt("success");
            String message = response.getString("message");
            int error = response.getInt("error");
            if (success == 1) {
                if (error == 0) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUp.this, LoginActivity.class));
                } else {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "==============response received===========" + response);
    }

    @Override
    public void onPostFail(int method, String response) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
            case 1:
                userType = userTypes.get(position);
                break;
            case 2:
                userType = userTypes.get(position);
                break;
            case 3:
                userType = userTypes.get(position);
                break;
            default:
                userType = null;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
