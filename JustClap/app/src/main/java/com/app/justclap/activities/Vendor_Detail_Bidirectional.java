package com.app.justclap.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.adapters.VendoDetailPagerAdapter;
import com.app.justclap.asyntask.AsyncPostDataResponse;
import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 12-02-2016.
 */
public class Vendor_Detail_Bidirectional extends AppCompatActivity implements OnCustomItemClicListener, ListenerPostData {


    Context context;
    ImageView image_background;
    VendoDetailPagerAdapter vendoDetailPagerAdapter;
    TextView text_vendor_name, text_review;
    RelativeLayout rl_message, rl_call;
    ViewPager pager;
    private TabLayout tabLayout;
    LinearLayout ll_call;
    Toolbar toolbar;
    RatingBar rating;
    RelativeLayout rl_main_layout, rl_network;
    ImageView image_profile;
    String vendor_id = "", service_id = "", vendorImage = "", is_paid = "", matchId = "", mobileNumber = "", searchId = "", flag = "", service_name = "";
    ConnectionDetector cd;
    ModelService detaildata;
    ArrayList<ModelService> arrayList;
    private BroadcastReceiver broadcastReceiver;
    TextView distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_detail_bidirectional);

        context = this;
        init();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Professional Profile");
        arrayList = new ArrayList<>();
        Intent in = getIntent();
        if (in.hasExtra("vendor_id")) {
            vendor_id = in.getExtras().getString("vendor_id");
            service_id = in.getExtras().getString("service_id");
            Log.e("vendor_id", vendor_id);
            Log.e("service_id", service_id);
        }
        if (in.hasExtra("service_name")) {
            service_name = in.getExtras().getString("service_name");
        }
        if (in.hasExtra("searchId")) {
            searchId = in.getExtras().getString("searchId");
        }
        if (in.hasExtra("matchId")) {
            matchId = in.getExtras().getString("matchId");
        }

      /*  if (in.hasExtra("flag")) {
            flag = in.getExtras().getString("flag");
            if (in.getExtras().getString("flag").equalsIgnoreCase("1")) {
                text_review.setVisibility(View.VISIBLE);
            } else if (in.getExtras().getString("flag").equalsIgnoreCase("2")) {
                text_review.setVisibility(View.GONE);
            } else
        }*/
       /* if (in.getExtras().getString("is_vendor").equalsIgnoreCase("1")){
            text_review.setVisibility(View.GONE);
        }else {
            text_review.setVisibility(View.VISIBLE);
        }*/

        setListener();

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        setupCollapsingToolbar();
        getVendorData();

    }

    private void setupCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(
                R.id.collapse_toolbar);


        collapsingToolbar.setTitleEnabled(false);
        //  collapsingToolbar.setTitle("");

       /* NestedScrollView scrollView = (NestedScrollView) findViewById (R.id.nest_scrollview);
        scrollView.setFillViewport(true);
*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }

    private void hireNow() {

        try {

            // for gcm condition check
            // =================================================

            cd = new ConnectionDetector(context);

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present


                rl_main_layout.setVisibility(View.GONE);
                rl_network.setVisibility(View.VISIBLE);

                // AppUtils.showCustomAlert(Vendor_Detail.this, getResources().getString(R.string.message_network_problem));

                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "userID", AppUtils.getUserId(context)));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "vendorID", vendor_id));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "serviceID", service_id));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "searchId", searchId));


                new AsyncPostDataResponse(Vendor_Detail_Bidirectional.this, 2, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.hireVendor));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void getVendorData() {

        try {

            // for gcm condition check
            // =================================================

            cd = new ConnectionDetector(context);

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present

                rl_main_layout.setVisibility(View.GONE);
                rl_network.setVisibility(View.VISIBLE);

                //  AppUtils.showCustomAlert(Vendor_Detail.this, getResources().getString(R.string.message_network_problem));

                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "vendorID", vendor_id));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "searchId", searchId));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "userID", AppUtils.getUserId(context)));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "matchId", matchId));


                new AsyncPostDataResponse(Vendor_Detail_Bidirectional.this, 1, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.vendorDetailPage));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void setupTabIcons() {

        tabLayout.getTabAt(0).setText("About");
        tabLayout.getTabAt(1).setText("Reviews");
        tabLayout.getTabAt(2).setText("Portfolio");
        tabLayout.setTabTextColors(getResources().getColor(R.color.txt_black),
                getResources().getColor(R.color.txt_orange));

    }


    private void init() {
        overridePendingTransition(R.anim.enter,
                R.anim.exit);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive", "Logout in progress");
                //At this point you should start the login activity and finish this one
                finish();
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        rl_main_layout = (RelativeLayout) findViewById(R.id.rl_main_layout);
        rl_network = (RelativeLayout) findViewById(R.id.rl_network);

        ll_call = (LinearLayout) findViewById(R.id.ll_2);
        pager = (ViewPager) findViewById(R.id.pager);
        rating = (RatingBar) findViewById(R.id.rating);
        rating.setEnabled(false);
        distance = (TextView) findViewById(R.id.distance);
        image_profile = (ImageView) findViewById(R.id.image_profile);
        text_vendor_name = (TextView) findViewById(R.id.text_vendor_name);
        rl_message = (RelativeLayout) findViewById(R.id.rl_message);
        rl_call = (RelativeLayout) findViewById(R.id.rl_call);
        text_review = (TextView) findViewById(R.id.text_review);
        image_background = (ImageView) findViewById(R.id.image_background);
    }

    private void showHireAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);

        alertDialog.setTitle("Hire Now");

        alertDialog.setMessage("Are you sure you want to hire this professional?");

        alertDialog.setPositiveButton("CONFIRM",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        hireNow();

                    }

                });

        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }


    private void setListener() {

        distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, ActivityShowRoute.class);
                in.putExtra("srcLat", arrayList.get(0).getSourceLatitude());
                in.putExtra("srcLng", arrayList.get(0).getSourceLongitude());
                in.putExtra("venLat", arrayList.get(0).getVendorLatitude());
                in.putExtra("venLng", arrayList.get(0).getVendorLongitude());
                startActivity(in);

            }
        });
        text_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(context, ActivityRatingBidirectional.class);
                in.putExtra("service_id", service_id);
                in.putExtra("service_name", service_name);
                in.putExtra("vendor_id", vendor_id);
                in.putExtra("searchId", searchId);
                startActivity(in);

            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO bind to drawer with... injection?
                overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);
                finish();
            }

        });

        rl_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String number = mobileNumber;
                    String uri = "tel:" + number.trim();
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }

            }
        });

        rl_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(context, ActivityChat.class);
                in.putExtra("reciever_id", vendor_id);
                in.putExtra("name", text_vendor_name.getText().toString());
                in.putExtra("image", vendorImage);
                in.putExtra("searchID", searchId);
                startActivity(in);

            }
        });
    }


    @Override
    public void onItemClickListener(int position, int flag) {


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);
    }

    @Override
    public void onPostRequestSucess(int position, String response) {

        try {

            if (position == 1) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    mobileNumber = data.getString("phoneNo");

                    is_paid = data.getString("is_paid");
                    if (is_paid.equalsIgnoreCase("0")) {
                        ll_call.setVisibility(View.GONE);
                        text_review.setVisibility(View.GONE);

                    } else {
                        if (data.getString("is_review_set").equalsIgnoreCase("0")) {
                            text_review.setVisibility(View.VISIBLE);
                            ll_call.setVisibility(View.VISIBLE);
                        } else {
                            ll_call.setVisibility(View.VISIBLE);
                            text_review.setVisibility(View.GONE);
                        }

                    }

                    text_vendor_name.setText(data.getString("vendorName"));
                    Picasso.with(context).load(getResources().getString(R.string.img_url) + data.getString("cover_image"))
                            .placeholder(R.drawable.placholder_profile)
                            .into(image_background);
                    vendorImage = getResources().getString(R.string.img_url) + data.getString("profile_image");
                    Picasso.with(context).load(getResources().getString(R.string.img_url) + data.getString("profile_image"))
                            .placeholder(R.drawable.placholder_profile)
                            .transform(new CircleTransform())
                            .into(image_profile);
                    rating.setRating(Float.parseFloat(data.getString("average_rating")));

                    detaildata = new ModelService();
                    detaildata.setPersonalDetailsArray(data.getJSONObject("detail").toString());
                    detaildata.setReviewArray(data.getJSONArray("review").toString());
                    detaildata.setPortfolioDetailsArray(data.getJSONArray("portfolio").toString());

                    detaildata.setVendorLatitude(data.getString("vendorLatitude"));
                    detaildata.setVendorLongitude(data.getString("vendorLongitude"));
                    detaildata.setVendorDistance(data.getString("vendorDistance"));
                    detaildata.setSourceLatitude(data.getString("sourceLatitude"));
                    detaildata.setSourceLongitude(data.getString("sourceLongitude"));

                    arrayList.add(detaildata);
                    SpannableString content = new SpannableString(AppUtils.convertToTwoDecimalDigit(data.getString("vendorDistance")) + " Km ");
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

                    distance.setText(content);

                    Log.e("arraylistasize", "**" + arrayList.size());

                    vendoDetailPagerAdapter = new VendoDetailPagerAdapter(getSupportFragmentManager(), 3, arrayList);
                    pager.setAdapter(vendoDetailPagerAdapter);
                    tabLayout.setupWithViewPager(pager);
                    setupTabIcons();
                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);
                }
            } else if (position == 2) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    AppUtils.showCustomAlert(Vendor_Detail_Bidirectional.this, commandResult.getString("message"));
                    Intent in = new Intent(context, Activity_VendorHired.class);
                    in.putExtra("service_id", data.getString("serviceID"));
                    in.putExtra("vendor_id", data.getString("vendorID"));
                    in.putExtra("service_name", data.getString("serviceName"));
                    in.putExtra("pageFlag", "1");
                    in.putExtra("searchId", searchId);
                    startActivity(in);
                    finish();

                } else {
                    AppUtils.showCustomAlert(Vendor_Detail_Bidirectional.this, commandResult.getString("message"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onPostRequestFailed(int position, String response) {
        AppUtils.showCustomAlert(Vendor_Detail_Bidirectional.this, getResources().getString(R.string.problem_server));

    }
}
