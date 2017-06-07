package com.app.justclap.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.R;
import com.app.justclap.models.ModelRideLocationType;
import com.app.justclap.utils.GPSTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.List;

import com.app.justclap.adapters.AdapterTabs;

import com.app.justclap.asyntask.AsyncPostDataResponse;

import com.app.justclap.interfaces.ConnectionDetector;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.interfaces.QuestionDatailInterface;
import com.app.justclap.models.ModelAddCityType;
import com.app.justclap.models.ModelAddressType;
import com.app.justclap.models.ModelAntiType;
import com.app.justclap.models.ModelCheckboxType;
import com.app.justclap.models.ModelDescriptionType;
import com.app.justclap.models.ModelGetDestinationLocationType;
import com.app.justclap.models.ModelGetLocationAddressType;
import com.app.justclap.models.ModelGetLocationType;
import com.app.justclap.models.ModelQuestionList;
import com.app.justclap.models.ModelRadioType;
import com.app.justclap.models.ModelSelectDateType;
import com.app.justclap.models.ModelSelectDayType;
import com.app.justclap.models.ModelSelectTimeType;
import com.app.justclap.models.ModelSkillType;
import com.app.justclap.models.ModelTextAreaType;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.vendordetail.Vendor_SearchList;
import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.enumeration.ProfileField;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;
import com.google.code.linkedinapi.schema.Person;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

public class SubmitServiceQuestions extends AppCompatActivity implements OnCustomItemClicListener, QuestionDatailInterface, ListenerPostData {

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
    TextView text_book;
    LinearLayout ll_bottom;
    CallbackManager callbackManager;
    String Location = "";
    ShareDialog shareDialog;
    ImageView image_fb_share, image_linkedin_share;
    Toolbar toolbar;
    boolean isfilled = false;
    private BroadcastReceiver broadcastReceiver;
    RelativeLayout rl_main_layout, rl_network;
    private LinkedInOAuthService oAuthService;
    private LinkedInApiClientFactory factory;
    private LinkedInRequestToken liToken;
    private LinkedInApiClient client;
    String is_naukri = "";
    LinkedInAccessToken accessToken = null;
    String shareNaukriData = "";
    double mLat = 0.0, mLong = 0.0;
    GPSTracker gTraker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_booking_step1);

        context = this;
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
      /*  IntentFilter intentFilter = new IntentFilter();
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
      */
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
        if (in.hasExtra("vendor_naukri")) {
            vendor_naukri = in.getStringExtra("vendor_naukri");
            is_naukri = in.getStringExtra("is_naukri");
        }

        Log.e("vendor_naukri", vendor_naukri + "");
        isUniDirectional = in.getExtras().getString("isUniDirectional");
        quesList = new ArrayList<>();
        optionList = new ArrayList<>();
        getQuestionlist();

        if (is_naukri.equalsIgnoreCase("1")) {
            image_linkedin_share.setVisibility(View.VISIBLE);
            image_fb_share.setVisibility(View.GONE);
        } else {
            image_linkedin_share.setVisibility(View.GONE);
            image_fb_share.setVisibility(View.VISIBLE);
        }

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
        overridePendingTransition(R.anim.enter,
                R.anim.exit);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        image_fb_share = (ImageView) findViewById(R.id.image_fb_share);
        image_linkedin_share = (ImageView) findViewById(R.id.image_linkedin_share);
        rl_main_layout = (RelativeLayout) findViewById(R.id.rl_main_layout);
        rl_network = (RelativeLayout) findViewById(R.id.rl_network);
        pagertab = (ViewPager) findViewById(R.id.pagertab);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        rl_bottom.setVisibility(View.VISIBLE);
        text_book = (TextView) findViewById(R.id.text_book);

    }

    private void shareDataOnLinkedin() {
        //client.postNetworkUpdate(share);
                                /*Toast.makeText(Share.this,
                                        "Shared sucessfully", Toast.LENGTH_SHORT).show();*/

        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(ConstantsNew.CONSUMER_KEY, ConstantsNew.CONSUMER_SECRET);
        consumer.setTokenWithSecret(accessToken.getToken(), accessToken.getTokenSecret());
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost post = new HttpPost("https://api.linkedin.com/v1/people/~/shares");

        try {
            consumer.sign(post);
        } catch (OAuthMessageSignerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OAuthExpectationFailedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OAuthCommunicationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // here need the consumer for sign in for post the share
        post.setHeader("content-type", "text/XML");
        Log.e("naukriContent", shareNaukriData);
        String myEntity = "<share>"
                + "<comment>" + servicename + "</comment>"
                + "<content>"
                + "<title>Hello Guys</title>"
                + "<description>" + "Hi Friends- I " + shareNaukriData +
                "\nhttp://play.google.com/store/apps/details?id=" + context.getPackageName()
                + "</description>"
                + "<submitted-url>dev.logiguyz.com/justclap/assets/admin/img/profile-pics/logo.png</submitted-url>"
                //+ "<submitted-image-url>http://m3.licdn.com/media/p/3/000/124/1a6/089a29a.png</submitted-image-url>"
                + "</content>"
                + "<visibility> "
                + " <code>anyone</code>"
                + "</visibility>"
                + "</share>";

        Log.e("myEntity", "*" + myEntity);
        try {
            String res = "";

            post.setEntity(new StringEntity(myEntity));
            org.apache.http.HttpResponse response = httpclient.execute(post);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                res = EntityUtils.toString(resEntity);
                // .....Read the response
            }

            Log.e("response", res);
            Toast.makeText(SubmitServiceQuestions.this, "Shared sucessfully", Toast.LENGTH_SHORT).show();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("onNewIntent", "&& onNewIntent");
        try {


            linkedInImport(intent);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    private void linkedInImport(Intent intent) {
        String verifier = intent.getData().getQueryParameter("oauth_verifier");
        Log.e("liToken ", "" + liToken);
        Log.e("verifier ", verifier);

        accessToken = oAuthService.getOAuthAccessToken(
                liToken, verifier);
        client = factory.createLinkedInApiClient(accessToken);

        Person profile = client.getProfileForCurrentUser(EnumSet.of(
                ProfileField.ID, ProfileField.FIRST_NAME,
                ProfileField.LAST_NAME, ProfileField.HEADLINE));

        Log.e("Id :: ", profile.getId());
        Log.e("Last Name :: ", profile.getLastName());
        Log.e("Head Line :: ", profile.getHeadline());

        shareDataOnLinkedin();

    }


    private void setListener() {

        image_fb_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPagerdataOnnext();
                if (isfilled) {

                    Log.e("location", "*" + Location);

                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle("Hello Guys")
                            .setContentDescription(servicename + " @ " + Location + "\nhttp://play.google.com/store/apps/details?id=" + context.getPackageName())
                            .setImageUrl(Uri.parse("http://dev.logiguyz.com/justclap/assets/admin/img/profile-pics/logo.png"))
                            .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                            .build();

                    shareDialog.show(linkContent);
                } else {
                    AppUtils.showCustomAlert(SubmitServiceQuestions.this, "Please fill all detail");
                }
            }
        });

        image_linkedin_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent in = new Intent(context, SharedataLinkedin.class);
                in.putExtra("shareNaukriData", shareNaukriData);
                in.putExtra("servicename", servicename);
                startActivityForResult(in, 21);

              /*  oAuthService = LinkedInOAuthServiceFactory.getInstance()
                        .createLinkedInOAuthService(ConstantsNew.CONSUMER_KEY,
                                ConstantsNew.CONSUMER_SECRET);


                Log.e("oAuthService : ", "*" + oAuthService);

                factory = LinkedInApiClientFactory.newInstance(
                        ConstantsNew.CONSUMER_KEY, ConstantsNew.CONSUMER_SECRET);

                liToken = oAuthService
                        .getOAuthRequestToken(ConstantsNew.OAUTH_CALLBACK_URL);
                Log.e("liToken : ", "*" + liToken.getAuthorizationUrl());
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(liToken
                        .getAuthorizationUrl()));
                startActivity(i);*/


            }
        });


        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {


            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(context, "Shared sucessfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(context, "Some error occured, Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
        rl_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPagerdataOnnext();
                if (pagerLastpos - 1 == pagerCurrentposition) {
                    text_book.setText("Submit");

                    if (isfilled) {
                        showConfirmtion();
                    } else {
                        AppUtils.showCustomAlert(SubmitServiceQuestions.this, "Please fill all detail");
                    }
                } else {
                    text_book.setText("Next");

                }

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO bind to drawer with... injection?

                if (pagertab.getCurrentItem() == 0) {
                    finish();
                    overridePendingTransition(R.anim.left_to_right,
                            R.anim.right_to_left);
                } else {
                    pagertab.setCurrentItem(pagertab.getCurrentItem() - 1);
                }
            }

        });


        pagertab.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                pagerCurrentposition = position;
                if (pagerLastpos - 1 == pagerCurrentposition) {
                    ll_bottom.setVisibility(View.VISIBLE);
                    text_book.setText("Submit");
                } else {
                    ll_bottom.setVisibility(View.GONE);
                    text_book.setText("Next");
                }
            /*    if (position == 0) {
                    rl_bottom.setVisibility(View.GONE);
                } else {
                    rl_bottom.setVisibility(View.VISIBLE);
                }*/

                Log.e("onPageScrolled", "onPageScrolled");
                Log.e("position", position + "");
                Log.e("getCurrentItemposition", pagertab.getCurrentItem() + "");
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

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 21 && resultCode == 12) {
            Toast.makeText(SubmitServiceQuestions.this, "Shared sucessfully", Toast.LENGTH_SHORT).show();


        }

        // Toast.makeText(context, "share sucessfully", Toast.LENGTH_SHORT).show();
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
                if (!shareNaukriData.contains(model.getInputAnswer())) {
                    if (shareNaukriData.equalsIgnoreCase("")) {
                        shareNaukriData = model.getInputAnswer();

                    } else {
                        shareNaukriData = shareNaukriData + " in " + model.getInputAnswer();
                    }
                }
            }

        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelAddCityType) {
            ModelAddCityType model = (ModelAddCityType) pagerDataList.get(pagerCurrentposition);

            if (model.getInputAnswer().equalsIgnoreCase("")) {
                pagertab.setCurrentItem(pagerCurrentposition);
                getVibrated();
                Log.e("ModelAddCityType", "ModelAddCityTypeVibraton");
            } else {
                if (!shareNaukriData.contains(model.getInputAnswer())) {
                    if (shareNaukriData.equalsIgnoreCase("")) {
                        shareNaukriData = model.getInputAnswer();

                    } else {
                        shareNaukriData = shareNaukriData + " at " + model.getInputAnswer();
                    }
                }

                Location = model.getInputAnswer();
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

        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelAntiType) {
            ModelAntiType model = (ModelAntiType) pagerDataList.get(pagerCurrentposition);

            int id = model.getSelectedOption();

            if (id == -1) {
                pagertab.setCurrentItem(pagerCurrentposition);
                getVibrated();
            } else {
                if (!shareNaukriData.contains(model.getOptionArray().get(id))) {
                    if (shareNaukriData.equalsIgnoreCase("")) {
                        shareNaukriData = model.getOptionArray().get(id);

                    } else {
                        shareNaukriData = shareNaukriData + " " + model.getOptionArray().get(id);
                    }
                }
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

        } else if (pagerDataList.get(pagerCurrentposition) instanceof ModelAntiType) {
            ModelAntiType model = (ModelAntiType) pagerDataList.get(pagerCurrentposition);

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
                } else if (pagerDataList.get(i) instanceof ModelAntiType) {
                    ModelAntiType model = (ModelAntiType) pagerDataList.get(i);
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

            // for gcm condition check
            // =================================================

            cd = new ConnectionDetector(context);

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                //Internet Connection is not present
                rl_main_layout.setVisibility(View.GONE);
                rl_network.setVisibility(View.VISIBLE);

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

            }
        } catch (Exception e) {

        }


    }


    /*
        @Override
        public void onBackPressed() {
            super.onBackPressed();

        }*/
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (pagertab.getCurrentItem() == 0) {
                finish();
                overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);
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
            int type = Integer.parseInt(jsonData.getString("questionTypeID"));

            switch (type) {

                case 0:
                    ModelDescriptionType objTemp0 = new ModelDescriptionType();
                    objTemp0.setQuestionID(jsonData.getString("questionID"));
                    objTemp0.setQuestionText(jsonData.getString("questionText"));
                    objTemp0.setIsMandatory(jsonData.getString("isMandatory"));
                    objTemp0.setQuestionType(jsonData.getString("questionType"));
                    //objTemp0.setInput_type(jsonData.getString("input_type"));
                    objTemp0.setPlaceholder(jsonData.getString("placeHolder"));
                    objTemp0.setQuestionTypeID(jsonData.getString("questionTypeID"));

                    JSONObject jo = jsonData.getJSONObject("options");
                    objTemp0.setBodyText(jo.getString("optionText"));

                    obj = objTemp0;
                    break;

                case 1:
                    ModelAddressType objTemp1 = new ModelAddressType();
                    objTemp1.setQuestionID(jsonData.getString("questionID"));
                    objTemp1.setQuestionText(jsonData.getString("questionText"));
                    objTemp1.setIsMandatory(jsonData.getString("isMandatory"));
                    objTemp1.setQuestionType(jsonData.getString("questionType"));
                    //objTemp1.setInput_type(jsonData.getString("input_type"));
                    objTemp1.setPlaceholder(jsonData.getString("placeHolder"));
                    objTemp1.setQuestionTypeID(jsonData.getString("questionTypeID"));

                    // JSONObject jo1 = jsonData.getJSONObject("options");
                    //  objTemp1.setAnswer(jo1.getString("optionText"));

                    obj = objTemp1;
                    break;

                case 2:

                    if (jsonData.getString("is_anti").equalsIgnoreCase("0")) {

                        ModelRadioType objTemp2 = new ModelRadioType();

                        objTemp2.setQuestionID(jsonData.getString("questionID"));
                        objTemp2.setQuestionText(jsonData.getString("questionText"));
                        objTemp2.setIsMandatory(jsonData.getString("isMandatory"));
                        objTemp2.setQuestionType(jsonData.getString("questionType"));
                        //objTemp2.setInput_type(jsonData.getString("input_type"));
                        objTemp2.setPlaceholder(jsonData.getString("placeHolder"));
                        objTemp2.setQuestionTypeID(jsonData.getString("questionTypeID"));

                        JSONArray jo2 = jsonData.getJSONArray("options");
                        ArrayList<String> array = new ArrayList<>();
                        ArrayList<String> id = new ArrayList<>();
                        for (int i = 0; i < jo2.length(); i++) {

                            JSONObject j1 = jo2.getJSONObject(i);
                            array.add(j1.getString("optionText"));
                            id.add(j1.getString("optionID"));
                            // array.add(j1.getString("optionID"));
                        }
                        objTemp2.setOptionArray(array);
                        objTemp2.setOptionId(id);

                        obj = objTemp2;
                    } else if (jsonData.getString("is_anti").equalsIgnoreCase("1")) {

                        ModelAntiType objTemp21 = new ModelAntiType();

                        objTemp21.setQuestionID(jsonData.getString("questionID"));
                        objTemp21.setQuestionText(jsonData.getString("questionText"));
                        objTemp21.setIsMandatory(jsonData.getString("isMandatory"));
                        objTemp21.setQuestionType(jsonData.getString("questionType"));
                        //objTemp2.setInput_type(jsonData.getString("input_type"));
                        objTemp21.setPlaceholder(jsonData.getString("placeHolder"));
                        objTemp21.setQuestionTypeID(jsonData.getString("questionTypeID"));

                        JSONArray jo2 = jsonData.getJSONArray("options");
                        ArrayList<String> array = new ArrayList<>();
                        ArrayList<String> id = new ArrayList<>();
                        ArrayList<String> image = new ArrayList<>();
                        for (int i = 0; i < jo2.length(); i++) {

                            JSONObject j1 = jo2.getJSONObject(i);
                            array.add(j1.getString("optionText"));
                            id.add(j1.getString("optionID"));
                            image.add(j1.getString("optionImage"));
                            // array.add(j1.getString("optionID"));
                        }
                        objTemp21.setOptionArray(array);
                        objTemp21.setOptionImage(image);
                        objTemp21.setOptionId(id);

                        obj = objTemp21;

                    }

                    break;

                case 3:
                    ModelSelectDateType objTemp3 = new ModelSelectDateType();
                    objTemp3.setQuestionID(jsonData.getString("questionID"));
                    objTemp3.setQuestionText(jsonData.getString("questionText"));
                    objTemp3.setIsMandatory(jsonData.getString("isMandatory"));
                    objTemp3.setQuestionType(jsonData.getString("questionType"));
                    // objTemp3.setInput_type(jsonData.getString("input_type"));
                    objTemp3.setPlaceholder(jsonData.getString("placeHolder"));
                    objTemp3.setQuestionTypeID(jsonData.getString("questionTypeID"));
                    Calendar now = Calendar.getInstance();
                    int month = now.get(Calendar.MONTH) + 1;

                    objTemp3.setSelectedDate(now.get(Calendar.DAY_OF_MONTH) + "/" + month + "/" + now.get(Calendar.YEAR));
                    obj = objTemp3;
                    break;


                case 4:
                    ModelGetLocationType objTemp4 = new ModelGetLocationType();
                    objTemp4.setQuestionID(jsonData.getString("questionID"));
                    objTemp4.setQuestionText(jsonData.getString("questionText"));
                    objTemp4.setIsMandatory(jsonData.getString("isMandatory"));
                    objTemp4.setQuestionType(jsonData.getString("questionType"));
                    //objTemp4.setInput_type(jsonData.getString("input_type"));
                    objTemp4.setPlaceholder(jsonData.getString("placeHolder"));
                    objTemp4.setQuestionTypeID(jsonData.getString("questionTypeID"));

                    obj = objTemp4;
                    break;

                case 5:
                    ModelCheckboxType objTemp5 = new ModelCheckboxType();
                    objTemp5.setQuestionID(jsonData.getString("questionID"));
                    objTemp5.setQuestionText(jsonData.getString("questionText"));
                    objTemp5.setIsMandatory(jsonData.getString("isMandatory"));
                    objTemp5.setQuestionType(jsonData.getString("questionType"));
                    // objTemp5.setInput_type(jsonData.getString("input_type"));
                    objTemp5.setPlaceholder(jsonData.getString("placeHolder"));
                    objTemp5.setQuestionTypeID(jsonData.getString("questionTypeID"));

                    ArrayList<Boolean> bool = new ArrayList<>();
                    JSONArray jo3 = jsonData.getJSONArray("options");
                    ArrayList<String> id1 = new ArrayList<>();
                    ArrayList<String> array1 = new ArrayList<>();
                    for (int i = 0; i < jo3.length(); i++) {

                        JSONObject j1 = jo3.getJSONObject(i);
                        bool.add(i, false);
                        id1.add(j1.getString("optionID"));
                        array1.add(j1.getString("optionText"));
                        // array.add(j1.getString("optionID"));
                    }
                    objTemp5.setOptionSelectionArray(bool);
                    objTemp5.setOptionArray(array1);
                    objTemp5.setOptionId(id1);
                    obj = objTemp5;
                    break;

                case 6:
                    ModelSelectDayType objTemp6 = new ModelSelectDayType();
                    objTemp6.setQuestionID(jsonData.getString("questionID"));
                    objTemp6.setQuestionText(jsonData.getString("questionText"));
                    objTemp6.setIsMandatory(jsonData.getString("isMandatory"));
                    objTemp6.setQuestionType(jsonData.getString("questionType"));
                    //objTemp6.setInput_type(jsonData.getString("input_type"));
                    objTemp6.setPlaceholder(jsonData.getString("placeHolder"));
                    objTemp6.setQuestionTypeID(jsonData.getString("questionTypeID"));

                    obj = objTemp6;
                    break;

                case 7:
                    ModelGetLocationAddressType objTemp7 = new ModelGetLocationAddressType();
                    objTemp7.setQuestionID(jsonData.getString("questionID"));
                    objTemp7.setQuestionText(jsonData.getString("questionText"));
                    objTemp7.setIsMandatory(jsonData.getString("isMandatory"));
                    objTemp7.setQuestionType(jsonData.getString("questionType"));
                    // objTemp7.setInput_type(jsonData.getString("input_type"));
                    objTemp7.setPlaceholder(jsonData.getString("placeHolder"));
                    objTemp7.setQuestionTypeID(jsonData.getString("questionTypeID"));

                    obj = objTemp7;
                    break;

                case 8:
                    ModelTextAreaType objTemp8 = new ModelTextAreaType();
                    objTemp8.setQuestionID(jsonData.getString("questionID"));
                    objTemp8.setQuestionText(jsonData.getString("questionText"));
                    objTemp8.setIsMandatory(jsonData.getString("isMandatory"));
                    objTemp8.setQuestionType(jsonData.getString("questionType"));
                    //objTemp8.setInput_type(jsonData.getString("input_type"));
                    objTemp8.setPlaceholder(jsonData.getString("placeHolder"));
                    objTemp8.setQuestionTypeID(jsonData.getString("questionTypeID"));

                    obj = objTemp8;
                    break;

                case 9:
                    ModelSelectTimeType objTemp9 = new ModelSelectTimeType();
                    objTemp9.setQuestionID(jsonData.getString("questionID"));
                    objTemp9.setQuestionText(jsonData.getString("questionText"));
                    objTemp9.setIsMandatory(jsonData.getString("isMandatory"));
                    objTemp9.setQuestionType(jsonData.getString("questionType"));
                    //objTemp9.setInput_type(jsonData.getString("input_type"));
                    objTemp9.setPlaceholder(jsonData.getString("placeHolder"));
                    objTemp9.setQuestionTypeID(jsonData.getString("questionTypeID"));


                    JSONArray options = jsonData.getJSONArray("options");
                    ArrayList<String> time_id = new ArrayList<>();
                    ArrayList<String> times = new ArrayList<>();
                    for (int i = 0; i < options.length(); i++) {

                        JSONObject j1 = options.getJSONObject(i);

                        times.add(j1.getString("optionText"));
                        time_id.add(j1.getString("optionID"));

                    }
                    objTemp9.setOptionArray(times);
                    objTemp9.setTime_id(time_id);

                    obj = objTemp9;
                    break;


                case 10:
                    ModelAddCityType objTemp10 = new ModelAddCityType();
                    objTemp10.setQuestionID(jsonData.getString("questionID"));
                    objTemp10.setQuestionText(jsonData.getString("questionText"));
                    objTemp10.setIsMandatory(jsonData.getString("isMandatory"));
                    objTemp10.setQuestionType(jsonData.getString("questionType"));
                    //objTemp8.setInput_type(jsonData.getString("input_type"));
                    objTemp10.setPlaceholder(jsonData.getString("placeHolder"));
                    objTemp10.setQuestionTypeID(jsonData.getString("questionTypeID"));

                    obj = objTemp10;
                    break;

                case 11:
                    ModelGetDestinationLocationType objTemp11 = new ModelGetDestinationLocationType();
                    objTemp11.setQuestionID(jsonData.getString("questionID"));
                    objTemp11.setQuestionText(jsonData.getString("questionText"));
                    objTemp11.setIsMandatory(jsonData.getString("isMandatory"));
                    objTemp11.setQuestionType(jsonData.getString("questionType"));
                    //objTemp4.setInput_type(jsonData.getString("input_type"));
                    objTemp11.setPlaceholder(jsonData.getString("placeHolder"));
                    objTemp11.setQuestionTypeID(jsonData.getString("questionTypeID"));

                    obj = objTemp11;
                    break;

                case 12:
                    ModelSkillType objTemp12 = new ModelSkillType();
                    objTemp12.setQuestionID(jsonData.getString("questionID"));
                    objTemp12.setQuestionText(jsonData.getString("questionText"));
                    objTemp12.setIsMandatory(jsonData.getString("isMandatory"));
                    objTemp12.setQuestionType(jsonData.getString("questionType"));
                    //objTemp8.setInput_type(jsonData.getString("input_type"));
                    objTemp12.setPlaceholder(jsonData.getString("placeHolder"));
                    objTemp12.setQuestionTypeID(jsonData.getString("questionTypeID"));

                    obj = objTemp12;
                    break;


                case 14:
                    ModelRideLocationType objTemp13 = new ModelRideLocationType();
                    objTemp13.setQuestionID(jsonData.getString("questionID"));
                    objTemp13.setQuestionText(jsonData.getString("questionText"));
                    objTemp13.setIsMandatory(jsonData.getString("isMandatory"));
                    objTemp13.setQuestionType(jsonData.getString("questionType"));
                    //objTemp8.setInput_type(jsonData.getString("input_type"));
                    objTemp13.setPlaceholder(jsonData.getString("placeHolder"));
                    objTemp13.setQuestionTypeID(jsonData.getString("questionTypeID"));

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
    public void onPostRequestSucess(int position, String response) {


        try {

            if (position == 1) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);
                    JSONObject data = commandResult.getJSONObject("data");
                    JSONObject schedule = data.getJSONObject("schedule");
                    AppUtils.showCustomAlert(SubmitServiceQuestions.this, commandResult.getString("message"));
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

                        Intent broadcastIntent = new Intent();
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
                        finish();

                    } else if (isUniDirectional.equalsIgnoreCase("2")) {

                        if (vendor_naukri.equalsIgnoreCase("1")) {

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
                        }
                    }


                } else {

                    AppUtils.showCustomAlert(SubmitServiceQuestions.this, commandResult.getString("message"));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onPostRequestFailed(int position, String response) {
        AppUtils.showCustomAlert(SubmitServiceQuestions.this, getResources().getString(R.string.problem_server));
    }
}

