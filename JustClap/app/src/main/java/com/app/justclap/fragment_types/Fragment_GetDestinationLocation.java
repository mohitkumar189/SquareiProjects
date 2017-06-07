package com.app.justclap.fragment_types;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.activities.PickLocation;
import com.app.justclap.activities.PlaceJSONParser;
import com.app.justclap.R;

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
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.app.justclap.adapters.AdaptercheckboxQues;
import com.app.justclap.interfaces.QuestionDatailInterface;
import com.app.justclap.models.ModelGetDestinationLocationType;
import com.app.justclap.models.ModelGetLocationType;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.utils.GPSTracker;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_GetDestinationLocation extends Fragment {

    AdaptercheckboxQues adaptercheckboxQues;
    TextView text_ques, text_loc, text_getLocation;
    AutoCompleteTextView edit_address;
    PlacesTask placesTask;
    ParserTask parserTask;
    ModelGetDestinationLocationType model;
    int position;
    ImageView img_find_loc;
    QuestionDatailInterface questionDatailInterface;
    Context context;
    String lat = "0.0", lng = "0.0";
    ArrayList<ModelGetLocationType> quesList;
    Bundle b;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GPSTracker gps = new GPSTracker(context);
        if (gps.canGetLocation) {
            lat = "" + gps.getLatitude();
            lng = "" + gps.getLongitude();
        }
        b = getArguments();

        position = b.getInt("position");
        quesList = new ArrayList<>();

        model = (ModelGetDestinationLocationType) questionDatailInterface.getPageDataModel(position);
        text_ques.setText(model.getQuestionText());

        edit_address.setText(model.getAddress().toString());
        Log.e("getQuestionText", "**" + model.getQuestionText());

        setListener();
    }


    @Override
    public void onPause() {
        super.onPause();
        model.setQuestionText(model.getQuestionText());
        model.setAddress(edit_address.getText().toString());
        model.setLatitude(lat);
        model.setLongitude(lng);
        edit_address.setText(model.getAddress().toString());
        questionDatailInterface.setPageDataModel(position, model);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        questionDatailInterface = (QuestionDatailInterface) activity;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        View viewCategory = inflater.inflate(R.layout.fragment_getdestlocation, container, false);
        context = getActivity();
        img_find_loc = (ImageView) viewCategory.findViewById(R.id.img_find_loc);
        text_ques = (TextView) viewCategory.findViewById(R.id.text_ques);
        text_loc = (TextView) viewCategory.findViewById(R.id.text_loc);
        text_getLocation = (TextView) viewCategory.findViewById(R.id.text_getLocation);
        edit_address = (AutoCompleteTextView) viewCategory.findViewById(R.id.edit_location);


        return viewCategory;
    }

    private void setListener() {


        img_find_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), PickLocation.class);

                i.putExtra("lat", lat);
                i.putExtra("lng", lng);
                startActivityForResult(i, 511);

            }
        });

        text_getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
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
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
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
                    setCurrentLocation();
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
                                // setSearchLocation(add);
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
                setSearchLocation(s.toString());
                model.setAddress(s.toString());
                questionDatailInterface.setPageDataModel(position, model);
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


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 511 && resultCode == 512) {

            data.getExtras().getString("location");
            Log.e("getExtras", "*" + data.getExtras().getString("location"));

            lat = data.getStringExtra("latitude");
            lng = data.getStringExtra("longitude");

            if (!data.getExtras().getString("location").equalsIgnoreCase("")) {
                edit_address.setText(data.getExtras().getString("location"));
                model.setAddress(data.getExtras().getString("location"));
                questionDatailInterface.setPageDataModel(position, model);

            }

        }

    }

    public void setSearchLocation(String address) {
        double lat1 = 0.0d;
        double longitu = 0.0d;
        Geocoder geocoder;


        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(address
                    .trim().toLowerCase(), 1);
            if (addresses.size() > 0) {
                lat1 = addresses.get(0).getLatitude();
                longitu = addresses.get(0).getLongitude();
                lat = String.valueOf(lat1);
                lng = String.valueOf(longitu);
                Log.e("latnew", lat);
                Log.e("lngnew", lng);
                model.setLatitude(lat);
                model.setLongitude(lng);
                questionDatailInterface.setPageDataModel(position, model);


            } else {
                // Toast.makeText(context, "No location found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            Log.d("Exception url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


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

            try {
                String[] from = new String[]{"description"};
                int[] to = new int[]{R.id.text1};

                // Creating a SimpleAdapter for the AutoCompleteTextView
                SimpleAdapter adapter = new SimpleAdapter(getActivity(), result, R.layout.autocomplete_row, from, to);

                // Setting the adapter
                edit_address.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void checkGps() {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
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
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
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

                }
            });
            dialog.show();
        }


    }

    private void setCurrentLocation() {

        // TODO Auto-generated method stub
        GPSTracker gps = new GPSTracker(context);
        if (gps.canGetLocation) {
            lat = "" + gps.getLatitude();
            lng = "" + gps.getLongitude();
            model.setLatitude(lat);
            model.setLongitude(lng);
            questionDatailInterface.setPageDataModel(position, model);
            GetAddressFromURLTask1 task1 = new GetAddressFromURLTask1();
            task1.execute(new String[]{lat, lng});

        } else {
            /*Toast.makeText(context, "Could not found lat long",
                    Toast.LENGTH_LONG).show();*/
        }

    }

    private class GetAddressFromURLTask1 extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... urls) {

            String response = "";
            HttpResponse response2 = null;
            StringBuilder stringBuilder = new StringBuilder();

            try {

                HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/geocode/json?latlng=" + urls[0] + "," + urls[1] + "&ln=en");

                HttpClient client = new DefaultHttpClient();
                Log.e("Url ", "http://maps.google.com/maps/api/geocode/json?ln=en&latlng=" + urls[0] + "," + urls[1]);


                try {
                    response2 = client.execute(httpGet);

                    HttpEntity entity = response2.getEntity();

                    char[] buffer = new char[2048];
                    Reader reader = new InputStreamReader(entity.getContent(), "UTF-8");

                    while (true) {
                        int n = reader.read(buffer);
                        if (n < 0) {
                            break;
                        }
                        stringBuilder.append(buffer, 0, n);
                    }

                    Log.e("Url response1", stringBuilder.toString());

                } catch (ClientProtocolException e) {
                } catch (IOException e) {
                }

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject = new JSONObject(stringBuilder.toString());

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (Exception e) {

                e.printStackTrace();
                Log.e("Error 2 :>>", "error in doINBackground OUTER");
                //infowindow.setText("Error in connecting to Google Server... try again later");

            }

            return stringBuilder.toString();
            //return jsonObject;
        }


        protected void onPostExecute(String result) {

            try {
                if (result != null) {


                    //result=	Html.fromHtml(result).toString();
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray resultsObject = jsonObject.getJSONArray("results");
                    JSONObject formattedAddress = (JSONObject) resultsObject.get(0);
                    String formatted_address = formattedAddress.getString("formatted_address");

                    Log.e("formatted Adss from>>", formatted_address);

                    edit_address.setText(formatted_address);

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


    }


}
