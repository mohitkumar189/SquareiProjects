package com.app.justclap.vendor_fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.activities.ActivityChat;
import com.app.justclap.activities.ActivityShowRoute;
import com.app.justclap.activities.ViewDetailAnswers;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.app.justclap.adapters.AdapterNewsLead;
import com.app.justclap.asyntask.AsyncPostDataFragment;
import com.app.justclap.asyntask.AsyncPostDataResponse;
import com.app.justclap.asyntask.AsyncPostDataResponseNoProgress;
import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.app.justclap.models.ModelVendorData;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.utils.GPSTracker;
import com.app.justclap.vendordetail.VendorQuestionDetail;

public class FragmentNewLead extends Fragment implements OnCustomItemClicListener, ListenerPostData {


    RelativeLayout rl_show_filter;
    TextView text_credits, text_submit, text_filter;
    Context context;
    ImageView image_hidefilter;
    Spinner spinner_filter, spinner_sort;
    RecyclerView mRecyclerView;
    ModelVendorData itemList;
    ArrayList<ModelVendorData> arrayList;
    SwipeRefreshLayout mSwipeRefreshLayout;
    AdapterNewsLead adapterNewsLead;
    RelativeLayout rl_main_layout, rl_network;
    ConnectionDetector cd;
    private boolean loading = true;
    String reason = "", maxlistLength = "";
    LinearLayout linear_empty_list;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager layoutManager;
    int skipCount = 0, deletedPosition;
    double mLat, mLong;
    GPSTracker gTraker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewProfile = inflater.inflate(R.layout.fragment_news_lead, container, false);

        context = getActivity();
        cd = new ConnectionDetector(context);
        linear_empty_list = (LinearLayout) viewProfile.findViewById(R.id.linear_empty_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) viewProfile.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        rl_show_filter = (RelativeLayout) viewProfile.findViewById(R.id.rl_show_filter);
        image_hidefilter = (ImageView) viewProfile.findViewById(R.id.image_hidefilter);
        text_credits = (TextView) viewProfile.findViewById(R.id.text_credits);
        text_submit = (TextView) viewProfile.findViewById(R.id.text_submit);
        mRecyclerView = (RecyclerView) viewProfile.findViewById(R.id.recycler_view);
        arrayList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        rl_main_layout = (RelativeLayout) viewProfile.findViewById(R.id.rl_main_layout);
        rl_network = (RelativeLayout) viewProfile.findViewById(R.id.rl_network);
        spinner_filter = (Spinner) viewProfile.findViewById(R.id.spinner_filter);
        spinner_sort = (Spinner) viewProfile.findViewById(R.id.spinner_sort);
        text_filter = (TextView) viewProfile.findViewById(R.id.text_filter);
        gTraker = new GPSTracker(context);

        if (gTraker.canGetLocation()) {

            mLat = gTraker.getLatitude();
            mLong = gTraker.getLongitude();

            Log.e("mLat", "" + mLat);
            Log.e("mLong", "" + mLong);

        } else {
            showSettingsAlert();
            // getTrainingList();
        }

        setListener();
        if (context != null && isAdded()) {
            hitService();
        }
        return viewProfile;
    }

    public void showSettingsAlert() {
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

            // Setting Dialog Title
            alertDialog.setTitle("GPS is settings");

            // Setting Dialog Message
            alertDialog
                    .setMessage("GPS is not enabled. Do you want to go to settings menu?");

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);

                        }
                    });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });

            // Showing Alert Message
            alertDialog.show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void setListener() {

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshItems();

            }
        });
        image_hidefilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rl_show_filter.setVisibility(View.GONE);
                text_filter.setVisibility(View.VISIBLE);

            }
        });
        text_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rl_show_filter.setVisibility(View.VISIBLE);
                text_filter.setVisibility(View.GONE);

            }
        });
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if ((AppUtils.isNetworkAvailable(context))) {

                    if (!maxlistLength.equalsIgnoreCase(arrayList.size() + "")) {
                        if (dy > 0) //check for scroll down
                        {

                            visibleItemCount = layoutManager.getChildCount();
                            totalItemCount = layoutManager.getItemCount();
                            pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                            if (loading) {
                                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                    loading = false;
                                    itemList = new ModelVendorData();
                                    itemList.setRowType(2);
                                    arrayList.add(itemList);
                                    adapterNewsLead.notifyDataSetChanged();

                                    skipCount = skipCount + 10;

                                    try {

                                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                                                2);

                                        nameValuePairs
                                                .add(new BasicNameValuePair(
                                                        "vendorID", AppUtils.getvendorId(context)));
                                        nameValuePairs
                                                .add(new BasicNameValuePair(
                                                        "skipCount", skipCount + ""));

                                        mSwipeRefreshLayout.setRefreshing(true);
                                        new AsyncPostDataResponseNoProgress(FragmentNewLead.this, 3, nameValuePairs,
                                                getString(R.string.url_base_new)
                                                        + getString(R.string.getNewLeads));
                                    } catch (Exception e) {
                                        e.printStackTrace();

                                    }
                                    //Do pagination.. i.e. fetch new data
                                }
                            }
                        }
                    } else {

                        Log.e("maxlength", "*" + arrayList.size());
                    }
                }
            }
        });


    }


    private void hitService() {

        try {
            skipCount = 0;
            // for gcm condition check
            // =================================================

            cd = new ConnectionDetector(context);

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present


                rl_main_layout.setVisibility(View.GONE);
                rl_network.setVisibility(View.VISIBLE);
                //  AppUtils.showCustomAlert(getActivity(), getResources().getString(R.string.message_network_problem));

                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "vendorID", AppUtils.getvendorId(context)));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "skipCount", skipCount + ""));

                mSwipeRefreshLayout.setRefreshing(true);
                new AsyncPostDataResponse(FragmentNewLead.this, 1, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.getNewLeads));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void deleteNewLead() {

        try {

            // for gcm condition check
            // =================================================


            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present

                rl_main_layout.setVisibility(View.GONE);
                rl_network.setVisibility(View.VISIBLE);

                // AppUtils.showCustomAlert(getActivity(), getResources().getString(R.string.message_network_problem));

                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "vendorID", AppUtils.getvendorId(context)));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "userID", arrayList.get(deletedPosition).getCustomerID()));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "serviceID", arrayList.get(deletedPosition).getServiceID()));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "searchId", arrayList.get(deletedPosition).getSearchId()));


                mSwipeRefreshLayout.setRefreshing(true);
                new AsyncPostDataResponseNoProgress(FragmentNewLead.this, 4, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.deleteRequestFromVendor));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void refreshItems() {

        try {
            skipCount = 0;

            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present


                rl_main_layout.setVisibility(View.GONE);
                rl_network.setVisibility(View.VISIBLE);
                //  AppUtils.showCustomAlert(getActivity(), getResources().getString(R.string.message_network_problem));

                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "vendorID", AppUtils.getvendorId(context)));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "skipCount", skipCount + ""));


                new AsyncPostDataResponseNoProgress(FragmentNewLead.this, 2, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.getNewLeads));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("onActivityResult", "onActivityResult" + resultCode);
        if (requestCode == 21 && resultCode == 2) {
            refreshItems();
        }
    }

    private void showConfirmtion() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);

        alertDialog.setTitle("Cancel Request");

        alertDialog.setMessage("Are you sure you want to cancel this request?");

        alertDialog.setPositiveButton("CONFIRM",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        deleteNewLead();

                    }

                });

        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }

    @Override
    public void onItemClickListener(int position, int flag) {


        if (flag == 1) {

            if (arrayList.get(position).getIs_quoted().equalsIgnoreCase("0") && arrayList.get(position).getIs_hire().equalsIgnoreCase("0")) {
                Intent in = new Intent(getActivity(), VendorQuestionDetail.class);
                in.putExtra("service_id", arrayList.get(position).getServiceID());
                in.putExtra("user_id", arrayList.get(position).getCustomerID());
                in.putExtra("category_name", arrayList.get(position).getSeervicename());
                in.putExtra("date", arrayList.get(position).getBookinTime());
                in.putExtra("vendor_name", arrayList.get(position).getVendorname());
                in.putExtra("serviceCharge", arrayList.get(position).getServiceCharge());
                in.putExtra("searchId", arrayList.get(position).getSearchId());
                in.putExtra("profile_image", arrayList.get(position).getProfileImage());
                in.putExtra("CustomerResponse", arrayList.get(position).getCustomerResponse());
                startActivityForResult(in, 21);
            }


        } else if (flag == 2) {
            deletedPosition = position;

            showConfirmtion();
        } else if (flag == 3) {
            if (arrayList.get(position).getIs_quoted().equalsIgnoreCase("1")) {
                Intent in = new Intent(context, ActivityChat.class);
                in.putExtra("reciever_id", arrayList.get(position).getCustomerID());
                in.putExtra("name", arrayList.get(position).getVendorname());
                in.putExtra("image", context.getResources().getString(R.string.img_url) + arrayList.get(position).getProfileImage());
                in.putExtra("searchID", arrayList.get(position).getSearchId());
                startActivity(in);
            } else {
                Intent in = new Intent(getActivity(), VendorQuestionDetail.class);
                in.putExtra("service_id", arrayList.get(position).getServiceID());
                in.putExtra("user_id", arrayList.get(position).getCustomerID());
                in.putExtra("category_name", arrayList.get(position).getSeervicename());
                in.putExtra("date", arrayList.get(position).getBookinTime());
                in.putExtra("vendor_name", arrayList.get(position).getVendorname());
                in.putExtra("serviceCharge", arrayList.get(position).getServiceCharge());
                in.putExtra("searchId", arrayList.get(position).getSearchId());
                in.putExtra("profile_image", arrayList.get(position).getProfileImage());
                in.putExtra("CustomerResponse", arrayList.get(position).getCustomerResponse());
                startActivityForResult(in, 21);

            }

        } else if (flag == 4) {
            if (arrayList.get(position).getIs_quoted().equalsIgnoreCase("1")) {

                String number = arrayList.get(position).getMobile();

                String uri = "tel:" + number.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            } else {
                Intent in = new Intent(getActivity(), VendorQuestionDetail.class);
                in.putExtra("service_id", arrayList.get(position).getServiceID());
                in.putExtra("user_id", arrayList.get(position).getCustomerID());
                in.putExtra("category_name", arrayList.get(position).getSeervicename());
                in.putExtra("date", arrayList.get(position).getBookinTime());
                in.putExtra("vendor_name", arrayList.get(position).getVendorname());
                in.putExtra("serviceCharge", arrayList.get(position).getServiceCharge());
                in.putExtra("searchId", arrayList.get(position).getSearchId());
                in.putExtra("profile_image", arrayList.get(position).getProfileImage());
                in.putExtra("CustomerResponse", arrayList.get(position).getCustomerResponse());
                startActivityForResult(in, 21);

            }
        } else if (flag == 5) {
            if (arrayList.get(position).getIs_quoted().equalsIgnoreCase("1")) {
                Intent in = new Intent(getActivity(), ViewDetailAnswers.class);
                in.putExtra("service_id", arrayList.get(position).getServiceID());
                in.putExtra("service_name", arrayList.get(position).getSeervicename());
                in.putExtra("user_id", arrayList.get(position).getCustomerID());
                in.putExtra("searchId", arrayList.get(position).getSearchId());
                in.putExtra("CustomerResponse", arrayList.get(position).getCustomerResponse());
                startActivity(in);
            } else {
                Intent in = new Intent(getActivity(), VendorQuestionDetail.class);
                in.putExtra("service_id", arrayList.get(position).getServiceID());
                in.putExtra("user_id", arrayList.get(position).getCustomerID());
                in.putExtra("category_name", arrayList.get(position).getSeervicename());
                in.putExtra("date", arrayList.get(position).getBookinTime());
                in.putExtra("vendor_name", arrayList.get(position).getVendorname());
                in.putExtra("serviceCharge", arrayList.get(position).getServiceCharge());
                in.putExtra("searchId", arrayList.get(position).getSearchId());
                in.putExtra("profile_image", arrayList.get(position).getProfileImage());
                in.putExtra("CustomerResponse", arrayList.get(position).getCustomerResponse());
                startActivityForResult(in, 21);

            }
        } else if (flag == 11) {

            Intent in = new Intent(context, ActivityShowRoute.class);
            in.putExtra("srcLat", arrayList.get(position).getSourceLatitude());
            in.putExtra("srcLng", arrayList.get(position).getSourceLongitude());
            in.putExtra("venLat", arrayList.get(position).getVendorLatitude());
            in.putExtra("venLng", arrayList.get(position).getVendorLongitude());
            startActivity(in);


        }
    }


    private void showAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Please contact to admin for activation ?");

        // set dialog message
        alertDialogBuilder

                .setCancelable(false)
                .setPositiveButton("Call",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                //   AppUtils.setUserId(context,false);

                                dialog.cancel();

                                String uri = "tel:" + "9810960920";
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse(uri));
                                startActivity(intent);

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    @Override
    public void onPostRequestSucess(int position, String response) {


        try {

            if (position == 1) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                arrayList = new ArrayList<>();
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONArray data = commandResult.getJSONArray("data");
                    maxlistLength = commandResult.getString("total");
                    arrayList.removeAll(arrayList);
                    for (int i = 0; i < data.length(); i++) {


                        JSONObject ob = data.getJSONObject(i);

                        itemList = new ModelVendorData();
                        itemList.setSeervicename(ob.getString("serviceName"));
                        itemList.setBookinTime(ob.getString("date"));
                        itemList.setAddress(ob.getString("address"));
                        itemList.setVendorname(ob.getString("name"));
                        itemList.setResponseTime(ob.getString("responseTime"));
                        itemList.setResponseCount(ob.getString("noOf") + " of " + ob.getString("total"));
                        itemList.setRowType(1);
                        itemList.setServiceID(ob.getString("serviceID"));
                        itemList.setSearchId(ob.getString("searchID"));
                        itemList.setRequestId(ob.getString("requestID"));
                        itemList.setServiceCharge(ob.getString("serviceCharge"));
                        itemList.setEmail(ob.getString("email"));
                        itemList.setCustomerID(ob.getString("customerID"));
                        itemList.setCustomerResponse(ob.getString("CustomerResponse"));
                        itemList.setLatitude(ob.getString("latitude"));

                        itemList.setVendorLatitude(ob.getString("latitude"));
                        itemList.setVendorLongitude(ob.getString("longitude"));
                        itemList.setVendorDistance(ob.getString("vendorDistance"));
                        itemList.setSourceLatitude(ob.getString("vendorLatitude"));
                        itemList.setSourceLongitude(ob.getString("vendorLongitude"));

                        itemList.setLongitude(ob.getString("longitude"));
                        itemList.setMobile(ob.getString("mobile"));
                        itemList.setStatus(ob.getString("status"));
                        itemList.setProfileImage(ob.getString("profileImage"));
                        itemList.setIs_hire(ob.getString("is_hire"));
                        itemList.setIs_quoted(ob.getString("is_quoted"));
                        itemList.setServiceType(ob.getString("serviceType"));
                        arrayList.add(itemList);

                    }

                    adapterNewsLead = new AdapterNewsLead(getActivity(), this, arrayList);
                    mRecyclerView.setAdapter(adapterNewsLead);
                    mSwipeRefreshLayout.setRefreshing(false);
                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);

                    if (arrayList.size() > 0) {
                        linear_empty_list.setVisibility(View.GONE);
                    } else {
                        linear_empty_list.setVisibility(View.VISIBLE);
                    }

                } else {
                    arrayList.removeAll(arrayList);
                    //adapterNewsLead.notifyDataSetChanged();
                    //   AppUtils.showCustomAlert(getActivity(), commandResult.getString("message"));
                    mSwipeRefreshLayout.setRefreshing(false);
                    linear_empty_list.setVisibility(View.VISIBLE);
                }

            } else if (position == 2) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                arrayList = new ArrayList<>();
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    maxlistLength = commandResult.getString("total");
                    JSONArray data = commandResult.getJSONArray("data");
                    //  maxlistLength = main.getString("total");
                    arrayList.removeAll(arrayList);
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject ob = data.getJSONObject(i);

                        itemList = new ModelVendorData();
                        itemList.setSeervicename(ob.getString("serviceName"));
                        itemList.setBookinTime(ob.getString("date"));
                        itemList.setAddress(ob.getString("address"));
                        itemList.setCustomerResponse(ob.getString("CustomerResponse"));
                        itemList.setVendorname(ob.getString("name"));
                        itemList.setResponseTime(ob.getString("responseTime"));
                        itemList.setResponseCount(ob.getString("noOf") + " of " + ob.getString("total"));
                        itemList.setRowType(1);
                        itemList.setServiceID(ob.getString("serviceID"));
                        itemList.setEmail(ob.getString("email"));
                        itemList.setProfileImage(ob.getString("profileImage"));
                        itemList.setSearchId(ob.getString("searchID"));
                        itemList.setVendorLatitude(ob.getString("latitude"));
                        itemList.setVendorLongitude(ob.getString("longitude"));
                        itemList.setVendorDistance(ob.getString("vendorDistance"));
                        itemList.setSourceLatitude(ob.getString("vendorLatitude"));
                        itemList.setSourceLongitude(ob.getString("vendorLongitude"));


                        itemList.setRequestId(ob.getString("requestID"));
                        itemList.setServiceCharge(ob.getString("serviceCharge"));
                        itemList.setCustomerID(ob.getString("customerID"));
                        itemList.setLatitude(ob.getString("latitude"));
                        itemList.setLongitude(ob.getString("longitude"));
                        itemList.setMobile(ob.getString("mobile"));
                        itemList.setStatus(ob.getString("status"));
                        itemList.setIs_hire(ob.getString("is_hire"));
                        itemList.setIs_quoted(ob.getString("is_quoted"));
                        itemList.setServiceType(ob.getString("serviceType"));
                        arrayList.add(itemList);

                    }

                    adapterNewsLead = new AdapterNewsLead(getActivity(), this, arrayList);
                    mRecyclerView.setAdapter(adapterNewsLead);
                    mSwipeRefreshLayout.setRefreshing(false);

                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);
                    if (arrayList.size() > 0) {
                        linear_empty_list.setVisibility(View.GONE);
                    } else {
                        linear_empty_list.setVisibility(View.VISIBLE);
                    }
                } else {
                    arrayList.removeAll(arrayList);
                    adapterNewsLead.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                    linear_empty_list.setVisibility(View.VISIBLE);
                }

            } else if (position == 3) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    maxlistLength = commandResult.getString("total");
                    JSONArray data = commandResult.getJSONArray("data");
                    //  maxlistLength = main.getString("total");
                    arrayList.remove(arrayList.size() - 1);
                    for (int i = 0; i < data.length(); i++) {


                        JSONObject ob = data.getJSONObject(i);

                        itemList = new ModelVendorData();
                        itemList.setSeervicename(ob.getString("serviceName"));
                        itemList.setBookinTime(ob.getString("date"));
                        itemList.setAddress(ob.getString("address"));
                        itemList.setVendorname(ob.getString("name"));
                        itemList.setResponseTime(ob.getString("responseTime"));
                        itemList.setResponseCount(ob.getString("noOf") + " of " + ob.getString("total"));
                        itemList.setRowType(1);
                        itemList.setServiceID(ob.getString("serviceID"));
                        itemList.setCustomerResponse(ob.getString("CustomerResponse"));
                        itemList.setEmail(ob.getString("email"));
                        itemList.setCustomerID(ob.getString("customerID"));
                        itemList.setSearchId(ob.getString("searchID"));
                        itemList.setRequestId(ob.getString("requestID"));
                        itemList.setServiceCharge(ob.getString("serviceCharge"));
                        itemList.setVendorLatitude(ob.getString("latitude"));
                        itemList.setVendorLongitude(ob.getString("longitude"));
                        itemList.setVendorDistance(ob.getString("vendorDistance"));
                        itemList.setSourceLatitude(ob.getString("vendorLatitude"));
                        itemList.setSourceLongitude(ob.getString("vendorLongitude"));

                        itemList.setLatitude(ob.getString("latitude"));
                        itemList.setProfileImage(ob.getString("profileImage"));
                        itemList.setLongitude(ob.getString("longitude"));
                        itemList.setMobile(ob.getString("mobile"));
                        itemList.setStatus(ob.getString("status"));
                        itemList.setIs_hire(ob.getString("is_hire"));
                        itemList.setIs_quoted(ob.getString("is_quoted"));
                        itemList.setServiceType(ob.getString("serviceType"));
                        arrayList.add(itemList);

                    }

                    if (data.length() == 0) {
                        skipCount = skipCount - 10;
                    }
                    loading = true;

                    if (arrayList.size() > 0) {
                        linear_empty_list.setVisibility(View.GONE);
                    } else {
                        linear_empty_list.setVisibility(View.VISIBLE);
                    }
                    adapterNewsLead.notifyDataSetChanged();
                } else {
                    arrayList.removeAll(arrayList);
                    adapterNewsLead.notifyDataSetChanged();

                    loading = true;
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            } else if (position == 4) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    AppUtils.showCustomAlert(getActivity(), commandResult.getString("message"));
                    arrayList.remove(deletedPosition);
                    adapterNewsLead.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    AppUtils.showCustomAlert(getActivity(), commandResult.getString("message"));
                }

            }
        } catch (JSONException e) {
            mSwipeRefreshLayout.setRefreshing(false);
            if (position == 3) {
                loading = true;
                skipCount = skipCount - 10;
            }
            e.printStackTrace();
        }

    }

    @Override
    public void onPostRequestFailed(int position, String response) {
        if (context != null && isAdded()) {
            AppUtils.showCustomAlert(getActivity(), getResources().getString(R.string.problem_server));
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
