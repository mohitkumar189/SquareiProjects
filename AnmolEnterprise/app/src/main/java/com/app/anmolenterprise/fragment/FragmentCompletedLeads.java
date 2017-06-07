package com.app.anmolenterprise.fragment;

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

import com.app.anmolenterprise.R;
import com.app.anmolenterprise.activities.LeadDetail;
import com.app.anmolenterprise.adapter.AdapterCompletedLeads;
import com.app.anmolenterprise.aynctask.CommonAsyncTaskHashmap;
import com.app.anmolenterprise.interfaces.ApiResponse;
import com.app.anmolenterprise.interfaces.OnCustomItemClicListener;
import com.app.anmolenterprise.model.ModelData;
import com.app.anmolenterprise.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentCompletedLeads extends Fragment implements OnCustomItemClicListener, ApiResponse {

    private Context context;
    private RecyclerView recycler_list;
    private ModelData model;
    private ArrayList<ModelData> listServices;
    private AdapterCompletedLeads adapterNewLeads;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_newleads, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        listServices = new ArrayList<>();
        recycler_list = (RecyclerView) view.findViewById(R.id.recycler_list);
        recycler_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        setListener();
        categoryList();
    }

    private void setListener() {

    }

    public void categoryList() {
        if (AppUtils.isNetworkAvailable(context)) {
            String params = "?user_id=" + AppUtils.getUserId(context) + "&user_role=" + AppUtils.getUserRole(context);

            String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.getCompleteLead);
            new CommonAsyncTaskHashmap(1, context, this).getquery(url + params);

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 1) {
            Intent in = new Intent(context, LeadDetail.class);

            in.putExtra("detail", listServices.get(position).getSetJsonArray());
            startActivity(in);
        }
    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {
        try {
            if (method == 1) {
                JSONObject commandResult = response.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONArray array = commandResult.getJSONArray("data");
                    listServices.clear();
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jo = array.getJSONObject(i);
                        model = new ModelData();

                        model.setSetJsonArray(jo.toString());
                        model.setFull_name(jo.getString("full_name"));
                        model.setEmail(jo.getString("email"));
                        model.setMobile(jo.getString("mobile"));
                        model.setPincode(jo.getString("pincode"));
                        model.setAddress(jo.getString("address"));
                        model.setProduct(jo.getString("product"));
                        model.setCompany_name(jo.getString("company_name"));
                        model.setPurchase_date(jo.getString("purchase_date"));
                        model.setComment(jo.getString("comment"));
                        model.setAutolocation(jo.getString("autolocation"));
                        model.setCreate_on(jo.getString("create_on"));
                        model.setStatus(jo.getString("status"));

                        listServices.add(model);

                    }
                    adapterNewLeads = new AdapterCompletedLeads(context, this, listServices);
                    recycler_list.setAdapter(adapterNewLeads);


                } else {
                    listServices.clear();
                    if (adapterNewLeads != null) {
                        adapterNewLeads.notifyDataSetChanged();
                    }
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostFail(int method, String response) {

    }
}
