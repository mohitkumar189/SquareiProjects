package com.app.justclap.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.justclap.R;
import com.facebook.CallbackManager;
import com.facebook.share.widget.ShareDialog;
import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.enumeration.ProfileField;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;
import com.google.code.linkedinapi.schema.Person;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.EnumSet;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

public class SharedataLinkedin extends AppCompatActivity {

    private LinkedInOAuthService oAuthService;
    private LinkedInApiClientFactory factory;
    private LinkedInRequestToken liToken;
    private LinkedInApiClient client;
    String is_naukri = "";
    LinkedInAccessToken accessToken = null;
    String shareNaukriData = "", servicename = "";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_sharedata_linkedin);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        context = this;

        Intent in = getIntent();
        shareNaukriData = in.getExtras().getString("shareNaukriData");
        servicename = in.getExtras().getString("servicename");

        oAuthService = LinkedInOAuthServiceFactory.getInstance()
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
        startActivity(i);


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
            setResult(12);
            finish();
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


}
