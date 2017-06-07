package com.app.justclap.asyntask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.app.justclap.interfaces.ListenerPostData;


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
            // requestData = new ArrayList<NameValuePair>(2);

        }
        Log.e("requestData", url + "***" + requestData.toString());
    }

    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub
        String data = "";
        HttpURLConnection urlConnection = null;
        InputStream iStream = null;

        try {
            URL url1 = new URL(url);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url1.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (ClientProtocolException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }

        return data;
    }

    @Override
    protected void onPostExecute(String result) {

        super.onPostExecute(result);
        try {
            Log.e("Response", "***" + result);
            if (result != null) {
                JSONObject jObject = new JSONObject(result);
                if (listener != null)
                    listener.onPostRequestSucess(method, result);
            } else {
                if (listener != null)
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
