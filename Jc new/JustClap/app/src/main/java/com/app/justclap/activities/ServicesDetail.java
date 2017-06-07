package com.app.justclap.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ServicesDetail extends AppCompatActivity implements ApiResponse, OnCustomItemClicListener {

    private Toolbar toolbar;
    private Context context;
    private TextView text_title, text_reviews, text_gallery, text_video, welcome_text;
    private ImageView[] dots;
    private int dotsCount;
    private LinearLayout pager_indicator;
    private ViewPager pager_banner;
    private RecyclerView recyler_reviews, recyler_users, recyler_video, recyler_gallery;
    private CustomPagerAdapter mCustomPagerAdapter;
    private ArrayList<String> mResources;
    private String serviceId = "";
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
    private RelativeLayout rl_welcome;
    private ImageView welcome_image;
    private String ServiceQueries = "",isUnidirectional="1";
    private Button btn_start;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_detail);

        context = this;
        init();
        serviceId = getIntent().getStringExtra("serviceId");
        getData();


        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ServiceQueries.equalsIgnoreCase("")) {
                    Intent in = new Intent(context, SubmitServiceQuestions.class);
                    in.putExtra("servicename", text_title.getText().toString());
                    in.putExtra("serviceid", serviceId);
                    in.putExtra("queryArray", ServiceQueries);
                    in.putExtra("isUniDirectional", isUnidirectional);

                    startActivity(in);
                } else {

                    Toast.makeText(context, "Something went wrong, try again later", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*
     * initialize all views
     */
    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_start = (Button) findViewById(R.id.btn_start);

        mResources = new ArrayList<>();
        listGallery = new ArrayList<>();
        listReview = new ArrayList<>();
        listUserlist = new ArrayList<>();
        listVideo = new ArrayList<>();
        pager_banner = (ViewPager) findViewById(R.id.pager_banner);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        linear_video = (LinearLayout) findViewById(R.id.linear_video);
        linear_gallery = (LinearLayout) findViewById(R.id.linear_gallery);

        text_title = (TextView) findViewById(R.id.text_title);
        text_video = (TextView) findViewById(R.id.text_video);
        text_gallery = (TextView) findViewById(R.id.text_gallery);
        text_reviews = (TextView) findViewById(R.id.text_reviews);
        welcome_text = (TextView) findViewById(R.id.welcome_text);
        welcome_image = (ImageView) findViewById(R.id.welcome_image);
        rl_welcome = (RelativeLayout) findViewById(R.id.rl_welcome);
        AppUtils.fontGotham_Medium(text_title, context);
        AppUtils.fontGotham_Medium(text_video, context);
        AppUtils.fontGotham_Medium(text_reviews, context);
        AppUtils.fontGotham_Medium(text_gallery, context);

        recyler_reviews = (RecyclerView) findViewById(R.id.recyler_reviews);
        recyler_reviews.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        recyler_gallery = (RecyclerView) findViewById(R.id.recyler_gallery);
        recyler_gallery.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        recyler_video = (RecyclerView) findViewById(R.id.recyler_video);
        recyler_video.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        recyler_users = (RecyclerView) findViewById(R.id.recyler_users);
        recyler_users.setLayoutManager(new GridLayoutManager(context, 3));


    }

    private void getData() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                HashMap<String, String> hm = new HashMap<>();
                hm.put("isUnidirectional", "1");
                hm.put("serviceId", serviceId);
                String url = getResources().getString(R.string.url_base_new) + getResources().getString(R.string.getServiceDetails);
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

        pager_banner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                try {

                    if (position >= dotsCount) {
                        position = (position % dotsCount);
                    }
                    Log.e("position", "*" + position);

                    for (int i = 0; i < dotsCount; i++) {
                        dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
                    }

                    dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void setUiPageViewController() {

        dotsCount = mCustomPagerAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(getApplicationContext());
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            //   params.gravity = Gravity.RIGHT;

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }


    @Override
    public void onPostSuccess(int method, JSONObject response) {
        try {
            if (method == 1) {

                JSONObject commandResult = response
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject maindata = commandResult.getJSONObject("data");
                    JSONObject serviceDetails = maindata.getJSONObject("ServiceDetails");

                    ServiceQueries = serviceDetails.getJSONArray("ServiceQueries").toString();
                    text_title.setText(serviceDetails.getString("Title"));

                              /*===========welcome image=============*/
                    JSONObject welcomeImage = serviceDetails.getJSONObject("WelcomeImage");

                    if (welcomeImage.has("ImageText")) {
                        welcome_text.setText(welcomeImage.getString("ImageText"));
                        if (!welcomeImage.getString("ImageUrl").equalsIgnoreCase("")) {
                            Picasso.with(context).load(welcomeImage.getString("ImageUrl")).into(welcome_image);
                        }
                    } else {
                        rl_welcome.setVisibility(View.GONE);
                    }

                    /*===========slider image=============*/
                    JSONArray ServiceSliderImages = serviceDetails.getJSONArray("ServiceSliderImages");

                    for (int i = 0; i < ServiceSliderImages.length(); i++) {

                        JSONObject jo = ServiceSliderImages.getJSONObject(i);

                        mResources.add(jo.getString("ImageUrl"));

                    }

                    mCustomPagerAdapter = new CustomPagerAdapter(this);
                    pager_banner.setAdapter(mCustomPagerAdapter);
                    pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
                    setUiPageViewController();
                    pager_banner.setCurrentItem(0);

                    setListener();

                      /*===========Reviews=============*/
                    JSONArray ServiceReviews = serviceDetails.getJSONArray("ServiceReviews");
                    for (int i = 0; i < ServiceReviews.length(); i++) {

                        JSONObject jo = ServiceReviews.getJSONObject(i);

                        modelService = new ModelService();
                        modelService.setReviewId(jo.getString("ReviewId"));
                        modelService.setReviewSubject(jo.getString("ReviewSubject"));
                        modelService.setReviewText(jo.getString("ReviewText"));
                        modelService.setCreatedOn(jo.getString("CreatedOn"));
                        modelService.setUserId(jo.getString("UserId"));
                        modelService.setUserName(jo.getString("UserName"));
                        modelService.setUserEmail(jo.getString("UserEmail"));

                        listReview.add(modelService);
                    }
                    adapterReviews = new AdapterReviews(context, this, listReview);
                    recyler_reviews.setAdapter(adapterReviews);

                    if (ServiceReviews.length() == 0) {
                        recyler_reviews.setVisibility(View.GONE);
                        text_reviews.setVisibility(View.GONE);
                    }
                    /*===========Gallery=============*/
                    JSONArray ServiceGalleryImages = serviceDetails.getJSONArray("ServiceGalleryImages");
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
                    JSONArray ServiceVideos = serviceDetails.getJSONArray("ServiceVideos");
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
                      /*===========Users list=============*/
                    JSONArray ServiceUsers = serviceDetails.getJSONArray("ServiceUsers");
                    for (int i = 0; i < ServiceUsers.length(); i++) {

                        JSONObject jo = ServiceUsers.getJSONObject(i);

                        modelService = new ModelService();
                        modelService.setId(jo.getString("Id"));
                        modelService.setUserName(jo.getString("Name"));
                        modelService.setUserEmail(jo.getString("Email"));
                        modelService.setImageUrl(jo.getString("ImageUrl"));

                        listUserlist.add(modelService);
                    }
                    adapterUsersList = new AdapterUsersList(context, this, listUserlist);
                    recyler_users.setAdapter(adapterUsersList);

                    if (ServiceUsers.length() == 0) {
                        recyler_users.setVisibility(View.GONE);
                    }


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

    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mResources.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item,
                    container, false);

            ImageView imageView = (ImageView) itemView
                    .findViewById(R.id.imageView);
            //  imageView.setImageResource(mResources.get(position));
            Picasso.with(mContext).load(mResources.get(position)).into(imageView);
            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }


}
