package com.app.justclap.vendordetail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.activities.PickLocation;
import com.app.justclap.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.app.justclap.adapters.AdapterSelectCategory;
import com.app.justclap.adapters.CitySpinnerAdapter;
import com.app.justclap.adapters.ServiceSpinnerAdapter;
import com.app.justclap.asyntask.AsyncPostDataResponse;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.utils.GPSTracker;

public class VendorEdit_BusinessDetail extends AppCompatActivity implements OnCustomItemClicListener, ListenerPostData {


    RelativeLayout rl_bottom;
    Context context;
    Button btnLocation;
    TextView text_seekbarprogress;
    AdapterSelectCategory adapterSelectCategory;
    ModelService serviceDetail;
    ArrayList<ModelService> service_list;
    ArrayList<String> services_array = new ArrayList<>();
    ArrayList<String> service_id = new ArrayList<>();
    ArrayList<String> city_id = new ArrayList<>();
    ArrayList<String> city_list = new ArrayList<>();
    SeekBar seekBar;
    RelativeLayout rl_main_layout, rl_network;
    ArrayList<String> service_name = new ArrayList<>();
    Spinner spinner_select_category, spinner_select_city;
    Toolbar toolbar;
    ArrayList<String> is_uni_directional = new ArrayList<>();
    EditText text_business_name, text_website_url, text_facebook_url, text_business_description;
    String user_type = "1", latitude = "", longitude = "";
    String lat = "0.0", lng = "0.0", cityId = "", serviceid = "";
    private BroadcastReceiver broadcastReceiver;
    CitySpinnerAdapter cityAdapter;
    EditText text_service;
    ServiceSpinnerAdapter serviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_edit_businessinfo);

        context = this;
        init();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(" Edit Business info");

        setListener();
        GPSTracker gps = new GPSTracker(context);
        if (gps.canGetLocation) {
            latitude = "" + gps.getLatitude();
            longitude = "" + gps.getLongitude();
        }

        Intent in = getIntent();
        text_business_name.setText(in.getExtras().getString("name"));
        seekBar.setProgress(Integer.parseInt(in.getExtras().getString("distance")));
        text_website_url.setText(in.getExtras().getString("web"));
        text_facebook_url.setText(in.getExtras().getString("fb"));
        text_business_description.setText(in.getExtras().getString("desc"));
        btnLocation.setText(in.getExtras().getString("address"));
        text_seekbarprogress.setText(in.getExtras().getString("distance") + "Km");
        // in.putExtra("service", text_services.getText().toString());
        //   cityId = in.getExtras().getString("city");
        serviceid = in.getExtras().getString("service");
        text_service.setText(in.getExtras().getString("servicename"));
        Log.e("serviceid", serviceid);
        text_seekbarprogress.setText(seekBar.getProgress() + " Km");
        //   setServices();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }

  /*  private void setServices() {

        try {

            if (AppUtils.isNetworkAvailable(context)) {

                new AsyncPostDataResponse(
                        VendorEdit_BusinessDetail.this,
                        2,
                        null,
                        getString(R.string.url_base_new)
                                + getString(R.string.vendorSignupData));
            } else {
                rl_main_layout.setVisibility(View.GONE);
                rl_network.setVisibility(View.VISIBLE);

                //  Toast.makeText(getApplicationContext(), R.string.message_network_problem, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/

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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        text_seekbarprogress = (TextView) findViewById(R.id.text_seekbarprogress);
        seekBar = (SeekBar) findViewById(R.id.seekbar);

        btnLocation = (Button) findViewById(R.id.btn_pickLocation);
        rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        text_business_description = (EditText) findViewById(R.id.text_business_description);
        text_business_name = (EditText) findViewById(R.id.text_business_name);
        text_website_url = (EditText) findViewById(R.id.text_website_url);
        text_facebook_url = (EditText) findViewById(R.id.text_facebook_url);
        spinner_select_city = (Spinner) findViewById(R.id.spinner_select_city);
        text_service = (EditText) findViewById(R.id.text_service);
        text_service.setEnabled(false);

    }

    private void setListener() {


        btnLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                GPSTracker gps = new GPSTracker(context);

                if (gps.isGPSEnabled) {

                    Intent i = new Intent(context, PickLocation.class);
                    i.putExtra("lat", latitude);
                    i.putExtra("lng", longitude);
                    startActivityForResult(i, 511);
                } else {
                    gps.showSettingsAlert();
                }
            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO bind to drawer with... injection?
                finish();
                overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);

            }

        });


        rl_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (isValidLoginDetails()) {
                        if (AppUtils.isNetworkAvailable(context)) {

                            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                                    2);


                            nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "distance", text_seekbarprogress.getText().toString()));
                            nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "websiteUrl", text_website_url.getText().toString()));

                            nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "fbUrl", text_facebook_url.getText().toString()));

                         /*   nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "cityID", city_id.get(spinner_select_city.getSelectedItemPosition())));
*/

                            if (AppUtils.getisunidirectional(context).equalsIgnoreCase("1")) {

                                nameValuePairs
                                        .add(new BasicNameValuePair(
                                                "services", serviceid));

                                nameValuePairs
                                        .add(new BasicNameValuePair(
                                                "categories", "-1"));


                            } else if (AppUtils.getisunidirectional(context).equalsIgnoreCase("2")) {

                                nameValuePairs
                                        .add(new BasicNameValuePair(
                                                "services", "-1"));

                                nameValuePairs
                                        .add(new BasicNameValuePair(
                                                "categories", serviceid));

                            }

                            nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "businessName", text_business_name.getText().toString()));
                            nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "address", btnLocation.getText().toString()));


                            nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "businessDesc", text_business_description.getText().toString()));
                            nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "vendorID", AppUtils.getvendorId(context)));

                            new AsyncPostDataResponse(VendorEdit_BusinessDetail.this, 1, nameValuePairs,
                                    getString(R.string.url_base_new)
                                            + getString(R.string.updateVendorBussinessDetails));

                        } else {
                            rl_main_layout.setVisibility(View.GONE);
                            rl_network.setVisibility(View.VISIBLE);

                            //  Toast.makeText(getApplicationContext(), R.string.message_network_problem, Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                text_seekbarprogress.setText(progress + " Km");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    private boolean isValidLoginDetails() {
        boolean isValidLoginDetails = true;


        String business = text_business_name.getText().toString();
        String bdesc = text_business_description.getText().toString();
        String seek = text_seekbarprogress.getText().toString();

        String website = text_website_url.getText().toString();
        String facebook = text_facebook_url.getText().toString();


        if (!business.equalsIgnoreCase("") && !bdesc.equalsIgnoreCase("")
                && !seek.equalsIgnoreCase("")) {

           /* if (!AppUtils.isEmailValid(emailaddress.trim())) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.validEmail, Toast.LENGTH_SHORT).show();
            } else if (mobileno.length() < 10) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.mobileno_Length, Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.passwordLength, Toast.LENGTH_SHORT).show();
            } else {*/
            isValidLoginDetails = true;
            // }

        } else {
            if (business.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.enter_business, Toast.LENGTH_SHORT).show();
            } else if (bdesc.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.enter_business_desc, Toast.LENGTH_SHORT).show();
            } else if (seek.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), "Please select distance", Toast.LENGTH_SHORT).show();
            }
        }

        return isValidLoginDetails;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {

            if (requestCode == 511 && resultCode == 512) {

                btnLocation.setText(data.getStringExtra("location"));
                latitude = data.getStringExtra("latitude");
                longitude = data.getStringExtra("longitude");


            }
        } catch (Exception e) {
            // Toast.makeText(getBaseContext(), "Exception "+e.getMessage(),
            // 5000).show();
            e.printStackTrace();
        }
    }

    private void setCurrentLocation() {

        // TODO Auto-generated method stub
        GPSTracker gps = new GPSTracker(context);
        if (gps.canGetLocation) {
            lat = "" + gps.getLatitude();
            lng = "" + gps.getLongitude();

            GetAddressFromURLTask1 task1 = new GetAddressFromURLTask1();
            task1.execute(new String[]{lat, lng});

        } else {
            /*Toast.makeText(context, "Could not found lat long",
                    Toast.LENGTH_LONG).show();*/
        }

    }


    @Override
    public void onItemClickListener(int position, int flag) {

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
                    Toast.makeText(context, commandResult.getString("message"),
                            Toast.LENGTH_SHORT).show();


                    Intent in = new Intent();
                    in.putExtra("businessName", text_business_name.getText().toString());
                    in.putExtra("distance", text_seekbarprogress.getText().toString());
                    in.putExtra("fbUrl", text_facebook_url.getText().toString());
                    in.putExtra("websiteUrl", text_website_url.getText().toString());
                    in.putExtra("businessName", text_business_name.getText().toString());
                    in.putExtra("address", btnLocation.getText().toString());
                    in.putExtra("service", text_service.getText().toString());
                    in.putExtra("businessDesc", text_business_description.getText().toString());
                    // in.putExtra("city", spinner_select_city.getSelectedItem().toString());
                    in.putExtra("serviceid", serviceid);
                    //  in.putExtra("cityid", city_id.get(spinner_select_city.getSelectedItemPosition()));
                    setResult(513, in);
                    finish();

                } else {
                    Toast.makeText(context, commandResult.getString("message"),
                            Toast.LENGTH_SHORT).show();

                }
            }
        /*    if (position == 2) {

                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {


                    JSONObject data = commandResult.getJSONObject("data");


                    JSONArray services = data.getJSONArray("services");

                    for (int i = 0; i < services.length(); i++) {

                        JSONObject ob = services.getJSONObject(i);

                        service_name.add(ob.getString("serviceName"));
                        service_id.add(ob.getString("serviceID"));
                        is_uni_directional.add(ob.getString("is_uni_directional"));

                    }
                    service_name.add("Select Service");
                    serviceAdapter = new ServiceSpinnerAdapter(getApplicationContext(), service_name);
                    spinner_select_category.setAdapter(serviceAdapter);
                    spinner_select_category.setSelection(serviceAdapter.getCount());

                    for (int j = 0; j < service_id.size(); j++) {
                        Log.e("service_idlist", service_id.get(j));
                        if (service_id.get(j).equalsIgnoreCase(serviceid)) {
                            Log.e("selected_service", service_id.get(j));
                            spinner_select_category.setSelection(j);
                            break;
                        } else {
                            spinner_select_category.setSelection(serviceAdapter.getCount());
                        }
                    }

                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);

                  *//*  JSONArray cities = data.getJSONArray("city_services");

                    for (int i = 0; i < cities.length(); i++) {

                        JSONObject ob = cities.getJSONObject(i);

                        city_list.add(ob.getString("cityName"));
                        city_id.add(ob.getString("cityId"));
                        services_array.add(ob.getJSONArray("services").toString());

                    }
                    city_list.add("Select City");
                    cityAdapter = new CitySpinnerAdapter(getApplicationContext(), city_list);
                    spinner_select_city.setAdapter(cityAdapter);
                    spinner_select_city.setSelection(cityAdapter.getCount());


                    for (int i = 0; i < city_id.size(); i++) {
                        if (city_id.get(i).equalsIgnoreCase(cityId)) {
                            spinner_select_city.setSelection(i);
                            String serv = services_array.get(i);
                            JSONArray services = new JSONArray(serv);
                            service_name = new ArrayList<>();
                            service_id = new ArrayList<>();

                            for (int j = 0; j < services.length(); j++) {

                                JSONObject ob1 = services.getJSONObject(j);

                                service_name.add(ob1.getString("serviceName"));
                                service_id.add(ob1.getString("serviceID"));

                            }
                            service_name.add("Select Service");

                            serviceAdapter = new ServiceSpinnerAdapter(getApplicationContext(), service_name);
                            spinner_select_category.setAdapter(serviceAdapter);

                            for (int k = 0; k < service_id.size(); k++) {
                                Log.e("service_idlist", service_id.get(k));
                                if (service_id.get(k).equalsIgnoreCase(serviceid)) {
                                    Log.e("selected_service", service_id.get(k));
                                    spinner_select_category.setSelection(k);
                                    serviceAdapter.notifyDataSetChanged();
                                    break;
                                } else {
                                    spinner_select_category.setSelection(serviceAdapter.getCount());
                                }
                            }

                            break;
                        } else {

                            spinner_select_city.setSelection(cityAdapter.getCount());
                        }
                    }*//*


                }


            }
*/
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostRequestFailed(int position, String response) {

        AppUtils.showCustomAlert(VendorEdit_BusinessDetail.this, getResources().getString(R.string.problem_server));

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

                    btnLocation.setText(formatted_address);

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);
    }
}
