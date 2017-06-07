package com.app.justclap.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.R;
import com.app.justclap.adapter.AdapterUniServiceList;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.model.ModelService;
import com.app.justclap.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UniServiceListActivity extends AppCompatActivity implements OnCustomItemClicListener {

    private RecyclerView recycler_view;
    private Context context;
    private ModelService modelService;
    private Toolbar toolbar;
    private TextView text_continue;
    private ArrayList<ModelService> arrayList;
    private AdapterUniServiceList adapterUniServiceList;
    private boolean isConfirm = false;
    private TextView text_category;
    private String selectedservices = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uniservicelist);
        context = this;
        init();
        setListener();
        setData();

        text_category.setText("Select service for " + getIntent().getStringExtra("ServicesName"));
    }

    private void setData() {

        arrayList = new ArrayList<>();

        String data = getIntent().getStringExtra("Servicesarray");
        Log.e("array", "*8" + data);
        try {
            JSONArray services = new JSONArray(data);
            for (int i = 0; i < services.length(); i++) {

                JSONObject jo = services.getJSONObject(i);
                modelService = new ModelService();

                modelService.setServiceId(jo.getString("ServiceId"));
                modelService.setServiceName(jo.getString("ServiceName"));
                modelService.setImageUrl(jo.getString("ServiceIcon"));
                modelService.setSelected(false);
                modelService.setIs_uniDirectional(jo.getString("IsUnidirectional"));
                modelService.setDescription(jo.getString("ServiceDescription"));

                arrayList.add(modelService);
            }
            adapterUniServiceList = new AdapterUniServiceList(context, this, arrayList);
            recycler_view.setAdapter(adapterUniServiceList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        text_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!selectedservices.equalsIgnoreCase("")) {
                    setResult(52);
                    finish();
                } else {
                    Toast.makeText(context, "Please select service", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Select Services");
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        text_continue = (TextView) findViewById(R.id.text_continue);
        text_category = (TextView) findViewById(R.id.text_category);
        AppUtils.fontGotham_Medium(text_category, context);
        text_category = (TextView) findViewById(R.id.text_category);
        AppUtils.fontGotham_Medium(text_category, context);
    }


    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 1) {
            if (AppUtils.getBiSelectedServices(context).equalsIgnoreCase("")) {
                if (arrayList.get(position).isSelected()) {
                    arrayList.get(position).setSelected(false);
                } else {
                    arrayList.get(position).setSelected(true);
                }
                adapterUniServiceList.notifyDataSetChanged();
                selectedservices="";
                int lenght = arrayList.size();

                for (int i = 0; i < lenght; i++) {
                    if (arrayList.get(i).isSelected()) {
                        if (selectedservices.equalsIgnoreCase("")) {
                            selectedservices = arrayList.get(i).getServiceId();
                        } else {
                            selectedservices = selectedservices + "," + arrayList.get(i).getServiceId();
                        }
                    }
                }
                AppUtils.setUniSelectedServices(context, selectedservices);
            } else {
                if (showConfimationAlert()) {
                    AppUtils.setBiSelectedServices(context, "");
                    if (arrayList.get(position).isSelected()) {
                        arrayList.get(position).setSelected(false);
                    } else {
                        arrayList.get(position).setSelected(true);
                    }
                    adapterUniServiceList.notifyDataSetChanged();

                    int lenght = arrayList.size();
                    String selectedservices = "";
                    for (int i = 0; i < lenght; i++) {
                        if (arrayList.get(i).isSelected()) {
                            if (selectedservices.equalsIgnoreCase("")) {
                                selectedservices = arrayList.get(i).getServiceId();
                            } else {
                                selectedservices = selectedservices + "," + arrayList.get(i).getServiceId();
                            }
                        }
                    }
                    AppUtils.setUniSelectedServices(context, selectedservices);
                } else {
                    for (int i = 0; i < arrayList.size(); i++) {
                        arrayList.get(i).setSelected(false);
                        adapterUniServiceList.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    private Boolean showConfimationAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);

        //    alertDialog.setTitle("LOG OUT !");
        alertDialog.setMessage("You can select only one type of service, If you select unidirectional service, you cannot select bidirectional services");

        alertDialog.setPositiveButton("Continue",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        isConfirm = true;
                        AppUtils.setBiSelectedServices(context, "");
                        dialog.cancel();
                    }

                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        isConfirm = false;
                        dialog.cancel();
                    }
                });

        alertDialog.show();
        return isConfirm;

    }

}
