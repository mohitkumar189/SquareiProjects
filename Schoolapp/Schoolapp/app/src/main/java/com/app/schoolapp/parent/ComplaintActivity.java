package com.app.schoolapp.parent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.schoolapp.R;
import com.app.schoolapp.asynctask.CommonAsyncTaskHashmap;
import com.app.schoolapp.interfaces.ApiResponse;
import com.app.schoolapp.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ComplaintActivity extends AppCompatActivity implements ApiResponse {

    private Context context;
    private Activity currentActivity;
    private Toolbar toolbar;
    private EditText edt_title, edt_description;
    private Button btn_submit;
    private Spinner spinner_department;
    private String[] departments = {"Management", "Accounts", "Library", "Others"};
    private static int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        context = this;
        currentActivity = this;
        init();
        setListener();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Complaint");
        edt_title = (EditText) findViewById(R.id.edt_title);
        edt_description = (EditText) findViewById(R.id.edt_description);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        spinner_department = (Spinner) findViewById(R.id.spinner_department);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, departments);
        spinner_department.setAdapter(dataAdapter);
    }

    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        spinner_department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ComplaintActivity.position = position;
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidInputs())
                    sendComplain();
            }
        });
    }

    private boolean isValidInputs() {
        if (edt_title.getText().toString().length() < 1) {
            Toast.makeText(context, "Title must be there", Toast.LENGTH_SHORT).show();
            edt_title.requestFocus();
            return false;
        } else if (edt_description.getText().toString().length() < 5) {
            Toast.makeText(context, "Title must be descriptive", Toast.LENGTH_SHORT).show();
            edt_description.requestFocus();
            return false;
        }
        return true;
    }

    private void sendComplain() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                HashMap<String, String> hm = new HashMap<>();
                hm.put("title", edt_title.getText().toString());
                hm.put("complain", edt_description.getText().toString());
                hm.put("dept", departments[position]);
                hm.put("client_id", AppUtils.getUserId(context));
                //       http://squarei.in/api/v2/login?email=anderson.soyug@gmail.com&password=4105
                String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.complain_post);
                new CommonAsyncTaskHashmap(1, context, this).getqueryJson(url, hm);

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
            if (success == 1 && error == 0) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                finish();
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