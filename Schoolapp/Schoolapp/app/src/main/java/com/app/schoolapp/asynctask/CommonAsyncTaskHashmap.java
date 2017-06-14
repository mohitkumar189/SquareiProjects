package com.app.schoolapp.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.schoolapp.R;
import com.app.schoolapp.interfaces.ApiResponse;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Hemanta on 11/7/2016.
 */

public class CommonAsyncTaskHashmap {

    private ProgressDialog pd;
    private RequestQueue queue;
    private Context context;
    private ApiResponse listener;
    int method;

    public CommonAsyncTaskHashmap(int method, Context context, ApiResponse response) {
        queue = Volley.newRequestQueue(context);
        this.context = context;
        listener = response;
        this.method = method;
        pd = new ProgressDialog(context);
        pd.setMessage("Processing ... ");
        pd.setCancelable(false);
    }

    public void getquery(String url) {
        // String url = context.getResources().getString(R.string.base_url) + addurl;
        Log.e("request", ": " + url);
        pd.show();
        final StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response.toString());
                        if (pd != null)
                            pd.cancel();
                        try {
                            if (response != null) {
                                JSONObject jo = new JSONObject(response);
                                if (listener != null)
                                    listener.onPostSuccess(method, jo);
                            } else {
                                if (listener != null)
                                    // listener.onPostRequestFailed(method, "Null data from server.");
                                    Toast.makeText(context,
                                            context.getResources().getString(R.string.problem_server),
                                            Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                if (pd != null)
                    pd.cancel();
                try {
                    if (listener != null) {
                        listener.onPostFail(method, error.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

// Adding request to request queue
        RetryPolicy retryPolicy = new DefaultRetryPolicy(
                600000,
                4,
                1000
        );
        jsonObjReq.setRetryPolicy(retryPolicy);

        queue.add(jsonObjReq);

    }

    public void getqueryJson(String url, final HashMap<String, String> jsonBody) {
        // String url = context.getResources().getString(R.string.base_url) + addurl;
        Log.e("request", ": " + url + jsonBody);
        pd.show();
        final String requestBody = jsonBody.toString();
        final StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response.toString());
                        if (pd != null)
                            pd.cancel();
                        try {
                            if (response != null) {
                                JSONObject jo = new JSONObject(response);
                                if (listener != null)
                                    listener.onPostSuccess(method, jo);
                            } else {
                                if (listener != null)
                                    // listener.onPostRequestFailed(method, "Null data from server.");
                                    Toast.makeText(context,
                                            context.getResources().getString(R.string.problem_server),
                                            Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("request", ": " + error.toString());
                // hide the progress dialog
                if (pd != null)
                    pd.cancel();
                try {
                    if (listener != null) {

                        String messageBody = null; // message received in the error
                        //    int responseCode = error.networkResponse.statusCode; // to get response code

                        if (error.networkResponse.data != null) {
                            try {
                                messageBody = new String(error.networkResponse.data, "UTF-8");
                                VolleyLog.d(TAG, "Error: " + messageBody);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            listener.onPostFail(method, messageBody);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params = jsonBody;
                return params;
            }
        };

// Adding request to request queue
        queue.add(jsonObjReq);

    }

    public void getqueryJson(String url, int requestMethod, final HashMap<String, String> jsonBody) {
        // String url = context.getResources().getString(R.string.base_url) + addurl;
        Log.e("request", ": " + url + jsonBody);
        pd.show();
        final String requestBody = jsonBody.toString();
        final StringRequest jsonObjReq = new StringRequest(Request.Method.PUT,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response.toString());
                        if (pd != null)
                            pd.cancel();
                        try {
                            if (response != null) {
                                JSONObject jo = new JSONObject(response);
                                if (listener != null)
                                    listener.onPostSuccess(method, jo);
                            } else {
                                if (listener != null)
                                    // listener.onPostRequestFailed(method, "Null data from server.");
                                    Toast.makeText(context,context.getResources().getString(R.string.problem_server),Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("request", ": " + error.toString());
                // hide the progress dialog
                if (pd != null)
                    pd.cancel();
                try {
                    if (listener != null) {

                        String messageBody = null; // message received in the error
                        //    int responseCode = error.networkResponse.statusCode; // to get response code
                        if (error.networkResponse.data != null) {
                            try {
                                messageBody = new String(error.networkResponse.data, "UTF-8");
                                VolleyLog.d(TAG, "Error: " + messageBody);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            listener.onPostFail(method, messageBody);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params = jsonBody;
                return params;
            }
        };
// Adding request to request queue
        RetryPolicy retryPolicy = new DefaultRetryPolicy(
                600000,
                4,
                1000
        );
        queue.add(jsonObjReq);
    }

    public void getqueryJsonNoProgress(String url, final HashMap<String, String> jsonBody) {
        // String url = context.getResources().getString(R.string.base_url) + addurl;
        Log.e("request", ": " + url + jsonBody);
        final String requestBody = jsonBody.toString();
        final StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response.toString());

                        try {
                            if (response != null) {
                                JSONObject jo = new JSONObject(response);
                                if (listener != null)
                                    listener.onPostSuccess(method, jo);
                            } else {
                                if (listener != null)
                                    // listener.onPostRequestFailed(method, "Null data from server.");
                                    Toast.makeText(context,
                                            context.getResources().getString(R.string.problem_server),
                                            Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog

                try {
                    if (listener != null) {
                        listener.onPostFail(method, error.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params = jsonBody;
                return params;
            }
        };
// Adding request to request queue
        queue.add(jsonObjReq);

    }

    public void getqueryNoProgress(String url) {
        // String url = context.getResources().getString(R.string.base_url) + addurl;
        Log.e("request", ": " + url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", response.toString());
                        try {
                            if (response != null) {

                                if (listener != null)
                                    listener.onPostSuccess(method, response);
                            } else {
                                if (listener != null)
                                    // listener.onPostRequestFailed(method, "Null data from server.");
                                    Toast.makeText(context,
                                            context.getResources().getString(R.string.problem_server),
                                            Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                try {
                    if (listener != null) {
                        listener.onPostFail(method, error.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

// Adding request to request queue
        queue.add(jsonObjReq);
    }
}
