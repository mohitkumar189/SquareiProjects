package com.app.justclap.fragment_types;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.R;
import com.app.justclap.activities.DirectionsJSONParser;
import com.app.justclap.activities.PlaceJSONParser;
import com.app.justclap.interfaces.QuestionDatailInterface;
import com.app.justclap.models.ModelRideLocationType;
import com.app.justclap.models.ModelSelectDateType;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.utils.GPSTracker;
import com.google.android.gms.ads.internal.overlay.zzo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class FragmentShareRideType extends Fragment implements OnMapReadyCallback, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    private GoogleMap mMap;
    ArrayList<LatLng> markerPoints;
    Context context;
    ImageView search_img_source, search_img_dest;
    AutoCompleteTextView find_address_source, find_address_dest;
    PlacesTask placesTask;
    ParserTaskSource parserTask;
    boolean isSourceSearch = true;
    GPSTracker gTraker;
    double mLat = 0.0, mLong = 0.0;
    RelativeLayout header_dest;
    Bundle b;
    TextView text_ques;
    ImageView image_date;
    QuestionDatailInterface questionDatailInterface;
    ModelRideLocationType model;
    int position;
    TextView text_date;
    FloatingActionButton floatingDate;
    SupportMapFragment supportMapFragment = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        View view_about = inflater.inflate(R.layout.activity_route_locations, container, false);
        context = getActivity();
        markerPoints = new ArrayList<LatLng>();
        b = getArguments();

        return view_about;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        search_img_source = (ImageView) view.findViewById(R.id.search_img_source);
        search_img_dest = (ImageView) view.findViewById(R.id.search_img_dest);
        image_date = (ImageView) view.findViewById(R.id.image_date);
        text_date = (TextView) view.findViewById(R.id.text_date);
        markerPoints = new ArrayList<LatLng>();
        header_dest = (RelativeLayout) view.findViewById(R.id.header_dest);
        floatingDate = (FloatingActionButton) view.findViewById(R.id.floatingDate);
        find_address_source = (AutoCompleteTextView) view.findViewById(R.id.find_address_source);
        find_address_dest = (AutoCompleteTextView) view.findViewById(R.id.find_address_dest);
        try {

         /*   if (Build.VERSION.SDK_INT < 21) {
                supportMapFragment = (SupportMapFragment) getActivity()
                        .getSupportFragmentManager().findFragmentById(R.id.mymap);
            } else {*/
            supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mymap);
            //  }
            supportMapFragment.getMapAsync(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        text_ques = (TextView) view.findViewById(R.id.text_ques);
        setListener();
        position = b.getInt("position");
        model = (ModelRideLocationType) questionDatailInterface.getPageDataModel(position);
        text_ques.setText(model.getQuestionText());

        find_address_source.setImeActionLabel("source", EditorInfo.IME_ACTION_SEARCH);
        find_address_dest.setImeActionLabel("dest", EditorInfo.IME_ACTION_SEARCH);

        find_address_source.setText(model.getSrcaddress());
        find_address_dest.setText(model.getDestaddress());

        gTraker = new GPSTracker(context);

        if (gTraker.canGetLocation()) {

            mLat = gTraker.getLatitude();
            mLong = gTraker.getLongitude();

            Log.e("mLat", "" + mLat);
            Log.e("mLong", "" + mLong);

        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        questionDatailInterface = (QuestionDatailInterface) activity;

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            if (supportMapFragment != null) {
                FragmentManager fM = getFragmentManager();
                fM.beginTransaction().remove(supportMapFragment).commit();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setListener() {

        image_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Calendar now = Calendar.getInstance();

                    DatePickerDialog dpd = DatePickerDialog.newInstance(FragmentShareRideType.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                    dpd.setMinDate(now);

                    dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        floatingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Calendar now = Calendar.getInstance();

                    DatePickerDialog dpd = DatePickerDialog.newInstance(FragmentShareRideType.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                    dpd.setMinDate(now);

                    dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        find_address_source.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                // TODO Auto-generated method stub
                if (v.getId() == R.id.find_address_source) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                        AppUtils.onKeyBoardDown(context);
                        String add = find_address_source.getText().toString().trim();
                        if (add.equalsIgnoreCase("")) {
                            Toast.makeText(context, "please enter address", Toast.LENGTH_SHORT).show();
                        } else {
                            isSourceSearch = true;
                            find_address_dest.setText("");
                            header_dest.setVisibility(View.VISIBLE);
                            setSearchLocation(add);
                        }

                        return true;
                    }
                }
                return false;

            }
        });

        find_address_source.setThreshold(1);
        find_address_source.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    isSourceSearch = true;
                    placesTask = new PlacesTask();
                    placesTask.execute(s.toString());

                    model.setSrcaddress(s + "");
                    questionDatailInterface.setPageDataModel(position, model);

                }
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
        find_address_dest.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                // TODO Auto-generated method stub
                if (v.getId() == R.id.find_address_dest) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                        AppUtils.onKeyBoardDown(context);
                        String add = find_address_dest.getText().toString().trim();
                        if (add.equalsIgnoreCase("")) {
                            Toast.makeText(context, "please enter address", Toast.LENGTH_SHORT).show();
                        } else {
                            isSourceSearch = false;

                            setSearchLocation(add);
                        }

                        return true;
                    }
                }
                return false;

            }
        });


        find_address_dest.setThreshold(1);
        find_address_dest.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (!find_address_source.getText().toString().equalsIgnoreCase("")) {
                        isSourceSearch = false;
                        placesTask = new PlacesTask();
                        placesTask.execute(s.toString());

                        model.setDestaddress(s + "");
                        questionDatailInterface.setPageDataModel(position, model);
                    } else {

                        Toast.makeText(context, "Please enter source.", Toast.LENGTH_SHORT).show();

                    }
                }
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


        search_img_source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppUtils.onKeyBoardDown(context);
                String add = find_address_source.getText().toString().trim();
                if (add.equalsIgnoreCase("")) {
                    Toast.makeText(context, "please enter address", Toast.LENGTH_SHORT).show();
                } else {
                    isSourceSearch = true;

                    find_address_dest.setText("");
                    header_dest.setVisibility(View.VISIBLE);
                    setSearchLocation(add);
                }


            }
        });

        search_img_dest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppUtils.onKeyBoardDown(context);
                String add = find_address_dest.getText().toString().trim();
                if (add.equalsIgnoreCase("")) {
                    Toast.makeText(context, "please enter address", Toast.LENGTH_SHORT).show();
                } else {
                    isSourceSearch = false;

                    setSearchLocation(add);
                }


            }
        });


    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        int month = monthOfYear + 1;
        model.setSelectedDate(dayOfMonth + "/" + month + "/" + year);
        questionDatailInterface.setPageDataModel(position, model);
        text_date.setText(dayOfMonth + "/" + month + "/" + year);

        TimePickerDialog time = TimePickerDialog.newInstance(FragmentShareRideType.this, 60, 60, true);
        time.show(getActivity().getFragmentManager(), "Timepickerdialog");

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

        model.setSelectedTime(hourOfDay + ":" + minute);
        questionDatailInterface.setPageDataModel(position, model);
        text_date.setText(text_date.getText().toString() + ", " + hourOfDay + ":" + minute);
    }

    public void setSearchLocation(String address) {
        double lat = 0.0d;
        double longitu = 0.0d;
        Geocoder geocoder;

        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(address
                    .trim().toLowerCase(), 1);
            if (addresses.size() > 0) {
                lat = addresses.get(0).getLatitude();
                longitu = addresses.get(0).getLongitude();
             /*   pic_lat = String.valueOf(lat);
                pic_long = String.valueOf(longitu);*/

                String address1 = addresses.get(0).getAddressLine(0);
                String city1 = addresses.get(0).getAddressLine(1);
                String country1 = addresses.get(0).getAddressLine(2);
             /*   location = address1 + " : " + city1 + " : " + country1;
                addre.setText(location);*/
                if (markerPoints.size() > 1) {

                    if (isSourceSearch) {

                        markerPoints.clear();
                        mMap.clear();
                    } else {
                        markerPoints.remove(1);
                        mMap.clear();
                    }
                }

                if (isSourceSearch) {
                    markerPoints.clear();
                    mMap.clear();
                }

                // Adding new item to the ArrayList
                markerPoints.add(new LatLng(lat, longitu));

                if (isSourceSearch) {

                    model.setSrclatitude(lat + "");
                    model.setSrclongitude(longitu + "");
                    questionDatailInterface.setPageDataModel(position, model);

                } else {
                    model.setDestlatitude(lat + "");
                    model.setDestlongitude(longitu + "");
                    questionDatailInterface.setPageDataModel(position, model);

                }


                /**
                 * For the start location, the color of marker is GREEN and
                 * for the end location, the color of marker is RED.
                 */
                Log.e("markerSize", "*" + markerPoints.size());
                if (markerPoints.size() == 1) {
                    mMap.addMarker(new MarkerOptions().position(markerPoints.get(0)).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker)));
                } else if (markerPoints.size() == 2) {
                    mMap.addMarker(new MarkerOptions().position(markerPoints.get(0)).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker)));

                    mMap.addMarker(new MarkerOptions().position(markerPoints.get(1)).icon(BitmapDescriptorFactory.fromResource(R.drawable.loaction_marker_dest)));
                }


                // Checks, whether start and end locations are captured


                if (markerPoints.size() >= 2) {
                    LatLng origin = markerPoints.get(0);
                    LatLng dest = markerPoints.get(1);

                    // Getting URL to the Google Directions API
                    String url = getDirectionsUrl(origin, dest);

                    DownloadTask downloadTask = new DownloadTask();

                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);
                }


            } else {
                Toast.makeText(context, "No location found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (lat != 0.0 && longitu != 0.0) {

            MarkerOptions marker = new MarkerOptions().position(new LatLng(lat,
                    longitu));

            // if(vtype1.get(i).contains("user"))
         /*   marker.icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));*/
            // marker.title(driverDetailList.size() + 1 + "");

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(lat, longitu)).zoom(10).build();

            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            // else
            // marker.icon(BitmapDescriptorFactory
            // .defaultMarker(BitmapDescriptorFactory.HUE_RED));

            // mMap.addMarker(marker);

        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        CameraPosition cameraPosition;
        if (mMap != null) {

            Log.e("onMapReady", "onMapReady");

            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(mLat, mLong)).zoom(10).build();

            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            find_address_dest.setText(model.getDestaddress());
            if (!find_address_source.getText().toString().equalsIgnoreCase("")) {
                header_dest.setVisibility(View.VISIBLE);

                Log.e("addressNotBlank", "Setdata");
                Log.e("srcLat", "*" + model.getSrclatitude());
                Log.e("destAddress", "*" + model.getDestaddress());
                Log.e("srclng", "*" + model.getSrclongitude());
                Log.e("destLat", "*" + model.getDestlatitude());
                Log.e("destLng", "*" + model.getDestlongitude());


                if (!model.getSrclatitude().equalsIgnoreCase("") && !model.getSrclongitude().equalsIgnoreCase("") &&
                        !model.getDestlongitude().equalsIgnoreCase("")
                        && !model.getDestlatitude().equalsIgnoreCase("")) {


                    markerPoints.add(new LatLng(Double.parseDouble(model.getSrclatitude()), Double.parseDouble(model.getSrclongitude())));

                    markerPoints.add(new LatLng(Double.parseDouble(model.getDestlatitude()), Double.parseDouble(model.getDestlongitude())));

                    mMap.addMarker(new MarkerOptions().position(markerPoints.get(0)).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker)));

                    mMap.addMarker(new MarkerOptions().position(markerPoints.get(1)).icon(BitmapDescriptorFactory.fromResource(R.drawable.loaction_marker_dest)));

                    cameraPosition = new CameraPosition.Builder()
                            .target(markerPoints.get(0)).zoom(10).build();

                    mMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));


                    if (markerPoints.size() >= 2) {
                        LatLng origin = markerPoints.get(0);
                        LatLng dest = markerPoints.get(1);

                        // Getting URL to the Google Directions API
                        String url = getDirectionsUrl(origin, dest);

                        DownloadTask downloadTask = new DownloadTask();

                        // Start downloading json data from Google Directions API
                        downloadTask.execute(url);
                    }

                }


            }


            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng point) {

                    // Already two locations
                    if (markerPoints.size() > 1) {
                        markerPoints.clear();
                        mMap.clear();
                        find_address_dest.setText("");
                        find_address_source.setText("");
                    }

                    // Adding new item to the ArrayList
                    markerPoints.add(point);

                    // Creating MarkerOptions
                    MarkerOptions options = new MarkerOptions();

                    // Setting the position of the marker
                    options.position(point);

                    /**
                     * For the start location, the color of marker is GREEN and
                     * for the end location, the color of marker is RED.
                     */

                    if (markerPoints.size() == 1) {
                        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));

                        try {
                            Geocoder geocoder;
                            List<Address> addresses = new ArrayList<Address>();
                            geocoder = new Geocoder(context, Locale.getDefault());

                            addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);

                            String address = addresses.get(0).getAddressLine(0);
                            String city = addresses.get(0).getAddressLine(1);
                            String country = addresses.get(0).getAddressLine(2);
                            String location = address + " : " + city + " : " + country;
                            find_address_source.setText(location);

                            header_dest.setVisibility(View.VISIBLE);

                            model.setSrclatitude(point.latitude + "");
                            model.setSrclongitude(point.longitude + "");
                            questionDatailInterface.setPageDataModel(position, model);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else if (markerPoints.size() == 2) {


                        try {
                            Geocoder geocoder;
                            List<Address> addresses = new ArrayList<Address>();
                            geocoder = new Geocoder(context, Locale.getDefault());

                            addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);

                            String address = addresses.get(0).getAddressLine(0);
                            String city = addresses.get(0).getAddressLine(1);
                            String country = addresses.get(0).getAddressLine(2);
                            String location = address + " : " + city + " : " + country;
                            find_address_dest.setText(location);
                            model.setDestlatitude(point.latitude + "");
                            model.setDestlongitude(point.longitude + "");
                            questionDatailInterface.setPageDataModel(position, model);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.loaction_marker_dest));
                    }


                    // Add new marker to the Google Map Android API V2
                    mMap.addMarker(options);

                    // Checks, whether start and end locations are captured
                    if (markerPoints.size() >= 2) {
                        LatLng origin = markerPoints.get(0);
                        LatLng dest = markerPoints.get(1);

                        // Getting URL to the Google Directions API
                        String url = getDirectionsUrl(origin, dest);

                        DownloadTask downloadTask = new DownloadTask();

                        // Start downloading json data from Google Directions API
                        downloadTask.execute(url);
                    }

                }
            });
        }


    }// Fetches all places from GooglePlaces AutoComplete Web Service


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
            parserTask = new ParserTaskSource();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

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
            Log.d("Exception nloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(3);
                lineOptions.color(Color.RED);

            }

            // Drawing polyline in the Google Map for the i-th route

            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            }
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTaskSource extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

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
            SimpleAdapter adapter = new SimpleAdapter(getActivity(), result, R.layout.autocomplete_row, from, to);

            // Setting the adapter
            if (isSourceSearch) {
                find_address_source.setAdapter(adapter);
            } else {
                find_address_dest.setAdapter(adapter);
            }
        }
    }


}
