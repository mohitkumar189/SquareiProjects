package com.app.justclap.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.R;
import com.app.justclap.adapter.AdapterGallery;
import com.app.justclap.adapter.AdapterReviews;
import com.app.justclap.adapter.AdapterUsersList;
import com.app.justclap.adapter.AdapterVideo;
import com.app.justclap.aynctask.CommonAsyncTaskHashmap;
import com.app.justclap.interfaces.ApiResponse;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.model.ModelService;
import com.app.justclap.utils.AppUtils;
import com.google.android.gms.maps.GoogleMap;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class VendorDetail extends AppCompatActivity implements ApiResponse, OnCustomItemClicListener {

    private Toolbar toolbar;
    private Context context;
    private TextView text_title, text_businesstype, text_gallery, text_video, text_followers;
    private RecyclerView recyler_video, recyler_gallery;
    private String vendorId = "";
    private AdapterGallery adapterGallery;
    private AdapterVideo adapterVideo;
    private AdapterUsersList adapterUsersList;
    private AdapterReviews adapterReviews;
    private ArrayList<ModelService> listGallery;
    private ArrayList<ModelService> listVideo;
    private ArrayList<ModelService> listUserlist;
    private ArrayList<ModelService> listReview;
    private ModelService modelService;
    private LinearLayout linear_gallery, linear_video;
    private TextView text_username, btn_follow, text_userdetail, text_quote, text_servicesname, text_followersdetail, text_hired;
    private RatingBar ratingbar;
    private GoogleMap map;
    private ImageView image_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_detail);

        context = this;
        init();
        vendorId = getIntent().getStringExtra("vendorId");
        getData();
        setListener();
    }

    /*
     * initialize all views
     */
    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listGallery = new ArrayList<>();
        listReview = new ArrayList<>();
        listUserlist = new ArrayList<>();
        listVideo = new ArrayList<>();
        linear_video = (LinearLayout) findViewById(R.id.linear_video);
        linear_gallery = (LinearLayout) findViewById(R.id.linear_gallery);
        text_title = (TextView) findViewById(R.id.text_title);
        text_video = (TextView) findViewById(R.id.text_video);
        text_gallery = (TextView) findViewById(R.id.text_gallery);
        text_businesstype = (TextView) findViewById(R.id.text_businesstype);
        text_followers = (TextView) findViewById(R.id.text_followers);
        text_username = (TextView) findViewById(R.id.text_username);
        text_userdetail = (TextView) findViewById(R.id.text_userdetail);
        text_quote = (TextView) findViewById(R.id.text_quote);
        text_servicesname = (TextView) findViewById(R.id.text_servicesname);
        text_followersdetail = (TextView) findViewById(R.id.text_followersdetail);
        text_hired = (TextView) findViewById(R.id.text_hired);
        ratingbar = (RatingBar) findViewById(R.id.ratingbar);
        image_user = (ImageView) findViewById(R.id.image_user);
        btn_follow = (TextView) findViewById(R.id.btn_follow);

        AppUtils.fontGotham_Medium(text_followers, context);
        AppUtils.fontGotham_Medium(text_username, context);
        AppUtils.fontGotham_Book(text_userdetail, context);
        AppUtils.fontGotham_Medium(text_quote, context);
        AppUtils.fontGotham_Book(text_servicesname, context);
        AppUtils.fontGotham_Book(text_followersdetail, context);

        AppUtils.fontGotham_Medium(text_title, context);
        AppUtils.fontGotham_Medium(text_video, context);
        AppUtils.fontGotham_Medium(text_businesstype, context);
        AppUtils.fontGotham_Medium(text_gallery, context);
        text_title.setText(getIntent().getStringExtra("title"));

        recyler_gallery = (RecyclerView) findViewById(R.id.recyler_gallery);
        recyler_gallery.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        recyler_video = (RecyclerView) findViewById(R.id.recyler_video);
        recyler_video.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    }

    private void getData() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("vendorId", vendorId);
                String url = getResources().getString(R.string.url_base_new) + getResources().getString(R.string.viewVendorProfileForHiring);
                new CommonAsyncTaskHashmap(1, context, this).getqueryJson(url, hm);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
  * manage click listener of all views
  */
    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        text_hired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hireVendor();
            }
        });
    }

    private void hireVendor() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                HashMap<String, String> hm = new HashMap<>();

                hm.put("quoteId", vendorId);
                hm.put("userId", AppUtils.getUserId(context));

                String url = getResources().getString(R.string.url_base_new) + getResources().getString(R.string.hireVendor);
                new CommonAsyncTaskHashmap(1, context, this).getqueryJson(url, hm);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                    JSONObject serviceDetails = maindata.getJSONObject("VendorInfo");

                    text_username.setText(serviceDetails.getString("Name"));
                    text_userdetail.setText(serviceDetails.getString("BusinessDescription"));
                    ratingbar.setRating(Float.parseFloat(serviceDetails.getString("Ratting")));
                    text_followersdetail.setText(serviceDetails.getString("FollowersCount"));
                    serviceDetails.getString("HappyClientsCount");
                    serviceDetails.getString("Latitude");
                    serviceDetails.getString("Longitude");
                  /*  "":"Meenakshi",
                            "Email":"meenundls@gmail.com",
                            "Mobile":"7532087889",
                            "Gender":"Male",
                            "Latitude":"28.4796850",
                            "Longitude":"77.3122400",
                            "Description":"",
                            "BusinessDistance":"300",
                            "BusinessDescription":"",*/
                    JSONArray services = serviceDetails.getJSONArray("Services");
                    for (int i = 0; i < services.length(); i++) {

                        JSONObject jo = services.getJSONObject(i);
                        if (text_servicesname.getText().toString().equalsIgnoreCase("")) {
                            text_servicesname.setText(jo.getString("ServiceName"));
                        } else {
                            text_servicesname.setText(text_servicesname.getText() + "\n" + "\n" + jo.getString("ServiceName"));
                        }
                    }

                    if (!serviceDetails.getString("ProfileImage").equalsIgnoreCase("")) {
                        Picasso.with(context).load(serviceDetails.getString("ImageUrl")).into(image_user);
                    }


                    /*===========Gallery=============*/
                    JSONArray ServiceGalleryImages = serviceDetails.getJSONArray("GalleryImages");
                    for (int i = 0; i < ServiceGalleryImages.length(); i++) {

                        JSONObject jo = ServiceGalleryImages.getJSONObject(i);

                        modelService = new ModelService();
                        modelService.setImageUrl(jo.getString("ImageUrl"));

                        listGallery.add(modelService);
                    }
                    adapterGallery = new AdapterGallery(context, this, listGallery);
                    recyler_gallery.setAdapter(adapterGallery);

                    if (ServiceGalleryImages.length() == 0) {
                        linear_gallery.setVisibility(View.GONE);
                    }
                         /*===========Videos=============*/
                    JSONArray ServiceVideos = serviceDetails.getJSONArray("GalleryVideos");
                    for (int i = 0; i < ServiceVideos.length(); i++) {

                        JSONObject jo = ServiceVideos.getJSONObject(i);

                        modelService = new ModelService();
                        modelService.setVideoId(jo.getString("VideoId"));
                        modelService.setVideoUrl(jo.getString("VideoUrl"));
                        modelService.setVideoThumbUrl(jo.getString("VideoThumbUrl"));

                        listVideo.add(modelService);
                    }
                    adapterVideo = new AdapterVideo(context, this, listVideo);
                    recyler_video.setAdapter(adapterVideo);

                    if (ServiceVideos.length() == 0) {
                        linear_video.setVisibility(View.GONE);
                    }
                    setListener();
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

    @Override
    public void onItemClickListener(int position, int flag) {

    }


}
