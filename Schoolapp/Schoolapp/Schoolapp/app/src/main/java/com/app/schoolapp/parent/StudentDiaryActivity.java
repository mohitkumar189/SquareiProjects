package com.app.schoolapp.parent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.app.schoolapp.R;
import com.app.schoolapp.adapter.AdapterStudentDiary;
import com.app.schoolapp.interfaces.OnCustomItemClicListener;
import com.app.schoolapp.model.ModelData;

import java.util.ArrayList;

public class StudentDiaryActivity extends AppCompatActivity implements OnCustomItemClicListener {

    private Context context;
    private Toolbar toolbar;
    private RecyclerView recycler_list;
    private ArrayList<ModelData> arrayList;
    private ModelData modelDoctor;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BroadcastReceiver broadcastReceiver;
    private AdapterStudentDiary adapterConnectTeacherList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_diary);
        context = this;
        init();
        setListener();
        setData();
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

        arrayList = new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimaryDark));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Dairy");

        recycler_list = (RecyclerView) findViewById(R.id.recycler_list);
        recycler_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
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
    }

    private void setData() {
        modelDoctor = new ModelData();

        arrayList.add(modelDoctor);
        arrayList.add(modelDoctor);
        arrayList.add(modelDoctor);
        arrayList.add(modelDoctor);
        arrayList.add(modelDoctor);
        arrayList.add(modelDoctor);
        arrayList.add(modelDoctor);

        adapterConnectTeacherList = new AdapterStudentDiary(context, this, arrayList);
        recycler_list.setAdapter(adapterConnectTeacherList);
    }


    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 1) {
            // Intent intent = new Intent(context, DoctorDetail.class);
            // startActivity(intent);
        }
    }

}
