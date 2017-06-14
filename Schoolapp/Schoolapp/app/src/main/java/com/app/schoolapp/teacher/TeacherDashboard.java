package com.app.schoolapp.teacher;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.app.schoolapp.R;
import com.app.schoolapp.activities.LoginActivity;
import com.app.schoolapp.adapter.AdapterTeacherDashbooard;
import com.app.schoolapp.interfaces.OnCustomItemClicListener;
import com.app.schoolapp.model.ModelData;
import com.app.schoolapp.parent.ComplaintActivity;
import com.app.schoolapp.parent.ConnectToTeacher;
import com.app.schoolapp.parent.Eventcalendar;
import com.app.schoolapp.parent.FeesPaymentHistory;
import com.app.schoolapp.parent.PublicForumActivity;
import com.app.schoolapp.parent.StudentAttendance;
import com.app.schoolapp.parent.StudentResult;
import com.app.schoolapp.parent.StudentTimeTable;
import com.app.schoolapp.utils.AppUtils;

import java.util.ArrayList;

public class TeacherDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnCustomItemClicListener {

    private Context context;
    private RecyclerView recyclerview;
    private AdapterTeacherDashbooard adapterTeacherDashbooard;
    private ArrayList<ModelData> arrayList;
    Integer imgaes[] = {R.drawable.wardresult, R.drawable.assignment, R.drawable.attandance, R.drawable.calnder, R.drawable.payments, R.drawable.timetable, R.drawable.feedback
            , R.drawable.connect/*, R.drawable.student_id_card*/, R.drawable.diary, R.drawable.public_forum};
    private ModelData model;
    private ImageView image_show;
    private ScrollView scrollview;
    boolean isVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("onCreate", "TeacherDashBoard");
        setContentView(R.layout.activity_teacher_dashboard);
        context = this;
        init();
        setData();

        scrollview = (ScrollView) findViewById(R.id.scrollview);
        image_show = (ImageView) findViewById(R.id.image_show);

        image_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible) {
                    // scrollview.setVisibility(View.GONE);
                    hideAnimation();
                    isVisible = false;
                } else {
                    //  scrollview.setVisibility(View.VISIBLE);
                    showAnimation();
                    isVisible = true;
                }
            }
        });
    }

    private void showAnimation() {
        Animation anim;
        anim = AnimationUtils.loadAnimation(context, R.anim.leftright);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                scrollview.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        scrollview.startAnimation(anim);
        image_show.startAnimation(anim);
    }

    private void hideAnimation() {
        Animation anim;
        anim = AnimationUtils.loadAnimation(context, R.anim.righttoleft);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                scrollview.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        scrollview.startAnimation(anim);
        image_show.startAnimation(anim);
    }


    private void setData() {
        ModelData model = new ModelData();
        model.setTitle("Student Result");
        arrayList.add(model);

        model = new ModelData();
        model.setTitle("Assignment");
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
        model.setTitle("Connect To Admin");
        arrayList.add(model);

/*        model = new ModelData();
        model.setTitle("Id Card");
        arrayList.add(model);*/

        model = new ModelData();
        model.setTitle("Dairy");
        arrayList.add(model);

        model = new ModelData();
        model.setTitle("Public Forum");
        arrayList.add(model);


        adapterTeacherDashbooard = new AdapterTeacherDashbooard(context, this, arrayList, imgaes);
        recyclerview.setAdapter(adapterTeacherDashbooard);

    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Teacher Dashboard");
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
      //  getMenuInflater().inflate(R.menu.parent_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_results) {
            Intent intent = new Intent(context, StudentResult.class);
            startActivity(intent);
        } else if (id == R.id.nav_assignment) {
            Intent intent = new Intent(context, AssignmentListActivity.class);
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
        } else if (id == R.id.nav_connect_to_admin) {
            Intent intent = new Intent(context, ConnectToTeacher.class);
            startActivity(intent);
        } else if (id == R.id.nav_dairy) {
            Intent intent = new Intent(context, TeacherDiaryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_public_forum) {
            Intent intent = new Intent(context, PublicForumActivity.class);
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
                TeacherDashboard.this);

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

    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 1) {

            if (position == 0) {
                Intent intent = new Intent(context, StudentResult.class);
                startActivity(intent);
            } else if (position == 1) {
                Intent intent = new Intent(context, AssignmentListActivity.class);
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
                Intent intent = new Intent(context, TeacherDiaryActivity.class);

/*
                Intent intent = new Intent(context, StudentIdCard.class);
*/
                startActivity(intent);
            } else if (position == 9) {
                Intent intent = new Intent(context, PublicForumActivity.class);

/*
                Intent intent = new Intent(context, TeacherDiaryActivity.class);
*/
                startActivity(intent);
            } else if (position == 10) {
                Intent intent = new Intent(context, PublicForumActivity.class);
                startActivity(intent);
            }
        }
    }
}
