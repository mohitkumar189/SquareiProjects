package com.app.justclap.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.justclap.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.app.justclap.adapters.AdapterRequeststatusHireProf;
import com.app.justclap.asyntask.AsyncPostDataResponse;
import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.app.justclap.utils.AppUtils;

public class FragmentRequestStatus extends Fragment implements OnCustomItemClicListener, ListenerPostData {

    RecyclerView mRecyclerView;
    AdapterRequeststatusHireProf adapterRequeststatusHireProf;
    ModelService serviceDetail;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<ModelService> service_list;
    LinearLayoutManager layoutManager;
    ConnectionDetector cd;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        View view_service = inflater.inflate(R.layout.fragment_request_status, container, false);
        mRecyclerView = (RecyclerView) view_service.findViewById(R.id.recycler_view);

        context = getActivity();
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        hitService();

        return view_service;
    }

    private void hitService() {

        try {

            // for gcm condition check
            // =================================================

            cd = new ConnectionDetector(context);

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present


                AppUtils.showCustomAlert(getActivity(), getResources().getString(R.string.message_network_problem));

                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "userID", AppUtils.getUserId(context)));

                new AsyncPostDataResponse(FragmentRequestStatus.this, 1, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.getNewLeadsCustomer));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onItemClickListener(int position, int flag) {

    }

    @Override
    public void onPostRequestSucess(int position, String response) {

        try {

            if (position == 1) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {


                    JSONArray data = commandResult.getJSONArray("data");

                    service_list.removeAll(service_list);
                    for (int i = 0; i < data.length(); i++) {

                        serviceDetail = new ModelService();
                        serviceDetail.setCategoryName("XM Naukriz");
                        serviceDetail.setServicesCount("5 servies");
                        //   serviceDetail.setService_image("http://www.friendskorner.com/forum/photopost/data/500/amazing-nature-beauty1.jpg");

                        service_list.add(serviceDetail);


                    }

                    adapterRequeststatusHireProf = new AdapterRequeststatusHireProf(getActivity(), this, service_list);
                    mRecyclerView.setAdapter(adapterRequeststatusHireProf);


                } else {
                    Toast.makeText(context, commandResult.getString("message"),
                            Toast.LENGTH_SHORT).show();

                }


            }
        } catch (JSONException e) {

            e.printStackTrace();
        }


    }

    @Override
    public void onPostRequestFailed(int position, String response) {

    }
}
