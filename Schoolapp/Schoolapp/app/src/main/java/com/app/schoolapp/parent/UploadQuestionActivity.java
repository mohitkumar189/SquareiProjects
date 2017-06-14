package com.app.schoolapp.parent;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.schoolapp.R;
import com.app.schoolapp.asynctask.CommonAsyncTaskHashmap;
import com.app.schoolapp.interfaces.ApiResponse;
import com.app.schoolapp.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class UploadQuestionActivity extends AppCompatActivity implements ApiResponse {
    private Context context;
    private Toolbar toolbar;
    private EditText edPostTitle;
    private EditText edPostQuestion;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_question);
        context = this;
        init();
        initListener();
    }

    private void init() {
        edPostTitle = (EditText) findViewById(R.id.edPostTitle);
        edPostQuestion = (EditText) findViewById(R.id.edPostQuestion);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Upload Post");
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    sendPost();
                }
            }
        });
    }

    private boolean validateInputs() {
        String subject = edPostTitle.getText().toString();
        String content = edPostQuestion.getText().toString();
        if (subject.length() == 0) {
            edPostTitle.setError("Title can not be blank");
            edPostTitle.requestFocus();
            return false;
        } else if (content.length() == 0) {
            edPostQuestion.setError("Question can not be blank");
            edPostQuestion.requestFocus();
            return false;
        } else
            return true;
    }

    private void sendPost() {

        try {
            if (AppUtils.isNetworkAvailable(context)) {

                HashMap<String, String> hm = new HashMap<>();
                hm.put("subject", edPostTitle.getText().toString());
                hm.put("content", edPostQuestion.getText().toString());
                hm.put("refid", "0");
                hm.put("conttype", "public");
                hm.put("client_id", AppUtils.getUserId(context));
                hm.put("appname", "school");
                //       http://squarei.in/api/v2/login?email=anderson.soyug@gmail.com&password=4105
                String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.upload_post);
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostFail(int method, String response) {

    }
}
