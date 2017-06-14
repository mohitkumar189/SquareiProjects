package com.app.schoolapp.student;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.schoolapp.R;
import com.app.schoolapp.activities.Assignmntdetail;
import com.app.schoolapp.activities.PostAssignment;
import com.app.schoolapp.adapter.AdapterStudentAssignmentList;
import com.app.schoolapp.asynctask.CommonAsyncTaskHashmap;
import com.app.schoolapp.interfaces.ApiResponse;
import com.app.schoolapp.interfaces.OnCustomItemClicListener;
import com.app.schoolapp.model.ModelData;
import com.app.schoolapp.model.ModelDataAssignment;
import com.app.schoolapp.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StudentAssignment extends AppCompatActivity implements OnCustomItemClicListener, ApiResponse {
    private Context context;
    private Toolbar toolbar;
    private RecyclerView recycler_list;
    private List<ModelDataAssignment> modelDataAssignment;
    private ModelData modelDoctor;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BroadcastReceiver broadcastReceiver;
    private FloatingActionButton floatingActionButton;
    private AdapterStudentAssignmentList adapterAssignmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_assignment);
        context = this;
        init();
        setListener();
    }

    private void init() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive", "Logout in progress");
                //At this point you should start the login activity and finish this one
                finish();
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);

        modelDataAssignment = new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimaryDark));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Assignments");
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        recycler_list = (RecyclerView) findViewById(R.id.recycler_list);
        recycler_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        getAssignmentsList();
    }

    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostAssignment.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void getAssignmentsList() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.assignment_list) + "?client_id=" + AppUtils.getWardId(context);
                new CommonAsyncTaskHashmap(1, context, this).getquery(url);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setData() {
        adapterAssignmentList = new AdapterStudentAssignmentList(context, this, modelDataAssignment);
        recycler_list.setAdapter(adapterAssignmentList);
    }

    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 1) {
            Intent intent = new Intent(context, Assignmntdetail.class);
            intent.putExtra("assId", modelDataAssignment.get(position).getId());
            startActivity(intent);
        }
    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {
        try {
            int success = response.getInt("success");
            int error = response.getInt("error");
            String message = response.getString("message");
            if (success == 1 && error == 0) {
                JSONArray data = response.getJSONArray("data");
                int length = data.length();
                if (length > 0) {
                    for (int i = 0; i < length; i++) {
                        JSONObject jsonObject = data.getJSONObject(i);
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
                    }
                    setData();
                }
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
