package com.app.anmolenterprise.activities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.anmolenterprise.R;
import com.app.anmolenterprise.adapter.AdapterUserDashBoard;
import com.app.anmolenterprise.aynctask.CommonAsyncTask;
import com.app.anmolenterprise.interfaces.ApiResponse;
import com.app.anmolenterprise.interfaces.OnCustomItemClicListener;
import com.app.anmolenterprise.model.ModelData;
import com.app.anmolenterprise.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ApiResponse, OnCustomItemClicListener {

    private Context context;
    private RecyclerView recycler_list;
    private ModelData model;
    private ArrayList<ModelData> listServices;
    private AdapterUserDashBoard adapterUserDashBoard;
    private TextView text_name, textnumber;
    private NavigationView navigationView;
    private ImageView imag_banner;
    String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, android.Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    int PERMISSION_ALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        context = this;
        init();
        setListener();
        categoryList();
    }

    private void setListener() {

        imag_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("market://details?id=com.pack.gift.packmygift");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=com.pack.gift.packmygift")));
                }


            }
        });
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void categoryList() {

        if (AppUtils.isNetworkAvailable(context)) {

            String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.getServiceCharge);
            new CommonAsyncTask(1, context, this).getquery(url);

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }

    private void init() {
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        listServices = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        textnumber = (TextView) header.findViewById(R.id.textnumber);
        text_name = (TextView) header.findViewById(R.id.text_name);
        textnumber.setText(AppUtils.getUserMobile(context));
        text_name.setText(AppUtils.getUserName(context));
        imag_banner = (ImageView) findViewById(R.id.imag_banner);

        recycler_list = (RecyclerView) findViewById(R.id.recycler_list);
        recycler_list.setLayoutManager(new GridLayoutManager(context, 2));
    }

    @Override
    protected void onResume() {
        super.onResume();

        textnumber.setText(AppUtils.getUserMobile(context));
        text_name.setText(AppUtils.getUserName(context));


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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            Intent in = new Intent(context, Login.class);
            startActivity(in);
        } else if (id == R.id.nav_profile) {
            Intent in = new Intent(context, UpdateProfile.class);
            startActivity(in);
        } else if (id == R.id.nav_history) {
            Intent in = new Intent(context, BookingHistory.class);
            startActivity(in);
        } else if (id == R.id.nav_contact) {
            try {
                String number = "9818143758";
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);
            } catch (SecurityException e) {
                e.printStackTrace();
            }

        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Anmol ,a fastest growing App ,It is very useful for Users" +
                    ", So why are you waiting Get it now." + "http://play.google.com/store/apps/details?id=" + context.getPackageName());
            sendIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (id == R.id.nav_rateus) {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
            }

        } else if (id == R.id.nav_logout) {
            showLogoutBox();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showLogoutBox() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                UserDashboard.this);

        alertDialog.setTitle("LOG OUT !");

        alertDialog.setMessage("Are you sure you want to Logout?");

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        AppUtils.setUserId(context, "");
                        Intent intent = new Intent(context, Login.class);
                        startActivity(intent);
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
    public void onPostSuccess(int method, JSONObject response) {
        try {
            if (method == 1) {
                JSONObject commandResult = response.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONArray array = commandResult.getJSONArray("data");
                    listServices.clear();
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jo = array.getJSONObject(i);
                        model = new ModelData();

                        model.setCategory_id(jo.getString("catId"));
                        model.setCategory_name(jo.getString("catName"));
                        model.setImage(jo.getString("image_url"));
                        model.setService_charge(jo.getString("service_charge"));
                        model.setSubCategoryArray(jo.getJSONArray("subCategory").toString());

                        listServices.add(model);
                    }
                    adapterUserDashBoard = new AdapterUserDashBoard(context, this, listServices);
                    recycler_list.setAdapter(adapterUserDashBoard);

                } else {
                    Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostFail(int method, String response) {

    }

    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 1) {
            Intent in = new Intent(context, SubCategory.class);
            in.putExtra("categories", listServices.get(position).getSubCategoryArray());
            startActivity(in);
        }
    }
}
