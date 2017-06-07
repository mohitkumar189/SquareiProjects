package com.app.justclap.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.justclap.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.app.justclap.utils.AppUtils;

public class ServiceNaukri extends AppCompatActivity implements OnCustomItemClicListener {


    Context context;
    ModelService serviceDetail;
    ConnectionDetector cd;
    ArrayList<ModelService> service_list;
    String serviceArray = "", catName = "", is_naukri = "";
    Toolbar toolbar;
    RelativeLayout rl_search, rl_overlay, rl_share;
    TextView text_job, text_matri;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_naukri);

        context = this;
        init();
        service_list = new ArrayList<>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent in = getIntent();
        serviceArray = in.getExtras().getString("servicearray");
        catName = in.getExtras().getString("categoryName");
        is_naukri = in.getExtras().getString("is_naukri");
        getSupportActionBar().setTitle(catName);


        if (is_naukri.equalsIgnoreCase("1")) {

            text_job.setText("Search a job");
            text_matri.setText("share a job");
        } else if (is_naukri.equalsIgnoreCase("2")) {

            text_matri.setText("Share  a matrimonial");
            text_job.setText("Looking for Matrimonial");
        }
        setListener();
        parseCategoryData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }

    private void init() {
        overridePendingTransition(R.anim.enter,
                R.anim.exit);
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

        text_matri = (TextView) findViewById(R.id.text_matri);
        text_job = (TextView) findViewById(R.id.text_job);
        rl_search = (RelativeLayout) findViewById(R.id.rl_search);
        rl_overlay = (RelativeLayout) findViewById(R.id.rl_overlay);
        rl_share = (RelativeLayout) findViewById(R.id.rl_share);
    }

    private void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO bind to drawer with... injection?
                overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);
                finish();
            }

        });

        rl_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (service_list.size() > 0) {

                    if (service_list.get(0).getServiceName().equalsIgnoreCase("Naukri-Search")) {
                        Intent in = new Intent(ServiceNaukri.this, QuestionPage.class);
                        in.putExtra("servicename", service_list.get(0).getServiceName());
                        in.putExtra("serviceid", service_list.get(0).getServiceID());
                        startActivity(in);
                    } else if (service_list.size() > 1) {
                        if (service_list.get(1).getServiceName().equalsIgnoreCase("Naukri-Search")) {
                            Intent in = new Intent(ServiceNaukri.this, QuestionPage.class);
                            in.putExtra("servicename", service_list.get(1).getServiceName());
                            in.putExtra("serviceid", service_list.get(1).getServiceID());
                            startActivity(in);
                        }

                    } else {
                        AppUtils.showCustomAlert(ServiceNaukri.this, "No Record Found");
                    }
                } else {

                    AppUtils.showCustomAlert(ServiceNaukri.this, "No Record Found");
                }

            }
        });

        rl_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (service_list.size() > 0) {

                        if (service_list.get(0).getServiceName().equalsIgnoreCase("Naukri-Share")) {
                            Intent in = new Intent(ServiceNaukri.this, QuestionPage.class);
                            in.putExtra("servicename", service_list.get(0).getServiceName());
                            in.putExtra("serviceid", service_list.get(0).getServiceID());
                            startActivity(in);
                        } else if (service_list.size() > 1) {
                            if (service_list.get(1).getServiceName().equalsIgnoreCase("Naukri-Share")) {
                                Intent in = new Intent(ServiceNaukri.this, QuestionPage.class);
                                in.putExtra("servicename", service_list.get(1).getServiceName());
                                in.putExtra("serviceid", service_list.get(1).getServiceID());
                                startActivity(in);
                            }

                        } else {
                            AppUtils.showCustomAlert(ServiceNaukri.this, "You Cannot share, Please try later.");
                        }
                    } else {

                        AppUtils.showCustomAlert(ServiceNaukri.this, "You Cannot share, Please try later.");

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

    }


    private void parseCategoryData() {


        try {

            JSONArray services = new JSONArray(serviceArray);


            for (int i = 0; i < services.length(); i++) {

                JSONObject jo = services.getJSONObject(i);
                serviceDetail = new ModelService();
                serviceDetail.setServiceID(jo.getString("serviceID"));
                serviceDetail.setServiceName(jo.getString("serviceName"));
                serviceDetail.setServiceIcon(jo.getString("serviceIcon"));
                service_list.add(serviceDetail);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onItemClickListener(int position, int flag) {


        if (flag == 1) {
            Log.e("serviceid", service_list.get(position).getServiceID());
            Intent in = new Intent(ServiceNaukri.this, QuestionPage.class);
            in.putExtra("servicename", service_list.get(position).getServiceName());
            in.putExtra("serviceid", service_list.get(position).getServiceID());

            startActivity(in);

            // finish();

        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);
    }


}
