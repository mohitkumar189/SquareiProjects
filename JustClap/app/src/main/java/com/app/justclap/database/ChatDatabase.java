package com.app.justclap.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.app.justclap.constant.ChatProperty;
import com.app.justclap.model.ModelChat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by admin on 18-06-2016.
 */
public class ChatDatabase {

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    private static final String TEXT_TYPE = " TEXT";
    private static final String NUMERIC = " NUMERIC";
    private static final String INTEGER = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String TAG = "ChatDatabase";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ChatProperty.TABLE_NAME + " (" +
                    ChatProperty._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ChatProperty.COL_SENDER_ID + TEXT_TYPE + COMMA_SEP +
                    ChatProperty.COL_RECIEVER_ID + TEXT_TYPE + COMMA_SEP +
                    ChatProperty.COL_MESSAGE + TEXT_TYPE + COMMA_SEP +
                    ChatProperty.COL_DATE_TIME + TEXT_TYPE + COMMA_SEP +
                    ChatProperty.COL_TIME_IN_MILLI + NUMERIC + COMMA_SEP +
                    ChatProperty.COL_ISONSERVER + INTEGER + COMMA_SEP +
                    ChatProperty.COL_ISREAD + INTEGER + COMMA_SEP +
                    "unique (" + ChatProperty.COL_SENDER_ID + "," + ChatProperty.COL_TIME_IN_MILLI + ")" +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ChatProperty.TABLE_NAME;

    public ChatDatabase(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, ChatProperty.DATABASE_NAME, null, 1);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {

            try {
                Log.e("SQL_CREATE_ENTRIES", "****" + SQL_CREATE_ENTRIES);
                db.execSQL(SQL_CREATE_ENTRIES);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

    }

    public ChatDatabase open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    // ---closes the database---
    public void close() {
        DBHelper.close();
    }


    public boolean insertChatData(ModelChat chatData) {
        long count = -1;
        ContentValues cv = new ContentValues();
        cv.put(ChatProperty.COL_SENDER_ID, chatData.getSender_id());
        cv.put(ChatProperty.COL_RECIEVER_ID, chatData.getReciever_id());
        cv.put(ChatProperty.COL_MESSAGE, chatData.getMessage());
        cv.put(ChatProperty.COL_DATE_TIME, chatData.getDate_time());
        cv.put(ChatProperty.COL_TIME_IN_MILLI, chatData.getTime_in_milli());
        cv.put(ChatProperty.COL_ISONSERVER, chatData.is_On_Server());
        cv.put(ChatProperty.COL_ISREAD, chatData.isRead());
        count = db.insert(ChatProperty.TABLE_NAME, null, cv);
        return count > -1;
    }


    public ArrayList<ModelChat> fetchData(String userId, String partnerId, int skipcount, int no_message) {
        Cursor cursor = null;
        ArrayList<ModelChat> chatListData = new ArrayList<>();
        try {

            /*cursor = db.rawQuery("select * from " + ChatProperty.TABLE_NAME + " where sender_id = '"
                    + userId + "'", null);*/
            cursor = db.rawQuery("select * from " + ChatProperty.TABLE_NAME,  null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();


                if (cursor != null && cursor.getCount() > 0) {

                    do {
                        ModelChat modelChat = new ModelChat();

                        modelChat.setSender_id(cursor.getString(cursor.getColumnIndex(ChatProperty.COL_SENDER_ID)));
                        modelChat.setReciever_id(cursor.getString(cursor.getColumnIndex(ChatProperty.COL_RECIEVER_ID)));

                        modelChat.setMessage(cursor.getString(cursor.getColumnIndex(ChatProperty.COL_MESSAGE)));


                        DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);
                        Date date = format.parse(cursor.getString(cursor.getColumnIndex(ChatProperty.COL_DATE_TIME)));

                        String FORMAT = "HH:mm";
                        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT, Locale.ENGLISH);
                        String time = sdf.format(date);
                        modelChat.setDate_time(time);
                        modelChat.setTime_in_milli(cursor.getLong(cursor.getColumnIndex(ChatProperty.COL_TIME_IN_MILLI)));
                        modelChat.setIs_On_Server(cursor.getInt(cursor.getColumnIndex(ChatProperty.COL_ISONSERVER)));
                        modelChat.setIsRead(cursor.getInt(cursor.getColumnIndex(ChatProperty.COL_ISREAD)));
                        chatListData.add(modelChat);


                    } while (cursor.moveToNext());
                }
            }
            //	this.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chatListData;
    }


    public JSONArray getDataToSyncServer(long lastSyncTime) {
        Cursor cursor = null;
        JSONArray chatListData = new JSONArray();
        try {
            /*cursor = db.rawQuery("select * from " + ChatProperty.TABLE_NAME + " where sender_id = '"
                    + sender_id + "'", null);*/
            cursor = db.rawQuery("select * from " + ChatProperty.TABLE_NAME + " where " + ChatProperty.COL_TIME_IN_MILLI + " > '"
                    + lastSyncTime + "'", null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();

                if (cursor != null && cursor.getCount() > 0) {

                    do {
                        DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);
                        Date date = format.parse(cursor.getString(cursor.getColumnIndex(ChatProperty.COL_DATE_TIME)));
                        String FORMAT = "dd-MM-yyyy HH:mm";
                        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT, Locale.ENGLISH);
                        String time = sdf.format(date);

                        JSONObject modelChat = new JSONObject();
                        modelChat.put("sender_id", cursor.getString(cursor.getColumnIndex(ChatProperty.COL_SENDER_ID)));
                        modelChat.put("receiver_id", cursor.getString(cursor.getColumnIndex(ChatProperty.COL_RECIEVER_ID)));
                        modelChat.put("message_text", cursor.getString(cursor.getColumnIndex(ChatProperty.COL_MESSAGE)));
                        modelChat.put("sent_time", time);
                        modelChat.put("time_in_millisecond", cursor.getLong(cursor.getColumnIndex(ChatProperty.COL_TIME_IN_MILLI)));

                        chatListData.put(modelChat);
                       /* {\"sender_id\":\"2\",\"receiver_id\":\"1\",\"message_text\":\"Hi Nitin\",\"sent_time\":\"2016-06-30 22:21:12\"" +
                                ",\"time_in_millisecond\":\"1467325272514\"}*/

                    } while (cursor.moveToNext());
                }
            }
            //	this.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chatListData;
    }


    public void clearChat(String reciever_id) {
        //SQLiteDatabase data = this.getReadableDatabase();
        db.delete(ChatProperty.TABLE_NAME + " where reciever_id = '"
                + reciever_id + "'", null, null);
        //this.close();
    }
}
