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
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.activities.UniServiceListActivity;
import com.app.justclap.adapter.AdapterFragmentuniServiceList;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.model.ModelService;
import com.app.justclap.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class FragmentUniServiceList extends Fragment implements OnCustomItemClicListener {


    private Context context;
    private RecyclerView recycler_view;
    private ArrayList<ModelService> arrayList;
    private AdapterFragmentuniServiceList adapterFragmentuniServiceList;
    private TextView text_category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_uni_service_list, container, false);
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

            JSONArray unidirectionalCategories = jsonObject.getJSONArray("UnidirectionalCategories");
            for (int i = 0; i < unidirectionalCategories.length(); i++) {

                JSONObject jo = unidirectionalCategories.getJSONObject(i);
                ModelService model = new ModelService();

                model.setCategoryID(jo.getString("CategoryId"));
                model.setCategoryName(jo.getString("CategoryName"));
                model.setDescription(jo.getString("CategoryDescription"));
                model.setServiceIcon(jo.getString("CategoryIcon"));
                model.setIs_uniDirectional(jo.getString("IsUnidirectional"));
                model.setServicesArray(jo.getJSONArray("Services").toString());

                arrayList.add(model);

            }

            adapterFragmentuniServiceList = new AdapterFragmentuniServiceList(context, this, arrayList);
            recycler_view.setAdapter(adapterFragmentuniServiceList);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 1) {

            Intent intent = new Intent(context, UniServiceListActivity.class);
            intent.putExtra("Servicesarray", arrayList.get(position).getServicesArray());
            intent.putExtra("ServicesName", arrayList.get(position).getCategoryName());
            startActivityForResult(intent, 21);
        }
    }

}
