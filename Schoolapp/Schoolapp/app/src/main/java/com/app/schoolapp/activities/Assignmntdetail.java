package com.app.schoolapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.schoolapp.R;
import com.app.schoolapp.asynctask.CommonAsyncTaskHashmap;
import com.app.schoolapp.interfaces.ApiResponse;
import com.app.schoolapp.model.ModelDataAssignment;
import com.app.schoolapp.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Assignmntdetail extends AppCompatActivity implements ApiResponse {


    private Context context;
    private RecyclerView recycler_list;
    private Toolbar toolbar;
    private TextView text_attachment, text_submissiondate, text_date, text_description, text_subject, text_class, text_name, text_title;
    private String assignmentId;
    private List<ModelDataAssignment> modelDataAssignment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignmnt_detail);
        context = this;
        Intent intent = getIntent();
        assignmentId = intent.getStringExtra("assId");
        init();
        setListener();
        getAssignmentDetail();
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
        modelDataAssignment = new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recycler_list = (RecyclerView) findViewById(R.id.recycler_list);
        recycler_list.setLayoutManager(new GridLayoutManager(context, 3));

        text_attachment = (TextView) findViewById(R.id.text_attachment);
        text_submissiondate = (TextView) findViewById(R.id.text_submissiondate);
        text_date = (TextView) findViewById(R.id.text_date);
        text_description = (TextView) findViewById(R.id.text_description);
        text_subject = (TextView) findViewById(R.id.text_subject);
        text_class = (TextView) findViewById(R.id.text_class);
        text_name = (TextView) findViewById(R.id.text_name);
        text_title = (TextView) findViewById(R.id.text_title);
    }

    private void getAssignmentDetail() {
        if (assignmentId != null) {
            try {
                if (AppUtils.isNetworkAvailable(context)) {
                    String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.assignment_list) + "/" + assignmentId + "?client_id=" + AppUtils.getUserId(context);
                    new CommonAsyncTaskHashmap(1, context, this).getquery(url);
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {
        try {
            int success = response.getInt("success");
            int error = response.getInt("error");
            String message = response.getString("message");
            if (success == 1 && error == 0) {
                JSONObject jsonObject = response.getJSONObject("data");
                String id = jsonObject.getString("id");
                String title = jsonObject.getString("title");
                String subject = jsonObject.getString("subject");
                String content = jsonObject.getString("content");
                String assignedClass = jsonObject.getString("AssignedClass");
                String assignedSection = jsonObject.getString("AssignedSection");
                String submissiondate = jsonObject.getString("submissiondate");
                String created = jsonObject.getString("created");
                String writtenBy = jsonObject.getString("writtenBy");
                String imageurl = jsonObject.getString("imageurl");
                modelDataAssignment.add(new ModelDataAssignment(id, title, subject, assignedClass, assignedSection, content, submissiondate, created, writtenBy, imageurl));
                setData(modelDataAssignment);
            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setData(List<ModelDataAssignment> modelDataAssignment) {
        text_title.setText(modelDataAssignment.get(0).getTitle());
        text_name.setText(modelDataAssignment.get(0).getWrittenBy());
        text_class.setText("Class:" + modelDataAssignment.get(0).getAssignedClass() + "(" + modelDataAssignment.get(0).getAssignedSection() + ")");
        text_subject.setText(modelDataAssignment.get(0).getSubject());
        text_description.setText(modelDataAssignment.get(0).getContent());
        text_date.setText(modelDataAssignment.get(0).getCreated());
        text_submissiondate.setText(modelDataAssignment.get(0).getSubmissiondate());
    }

    @Override
    public void onPostFail(int method, String response) {

    }
}
