package com.app.anmolenterprise.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.anmolenterprise.R;
import com.app.anmolenterprise.adapter.AdapterNewLeads;
import com.app.anmolenterprise.aynctask.CommonAsyncTaskHashmap;
import com.app.anmolenterprise.interfaces.ApiResponse;
import com.app.anmolenterprise.interfaces.OnCustomItemClicListener;
import com.app.anmolenterprise.model.ModelData;
import com.app.anmolenterprise.utils.AppUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewLeads extends AppCompatActivity implements OnCustomItemClicListener, ApiResponse {

    private Context context;
    private RecyclerView recycler_list;
    private ModelData model;
    private ArrayList<ModelData> listServices;
    private AdapterNewLeads adapterNewLeads;
    private Toolbar toolbar;
    private ImageView img_cross;
    private RelativeLayout rl_image;
    private ImageView download_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newleads);
        context = this;
        init();
        setListener();
        categoryList();
    }

    private void setListener() {
        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_image.setVisibility(View.GONE);

            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void categoryList() {
        if (AppUtils.isNetworkAvailable(context)) {
            String params = "?user_id=" + AppUtils.getUserId(context) + "&user_role=" + AppUtils.getUserRole(context)
                    + "&service=" + AppUtils.getService(context);

            String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.getOngoingLead);
            new CommonAsyncTaskHashmap(1, context, this).getquery(url + params);

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }


    private void init() {
        listServices = new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("New leads");
        recycler_list = (RecyclerView) findViewById(R.id.recycler_list);

        rl_image = (RelativeLayout) findViewById(R.id.rl_image);
        download_image = (ImageView) findViewById(R.id.download_image);
        img_cross = (ImageView) findViewById(R.id.img_cross);

        recycler_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 1) {
            Intent in = new Intent(context, LeadDetail.class);
            in.putExtra("detail", listServices.get(position).getSetJsonArray());
            startActivity(in);
        } else if (flag == 2) {
            completeLead(position);
        } else if (flag == 3) {

            if (!listServices.get(position).getImage().equalsIgnoreCase("")) {
                rl_image.setVisibility(View.VISIBLE);
                Picasso.with(context).load(listServices.get(position).getImage()).into(download_image);
            }
        }
    }

    public void completeLead(int position) {
        if (AppUtils.isNetworkAvailable(context)) {
            String params = "?user_id=" + AppUtils.getUserId(context) + "&id=" + listServices.get(position).getId();
            String url = getResources().getString(R.string.base_url) + getResources().getString(R.string.leadComplete);
            new CommonAsyncTaskHashmap(2, context, this).getquery(url + params);

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (rl_image.getVisibility() == View.VISIBLE) {
                rl_image.setVisibility(View.GONE);
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
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

                        model.setId(jo.getString("id"));
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
                        model.setImage(jo.getString("image"));
                        listServices.add(model);
                    }
                    adapterNewLeads = new AdapterNewLeads(context, this, listServices);
                    recycler_list.setAdapter(adapterNewLeads);
                } else {
                    listServices.clear();
                    if (adapterNewLeads != null) {
                        adapterNewLeads.notifyDataSetChanged();
                    }
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }

            } else if (method == 2) {
                JSONObject commandResult = response.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                    categoryList();
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
