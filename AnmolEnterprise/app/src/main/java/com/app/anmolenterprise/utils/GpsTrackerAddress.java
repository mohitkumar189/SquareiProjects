package com.app.anmolenterprise.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by hemanta on 28-03-2017.
 */

public class GpsTrackerAddress implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final Context mContext;
    private LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private double mLat = 0.0;
    private double mLong = 0.0;
    public boolean canGetLocation = false;

    public GpsTrackerAddress(Context context) {
        this.mContext = context;
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        googleapi();
    }

    private void googleapi() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        try {
            if (mLastLocation != null) {
                mLat = mLastLocation.getLatitude();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // return latitude
        return mLat;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        try {

            if (mLastLocation != null) {
                mLong = mLastLocation.getLongitude();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // return longitude
        return mLong;
    }

    private void con() {
        Log.e("Connected", "inCon");
        try {
            // startLocationUpdate();

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            Log.e("mLastLocation", "**" + mLastLocation);
            if (mLastLocation != null) {
                mLat = mLastLocation.getLatitude();
                mLong = mLastLocation.getLongitude();

                Log.e("latitude", "**" + mLat + "*" + mLong);
                canGetLocation = true;
                getaddress(mLat, mLong);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }


    public String getaddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(mContext, Locale.getDefault());
        String formatted_address = "";
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses.size() > 0) {
                String address;
                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                if (address.contains("Unnamed")) {
                    address = addresses.get(1).getAddressLine(0);
                }
                String city = addresses.get(0).getLocality();
                String sublocality = addresses.get(0).getSubLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();

                String newaddress = "";
                if (address == null)
                    newaddress = city + ", " + state;
                else if (city == null)
                    newaddress = address + ", " + state;
                else if (state == null)
                    newaddress = address + ", " + city;
                else if (address == null && state == null)
                    newaddress = city;
                else if (city == null && state == null)
                    newaddress = address;
                else
                    newaddress = knownName + ", " + sublocality + ", " + city;

                //newaddress = knownName + ", "+sublocality + ", " + city + ", " + state;
                String noSpaces = newaddress.replaceAll("\0", " ");
                formatted_address = noSpaces;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return formatted_address;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            canGetLocation = true;
            con();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
