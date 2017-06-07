package com.app.justclap.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.adapter.AdapterAllServices;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.model.ModelService;
import com.app.justclap.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllServicesPage extends AppCompatActivity implements OnCustomItemClicListener {

    private TextView text_explore;
    private RecyclerView recycler_view;
    private Context context;
    private ImageView img_cross, img_search;
    private ModelService modelService;
    private ArrayList<ModelService> listServices;
    private AdapterAllServices adapterTrendingServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_services_page);
        context = this;
        init();
        setListener();
        setData();
    }

    private void setData() {

        listServices = new ArrayList<>();
        String data = getIntent().getStringExtra("services");
        try {
            JSONArray services = new JSONArray(data);
            for (int i = 0; i < services.length(); i++) {

                JSONObject jo = services.getJSONObject(i);
                modelService = new ModelService();

                modelService.setServiceId(jo.getString("ServiceId"));
                modelService.setServiceName(jo.getString("ServiceName"));
                modelService.setImageUrl(jo.getString("ServiceIcon"));
                modelService.setDescription(jo.getString("ServiceDescription"));

                listServices.add(modelService);
            }
            adapterTrendingServices = new AdapterAllServices(context, this, listServices);
            recycler_view.setAdapter(adapterTrendingServices);

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
        text_explore = (TextView) findViewById(R.id.text_explore);
        AppUtils.fontGotham_Book(text_explore, context);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new GridLayoutManager(context, 3));
        img_search = (ImageView) findViewById(R.id.img_search);
        img_cross = (ImageView) findViewById(R.id.img_cross);
    }

    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.stay, R.anim.slide_in_down);
    }

    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 2) {
            Intent in = new Intent(context, ServicesDetail.class);
            in.putExtra("serviceId", listServices.get(position).getServiceId());
            in.putExtra("isUniDirectional","1");
            startActivity(in);
        }
    }
}
