package com.app.lunavista.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.lunavista.AppUtils.AppUtils;
import com.app.lunavista.R;
import com.app.lunavista.asyntask.AsyncPostDataResponse;
import com.app.lunavista.interfaces.ListenerPostData;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Logiguyz on 7/10/2016.
 */
public class ForgotPassword extends AppCompatActivity implements ListenerPostData{
    Context context;
    EditText edtEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword);
        context=this;
        init();
    }
    private void init(){
        edtEmail = (EditText) findViewById(R.id.edtUsername);
        RelativeLayout rlSignUp = (RelativeLayout) findViewById(R.id.rlForgotPassword);


        rlSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email="";

                email=edtEmail.getText().toString();

                if (email.length()>0) {

                    if (!AppUtils.isEmailValid(email)){
                        Toast.makeText(context, "Please input valid email.", Toast.LENGTH_LONG).show();
                    }else {
                        ArrayList<NameValuePair> requestData = new ArrayList<NameValuePair>(2);
                        requestData.add(new BasicNameValuePair(
                                "email", email));

                        new AsyncPostDataResponse(context, 1, requestData, getString(R.string.url_base) + getString(R.string.url_forgetPassword));
                    }
                }else{
                    Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    @Override
    public void onPostRequestSucess(int position, String response) {

        if (position == 1) {
            try {
                JSONObject jObject=new JSONObject(response);
                JSONObject commandResult=jObject.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPostRequestFailed(int position, String response) {
        Toast.makeText(context, "Problem to connect server, please try again.", Toast.LENGTH_LONG).show();
    }
}
