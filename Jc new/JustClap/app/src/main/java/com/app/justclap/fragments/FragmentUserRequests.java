package com.app.justclap.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.justclap.R;
import com.app.justclap.activities.MapViewVendors;
import com.app.justclap.adapter.AdapterUserPastRequest;
import com.app.justclap.adapter.AdapterUserRequests;
import com.app.justclap.aynctask.CommonAsyncTaskHashmap;
import com.app.justclap.interfaces.ApiResponse;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.model.ModelRequest;
import com.app.justclap.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class FragmentUserRequests extends Fragment implements OnCustomItemClicListener, ApiResponse {

    private Context context;
    private RecyclerView recycler_request, recycler_past_request;
    private ArrayList<ModelRequest> listRequests;
    private ArrayList<ModelRequest> listPastRequests;
    private AdapterUserRequests adapterUserRequests;
    private ModelRequest modelRequest;
    private AdapterUserPastRequest adapterUserPastRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_request, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        recycler_request = (RecyclerView) view.findViewById(R.id.recycler_request);
        recycler_request.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        recycler_past_request = (RecyclerView) view.findViewById(R.id.recycler_past_request);
        recycler_past_request.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        listRequests = new ArrayList<>();
        listPastRequests = new ArrayList<>();
        setListener();
        getRequestData();
        if (getUserVisibleHint()) {

        }
    }


    /*
            * for getting all requests detail
            */
    private void getRequestData() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                HashMap<String, String> hm = new HashMap<>();
                hm.put("userId", "1124");
                hm.put("pageNum", "1");

                String url = getResources().getString(R.string.url_base_new) + getResources().getString(R.string.getRequestByUserId);
                new CommonAsyncTaskHashmap(1, context, this).getqueryJson(url, hm);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
   * for referesh list
   */
    private void refreshRequestData() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                HashMap<String, String> hm = new HashMap<>();
                hm.put("userId", "1124");
                hm.put("pageNum", "1");

                String url = getResources().getString(R.string.url_base_new) + getResources().getString(R.string.getRequestByUserId);
                new CommonAsyncTaskHashmap(1, context, this).getqueryJsonNoProgress(url, hm);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setListener() {

    }

    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 2) {
            if (listRequests.get(position).getQuoteCount() > 0) {
                Intent in = new Intent(context, MapViewVendors.class);
                in.putExtra("vendorarray", listRequests.get(position).getQuoteVendorsArray());
                in.putExtra("title", listRequests.get(position).getServiceName());
                in.putExtra("RequestId", listRequests.get(position).getRequestId());
                startActivityForResult(in, 21);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 21 && resultCode == 22) {
            refreshRequestData();
        }
    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {
        try {
            if (method == 1) {

                JSONObject commandResult = response
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    JSONObject maindata = commandResult.getJSONObject("data");
                    listRequests.clear();
                    JSONArray requests = maindata.getJSONArray("OngoingServices");
                    for (int i = 0; i < requests.length(); i++) {

                        JSONObject jo = requests.getJSONObject(i);
                        ModelRequest modelService = new ModelRequest();

                        modelService.setRequestId(jo.getString("RequestId"));
                        modelService.setServiceId(jo.getString("ServiceId"));
                        modelService.setIsUnidirectional(jo.getString("IsUnidirectional"));
                        modelService.setRequestCode(jo.getString("RequestCode"));
                        modelService.setRequestLatitude(jo.getString("RequestLatitude"));
                        modelService.setRequestLongitude(jo.getString("RequestLongitude"));
                        modelService.setRequestDate(jo.getString("RequestDate"));
                        modelService.setRequestTime(jo.getString("RequestTime"));
                        modelService.setRequestStatusId(jo.getString("RequestStatusId"));
                        modelService.setRequestStatus(jo.getString("RequestStatus"));
                        modelService.setServiceName(jo.getString("ServiceName"));
                        modelService.setServiceIcon(jo.getString("ServiceIcon"));
                        modelService.setQuoteCount(jo.getInt("QuoteCount"));
                        modelService.setQuoteVendorsArray(jo.getJSONArray("QuoteVendors").toString());

                        listRequests.add(modelService);
                    }
                    adapterUserRequests = new AdapterUserRequests(context, FragmentUserRequests.this, listRequests);
                    recycler_request.setAdapter(adapterUserRequests);


                    JSONArray pastServices = maindata.getJSONArray("PastServices");
                    for (int i = 0; i < pastServices.length(); i++) {

                        JSONObject jo = pastServices.getJSONObject(i);
                        ModelRequest modelService = new ModelRequest();

                        modelService.setRequestId(jo.getString("RequestId"));
                        modelService.setServiceId(jo.getString("ServiceId"));
                        modelService.setIsUnidirectional(jo.getString("IsUnidirectional"));
                        modelService.setRequestCode(jo.getString("RequestCode"));
                        modelService.setRequestLatitude(jo.getString("RequestLatitude"));
                        modelService.setRequestLongitude(jo.getString("RequestLongitude"));
                        modelService.setRequestDate(jo.getString("RequestDate"));
                        modelService.setRequestTime(jo.getString("RequestTime"));
                        modelService.setRequestStatusId(jo.getString("RequestStatusId"));
                        modelService.setRequestStatus(jo.getString("RequestStatus"));
                        modelService.setServiceName(jo.getString("ServiceName"));
                        modelService.setServiceIcon(jo.getString("ServiceIcon"));
                        modelService.setRequestStatusImage(jo.getString("RequestStatusImage"));
                        modelService.setQuoteCount(jo.getInt("QuoteCount"));
                        modelService.setQuoteVendorsArray(jo.getJSONArray("QuoteVendors").toString());
                        modelService.setRowType(1);
                        listPastRequests.add(modelService);
                    }
                    adapterUserPastRequest = new AdapterUserPastRequest(context, FragmentUserRequests.this, listPastRequests);
                    recycler_past_request.setAdapter(adapterUserPastRequest);

                } else {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();

                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void onPostFail(int method, String response) {

    }
}
