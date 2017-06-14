package com.app.schoolapp.parent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.schoolapp.R;
import com.app.schoolapp.adapter.AdapterStudentTimeTable;
import com.app.schoolapp.adapter.StudentTimeTableAdapter;
import com.app.schoolapp.asynctask.CommonAsyncTaskHashmap;
import com.app.schoolapp.interfaces.ApiResponse;
import com.app.schoolapp.interfaces.OnCustomItemClicListener;
import com.app.schoolapp.model.ModelTimeTable;
import com.app.schoolapp.model.PeriodDetails;
import com.app.schoolapp.model.TimeTabelModel;
import com.app.schoolapp.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentTimeTable extends AppCompatActivity implements OnCustomItemClicListener, ApiResponse {

    private Toolbar toolbar;
    private Context context;
    private BroadcastReceiver broadcastReceiver;
    private RecyclerView mRecyclerView;
    private ModelTimeTable model;
    private ArrayList<ModelTimeTable> timetableList;
    private ArrayList<String> periodName;
    private ArrayList<String> daysList;
    private LinearLayoutManager layoutManager;
    private AdapterStudentTimeTable adapterTimeTable;
    private StudentTimeTableAdapter studentTimeTableAdapter;
    //   private ArrayList<PeriodDetails> periodDetails;
    private ArrayList<ArrayList<ModelTimeTable>> timeTableHolder = new ArrayList<>(); // this is for the wholde data
    private ArrayList<ModelTimeTable> timeTableList1 = new ArrayList<>();// this is for the single result
    private ArrayList<ModelTimeTable> timeTableList2 = new ArrayList<>();// this is for the single result
    private ArrayList<ModelTimeTable> timeTableList3 = new ArrayList<>();// this is for the single result
    private ArrayList<ModelTimeTable> timeTableList4 = new ArrayList<>();// this is for the single result
    private ArrayList<ModelTimeTable> timeTableList5 = new ArrayList<>();// this is for the single result
    private ArrayList<ModelTimeTable> timeTableList6 = new ArrayList<>();// this is for the single result
    private ArrayList<ModelTimeTable> timeTableList7 = new ArrayList<>();// this is for the single result


    private HashMap<String, HashMap<String, PeriodDetails>> periods = new HashMap<>();
    HashMap<String, PeriodDetails> periodsDetails1 = new HashMap<>();
    HashMap<String, PeriodDetails> periodsDetails2 = new HashMap<>();
    HashMap<String, PeriodDetails> periodsDetails3 = new HashMap<>();
    HashMap<String, PeriodDetails> periodsDetails4 = new HashMap<>();
    HashMap<String, PeriodDetails> periodsDetails5 = new HashMap<>();
    HashMap<String, PeriodDetails> periodsDetails6 = new HashMap<>();
    HashMap<String, PeriodDetails> periodsDetails7 = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        context = this;
        init();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //  gettimetableList();
        setListener();
        gettimetableList();

        //  model = new ModelTimeTable();
        //  model.setTeacher_name("");

        adapterTimeTable = new AdapterStudentTimeTable(context, this, timetableList, periodName, daysList);
    }

    private void init() {
        timetableList = new ArrayList<>();
        periodName = new ArrayList<>();
        daysList = new ArrayList<>();
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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(context);

        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(layoutManager);

    }


    public void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

    }

    private void gettimetableList() {
        String url = null;
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                if (AppUtils.getUserType(context).equals("Student")) {
                    url = getResources().getString(R.string.base_url) + getResources().getString(R.string.time_table) + "?client_id=" + AppUtils.getUserId(context);
                } else if (AppUtils.getUserType(context).equals("Parent")) {
                    url = getResources().getString(R.string.base_url) + getResources().getString(R.string.time_table) + "?client_id=" + AppUtils.getWardId(context);
                } else if ((AppUtils.getUserType(context).equals("Teacher"))) {
                    url = getResources().getString(R.string.base_url) + getResources().getString(R.string.time_table) + "?client_id=" + AppUtils.getUserId(context);
                }

                new CommonAsyncTaskHashmap(1, context, this).getquery(url);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   /* public void gettimetableList() {

        if (AppUtils.isNetworkAvailable(context)) {

            HashMap<String, Object> hm = new HashMap<>();

            hm.put("teacherid", AppUtils.getUserId(context));
            hm.put("authkey", Constant.AUTHKEY);
            hm.put("schoolid", AppUtils.getSchoolId(context));
            //hm.put("studentid", "70");

            String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.teacher_timetable);
            new CommonAsyncTaskHashmap(1, context, this).getquery(url, hm);

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }

    }*/


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }


    @Override
    public void onItemClickListener(int position, int flag) {

    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {
        int success = 0;
        ArrayList<TimeTabelModel> timeTabelModel = new ArrayList<>();
        //   ArrayList<String> periods = new ArrayList<>();
        try {
            success = response.getInt("success");
            int error = response.getInt("error");
            if (success == 1 && error == 0) {
                JSONArray data = response.getJSONArray("data");
                int length = data.length();
                for (int i = 0; i < length; i++) {
                    JSONObject jsonObject = data.getJSONObject(i);
                    String period = jsonObject.getString("period");
                    JSONObject Sat = jsonObject.getJSONObject("Sat");
                    JSONObject Fri = jsonObject.getJSONObject("Fri");
                    JSONObject Thu = jsonObject.getJSONObject("Thu");
                    JSONObject Tue = jsonObject.getJSONObject("Tue");
                    JSONObject Mon = jsonObject.getJSONObject("Mon");
                    JSONObject Wed = jsonObject.getJSONObject("Wed");
                    switch (i + 1) {
                        case 1:
                            periodsDetails1.put("Sat", new PeriodDetails(Sat.getString("subject"), Sat.getString("teacher")));
                            periodsDetails1.put("Fri", new PeriodDetails(Fri.getString("subject"), Fri.getString("teacher")));
                            periodsDetails1.put("Thu", new PeriodDetails(Thu.getString("subject"), Thu.getString("teacher")));
                            periodsDetails1.put("Tue", new PeriodDetails(Tue.getString("subject"), Tue.getString("teacher")));
                            periodsDetails1.put("Mon", new PeriodDetails(Mon.getString("subject"), Mon.getString("teacher")));
                            periodsDetails1.put("Wed", new PeriodDetails(Wed.getString("subject"), Wed.getString("teacher")));
                            periods.put(period, periodsDetails1);
                            break;
                        case 2:
                            periodsDetails2.put("Sat", new PeriodDetails(Sat.getString("subject"), Sat.getString("teacher")));
                            periodsDetails2.put("Fri", new PeriodDetails(Fri.getString("subject"), Fri.getString("teacher")));
                            periodsDetails2.put("Thu", new PeriodDetails(Thu.getString("subject"), Thu.getString("teacher")));
                            periodsDetails2.put("Tue", new PeriodDetails(Tue.getString("subject"), Tue.getString("teacher")));
                            periodsDetails2.put("Mon", new PeriodDetails(Mon.getString("subject"), Mon.getString("teacher")));
                            periodsDetails2.put("Wed", new PeriodDetails(Wed.getString("subject"), Wed.getString("teacher")));
                            periods.put(period, periodsDetails2);
                            break;
                        case 3:
                            periodsDetails3.put("Sat", new PeriodDetails(Sat.getString("subject"), Sat.getString("teacher")));
                            periodsDetails3.put("Fri", new PeriodDetails(Fri.getString("subject"), Fri.getString("teacher")));
                            periodsDetails3.put("Thu", new PeriodDetails(Thu.getString("subject"), Thu.getString("teacher")));
                            periodsDetails3.put("Tue", new PeriodDetails(Tue.getString("subject"), Tue.getString("teacher")));
                            periodsDetails3.put("Mon", new PeriodDetails(Mon.getString("subject"), Mon.getString("teacher")));
                            periodsDetails3.put("Wed", new PeriodDetails(Wed.getString("subject"), Wed.getString("teacher")));
                            periods.put(period, periodsDetails3);
                            break;
                        case 4:
                            periodsDetails4.put("Sat", new PeriodDetails(Sat.getString("subject"), Sat.getString("teacher")));
                            periodsDetails4.put("Fri", new PeriodDetails(Fri.getString("subject"), Fri.getString("teacher")));
                            periodsDetails4.put("Thu", new PeriodDetails(Thu.getString("subject"), Thu.getString("teacher")));
                            periodsDetails4.put("Tue", new PeriodDetails(Tue.getString("subject"), Tue.getString("teacher")));
                            periodsDetails4.put("Mon", new PeriodDetails(Mon.getString("subject"), Mon.getString("teacher")));
                            periodsDetails4.put("Wed", new PeriodDetails(Wed.getString("subject"), Wed.getString("teacher")));
                            periods.put(period, periodsDetails4);
                            break;
                        case 5:
                            periodsDetails5.put("Sat", new PeriodDetails(Sat.getString("subject"), Sat.getString("teacher")));
                            periodsDetails5.put("Fri", new PeriodDetails(Fri.getString("subject"), Fri.getString("teacher")));
                            periodsDetails5.put("Thu", new PeriodDetails(Thu.getString("subject"), Thu.getString("teacher")));
                            periodsDetails5.put("Tue", new PeriodDetails(Tue.getString("subject"), Tue.getString("teacher")));
                            periodsDetails5.put("Mon", new PeriodDetails(Mon.getString("subject"), Mon.getString("teacher")));
                            periodsDetails5.put("Wed", new PeriodDetails(Wed.getString("subject"), Wed.getString("teacher")));
                            periods.put(period, periodsDetails5);
                            break;
                        case 6:
                            periodsDetails6.put("Sat", new PeriodDetails(Sat.getString("subject"), Sat.getString("teacher")));
                            periodsDetails6.put("Fri", new PeriodDetails(Fri.getString("subject"), Fri.getString("teacher")));
                            periodsDetails6.put("Thu", new PeriodDetails(Thu.getString("subject"), Thu.getString("teacher")));
                            periodsDetails6.put("Tue", new PeriodDetails(Tue.getString("subject"), Tue.getString("teacher")));
                            periodsDetails6.put("Mon", new PeriodDetails(Mon.getString("subject"), Mon.getString("teacher")));
                            periodsDetails6.put("Wed", new PeriodDetails(Wed.getString("subject"), Wed.getString("teacher")));
                            periods.put(period, periodsDetails6);
                            break;
                        case 7:
                            periodsDetails7.put("Sat", new PeriodDetails(Sat.getString("subject"), Sat.getString("teacher")));
                            periodsDetails7.put("Fri", new PeriodDetails(Fri.getString("subject"), Fri.getString("teacher")));
                            periodsDetails7.put("Thu", new PeriodDetails(Thu.getString("subject"), Thu.getString("teacher")));
                            periodsDetails7.put("Tue", new PeriodDetails(Tue.getString("subject"), Tue.getString("teacher")));
                            periodsDetails7.put("Mon", new PeriodDetails(Mon.getString("subject"), Mon.getString("teacher")));
                            periodsDetails7.put("Wed", new PeriodDetails(Wed.getString("subject"), Wed.getString("teacher")));
                            periods.put(period, periodsDetails7);
                            break;
                    }

                    //    System.out.println("after values====" + periods);
                    //   periodsDetails.clear();
                }
                //       System.out.println("sending values====" + periods);
                studentTimeTableAdapter = new StudentTimeTableAdapter(context, this, periods);
                //  studentTimeTableAdapter = new StudentTimeTableAdapter(timeTableHolder, context, this);
                mRecyclerView.setAdapter(studentTimeTableAdapter);
            }




/*                JSONObject dataObject = response.getJSONObject("data");
                Iterator<?> keys = response.keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    periods.add(key);
                    //     if ( response.get(key) instanceof JSONObject ) {
                    //         JSONObject xx = new JSONObject(response.get(key).toString());
                    System.out.println(key);
                    //      }
                }
                int size = periods.size();
                for (int i = 0; i < size; i++) {
                    JSONObject jsonObject = dataObject.getJSONObject(periods.get(i));
                    JSONObject tue = jsonObject.getJSONObject("Tue");
                    JSONObject thu = jsonObject.getJSONObject("Thu");
                    JSONObject wed = jsonObject.getJSONObject("Wed");
                    JSONObject mon = jsonObject.getJSONObject("Mon");
                    JSONObject sat = jsonObject.getJSONObject("Sat");
                    JSONObject fri = jsonObject.getJSONObject("Fri");
                    //  ArrayList<String> days=new ArrayList<>();
                    timeTabelModel.add(new TimeTabelModel(jsonObject.toString(), tue.getString("subject"), tue.toString(), tue.getString("teacher")));
                }*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostFail(int method, String response) {

    }
    /*
            try {
        int success = response.getInt("success");
        int error = response.getInt("error");

        if (success == 1 && error == 0) {
            JSONArray data = response.getJSONArray("data");
            int length = data.length();
            if (length > 0) {
                periodDetails = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    periodDetails.clear();
                    JSONObject jsonObject = data.getJSONObject(i);
                    System.out.println(jsonObject);
                    int period = jsonObject.getInt("period");
                    String subject = jsonObject.getString("subject");
                    String rday = jsonObject.getString("rday");
                    String teacher = jsonObject.getString("teacher");
                    if (rday != null && teacher != null) {
                        System.out.println("saving data: " + subject + " " + teacher);
                        periodDetails.add(new PeriodDetails(subject, teacher));
                    }

                    switch (period) {
                        case 1:
                            timeTableList3.clear();
                            timeTableList1.add(new ModelTimeTable(periodDetails, rday, period));
                            if (timeTableHolder.size() == 1 && timeTableHolder.get(0) != null)
                                timeTableHolder.remove(0);
                            timeTableHolder.add(0, timeTableList1);
                            System.out.println("saving data: " + timeTableList1);
                            System.out.println("Saving: " + subject + " " + rday + " " + teacher + " " + period);
                            break;
                        case 2:
                            timeTableList3.clear();
                            timeTableList2.add(new ModelTimeTable(periodDetails, rday, period));
                            if (timeTableHolder.size() == 2 && timeTableHolder.get(1) != null)
                                timeTableHolder.remove(1);
                            timeTableHolder.add(1, timeTableList2);
                            System.out.println("saving data: " + timeTableList2);
                            break;
                        case 3:
                            timeTableList3.clear();
                            timeTableList3.add(new ModelTimeTable(periodDetails, rday, period));
                            if (timeTableHolder.size() == 3 && timeTableHolder.get(2) != null)
                                timeTableHolder.remove(2);
                            timeTableHolder.add(2, timeTableList3);
                            System.out.println("saving data: " + timeTableList3);
                            break;
/*                            case 4:
                                timeTableList4.add(new ModelTimeTable(periodDetails, rday, period));
                                if (timeTableHolder.size() ==4 && timeTableHolder.get(3) != null)
                             //       timeTableHolder.remove(3);
                                timeTableHolder.add(3, timeTableList4);
                                System.out.println("Saving: "+subject+" "+rday+" "+teacher+" "+period);
                                break;
                            case 5:
                                timeTableList5.add(new ModelTimeTable(periodDetails, rday, period));
                                if (timeTableHolder.size() ==5 && timeTableHolder.get(4) != null)
                              //      timeTableHolder.remove(4);
                                timeTableHolder.add(4, timeTableList5);
                                System.out.println("Saving: "+subject+" "+rday+" "+teacher+" "+period);
                                break;
                            case 6:
                                timeTableList6.add(new ModelTimeTable(periodDetails, rday, period));
                                if (timeTableHolder.size() ==6 && timeTableHolder.get(6) != null)
                               //     timeTableHolder.remove(6);
                                timeTableHolder.add(5, timeTableList6);
                                System.out.println("Saving: "+subject+" "+rday+" "+teacher+" "+period);
                                break;
                            case 7:
                                timeTableList7.add(new ModelTimeTable(periodDetails, rday, period));
                                if (timeTableHolder.size() ==7 && timeTableHolder.get(6) != null)
                               //     timeTableHolder.remove(6);
                                timeTableHolder.add(6, timeTableList7);
                                System.out.println("Saving: "+subject+" "+rday+" "+teacher+" "+period);
                                break;*/
/*                        default:
                            break;
                    }
                }
                studentTimeTableAdapter = new StudentTimeTableAdapter(timeTableHolder, context, this);
                mRecyclerView.setAdapter(studentTimeTableAdapter);
                // studentTimeTableAdapter.notifyDataSetChanged();
            }

        }
    } catch (JSONException e) {
        e.printStackTrace();
    }


        for (int i = 0; i < timeTableHolder.size(); i++) {
        //    System.out.println("============" + (timeTableHolder.get(i)).toString());
    }
*//*
        try {
            if (method == 1) {
                if (response.getString("response").equalsIgnoreCase("1")) {

                    periodName.clear();
                    timetableList.clear();

                    //   JSONObject result = response.getJSONObject("result");
                    JSONArray Period = response.getJSONArray("result");

                    for (int i = 0; i < Period.length(); i++) {

                        JSONObject ob = Period.getJSONObject(i);

                        periodName.add(ob.getString("PeriodName"));
                        daysList.add(ob.getJSONArray("days").toString());

                        JSONArray days = ob.getJSONArray("days");
                        for (int j = 0; j < days.length(); j++) {

*//**//*                            JSONObject jo = days.getJSONObject(j);
                            model = new ModelTimeTable();

                            model.setDaysArray(jo.toString());
                            model.setDay(jo.getString("day"));
                            model.setClassname(jo.getString("class"));
                            model.setSubject(jo.getString("subject"));
                            model.setTime(jo.getString("time"));
                            model.setTeacher_name(jo.getString("teacher_name"));

                            timetableList.add(model);*//**//*
                        }
                    }
                    adapterTimeTable = new AdapterStudentTimeTable(context, this, timetableList, periodName, daysList);
                    mRecyclerView.setAdapter(adapterTimeTable);

                } else {

                    Toast.makeText(context, response.getString("msg"), Toast.LENGTH_SHORT).show();
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
}


