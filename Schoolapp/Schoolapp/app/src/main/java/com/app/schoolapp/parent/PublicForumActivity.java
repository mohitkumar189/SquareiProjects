package com.app.schoolapp.parent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.schoolapp.R;
import com.app.schoolapp.activities.PublicForumDetailActivity;
import com.app.schoolapp.adapter.AdapterPublicforum;
import com.app.schoolapp.asynctask.CommonAsyncTaskHashmap;
import com.app.schoolapp.interfaces.ApiResponse;
import com.app.schoolapp.interfaces.OnCustomItemClicListener;
import com.app.schoolapp.model.ModelData;
import com.app.schoolapp.model.ModelPublicForum;
import com.app.schoolapp.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PublicForumActivity extends AppCompatActivity implements OnCustomItemClicListener, ApiResponse {

    private Context context;
    private Toolbar toolbar;
    private RecyclerView recycler_list;
    private ArrayList<ModelData> arrayList;
    private ModelData modelDoctor;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BroadcastReceiver broadcastReceiver;
    private AdapterPublicforum adapterPublicForum;
    private FloatingActionButton floatingActionButton;
    private ArrayList<ModelPublicForum> modelPublicForum;
    private AlertDialog answerPostDialog;
    private int positionPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_forum);
        context = this;
        init();
        setListener();
        getBlogData();
        //setData();
    }

    private void init() {
        modelPublicForum = new ArrayList<>();

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
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimaryDark));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Public Forum");

        recycler_list = (RecyclerView) findViewById(R.id.recycler_list);
        recycler_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }

    private void getBlogData() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.blog) + "?client_id=" + AppUtils.getUserId(context);
                new CommonAsyncTaskHashmap(1, context, this).getquery(url);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PublicForumActivity.this, UploadQuestionActivity.class));
            }
        });
    }

    private void setData() {
        modelDoctor = new ModelData();

        arrayList.add(modelDoctor);
        arrayList.add(modelDoctor);
        arrayList.add(modelDoctor);
        arrayList.add(modelDoctor);
        arrayList.add(modelDoctor);
        arrayList.add(modelDoctor);
        arrayList.add(modelDoctor);

        recycler_list.setAdapter(adapterPublicForum);
    }


    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 1) {
            Intent intent = new Intent(context, PublicForumDetailActivity.class);
            Log.e("sent question id:==", modelPublicForum.get(position).getId());

            //     intent.putExtra("questionId", modelPublicForum.get(position).getId());
            intent.putExtra("obj", modelPublicForum.get(position));
            startActivity(intent);
        } else if (flag == 2) {
            positionPost = position;
            showDialogForAnswer();
        }
    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {
        switch (method) {
            case 1:
                extractJsonAndSet(response);
                break;
            case 3:
                try {
                    int success = response.getInt("success");
                    int error = response.getInt("error");
                    String message = response.getString("message");
                    if (success == 1 && error == 0) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        answerPostDialog.dismiss();
                    } else {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void extractJsonAndSet(JSONObject response) {
        try {
            int success = response.getInt("success");
            int error = response.getInt("error");
            String message = response.getString("message");
            if (success == 1 && error == 0) {
                JSONArray data = response.getJSONArray("data");
                int length = data.length();
                for (int i = 0; i < length; i++) {
                    JSONObject jsonObject = data.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    String subject = jsonObject.getString("subject");
                    String conttype = jsonObject.getString("conttype");
                    String content = jsonObject.getString("content");
                    String created = jsonObject.getString("created");
                    String writtenBy = jsonObject.getString("writtenBy");
                    JSONArray ans = jsonObject.getJSONArray("ans");
                    int ansLength = ans.length();
                    modelPublicForum.add(new ModelPublicForum(id, subject, conttype, created, writtenBy, "" + ansLength, content));

                }
                adapterPublicForum = new AdapterPublicforum(context, this, modelPublicForum);
                recycler_list.setAdapter(adapterPublicForum);
            } else {
                ///////////////show something here//////////
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostFail(int method, String response) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // getBlogData();
    }

    private void showDialogForAnswer() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_for_answer, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);
     //   dialogBuilder.setTitle("Write your answer");
        answerPostDialog = dialogBuilder.create();
        Button btnSubmit = (Button) dialogView.findViewById(R.id.btnSubmit);
        TextView tvQuestion = (TextView) dialogView.findViewById(R.id.tvQuestion);
        final EditText edInputAnswer = (EditText) dialogView.findViewById(R.id.edInputAnswer);
        answerPostDialog.show();
        tvQuestion.setText(modelPublicForum.get(positionPost).getContent());
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = edInputAnswer.getText().toString();
                if (input.length() > 10) {
                    sendPost(input);
                } else {
                    Toast.makeText(context, "Answer must have lenght 10", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendPost(String answer) {

        try {
            if (AppUtils.isNetworkAvailable(context)) {

                HashMap<String, String> hm = new HashMap<>();
                hm.put("subject", modelPublicForum.get(positionPost).getSubject().toString());
                hm.put("content", answer);
                hm.put("refid", modelPublicForum.get(positionPost).getId());
                hm.put("conttype", "public");
                hm.put("client_id", AppUtils.getUserId(context));
                hm.put("appname", "school");
                //       http://squarei.in/api/v2/login?email=anderson.soyug@gmail.com&password=4105
                String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.upload_post);
                new CommonAsyncTaskHashmap(3, context, this).getqueryJson(url, hm);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
