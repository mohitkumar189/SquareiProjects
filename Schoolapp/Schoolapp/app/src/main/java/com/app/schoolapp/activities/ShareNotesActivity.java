package com.app.schoolapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.schoolapp.R;
import com.app.schoolapp.asynctask.CommonAsyncTaskHashmap;
import com.app.schoolapp.interfaces.ApiResponse;
import com.app.schoolapp.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ShareNotesActivity extends AppCompatActivity implements ApiResponse {
    private RecyclerView recycler_list;
    private Toolbar toolbar;
    private Context context;
    private EditText edt_title;
    private EditText edt_desc;
    private RadioGroup radioGroup;
    private RelativeLayout rl_add_image;
    private Button btnSubmitNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_notes);
        context = this;
        init();
        setListener();
    }

    private void init() {
        recycler_list = (RecyclerView) findViewById(R.id.recycler_list);
        edt_title = (EditText) findViewById(R.id.edt_title);
        edt_desc = (EditText) findViewById(R.id.edt_desc);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        rl_add_image = (RelativeLayout) findViewById(R.id.rl_add_image);
        btnSubmitNote = (Button) findViewById(R.id.btnSubmitNote);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSubmitNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    submitNotes();
                }
            }
        });
    }


    private boolean validateInputs() {
        String subject = edt_title.getText().toString();
        String content = edt_desc.getText().toString();
        if (subject.length() == 0) {
            edt_title.setError("Title can not be blank");
            edt_title.requestFocus();
            return false;
        } else if (content.length() == 0) {
            edt_desc.setError("Question can not be blank");
            edt_desc.requestFocus();
            return false;
        } else
            return true;
    }

    private void submitNotes() {
        if (AppUtils.isNetworkAvailable(context)) {

            HashMap<String, String> hm = new HashMap<>();
            hm.put("title", edt_title.getText().toString());
            hm.put("content", edt_desc.getText().toString());
            hm.put("shared", "0");
            hm.put("client_id", AppUtils.getUserId(context));

            String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.upload_notes);
            new CommonAsyncTaskHashmap(1, context, this).getqueryJson(url, hm);
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
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
