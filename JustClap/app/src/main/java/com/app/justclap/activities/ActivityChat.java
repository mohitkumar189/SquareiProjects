package com.app.justclap.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.app.justclap.R;
import com.app.justclap.adapters.AdapterChatDetail;
import com.app.justclap.asyntask.AsyncPostDataFragmentNoProgress;
import com.app.justclap.asyntask.AsyncPostDataResponseNoProgress;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.model.ModelChat;
import com.app.justclap.model.ModelContactList;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class ActivityChat extends AppCompatActivity implements OnCustomItemClicListener {

    Context context;
    RecyclerView chatList;
    AdapterChatDetail adapterChatDetail;
    ArrayList<ModelChat> chatListData;
    EditText edtMessage;
    Toolbar toolbar;
    TextView username;
    String reciever_id = "", searchID = "";
    ImageView imgSendMessage, image_user;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager layoutManager;
    int skipCount = 0;
    private boolean loading = true, isActivityVisible = true;
    String maxlistLength = "";
    ModelChat chatData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_detail);
        context = this;
        init();
        setListener();
        AppUtils.setIsChatVisible(context, true);
        adapterChatDetail = new AdapterChatDetail(context, this, chatListData);
        chatList.setAdapter(adapterChatDetail);
        // syncData();
        skipCount = 0;

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                2);
        nameValuePairs.add(new BasicNameValuePair(
                "receiverID", reciever_id));

        nameValuePairs.add(new BasicNameValuePair(
                "searchID", searchID));

        nameValuePairs.add(new BasicNameValuePair(
                "skipCount", skipCount + ""));


        new SyncDataToServer(nameValuePairs, 1, getResources().getString(R.string.url_base_new) + getResources().getString(R.string.listMassages));


    }

    private void init() {
        edtMessage = (EditText) findViewById(R.id.edit_message);
        imgSendMessage = (ImageView) findViewById(R.id.send_message);
        image_user = (ImageView) findViewById(R.id.image_user);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        chatList = (RecyclerView) findViewById(R.id.list_request);
        //==============================================
        username = (TextView) findViewById(R.id.username);
        chatListData = new ArrayList<>();
        Intent in = getIntent();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //  getSupportActionBar().setTitle(in.getExtras().getString("name"));
        reciever_id = in.getExtras().getString("reciever_id");
        AppUtils.setChatUserId(context, reciever_id);


        searchID = in.getExtras().getString("searchID");
        //=============================
        username.setText(in.getExtras().getString("name"));

        if (!in.getExtras().getString("image").equalsIgnoreCase("")) {
            Picasso.with(context)
                    .load(in.getExtras().getString("image"))
                    .placeholder(R.drawable.user)
                    .transform(new CircleTransform())
                    .into(image_user);
        }

        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        chatList.setLayoutManager(layoutManager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = edtMessage.getText().toString();
                if (!edtMessage.getText().toString().equalsIgnoreCase("")) {
                    try {
                        Calendar calander = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                        Date date = calander.getTime();
                        String ISO_FORMAT = "dd-MM-yyyy HH:mm";
                        SimpleDateFormat sdf = new SimpleDateFormat(ISO_FORMAT);
                        long time = System.currentTimeMillis();
                        ModelChat chatData = new ModelChat();
                        chatData.setSender_id(AppUtils.getUserIdChat(context));
                        chatData.setMessage(msg);
                        chatData.setSender_name(AppUtils.getUserNameChat(context));
                        chatData.setDate_time(sdf.format(date));
                        chatListData.add(chatData);
                       /* adapterChatDetail.notifyDataSetChanged();
*/
                        chatList.setAdapter(adapterChatDetail);

                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                                2);
                        nameValuePairs.add(new BasicNameValuePair(
                                "receiverID", reciever_id));
                        nameValuePairs.add(new BasicNameValuePair(
                                "senderID", AppUtils.getUserIdChat(context)));
                        nameValuePairs.add(new BasicNameValuePair(
                                "message", msg));

                        nameValuePairs.add(new BasicNameValuePair(
                                "searchID", searchID));

                        new SendDataToServer(nameValuePairs, getResources().getString(R.string.url_base_new) + getResources().getString(R.string.sendMessage));

                        edtMessage.setText("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });


/*
        chatList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if ((AppUtils.isNetworkAvailable(context))) {

                    if (!maxlistLength.equalsIgnoreCase(chatListData.size() + "")) {
                        if (dy > 0) //check for scroll down
                        {

                            visibleItemCount = layoutManager.getChildCount();
                            totalItemCount = layoutManager.getItemCount();
                            pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                            if (loading) {
                                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                    loading = false;
                                  */
/*  chatData = new ModelChat();
                                    chatData.setRowType(2);
                                    chatListData.add(chatData);
                                    adapterChatDetail.notifyDataSetChanged();
*//*

                                    skipCount = skipCount + 10;

                                    try {

                                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                                                2);
                                        nameValuePairs.add(new BasicNameValuePair(
                                                "receiverID", reciever_id));

                                        nameValuePairs.add(new BasicNameValuePair(
                                                "searchID", searchID));

                                        nameValuePairs.add(new BasicNameValuePair(
                                                "skipCount", skipCount + ""));


                                        new SyncDataToServer(nameValuePairs, 2, getResources().getString(R.string.url_base_new) + getResources().getString(R.string.listMassages));


                                    } catch (Exception e) {
                                        e.printStackTrace();

                                    }
                                    //Do pagination.. i.e. fetch new data
                                }
                            }
                        }
                    } else {

                        Log.e("maxlength", "*" + chatListData.size());
                    }
                }
            }
        });
*/


    }


/*
    private void showMessages() {
        try {
            chatListData.removeAll(chatListData);
            chatListData.addAll(db.fetchData(AppUtils.getUserIdChat(context), reciever_id, 1, 1));
           // adapterChatDetail.notifyDataSetChanged();
            chatList.setAdapter(adapterChatDetail);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setStackFromEnd(true);
            chatList.setLayoutManager(linearLayoutManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/


    /*private void updateMessageList() {
        try {
            chatListData.addAll(db.fetchData(AppUtils.getUserIdChat(context), reciever_id, 1, 1));
            adapterChatDetail.notifyDataSetChanged();
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setStackFromEnd(true);
            chatList.setLayoutManager(linearLayoutManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    @Override
    public void onItemClickListener(int position, int flag) {

    }


    public class SendDataToServer extends AsyncTask<String, Void, String> {
        String url;
        Context context;
        List<NameValuePair> requestData;

        public SendDataToServer(List<NameValuePair> requestData, String url) {
            // TODO Auto-generated constructor stub
            this.context = context;
            this.requestData = requestData;
            this.url = url;
            this.execute(new String[]{});
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            if (requestData == null) {
                requestData = new ArrayList<NameValuePair>(2);
            }
            Log.e("requestData", url + "***" + requestData.toString());
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String responce = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams
                    .setConnectionTimeout(httpclient.getParams(), 11000);
            HttpPost httppost = new HttpPost(url);
            try {
                httppost.setEntity(new UrlEncodedFormEntity(requestData, HTTP.UTF_8));
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                if (response != null) {
                    HttpEntity resEntity = response.getEntity();
                    responce = EntityUtils.toString(resEntity);
                    // .....Read the response
                }
            } catch (ClientProtocolException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }

            return responce;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                Log.e("Response", "***" + result);
                if (result != null) {
                    JSONObject jObject = new JSONObject(result);
                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public class SyncDataToServer extends AsyncTask<String, Void, String> {
        String url;
        Context context;
        List<NameValuePair> requestData;
        int method;

        public SyncDataToServer(List<NameValuePair> requestData, int method, String url) {
            // TODO Auto-generated constructor stub
            this.context = context;
            this.requestData = requestData;
            this.url = url;
            this.method = method;
            this.execute(new String[]{});
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            if (requestData == null) {
                requestData = new ArrayList<NameValuePair>(2);
            }
            Log.e("requestData", url + "***" + requestData.toString());
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String responce = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpConnectionParams
                    .setConnectionTimeout(httpclient.getParams(), 11000);
            HttpPost httppost = new HttpPost(url);
            try {
                httppost.setEntity(new UrlEncodedFormEntity(requestData, HTTP.UTF_8));
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                if (response != null) {
                    HttpEntity resEntity = response.getEntity();
                    responce = EntityUtils.toString(resEntity);
                    // .....Read the response
                }
            } catch (ClientProtocolException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }

            return responce;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                Log.e("Response", "***" + result);
                if (result != null) {

                    if (method == 1) {
                        JSONObject jObject = new JSONObject(result);
                        JSONObject commandResult = jObject.getJSONObject("commandResult");
                        if (commandResult.getString("success").equalsIgnoreCase("1")) {

                            maxlistLength = commandResult.getString("total");
                            JSONArray messageList = commandResult.getJSONArray("data");
                            chatListData.clear();
                            for (int i = chatListData.size(); i < messageList.length(); i++) {
                                JSONObject chat = messageList.getJSONObject(i);
                                ModelChat chatData = new ModelChat();
                                chatData.setSender_id(chat.getString("senderID"));
                                chatData.setRowType(1);
                                chatData.setReciever_id(chat.getString("receiverID"));
                                chatData.setMessage(chat.getString("message"));
                                chatData.setSender_name(chat.getString("senderName"));
                                chatData.setReceiverName(chat.getString("receiverName"));
                                chatData.setDate_time(chat.getString("message_date"));
                                chatListData.add(chatData);
                            }

                            Collections.reverse(chatListData);
                            adapterChatDetail.notifyDataSetChanged();
                        }
                        if (isActivityVisible) {
                            syncData();
                        }
                    }
                } else {
                    if (isActivityVisible) {
                        syncData();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                if (isActivityVisible) {
                    syncData();
                }
            }
        }
    }


    private void syncData() {

        skipCount = 0;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);
                nameValuePairs.add(new BasicNameValuePair(
                        "receiverID", reciever_id));

                nameValuePairs.add(new BasicNameValuePair(
                        "searchID", searchID));

                nameValuePairs.add(new BasicNameValuePair(
                        "skipCount", skipCount + ""));
                new SyncDataToServer(nameValuePairs, 1, getResources().getString(R.string.url_base_new) + getResources().getString(R.string.listMassages));
            }
        }, 3000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActivityVisible = false;
        AppUtils.setIsChatVisible(context, false);
    }
}
