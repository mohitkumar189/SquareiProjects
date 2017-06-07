package com.app.justclap.vendorfragments;

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
import com.app.justclap.aynctask.CommonAsyncTaskHashmap;
import com.app.justclap.interfaces.ApiResponse;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.model.ModelVendor;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.vendor.VendorQuotePage;
import com.app.justclap.vendoradapter.AdapterVendorLeads;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class FragmentVendorLeads extends Fragment implements OnCustomItemClicListener, ApiResponse {

    private Context context;
    private RecyclerView recycler_request;
    private ArrayList<ModelVendor> listRequests;
    private AdapterVendorLeads adapterUserRequests;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_newleads, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        recycler_request = (RecyclerView) view.findViewById(R.id.recycler_request);
        recycler_request.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        listRequests = new ArrayList<>();
        setListener();

        getRequestData();

    }

    /*
    * for getting all requests detail
    */
    private void getRequestData() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                HashMap<String, String> hm = new HashMap<>();
                hm.put("userId", "1122");
                hm.put("pageNum", "1");

                String url = getResources().getString(R.string.url_base_new) + getResources().getString(R.string.newLeadsByVendorId);
                new CommonAsyncTaskHashmap(1, context, this).getqueryJson(url, hm);

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
            Intent in = new Intent(context, MapViewVendors.class);
            in.putExtra("vendorarray", listRequests.get(position).getRequestQueriesArray());
            in.putExtra("title", listRequests.get(position).getServiceName());
            startActivity(in);

        } else if (flag == 1) {
            Intent in = new Intent(context, VendorQuotePage.class);
            in.putExtra("vendorarray", listRequests.get(position).getJsonArray().toString());
            startActivityForResult(in,21);
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

                    JSONArray requests = maindata.getJSONArray("NewLeads");
                    for (int i = 0; i < requests.length(); i++) {

                        JSONObject jo = requests.getJSONObject(i);
                        ModelVendor modelService = new ModelVendor();

                        modelService.setJsonArray(jo);
                        modelService.setRequestId(jo.getString("RequestId"));
                        modelService.setServiceId(jo.getString("ServiceId"));
                        modelService.setIsUnidirectional(jo.getString("IsUnidirectional"));
                        modelService.setRequestCode(jo.getString("RequestCode"));
                        modelService.setRequestLatitude(jo.getString("RequestLatitude"));
                        modelService.setRequestLongitude(jo.getString("RequestLongitude"));
                        modelService.setRequestDate(jo.getString("RequestDate"));
                        modelService.setRequestTime(jo.getString("RequestTime"));
                        modelService.setServiceName(jo.getString("ServiceName"));
                        modelService.setServiceIcon(jo.getString("ServiceIcon"));
                        modelService.setRequestQueriesArray(jo.getString("RequestQueries"));
                        modelService.setRowType(1);
                        modelService.setLeadId(jo.getString("LeadId"));
                        modelService.setIsReviewed(jo.getString("IsReviewed"));
                        modelService.setRewardPoint(jo.getString("RewardPoint"));
                        modelService.setOfferPercentage(jo.getString("OfferPercentage"));
                        modelService.setUserId(jo.getString("UserId"));
                        modelService.setUserName(jo.getString("UserName"));
                        modelService.setUserEmail(jo.getString("UserEmail"));
                        modelService.setUserMobile(jo.getString("UserMobile"));
                        modelService.setUserImage(jo.getString("UserImage"));
                        modelService.setUserCoverImage(jo.getString("UserCoverImage"));

                        listRequests.add(modelService);
                    }
                    adapterUserRequests = new AdapterVendorLeads(context, FragmentVendorLeads.this, listRequests);
                    recycler_request.setAdapter(adapterUserRequests);

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
