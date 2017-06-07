package com.app.justclap.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.enumeration.ProfileField;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;
import com.google.code.linkedinapi.schema.Person;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import com.app.justclap.asyntask.AsyncPostDataResponse;
import com.app.justclap.interfaces.ListenerPostData;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.utils.GPSTracker;
import com.app.justclap.vendordetail.VendorLogin;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

public class Login extends Activity implements ListenerPostData {


    TextView text_new_account, text_forgot_password, text_vendor_login;
    Context context;
    EditText edit_email, edit_password;
    RelativeLayout rl_signIn;
    ImageView image_facebook, image_linked;
    GPSTracker gTraker;
    RelativeLayout rl_main_layout, rl_network;
    private BroadcastReceiver broadcastReceiver;
    private CallbackManager callbackManager;
    double mLat, mLong;
    String flagLogin = "";
    String sName = "", sEmail = "", sId = "", sSocialType = "";
    private LinkedInOAuthService oAuthService;
    private LinkedInApiClientFactory factory;
    private LinkedInRequestToken liToken;
    private LinkedInApiClient client;


    LinkedInAccessToken accessToken=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.content_login);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        context = this;
        Intent in = getIntent();
        if (in.hasExtra("flagLogin")) {
            flagLogin = in.getExtras().getString("flagLogin");
        }
        registerFacebookCallback();
        init();

        setListener();

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
        gTraker = new GPSTracker(context);
        rl_main_layout = (RelativeLayout) findViewById(R.id.rl_main_layout);
        rl_network = (RelativeLayout) findViewById(R.id.rl_network);

        edit_email = (EditText) findViewById(R.id.emailaddress);
        edit_password = (EditText) findViewById(R.id.password);
        rl_signIn = (RelativeLayout) findViewById(R.id.rl_signIn);
        image_facebook = (ImageView) findViewById(R.id.image_facebook);

        image_linked = (ImageView) findViewById(R.id.image_linkedin);
        text_forgot_password = (TextView) findViewById(R.id.text_forgot_password);
        text_new_account = (TextView) findViewById(R.id.text_new_account);
        text_vendor_login = (TextView) findViewById(R.id.text_vendor_login);
    }


    private void setListener() {

        text_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);

            }
        });

        text_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);

            }
        });

        rl_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidLoginDetails()) {
                    if (AppUtils.isNetworkAvailable(context)) {
                        try {

                            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                                    2);

                            nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "email", edit_email
                                            .getText()
                                            .toString()));

                            nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "password", edit_password
                                            .getText()
                                            .toString()));

                            nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "deviceType", "1"));

                            nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "deviceToken", AppUtils.getGcmRegistrationKey(context)));

                            nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "latitude", "" + gTraker.getLatitude()));

                            nameValuePairs
                                    .add(new BasicNameValuePair(
                                            "longitude", "" + gTraker.getLongitude()));

                            new AsyncPostDataResponse(
                                    Login.this,
                                    1,
                                    nameValuePairs,
                                    getString(R.string.url_base_new)
                                            + getString(R.string.loginUser));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {


                        rl_main_layout.setVisibility(View.GONE);
                        rl_network.setVisibility(View.VISIBLE);

                   /*     Toast.makeText(context,
                                getString(R.string.message_network_problem),
                                Toast.LENGTH_SHORT).show();*/
                    }

                }
            }
        });

        image_linked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              /*  final LinkedInOAuthService oAuthService = LinkedInOAuthServiceFactory
                        .getInstance().createLinkedInOAuthService(Constants.CONSUMER_KEY,
                                Constants.CONSUMER_SECRET, Constants.scopeParams);
                final LinkedInApiClientFactory factory = LinkedInApiClientFactory.newInstance(
                        Config.LINKEDIN_CONSUMER_KEY,Config.LINKEDIN_CONSUMER_SECRET);*/
                 String scopeParams="rw_nus+r_basicprofile";
                oAuthService = LinkedInOAuthServiceFactory.getInstance()
                        .createLinkedInOAuthService(Constants.CONSUMER_KEY,
                                Constants.CONSUMER_SECRET);


                Log.e("oAuthService : ", "*" + oAuthService);

                factory = LinkedInApiClientFactory.newInstance(
                        Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);

                liToken = oAuthService
                        .getOAuthRequestToken(Constants.OAUTH_CALLBACK_URL);
                Log.e("liToken : ", "*" + liToken.getAuthorizationUrl());
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(liToken
                        .getAuthorizationUrl()));
                startActivity(i);

            }
        });

        image_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  Intent intent = new Intent(Login.this, SharingDataonFacebook.class);
                startActivity(intent);*/

                onFacebookLoginClick();

            }
        });
        text_vendor_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this, VendorLogin.class);
                startActivity(intent);

            }
        });

    }

    private void shareDataOnLinkedin() {
        //client.postNetworkUpdate(share);
                                /*Toast.makeText(Share.this,
                                        "Shared sucessfully", Toast.LENGTH_SHORT).show();*/

        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
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
/*
        <share ><comment > This is a test</comment ><visibility ><code > anyone </code ></
        visibility ></share >*/

       /* String myEntity = "{" +
                "\"comment\":\"Check out developer.linkedin.com! " +
                "http://linkd.in/1FC2PyG\"," +
                "\"visibility\":{" + "\"code\":\"anyone\"}" +
                "}";*/

                String myEntity = "<share>"
                + "<comment>Check out work of Logiguyz Tech!</comment>"
                              + "<content>"
              + "<title>Logiguyz Tech</title>"
              + "<description>Great mobile apps development organization</description>"
                              + "<submitted-url>www.logiguyz.com</submitted-url>"
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
            Toast.makeText(Login.this, "Shared sucessfully" + res, Toast.LENGTH_SHORT).show();
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

    private boolean isValidLoginDetails() {
        boolean isValidLoginDetails = true;

        if (!edit_email.getText().toString().equalsIgnoreCase("") && !edit_password.getText().toString().equalsIgnoreCase("")) {

            if (!AppUtils.isEmailValid(edit_email.getText().toString().trim())) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.validEmail, Toast.LENGTH_SHORT).show();
            } else if (edit_password.getText().toString().length() < 6) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.passwordLength, Toast.LENGTH_SHORT).show();
            } else {
                isValidLoginDetails = true;
            }

        } else {
            if (edit_email.getText().toString().equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.enterEmail, Toast.LENGTH_SHORT).show();
            } else if (edit_password.getText().toString().equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.enterPassword, Toast.LENGTH_SHORT).show();
            }
        }

        return isValidLoginDetails;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);
    }


    private void onFacebookLoginClick() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    private void registerFacebookCallback() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        //  Toast.makeText(SignInActivity.this, "accesstoken****" + loginResult.getAccessToken(), Toast.LENGTH_SHORT).show();
                        getProfileFromFacebook(loginResult.getAccessToken());

                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

    }

    private void getProfileFromFacebook(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.e("fb data  ", "fb data  " + object.toString());
                        try {
                            //  Toast.makeText(context, "F Login" + object.toString(), Toast.LENGTH_LONG).show();
                            String email = "";
                            if (object.has("email")) {
                                email = object.getString("email");
                            } else {
                                email = "";
                            }

                            loginToSocial("1", object.getString("name"), email, object.getString("id"));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //finish();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,first_name,last_name,gender,birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);

            Bundle b = data.getExtras();
            Log.e("Data", "&&&&&&&&&" + b.getString("name"));

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

        // client.postNetworkUpdate("LinkedIn Android app test");

        Person profile = client.getProfileForCurrentUser(EnumSet.of(
                ProfileField.ID, ProfileField.FIRST_NAME,
                ProfileField.LAST_NAME, ProfileField.HEADLINE));

        Log.e("Id :: ", profile.getId());
        Log.e("Last Name :: ", profile.getLastName());
        Log.e("Head Line :: ", profile.getHeadline());


        loginToSocial("2", profile.getFirstName(), "", profile.getId());
    }

    private void loginToSocial(String socialType, String uname, String email, String password) {

      /*  name: registrationSocial.php
        Parameters: account_type,uname,email,password,image_url,social_type(Google+ 1, facebook 2, twiter 3)
                ,device_token,device_type,latitude,longitude
        */

        try {
         /*   double lat = DataManager.getInstance(getApplicationContext()).latitude;
            double lng = DataManager.getInstance(getApplicationContext()).longitude;*/

            sName = uname;
            sEmail = email;
            sSocialType = socialType;
            sId = password;

            edit_email.setText(sEmail);
            edit_password.setText(sId);

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("name", sName));
            params.add(new BasicNameValuePair("email", sEmail));
            params.add(new BasicNameValuePair("socialID", sId));

            params.add(new BasicNameValuePair("socialType", socialType));
            params.add(new BasicNameValuePair(
                    "deviceType", "1"));

            params.add(new BasicNameValuePair(
                    "deviceToken", AppUtils.getGcmRegistrationKey(context)));

            params.add(new BasicNameValuePair(
                    "latitude", "" + mLat));

            params.add(new BasicNameValuePair(
                    "longitude", "" + mLong));

            String url = getString(R.string.url_base_new) + getString(R.string.loginSocialUser);
            // new AsyncGetDataFragment(getActivity(), this, 1, params, url);

            new AsyncPostDataResponse(this, 2, params, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostRequestSucess(int method, String response) {

        try {

            if (method == 1) {
                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    rl_main_layout.setVisibility(View.VISIBLE);
                    rl_network.setVisibility(View.GONE);

                    JSONObject data = commandResult.getJSONObject("data");
                    AppUtils.setUserId(context, data.getString("userID"));
                    AppUtils.setUserName(context, data.getString("name"));
                    AppUtils.setUserMobile(context, data.getString("mobile"));
                    AppUtils.setUseremail(context, data.getString("email"));
                    if (!data.getString("profileImage").equalsIgnoreCase("")) {
                        AppUtils.setUserImage(context, getResources().getString(R.string.img_url) + data.getString("profileImage"));
                    } else {
                        AppUtils.setUserImage(context, "");
                    }
                    Log.e("customer_id", AppUtils.getUserId(context));
                    //  setCartdata();
                    edit_email.setText("");
                    edit_password.setText("");
                    edit_password.setText("");

                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                    sendBroadcast(broadcastIntent);

                    if (flagLogin.equalsIgnoreCase("2")) {
                        Intent in = new Intent(Login.this, DashBoardActivity.class);
                        setResult(512, in);
                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //  startActivity(in);
                        finish();
                    } else {
                        Intent in = new Intent(Login.this, DashBoardActivity.class);
                        setResult(512, in);
                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(in);
                        finish();
                    }


                } else {
                    Toast.makeText(context, commandResult.getString("message"),
                            Toast.LENGTH_SHORT).show();

                }

                if (commandResult.getString("success").equalsIgnoreCase("0")) {

                    Toast.makeText(context, commandResult.getString("message"),
                            Toast.LENGTH_SHORT).show();

                }

            } else if (method == 2) {

                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    if (data.getString("is_new").equalsIgnoreCase("1")) {
                        Log.e("sendname", sName);
                        Intent intent = new Intent(Login.this, SignUp.class);
                        intent.putExtra("name", sName);
                        intent.putExtra("email", sEmail);
                        intent.putExtra("social_type", sSocialType);
                        intent.putExtra("password", sId);
                        startActivity(intent);

                    } else {
                        AppUtils.setUserId(context, data.getString("userID"));
                        AppUtils.setUserName(context, data.getString("name"));
                        AppUtils.setUserMobile(context, data.getString("mobile"));
                        AppUtils.setUseremail(context, data.getString("email"));
                        if (!data.getString("profileImage").equalsIgnoreCase("")) {
                            AppUtils.setUserImage(context, getResources().getString(R.string.img_url) + data.getString("profileImage"));
                        } else {
                            AppUtils.setUserImage(context, "");
                        }
                        // Log.e("customer_id", AppUtils.getUserId(context));
                        //  setCartdata();
                        edit_email.setText("");
                        edit_password.setText("");
                        edit_password.setText("");

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                        sendBroadcast(broadcastIntent);

                        if (flagLogin.equalsIgnoreCase("2")) {
                            Intent in = new Intent(Login.this, DashBoardActivity.class);
                            setResult(512, in);
                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            //  startActivity(in);
                            finish();
                        } else {
                            Intent in = new Intent(Login.this, DashBoardActivity.class);
                            setResult(512, in);
                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(in);
                            finish();
                        }

                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostRequestFailed(int position, String response) {
        AppUtils.showCustomAlert(Login.this, getResources().getString(R.string.problem_server));
    }
}
