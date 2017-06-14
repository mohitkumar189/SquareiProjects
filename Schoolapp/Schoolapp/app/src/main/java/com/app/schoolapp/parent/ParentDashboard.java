package com.app.schoolapp.parent;

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
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.app.schoolapp.R;
import com.app.schoolapp.activities.LoginActivity;
import com.app.schoolapp.adapter.AdapterParentdashbooard;
import com.app.schoolapp.adapter.NavigationViewAdapter;
import com.app.schoolapp.asynctask.CommonAsyncTaskHashmap;
import com.app.schoolapp.interfaces.ApiResponse;
import com.app.schoolapp.interfaces.OnCustomItemClicListener;
import com.app.schoolapp.model.ModelData;
import com.app.schoolapp.model.NavigationViewModel;
import com.app.schoolapp.student.StudentAssignment;
import com.app.schoolapp.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParentDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnCustomItemClicListener, ApiResponse {

    private Context context;
    private RecyclerView recyclerview;
    private AdapterParentdashbooard adapterParentdashbooard;
    private ArrayList<ModelData> arrayList;
    Integer imgaes[] = {R.drawable.wardresult, R.drawable.tracktrabsport, R.drawable.attandance, R.drawable.calnder, R.drawable.payments, R.drawable.timetable, R.drawable.feedback
            , R.drawable.connect, R.drawable.student_id_card, R.drawable.diary, R.drawable.public_forum, R.drawable.assignment};
    private ImageView image_show;
    private ScrollView scrollview;
    boolean isVisible = true;
    private RecyclerView navRecyclerView1, navRecyclerView2;
    private List<NavigationViewModel> navModelMenu;
    private List<NavigationViewModel> navModelWards;
    private ImageView ivNavArrow;
    private TextView tvNavWardName;
    private TextView tvNavWardRoll;
    private DrawerLayout drawer;

    private boolean isMenuList = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("onCreate", "ParentDashboard");

        setContentView(R.layout.activity_parent_dashboard);
        context = this;
        init();
        setNavigationView();
        setData();
        initListener();
        setNavContent();
        if (!AppUtils.getWardsStatus(context))
            getWardsList();
        else setWardsData();

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

    private void setNavigationView() {
        navModelMenu.add(new NavigationViewModel("Ward's Result", getResources().getDrawable(R.drawable.app_logo), ""));
        navModelMenu.add(new NavigationViewModel("Track Transport", getResources().getDrawable(R.drawable.app_logo), ""));
        navModelMenu.add(new NavigationViewModel("Attendance", getResources().getDrawable(R.drawable.app_logo), ""));
        navModelMenu.add(new NavigationViewModel("Event Calendar", getResources().getDrawable(R.drawable.app_logo), ""));
        navModelMenu.add(new NavigationViewModel("Payments", getResources().getDrawable(R.drawable.app_logo), ""));
        navModelMenu.add(new NavigationViewModel("Ward's Time Table", getResources().getDrawable(R.drawable.app_logo), ""));
        navModelMenu.add(new NavigationViewModel("Complaint", getResources().getDrawable(R.drawable.app_logo), ""));
        navModelMenu.add(new NavigationViewModel("Connect To Teacher", getResources().getDrawable(R.drawable.app_logo), ""));
        navModelMenu.add(new NavigationViewModel("Student Id Card", getResources().getDrawable(R.drawable.app_logo), ""));
        navModelMenu.add(new NavigationViewModel("Diary", getResources().getDrawable(R.drawable.app_logo), ""));
        navModelMenu.add(new NavigationViewModel("Public Forum", getResources().getDrawable(R.drawable.app_logo), ""));
        navModelMenu.add(new NavigationViewModel("Assignments", getResources().getDrawable(R.drawable.app_logo), ""));
        navModelMenu.add(new NavigationViewModel("Logout", getResources().getDrawable(R.drawable.app_logo), ""));


        NavigationViewAdapter navigationViewAdapter = new NavigationViewAdapter(context, this, navModelMenu, false);

        navRecyclerView1.setAdapter(navigationViewAdapter);
    }

    private void initListener() {
        ivNavArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavContent();
            }
        });
    }

    private void setNavContent() {
        if (!isMenuList) {
            navRecyclerView1.setVisibility(View.GONE);
            navRecyclerView2.setVisibility(View.VISIBLE);
            isMenuList = !isMenuList;
            ivNavArrow.setImageDrawable(getResources().getDrawable(R.drawable.drop_up_arrow));
        } else {
            ivNavArrow.setImageDrawable(getResources().getDrawable(R.drawable.drop_down_arrow));
            navRecyclerView1.setVisibility(View.VISIBLE);
            navRecyclerView2.setVisibility(View.GONE);
            isMenuList = !isMenuList;

        }
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

    private void setWardsData() {
        String wardsData = AppUtils.getWardsData(context);
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(wardsData);
            int length = jsonArray.length();
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    String name = jsonObject.getString("name");
                    navModelWards.add(new NavigationViewModel(name, getResources().getDrawable(R.drawable.app_logo), id));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //   navModelWards.add(new NavigationViewModel("Chota Bacha", getResources().getDrawable(R.drawable.app_logo)));
        NavigationViewAdapter navigationViewAdapter2 = new NavigationViewAdapter(context, this, navModelWards, true);
        navRecyclerView2.setAdapter(navigationViewAdapter2);

        tvNavWardName.setText(navModelWards.get(0).getNavText());
        setWardId(navModelWards.get(0).getId());
    }

    private void setWardId(String id) {
        AppUtils.setWardId(context, id);
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

        model = new ModelData();
        model.setTitle("Assignments");
        arrayList.add(model);


        adapterParentdashbooard = new AdapterParentdashbooard(context, this, arrayList, imgaes);
        recyclerview.setAdapter(adapterParentdashbooard);

    }

    private void init() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        arrayList = new ArrayList<>();
        navModelMenu = new ArrayList<>();
        navModelWards = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ivNavArrow = (ImageView) findViewById(R.id.ivNavArrow);
        tvNavWardName = (TextView) findViewById(R.id.tvNavWardName);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        navRecyclerView1 = (RecyclerView) findViewById(R.id.navRecyclerView1);
        navRecyclerView2 = (RecyclerView) findViewById(R.id.navRecyclerView2);
        navRecyclerView1.setLayoutManager(new LinearLayoutManager(context));
        navRecyclerView2.setLayoutManager(new LinearLayoutManager(context));
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

    private void getWardsList() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.wards_list) + "?client_id=" + AppUtils.getUserId(context);
                new CommonAsyncTaskHashmap(1, context, this).getquery(url);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile) {
            startActivity(new Intent(this, ParentProfile.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
/*
        if (id == R.id.nav_profile) {
            Intent intent = new Intent(context, ParentProfile.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_attendance) {
            Intent intent = new Intent(context, StudentAttendance.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            showLogoutBox();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showLogoutBox() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                ParentDashboard.this);

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
                Intent intent = new Intent(context, StudentAssignment.class);
                startActivity(intent);
            } else if (position == 12) {
                showLogoutBox();
            }
        } else if (flag == 2) {

            tvNavWardName.setText(navModelWards.get(position).getNavText());
            setWardId(navModelWards.get(position).getId());
        }

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            isMenuList = true;
        }
    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {
        switch (method) {
            case 1:
                try {
                    int success = response.getInt("success");
                    int error = response.getInt("error");
                    String message = response.getString("message");
                    if (success == 1 && error == 0) {
                        JSONArray jsonArray = response.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            String wardsData = jsonArray.toString();
                            AppUtils.setWardsData(context, wardsData);
                            AppUtils.setWardsStatus(context, true);
                            setWardsData();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onPostFail(int method, String response) {

    }
}
