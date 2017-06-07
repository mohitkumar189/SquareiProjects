package com.app.justclap.fragment;


import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ExpandableListView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.justclap.adapters.AdapterDashBoardService;
import com.app.justclap.adapters.AdapterServiceList;
import com.app.justclap.fragment_types.FragmentShareRideType;
import com.app.justclap.activities.QuestionPage;
import com.app.justclap.R;
import com.app.justclap.activities.SearchActivity;
import com.app.justclap.activities.ServiceSubMenu;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.app.justclap.adapters.AdapterExpandableDashBoardService;
import com.app.justclap.adapters.AdapterExpandableServiceList;

import com.app.justclap.asyntask.AsyncPostDataFragmentNoProgress;
import com.app.justclap.customview.ExpandableHeightGridView;
import com.app.justclap.customview.ExpandableHeightListView;
import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.app.justclap.utils.AppUtils;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;


public class FragmentDashboardServices extends Fragment implements OnCustomItemClicListener, ListenerPostData, TimePickerDialog.OnTimeSetListener {


    RecyclerView mRecyclerView;
    AdapterDashBoardService adapterDashBoardService;
    ModelService serviceDetail;
    ArrayList<ModelService> service_list;
    ArrayList<ModelService> popular_service_list;
    RecyclerView list_servicesGrid;
    AdapterServiceList adapterServiceList;
    //  Spinner sp_country_list;
    ConnectionDetector cd;
    ArrayList<ModelService> offerImagesBackup;
    Handler handler;
    Context context;
    ScrollView scrollview;
    JSONArray offers;
    Runnable runabble;
    RelativeLayout rl_main_layout, rl_network;
    TextView text_search;
    String[] countries = {"Delhi NCR"};
    ArrayList<String> citiesList = new ArrayList<>();
    String jsonData = "", cityid = "";
    private LinearLayout scroll;
    ArrayList<ModelService> cityList;
    ModelService dataListImages;
    ArrayList<ModelService> OfferImages;
    SwipeRefreshLayout mSwipeRefreshLayout;
    FloatingActionButton floatingIcon, searchfloatingIcon;
    RelativeLayout rl_pager;
    int selectListView = 0;
    HorizontalScrollView hv;
    boolean isHvScrolled = false;
    boolean isfabAnimated = true;
    // CollapsingToolbarLayout collapsingToolbar;
    TimePickerDialog timePickerDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        View view_service = inflater.inflate(R.layout.fragment_dashboard_servicesupdated, container, false);

        context = getActivity();
        cityList = new ArrayList<>();
        OfferImages = new ArrayList<>();
        offerImagesBackup = new ArrayList<>();
        popular_service_list = new ArrayList<>();
        scroll = (LinearLayout) view_service.findViewById(R.id.ll_scrol);
        service_list = new ArrayList<>();
       /* collapsingToolbar = (CollapsingToolbarLayout) view_service.findViewById(
                R.id.collapse_toolbar);
        collapsingToolbar.setTitleEnabled(false);
        NestedScrollView scrollView = (NestedScrollView) view_service.findViewById(R.id.nest_scrollview);
        scrollView.setFillViewport(true);
        scrollView.setSmoothScrollingEnabled(true);*/
        hv = (HorizontalScrollView) view_service.findViewById(R.id.myHsView);

        rl_pager = (RelativeLayout) view_service.findViewById(R.id.rl_pager);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view_service.findViewById(R.id.swipeRefreshLayout);
        scrollview = (ScrollView) view_service.findViewById(R.id.scrollview);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        //mIndicator = (CircleIndicator) view_service.findViewById(R.id.indicator);
        rl_main_layout = (RelativeLayout) view_service.findViewById(R.id.rl_main_layout);
        rl_network = (RelativeLayout) view_service.findViewById(R.id.rl_network);
        //   mSwipeRefreshLayout1 = (SwipeRefreshLayout) view_service.findViewById(R.id.swipeRefreshLayout1);
        //  mSwipeRefreshLayout1.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        floatingIcon = (FloatingActionButton) view_service.findViewById(R.id.floatingIcon);
        searchfloatingIcon = (FloatingActionButton) view_service.findViewById(R.id.searchfloatingIcon);
        mRecyclerView = (RecyclerView) view_service.findViewById(R.id.recycler_view1);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //   layoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);

        //  mRecyclerView.setExpanded(true);
        mRecyclerView.setFocusable(false);
    /*    mRecyclerView.setDivider(getResources().getDrawable(R.color.text_white));
        mRecyclerView.setDividerHeight(0);*/

        //  sp_country_list = (Spinner) view_service.findViewById(R.id.spinner_county);
        //   sp_country_list.setOnItemSelectedListener(this);
        text_search = (TextView) view_service.findViewById(R.id.text_search);
        list_servicesGrid = (RecyclerView) view_service.findViewById(R.id.list_servicesGrid);
        //  list_servicesGrid.setExpanded(true);

        GridLayoutManager gridlayoutManager = new GridLayoutManager(context, 2);
        gridlayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        //   gridlayoutManager.setAutoMeasureEnabled(true);
        list_servicesGrid.setLayoutManager(gridlayoutManager);
        //   list_servicesGrid.setHasFixedSize(true);
        list_servicesGrid.setNestedScrollingEnabled(false);
        list_servicesGrid.setLayoutManager(gridlayoutManager);
        floatingIcon.setImageResource(R.drawable.grid_icon);

        mRecyclerView.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        list_servicesGrid.setVisibility(View.GONE);
        parseCategoryData();
        setListener();
        searchfloatingIcon.setVisibility(View.GONE);
        floatingIcon.setVisibility(View.VISIBLE);

        return view_service;
    }


    private void setListener() {

        hv.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("onTouch", "onTouch");
                isHvScrolled = false;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isHvScrolled = true;
                    Log.e("onTouch", "MotionEvent.ACTION_DOWN");
                }

                return false;
            }
        });

        scrollview.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                isHvScrolled = true;
                return false;
            }
        });


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshItems();

            }
        });

        text_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* timePickerDialog = TimePickerDialog.newInstance(FragmentDashboardServices.this, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true);

                timePickerDialog.show(getActivity().getFragmentManager(), "Material Design TimePicker Dialog");
*/
                Intent in = new Intent(getActivity(), SearchActivity.class);
                startActivity(in);
            }
        });


        searchfloatingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getActivity(), SearchActivity.class);
                startActivity(in);

            }
        });

        scrollview.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                int scrollY = scrollview.getScrollY();
                //   Log.e("scrollvalue", "*" + scrollY);

                if (scrollY == 0 && isHvScrolled) {

                    searchfloatingIcon.setVisibility(View.GONE);
                    isfabAnimated = true;
                    if (!mSwipeRefreshLayout.isRefreshing())
                        mSwipeRefreshLayout.setEnabled(true);
                } else {
                    if (scrollY > 100) {
                        if (isfabAnimated) {

                            Animation anim = AnimationUtils.loadAnimation(context, R.anim.fadein);
                            anim.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                    searchfloatingIcon.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            searchfloatingIcon.startAnimation(anim);
                            isfabAnimated = false;
                        }


                    } else {
                        if (!isfabAnimated) {
                            Animation anim = AnimationUtils.loadAnimation(context, R.anim.fadeout);
                            anim.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    searchfloatingIcon.setVisibility(View.GONE);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            searchfloatingIcon.startAnimation(anim);

                        }
                        isfabAnimated = true;
                        searchfloatingIcon.setVisibility(View.GONE);
                    }
                    if (scrollY > 0) {
                        mSwipeRefreshLayout.setEnabled(false);
                    }
                }

            }
        });

        floatingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                if (selectListView == 1) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.e("dgdfgds", "clicked" + selectListView);

                            mRecyclerView.setVisibility(View.VISIBLE);
                            list_servicesGrid.setVisibility(View.GONE);
                            floatingIcon.setImageResource(R.drawable.grid_icon);
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            selectListView = 0;

                        }
                    });

                } else if (selectListView == 0) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            list_servicesGrid.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            floatingIcon.setImageResource(R.drawable.list_icon);
                            selectListView = 1;

                        }
                    });
                    Log.e("cccccccc", "clicked" + selectListView);


                }
                    /* else if (selectListView == 2) {
                        floatingIcon.setImageResource(R.drawable.list_icon);
                        selectListView = 0;
                    }*/

            }
        });


    }


    private void addSubCategory(final int position) {
        final ModelService list;
        list = OfferImages.get(position);
        final ImageView image;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_pager_adapter, null);
        view.setTag(position);
        image = (ImageView) view.findViewById(R.id.images_flipper);
        image.setTag(position);

        try {

            Picasso.with(context)
                    .load(list.getOfferImage())
                    .placeholder(R.drawable.default_product)
                    .into(image);

        } catch (Exception e) {

        }


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("On viewclicked", "offer" + OfferImages.get(Integer.parseInt(image.getTag().toString())).getOffername());
                Intent in = new Intent(getActivity(), QuestionPage.class);
                in.putExtra("servicename", OfferImages.get(Integer.parseInt(image.getTag().toString())).getOffername());
                in.putExtra("serviceid", OfferImages.get(Integer.parseInt(image.getTag().toString())).getOfferId());
                in.putExtra("Is_uniDirectional", OfferImages.get(Integer.parseInt(image.getTag().toString())).getIs_uniDirectional());
                startActivity(in);

                //Toast.makeText(context, "ID "+subCatlist.get(Integer.parseInt(view.getTag().toString())).get("id"), Toast.LENGTH_LONG).show();
            }
        });

        scroll.addView(view);
    }


    void refreshItems() {
        getServicelist();
        mSwipeRefreshLayout.setRefreshing(true);
        //  mSwipeRefreshLayout1.setRefreshing(true);
    }

/*
    private void setUiPageViewController() {

        dotsCount = customPagerAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(getActivity());
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
*/

    private void parseCategoryData() {


        jsonData = AppUtils.getCategoryJsondata(context);

        try {

            JSONObject data = new JSONObject(jsonData);

            offers = data.getJSONArray("newoffer");

            if (offers.length() > 0) {

                for (int i = 0; i < offers.length(); i++) {

                    JSONObject jo = offers.getJSONObject(i);
                    dataListImages = new ModelService();

                    dataListImages.setOfferId(jo.getString("serviceId"));
                    dataListImages.setOffername(jo.getString("serviceName"));
                    dataListImages.setOfferImage(getResources().getString(R.string.img_url) + jo.getString("imageUrl"));
                    if (jo.has("is_uniDirectional")) {
                        dataListImages.setIs_uniDirectional(jo.getString("is_uniDirectional"));
                    }
                    OfferImages.add(dataListImages);
                    // offerImagesBackup.add(dataListImages);
                    addSubCategory(i);
                }

                rl_pager.setVisibility(View.VISIBLE);

            } else {

                rl_pager.setVisibility(View.GONE);


            }

            JSONArray categories = data.getJSONArray("categories");

            for (int i = 0; i < categories.length(); i++) {

                JSONObject jo = categories.getJSONObject(i);
                serviceDetail = new ModelService();
                serviceDetail.setCategoryID(jo.getString("categoryID"));
                if (jo.has("is_uniDirectional")) {

                    serviceDetail.setIs_uniDirectional(jo.getString("is_uniDirectional"));
                }
                //  serviceDetail.setCategorycityName(jo.getString("cityName"));
                serviceDetail.setCategoryBGImage(jo.getString("categoryBGImage"));
                serviceDetail.setServicesCount(jo.getString("servicesCount"));
                serviceDetail.setCategoryName(jo.getString("categoryName"));
                serviceDetail.setIsCategoryShown(jo.getString("is_naukri"));

                if (jo.has("services")) {
                    serviceDetail.setServicesArray(jo.getString("services"));
                }
                service_list.add(serviceDetail);

            }
            adapterDashBoardService = new AdapterDashBoardService(getActivity(), this, service_list);
            mRecyclerView.setAdapter(adapterDashBoardService);

            adapterServiceList = new AdapterServiceList(getActivity(), this, service_list);
            list_servicesGrid.setAdapter(adapterServiceList);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*
    private void pagerMove() {
        try {

            handler = new Handler();
            runabble = new Runnable() {
                @Override
                public void run() {

                    int countPager = 0;
                    int page = pager1.getCurrentItem();
                    if (customPagerAdapter != null && customPagerAdapter.getCount() > 0) {
                        countPager = customPagerAdapter.getCount();
                    }
                    //  Toast.makeText(getApplicationContext(),"handler",Toast.LENGTH_SHORT).show();
                    page++;

                    if (page >= countPager) {

                        //   mIndicator.setCurrentItem(0);
                        for (int i = 0; i < offerImagesBackup.size(); i++) {
                            OfferImages.add(offerImagesBackup.get(i));
                        }
                        //  dotsCount = customPagerAdapter.getCount() % 2;

                        customPagerAdapter.notifyDataSetChanged();
                    }
                    //countPager--;
                    pager1.setCurrentItem(page, true);
                    pagerMove();
                    //  handler.postDelayed(this, 4000);

                }
            };
            handler.postDelayed(runabble, 4000);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onStop() {
        super.onStop();

        try {
            if (handler != null) {
                handler.removeCallbacks(runabble);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getServicelist() {

        try {

            // for gcm condition check
            // =================================================

            cd = new ConnectionDetector(context);

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present
                rl_main_layout.setVisibility(View.GONE);
                rl_network.setVisibility(View.VISIBLE);

                //  AppUtils.showCustomAlert(getActivity(), "Internet Connection Error, Please connect to working Internet connection");

                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);
/*
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "cityID", cityid));*/

                new AsyncPostDataFragmentNoProgress(getActivity(), FragmentDashboardServices.this, 1, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.homeData));

            }
        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    @Override
    public void onItemClickListener(int position, int flag) {

        try {

            if (flag == 1) {

                if (service_list.get(position).getIs_uniDirectional().equalsIgnoreCase("1")) {

                    Intent in = new Intent(getActivity(), ServiceSubMenu.class);
                    in.putExtra("servicearray", service_list.get(position).getServicesArray());
                    in.putExtra("categoryName", service_list.get(position).getCategoryName());

                    startActivity(in);
                } else if (service_list.get(position).getIs_uniDirectional().equalsIgnoreCase("2")) {

                    Intent in = new Intent(getActivity(), QuestionPage.class);
                    in.putExtra("serviceid", service_list.get(position).getCategoryID());
                    in.putExtra("servicename", service_list.get(position).getCategoryName());
                    in.putExtra("Is_uniDirectional", service_list.get(position).getIs_uniDirectional());
                    startActivity(in);

                }

            } else if (flag == 21) {

                Intent in = new Intent(getActivity(), QuestionPage.class);
                in.putExtra("servicename", popular_service_list.get(position).getServiceName());
                in.putExtra("serviceid", popular_service_list.get(position).getServiceID());
                in.putExtra("Is_uniDirectional", "1");
                startActivity(in);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostRequestSucess(int position, String response) {

        try {

            if (position == 1) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject maindata = commandResult.getJSONObject("data");

                    // JSONArray cities1 = maindata.getJSONArray("cities");
                    JSONArray categories1 = maindata.getJSONArray("categories");

                    AppUtils.setcategories(context, categories1.toString());

                    //   AppUtils.setcities(context, cities1.toString());

                    AppUtils.setCategoryJsondata(context, maindata.toString());

                    JSONObject data = commandResult.getJSONObject("data");

                    offers = data.getJSONArray("newoffer");
                    scroll.removeAllViews();
                    OfferImages.removeAll(OfferImages);
                    // offerImagesBackup.removeAll(offerImagesBackup);
                    if (offers.length() > 0) {

                        for (int i = 0; i < offers.length(); i++) {

                            JSONObject jo = offers.getJSONObject(i);
                            dataListImages = new ModelService();

                            dataListImages.setOfferId(jo.getString("serviceId"));
                            dataListImages.setOffername(jo.getString("serviceName"));
                            dataListImages.setOfferImage(getResources().getString(R.string.img_url) + jo.getString("imageUrl"));
                            if (jo.has("is_uniDirectional")) {
                                dataListImages.setIs_uniDirectional(jo.getString("is_uniDirectional"));
                            }
                            OfferImages.add(dataListImages);
                            addSubCategory(i);
                        }

                        //  customPagerAdapter = new CustomPagerAdapter(context, this, OfferImages);

                        rl_pager.setVisibility(View.VISIBLE);
                        //mIndicator.setViewPager(pager1);
                    } else {

                        rl_pager.setVisibility(View.GONE);

                    }

                    JSONArray categories = data.getJSONArray("categories");

                    service_list.removeAll(service_list);
                    for (int i = 0; i < categories.length(); i++) {

                        JSONObject jo = categories.getJSONObject(i);
                        serviceDetail = new ModelService();
                        serviceDetail.setCategoryID(jo.getString("categoryID"));
                        if (jo.has("is_uniDirectional")) {

                            serviceDetail.setIs_uniDirectional(jo.getString("is_uniDirectional"));
                        }
                        //  serviceDetail.setCategorycityName(jo.getString("cityName"));
                        serviceDetail.setCategoryBGImage(jo.getString("categoryBGImage"));
                        serviceDetail.setCategoryName(jo.getString("categoryName"));
                        serviceDetail.setServicesCount(jo.getString("servicesCount"));
                        serviceDetail.setIsCategoryShown(jo.getString("is_naukri"));
                        if (jo.has("services")) {
                            serviceDetail.setServicesArray(jo.getString("services"));
                        }
                        service_list.add(serviceDetail);

                    }
                    adapterDashBoardService = new AdapterDashBoardService(getActivity(), this, service_list);
                    mRecyclerView.setAdapter(adapterDashBoardService);
                    adapterServiceList = new AdapterServiceList(getActivity(), this, service_list);
                    list_servicesGrid.setAdapter(adapterServiceList);
                    mSwipeRefreshLayout.setRefreshing(false);
                    //    mSwipeRefreshLayout1.setRefreshing(false);

                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);


                } else {

                    mSwipeRefreshLayout.setRefreshing(false);
                    //   mSwipeRefreshLayout1.setRefreshing(false);

                    AppUtils.showCustomAlert(getActivity(), commandResult.getString("message")
                    );

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostRequestFailed(int position, String response) {

        AppUtils.showCustomAlert(getActivity(), getResources().getString(R.string.problem_server));
    }


    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

    }
}
