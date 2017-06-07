package com.app.veraxe.student;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.veraxe.R;
import com.app.veraxe.adapter.AdapterStudentFeedbackDetail;
import com.app.veraxe.adapter.AdapterStudentFeedbackList;
import com.app.veraxe.asyncTask.CommonAsyncTaskHashmap;
import com.app.veraxe.interfaces.ApiResponse;
import com.app.veraxe.interfaces.ConnectionDetector;
import com.app.veraxe.interfaces.OnCustomItemClicListener;
import com.app.veraxe.model.ModelStudent;
import com.app.veraxe.utils.AppUtils;
import com.app.veraxe.utils.Constant;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 06-01-2016.
 */
public class StudentFeedbackDetail extends AppCompatActivity implements OnCustomItemClicListener, ApiResponse {


    Context context;
    RecyclerView mRecyclerView;
    ModelStudent itemList;
    AdapterStudentFeedbackDetail adapterStudentFeedbackList;
    ArrayList<ModelStudent> arrayList;
    ConnectionDetector cd;
    RelativeLayout rl_main_layout, rl_network, rl_message;
    LinearLayoutManager layoutManager;
    FloatingActionButton btn_addfeedback;
    Toolbar toolbar;
    String feedback_id = "", status_id = "";
    private BroadcastReceiver broadcastReceiver;
    SwipeRefreshLayout swipe_refresh;
    ImageView send_message;
    TextView text_dapartment_label, text_feedbak_type, text_status, text_date, text_message;
    EditText edit_message;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedbackdetail);

        context = this;
        init();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        cd = new ConnectionDetector(context);
        arrayList = new ArrayList<>();
        setListener();

        feedback_id = getIntent().getStringExtra("feedback_id");
        status_id = getIntent().getStringExtra("status_id");
        if (status_id.equalsIgnoreCase("1")) {
            rl_message.setVisibility(View.GONE);
        } else {

            rl_message.setVisibility(View.VISIBLE);
        }

        Log.e("feeback_id", "*" + getIntent().getStringExtra("feedback_id"));
        messageList();
        mAdView = (AdView) findViewById(R.id.adView);
        if (AppUtils.getAdd_status(context).equalsIgnoreCase("1")) {
            mAdView.setVisibility(View.GONE);
            MobileAds.initialize(getApplicationContext(), "ca-app-pub-5990787515520459~9332653723");

            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        } else {
            mAdView.setVisibility(View.GONE);
        }


    }

    /**
     * Called when leaving the activity
     */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }


    /**
     * Called before the activity is destroyed
     */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }

        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, Constant.MIXPELTOKEN);
        mixpanel.track("Feedback List", null);
        mixpanel.flush();
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
        text_dapartment_label = (TextView) findViewById(R.id.text_dapartment_label);
        text_date = (TextView) findViewById(R.id.text_date);
        text_status = (TextView) findViewById(R.id.text_status);
        text_message = (TextView) findViewById(R.id.text_message);
        text_feedbak_type = (TextView) findViewById(R.id.text_feedbak_type);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rl_main_layout = (RelativeLayout) findViewById(R.id.rl_main_layout);
        rl_message = (RelativeLayout) findViewById(R.id.rl_message);
        rl_network = (RelativeLayout) findViewById(R.id.rl_network);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipe_refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimaryDark));
        btn_addfeedback = (FloatingActionButton) findViewById(R.id.btn_addfeedback);
        edit_message = (EditText) findViewById(R.id.edit_message);
        send_message = (ImageView) findViewById(R.id.send_message);
    }


    public void setListener() {


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                messageListRefresh();

            }
        });

        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!edit_message.getText().toString().equalsIgnoreCase("")) {
                    sendMessage();
                } else {
                    Toast.makeText(context, "Please enter message", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void messageList() {
        if (AppUtils.isNetworkAvailable(context)) {

            HashMap<String, Object> hm = new HashMap<>();
            hm.put("id", feedback_id);
            hm.put("authkey", Constant.AUTHKEY);

            String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.feedback_detail);
            new CommonAsyncTaskHashmap(1, context, this).getquery(url, hm);

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }

    }


    public void sendMessage() {
        if (AppUtils.isNetworkAvailable(context)) {

            HashMap<String, Object> hm = new HashMap<>();
            hm.put("id", feedback_id);
            hm.put("comment_text", edit_message.getText().toString());
            hm.put("student_id", AppUtils.getStudentId(context));
            hm.put("authkey", Constant.AUTHKEY);

            String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.feedback_addcomment);
            new CommonAsyncTaskHashmap(2, context, this).getquery(url, hm);

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }

    }


    public void feedbackClose() {
        if (AppUtils.isNetworkAvailable(context)) {

            HashMap<String, Object> hm = new HashMap<>();
            hm.put("id", feedback_id);
            hm.put("authkey", Constant.AUTHKEY);

            String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.feedback_close);
            new CommonAsyncTaskHashmap(4, context, this).getquery(url, hm);

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }

    }

    public void feedbackTrash() {
        if (AppUtils.isNetworkAvailable(context)) {

            HashMap<String, Object> hm = new HashMap<>();
            hm.put("id", feedback_id);
            hm.put("authkey", Constant.AUTHKEY);

            String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.feedback_trash);
            new CommonAsyncTaskHashmap(3, context, this).getquery(url, hm);

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }

    }


    public void messageListRefresh() {
        if (AppUtils.isNetworkAvailable(context)) {

            HashMap<String, Object> hm = new HashMap<>();
            hm.put("id", feedback_id);
            hm.put("authkey", Constant.AUTHKEY);

            String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.feedback_detail);
            new CommonAsyncTaskHashmap(1, context, this).getqueryNoProgress(url, hm);

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onItemClickListener(int position, int flag) {

        if (flag == 1) {

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        if (id == R.id.action_trash) {

            feedbackTrash();

        } else if (id == R.id.action_close) {

            feedbackClose();
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void getResponse(int method, JSONObject response) {
        try {
            if (method == 1) {
                if (response.getString("response").equalsIgnoreCase("1")) {

                    JSONObject result = response.getJSONObject("result");

                    text_dapartment_label.setText(result.getString("department_label"));
                    text_feedbak_type.setText(result.getString("feedback_type_label"));
                    text_status.setText(result.getString("status_name"));
                    text_date.setText(result.getString("datetime"));
                    text_message.setText(result.getString("message_text"));

                    if (result.getString("feedback_type_id").equalsIgnoreCase("3")) {
                        text_feedbak_type.setBackgroundColor(getResources().getColor(R.color.complaint_color));
                    } else if (result.getString("feedback_type_id").equalsIgnoreCase("1")) {
                        text_feedbak_type.setBackgroundColor(getResources().getColor(R.color.feedback_color));
                    } else if (result.getString("feedback_type_id").equalsIgnoreCase("2")) {
                        text_feedbak_type.setBackgroundColor(getResources().getColor(R.color.complement_color));
                    }


                    if (result.getString("status_id").equalsIgnoreCase("1")) {
                        text_status.setBackgroundColor(getResources().getColor(R.color.text_color_lightblue));
                    } else {
                        text_status.setBackgroundColor(getResources().getColor(R.color.red_color));
                    }


                    JSONArray array = result.getJSONArray("comments");
                    arrayList.clear();
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jo = array.getJSONObject(i);
                        itemList = new ModelStudent();

                        itemList.setId(jo.getString("id"));
                        itemList.setFrom(jo.getString("from"));
                        itemList.setDatetime(jo.getString("datetime"));
                        itemList.setName(jo.getString("name"));
                        itemList.setRowType(1);
                        itemList.setMessage_text(jo.getString("comment_text"));

                        arrayList.add(itemList);

                    }
                    adapterStudentFeedbackList = new AdapterStudentFeedbackDetail(context, this, arrayList);
                    mRecyclerView.setAdapter(adapterStudentFeedbackList);
                    if (swipe_refresh != null) {
                        swipe_refresh.setRefreshing(false);
                    }
                } else {

                    Toast.makeText(context, response.getString("msg"), Toast.LENGTH_SHORT).show();
                }


            } else if (method == 2) {
                if (response.getString("response").equalsIgnoreCase("1")) {
                    edit_message.setText("");
                    messageListRefresh();

                } else {
                    Toast.makeText(context, response.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } else if (method == 3) {
                if (response.getString("response").equalsIgnoreCase("1")) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(context, response.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } else if (method == 4) {
                if (response.getString("response").equalsIgnoreCase("1")) {
                    setResult(RESULT_OK);
                    finish();

                } else {
                    Toast.makeText(context, response.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
