package com.app.schoolapp.parent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.app.schoolapp.R;
import com.app.schoolapp.decorators.EventDecorator;
import com.app.schoolapp.interfaces.ApiResponse;
import com.app.schoolapp.model.ModelData;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class StudentAttendance extends AppCompatActivity implements OnDateSelectedListener, ApiResponse {

    Toolbar toolbar;
    private BroadcastReceiver broadcastReceiver;
    Context context;
    MaterialCalendarView widget;
    ArrayList<ModelData> arrayList;
    ArrayList<CalendarDay> presentDates;
    ArrayList<CalendarDay> absentDates;
    ArrayList<CalendarDay> leaveDates;
    ArrayList<CalendarDay> holidayDates;
    LinearLayoutManager layoutManager;
    TextView text_total_present, text_total_absent, text_total_leave, text_total_holiay;
    private ModelData itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);
        context = this;
        init();
        setListener();
        setdata();

    }

    private void setdata() {

        itemList = new ModelData();
        itemList.setAttn_status("1");
        itemList.setAttn_date("2017-05-12");
        arrayList.add(itemList);

        itemList = new ModelData();
        itemList.setAttn_status("1");
        itemList.setAttn_date("2017-05-18");
        arrayList.add(itemList);

        itemList = new ModelData();
        itemList.setAttn_status("2");
        itemList.setAttn_date("2017-05-22");
        arrayList.add(itemList);

        itemList = new ModelData();
        itemList.setAttn_status("3");
        itemList.setAttn_date("2017-05-02");
        arrayList.add(itemList);

        itemList = new ModelData();
        itemList.setAttn_status("4");
        itemList.setAttn_date("2017-05-11");
        arrayList.add(itemList);

        if (arrayList.size() > 0) {
            new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());
        }


    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Student Attendance");
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
        presentDates = new ArrayList<>();
        absentDates = new ArrayList<>();
        holidayDates = new ArrayList<>();
        leaveDates = new ArrayList<>();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        widget = (MaterialCalendarView) findViewById(R.id.calendarView);
        //  widget.setFirstDayOfWeek(Calendar.MONDAY);
        widget.setOnDateChangedListener(this);
        Calendar calendar = Calendar.getInstance();
        widget.setSelectedDate(calendar.getTime());
        text_total_absent = (TextView) findViewById(R.id.text_total_absent);
        text_total_present = (TextView) findViewById(R.id.text_total_present);
        text_total_holiay = (TextView) findViewById(R.id.text_total_holiay);
        text_total_leave = (TextView) findViewById(R.id.text_total_leave);
    }

    public void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {

    }

    @Override
    public void onPostFail(int method, String response) {

    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

    }

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {

            Calendar calendar = Calendar.getInstance();
            presentDates = new ArrayList<>();
            leaveDates = new ArrayList<>();
            absentDates = new ArrayList<>();
            holidayDates = new ArrayList<>();
            for (int i = 0; i < arrayList.size(); i++) {

                CalendarDay day = CalendarDay.from(fromDateToCalendar(arrayList.get(i).getAttn_date()));
                if (arrayList.get(i).getAttn_status().equalsIgnoreCase("1")) {
                    presentDates.add(day);
                } else if (arrayList.get(i).getAttn_status().equalsIgnoreCase("2")) {
                    absentDates.add(day);
                } else if (arrayList.get(i).getAttn_status().equalsIgnoreCase("3")) {
                    leaveDates.add(day);
                } else if (arrayList.get(i).getAttn_status().equalsIgnoreCase("4")) {
                    holidayDates.add(day);
                }

            }

            return presentDates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            widget.addDecorator(new EventDecorator(getResources().getColor(R.color.green_color), presentDates));
            widget.addDecorator(new EventDecorator(getResources().getColor(R.color.blue_color), holidayDates));
            widget.addDecorator(new EventDecorator(getResources().getColor(R.color.yellow_color), leaveDates));
            widget.addDecorator(new EventDecorator(getResources().getColor(R.color.red_color), absentDates));


        }
    }

    private Calendar fromDateToCalendar(String date) {


        Calendar cal = Calendar.getInstance();

        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date1 = format.parse(date);
            cal.setTime(date1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cal;

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }


}
