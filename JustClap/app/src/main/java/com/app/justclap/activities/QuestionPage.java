package com.app.justclap.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.R;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.app.justclap.adapters.AdapterQuestionpage;
import com.app.justclap.asyntask.AsyncPostDataResponse;
import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.app.justclap.utils.AppUtils;

public class QuestionPage extends AppCompatActivity implements OnCustomItemClicListener, ListenerPostData {


    ImageView image_back, image_background;
    TextView text_services_name;
    ListView list_services;
    RelativeLayout rl_bottom;
    AdapterQuestionpage adapterQuestionpage;
    Context context;
    ModelService serviceDetail;
    ModelService iconDetail;
    ArrayList<ModelService> question_list;
    ArrayList<ModelService> service_list;
    String serviceId = "", vendor_naukri = "2", isUniDirectional = "1", is_naukri = "";
    private BroadcastReceiver broadcastReceiver;
    ConnectionDetector cd;
    RelativeLayout rl_main_layout, rl_network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_question_page);

        context = this;
        init();
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

        setListener();
        cd = new ConnectionDetector(this);
        question_list = new ArrayList<>();
        service_list = new ArrayList<>();
        Intent in = getIntent();

        if (in.hasExtra("servicename")) {
            text_services_name.setText(in.getExtras().getString("servicename"));
        }
        serviceId = in.getExtras().getString("serviceid");

        if (in.hasExtra("Is_uniDirectional")) {
            isUniDirectional = in.getStringExtra("Is_uniDirectional");
        }

        if (in.hasExtra("vendor_naukri")) {
            vendor_naukri = in.getStringExtra("vendor_naukri");
        }
        getQuestionList();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);


    }


    private void init() {
        overridePendingTransition(R.anim.enter,
                R.anim.exit);
        rl_main_layout = (RelativeLayout) findViewById(R.id.rl_main_layout);
        rl_network = (RelativeLayout) findViewById(R.id.rl_network);

        image_back = (ImageView) findViewById(R.id.image_back);
        image_background = (ImageView) findViewById(R.id.image_background);
        text_services_name = (TextView) findViewById(R.id.text_services_name);
        list_services = (ListView) findViewById(R.id.list_services);
        rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);


    }

    private void setListener() {

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);
            }
        });

        image_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String questionArray = question_list.get(0).getQueriesArray();
                try {
                    JSONArray questionArray1 = new JSONArray(questionArray);

                    if (questionArray1.length() != 0) {
                        //  Log.e("servicename"+"**", question_list.get(0).getServiceName());
                        Intent in = new Intent(QuestionPage.this, SubmitServiceQuestions.class);
                        in.putExtra("queryArray", question_list.get(0).getQueriesArray());
                        in.putExtra("servicename", question_list.get(0).getServiceName());
                        in.putExtra("vendor_naukri", vendor_naukri);
                        in.putExtra("isUniDirectional", isUniDirectional);
                        in.putExtra("serviceid", question_list.get(0).getServiceID());
                        in.putExtra("is_naukri", is_naukri);
                        startActivity(in);
                    } else {

                        AppUtils.showCustomAlert(QuestionPage.this, "Please try again later.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        rl_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String questionArray = question_list.get(0).getQueriesArray();
                try {
                    JSONArray questionArray1 = new JSONArray(questionArray);

                    if (questionArray1.length() != 0) {
                        //  Log.e("servicename"+"**", question_list.get(0).getServiceName());
                        Intent in = new Intent(QuestionPage.this, SubmitServiceQuestions.class);
                        in.putExtra("queryArray", question_list.get(0).getQueriesArray());
                        in.putExtra("servicename", question_list.get(0).getServiceName());
                        in.putExtra("vendor_naukri", vendor_naukri);
                        in.putExtra("isUniDirectional", isUniDirectional);
                        in.putExtra("serviceid", question_list.get(0).getServiceID());
                        in.putExtra("is_naukri", is_naukri);
                        startActivity(in);
                    } else {

                        AppUtils.showCustomAlert(QuestionPage.this, "Please try again later.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void getQuestionList() {

        try {

            // for gcm condition check
            // =================================================

            cd = new ConnectionDetector(context);

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present

                rl_main_layout.setVisibility(View.GONE);
                rl_network.setVisibility(View.VISIBLE);

                // AppUtils.showCustomAlert(QuestionPage.this, getResources().getString(R.string.message_network_problem));

                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "serviceID", serviceId));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "isUniDirectional", isUniDirectional));


                new AsyncPostDataResponse(QuestionPage.this, 1, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.getServiceDetails));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onItemClickListener(int position, int flag) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_BACK)
        {
            finish();
            overridePendingTransition(R.anim.left_to_right,
                    R.anim.right_to_left);
        }

        return super.onKeyDown(keyCode, event);


    }



    @Override
    public void onPostRequestSucess(int position, String response) {

        try {

            if (position == 1) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {


                    JSONObject data = commandResult.getJSONObject("data");
                    text_services_name.setText(data.getString("serviceName"));

                    is_naukri = data.getString("is_naukri");
                    serviceDetail = new ModelService();
                    serviceDetail.setServiceID(data.getString("serviceID"));
                    //  serviceDetail.setServiceTitle(data.getString("serviceTitle"));
                    serviceDetail.setServiceDescription(data.getString("serviceDescription"));
                    serviceDetail.setQueriesArray(data.getJSONArray("queries").toString());
                    serviceDetail.setServiceName(data.getString("serviceName"));
                    //  serviceDetail.setServiceImage(data.getString("serviceImage"));
                    serviceDetail.setBgimage1(data.getString("bgImage1"));
                    serviceDetail.setBgimage2(data.getString("bgImage2"));
                    serviceDetail.setBgimage3(data.getString("bgImage3"));
                    question_list.add(serviceDetail);

                    JSONArray jo = data.getJSONArray("images");
                    for (int i = 0; i < jo.length(); i++) {

                        JSONObject j1 = jo.getJSONObject(i);

                        iconDetail = new ModelService();
                        iconDetail.setIconImage1(j1.getString("icon"));
                        iconDetail.setIconText(j1.getString("name"));
                        service_list.add(iconDetail);

                    }


                    Picasso.with(context)
                            .load(context.getResources().getString(R.string.img_url)
                                    + question_list.get(0).getBgimage1())
                            // .transform(new CircleTransform())
                            // .placeholder(R.drawable.profile_about_user)
                            .into(image_background);


                    adapterQuestionpage = new AdapterQuestionpage(getApplicationContext(), this, service_list);
                    list_services.setAdapter(adapterQuestionpage);
                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);

                } else {
                    Toast.makeText(context, commandResult.getString("message"),
                            Toast.LENGTH_SHORT).show();

                    finish();
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onPostRequestFailed(int position, String response) {
        AppUtils.showCustomAlert(QuestionPage.this, getResources().getString(R.string.problem_server));
    }
}
