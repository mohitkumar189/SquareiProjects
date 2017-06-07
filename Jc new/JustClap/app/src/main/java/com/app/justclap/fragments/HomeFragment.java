package com.app.justclap.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.activities.AllServicesPage;
import com.app.justclap.activities.ServicesCategory;
import com.app.justclap.adapter.AdapterBanner;
import com.app.justclap.adapter.AdapterPopularServices;
import com.app.justclap.adapter.AdapterTrendingServices;
import com.app.justclap.adapter.AdapterTrendingServices2;
import com.app.justclap.adapter.AdapterTrendingServices3;
import com.app.justclap.adapter.AdapterUsedServices;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.model.ModelService;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.utils.MyConstant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements OnCustomItemClicListener {

    private Context context;
    private RecyclerView recycler_banner, recycler_popular, recycler_used_services, recycler_trending_services3, recycler_trending_services2, recycler_trending_services1;
    private Button btn_explore;
    private AdapterPopularServices adapterPopularServices;
    private AdapterBanner adapterBanner;
    private ModelService modelService;
    private ArrayList<ModelService> listPopularServices;
    private ArrayList<ModelService> offerImages;
    private AdapterTrendingServices adapterTrendingServices;
    private AdapterTrendingServices2 adapterTrendingServices2;
    private AdapterTrendingServices3 adapterTrendingServices3;
    private ArrayList<ModelService> trending_list;
    private ArrayList<ModelService> trending_list2;
    private ArrayList<ModelService> trending_list3;
    private Button btn_refer;
    private ArrayList<ModelService> used_list;
    private AdapterUsedServices adapterUsedServices;
    private TextView all_services, text_trending_services, text_used_services, text_trending_services3, text_trending_services2, text_trending_services1;
    private String unidirectionalServices = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        btn_explore = (Button) view.findViewById(R.id.btn_explore);
        btn_refer = (Button) view.findViewById(R.id.btn_refer);
        text_used_services = (TextView) view.findViewById(R.id.text_used_services);
        text_trending_services = (TextView) view.findViewById(R.id.text_trending_services);
        text_trending_services3 = (TextView) view.findViewById(R.id.text_trending_services3);
        text_trending_services2 = (TextView) view.findViewById(R.id.text_trending_services2);
        text_trending_services1 = (TextView) view.findViewById(R.id.text_trending_services1);
        all_services = (TextView) view.findViewById(R.id.all_services);
        AppUtils.fontGotham_Book(text_trending_services, context);
        AppUtils.fontGotham_Book(text_used_services, context);
        AppUtils.fontGotham_Book(all_services, context);
        AppUtils.fontGotham_Button(btn_explore, context);
        recycler_banner = (RecyclerView) view.findViewById(R.id.recycler_banner);
        recycler_popular = (RecyclerView) view.findViewById(R.id.recycler_popular);
        recycler_trending_services3 = (RecyclerView) view.findViewById(R.id.recycler_trending_services3);
        recycler_trending_services2 = (RecyclerView) view.findViewById(R.id.recycler_trending_services2);
        recycler_trending_services1 = (RecyclerView) view.findViewById(R.id.recycler_trending_services1);
        recycler_used_services = (RecyclerView) view.findViewById(R.id.recycler_used_services);
        recycler_used_services.setNestedScrollingEnabled(false);
        recycler_popular.setNestedScrollingEnabled(false);
        recycler_banner.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recycler_used_services.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recycler_popular.setLayoutManager(new GridLayoutManager(context, 3));
        recycler_trending_services1.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recycler_trending_services2.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recycler_trending_services3.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        listPopularServices = new ArrayList<>();
        offerImages = new ArrayList<>();
        trending_list = new ArrayList<>();
        trending_list2 = new ArrayList<>();
        trending_list3 = new ArrayList<>();
        used_list = new ArrayList<>();
        setListener();
        setHomeData();

    }


    private void setHomeData() {

        String jsonData = AppUtils.getCategoryJsondata(context);
        try {
            JSONObject data = new JSONObject(jsonData);
            unidirectionalServices = data.getJSONArray("UnidirectionalServices").toString();
            JSONArray offers = data.getJSONArray("NewOffers");
            int length = offers.length();
            for (int i = 0; i < length; i++) {

                JSONObject jo = offers.getJSONObject(i);
                modelService = new ModelService();

                modelService.setServiceId(jo.getString("ServiceId"));
                modelService.setServiceName(jo.getString("ServiceName"));
                modelService.setImageUrl(jo.getString("ServiceIcon"));
                modelService.setDescription(jo.getString("ServiceDescription"));
                if (jo.has("IsUnidirectional")) {
                    modelService.setIs_uniDirectional(jo.getString("IsUnidirectional"));
                }
                offerImages.add(modelService);
            }

            adapterBanner = new AdapterBanner(context, this, offerImages);
            recycler_banner.setAdapter(adapterBanner);
/*==================================================*/
            JSONArray categories = data.getJSONArray("ReviewedServices");
            int length1 = categories.length();
            for (int i = 0; i < length1; i++) {

                JSONObject jo = categories.getJSONObject(i);
                modelService = new ModelService();

                modelService.setCategoryID(jo.getString("ServiceId"));
                modelService.setIs_uniDirectional(jo.getString("IsUnidirectional"));
                modelService.setCategoryBGImage(jo.getString("ServiceIcon"));
                modelService.setCategoryName(jo.getString("ServiceName"));
                modelService.setServiceOffer(jo.getString("ServiceOffer"));
                modelService.setServiceRatting(jo.getInt("ServiceRatting"));
                modelService.setDescription(jo.getString("ServiceDescription"));

                used_list.add(modelService);
            }
            adapterUsedServices = new AdapterUsedServices(getActivity(), this, used_list);
            recycler_used_services.setAdapter(adapterUsedServices);
/*==================================================*/
            JSONArray UnidirectionalTrendingCategories = data.getJSONArray("UnidirectionalTrendingCategories");

            JSONObject TrendingServicesFirstRow = UnidirectionalTrendingCategories.getJSONObject(0);
            text_trending_services1.setText(TrendingServicesFirstRow.getString("CategoryName"));
            JSONArray trending1 = TrendingServicesFirstRow.getJSONArray("UnidirectionalTrendingServices");
            int length2 = trending1.length();

            for (int i = 0; i < length2; i++) {

                JSONObject jo = trending1.getJSONObject(i);
                modelService = new ModelService();

                modelService.setCategoryID(jo.getString("ServiceId"));
                modelService.setIs_uniDirectional("1");
                modelService.setCategoryBGImage(jo.getString("ServiceIcon"));
                modelService.setCategoryName(jo.getString("ServiceName"));

                trending_list.add(modelService);

            }
            adapterTrendingServices = new AdapterTrendingServices(getActivity(), this, trending_list);
            recycler_trending_services1.setAdapter(adapterTrendingServices);


            JSONObject TrendingServicesSecondRow = UnidirectionalTrendingCategories.getJSONObject(1);
            text_trending_services2.setText(TrendingServicesSecondRow.getString("CategoryName"));
            JSONArray trending2 = TrendingServicesSecondRow.getJSONArray("UnidirectionalTrendingServices");

            int length4 = trending2.length();
            for (int i = 0; i < length4; i++) {

                JSONObject jo = trending2.getJSONObject(i);
                modelService = new ModelService();

                modelService.setCategoryID(jo.getString("ServiceId"));
                modelService.setIs_uniDirectional("1");
                modelService.setCategoryBGImage(jo.getString("ServiceIcon"));
                modelService.setCategoryName(jo.getString("ServiceName"));

                trending_list2.add(modelService);

            }
            adapterTrendingServices2 = new AdapterTrendingServices2(getActivity(), this, trending_list2);
            recycler_trending_services2.setAdapter(adapterTrendingServices2);
/*==================================================*/
            JSONObject TrendingServicesThirdRow = data.getJSONObject("BidirectionalTrendingCategory");
            text_trending_services3.setText(TrendingServicesThirdRow.getString("CategoryName"));
            JSONArray trending3 = TrendingServicesThirdRow.getJSONArray("BidirectionalTrendingServices");

            int length3 = trending3.length();
            for (int i = 0; i < length3; i++) {

                JSONObject jo = trending3.getJSONObject(i);
                modelService = new ModelService();

                modelService.setCategoryID(jo.getString("ServiceId"));
                modelService.setIs_uniDirectional("2");
                modelService.setCategoryBGImage(jo.getString("ServiceIcon"));
                modelService.setCategoryName(jo.getString("ServiceName"));

                trending_list3.add(modelService);

            }
            adapterTrendingServices3 = new AdapterTrendingServices3(getActivity(), this, trending_list3);
            recycler_trending_services3.setAdapter(adapterTrendingServices3);
/*==================================================*/

            JSONArray popular_services = data.getJSONArray("DashboardServices");

            if (popular_services.length() > 0) {

                int length5 = popular_services.length();
                for (int i = 0; i < length5; i++) {

                    JSONObject jo = popular_services.getJSONObject(i);
                    modelService = new ModelService();
                    modelService.setServiceId(jo.getString("ServiceId"));
                    modelService.setServiceName(jo.getString("Title"));
                    modelService.setDescription(jo.getString("Description"));
                    modelService.setServiceIcon(jo.getString("CategoryIcon"));
                    modelService.setIs_uniDirectional(jo.getString("IsUnidirectional"));
                    listPopularServices.add(modelService);
                }
                adapterPopularServices = new AdapterPopularServices(context, this, listPopularServices);
                recycler_popular.setAdapter(adapterPopularServices);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setListener() {

        btn_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, AllServicesPage.class);
                in.putExtra("services", unidirectionalServices);
                startActivity(in);
                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
            }
        });

        btn_refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String url = MyConstant.SHAREURL + AppUtils.getOtherData(context, MyConstant.UNIQUECODE);
                    Log.e("share_url", url);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 2) {
            Intent in = new Intent(context, ServicesCategory.class);
            in.putExtra("services", trending_list.get(position).getServicesArray());
            startActivity(in);
        } else if (flag == 12) {
            if (listPopularServices.get(position).getIs_uniDirectional().equalsIgnoreCase("1")) {
                Intent in = new Intent(context, ServicesCategory.class);
                in.putExtra("serviceId", listPopularServices.get(position).getServiceId());
                startActivity(in);
            }
        }
    }
}
