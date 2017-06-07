package com.app.justclap.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.app.justclap.R;
import com.app.justclap.models.ModelService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VendorsSearchMapView extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<LatLng> markerLocation;
    ArrayList<String> userName;
    ArrayList<String> is_vendor;
    Context context;
    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
    private static final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);
    String latArray = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_ride_map_view);
        //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        context = this;
        // setSupportActionBar(toolbar);
        markerLocation = new ArrayList<>();
        userName = new ArrayList<>();
        is_vendor = new ArrayList<>();

        Intent in = getIntent();
        latArray = in.getExtras().getString("latArray");

        setData();


    }

    private void setData() {
        try {
            JSONArray data = new JSONArray(latArray);

            for (int i = 0; i < data.length(); i++) {

                JSONObject jo = data.getJSONObject(i);
                userName.add(jo.getString("userName"));
                is_vendor.add(jo.getString("is_vendor"));
                LatLng lat = new LatLng(Double.parseDouble(jo.getString("latitude")), Double.parseDouble(jo.getString("longitude")));
                markerLocation.add(lat);


            }
            Log.e("vendorsCount", "*" + markerLocation.size());

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mymap);
            mapFragment.getMapAsync(this);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        for (int i = 0; i < markerLocation.size(); i++) {

            if (is_vendor.get(i).equalsIgnoreCase("1")) {
                mMap.addMarker(new MarkerOptions().position(markerLocation.get(i)).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker)).title(userName.get(i)));
            } else {
                mMap.addMarker(new MarkerOptions().position(markerLocation.get(i)).icon(BitmapDescriptorFactory.fromResource(R.drawable.loaction_marker_dest)).title(userName.get(i)));

            }
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Enable / Disable my location button
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // mMap.moveCamera(CameraUpdateFactory.newLatLng(markerLocation.get(0)));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(markerLocation.get(0)).zoom(10).build();

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

    }
}
