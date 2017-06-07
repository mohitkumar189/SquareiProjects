package com.app.justclap.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.R;
import com.app.justclap.adapter.AdapterServicesCategory;
import com.app.justclap.aynctask.CommonAsyncTaskHashmap;
import com.app.justclap.interfaces.ApiResponse;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.model.ModelService;
import com.app.justclap.utils.AppUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ServicesCategory extends AppCompatActivity implements OnCustomItemClicListener, ApiResponse {

    private RecyclerView recycler_view;
    private Context context;
    private ImageView img_cross, img_search, img_service_icon;
    private ModelService modelService;
    private ArrayList<ModelService> listServices;
    private AdapterServicesCategory adapterServicesCategory;
    private String serviceId = "";
    private TextView text_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_category_page);
        context = this;
        init();
        serviceId = getIntent().getStringExtra("serviceId");
        setListener();
        getData();
    }

    private void getData() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                HashMap<String, String> hm = new HashMap<>();
                hm.put("isUnidirectional", "1");
                hm.put("categoryId", serviceId);
                String url = getResources().getString(R.string.url_base_new) + getResources().getString(R.string.getCategoryWiseServices);
                new CommonAsyncTaskHashmap(1, context, this).getqueryJson(url, hm);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setData() {
        String data = getIntent().getStringExtra("services");
        try {
            JSONArray services = new JSONArray(data);
            Log.e("services", "*" + services.toString());
            for (int i = 0; i < services.length(); i++) {

                JSONObject jo = services.getJSONObject(i);
                modelService = new ModelService();

                modelService.setServiceId(jo.getString("serviceID"));
                modelService.setServiceName(jo.getString("serviceName"));
                modelService.setImageUrl(getResources().getString(R.string.img_url) + jo.getString("serviceIcon"));

                listServices.add(modelService);
            }
            adapterServicesCategory = new AdapterServicesCategory(context, this, listServices);
            recycler_view.setAdapter(adapterServicesCategory);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setListener() {

        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_in_down);
            }
        });
    }

    private void init() {
        listServices = new ArrayList<>();
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        img_search = (ImageView) findViewById(R.id.img_search);
        img_cross = (ImageView) findViewById(R.id.img_cross);
        img_service_icon = (ImageView) findViewById(R.id.img_service_icon);
        text_title = (TextView) findViewById(R.id.text_title);
    }


    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 1) {
            Intent in = new Intent(context, ServicesDetail.class);
            in.putExtra("serviceId", serviceId);
            in.putExtra("isUniDirectional", "1");
            startActivity(in);
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
                    JSONObject CategoryDetails = maindata.getJSONObject("CategoryDetails");

                   /* "Title":"Hobbies & Lifestyle",
                            "Description":"Hobbies",
                            "IsUnidirectional":"1",
                            "BgColor":"#FFFFFF",
                            "CategoryIcon":"http://dev.justclapindia.com/jcnew/uploads/category/100/"*/
                    text_title.setText(CategoryDetails.getString("Title"));
                    Picasso.with(context).load(CategoryDetails.getString("CategoryIcon")).into(img_service_icon);
                    JSONArray services = CategoryDetails.getJSONArray("Services");

                    for (int i = 0; i < services.length(); i++) {

                        JSONObject jo = services.getJSONObject(i);
                        modelService = new ModelService();

                        modelService.setServiceId(jo.getString("ServiceId"));
                        modelService.setServiceName(jo.getString("ServiceName"));
                        modelService.setImageUrl(jo.getString("ServiceIcon"));
                        modelService.setDescription(jo.getString("ServiceDescription"));

                        listServices.add(modelService);
                    }
                    adapterServicesCategory = new AdapterServicesCategory(context, this, listServices);
                    recycler_view.setAdapter(adapterServicesCategory);

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
