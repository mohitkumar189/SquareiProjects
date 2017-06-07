package com.app.justclap.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.app.justclap.activities.GalleryViewActivity;
import com.app.justclap.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.app.justclap.adapters.AdapterPortfolio;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelQuestionList;
import com.app.justclap.models.ModelService;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_Portfolio extends Fragment implements OnCustomItemClicListener {

    GridView list_portfolio;
    ModelService serviceDetail;
    Context context;
    AdapterPortfolio adapterPortfolio;
    ArrayList<ModelQuestionList> quesList;
    Bundle b;
    ArrayList<ModelService> imagelist;
    LinearLayout linear_empty_list;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        b = getArguments();
        imagelist = new ArrayList<>();
        quesList = new ArrayList<>();
        NestedScrollView scrollView = (NestedScrollView) view.findViewById(R.id.nest_scrollview1);
        scrollView.setFillViewport(true);
        setData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        View viewCategory = inflater.inflate(R.layout.fragment_portfolio, container, false);
        list_portfolio = (GridView) viewCategory.findViewById(R.id.list_portfolio);
        linear_empty_list = (LinearLayout) viewCategory.findViewById(R.id.linear_empty_list);


        return viewCategory;
    }


    public void setData() {

        if (b != null) {

            String data = b.getString("portfolio");

            try {

                JSONArray images = new JSONArray(data);

                for (int i = 0; i < images.length(); i++) {

                    JSONObject jo = images.getJSONObject(i);

                    serviceDetail = new ModelService();
                    serviceDetail.setPorfolioImage(context.getResources().getString(
                            R.string.img_url) + jo.getString("imageUrl"));
                    serviceDetail.setImageId(jo.getString("imageID"));
                    imagelist.add(serviceDetail);

                }

                adapterPortfolio = new AdapterPortfolio(context, this, imagelist);
                list_portfolio.setAdapter(adapterPortfolio);

                if (imagelist.size() > 0) {
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
        if (flag == 1) {
            String images = "";

            for (int i = 0; i < imagelist.size(); i++) {

                if (images.length() > 0) {
                    images = images + "," + imagelist.get(i).getPorfolioImage();
                } else {
                    images = imagelist.get(i).getPorfolioImage();
                }

            }

            Log.e("position", "*" + position);
            Intent intent = new Intent(getActivity(), GalleryViewActivity.class);
            intent.putExtra("posi", position);
            intent.putExtra("images", images);
            startActivity(intent);
        }
    }
}
