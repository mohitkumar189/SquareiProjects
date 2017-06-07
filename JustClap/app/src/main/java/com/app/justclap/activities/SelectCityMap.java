package com.app.justclap.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.app.justclap.utils.AppUtils;
import com.app.justclap.utils.GPSTracker;

@SuppressLint("NewApi")
public class SelectCityMap extends AppBaseActivity implements OnClickListener {

    GoogleMap googleMap;
    Context context;
    String location = "", city = "";
    String pickupAddress = "";
    String pic_lat = "", pic_long = "";
    private Double mLat = 0.0, mLong = 0.0;
    String myLat = "", mylong = "", start_address, start_area, start_locality,
            end_address, end_area, end_locality, address, locality, area;
    LatLng ltlng;
    Intent i;
    Boolean addressSet = false, isFirtLoad = true;
    Geocoder geocoder;
    List<Address> addresses = new ArrayList<Address>();
    TextView addre;
    AutoCompleteTextView edit_address;
    PlacesTask placesTask;
    ParserTask parserTask;
    ImageView btn_current_loc;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city_map);
        context = this;

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
        addre = ((TextView) findViewById(R.id.textView3));
        addre.setSelected(true);

     /*   try {
            Bundle b = getIntent().getExtras();
            mLat = Double.parseDouble(b.getString("lat"));
            mLong = Double.parseDouble(b.getString("lng"));
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
*/

        LocationManager lm = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(context);
            dialog.setMessage("Gps is not enabled, Please on your gps!");
            dialog.setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    paramDialogInterface.dismiss();
                }
            });
            dialog.show();
        } else {

            GPSTracker mGPS = new GPSTracker(this);

            if (mGPS.canGetLocation) {

                mLat = mGPS.getLatitude();
                mLong = mGPS.getLongitude();
            }
            // infowindow.setText("Getting address..");
            myLat = "" + mLat;
            mylong = "" + mLong;

        }
        initilizeMap();

        ((ImageView) findViewById(R.id.img_fav)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.search_img)).setOnClickListener(this);
        btn_current_loc = (ImageView) findViewById(R.id.btn_current_loc);
        edit_address = (AutoCompleteTextView) findViewById(R.id.find_address);
        btn_current_loc.setOnClickListener(this);
        ((Button) findViewById(R.id.select))
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        if (city != null && !city.equalsIgnoreCase("")) {
                            Intent i = new Intent();
                            i.putExtra("location", city);
                            i.putExtra("latitude", pic_lat);
                            i.putExtra("longitude", pic_long);
                            setResult(512, i);
                            finish();

                        } else {

                            Toast.makeText(context, "Unable to find city, please change location.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        edit_address
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {
                        // TODO Auto-generated method stub
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                            AppUtils.onKeyBoardDown(context);
                            String add = edit_address.getText().toString().trim();
                            if (add.equalsIgnoreCase("")) {
                                Toast.makeText(context, "please enter address", Toast.LENGTH_SHORT).show();
                            } else {
                                setSearchLocation(add);
                            }

                            return true;
                        }
                        return false;

                    }
                });

        edit_address.setThreshold(1);
        edit_address.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

		/*
         * GetAddressTask task = new GetAddressTask(); task.execute(new String[]
		 * {});
		 */

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        /*GetFavoriteLocationTask task = new GetFavoriteLocationTask();
        task.execute(MyUtil.getUserId(context));*/
    }

    public void onKeyBoardDown() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(
                getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

    }

    public boolean isNetworkAvailable() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("Network", "is not available");
        return false;
    }

    @Override
    protected void onResume() {

        super.onResume();
        overridePendingTransition(0, 0);
    }

    @SuppressLint("NewApi")
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            if (googleMap == null) {
                Log.e("Google Map", "is Null");
            }

            // Changing map type
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

            // Showing / hiding your current location

            //	googleMap.setMyLocationEnabled(false);

            // Enable / Disable zooming controls
            googleMap.getUiSettings().setZoomControlsEnabled(true);

            // Enable / Disable my location button
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);

            // Enable / Disable Compass icon
            googleMap.getUiSettings().setCompassEnabled(true);

            // Enable / Disable Rotate gesture
            googleMap.getUiSettings().setRotateGesturesEnabled(true);

            // Enable / Disable zooming functionality
            googleMap.getUiSettings().setZoomGesturesEnabled(true);

		/*	GPSTracker mGPS = new GPSTracker(this);

			if (mGPS.canGetLocation) {

				mLat = mGPS.getLatitude();
				mLong = mGPS.getLongitude();

			}*/

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(mLat, mLong)).zoom(13).build();

            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            if (isFirtLoad) {
                MarkerOptions marker = new MarkerOptions().position(new LatLng(
                        mLat, mLong));
                marker.icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.location_marker));
                googleMap.addMarker(marker);

                try {
                    Geocoder geocoder;
                    List<Address> addresses = new ArrayList<Address>();
                    geocoder = new Geocoder(context, Locale.getDefault());

                    addresses = geocoder.getFromLocation(mLat, mLong, 1);
                    pic_lat = mLat + "";
                    pic_long = mLong + "";
                    String address = addresses.get(0).getAddressLine(0);
                    String city1 = addresses.get(0).getAddressLine(1);
                    String country = addresses.get(0).getAddressLine(2);
                    city = addresses.get(0).getLocality();
                    Log.e("selected_city", city);
                    location = address + " : " + city1 + " : " + country;
                    addre.setText(city);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            googleMap.setOnMapClickListener(new OnMapClickListener() {

                @Override
                public void onMapClick(LatLng point) {
                    onKeyBoardDown();
                    isFirtLoad = false;
                    try {
                        Geocoder geocoder;
                        List<Address> addresses = new ArrayList<Address>();
                        geocoder = new Geocoder(context, Locale.getDefault());
                        try {
                            addresses = geocoder.getFromLocation(
                                    point.latitude, point.longitude, 1);
                            pic_lat = point.latitude + "";
                            pic_long = point.longitude + "";
                            googleMap.clear();

                            googleMap = null;
                            initilizeMap();
                            MarkerOptions marker = new MarkerOptions()
                                    .position(new LatLng(point.latitude,
                                            point.longitude));


                            // if(vtype1.get(i).contains("user"))
                            marker.icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.location_marker));
                            // marker.title(driverDetailList.size() + 1 + "");

                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(new LatLng(point.latitude,
                                            point.longitude)).zoom(13).build();

                            googleMap.animateCamera(CameraUpdateFactory
                                    .newCameraPosition(cameraPosition));

                            // else
                            // marker.icon(BitmapDescriptorFactory
                            // .defaultMarker(BitmapDescriptorFactory.HUE_RED));

                            googleMap.addMarker(marker);

                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        String address = addresses.get(0).getAddressLine(0);
                        String city1 = addresses.get(0).getAddressLine(1);
                        String country = addresses.get(0).getAddressLine(2);

                        city = addresses.get(0).getLocality();
                        Log.e("selected_city", city);
                        location = address + " : " + city1 + " : " + country;
                        addre.setText(city);

						/*
                         * Toast.makeText( context, "Address is :" + address +
						 * " : " + city + " : " + country,
						 * Toast.LENGTH_LONG).show();
						 */
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            });
            googleMap
                    .setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                        @Override
                        public void onInfoWindowClick(Marker marker) {

							/*
                             * Intent i= new Intent(context, BookTrip.class);
							 * i.putExtra("pickupAddress", pickupAddress);
							 * i.putExtra("lat", myLat); i.putExtra("long",
							 * mylong);
							 */
                            /*
                             * i.putExtra("pickupAddress", "Noida 64");
							 * i.putExtra("lat", "73.129875");
							 * i.putExtra("long", "18.47622");
							 */

                            // startActivity(i);
                        }
                    });

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
            }
        }

		/*
         * googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback()
		 * {
		 * 
		 * @Override public void onMapLoaded() { Log.e("TAG",
		 * googleMap.getCameraPosition().target.toString());
		 * 
		 * // Toast.makeText(context, ltlng.toString(),
		 * Toast.LENGTH_LONG).show(); } });
		 */

        googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition position) {

                // Log.e("GETTING ADDRESS", "RUN");

                ltlng = googleMap.getCameraPosition().target;

                myLat = "" + ltlng.latitude;
                mylong = "" + ltlng.longitude;
                // infowindow.setText("Getting address..");

                GetAddressTask task = new GetAddressTask();
                task.execute(new String[]{});

				/*
                 * GetAddressFromURLTask task= new GetAddressFromURLTask();
				 * task.execute(new String[]
				 * {String.valueOf(ltlng.latitude),String
				 * .valueOf(ltlng.longitude)});
				 */

            }

        });

    }

    // *******************************************************************

    private class GetAddressTask extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            // infowindow.setText("Getting address..");
            /*
             * Double
			 * lat_chk=Double.parseDouble(myLat),lng_chk=Double.parseDouble
			 * (mylong); if ((lat_chk <= 18.401195) || (lat_chk >= 18.689582) ||
			 * (lng_chk <= 73.665532) || (lng_chk >= 74.030166)) {
			 * 
			 * Toast.makeText(context,"Source is outside Pune. Choose properly",
			 * Toast.LENGTH_LONG).show();
			 * 
			 * CameraPosition cameraPosition = new CameraPosition.Builder()
			 * .target(new LatLng(18.5246164, 73.8629674)).zoom(15).build();
			 * 
			 * googleMap.animateCamera(CameraUpdateFactory
			 * .newCameraPosition(cameraPosition)); //desiredLocation=false;
			 * ltlng = googleMap.getCameraPosition().target;
			 * 
			 * myLat=""+ltlng.latitude; mylong=""+ltlng.longitude;
			 */

			/*
             * ad1=new AlertDialog.Builder(MyMap2.this)
			 * .setTitle("Invalid location")
			 * .setMessage("Location is outside Pune. Choose properly")
			 * .setPositiveButton(android.R.string.yes, new
			 * DialogInterface.OnClickListener() { public void
			 * onClick(DialogInterface dialog, int which) { CameraPosition
			 * cameraPosition = new CameraPosition.Builder() .target(new
			 * LatLng(18.5246164, 73.8629674)).zoom(16).build();
			 * 
			 * googleMap.animateCamera(CameraUpdateFactory
			 * .newCameraPosition(cameraPosition)); //desiredLocation=false;
			 * ltlng = googleMap.getCameraPosition().target;
			 * 
			 * myLat=""+ltlng.latitude; mylong=""+ltlng.longitude;
			 * 
			 * ad1.dismiss();
			 * 
			 * 
			 * 
			 * 
			 * } })
			 * 
			 * .setIcon(android.R.drawable.ic_dialog_alert) .show();
			 */

            // }

        }

        protected String doInBackground(String... urls) {

            String response = "";

            try {

                geocoder = new Geocoder(context, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(ltlng.latitude,
                            ltlng.longitude, 20);
                    Log.e("addresses", addresses.toString());
                    if (addresses.size() > 0) {

                        address = addresses.get(0).getAddressLine(0);
                        locality = addresses.get(0).getAddressLine(1);
                        area = addresses.get(0).getAddressLine(1);

                        // String country = addresses.get(0).getAddressLine(2);

                        pickupAddress = address + " " + locality + " " + area;

                        addressSet = true;
                    } else
                        return null;

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                /*    Log.e("Error :>>", "error in postexecute");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            GetAddressFromURLTask task = new GetAddressFromURLTask();
                            task.execute(new String[]{String.valueOf(ltlng.latitude),
                                    String.valueOf(ltlng.longitude)});

                        }
                    }, 100);
*/

                    e.printStackTrace();
                }
                return pickupAddress;

            } catch (Exception e) {
                e.printStackTrace();
/*


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        GetAddressFromURLTask task = new GetAddressFromURLTask();
                        task.execute(new String[]{String.valueOf(ltlng.latitude),
                                String.valueOf(ltlng.longitude)});

                    }
                }, 100);
*/


                Log.e("Error 2 :>>", "error in postexecute");

            }

            return response;
        }

        protected void onPostExecute(String result) {

            try {

                if (result != null) {
                    if (addressSet) {
                        // infowindow.setText(result);
                        // infowindow.setSelected(true);
                        Log.e("PickUpAddress 1:", result);
                    } else {
                        // infowindow.setText("Error in connecting to Google Server... try again later");
                        GetAddressFromURLTask task = new GetAddressFromURLTask();
                        task.execute(new String[]{
                                String.valueOf(ltlng.latitude),
                                String.valueOf(ltlng.longitude)});

                    }
                } else {
                    Toast.makeText(
                            context,
                            "Unable to find address of this location, please try again", Toast.LENGTH_SHORT).show();
                    // infowindow.setText("Error in connecting to Google Server... try again later");

                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(
                        context,
                        "Unable to find address of this location, please try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GetAddressFromURLTask extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... urls) {

            String response = "";
            HttpResponse response2 = null;
            StringBuilder stringBuilder = new StringBuilder();

            try {

				/*
                 * URL url = new URL(URL + "?latlng=" +
				 * URLEncoder.encode(urls[0], "UTF-8") + "&sensor=false");
				 */

                HttpGet httpGet = new HttpGet(
                        "http://maps.google.com/maps/api/geocode/json?latlng="
                                + urls[0] + "," + urls[1]);
                HttpClient client = new DefaultHttpClient();
                Log.e("Url ",
                        "http://maps.google.com/maps/api/geocode/json?latlng="
                                + urls[0] + "," + urls[1]);

                try {
                    response2 = client.execute(httpGet);
                    HttpEntity entity = response2.getEntity();
                    InputStream stream = entity.getContent();
                    int b;
                    while ((b = stream.read()) != -1) {
                        stringBuilder.append((char) b);
                    }
                } catch (ClientProtocolException e) {
                } catch (IOException e) {
                }

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject = new JSONObject(stringBuilder.toString());

                    Log.e("Url response", stringBuilder.toString());
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

				/*
				 * if (response != null) { HttpEntity resEntity =
				 * response.getEntity(); response_data =
				 * EntityUtils.toString(resEntity); }
				 */

            } catch (Exception e) {

                e.printStackTrace();
                Log.e("Error 2 :>>", "error in doINBackground OUTER");
                // infowindow.setText("Error in connecting to Google Server... try again later");

            }

            return stringBuilder.toString();
        }

        protected void onPostExecute(String result) {

            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray resultsObject = jsonObject
                            .getJSONArray("results");
                    JSONObject formattedAddress = (JSONObject) resultsObject
                            .get(0);
                    String formatted_address = formattedAddress
                            .getString("formatted_address");

                    Log.e("formatted Address>>", formatted_address);

					/*
					 * infowindow.setText(formatted_address);
					 * infowindow.setSelected(true);
					 */

                    String[] str = formatted_address.split(",");
                    int length = str.length;

                    for (int i = str.length; i <= 0; i--) {
                        if (i == length) {
                            end_address = str[i];
                        }

                        if (i == length - 1) {
                            end_area = str[i];
                        }

                        if (i == length - 2) {
                            end_locality = str[i];
                        }

                        start_address = str[i];
                        start_area = str[i];
                        start_locality = str[i];

                    }

                    addressSet = true;
                    pickupAddress = formatted_address;

                } else
                    Toast.makeText(context, "Result is null", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 515 && resultCode == 522) {

            Bundle b = data.getExtras();

            location = b.getString("location");
            pic_lat = b.getString("latitude");
            pic_long = b.getString("longitude");
            addre.setText(location);

            isFirtLoad = false;

            googleMap.clear();
            googleMap = null;
            initilizeMap();
            MarkerOptions marker = new MarkerOptions().position(new LatLng(
                    Double.parseDouble(pic_lat), Double.parseDouble(pic_long)));
            marker.icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.location_marker));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(Double.parseDouble(pic_lat), Double
                            .parseDouble(pic_long))).zoom(13).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            googleMap.addMarker(marker);

        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.search_img:
                AppUtils.onKeyBoardDown(context);
                String add = edit_address.getText().toString().trim();
                if (add.equalsIgnoreCase("")) {
                    Toast.makeText(context, "please enter address", Toast.LENGTH_SHORT).show();
                } else {

                    setSearchLocation(add);
                }

                break;

            case R.id.btn_current_loc:
			/*
			 * CameraPosition cameraPosition = new CameraPosition.Builder()
			 * .target(new LatLng(mLat, mLong)).zoom(11).build();
			 * googleMap.animateCamera(CameraUpdateFactory
			 * .newCameraPosition(cameraPosition));
			 */
                try {

                    GPSTracker mGPS = new GPSTracker(this);

                    if (mGPS.canGetLocation) {

                        mLat = mGPS.getLatitude();
                        mLong = mGPS.getLongitude();
                    }
                    myLat = "" + mLat;
                    mylong = "" + mLong;


                    isFirtLoad = true;
                    googleMap.clear();
                    googleMap = null;
                    initilizeMap();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.img_fav:
                // pic_lat,pic_long,location;
		/*	if (!location.equalsIgnoreCase("")) {
				Intent ifav = new Intent(MyMapPicDestination.this,
						FavoriteLocationActivity.class);

				ifav.putExtra("latitude", pic_lat);
				ifav.putExtra("longitude", pic_long);
				ifav.putExtra("location", location);
				startActivityForResult(ifav, 515);

			} else {

			}*/

                break;

            default:
                break;
        }
    }

    public void setSearchLocation(String address) {
        double lat = 0.0d;
        double longitu = 0.0d;
        Geocoder geocoder;

        isFirtLoad = false;

        googleMap.clear();
        googleMap = null;
        initilizeMap();
        // setMarker();

        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(address
                    .trim().toLowerCase(), 1);
            if (addresses.size() > 0) {
                lat = addresses.get(0).getLatitude();
                longitu = addresses.get(0).getLongitude();
                pic_lat = String.valueOf(lat);
                pic_long = String.valueOf(longitu);

                String address1 = addresses.get(0).getAddressLine(0);
                String city1 = addresses.get(0).getAddressLine(1);
                String country1 = addresses.get(0).getAddressLine(2);
                city = addresses.get(0).getLocality();
                Log.e("selected_city", "**" + city);
                location = address1 + " : " + city1 + " : " + country1;
                addre.setText(city);
            } else {
                Toast.makeText(context, "No location found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (lat != 0.0 && longitu != 0.0) {

            MarkerOptions marker = new MarkerOptions().position(new LatLng(lat,
                    longitu));

            // if(vtype1.get(i).contains("user"))
            marker.icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.location_marker));
            // marker.title(driverDetailList.size() + 1 + "");

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(lat, longitu)).zoom(13).build();

            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            // else
            // marker.icon(BitmapDescriptorFactory
            // .defaultMarker(BitmapDescriptorFactory.HUE_RED));

            googleMap.addMarker(marker);

        }

    }

    // /
    // get favorite location task
    // ***************************************************


    // location place autocom

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=AIzaSyCaaF6XsSGcDRdVXy0akLa6t39xfBeAdKU";

            String input = "";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }


            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input + "&" + types + "&" + sensor + "&" + key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

            try {
                // Fetching the data from web service in background
                data = downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[]{"description"};
            int[] to = new int[]{R.id.text1};

            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, R.layout.autocomplete_row, from, to);

            // Setting the adapter
            edit_address.setAdapter(adapter);
        }
    }

}
