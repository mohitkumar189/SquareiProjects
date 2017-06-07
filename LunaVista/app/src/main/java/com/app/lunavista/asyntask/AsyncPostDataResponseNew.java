package com.app.lunavista.asyntask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.app.lunavista.interfaces.ListenerPostData;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class AsyncPostDataResponseNew extends AsyncTask<String, Void, String> {
    String url;
    int method;
    Context context;
    ProgressDialog dialog;
    String requestData;
    ListenerPostData listener;

    public AsyncPostDataResponseNew(Context context, int method,
                                    String requestData, String url) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.method = method;
        this.requestData = requestData;
        this.url = url;
        this.listener = (ListenerPostData) context;
        this.execute(new String[]{});
    }


    public AsyncPostDataResponseNew(android.support.v4.app.Fragment _context, int method,
                                    String requestData, String url) {
        // TODO Auto-generated constructor stub
        this.context = _context.getActivity();
        this.method = method;
        this.requestData = requestData;
        this.url = url;
        this.listener = (ListenerPostData) _context;
        this.execute(new String[]{});
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        //AppUtils.onKeyBoardDown(context);

        dialog = ProgressDialog.show(context, "", "Processing...");

        if (requestData == null) {
            requestData = "";

        }
        Log.e("requestData", url + "***" + requestData.toString());
    }

    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub
        String responce = "";
        BufferedReader reader=null;
        try {
            URL url1 = new URL(url);

            // Send POST data request

            URLConnection conn = url1.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(requestData);
            wr.flush();


            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");
            }

            responce = sb.toString();
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
                listener.onPostRequestSucess(method, result);
            } else {
                listener.onPostRequestFailed(method, "Null data from server.");
                Toast.makeText(context,
                        "Problem to connect server please try later.",
                        Toast.LENGTH_LONG).show();
            }
            if (dialog != null)
                dialog.cancel();
        } catch (Exception e) {
            e.printStackTrace();
            listener.onPostRequestFailed(method, "Invalid data: " + e.getMessage());
            if (dialog != null)
                dialog.cancel();
        }
    }
}
