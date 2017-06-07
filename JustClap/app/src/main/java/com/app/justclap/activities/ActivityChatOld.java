package com.app.justclap.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.app.justclap.R;
import com.app.justclap.adapters.AdapterChatDetail;
import com.app.justclap.database.ChatDatabase;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.model.ModelChat;
import com.app.justclap.utils.AppUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ActivityChatOld extends AppCompatActivity implements OnCustomItemClicListener {

    Context context;
    RecyclerView chatList;
    AdapterChatDetail adapterChatDetail;
    ArrayList<ModelChat> chatListData;
    EditText edtMessage;
    Toolbar toolbar;
    ImageView imgSendMessage;
    String reciever_id = "";
    BroadcastReceiver updateChatListReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_detail);
        context = this;
        init();
        setListener();
        adapterChatDetail = new AdapterChatDetail(context, this, chatListData);
        chatList.setAdapter(adapterChatDetail);
        showMessages();
        registerChatReceiver();
    }

    private void init() {
        edtMessage = (EditText) findViewById(R.id.edit_message);
        imgSendMessage = (ImageView) findViewById(R.id.send_message);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        chatList = (RecyclerView) findViewById(R.id.list_request);
        //==============================================
        Intent in = getIntent();
        reciever_id = in.getExtras().getString("reciever_id");

        chatListData = new ArrayList<>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(in.getExtras().getString("name"));
        //=============================

        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        chatList.setLayoutManager(layoutManager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterChatReceiver();
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
                    ChatDatabase db = null;
                    try {
                        db = new ChatDatabase(getBaseContext());
                        db.open();

                        Calendar calander = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                        Date date = calander.getTime();
                        String ISO_FORMAT = "dd-MM-yyyy HH:mm";
                        SimpleDateFormat sdf = new SimpleDateFormat(ISO_FORMAT);
                        long time = System.currentTimeMillis();
                        ModelChat chatData = new ModelChat();
                        chatData.setSender_id(AppUtils.getUserIdChat(context));
                        chatData.setReciever_id(reciever_id);
                        chatData.setMessage(msg);
                        chatData.setDate_time(sdf.format(date));
                        chatData.setTime_in_milli(time);
                        chatData.setIs_On_Server(0);
                        chatData.setIsRead(0);
                        db.insertChatData(chatData);
                        edtMessage.setText("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (db != null) {
                            db.close();
                        }
                    }
               //     startService(new Intent(context, SendChatService.class));
                }

                showMessages();
            }
        });

    }


    private void showMessages() {
        ChatDatabase db = null;
        try {
            db = new ChatDatabase(context);
            db.open();
            chatListData.removeAll(chatListData);
            chatListData.addAll(db.fetchData(AppUtils.getUserIdChat(context), reciever_id, 1, 1));
           // adapterChatDetail.notifyDataSetChanged();

            chatList.setAdapter(adapterChatDetail);

            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setStackFromEnd(true);
            chatList.setLayoutManager(linearLayoutManager);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }


    private void updateMessageList() {
        ChatDatabase db = null;
        try {
            db = new ChatDatabase(context);
            db.open();
            chatListData.removeAll(chatListData);
            chatListData.addAll(db.fetchData(AppUtils.getUserIdChat(context), reciever_id, 1, 1));
            adapterChatDetail.notifyDataSetChanged();
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setStackFromEnd(true);
            chatList.setLayoutManager(linearLayoutManager);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void onItemClickListener(int position, int flag) {

    }

    private void registerChatReceiver(){
        updateChatListReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateMessageList();
              //  Toast.makeText(context, "Chat list updated!!", Toast.LENGTH_SHORT).show();
            }
        };
        IntentFilter filterUpdateChatList = new IntentFilter();
        filterUpdateChatList.addAction("com.logiguyz.updatechatlist");
        registerReceiver(updateChatListReceiver, filterUpdateChatList);
    }

    private void unregisterChatReceiver(){
       unregisterReceiver(updateChatListReceiver);
    }
}
