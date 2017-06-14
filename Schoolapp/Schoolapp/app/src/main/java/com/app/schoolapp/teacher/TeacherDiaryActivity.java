package com.app.schoolapp.teacher;

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

public class TeacherDiaryActivity extends AppCompatActivity implements ApiResponse {

    private Context context;
    private TextView text_class, text_section, text_subject;
    private Spinner spinner_class, spinner_section, spinner_subject;
    private EditText edt_title, edt_desc;
    private Button btn_submit;
    private Toolbar toolbar;
    ArrayList<String> listClass;
    ArrayList<String> listSection;
    ArrayList<String> listSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_dairy);
        context = this;
        init();
        setListener();

        setDate();
    }

    private void setDate() {

        listClass = new ArrayList<>();
        listSection = new ArrayList<>();
        listSubject = new ArrayList<>();


        listClass.add("1st");
        listClass.add("2nd");
        listClass.add("3rd");
        listClass.add("4th");
        listClass.add("5th");
        listClass.add("6th");
        listClass.add("7th");
        listClass.add("8th");
        listClass.add("9th");
        listClass.add("10th");

        ArrayAdapter<String> adpClass = new ArrayAdapter<String>(context, R.layout.row_spinner, R.id.text_time, listClass);
        spinner_class.setAdapter(adpClass);

        listSection.add("A");
        listSection.add("B");
        listSection.add("C");
        listSection.add("D");
        ArrayAdapter<String> adpSection = new ArrayAdapter<String>(context, R.layout.row_spinner, R.id.text_time, listSection);
        spinner_section.setAdapter(adpSection);

        listSubject.add("Math");
        listSubject.add("English");
        listSubject.add("Hindi");
        listSubject.add("Science");
        listSubject.add("Social Studies");
        ArrayAdapter<String> adpSubject = new ArrayAdapter<String>(context, R.layout.row_spinner, R.id.text_time, listSubject);
        spinner_subject.setAdapter(adpSubject);

    }

    private void setListener() {

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!edt_desc.getText().toString().equalsIgnoreCase("") && !edt_title.getText().toString().equalsIgnoreCase("")) {

                    submitData();

                } else {
                    Toast.makeText(context, "Please fill all details", Toast.LENGTH_SHORT).show();
                }

            }


        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        text_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spinner_class.performClick();
            }
        });
        text_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spinner_section.performClick();
            }
        });
        text_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spinner_subject.performClick();
            }
        });


        spinner_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                text_subject.setText(spinner_subject.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                text_section.setText(spinner_section.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                text_class.setText(spinner_class.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void submitData() {

        if (AppUtils.isNetworkAvailable(context)) {

            HashMap<String, String> hm = new HashMap<>();

          /*  hm.put("class",spinner_class.getSelectedItem().toString());
            hm.put("class",spinner_class.getSelectedItem().toString());
            hm.put("class",spinner_class.getSelectedItem().toString());*/
            hm.put("title", edt_title.getText().toString());
            hm.put("content", edt_desc.getText().toString());
            hm.put("subject", "testing");
/*            hm.put("forSubject", "");
            hm.put("forClass", "");
            hm.put("forSection", "");*/
            hm.put("client_id", AppUtils.getUserId(context));

            String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.user_diary_create);
            new CommonAsyncTaskHashmap(1, context, this).getqueryJson(url, hm);
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        text_class = (TextView) findViewById(R.id.text_class);
        text_section = (TextView) findViewById(R.id.text_section);
        text_subject = (TextView) findViewById(R.id.text_subject);
        spinner_class = (Spinner) findViewById(R.id.spinner_class);
        spinner_section = (Spinner) findViewById(R.id.spinner_section);
        spinner_subject = (Spinner) findViewById(R.id.spinner_subject);
        edt_title = (EditText) findViewById(R.id.edt_title);
        edt_desc = (EditText) findViewById(R.id.edt_desc);
        btn_submit = (Button) findViewById(R.id.btn_submit);

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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostFail(int method, String response) {

    }
}
