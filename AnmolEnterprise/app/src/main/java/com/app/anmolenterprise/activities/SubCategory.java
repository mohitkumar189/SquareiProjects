package com.app.anmolenterprise.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.app.anmolenterprise.R;
import com.app.anmolenterprise.adapter.AdapterSubCategory;
import com.app.anmolenterprise.interfaces.OnCustomItemClicListener;
import com.app.anmolenterprise.model.ModelData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubCategory extends AppCompatActivity implements OnCustomItemClicListener {

    private Context context;
    private RecyclerView recycler_list;
    private ModelData model;
    private ArrayList<ModelData> listServices;
    private AdapterSubCategory adapterSubCategory;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        context = this;
        init();
        setListener();
        categoryList();
    }

    private void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void categoryList() {
        try {
            String array = getIntent().getStringExtra("categories");
            JSONArray category = new JSONArray(array);
            int length = category.length();
            for (int i = 0; i < length; i++) {

                JSONObject jo = category.getJSONObject(i);
                model = new ModelData();
                model.setId(jo.getString("Id"));
                model.setSubCategoryName(jo.getString("SubCategoryName"));
                model.setImage(jo.getString("Image"));
                model.setDescription(jo.getString("Description"));
                listServices.add(model);
            }
            adapterSubCategory = new AdapterSubCategory(context, this, listServices);
            recycler_list.setAdapter(adapterSubCategory);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void init() {

        listServices = new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Select Category");
        recycler_list = (RecyclerView) findViewById(R.id.recycler_list);
        recycler_list.setLayoutManager(new GridLayoutManager(context, 2));
    }

    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 1) {
            Intent in = new Intent(context, FillForm.class);
            in.putExtra("name", listServices.get(position).getSubCategoryName());
            startActivity(in);
        }
    }
}
