package com.app.justclap.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.adapter.AdapterFragmentBiServiceList;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.model.ModelService;
import com.app.justclap.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class FragmentBiServiceList extends Fragment implements OnCustomItemClicListener {


    private Context context;
    private RecyclerView recycler_view;
    private ArrayList<ModelService> arrayList;
    private AdapterFragmentBiServiceList adapterFragmentBiServiceList;
    boolean isConfirm = false;
    private TextView text_category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bi_service_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        arrayList = new ArrayList<>();
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        text_category = (TextView) view.findViewById(R.id.text_category);
        AppUtils.fontGotham_Medium(text_category, context);
        setData();

    }

    private void setData() {
        try {
            String data = AppUtils.getCategoryJsondata(context);

            JSONObject jsonObject = new JSONObject(data);

            JSONArray bidirectionalServices = jsonObject.getJSONArray("BidirectionalServices");
            for (int i = 0; i < bidirectionalServices.length(); i++) {

                JSONObject jo = bidirectionalServices.getJSONObject(i);
                ModelService model = new ModelService();

                model.setServiceId(jo.getString("ServiceId"));
                model.setServiceName(jo.getString("ServiceName"));
                model.setDescription(jo.getString("ServiceDescription"));
                model.setServiceIcon(jo.getString("ServiceIcon"));
                model.setSelected(false);
                model.setIs_uniDirectional(jo.getString("IsUnidirectional"));

                arrayList.add(model);
            }

            adapterFragmentBiServiceList = new AdapterFragmentBiServiceList(context, this, arrayList);
            recycler_view.setAdapter(adapterFragmentBiServiceList);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 1) {
            if (AppUtils.getUniSelectedServices(context).equalsIgnoreCase("")) {
                int lenght = arrayList.size();
                for (int i = 0; i < lenght; i++) {
                    arrayList.get(i).setSelected(false);
                }
                arrayList.get(position).setSelected(true);

                adapterFragmentBiServiceList.notifyDataSetChanged();

                String selectedservices = "";
                for (int i = 0; i < lenght; i++) {
                    if (arrayList.get(i).isSelected()) {
                        selectedservices = arrayList.get(i).getServiceId();
                    }
                }
                AppUtils.setBiSelectedServices(context, selectedservices);
            } else {
                if (showConfimationAlert()) {


                    int lenght = arrayList.size();
                    for (int i = 0; i < lenght; i++) {
                        arrayList.get(i).setSelected(false);
                    }
                    arrayList.get(position).setSelected(true);

                    adapterFragmentBiServiceList.notifyDataSetChanged();

                    String selectedservices = "";
                    for (int i = 0; i < lenght; i++) {
                        if (arrayList.get(i).isSelected()) {
                            selectedservices = arrayList.get(i).getServiceId();
                        }
                    }
                    AppUtils.setBiSelectedServices(context, selectedservices);
                } else {
                    for (int i = 0; i < arrayList.size(); i++) {
                        arrayList.get(i).setSelected(false);
                        adapterFragmentBiServiceList.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    private Boolean showConfimationAlert() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);

        //    alertDialog.setTitle("LOG OUT !");
        alertDialog.setMessage("You can select only one type of service, If you select bidirectional service, you cannot select unidirectional services");

        alertDialog.setPositiveButton("Continue",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        isConfirm = true;
                        AppUtils.setUniSelectedServices(context, "");
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
