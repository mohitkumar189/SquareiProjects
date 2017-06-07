package com.app.justclap.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.justclap.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.app.justclap.adapters.AdapterReviews;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_Reviews extends Fragment implements OnCustomItemClicListener {

    RecyclerView list_reviews;
    ModelService serviceDetail;
    Context context;
    AdapterReviews adapterReviews;
    ArrayList<ModelService> reviewList;
    Bundle b;
    LinearLayoutManager layoutManager;
    LinearLayout linear_empty_list;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        b = getArguments();
        reviewList = new ArrayList<>();
/*
        NestedScrollView scrollView = (NestedScrollView) view.findViewById(R.id.nest_scrollview1);
        scrollView.setFillViewport(true);*/
        setData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        View viewCategory = inflater.inflate(R.layout.fragment_reviews, container, false);
        list_reviews = (RecyclerView) viewCategory.findViewById(R.id.list_reviews);
        linear_empty_list = (LinearLayout) viewCategory.findViewById(R.id.linear_empty_list);
        layoutManager = new LinearLayoutManager(getActivity());
        list_reviews.setLayoutManager(layoutManager);


        return viewCategory;
    }

    public void setData() {

        if (b != null) {

            String data = b.getString("review");

            try {


                JSONArray images = new JSONArray(data);

                for (int i = 0; i < images.length(); i++) {

                    JSONObject jo = images.getJSONObject(i);

                    serviceDetail = new ModelService();
                    serviceDetail.setReviewimage(context.getResources().getString(
                            R.string.img_url) + jo.getString("userImage"));
                    serviceDetail.setReview(jo.getString("reviewComment"));
                    serviceDetail.setReviewName(jo.getString("userName"));
                    serviceDetail.setReviewDate(jo.getString("reviewDate"));
                    serviceDetail.setRating(jo.getString("rating"));
                    reviewList.add(serviceDetail);

                }

                adapterReviews = new AdapterReviews(context, this, reviewList);
                list_reviews.setAdapter(adapterReviews);

                if (reviewList.size() > 0) {
                    linear_empty_list.setVisibility(View.GONE);
                } else {
                    linear_empty_list.setVisibility(View.VISIBLE);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }


    @Override
    public void onItemClickListener(int position, int flag) {

    }
}
