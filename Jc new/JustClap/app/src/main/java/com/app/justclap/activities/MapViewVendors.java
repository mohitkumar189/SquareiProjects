package com.app.justclap.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.R;
import com.app.justclap.aynctask.CommonAsyncTaskHashmap;
import com.app.justclap.interfaces.ApiResponse;
import com.app.justclap.model.ModelRequest;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.utils.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MapViewVendors extends AppCompatActivity implements OnMapReadyCallback, ApiResponse {

    private Toolbar toolbar;
    private Context context;
    private GoogleMap map;
    private ArrayList<LatLng> markerLocation;
    private ArrayList<ModelRequest> listVendor;
    private TextView text_quote, text_username, text_title;
    private ImageView image_user;
    private RatingBar ratingbar;
    private Button btn_hire;
    private RelativeLayout rl_userdetail, relative_hire;
    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();
    private int clickedPos;
    private ImageView image_detail, image_delete;
    private String RequestId = "";
    private String reason = "";
    private double mLat, mLong;
    private GPSTracker gTraker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view_vendors);
        context = this;
        init();
        setListener();
        setData();

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    public void showSettingsAlert() {
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

            // Setting Dialog Title
            alertDialog.setTitle("GPS is settings");

            // Setting Dialog Message
            alertDialog
                    .setMessage("GPS is not enabled. Do you want to go to settings menu?");

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);

                        }
                    });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    });

            // Showing Alert Message
            alertDialog.show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /*
          * initialize all views
          */
    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        text_title = (TextView) findViewById(R.id.text_title);
        text_quote = (TextView) findViewById(R.id.text_quote);
        text_username = (TextView) findViewById(R.id.text_username);
        image_user = (ImageView) findViewById(R.id.image_user);
        ratingbar = (RatingBar) findViewById(R.id.ratingbar);
        btn_hire = (Button) findViewById(R.id.btn_hire);
        rl_userdetail = (RelativeLayout) findViewById(R.id.rl_userdetail);
        relative_hire = (RelativeLayout) findViewById(R.id.relative_hire);
        text_title.setText(getIntent().getStringExtra("title"));
        RequestId = getIntent().getStringExtra("RequestId");
        image_delete = (ImageView) findViewById(R.id.image_delete);
        image_detail = (ImageView) findViewById(R.id.image_detail);
    }

    private void setData() {

        gTraker = new GPSTracker(context);

        if (gTraker.canGetLocation()) {

            mLat = gTraker.getLatitude();
            mLong = gTraker.getLongitude();

            Log.e("mLat", "" + mLat);
            Log.e("mLong", "" + mLong);

        } else {
            showSettingsAlert();
            // getTrainingList();
        }


        listVendor = new ArrayList<>();
        markerLocation = new ArrayList<>();

        String vendorarray = getIntent().getStringExtra("vendorarray");
        try {
            JSONArray array = new JSONArray(vendorarray);

            for (int i = 0; i < array.length(); i++) {

                JSONObject jo = array.getJSONObject(i);

                ModelRequest modelRequest = new ModelRequest();
                modelRequest.setQuoteId(jo.getString("QuoteId"));
                modelRequest.setQuoteValue(jo.getString("QuoteValue"));
                modelRequest.setQuoteDate(jo.getString("QuoteDate"));
                modelRequest.setVendorId(jo.getString("VendorId"));
                modelRequest.setVendorName(jo.getString("VendorName"));
                modelRequest.setVendorEmail(jo.getString("VendorEmail"));
                modelRequest.setVendorMobile(jo.getString("VendorMobile"));
                modelRequest.setVendorLatitude(jo.getString("VendorLatitude"));
                modelRequest.setVendorAddress(jo.getString("VendorAddress"));
                modelRequest.setVendorProfileImage(jo.getString("VendorProfileImage"));
                modelRequest.setVendorLongitude(jo.getString("VendorLongitude"));

                LatLng lat = new LatLng(Double.parseDouble(jo.getString("VendorLatitude")), Double.parseDouble(jo.getString("VendorLongitude")));
                markerLocation.add(lat);

                listVendor.add(modelRequest);
            }

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showAnimation() {
        Animation anim;
        anim = AnimationUtils.loadAnimation(context, R.anim.bottom_up);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                rl_userdetail.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        rl_userdetail.startAnimation(anim);
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
        relative_hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(context, VendorDetail.class);
                in.putExtra("vendorId", listVendor.get(clickedPos).getQuoteId());
                in.putExtra("title", text_title.getText().toString());
                startActivity(in);
            }
        });
        image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmtion();

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
                            Toast.makeText(context, "Please select reason for cancel request", Toast.LENGTH_SHORT).show();
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


    private void deleteService() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {

                HashMap<String, String> hm = new HashMap<>();

                hm.put("cancellationReasonId", "1");
                hm.put("requestId", RequestId);
                hm.put("userId", "1124");

                String url = getResources().getString(R.string.url_base_new) + getResources().getString(R.string.cancelServiceRequest);
                new CommonAsyncTaskHashmap(1, context, this).getqueryJson(url, hm);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        map.addMarker(new MarkerOptions().position(new LatLng(mLat, mLong)).icon(BitmapDescriptorFactory.fromResource(R.drawable.redmap)).title("Current Location"));


        try {
            map.setMyLocationEnabled(true);
            try {
                for (int i = 0; i < markerLocation.size(); i++) {
                    Log.e("markerLocation", "**" + markerLocation.get(i));
                    Marker marker = map.addMarker(new MarkerOptions().position(markerLocation.get(i)).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_red_circle)).title(listVendor.get(i).getQuoteValue()));
                    //       map.addMarker(new MarkerOptions().position(markerLocation.get(i)).icon(BitmapDescriptorFactory.fromResource(R.drawable.loaction_marker_dest)).title(listVendor.get(i).getQuoteValue()));
                    mHashMap.put(marker, i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            map.getUiSettings().setZoomControlsEnabled(true);

            // Enable / Disable my location button
            map.getUiSettings().setMyLocationButtonEnabled(true);

            // mMap.moveCamera(CameraUpdateFactory.newLatLng(markerLocation.get(0)));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(markerLocation.get(0)).zoom(10).build();

            map.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    clickedPos = mHashMap.get(marker);
                    Log.e("Position of arraylist", clickedPos + "");
                    text_username.setText(listVendor.get(clickedPos).getVendorName());
                    text_quote.setText(listVendor.get(clickedPos).getQuoteValue());
                    if (!listVendor.get(clickedPos).getVendorProfileImage().equalsIgnoreCase("")) {
                        Picasso.with(context).load(listVendor.get(clickedPos).getVendorProfileImage()).into(image_user);
                    }
                    if (rl_userdetail.getVisibility() == View.GONE) {
                        showAnimation();
                    }

                    return false;
                }
            });

        } catch (SecurityException e) {
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
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                    setResult(22);
                    finish();

                } else {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();

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
}
