package com.app.lunavista.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.lunavista.AppUtils.AppUtils;
import com.app.lunavista.AppUtils.GPSTracker;
import com.app.lunavista.R;
import com.app.lunavista.asyntask.AsyncPostDataResponse;
import com.app.lunavista.interfaces.ListenerPostData;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Logiguyz on 7/10/2016.
 */
public class Login extends AppCompatActivity implements ListenerPostData, GoogleApiClient.OnConnectionFailedListener {

    Context context;
    SharedPreferences.Editor ed;
    EditText edtUsername, edtPassword;
    TextView textView, textViewTc, txtForgotpassword;
    RelativeLayout rlLogin, rlFacebook, rlGoogle;
    private CallbackManager callbackManager;
    String sName = "", sEmail = "", sId = "", sSocialType = "";
    GPSTracker gTraker;
    private GoogleApiClient mGoogleApiClient;
    double mLat, mLong;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.login);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        context = this;
        init();
        registerFacebookCallback();

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

    private void init() {

        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

        textView = (TextView) findViewById(R.id.txtSignUp);
        textViewTc = (TextView) findViewById(R.id.txtTC);
        txtForgotpassword = (TextView) findViewById(R.id.txtForgotpassword);

        rlLogin = (RelativeLayout) findViewById(R.id.rlLogin);
        rlFacebook = (RelativeLayout) findViewById(R.id.rlFacebook);
        rlGoogle = (RelativeLayout) findViewById(R.id.rlGoogle);

        SpannableString content = new SpannableString("Sign up");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);


        final SpannableString text = new SpannableString("I agree to the Term & Conditions & Privacy Policy");
        text.setSpan(new RelativeSizeSpan(0.95f), 0, 15,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new UnderlineSpan(), 15, 32, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new RelativeSizeSpan(0.95f), 33, 34,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new UnderlineSpan(), 35, 49, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(context, TermsConditions.class));
            }
        };

        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(context, PrivecyPolicy.class));
            }
        };


        text.setSpan(clickableSpan1, 15, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(clickableSpan2, 35, 49, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textViewTc.setText(text);
        textViewTc.setMovementMethod(LinkMovementMethod.getInstance());


    }

    private void setListener() {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, SignUp.class));
            }
        });


        txtForgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ForgotPassword.class));
            }
        });

        rlFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFacebookLoginClick();

            }
        });

        rlGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);


            }
        });


        rlLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = "", password = "";
                username = edtUsername.getText().toString();
                password = edtPassword.getText().toString();

                if (username.length() > 0 && password.length() > 0) {

                    ArrayList<NameValuePair> requestData = new ArrayList<NameValuePair>(2);
                    requestData.add(new BasicNameValuePair(
                            "email", username));
                    requestData.add(new BasicNameValuePair(
                            "password", password));
                    requestData.add(new BasicNameValuePair(
                            "latitude", "28.21"));
                    requestData.add(new BasicNameValuePair(
                            "longitude", "78.54"));
                    requestData.add(new BasicNameValuePair(
                            "device_type", "Android"));
                    requestData.add(new BasicNameValuePair(
                            "device_token", "nnxsnsknksxkmxskmxsksxk"));
                    new AsyncPostDataResponse(context, 1, requestData, getString(R.string.url_base) + getString(R.string.url_login));

                } else {
                    Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_LONG).show();
                }

            }
        });


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
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.e("googleresult", "**" + result.getStatus().getStatusCode());
            int statusCode = result.getStatus().getStatusCode();
            handleSignInResult(result);

        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.

            GoogleSignInAccount acct = result.getSignInAccount();
            //     Toast.makeText(context, acct.getDisplayName(), Toast.LENGTH_SHORT).show();
            Log.e("nameFromgoogle", acct.getDisplayName());
            Log.e("emailFromgoogle", acct.getEmail());
            Log.e("IDFromgoogle", acct.getId());

            try {
                sName = acct.getDisplayName();
                sEmail = acct.getEmail();
                sSocialType = "1";
                sId = acct.getId();

                loginToSocial("2", sName, sEmail, sId);

            } catch (Exception e) {
                e.printStackTrace();
            }


            //  mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    private void loginToSocial(String socialType, String uname, String email, String password) {

        try {


            sName = uname;
            sEmail = email;
            sSocialType = socialType;
            sId = password;
            edtUsername.setText(sEmail);
            edtPassword.setText(sId);

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("password", sId));

            params.add(new BasicNameValuePair("socialType", socialType));
            params.add(new BasicNameValuePair(
                    "device_type", "1"));

            params.add(new BasicNameValuePair(
                    "device_token", AppUtils.getGcmRegistrationKey(context)));

            params.add(new BasicNameValuePair(
                    "latitude", "" + mLat));

            params.add(new BasicNameValuePair(
                    "longitude", "" + mLong));

            String url = getString(R.string.url_base) + getString(R.string.socialLogin);
            // new AsyncGetDataFragment(getActivity(), this, 1, params, url);

            new AsyncPostDataResponse(this, 2, params, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostRequestSucess(int position, String response) {
        try {
            if (position == 1) {

                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    JSONObject data = commandResult.getJSONObject("data");
                    AppUtils.setUserId(context, data.getString("Id"));
                    AppUtils.setUserName(context, data.getString("Name"));
                    AppUtils.setUserEmail(context, data.getString("Email"));
                    AppUtils.setSongsList(context, data.getJSONObject("Songs").toString());
                    AppUtils.setPlayList(context, data.getJSONObject("Playlist").toString());
                    AppUtils.setUserImage(context, data.getString("ProfileImage"));

                    Intent intent = new Intent(context, Home.class);
                    intent.putExtra("songs", data.getJSONObject("Songs").toString());
                    startActivity(intent);
                    finish();
                    SharedPreferences pref = getSharedPreferences("new", MODE_PRIVATE);
                    ed = pref.edit();
                    ed.putBoolean("Deactivate", true);
                    ed.commit();

                } else {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_LONG).show();
                }


            } else if (position == 2) {

                JSONObject jObject = new JSONObject(response);
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    //   JSONObject data = commandResult.getJSONObject("data");
                    if (commandResult.getString("is_new_user").equalsIgnoreCase("1")) {
                        Log.e("sendname", sName);
                        Intent intent = new Intent(Login.this, SignUp.class);
                        intent.putExtra("name", sName);
                        intent.putExtra("email", sEmail);
                        intent.putExtra("social_type", sSocialType);
                        intent.putExtra("password", sId);
                        startActivity(intent);

                    } else {

                        JSONObject data = commandResult.getJSONObject("data");

                        AppUtils.setUserId(context, data.getString("Id"));
                        AppUtils.setUserName(context, data.getString("Name"));
                        AppUtils.setUserEmail(context, data.getString("Email"));
                        AppUtils.setSongsList(context, data.getJSONObject("Songs").toString());
                        AppUtils.setPlayList(context, data.getJSONObject("Playlist").toString());
                     //   AppUtils.setUserImage(context, data.getString("ProfileImage"));

                        Intent intent = new Intent(context, Home.class);
                        //  intent.putExtra("songs", data.getJSONObject("Songs").toString());
                        startActivity(intent);
                        finish();
                        SharedPreferences pref = getSharedPreferences("new", MODE_PRIVATE);
                        ed = pref.edit();
                        ed.putBoolean("Deactivate", true);
                        ed.commit();

                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostRequestFailed(int position, String response) {
        Toast.makeText(context, "Problem to connect server, please try again.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
