package com.app.justclap.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.justclap.R;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.app.justclap.asyntask.AsyncPostDataFragment;
import com.app.justclap.asyntask.AsyncPostDataResponse;
import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.utils.CircleTransform;

public class Activity_VendorHired extends AppCompatActivity implements ListenerPostData, OnCustomItemClicListener {


    TextView text_service_name, distance;
    ImageView img_back, image_background;
    String service_id = "", service_name = "", searchId = "", pageFlag = "0", vendor_id = "", reason = "";
    ModelService serviceDetail;
    ArrayList<ModelService> service_list;
    Context context;
    ConnectionDetector cd;
    RelativeLayout rl_main_layout, rl_network;
    int deleteposition, menu_position;
    TextView text_services_name, text_serviceAssigned, text_quoted_detail, review_count, text_chats, text_phone, text_profile;
    ImageView image_viewers;
    RatingBar rating;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__hirevendor);

        context = this;
        init();
        service_list = new ArrayList<>();
        Intent in = getIntent();
        if (in.hasExtra("service_id")) {
            vendor_id = in.getExtras().getString("vendor_id");
            service_id = in.getExtras().getString("service_id");
            service_name = in.getExtras().getString("service_name");
        }
        if (in.hasExtra("service_image")) {

            Picasso.with(context).load(in.getExtras().getString("service_image"))
                    .into(image_background);
        }

        if (in.hasExtra("searchId")) {
            searchId = in.getExtras().getString("searchId");
        }
        if (in.hasExtra("pageFlag")) {
            pageFlag = in.getExtras().getString("pageFlag");
        }
        setListener();
        text_service_name.setText(service_name);

        getServicelist();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

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

        rl_main_layout = (RelativeLayout) findViewById(R.id.rl_main_layout);
        rl_network = (RelativeLayout) findViewById(R.id.rl_network);

        image_viewers = (ImageView) findViewById(R.id.image_viewers);
        text_services_name = (TextView) findViewById(R.id.text_services_name);
        text_serviceAssigned = (TextView) findViewById(R.id.text_serviceAssigned);
        text_quoted_detail = (TextView) findViewById(R.id.text_quoted_detail);
        review_count = (TextView) findViewById(R.id.review_count);
        distance = (TextView) findViewById(R.id.distance);
        text_chats = (TextView) findViewById(R.id.text_chats);
        text_phone = (TextView) findViewById(R.id.text_phone);
        text_profile = (TextView) findViewById(R.id.text_profile);
        rating = (RatingBar) findViewById(R.id.rating);
        image_background = (ImageView) findViewById(R.id.image_background);
        img_back = (ImageView) findViewById(R.id.img_back);
        text_service_name = (TextView) findViewById(R.id.text_service_name);

    }

    private void getServicelist() {

        try {

            cd = new ConnectionDetector(context);

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present
                rl_main_layout.setVisibility(View.GONE);
                rl_network.setVisibility(View.VISIBLE);
                //  AppUtils.showCustomAlert(Activity_VendorHired.this, "Internet Connection Error, Please connect to working Internet connection");
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
                                "serviceID", service_id));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "vendorID", vendor_id));

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "searchId", searchId));

                new AsyncPostDataFragment(Activity_VendorHired.this, (ListenerPostData) this, 1, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.hireVendorDetailPage));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void setListener() {

        distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, ActivityShowRoute.class);
                in.putExtra("srcLat", service_list.get(0).getSourceLatitude());
                in.putExtra("srcLng", service_list.get(0).getSourceLongitude());
                in.putExtra("venLat", service_list.get(0).getVendorLatitude());
                in.putExtra("venLng", service_list.get(0).getVendorLongitude());
                startActivity(in);

            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO bind to drawer with... injection?
                if (pageFlag.equalsIgnoreCase("1")) {
                    Intent in = new Intent(Activity_VendorHired.this, DashBoardActivity.class);
                    startActivity(in);
                    finish();
                    overridePendingTransition(R.anim.left_to_right,
                            R.anim.right_to_left);
                } else {

                    finish();
                    overridePendingTransition(R.anim.left_to_right,
                            R.anim.right_to_left);
                }

            }

        });

        text_chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(Activity_VendorHired.this, ActivityChat.class);
                in.putExtra("reciever_id", service_list.get(0).getVendorID());
                in.putExtra("name", service_list.get(0).getVendorName());
                in.putExtra("image", service_list.get(0).getVendorimage());
                in.putExtra("searchID", searchId);
                startActivity(in);
            }
        });

        text_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO bind to drawer with... injection?
                try {
                    String number = service_list.get(0).getMobileNumber();

                    String uri = "tel:" + number.trim();
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }

            }

        });
        text_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO bind to drawer with... injection?
                Intent in = new Intent(Activity_VendorHired.this, Vendor_Detail.class);
                in.putExtra("vendor_id", service_list.get(0).getVendorID());
                in.putExtra("service_id", service_id);
                in.putExtra("searchId", searchId);
                in.putExtra("flag", "4");
                startActivity(in);

            }

        });

    }


    private void showConfirmtion() {

        final CharSequence[] items = {"Due to my personal reason", "I don't like responses", "Submit by mistake"
        };
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);

        alertDialog.setTitle("Cancel Request");

        // alertDialog.setMessage("Are you sure you want to cancel this request?");

        alertDialog.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Due to my personal reason")) {

                    reason = "0";

                } else if (items[item].equals("I don't like responses")) {

                    reason = "1";

                } else if (items[item].equals("Submit by mistake")) {

                    reason = "2";
                }

            }
        });


        alertDialog.setPositiveButton("CONFIRM",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (reason.equalsIgnoreCase("")) {
                            AppUtils.showCustomAlert(Activity_VendorHired.this, "Please select reason for cancel request");
                        } else {
                            deleteService();
                            dialog.cancel();
                        }

                        Log.e("clickedPosiion", which + "");

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

    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 6) {
            menu_position = position;
            deleteposition = position;
            showMenu();

        }
        if (flag == 1) {
            Intent in = new Intent(context, Vendor_Detail.class);
            in.putExtra("vendor_id", service_list.get(position).getVendorID());
            in.putExtra("service_id", service_list.get(position).getServiceID());
            in.putExtra("service_name", service_list.get(position).getServiceName());
            in.putExtra("searchId", searchId);
            in.putExtra("flag", "2");
            startActivity(in);

        }
    }

    private void showMenu() {
        final CharSequence[] items = {"View Details", "Cancel Request",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(
                context);
        builder.setTitle("JustClap");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("View Details")) {

                    Intent in = new Intent(context, ViewDetailAnswers.class);
                    in.putExtra("service_id", service_list.get(menu_position).getServiceID());
                    in.putExtra("service_name", service_list.get(menu_position).getServiceName());
                    startActivity(in);

                } else if (items[item].equals("Cancel Request")) {

                    showConfirmtion();


                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void deleteService() {

        try {

            // for gcm condition check
            // =================================================

            cd = new ConnectionDetector(context);

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present
                AppUtils.showCustomAlert(Activity_VendorHired.this, getResources().getString(R.string.message_network_problem));

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
                                "serviceID", service_id));
                nameValuePairs
                        .add(new BasicNameValuePair(
                                "reason", reason));
                new AsyncPostDataResponse(Activity_VendorHired.this, 3, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.deleteCustomerService));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (pageFlag.equalsIgnoreCase("1")) {
            Intent in = new Intent(Activity_VendorHired.this, DashBoardActivity.class);
            startActivity(in);
            finish();
            overridePendingTransition(R.anim.left_to_right,
                    R.anim.right_to_left);
        } else {

            finish();
            overridePendingTransition(R.anim.left_to_right,
                    R.anim.right_to_left);
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

                    JSONObject jo = commandResult.getJSONObject("data");

                    service_list.removeAll(service_list);

                    Picasso.with(context).load(getResources().getString(R.string.img_url) + jo.getString("service_image"))
                            // .transform(new CircleTransform())
                            .into(image_background);

                    serviceDetail = new ModelService();
                    serviceDetail.setServiceName(jo.getString("serviceName"));
                    serviceDetail.setVendorID(jo.getString("vendorID"));
                    serviceDetail.setServiceID(jo.getString("serviceID"));
                    serviceDetail.setQuote("Rs. " + jo.getString("price"));
                    serviceDetail.setVendorimage(getResources().getString(R.string.img_url) + jo.getString("vendor_profile_image"));
                    serviceDetail.setServiceImage(getResources().getString(R.string.img_url) + jo.getString("service_image"));
                    serviceDetail.setVendorName(jo.getString("vendorName"));
                    serviceDetail.setMobileNumber(jo.getString("phoneNo"));

                    serviceDetail.setVendorLatitude(jo.getString("vendorLatitude"));
                    serviceDetail.setVendorLongitude(jo.getString("vendorLongitude"));
                    serviceDetail.setVendorDistance(jo.getString("vendorDistance"));
                    serviceDetail.setSourceLatitude(jo.getString("sourceLatitude"));
                    serviceDetail.setSourceLongitude(jo.getString("sourceLongitude"));


                    service_list.add(serviceDetail);
                    text_services_name.setText(jo.getString("vendorName"));
                    Picasso.with(context).load(getResources().getString(R.string.img_url) + jo.getString("vendor_profile_image"))
                            .transform(new CircleTransform())
                            .placeholder(R.drawable.user)
                            .into(image_viewers);

                    if (jo.getString("noOfTimeHire").equalsIgnoreCase("0")) {
                        text_serviceAssigned.setText("Not Hired till now");
                    } else {
                        text_serviceAssigned.setText("Hired " + jo.getString("noOfTimeHire") + " times");
                    }

                    text_quoted_detail.setText("Rs. " + jo.getString("price"));
                    if (Integer.parseInt(jo.getString("noOfReview")) > 1) {
                        review_count.setText(jo.getString("noOfReview") + " reviews");
                    } else {
                        review_count.setText(jo.getString("noOfReview") + " review");
                    }

                    rating.setRating(Float.parseFloat(jo.getString("averageRating")));
                    SpannableString content = new SpannableString(AppUtils.convertToTwoDecimalDigit(jo.getString("vendorDistance")) + " Km ");
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

                    distance.setText(content);
                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);
                    rating.setEnabled(false);

                } else {
                    AppUtils.showCustomAlert(Activity_VendorHired.this, commandResult.getString("message"));

                }


            } else if (position == 3) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    //JSONObject data = commandResult.getJSONObject("data");

                    AppUtils.showCustomAlert(Activity_VendorHired.this, commandResult.getString("message"));
                    // finish();

                } else {
                    AppUtils.showCustomAlert(Activity_VendorHired.this, commandResult.getString("message"));

                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onPostRequestFailed(int position, String response) {
        AppUtils.showCustomAlert(Activity_VendorHired.this, getResources().getString(R.string.problem_server));
    }


}
