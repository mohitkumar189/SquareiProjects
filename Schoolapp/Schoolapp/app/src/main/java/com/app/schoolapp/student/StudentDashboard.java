package com.app.schoolapp.student;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.app.schoolapp.R;
import com.app.schoolapp.activities.LoginActivity;
import com.app.schoolapp.adapter.AdapterParentdashbooard;
import com.app.schoolapp.interfaces.OnCustomItemClicListener;
import com.app.schoolapp.model.ModelData;
import com.app.schoolapp.parent.ComplaintActivity;
import com.app.schoolapp.parent.ConnectToTeacher;
import com.app.schoolapp.parent.Eventcalendar;
import com.app.schoolapp.parent.FeesPaymentHistory;
import com.app.schoolapp.parent.PublicForumActivity;
import com.app.schoolapp.parent.StudentAttendance;
import com.app.schoolapp.parent.StudentDiaryActivity;
import com.app.schoolapp.parent.StudentIdCard;
import com.app.schoolapp.parent.StudentResult;
import com.app.schoolapp.parent.StudentTimeTable;
import com.app.schoolapp.parent.Tracktransport;
import com.app.schoolapp.utils.AppUtils;

import java.util.ArrayList;

public class StudentDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnCustomItemClicListener {
    private Context context;
    private RecyclerView recyclerview;
    private AdapterParentdashbooard adapterParentdashbooard;
    private ArrayList<ModelData> arrayList;
    Integer imgaes[] = {R.drawable.wardresult, R.drawable.tracktrabsport, R.drawable.attandance, R.drawable.calnder, R.drawable.payments, R.drawable.timetable, R.drawable.feedback
            , R.drawable.connect, R.drawable.student_id_card, R.drawable.diary, R.drawable.public_forum, R.drawable.notes, R.drawable.assignment};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_student_dashboard);

        init();
        setData();

    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Student Dashboard");
        arrayList = new ArrayList<>();
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerview.setNestedScrollingEnabled(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.student_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile) {
            startActivity(new Intent(this, StudentProfileActivity.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_results) {
            Intent intent = new Intent(context, StudentResult.class);
            startActivity(intent);
        } else if (id == R.id.nav_track_transport) {
            Intent intent = new Intent(context, Tracktransport.class);
            startActivity(intent);

        } else if (id == R.id.nav_attendance) {
            Intent intent = new Intent(context, StudentAttendance.class);
            startActivity(intent);

        } else if (id == R.id.nav_event_calender) {
            Intent intent = new Intent(context, Eventcalendar.class);
            startActivity(intent);

        } else if (id == R.id.nav_payments) {
            Intent intent = new Intent(context, FeesPaymentHistory.class);
            startActivity(intent);

        } else if (id == R.id.nav_time_table) {
            Intent intent = new Intent(context, StudentTimeTable.class);
            startActivity(intent);
        } else if (id == R.id.nav_complaint) {
            Intent intent = new Intent(context, ComplaintActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_connect_to_teacher) {
            Intent intent = new Intent(context, ConnectToTeacher.class);
            startActivity(intent);
        } else if (id == R.id.nav_student_id_card) {
            Intent intent = new Intent(context, StudentIdCard.class);
            startActivity(intent);
        } else if (id == R.id.nav_dairy) {
            Intent intent = new Intent(context, StudentDiaryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_public_forum) {
            Intent intent = new Intent(context, PublicForumActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_notes) {
            Intent intent = new Intent(context, StudentNotesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_assignment) {
            Intent intent = new Intent(context, StudentAssignment.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            showLogoutBox();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showLogoutBox() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                StudentDashboard.this);

        alertDialog.setTitle("LOG OUT !");

        alertDialog.setMessage("Are you sure you want to Logout?");

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        AppUtils.setUserId(context, "");
                        Intent in = new Intent(context, LoginActivity.class);
                        startActivity(in);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        finish();

                    }

                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

        alertDialog.show();


    }

    private void setData() {
        ModelData model = new ModelData();
        model.setTitle("Result");
        arrayList.add(model);

        model = new ModelData();
        model.setTitle("Track Transport");
        arrayList.add(model);

        model = new ModelData();
        model.setTitle("Attendance");
        arrayList.add(model);

        model = new ModelData();
        model.setTitle("Event Calendar");
        arrayList.add(model);

        model = new ModelData();
        model.setTitle("Payments");
        arrayList.add(model);

        model = new ModelData();
        model.setTitle("Time Table");
        arrayList.add(model);

        model = new ModelData();
        model.setTitle("Complaint");
        arrayList.add(model);

        model = new ModelData();
        model.setTitle("Connect To Teacher");
        arrayList.add(model);

        model = new ModelData();
        model.setTitle("Student Id Card");
        arrayList.add(model);

        model = new ModelData();
        model.setTitle("Dairy");
        arrayList.add(model);

        model = new ModelData();
        model.setTitle("Public Forum");
        arrayList.add(model);

        model = new ModelData();
        model.setTitle("Notes");
        arrayList.add(model);

        model = new ModelData();
        model.setTitle("Assignments");
        arrayList.add(model);

        adapterParentdashbooard = new AdapterParentdashbooard(context, this, arrayList, imgaes);
        recyclerview.setAdapter(adapterParentdashbooard);

    }

    @Override
    public void onItemClickListener(int position, int flag) {

       /* switch (position) {
            case 9:
                Intent intent = new Intent(context, StudentDiaryActivity.class);
                startActivity(intent);
                break;
            case 10:
                Intent intent1 = new Intent(context, PublicForumActivity.class);
                startActivity(intent1);
                break;
            case 11:
                startActivity(new Intent(StudentDashboard.this, StudentNotesActivity.class));
                break;
*/
        if (flag == 1) {

            if (position == 0) {
                Intent intent = new Intent(context, StudentResult.class);
                startActivity(intent);
            } else if (position == 1) {
                Intent intent = new Intent(context, Tracktransport.class);
                startActivity(intent);
            } else if (position == 2) {
                Intent intent = new Intent(context, StudentAttendance.class);
                startActivity(intent);
            } else if (position == 3) {
                Intent intent = new Intent(context, Eventcalendar.class);
                startActivity(intent);
            } else if (position == 4) {
                Intent intent = new Intent(context, FeesPaymentHistory.class);
                startActivity(intent);
            } else if (position == 5) {
                Intent intent = new Intent(context, StudentTimeTable.class);
                startActivity(intent);
            } else if (position == 6) {
                Intent intent = new Intent(context, ComplaintActivity.class);
                startActivity(intent);
            } else if (position == 7) {
                Intent intent = new Intent(context, ConnectToTeacher.class);
                startActivity(intent);
            } else if (position == 8) {
                Intent intent = new Intent(context, StudentIdCard.class);
                startActivity(intent);
            } else if (position == 9) {
                Intent intent = new Intent(context, StudentDiaryActivity.class);
                startActivity(intent);
            } else if (position == 10) {
                Intent intent = new Intent(context, PublicForumActivity.class);
                startActivity(intent);
            } else if (position == 11) {
                Intent intent = new Intent(context, StudentNotesActivity.class);
                startActivity(intent);
            } else if (position == 12) {
                Intent intent = new Intent(context, StudentAssignment.class);
                startActivity(intent);
            }
        }

    }
}
