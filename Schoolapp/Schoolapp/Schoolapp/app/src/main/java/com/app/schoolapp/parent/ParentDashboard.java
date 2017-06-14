package com.app.schoolapp.parent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.app.schoolapp.R;
import com.app.schoolapp.adapter.AdapterParentdashbooard;
import com.app.schoolapp.interfaces.OnCustomItemClicListener;
import com.app.schoolapp.model.ModelData;

import java.util.ArrayList;

public class ParentDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnCustomItemClicListener {

    private Context context;
    private RecyclerView recyclerview;
    private AdapterParentdashbooard adapterParentdashbooard;
    private ArrayList<ModelData> arrayList;
    Integer imgaes[] = {R.drawable.wardresult, R.drawable.tracktrabsport, R.drawable.attandance, R.drawable.calnder, R.drawable.payments, R.drawable.timetable, R.drawable.feedback
            , R.drawable.connect, R.drawable.student_id_card, R.drawable.diary, R.drawable.public_forum};
    private ModelData model;
    private ImageView image_show;
    private ScrollView scrollview;
    boolean isVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard);
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
        model.setTitle("Ward's Result");
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
        model.setTitle("Ward's Time Table");
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


        adapterParentdashbooard = new AdapterParentdashbooard(context, this, arrayList,imgaes);
        recyclerview.setAdapter(adapterParentdashbooard);

    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        getMenuInflater().inflate(R.menu.parent_dashboard, menu);
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
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(context, ParentProfile.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_attendance) {
            Intent intent = new Intent(context, StudentAttendance.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClickListener(int position, int flag) {
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
            }
        }
    }
}
