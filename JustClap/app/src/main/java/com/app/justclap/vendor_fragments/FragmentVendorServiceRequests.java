package com.app.justclap.vendor_fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.justclap.R;
import com.app.justclap.adapters.AdapterFragmentVendorResponses;
import com.app.justclap.adapters.AdapterVendorServicerequest;
import com.app.justclap.asyntask.AsyncPostDataFragment;
import com.app.justclap.asyntask.AsyncPostDataFragmentNoProgress;
import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.vendordetail.Vendor_SearchList;
import com.app.justclap.vendordetail.ViewBidirectionalQuesAnswers;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentVendorServiceRequests extends Fragment implements OnCustomItemClicListener, ListenerPostData {

    RecyclerView list_request;
    AdapterVendorServicerequest adapterResponses;
    ModelService serviceDetail;
    ArrayList<ModelService> service_list;
    Context context;
    ConnectionDetector cd;
    LinearLayout linear_empty_list;
    RelativeLayout rl_main_layout, rl_network;
    int deleteposition, menu_position;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String reason = "", maxlistLength = "";
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager layoutManager;
    int skipCount = 0;
    private boolean loading = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        View view_service = inflater.inflate(R.layout.fragment_vendor_request, container, false);
        context = getActivity();
        service_list = new ArrayList<>();
        rl_main_layout = (RelativeLayout) view_service.findViewById(R.id.rl_main_layout);
        rl_network = (RelativeLayout) view_service.findViewById(R.id.rl_network);
        linear_empty_list = (LinearLayout) view_service.findViewById(R.id.linear_empty_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view_service.findViewById(R.id.swipeRefreshLayout1);
        list_request = (RecyclerView) view_service.findViewById(R.id.list_request);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list_request.setLayoutManager(layoutManager);

        getServicelist();

        setListener();

        return view_service;
    }


    private void setListener() {

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getServicelistrefresh();

            }
        });

        list_request.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if ((AppUtils.isNetworkAvailable(context))) {

                    if (!maxlistLength.equalsIgnoreCase(service_list.size() + "")) {
                        if (dy > 0) //check for scroll down
                        {

                            visibleItemCount = layoutManager.getChildCount();
                            totalItemCount = layoutManager.getItemCount();
                            pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                            if (loading) {
                                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                    loading = false;
                                    serviceDetail = new ModelService();
                                    serviceDetail.setRowType(2);
                                    service_list.add(serviceDetail);
                                    adapterResponses.notifyDataSetChanged();

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


                                        new AsyncPostDataFragmentNoProgress(getActivity(), FragmentVendorServiceRequests.this, 4, nameValuePairs,
                                                getString(R.string.url_base_new)
                                                        + getString(R.string.getVendorQuoteListBiDirectional));


                                    } catch (Exception e) {
                                        e.printStackTrace();

                                    }
                                    //Do pagination.. i.e. fetch new data
                                }
                            }
                        }
                    } else {

                        Log.e("maxlength", "*" + service_list.size());
                    }
                }
            }
        });

    }

    private void getServicelist() {

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

                //   AppUtils.showCustomAlert(getActivity(), "Internet Connection Error, Please connect to working Internet connection");
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

                new AsyncPostDataFragment(getActivity(), (ListenerPostData) this, 1, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.getVendorQuoteListBiDirectional));

            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void getServicelistrefresh() {

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

                //   AppUtils.showCustomAlert(getActivity(), "Internet Connection Error, Please connect to working Internet connection");
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

                new AsyncPostDataFragmentNoProgress(getActivity(), (ListenerPostData) this, 2, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.getVendorQuoteListBiDirectional));

            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void deleteService() {

        try {

            // for gcm condition check
            // =================================================

            cd = new ConnectionDetector(context);

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
                                "serviceID", service_list.get(deleteposition).getServiceID()));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "reason", reason));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "searchId", service_list.get(deleteposition).getSearchId()));


                new AsyncPostDataFragment(getActivity(), (ListenerPostData) this, 3, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.cancelBiDirectionalOfVendor));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 6) {
            menu_position = position;
            deleteposition = position;
            showConfirmtion();

        } else if (flag == 7) {
            menu_position = position;
            Intent in = new Intent(getActivity(), ViewBidirectionalQuesAnswers.class);
            in.putExtra("service_id", service_list.get(menu_position).getServiceID());
            in.putExtra("service_name", service_list.get(menu_position).getServiceName());
            in.putExtra("searchId", service_list.get(menu_position).getSearchId());
            startActivity(in);

        } else if (flag == 1) {
            menu_position = position;
            Intent in = new Intent(getActivity(), Vendor_SearchList.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            in.putExtra("service_id", service_list.get(menu_position).getServiceID());
            in.putExtra("service_name", service_list.get(menu_position).getServiceName());
            in.putExtra("vendor_naukri", "1");
            in.putExtra("searchId", service_list.get(menu_position).getSearchId());

            startActivity(in);
        }
    }

    private void showConfirmtion() {

        final CharSequence[] items = {"Due to my personal reason", "I don't like responses", "Submit by mistake"
        };
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);

        alertDialog.setTitle("Cancel Request");

        // alertDialog.setMessage("Are you sure you want to cancel this request?");

        alertDialog.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Due to my personal reason")) {

                    reason = "0";

                } else if (items[item].equals("I don't like responses")) {

                    reason = "1";

                } else if (items[item].equals("Submit by mistake")) {

                    reason = "2";
                }


            }
        });


        alertDialog.setPositiveButton("CONFIRM",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (reason.equalsIgnoreCase("")) {
                            AppUtils.showCustomAlert(getActivity(), "Please select reason for cancel request");
                        } else {
                            deleteService();
                            dialog.cancel();
                        }

                        Log.e("clickedPosiion", which + "");

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

    private void showMenu() {
        final CharSequence[] items = {"View Details", "Cancel Request"};

        AlertDialog.Builder builder = new AlertDialog.Builder(
                context);
        builder.setTitle("  JustClap");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("View Details")) {

                    Intent in = new Intent(getActivity(), ViewBidirectionalQuesAnswers.class);
                    in.putExtra("service_id", service_list.get(menu_position).getServiceID());
                    in.putExtra("service_name", service_list.get(menu_position).getServiceName());
                    in.putExtra("searchId", service_list.get(menu_position).getSearchId());
                    startActivity(in);
                    dialog.dismiss();

                } else if (items[item].equals("Cancel Request")) {

                    showConfirmtion();
                    dialog.dismiss();

                }/* else if (items[item].equals("View Status")) {

                    Intent in = new Intent(getActivity(), Vendor_SearchList.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("service_id", service_list.get(menu_position).getServiceID());
                    in.putExtra("service_name", service_list.get(menu_position).getServiceName());
                    in.putExtra("vendor_naukri", "1");
                    in.putExtra("searchId", service_list.get(menu_position).getSearchId());
                    startActivity(in);
                    dialog.dismiss();

                }*/
            }
        });
        builder.show();
    }


    @Override
    public void onPostRequestSucess(int position, String response) {


        try {

            if (position == 1) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    //  JSONObject data = commandResult.getJSONObject("data");

                    JSONArray services = commandResult.getJSONArray("data");
                    maxlistLength = commandResult.getString("total");
                    service_list.removeAll(service_list);
                    for (int i = 0; i < services.length(); i++) {

                        JSONObject jo = services.getJSONObject(i);

                        serviceDetail = new ModelService();
                        serviceDetail.setServiceName(jo.getString("categoryName"));
                        if (jo.has("NoOfUsers")) {
                            serviceDetail.setServicesCount(jo.getString("NoOfUsers"));
                        } else {
                            serviceDetail.setServicesCount("");
                        }
                        serviceDetail.setRowType(1);
                        serviceDetail.setSearchId(jo.getString("searchId"));
                        serviceDetail.setServiceID(jo.getString("categoryId"));
                        serviceDetail.setServiceImage(getResources().getString(R.string.img_url) + jo.getString("categoryImage"));
                        serviceDetail.setRequestId(jo.getString("requestID"));
                        serviceDetail.setSearchId(jo.getString("searchId"));
                        serviceDetail.setCreatedDate(jo.getString("createdDate"));
                        serviceDetail.setNewVendorCount(jo.getString("newUserCount"));
                        if (jo.has("user")) {
                            serviceDetail.setVendorArray(jo.getJSONArray("user").toString());
                        } else {
                            serviceDetail.setVendorArray("");
                        }

                        service_list.add(serviceDetail);
                    }

                    adapterResponses = new AdapterVendorServicerequest(getActivity(), this, service_list);
                    list_request.setAdapter(adapterResponses);
                    //   mSwipeRefreshLayout.setRefreshing(false);

                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);
                    if (service_list.size() > 0) {
                        linear_empty_list.setVisibility(View.GONE);
                    } else {
                        linear_empty_list.setVisibility(View.VISIBLE);
                    }

                } else {
                    linear_empty_list.setVisibility(View.VISIBLE);
                    //  AppUtils.showCustomAlert(getActivity(), commandResult.getString("message"));

                }
            } else if (position == 2) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    maxlistLength = commandResult.getString("total");
                    JSONArray services = commandResult.getJSONArray("data");
                    service_list.removeAll(service_list);
                    for (int i = 0; i < services.length(); i++) {

                        JSONObject jo = services.getJSONObject(i);

                        serviceDetail = new ModelService();
                        serviceDetail.setServiceName(jo.getString("categoryName"));
                        if (jo.has("NoOfUsers")) {
                            serviceDetail.setServicesCount(jo.getString("NoOfUsers"));
                        } else {
                            serviceDetail.setServicesCount("");
                        }
                        serviceDetail.setRowType(1);
                        serviceDetail.setSearchId(jo.getString("searchId"));
                        serviceDetail.setServiceID(jo.getString("categoryId"));
                        serviceDetail.setServiceImage(getResources().getString(R.string.img_url) + jo.getString("categoryImage"));
                        serviceDetail.setRequestId(jo.getString("requestID"));
                        serviceDetail.setCreatedDate(jo.getString("createdDate"));
                        serviceDetail.setSearchId(jo.getString("searchId"));
                        serviceDetail.setNewVendorCount(jo.getString("newUserCount"));
                        if (jo.has("user")) {
                            serviceDetail.setVendorArray(jo.getJSONArray("user").toString());
                        } else {
                            serviceDetail.setVendorArray("");
                        }

                        service_list.add(serviceDetail);
                    }

                    adapterResponses = new AdapterVendorServicerequest(getActivity(), this, service_list);
                    list_request.setAdapter(adapterResponses);
                    mSwipeRefreshLayout.setRefreshing(false);
                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);
                    if (service_list.size() > 0) {
                        linear_empty_list.setVisibility(View.GONE);
                    } else {
                        linear_empty_list.setVisibility(View.VISIBLE);
                    }


                } else {
                    linear_empty_list.setVisibility(View.VISIBLE);
                    //  AppUtils.showCustomAlert(getActivity(), commandResult.getString("message"));
                    mSwipeRefreshLayout.setRefreshing(false);
                }


            } else if (position == 4) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    maxlistLength = commandResult.getString("total");
                    JSONArray services = commandResult.getJSONArray("data");
                    service_list.remove(service_list.size() - 1);
                    for (int i = 0; i < services.length(); i++) {

                        JSONObject jo = services.getJSONObject(i);

                        serviceDetail = new ModelService();
                        serviceDetail.setServiceName(jo.getString("categoryName"));
                        if (jo.has("NoOfUsers")) {
                            serviceDetail.setServicesCount(jo.getString("NoOfUsers"));
                        } else {
                            serviceDetail.setServicesCount("");
                        }
                        serviceDetail.setRowType(1);
                        serviceDetail.setSearchId(jo.getString("searchId"));
                        serviceDetail.setServiceID(jo.getString("categoryId"));
                        serviceDetail.setServiceImage(getResources().getString(R.string.img_url) + jo.getString("categoryImage"));
                        serviceDetail.setRequestId(jo.getString("requestID"));
                        serviceDetail.setSearchId(jo.getString("searchId"));
                        serviceDetail.setCreatedDate(jo.getString("createdDate"));
                        serviceDetail.setNewVendorCount(jo.getString("newUserCount"));
                        if (jo.has("user")) {
                            serviceDetail.setVendorArray(jo.getJSONArray("user").toString());
                        } else {
                            serviceDetail.setVendorArray("");
                        }

                        service_list.add(serviceDetail);
                    }

                    adapterResponses.notifyDataSetChanged();
                    loading = true;

                    if (services.length() == 0) {
                        skipCount = skipCount - 10;
                        //  return;
                    }

                } else {
                    adapterResponses.notifyDataSetChanged();
                    loading = true;
                    skipCount = skipCount - 10;

                    linear_empty_list.setVisibility(View.VISIBLE);
                    //  AppUtils.showCustomAlert(getActivity(), commandResult.getString("message"));
                    mSwipeRefreshLayout.setRefreshing(false);
                }


            } else if (position == 3) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    //   JSONObject data = commandResult.getJSONObject("data");
                    AppUtils.showCustomAlert(getActivity(), commandResult.getString("message"));

                    service_list.remove(deleteposition);
                    maxlistLength = Integer.parseInt(maxlistLength.trim()) - 1 + "";
                    adapterResponses.notifyDataSetChanged();
                    list_request.invalidate();
                    //  mSwipeRefreshLayout.setRefreshing(true);


                } else {
                    AppUtils.showCustomAlert(getActivity(), commandResult.getString("message"));

                }


            }
        } catch (JSONException e) {
            if (position == 4) {
                loading = true;
                skipCount = skipCount - 10;

            }
            e.printStackTrace();
        }
    }

    @Override
    public void onPostRequestFailed(int position, String response) {

        AppUtils.showCustomAlert(getActivity(), getResources().getString(R.string.problem_server));
        mSwipeRefreshLayout.setRefreshing(false);

    }
}
