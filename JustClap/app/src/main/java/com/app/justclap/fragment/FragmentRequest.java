package com.app.justclap.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.app.justclap.activities.Activity_RequestCompleted;
import com.app.justclap.activities.Activity_RequestStatus;
import com.app.justclap.activities.Activity_SearchNaukriList;
import com.app.justclap.activities.Activity_VendorHired;
import com.app.justclap.activities.BookingHistory;
import com.app.justclap.activities.ViewDetailAnswers;
import com.app.justclap.adapters.AdapterResponses;
import com.app.justclap.asyntask.AsyncPostDataFragment;
import com.app.justclap.asyntask.AsyncPostDataFragmentNoProgress;
import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.vendordetail.ViewBidirectionalQuesAnswers;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentRequest extends Fragment implements OnCustomItemClicListener, ListenerPostData {

    RecyclerView list_request;
    AdapterResponses adapterResponses;
    ModelService serviceDetail;
    ArrayList<ModelService> service_list;
    Context context;
    FloatingActionButton floatingIcon;
    ConnectionDetector cd;
    RelativeLayout rl_main_layout, rl_network;
    LinearLayout linear_empty_list;
    int deleteposition, menu_position;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String reason = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        View view_service = inflater.inflate(R.layout.fragment_request, container, false);
        context = getActivity();
        service_list = new ArrayList<>();
        linear_empty_list = (LinearLayout) view_service.findViewById(R.id.linear_empty_list);
        rl_main_layout = (RelativeLayout) view_service.findViewById(R.id.rl_main_layout);
        rl_network = (RelativeLayout) view_service.findViewById(R.id.rl_network);
        floatingIcon = (FloatingActionButton) view_service.findViewById(R.id.floatingIcon);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view_service.findViewById(R.id.swipeRefreshLayout1);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        list_request = (RecyclerView) view_service.findViewById(R.id.list_request);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list_request.setLayoutManager(layoutManager);

        if (!AppUtils.getUserId(context).equalsIgnoreCase("")) {
            floatingIcon.setVisibility(View.VISIBLE);
            getServicelist();
        } else {
            floatingIcon.setVisibility(View.GONE);
            linear_empty_list.setVisibility(View.VISIBLE);

        }

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

        floatingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getActivity(), BookingHistory.class);
                startActivity(in);

            }
        });


    }

    private void getServicelist() {

        try {

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
                                "userID", AppUtils.getUserId(context)));
                new AsyncPostDataFragment(getActivity(), (ListenerPostData) this, 1, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.getVendorQuoteList));

            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void getServicelistrefresh() {

        try {

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
                                "userID", AppUtils.getUserId(context)));
                new AsyncPostDataFragmentNoProgress(getActivity(), (ListenerPostData) this, 2, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.getVendorQuoteList));

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
                                "userID", AppUtils.getUserId(context)));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "serviceID", service_list.get(deleteposition).getServiceID()));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "searchId", service_list.get(deleteposition).getSearchId()));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "reason", reason));
                new AsyncPostDataFragment(getActivity(), (ListenerPostData) this, 3, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.deleteCustomerService));
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
            showMenu();
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
        final CharSequence[] items = {"View Details", "Cancel Request", "View Status"};

        AlertDialog.Builder builder = new AlertDialog.Builder(
                context);
        builder.setTitle("  JustClap");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("View Details")) {

                    if (service_list.get(menu_position).getIsNaukri().equalsIgnoreCase("1")) {
                        Intent in = new Intent(getActivity(), ViewDetailAnswers.class);
                        in.putExtra("service_id", service_list.get(menu_position).getServiceID());
                        in.putExtra("service_name", service_list.get(menu_position).getServiceName());
                        in.putExtra("searchId", service_list.get(menu_position).getSearchId());
                        startActivity(in);
                    } else {
                        Intent in = new Intent(getActivity(), ViewBidirectionalQuesAnswers.class);
                        in.putExtra("service_id", service_list.get(menu_position).getServiceID());
                        in.putExtra("service_name", service_list.get(menu_position).getServiceName());
                        in.putExtra("searchId", service_list.get(menu_position).getSearchId());
                        in.putExtra("user_id", AppUtils.getUserId(context));
                        startActivity(in);
                    }
                    dialog.dismiss();
                    dialog.cancel();
                } else if (items[item].equals("Cancel Request")) {

                    showConfirmtion();
                    dialog.dismiss();
                    dialog.cancel();

                } else if (items[item].equals("View Status")) {

                    if (service_list.get(menu_position).getIsNaukri().equalsIgnoreCase("1")) {
                        if (service_list.get(menu_position).getFlag().equalsIgnoreCase("1")) {
                            Intent in = new Intent(getActivity(), Activity_RequestCompleted.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            in.putExtra("service_id", service_list.get(menu_position).getServiceID());
                            in.putExtra("service_name", service_list.get(menu_position).getServiceName());
                            startActivity(in);
                            dialog.dismiss();
                        } else if (service_list.get(menu_position).getFlag().equalsIgnoreCase("0")) {
                            Intent in = new Intent(getActivity(), Activity_RequestStatus.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            in.putExtra("service_image", service_list.get(menu_position).getServiceImage());
                            in.putExtra("service_id", service_list.get(menu_position).getServiceID());
                            in.putExtra("service_name", service_list.get(menu_position).getServiceName());
                            in.putExtra("searchId", service_list.get(menu_position).getSearchId());
                            in.putExtra("time", service_list.get(menu_position).getBookingdate());
                            startActivity(in);
                            dialog.dismiss();
                        } else if (service_list.get(menu_position).getFlag().equalsIgnoreCase("2")) {
                            Intent in = new Intent(getActivity(), Activity_VendorHired.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            in.putExtra("service_image", service_list.get(menu_position).getServiceImage());
                            in.putExtra("service_id", service_list.get(menu_position).getServiceID());
                            in.putExtra("service_name", service_list.get(menu_position).getServiceName());
                            in.putExtra("vendor_id", service_list.get(menu_position).getHiredVendor());
                            startActivity(in);
                            dialog.dismiss();
                        }
                    } else if (service_list.get(menu_position).getIsNaukri().equalsIgnoreCase("2")) {

                        Intent in = new Intent(getActivity(), Activity_SearchNaukriList.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        in.putExtra("service_id", service_list.get(menu_position).getServiceID());
                        in.putExtra("service_name", service_list.get(menu_position).getServiceName());
                        in.putExtra("vendor_naukri", "2");
                        in.putExtra("searchId", service_list.get(menu_position).getSearchId());
                        startActivity(in);
                        dialog.dismiss();
                    }
                }
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

                    JSONObject data = commandResult.getJSONObject("data");
                    service_list.removeAll(service_list);
                    JSONArray services = data.getJSONArray("services");

                    Log.e("services", "*" + services.length());
                    if (services.length() > 0) {
                        for (int i = 0; i < services.length(); i++) {

                            JSONObject jo = services.getJSONObject(i);

                            serviceDetail = new ModelService();
                            serviceDetail.setServiceName(jo.getString("serviceName"));
                            if (jo.has("NoOfVendor")) {
                                serviceDetail.setServicesCount(jo.getString("NoOfVendor"));
                            } else {
                                serviceDetail.setServicesCount("");
                            }
                            serviceDetail.setSearchId(jo.getString("searchId"));
                            serviceDetail.setIsNaukri(jo.getString("is_uniDirectional"));
                            serviceDetail.setServiceID(jo.getString("serviceID"));
                            serviceDetail.setFlag(jo.getString("flag"));
                            serviceDetail.setBookingdate(jo.getString("timeslot") + " " + jo.getString("day") + " " + jo.getString("date"));
                            serviceDetail.setServiceImage(getResources().getString(R.string.img_url) + jo.getString("serviceImage"));
                            if (jo.has("vendors")) {
                                serviceDetail.setVendorArray(jo.getJSONArray("vendors").toString());
                            } else {
                                serviceDetail.setVendorArray("");
                            }
                            if (jo.has("jobsUsers")) {
                                serviceDetail.setNaukriArray(jo.getJSONArray("jobsUsers").toString());
                            } else {
                                serviceDetail.setNaukriArray("");
                            }

                            if (jo.has("vendorID")) {
                                serviceDetail.setHiredVendor(jo.getString("vendorID"));
                            }
                            service_list.add(serviceDetail);
                        }


                        adapterResponses = new AdapterResponses(getActivity(), this, service_list);
                        list_request.setAdapter(adapterResponses);
                        //   mSwipeRefreshLayout.setRefreshing(false);

                        if (service_list.size() > 0) {
                            linear_empty_list.setVisibility(View.GONE);
                        } else {
                            linear_empty_list.setVisibility(View.VISIBLE);
                        }

                        rl_main_layout.setVisibility(View.VISIBLE);
                        rl_network.setVisibility(View.GONE);
                    }

                } else {
                    //  AppUtils.showCustomAlert(getActivity(), commandResult.getString("message"));

                }
            } else if (position == 2) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");

                    JSONArray services = data.getJSONArray("services");
                    service_list.removeAll(service_list);
                    for (int i = 0; i < services.length(); i++) {

                        JSONObject jo = services.getJSONObject(i);

                        serviceDetail = new ModelService();
                        serviceDetail.setServiceName(jo.getString("serviceName"));
                        if (jo.has("NoOfVendor")) {
                            serviceDetail.setServicesCount(jo.getString("NoOfVendor"));
                        } else {
                            serviceDetail.setServicesCount("");
                        }
                        serviceDetail.setServiceID(jo.getString("serviceID"));
                        serviceDetail.setFlag(jo.getString("flag"));
                        serviceDetail.setIsNaukri(jo.getString("is_uniDirectional"));
                        serviceDetail.setSearchId(jo.getString("searchId"));
                        serviceDetail.setBookingdate(jo.getString("timeslot") + " " + jo.getString("day") + " " + jo.getString("date"));
                        serviceDetail.setServiceImage(getResources().getString(R.string.img_url) + jo.getString("serviceImage"));
                        if (jo.has("vendors")) {
                            serviceDetail.setVendorArray(jo.getJSONArray("vendors").toString());
                        } else {
                            serviceDetail.setVendorArray("");
                        }

                        if (jo.has("jobsUsers")) {
                            serviceDetail.setNaukriArray(jo.getJSONArray("jobsUsers").toString());
                        } else {
                            serviceDetail.setNaukriArray("");
                        }

                        if (jo.has("vendorID")) {
                            serviceDetail.setHiredVendor(jo.getString("vendorID"));
                        }

                        service_list.add(serviceDetail);
                    }

                    adapterResponses = new AdapterResponses(getActivity(), this, service_list);
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

                    adapterResponses.notifyDataSetChanged();
                    list_request.invalidate();
                    //  mSwipeRefreshLayout.setRefreshing(true);


                } else {
                    AppUtils.showCustomAlert(getActivity(), commandResult.getString("message"));

                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostRequestFailed(int position, String response) {

        //    AppUtils.showCustomAlert(getActivity(), getResources().getString(R.string.problem_server));
        mSwipeRefreshLayout.setRefreshing(false);

    }
}
