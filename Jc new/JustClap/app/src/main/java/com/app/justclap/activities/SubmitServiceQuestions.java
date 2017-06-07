package com.app.justclap.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.app.justclap.R;
import com.app.justclap.adapter.AdapterTabs;
import com.app.justclap.interfaces.ApiResponse;
import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.interfaces.QuestionDatailInterface;
import com.app.justclap.models.ModelAddCityType;
import com.app.justclap.models.ModelAddressType;
import com.app.justclap.models.ModelJobType;
import com.app.justclap.models.ModelCheckboxType;
import com.app.justclap.models.ModelDescriptionType;
import com.app.justclap.models.ModelGetDestinationLocationType;
import com.app.justclap.models.ModelGetLocationAddressType;
import com.app.justclap.models.ModelGetLocationType;
import com.app.justclap.models.ModelQuestionList;
import com.app.justclap.models.ModelRadioType;
import com.app.justclap.models.ModelRideLocationType;
import com.app.justclap.models.ModelSelectDateType;
import com.app.justclap.models.ModelSelectDayType;
import com.app.justclap.models.ModelSelectTimeType;
import com.app.justclap.models.ModelSkillType;
import com.app.justclap.models.ModelTextAreaType;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.utils.GPSTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class SubmitServiceQuestions extends AppCompatActivity implements OnCustomItemClicListener, QuestionDatailInterface, ApiResponse {

    private AdapterTabs tabAdapter;
    RelativeLayout rl_bottom;
    Context context;
    ConnectionDetector cd;
    ViewPager pagertab;
    String questionArray = "", serviceId = "", servicename = "", vendor_naukri = "2", isUniDirectional = "";
    ModelQuestionList quesDetail;
    ArrayList<ModelQuestionList> quesList;
    ArrayList<ModelQuestionList> optionList;
    ArrayList<Object> questionanswer;
    public ArrayList<Object> pagerDataList;
    int pagerLastpos, pagerCurrentposition;
    String Location = "";
    Toolbar toolbar;
    boolean isfilled = false;
    private BroadcastReceiver broadcastReceiver;
    String is_naukri = "";
    double mLat = 0.0, mLong = 0.0;
    GPSTracker gTraker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_service_queries);

        context = this;
        init();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //   adapter = new SocialAuthAdapter(new ResponseListener());
        questionanswer = new ArrayList<>();
        setListener();
        Intent in = getIntent();
        getSupportActionBar().setTitle(in.getExtras().getString("servicename"));
        servicename = in.getExtras().getString("servicename");
        serviceId = in.getExtras().getString("serviceid");
        questionArray = in.getExtras().getString("queryArray");
       /* if (in.hasExtra("vendor_naukri")) {
            vendor_naukri = in.getStringExtra("vendor_naukri");
            is_naukri = in.getStringExtra("is_naukri");
        }

        Log.e("vendor_naukri", vendor_naukri + "");*/
        isUniDirectional = in.getExtras().getString("isUniDirectional");
        quesList = new ArrayList<>();
        optionList = new ArrayList<>();
        getQuestionlist();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unregisterReceiver(broadcastReceiver);

    }


    @Override
    protected void onResume() {
        super.onResume();
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


    private void init() {
        pagerDataList = new ArrayList<>();
        pagertab = (ViewPager) findViewById(R.id.pagertab);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }


    private void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO bind to drawer with... injection?

                if (pagertab.getCurrentItem() == 0) {
                    finish();
                } else {
                    pagertab.setCurrentItem(pagertab.getCurrentItem() - 1);
                }
            }

        });


        pagertab.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                pagerCurrentposition = position;

                if (positionOffsetPixels > 300) {
                    setPagerdata();
                }
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("onPageected", "onPageSelected");
                Log.e("onPageectedposition", position + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("onPageScrollStateCha", "onPageScrollStateChanged");
            }
        });

    }


    private void showConfirmtion() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                SubmitServiceQuestions.this);

        alertDialog.setTitle("Confirm to Send Request");

        alertDialog.setMessage("Are you sure you want to submit this request?");

        alertDialog.setPositiveButton("CONFIRM",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (vendor_naukri.equalsIgnoreCase("2")) {

                            if (AppUtils.getUserId(context).equalsIgnoreCase("")) {
                                Intent in = new Intent(SubmitServiceQuestions.this, Login.class);
                                in.putExtra("flagLogin", "2");
                                startActivity(in);
                            } else {
                                SubmitQuestionsArray();
                            }
                        } else if (vendor_naukri.equalsIgnoreCase("1")) {
                            SubmitQuestionsArray();
                        }

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

    private void setPagerdata() {

        if (pagerDataList.get(pagerCurrentposition) instanceof ModelAddressType) {
            ModelAddressType model = (ModelAddressType) pagerDataList.get(pagerCurrentposition);

            try {
                if (model.getAnswer().equalsIgnoreCase("")) {
                    pagertab.setCurrentItem(pagerCurrentposition);
                    Log.e("ModelAddressType", "ModelAddressTypeVibraton");
                    getVibrated();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelGetLocationType) {
            ModelGetLocationType model = (ModelGetLocationType) pagerDataList.get(pagerCurrentposition);

            Log.e("getLandmarknew", "**" + model.getAddress() + "*" + model.getLatitude());
            if (model.getLatitude().equalsIgnoreCase("") || model.getLongitude().equalsIgnoreCase("") || model.getAddress().equalsIgnoreCase("")) {
                pagertab.setCurrentItem(pagerCurrentposition);
                getVibrated();
                Log.e("ModelGetLocationType", "ModelGetLocationTypeVibraton");
            } else {


                Location = model.getAddress();
            }
        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelGetDestinationLocationType) {
            ModelGetDestinationLocationType model = (ModelGetDestinationLocationType) pagerDataList.get(pagerCurrentposition);

            Log.e("getLandmarknew", "**" + model.getAddress() + "*" + model.getLatitude());
            if (model.getLatitude().equalsIgnoreCase("") || model.getLongitude().equalsIgnoreCase("") || model.getAddress().equalsIgnoreCase("")) {
                pagertab.setCurrentItem(pagerCurrentposition);
                getVibrated();
                Log.e("ModelGetDestinationType", "ModelGetDestinaTypeVibraton");
            } else {

                Location = model.getAddress();
            }
        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelGetLocationAddressType) {
            ModelGetLocationAddressType model = (ModelGetLocationAddressType) pagerDataList.get(pagerCurrentposition);
            if (model.getLatitude().equalsIgnoreCase("") || model.getCity().equalsIgnoreCase("") || model.getLandmark().equalsIgnoreCase("") || model.getLongitude().equalsIgnoreCase("") || model.getAddress().equalsIgnoreCase("")) {
                pagertab.setCurrentItem(pagerCurrentposition);
                Log.e("getLandmark", model.getLandmark());
                getVibrated();
                Log.e("ModelGetLoAddressType", "ModelGetLocatiTypeVibraton");
            } else {

                Location = model.getAddress();
            }
            Log.e("getLandmarknew", model.getLandmark());

        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelRideLocationType) {
            ModelRideLocationType model = (ModelRideLocationType) pagerDataList.get(pagerCurrentposition);
            if (model.getDestaddress().equalsIgnoreCase("") || model.getSrcaddress().equalsIgnoreCase("") || model.getSrclatitude().equalsIgnoreCase("0.0")
                    || model.getSrclongitude().equalsIgnoreCase("0.0") || model.getDestlatitude().equalsIgnoreCase("0.0") || model.getDestlongitude().equalsIgnoreCase("0.0")
                    || model.getSelectedDate().equalsIgnoreCase("") || model.getSelectedTime().equalsIgnoreCase("")) {
                pagertab.setCurrentItem(pagerCurrentposition);
                Log.e("getdate", model.getSelectedDate());
                getVibrated();
                Log.e("ModelRideLocationType", "ModelRideLocationType");
            } else {

                //  Location = model.getAddress();
            }
            //  Log.e("getLandmarknew", model.getLandmark());

        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelTextAreaType) {
            ModelTextAreaType model = (ModelTextAreaType) pagerDataList.get(pagerCurrentposition);

            if (model.getInputAnswer().equalsIgnoreCase("")) {
                pagertab.setCurrentItem(pagerCurrentposition);
                getVibrated();
                Log.e("ModelTextAreaType", "ModelTextAreaTypeVibraton");
            }

        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelSkillType) {
            ModelSkillType model = (ModelSkillType) pagerDataList.get(pagerCurrentposition);

            if (model.getInputAnswer().equalsIgnoreCase("")) {
                pagertab.setCurrentItem(pagerCurrentposition);
                getVibrated();
                Log.e("ModelSkillType", "ModelSkillTypeeVibraton");
            } else {

            }

        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelAddCityType) {
            ModelAddCityType model = (ModelAddCityType) pagerDataList.get(pagerCurrentposition);

            if (model.getInputAnswer().equalsIgnoreCase("")) {
                pagertab.setCurrentItem(pagerCurrentposition);
                getVibrated();
                Log.e("ModelAddCityType", "ModelAddCityTypeVibraton");
            } else {

            }
        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelSelectDateType) {
            ModelSelectDateType model = (ModelSelectDateType) pagerDataList.get(pagerCurrentposition);
            if (model.getSelectedDate().equalsIgnoreCase("")) {
                pagertab.setCurrentItem(pagerCurrentposition);
                getVibrated();
                Log.e("ModelSelectDateType", "ModelSelectDateTypeVibraton");
            }

        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelSelectDayType) {
            ModelSelectDayType model = (ModelSelectDayType) pagerDataList.get(pagerCurrentposition);
            if (model.getSelectedTime().equalsIgnoreCase("")) {
                pagertab.setCurrentItem(pagerCurrentposition);
                getVibrated();
                Log.e("ModelSelectDayType", "ModelSelectDayTypeVibraton");
            }

        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelSelectTimeType) {
            ModelSelectTimeType model = (ModelSelectTimeType) pagerDataList.get(pagerCurrentposition);

            int id = model.getSelectedTimeId();

            if (model.getOptionArray().get(id).equalsIgnoreCase("")) {
                pagertab.setCurrentItem(pagerCurrentposition);
                getVibrated();
                Log.e("ModelSelectTimeType", "ModelSelectTimeTypeVibraton");
            }

        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelRadioType) {
            ModelRadioType model = (ModelRadioType) pagerDataList.get(pagerCurrentposition);

            int id = model.getSelectedOption();

            if (id == -1) {
                pagertab.setCurrentItem(pagerCurrentposition);
                getVibrated();
                Log.e("ModelRadioType", "ModelRadioTypeVibraton");
            }

        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelJobType) {
            ModelJobType model = (ModelJobType) pagerDataList.get(pagerCurrentposition);

            int id = model.getSelectedOption();

            if (id == -1) {
                pagertab.setCurrentItem(pagerCurrentposition);
                getVibrated();
            } else {

            }


        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelCheckboxType) {
            ModelCheckboxType model = (ModelCheckboxType) pagerDataList.get(pagerCurrentposition);

            ArrayList<Boolean> optionSelectionArray = new ArrayList<Boolean>();
            optionSelectionArray = model.getOptionSelectionArray();

            for (int i = 0; i < optionSelectionArray.size(); i++) {
                Log.e("optionSelectionArray", "*" + optionSelectionArray.get(i));
            }

            if (!optionSelectionArray.contains(true)) {
                pagertab.setCurrentItem(pagerCurrentposition);
                getVibrated();
            }
        }
    }

    private void setPagerdataOnnext() {

        isfilled = false;

        if (pagerDataList.get(pagerCurrentposition) instanceof ModelAddressType) {
            ModelAddressType model = (ModelAddressType) pagerDataList.get(pagerCurrentposition);
            if (model.getAnswer().equalsIgnoreCase("")) {
                pagertab.setCurrentItem(pagerCurrentposition);
                getVibrated();
                isfilled = false;

            } else {
                isfilled = true;
                pagertab.setCurrentItem(pagerCurrentposition + 1);
            }
        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelGetLocationType) {
            ModelGetLocationType model = (ModelGetLocationType) pagerDataList.get(pagerCurrentposition);

            Log.e("getLandmarknew", "**" + model.getAddress() + "*" + model.getLatitude());
            if (model.getLatitude().equalsIgnoreCase("") || model.getLongitude().equalsIgnoreCase("") || model.getAddress().equalsIgnoreCase("")) {
                pagertab.setCurrentItem(pagerCurrentposition);
                getVibrated();
                isfilled = false;
            } else {
                isfilled = true;
                Location = model.getAddress();
                pagertab.setCurrentItem(pagerCurrentposition + 1);
            }


        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelGetDestinationLocationType) {
            ModelGetDestinationLocationType model = (ModelGetDestinationLocationType) pagerDataList.get(pagerCurrentposition);

            Log.e("getLandmarknew", "**" + model.getAddress() + "*" + model.getLatitude());
            if (model.getLatitude().equalsIgnoreCase("") || model.getLongitude().equalsIgnoreCase("") || model.getAddress().equalsIgnoreCase("")) {
                pagertab.setCurrentItem(pagerCurrentposition);
                getVibrated();
                isfilled = false;
            } else {
                isfilled = true;
                Location = model.getAddress();
                pagertab.setCurrentItem(pagerCurrentposition + 1);
            }


        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelGetLocationAddressType) {
            ModelGetLocationAddressType model = (ModelGetLocationAddressType) pagerDataList.get(pagerCurrentposition);
            if (model.getLatitude().equalsIgnoreCase("") || model.getCity().equalsIgnoreCase("") || model.getLandmark().equalsIgnoreCase("") || model.getLongitude().equalsIgnoreCase("") || model.getAddress().equalsIgnoreCase("")) {
                pagertab.setCurrentItem(pagerCurrentposition);
                Log.e("getLandmark", model.getLandmark());
                getVibrated();
                isfilled = false;
            } else {
                isfilled = true;
                Location = model.getAddress();
                pagertab.setCurrentItem(pagerCurrentposition + 1);
            }
            Log.e("getLandmarknew", model.getLandmark());

        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelRideLocationType) {
            ModelRideLocationType model = (ModelRideLocationType) pagerDataList.get(pagerCurrentposition);
            if (model.getDestaddress().equalsIgnoreCase("") || model.getSrcaddress().equalsIgnoreCase("") || model.getSrclatitude().equalsIgnoreCase("0.0")
                    || model.getSrclongitude().equalsIgnoreCase("0.0") || model.getDestlatitude().equalsIgnoreCase("0.0") || model.getDestlongitude().equalsIgnoreCase("0.0")
                    || model.getSelectedDate().equalsIgnoreCase("") || model.getSelectedTime().equalsIgnoreCase("")) {
                pagertab.setCurrentItem(pagerCurrentposition);
                Log.e("getdate", model.getSelectedDate());
                getVibrated();
                isfilled = false;
                Log.e("ModelRideLocationType", "ModelRideLocationType");
            } else {
                isfilled = true;
                pagertab.setCurrentItem(pagerCurrentposition + 1);
                Location = model.getSrcaddress() + " to " + model.getDestaddress() + " on " + model.getSelectedDate() + ", " + model.getSelectedTime();

                //  Location = model.getAddress();
            }
        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelTextAreaType) {
            ModelTextAreaType model = (ModelTextAreaType) pagerDataList.get(pagerCurrentposition);

            if (model.getInputAnswer().equalsIgnoreCase("")) {
                pagertab.setCurrentItem(pagerCurrentposition);
                getVibrated();
                isfilled = false;
            } else {
                isfilled = true;
                pagertab.setCurrentItem(pagerCurrentposition + 1);
            }

        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelSkillType) {
            ModelSkillType model = (ModelSkillType) pagerDataList.get(pagerCurrentposition);

            if (model.getInputAnswer().equalsIgnoreCase("")) {
                pagertab.setCurrentItem(pagerCurrentposition);
                getVibrated();
                isfilled = false;
            } else {
                isfilled = true;
                pagertab.setCurrentItem(pagerCurrentposition + 1);
            }

        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelAddCityType) {
            ModelAddCityType model = (ModelAddCityType) pagerDataList.get(pagerCurrentposition);

            if (model.getInputAnswer().equalsIgnoreCase("")) {
                pagertab.setCurrentItem(pagerCurrentposition);
                getVibrated();
                isfilled = false;
            } else {
                isfilled = true;
                Location = model.getInputAnswer();
                pagertab.setCurrentItem(pagerCurrentposition + 1);
            }

        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelSelectDateType) {
            ModelSelectDateType model = (ModelSelectDateType) pagerDataList.get(pagerCurrentposition);
            if (model.getSelectedDate().equalsIgnoreCase("")) {
                pagertab.setCurrentItem(pagerCurrentposition);
                getVibrated();
                isfilled = false;
            } else {
                isfilled = true;
                pagertab.setCurrentItem(pagerCurrentposition + 1);
            }

        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelSelectDayType) {
            ModelSelectDayType model = (ModelSelectDayType) pagerDataList.get(pagerCurrentposition);
            if (model.getSelectedTime().equalsIgnoreCase("")) {
                pagertab.setCurrentItem(pagerCurrentposition);
                getVibrated();
                isfilled = false;
            } else {
                pagertab.setCurrentItem(pagerCurrentposition + 1);
            }

        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelSelectTimeType) {
            ModelSelectTimeType model = (ModelSelectTimeType) pagerDataList.get(pagerCurrentposition);

            int id = model.getSelectedTimeId();

            if (model.getOptionArray().get(id).equalsIgnoreCase("")) {
                pagertab.setCurrentItem(pagerCurrentposition);
                getVibrated();
                isfilled = false;
            } else {
                isfilled = true;
                pagertab.setCurrentItem(pagerCurrentposition + 1);
            }

        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelRadioType) {
            ModelRadioType model = (ModelRadioType) pagerDataList.get(pagerCurrentposition);

            int id = model.getSelectedOption();

            if (id == -1) {
                pagertab.setCurrentItem(pagerCurrentposition);
                getVibrated();
                isfilled = false;
            } else {
                isfilled = true;
                pagertab.setCurrentItem(pagerCurrentposition + 1);
            }

        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelJobType) {
            ModelJobType model = (ModelJobType) pagerDataList.get(pagerCurrentposition);

            int id = model.getSelectedOption();

            if (id == -1) {
                pagertab.setCurrentItem(pagerCurrentposition);
                getVibrated();
                isfilled = false;
            } else {
                isfilled = true;
                pagertab.setCurrentItem(pagerCurrentposition + 1);
            }

        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelCheckboxType) {
            ModelCheckboxType model = (ModelCheckboxType) pagerDataList.get(pagerCurrentposition);

            ArrayList<Boolean> optionSelectionArray = new ArrayList<Boolean>();
            optionSelectionArray = model.getOptionSelectionArray();

            if (!optionSelectionArray.contains(true)) {
                pagertab.setCurrentItem(pagerCurrentposition);
                getVibrated();
                isfilled = false;
            } else {
                isfilled = true;
                pagertab.setCurrentItem(pagerCurrentposition + 1);
            }

        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelDescriptionType) {
            ModelDescriptionType model = (ModelDescriptionType) pagerDataList.get(pagerCurrentposition);

            pagertab.setCurrentItem(pagerCurrentposition + 1);
        }

    }

    private void getVibrated() {

        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500);
    }

    private void SubmitQuestionsArray() {

        JSONObject data = new JSONObject();
        gTraker = new GPSTracker(context);
        if (gTraker.canGetLocation()) {

            mLat = gTraker.getLatitude();
            mLong = gTraker.getLongitude();

            Log.e("mLat", "" + mLat);
            Log.e("mLong", "" + mLong);
        }
        try {
            data.put("serviceID", serviceId);
            data.put("latitude", mLat);
            data.put("longitude", mLong);
            if (vendor_naukri.equalsIgnoreCase("2")) {

                data.put("userID", AppUtils.getUserId(context));
            } else if (vendor_naukri.equalsIgnoreCase("1")) {
                data.put("userID", AppUtils.getvendorId(context));
            }
            data.put("isUniDirectional", isUniDirectional);

            JSONArray query = new JSONArray();

            for (int i = 0; i < pagerDataList.size(); i++) {
                JSONObject ob = new JSONObject();
                if (pagerDataList.get(i) instanceof ModelAddressType) {
                    ModelAddressType model = (ModelAddressType) pagerDataList.get(i);
                    ob.put("questionID", model.getQuestionID());
                    ob.put("questionTypeID", model.getQuestionTypeID());
                    ob.put("questionText", model.getQuestionText());
                    JSONObject option = new JSONObject();
                    option.put("optionText", model.getAnswer());
                    option.put("optionID", "0");

                    ob.put("options", option);
                    query.put(ob);
                } else if (pagerDataList.get(i) instanceof ModelGetLocationType) {
                    ModelGetLocationType model = (ModelGetLocationType) pagerDataList.get(i);
                    ob.put("questionID", model.getQuestionID());
                    ob.put("questionTypeID", model.getQuestionTypeID());
                    ob.put("questionText", model.getQuestionText());
                    JSONObject option = new JSONObject();
                    option.put("optionText", model.getAddress());
                    option.put("optionID", "0");
                    option.put("latitude", model.getLatitude());
                    option.put("longitude", model.getLongitude());

                    ob.put("options", option);
                    query.put(ob);
                } else if (pagerDataList.get(i) instanceof ModelGetDestinationLocationType) {
                    ModelGetDestinationLocationType model = (ModelGetDestinationLocationType) pagerDataList.get(i);
                    ob.put("questionID", model.getQuestionID());
                    ob.put("questionTypeID", model.getQuestionTypeID());
                    ob.put("questionText", model.getQuestionText());
                    JSONObject option = new JSONObject();
                    option.put("optionText", model.getAddress());
                    option.put("optionID", "0");
                    option.put("latitude", model.getLatitude());
                    option.put("longitude", model.getLongitude());

                    ob.put("options", option);
                    query.put(ob);
                } else if (pagerDataList.get(i) instanceof ModelGetLocationAddressType) {
                    ModelGetLocationAddressType model = (ModelGetLocationAddressType) pagerDataList.get(i);
                    ob.put("questionID", model.getQuestionID());
                    ob.put("questionTypeID", model.getQuestionTypeID());
                    ob.put("questionText", model.getQuestionText());
                    JSONObject option = new JSONObject();
                    option.put("optionText", model.getAddress());
                    option.put("optionID", "0");
                    option.put("latitude", model.getLatitude());
                    option.put("longitude", model.getLongitude());
                    option.put("city", model.getCity());
                    option.put("landmark", model.getLandmark());

                    ob.put("options", option);
                    query.put(ob);
                } else if (pagerDataList.get(i) instanceof ModelRideLocationType) {
                    ModelRideLocationType model = (ModelRideLocationType) pagerDataList.get(i);
                    ob.put("questionID", model.getQuestionID());
                    ob.put("questionTypeID", model.getQuestionTypeID());
                    ob.put("questionText", model.getQuestionText());
                    JSONObject option = new JSONObject();
                    option.put("optionText", "");
                    option.put("optionID", "0");
                    option.put("src_latitude", model.getSrclatitude());
                    option.put("src_longitude", model.getSrclongitude());
                    option.put("dest_latitude", model.getDestlatitude());
                    option.put("dest_longitude", model.getDestlongitude());
                    option.put("dest_address", model.getDestaddress());
                    option.put("src_address", model.getSrcaddress());
                    option.put("selected_date", model.getSelectedDate());
                    option.put("selected_time", model.getSelectedTime());

                    ob.put("options", option);
                    query.put(ob);
                } else if (pagerDataList.get(i) instanceof ModelTextAreaType) {
                    ModelTextAreaType model = (ModelTextAreaType) pagerDataList.get(i);
                    ob.put("questionID", model.getQuestionID());
                    ob.put("questionTypeID", model.getQuestionTypeID());
                    ob.put("questionText", model.getQuestionText());
                    JSONObject option = new JSONObject();
                    option.put("optionText", model.getInputAnswer());
                    option.put("optionID", "0");

                    ob.put("options", option);
                    query.put(ob);
                } else if (pagerDataList.get(i) instanceof ModelSkillType) {
                    ModelSkillType model = (ModelSkillType) pagerDataList.get(i);
                    ob.put("questionID", model.getQuestionID());
                    ob.put("questionTypeID", model.getQuestionTypeID());
                    ob.put("questionText", model.getQuestionText());
                    JSONObject option = new JSONObject();
                    option.put("optionText", model.getInputAnswer());
                    option.put("optionID", "0");

                    ob.put("options", option);
                    query.put(ob);
                } else if (pagerDataList.get(i) instanceof ModelAddCityType) {
                    ModelAddCityType model = (ModelAddCityType) pagerDataList.get(i);
                    ob.put("questionID", model.getQuestionID());
                    ob.put("questionTypeID", model.getQuestionTypeID());
                    ob.put("questionText", model.getQuestionText());
                    JSONObject option = new JSONObject();
                    option.put("optionText", model.getInputAnswer());
                    option.put("optionID", "0");

                    ob.put("options", option);
                    query.put(ob);
                } else if (pagerDataList.get(i) instanceof ModelSelectDateType) {
                    ModelSelectDateType model = (ModelSelectDateType) pagerDataList.get(i);
                    ob.put("questionID", model.getQuestionID());
                    ob.put("questionTypeID", model.getQuestionTypeID());
                    ob.put("questionText", model.getQuestionText());
                    JSONObject option = new JSONObject();
                    option.put("optionText", model.getSelectedDate());
                    option.put("optionID", "0");

                    ob.put("options", option);
                    query.put(ob);
                } else if (pagerDataList.get(i) instanceof ModelSelectDayType) {
                    ModelSelectDayType model = (ModelSelectDayType) pagerDataList.get(i);
                    ob.put("questionID", model.getQuestionID());
                    ob.put("questionTypeID", model.getQuestionTypeID());
                    ob.put("questionText", model.getQuestionText());
                    JSONObject option = new JSONObject();
                    option.put("optionText", model.getSelectedTime());
                    option.put("optionID", "0");

                    ob.put("options", option);
                    query.put(ob);
                } else if (pagerDataList.get(i) instanceof ModelSelectTimeType) {
                    ModelSelectTimeType model = (ModelSelectTimeType) pagerDataList.get(i);
                    ob.put("questionID", model.getQuestionID());
                    ob.put("questionTypeID", model.getQuestionTypeID());
                    ob.put("questionText", model.getQuestionText());
                    int id = model.getSelectedTimeId();
                    JSONObject option = new JSONObject();
                    option.put("optionText", model.getOptionArray().get(id));
                    option.put("optionID", model.getTime_id().get(id));

                    ob.put("options", option);
                    query.put(ob);
                } else if (pagerDataList.get(i) instanceof ModelRadioType) {
                    ModelRadioType model = (ModelRadioType) pagerDataList.get(i);
                    ob.put("questionID", model.getQuestionID());
                    ob.put("questionTypeID", model.getQuestionTypeID());
                    ob.put("questionText", model.getQuestionText());
                    int id = model.getSelectedOption();
                    JSONObject option = new JSONObject();
                    option.put("optionText", model.getOptionArray().get(id));
                    option.put("optionID", model.getOptionId().get(id));

                    ob.put("options", option);

                    query.put(ob);
                } else if (pagerDataList.get(i) instanceof ModelJobType) {
                    ModelJobType model = (ModelJobType) pagerDataList.get(i);
                    ob.put("questionID", model.getQuestionID());
                    ob.put("questionTypeID", model.getQuestionTypeID());
                    ob.put("questionText", model.getQuestionText());
                    int id = model.getSelectedOption();
                    JSONObject option = new JSONObject();
                    option.put("optionText", model.getOptionArray().get(id));
                    option.put("optionID", model.getOptionId().get(id));

                    ob.put("options", option);

                    query.put(ob);
                } else if (pagerDataList.get(i) instanceof ModelCheckboxType) {
                    ModelCheckboxType model = (ModelCheckboxType) pagerDataList.get(i);
                    ob.put("questionID", model.getQuestionID());
                    ob.put("questionTypeID", model.getQuestionTypeID());
                    ob.put("questionText", model.getQuestionText());
                    ArrayList<Boolean> optionSelectionArray = new ArrayList<Boolean>();
                    optionSelectionArray = model.getOptionSelectionArray();
                    JSONArray op = new JSONArray();
                    for (int j = 0; j < optionSelectionArray.size(); j++) {

                        if (optionSelectionArray.get(j)) {
                            JSONObject option = new JSONObject();
                            option.put("optionText", model.getOptionArray().get(j));
                            option.put("optionID", model.getOptionId().get(j));

                            op.put(option);
                        }
                    }
                    ob.put("options", op);
                 /*   JSONArray op1 = new JSONArray();
                    for (int j = 0; j < model.getOptionArray().size(); j++) {

                        JSONObject option1 = new JSONObject();
                        option1.put("optionText", model.getOptionArray().get(j));
                        option1.put("optionID", model.getOptionId().get(j));

                        op1.put(option1);

                    }
                    ob.put("options_original", op1);

*/
                    query.put(ob);
                }
            }
            data.put("queries", query);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {

           /* // for gcm condition check
            // =================================================

            cd = new ConnectionDetector(context);

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present

                // AppUtils.showCustomAlert(SubmitServiceQuestions.this, "Internet Connection Error, Please connect to working Internet connection");

                // stop executing code by return
                return;

            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);

                nameValuePairs
                        .add(new BasicNameValuePair(
                                "respID", data.toString()));

                new AsyncPostDataResponse(SubmitServiceQuestions.this, 1, nameValuePairs,
                        getString(R.string.url_base_new)
                                + getString(R.string.submitServiceQueries));

            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (pagertab.getCurrentItem() == 0) {
                finish();
            } else {
                pagertab.setCurrentItem(pagertab.getCurrentItem() - 1);
            }


            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void getQuestionlist() {
        try {

            JSONArray questionArray1 = new JSONArray(questionArray);

            for (int i = 0; i < questionArray1.length(); i++) {

                JSONObject jo = questionArray1.getJSONObject(i);
                pagerDataList.add(createQuestionModerData(jo));

            }

            pagerLastpos = questionArray1.length();
            Log.e("QuesList11", "**" + pagerDataList.size());
            tabAdapter = new AdapterTabs(getSupportFragmentManager(), questionArray1.length(), pagerDataList);
            pagertab.setAdapter(tabAdapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private Object createQuestionModerData(JSONObject jsonData) {
        Object obj = null;
        try {
            int type = Integer.parseInt(jsonData.getString("QuestionTypeId"));

            switch (type) {
                case 11:

                    ModelDescriptionType objTemp0 = new ModelDescriptionType();
                    objTemp0.setQuestionID(jsonData.getString("QuestionId"));
                    objTemp0.setQuestionText(jsonData.getString("QuestionText"));
                    objTemp0.setIsMandatory(jsonData.getString("IsMandatory"));
                    objTemp0.setQuestionType(jsonData.getString("QuestionType"));
                    objTemp0.setQuestionImageUrl(jsonData.getString("QuestionImageUrl"));
                    objTemp0.setPlaceholder(jsonData.getString("AnswerHint"));
                    objTemp0.setQuestionTypeID(jsonData.getString("QuestionTypeId"));

                    JSONObject jo = jsonData.getJSONObject("QuestionOptions");
                    objTemp0.setBodyText(jo.getString("OptionText"));

                    obj = objTemp0;
                    break;

                case 5:

                    ModelAddressType objTemp1 = new ModelAddressType();
                    objTemp1.setQuestionID(jsonData.getString("QuestionId"));
                    objTemp1.setQuestionText(jsonData.getString("QuestionText"));
                    objTemp1.setIsMandatory(jsonData.getString("IsMandatory"));
                    objTemp1.setQuestionType(jsonData.getString("QuestionType"));
                    objTemp1.setPlaceholder(jsonData.getString("AnswerHint"));
                    objTemp1.setQuestionTypeID(jsonData.getString("QuestionTypeId"));
                    objTemp1.setQuestionImageUrl(jsonData.getString("QuestionImageUrl"));
                    // JSONObject jo1 = jsonData.getJSONObject("options");
                    //  objTemp1.setAnswer(jo1.getString("optionText"));

                    obj = objTemp1;
                    break;

                case 1:

                    if (jsonData.getString("IsAnti").equalsIgnoreCase("0")) {

                        ModelRadioType objTemp2 = new ModelRadioType();

                        objTemp2.setQuestionID(jsonData.getString("QuestionId"));
                        objTemp2.setQuestionText(jsonData.getString("QuestionText"));
                        objTemp2.setIsMandatory(jsonData.getString("IsMandatory"));
                        objTemp2.setQuestionType(jsonData.getString("QuestionType"));
                        objTemp2.setPlaceholder(jsonData.getString("AnswerHint"));
                        objTemp2.setQuestionTypeID(jsonData.getString("QuestionTypeId"));
                        objTemp2.setQuestionImageUrl(jsonData.getString("QuestionImageUrl"));

                        JSONArray jo2 = jsonData.getJSONArray("QuestionOptions");
                        ArrayList<String> array = new ArrayList<>();
                        ArrayList<String> id = new ArrayList<>();
                        for (int i = 0; i < jo2.length(); i++) {

                            JSONObject j1 = jo2.getJSONObject(i);
                            array.add(j1.getString("OptionText"));
                            id.add(j1.getString("OptionId"));
                            // array.add(j1.getString("optionID"));
                        }
                        objTemp2.setOptionArray(array);
                        objTemp2.setOptionId(id);

                        obj = objTemp2;
                    } else if (jsonData.getString("is_anti").equalsIgnoreCase("1")) {


                        ModelJobType objTemp21 = new ModelJobType();

                        objTemp21.setQuestionID(jsonData.getString("QuestionId"));
                        objTemp21.setQuestionText(jsonData.getString("QuestionText"));
                        objTemp21.setIsMandatory(jsonData.getString("IsMandatory"));
                        objTemp21.setQuestionType(jsonData.getString("QuestionType"));
                        objTemp21.setPlaceholder(jsonData.getString("AnswerHint"));
                        objTemp21.setQuestionTypeID(jsonData.getString("QuestionTypeId"));
                        objTemp21.setQuestionImageUrl(jsonData.getString("QuestionImageUrl"));

                        JSONArray jo2 = jsonData.getJSONArray("QuestionOptions");
                        ArrayList<String> array = new ArrayList<>();
                        ArrayList<String> id = new ArrayList<>();
                        ArrayList<String> image = new ArrayList<>();
                        for (int i = 0; i < jo2.length(); i++) {

                            JSONObject j1 = jo2.getJSONObject(i);
                            array.add(j1.getString("OptionText"));
                            id.add(j1.getString("OptionId"));
                            image.add(j1.getString("OptionImageUrl"));
                            // array.add(j1.getString("optionID"));
                        }
                        objTemp21.setOptionArray(array);
                        objTemp21.setOptionImage(image);
                        objTemp21.setOptionId(id);

                        obj = objTemp21;

                    }

                    break;

                case 10:
                    ModelSelectDateType objTemp3 = new ModelSelectDateType();

                    objTemp3.setQuestionID(jsonData.getString("QuestionId"));
                    objTemp3.setQuestionText(jsonData.getString("QuestionText"));
                    objTemp3.setIsMandatory(jsonData.getString("IsMandatory"));
                    objTemp3.setQuestionType(jsonData.getString("QuestionType"));
                    objTemp3.setPlaceholder(jsonData.getString("AnswerHint"));
                    objTemp3.setQuestionTypeID(jsonData.getString("QuestionTypeId"));
                    objTemp3.setQuestionImageUrl(jsonData.getString("QuestionImageUrl"));

                    Calendar now = Calendar.getInstance();
                    int month = now.get(Calendar.MONTH) + 1;

                    objTemp3.setSelectedDate(now.get(Calendar.DAY_OF_MONTH) + "/" + month + "/" + now.get(Calendar.YEAR));
                    obj = objTemp3;
                    break;


                case 6:
                    ModelGetLocationType objTemp4 = new ModelGetLocationType();

                    objTemp4.setQuestionID(jsonData.getString("QuestionId"));
                    objTemp4.setQuestionText(jsonData.getString("QuestionText"));
                    objTemp4.setIsMandatory(jsonData.getString("IsMandatory"));
                    objTemp4.setQuestionType(jsonData.getString("QuestionType"));
                    objTemp4.setPlaceholder(jsonData.getString("AnswerHint"));
                    objTemp4.setQuestionTypeID(jsonData.getString("QuestionTypeId"));
                    objTemp4.setQuestionImageUrl(jsonData.getString("QuestionImageUrl"));

                    obj = objTemp4;
                    break;

                case 2:
                    ModelCheckboxType objTemp5 = new ModelCheckboxType();

                    objTemp5.setQuestionID(jsonData.getString("QuestionId"));
                    objTemp5.setQuestionText(jsonData.getString("QuestionText"));
                    objTemp5.setIsMandatory(jsonData.getString("IsMandatory"));
                    objTemp5.setQuestionType(jsonData.getString("QuestionType"));
                    objTemp5.setPlaceholder(jsonData.getString("AnswerHint"));
                    objTemp5.setQuestionTypeID(jsonData.getString("QuestionTypeId"));
                    objTemp5.setQuestionImageUrl(jsonData.getString("QuestionImageUrl"));

                    ArrayList<Boolean> bool = new ArrayList<>();
                    JSONArray jo3 = jsonData.getJSONArray("QuestionOptions");
                    ArrayList<String> id1 = new ArrayList<>();
                    ArrayList<String> array1 = new ArrayList<>();
                    for (int i = 0; i < jo3.length(); i++) {

                        JSONObject j1 = jo3.getJSONObject(i);
                        bool.add(i, false);
                        id1.add(j1.getString("OptionId"));
                        array1.add(j1.getString("OptionText"));
                        // array.add(j1.getString("optionID"));
                    }
                    objTemp5.setOptionSelectionArray(bool);
                    objTemp5.setOptionArray(array1);
                    objTemp5.setOptionId(id1);
                    obj = objTemp5;
                    break;

                case 3:
                    ModelSelectDayType objTemp6 = new ModelSelectDayType();

                    objTemp6.setQuestionID(jsonData.getString("QuestionId"));
                    objTemp6.setQuestionText(jsonData.getString("QuestionText"));
                    objTemp6.setIsMandatory(jsonData.getString("IsMandatory"));
                    objTemp6.setQuestionType(jsonData.getString("QuestionType"));
                    objTemp6.setPlaceholder(jsonData.getString("AnswerHint"));
                    objTemp6.setQuestionTypeID(jsonData.getString("QuestionTypeId"));
                    objTemp6.setQuestionImageUrl(jsonData.getString("QuestionImageUrl"));

                    obj = objTemp6;
                    break;

                case 7:
                    ModelGetLocationAddressType objTemp7 = new ModelGetLocationAddressType();

                    objTemp7.setQuestionID(jsonData.getString("QuestionId"));
                    objTemp7.setQuestionText(jsonData.getString("QuestionText"));
                    objTemp7.setIsMandatory(jsonData.getString("IsMandatory"));
                    objTemp7.setQuestionType(jsonData.getString("QuestionType"));
                    objTemp7.setPlaceholder(jsonData.getString("AnswerHint"));
                    objTemp7.setQuestionTypeID(jsonData.getString("QuestionTypeId"));
                    objTemp7.setQuestionImageUrl(jsonData.getString("QuestionImageUrl"));

                    obj = objTemp7;
                    break;

                case 9:
                    ModelTextAreaType objTemp8 = new ModelTextAreaType();

                    objTemp8.setQuestionID(jsonData.getString("QuestionId"));
                    objTemp8.setQuestionText(jsonData.getString("QuestionText"));
                    objTemp8.setIsMandatory(jsonData.getString("IsMandatory"));
                    objTemp8.setQuestionType(jsonData.getString("QuestionType"));
                    objTemp8.setPlaceholder(jsonData.getString("AnswerHint"));
                    objTemp8.setQuestionTypeID(jsonData.getString("QuestionTypeId"));
                    objTemp8.setQuestionImageUrl(jsonData.getString("QuestionImageUrl"));

                    obj = objTemp8;
                    break;

                case 4:
                    ModelSelectTimeType objTemp9 = new ModelSelectTimeType();

                    objTemp9.setQuestionID(jsonData.getString("QuestionId"));
                    objTemp9.setQuestionText(jsonData.getString("QuestionText"));
                    objTemp9.setIsMandatory(jsonData.getString("IsMandatory"));
                    objTemp9.setQuestionType(jsonData.getString("QuestionType"));
                    objTemp9.setPlaceholder(jsonData.getString("AnswerHint"));
                    objTemp9.setQuestionTypeID(jsonData.getString("QuestionTypeId"));
                    objTemp9.setQuestionImageUrl(jsonData.getString("QuestionImageUrl"));

                    JSONArray options = jsonData.getJSONArray("QuestionOptions");
                    ArrayList<String> time_id = new ArrayList<>();
                    ArrayList<String> times = new ArrayList<>();
                    for (int i = 0; i < options.length(); i++) {

                        JSONObject j1 = options.getJSONObject(i);

                        times.add(j1.getString("OptionText"));
                        time_id.add(j1.getString("OptionId"));
                    }
                    objTemp9.setOptionArray(times);
                    objTemp9.setTime_id(time_id);

                    obj = objTemp9;
                    break;


                case 13:
                    ModelAddCityType objTemp10 = new ModelAddCityType();


                    objTemp10.setQuestionID(jsonData.getString("QuestionId"));
                    objTemp10.setQuestionText(jsonData.getString("QuestionText"));
                    objTemp10.setIsMandatory(jsonData.getString("IsMandatory"));
                    objTemp10.setQuestionType(jsonData.getString("QuestionType"));
                    objTemp10.setPlaceholder(jsonData.getString("AnswerHint"));
                    objTemp10.setQuestionTypeID(jsonData.getString("QuestionTypeId"));
                    objTemp10.setQuestionImageUrl(jsonData.getString("QuestionImageUrl"));

                    obj = objTemp10;
                    break;

                case 14:
                    ModelGetDestinationLocationType objTemp11 = new ModelGetDestinationLocationType();

                    objTemp11.setQuestionID(jsonData.getString("QuestionId"));
                    objTemp11.setQuestionText(jsonData.getString("QuestionText"));
                    objTemp11.setIsMandatory(jsonData.getString("IsMandatory"));
                    objTemp11.setQuestionType(jsonData.getString("QuestionType"));
                    objTemp11.setPlaceholder(jsonData.getString("AnswerHint"));
                    objTemp11.setQuestionTypeID(jsonData.getString("QuestionTypeId"));
                    objTemp11.setQuestionImageUrl(jsonData.getString("QuestionImageUrl"));

                    obj = objTemp11;
                    break;

                case 15:
                    ModelSkillType objTemp12 = new ModelSkillType();

                    objTemp12.setQuestionID(jsonData.getString("QuestionId"));
                    objTemp12.setQuestionText(jsonData.getString("QuestionText"));
                    objTemp12.setIsMandatory(jsonData.getString("IsMandatory"));
                    objTemp12.setQuestionType(jsonData.getString("QuestionType"));
                    objTemp12.setPlaceholder(jsonData.getString("AnswerHint"));
                    objTemp12.setQuestionTypeID(jsonData.getString("QuestionTypeId"));
                    objTemp12.setQuestionImageUrl(jsonData.getString("QuestionImageUrl"));


                    obj = objTemp12;
                    break;


                case 16:
                    ModelRideLocationType objTemp13 = new ModelRideLocationType();

                    objTemp13.setQuestionID(jsonData.getString("QuestionId"));
                    objTemp13.setQuestionText(jsonData.getString("QuestionText"));
                    objTemp13.setIsMandatory(jsonData.getString("IsMandatory"));
                    objTemp13.setQuestionType(jsonData.getString("QuestionType"));
                    objTemp13.setPlaceholder(jsonData.getString("AnswerHint"));
                    objTemp13.setQuestionTypeID(jsonData.getString("QuestionTypeId"));
                    objTemp13.setQuestionImageUrl(jsonData.getString("QuestionImageUrl"));

                    obj = objTemp13;
                    break;

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return obj;

    }


    @Override
    public void onItemClickListener(int position, int flag) {

    }

    @Override
    public Object getPageDataModel(int position) {
        //return pager position and position of arraylist
        return pagerDataList.get(position);
    }

    @Override
    public void setPageDataModel(int position, Object obj) {

        //set data in pager fragments
        pagerDataList.set(position, obj);
    }

    @Override
    public void showNext(Boolean bool) {
        setPagerdataOnnext();
    }


    @Override
    public void onPostSuccess(int position, JSONObject jObject) {
        try {

            if (position == 1) {
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    JSONObject schedule = data.getJSONObject("schedule");
                    //    AppUtils.showCustomAlert(SubmitServiceQuestions.this, commandResult.getString("message"));
                    String searchId = data.getString("searchId");
                    String service_id = schedule.getString("serviceId");
                    String request_id = schedule.getString("request_id");
                    String serviceName = schedule.getString("serviceName");

                    String time = "";

                    if (!schedule.getString("timeSlot").equalsIgnoreCase("")) {
                        time = schedule.getString("timeSlot");
                    }
                    if (!schedule.getString("weekDays").equalsIgnoreCase("")) {

                        if (!time.equalsIgnoreCase("")) {
                            time = time + ", " + schedule.getString("weekDays");
                        } else {
                            time = schedule.getString("weekDays");

                        }
                    }
                    if (!schedule.getString("date").equalsIgnoreCase("")) {

                        if (!time.equalsIgnoreCase("")) {
                            time = time + ", " + schedule.getString("date");
                        } else {
                            time = schedule.getString("date");

                        }
                    }


                    String isUniDirectional = schedule.getString("isUniDirectional");

                    if (isUniDirectional.equalsIgnoreCase("1")) {

                       /* Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                        sendBroadcast(broadcastIntent);
                        Intent in = new Intent(SubmitServiceQuestions.this, Activity_RequestStatus.class);
                        in.putExtra("service_id", service_id);
                        in.putExtra("service_name", serviceName);
                        in.putExtra("service_image", getResources().getString(R.string.img_url) + schedule.getString("serviceImage"));
                        in.putExtra("searchId", searchId);
                        in.putExtra("time", time);
                        in.putExtra("requestId", request_id);
                        in.putExtra("pageFlag", "1");

                        startActivity(in);
                        finish();*/

                    } else if (isUniDirectional.equalsIgnoreCase("2")) {

                      /*  if (vendor_naukri.equalsIgnoreCase("1")) {

                            Intent broadcastIntent = new Intent();
                            broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                            sendBroadcast(broadcastIntent);
                            Intent in = new Intent(SubmitServiceQuestions.this, Vendor_SearchList.class);
                            in.putExtra("service_id", service_id);
                            in.putExtra("service_name", serviceName);
                            in.putExtra("vendor_naukri", vendor_naukri);
                            in.putExtra("searchId", searchId);
                            in.putExtra("pageFlag", "1");

                            startActivity(in);
                            finish();

                        } else {

                            Intent broadcastIntent = new Intent();
                            broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                            sendBroadcast(broadcastIntent);
                            Intent in = new Intent(SubmitServiceQuestions.this, Activity_SearchNaukriList.class);
                            in.putExtra("service_id", service_id);
                            in.putExtra("service_name", serviceName);
                            in.putExtra("vendor_naukri", vendor_naukri);
                            in.putExtra("searchId", searchId);
                            in.putExtra("pageFlag", "1");
                            startActivity(in);
                            finish();
                        }*/
                    }


                } else {

                    //  AppUtils.showCustomAlert(SubmitServiceQuestions.this, commandResult.getString("message"));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostFail(int method, String response) {

    }
}

