package com.app.justclap.activities;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.app.justclap.R;
import com.app.justclap.utils.AppUtils;

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
import java.util.ArrayList;
import java.util.List;


/**
 * Created by admin on 05-08-2016.
 */
public class SubmitContactService extends IntentService {

    Context context;
    JSONArray contactsArray;

    public SubmitContactService() {
        super("");

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        context = this;

        getContactList();
    }

    private void getContactList() {

        contactsArray = new JSONArray();
        JSONObject numbers;

        try {
            StringBuffer sb = new StringBuffer();
            //  sb.append("......Contact Details.....");
            ContentResolver cr = getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            String phone = null;

            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

                        //     sb.append("\n Contact Name:" + name);
                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            sb.append("\n Phone number:" + phone);
                            Log.e("phone", "*" + phone);
                            Log.e("name : " + name, ", ID : " + id);

                            numbers = new JSONObject();
                            numbers.put("phone", phone);
                            numbers.put("name", name);
                            numbers.put("id", id);
                            contactsArray.put(numbers);

                        }
                        pCur.close();
                    }
                }

                sendContacts(context);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void sendContacts(Context context) {


        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                2);

        nameValuePairs
                .add(new BasicNameValuePair(
                        "contacts", contactsArray.toString()));

        nameValuePairs
                .add(new BasicNameValuePair(
                        "deviceType", "1"));

        nameValuePairs
                .add(new BasicNameValuePair(
                        "deviceToken", AppUtils.getGcmRegistrationKey(context)));

        new AsyncPostDataResponseNew(context, 1, nameValuePairs,
                getString(R.string.url_base_new)
                        + getString(R.string.getUserContacts));

    }


    public class AsyncPostDataResponseNew extends AsyncTask<String, Void, String> {
        String url;
        int method;
        Context context;
        List<NameValuePair> requestData;

        public AsyncPostDataResponseNew(Context context, int method,
                                        List<NameValuePair> requestData, String url) {
            // TODO Auto-generated constructor stub
            this.context = context;
            this.method = method;
            this.requestData = requestData;
            this.url = url;
            this.execute(new String[]{});
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //AppUtils.onKeyBoardDown(context);

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

            Log.e("Response", "***" + result);
            if (result != null) {
                try {
                    if (method == 1) {
                        JSONObject jObject = new JSONObject(result);
                        JSONObject commandResult = jObject
                                .getJSONObject("commandResult");
                        if (commandResult.getString("success").equalsIgnoreCase("1")) {

                            SharedPreferences pref = getSharedPreferences("contact", MODE_PRIVATE);
                            SharedPreferences.Editor ed;
                            ed = pref.edit();
                            ed.putBoolean("Deactivate1", true);
                            ed.commit();
                        }

                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

        }

    }
}

