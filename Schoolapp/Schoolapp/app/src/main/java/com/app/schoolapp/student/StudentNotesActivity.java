package com.app.schoolapp.student;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.app.schoolapp.R;
import com.app.schoolapp.activities.ShareNotesActivity;
import com.app.schoolapp.adapter.AdapterNotes;
import com.app.schoolapp.asynctask.CommonAsyncTaskHashmap;
import com.app.schoolapp.interfaces.ApiResponse;
import com.app.schoolapp.interfaces.OnCustomItemClicListener;
import com.app.schoolapp.model.NotesDataModal;
import com.app.schoolapp.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StudentNotesActivity extends AppCompatActivity implements ApiResponse, OnCustomItemClicListener {
    private RecyclerView recyclerview;
    private Toolbar toolbar;
    private Context context;
    private List<NotesDataModal> notesDataModal;
    private AdapterNotes adapterNotes;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_notes);
        context = StudentNotesActivity.this;
        init();
        setListener();
        getNotesData();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        notesDataModal = new ArrayList<>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(context));
        recyclerview.setNestedScrollingEnabled(false);
    }

    private void setListener() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ShareNotesActivity.class));
            }
        });
    }

    private void setData() {
        adapterNotes = new AdapterNotes(context, this, notesDataModal);
        recyclerview.setAdapter(adapterNotes);
    }

    private void getNotesData() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.notes) + "?client_id=" + AppUtils.getUserId(context);
                new CommonAsyncTaskHashmap(1, context, this).getquery(url);
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
            JSONArray jsonArray = response.getJSONArray("data");
            if (success == 1 && error == 0) {
                int length = jsonArray.length();
                for (int i = 0; i < length; i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    String title = jo.getString("title");
                    String content = jo.getString("content");
                    String created = jo.getString("created");
                    String writtenBy = jo.getString("writtenBy");
                    notesDataModal.add(new NotesDataModal("", title, content, "", created, writtenBy));
                }
                setData();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostFail(int method, String response) {

    }

    @Override
    public void onItemClickListener(int position, int flag) {

    }
}
