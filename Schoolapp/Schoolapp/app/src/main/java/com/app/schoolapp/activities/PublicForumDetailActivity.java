package com.app.schoolapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.app.schoolapp.adapter.AdapterforumAnswerList;
import com.app.schoolapp.asynctask.CommonAsyncTaskHashmap;
import com.app.schoolapp.interfaces.ApiResponse;
import com.app.schoolapp.interfaces.OnCustomItemClicListener;
import com.app.schoolapp.model.ModelPublicForum;
import com.app.schoolapp.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PublicForumDetailActivity extends AppCompatActivity implements ApiResponse, OnCustomItemClicListener {
    private Context context;
    private Toolbar toolbar;
    private RecyclerView recycler_list;
    private BroadcastReceiver broadcastReceiver;
    private String id;
    private ArrayList<ModelPublicForum> modelPublicForum;
    private ModelPublicForum modelPublicForumData;
    private AdapterforumAnswerList adapterforumAnswerList;
    private TextView textViewQuestionDetail, textViewDate,  textViewAnswers, textViewWriter;
    private FloatingActionButton floatingActionButton;

    private AlertDialog answerPostDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_forum_detail);

        Intent intent = getIntent();
        //  id = intent.getStringExtra("questionId");
        modelPublicForumData = (ModelPublicForum) intent.getSerializableExtra("obj");
        Log.e("received question id:==", modelPublicForumData.getId());

        context = this;
        init();
        setData();
        getAnswers();
        initListeners();

    }

    private void init() {
        modelPublicForum = new ArrayList<>();
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        this.textViewQuestionDetail = (TextView) findViewById(R.id.textViewQuestionDetail);
        this.textViewWriter = (TextView) findViewById(R.id.textViewWriter);
        this.textViewAnswers = (TextView) findViewById(R.id.textViewAnswers);
        this.textViewDate = (TextView) findViewById(R.id.textViewDate);

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
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Forum");

        recycler_list = (RecyclerView) findViewById(R.id.recycler_list);
        recycler_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void setData() {
        textViewQuestionDetail.setText(modelPublicForumData.getContent());
        textViewWriter.setText(modelPublicForumData.getWrittenBy() + " (" + modelPublicForumData.getSubject() + ")");
        textViewDate.setText(modelPublicForumData.getCreated().substring(0, 10));
        textViewAnswers.setText("Answers: " + modelPublicForumData.getAnswers());

    }

    private void initListeners() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogForAnswer();
                // startActivity(new Intent(PublicForumDetailActivity.this, UploadQuestionActivity.class));
            }
        });
    }

    private void showDialogForAnswer() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_for_answer, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);
        //  dialogBuilder.setTitle("Communities");
        answerPostDialog = dialogBuilder.create();
        Button btnSubmit = (Button) dialogView.findViewById(R.id.btnSubmit);
        final EditText edInputAnswer = (EditText) dialogView.findViewById(R.id.edInputAnswer);
        answerPostDialog.show();
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
                hm.put("subject", modelPublicForumData.getSubject().toString());
                hm.put("content", answer);
                hm.put("refid", modelPublicForumData.getId());
                hm.put("conttype", "public");
                hm.put("client_id", AppUtils.getUserId(context));
                hm.put("appname", "school");
                //       http://squarei.in/api/v2/login?email=anderson.soyug@gmail.com&password=4105
                String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.upload_post);
                new CommonAsyncTaskHashmap(2, context, this).getqueryJson(url, hm);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAnswers() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.blog_answers) + "?client_id=" + AppUtils.getUserId(context) + "&refid=" + modelPublicForumData.getId();
                new CommonAsyncTaskHashmap(1, context, this).getquery(url);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {
        if (method == 1) {
            extractJsonAndSet(response);
        } else if (method == 2) {
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
        }

    }

    @Override
    public void onPostFail(int method, String response) {

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
                adapterforumAnswerList = new AdapterforumAnswerList(context, this, modelPublicForum);
                recycler_list.setAdapter(adapterforumAnswerList);
            } else {
                ///////////////show something here//////////
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemClickListener(int position, int flag) {

    }
}
