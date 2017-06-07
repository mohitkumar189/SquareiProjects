package com.app.justclap.vendor_fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.justclap.R;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.justclap.vendordetail.VendorEdit_profile;
import com.app.justclap.vendordetail.Vendor_homePage;

public class FragmentProfile extends Fragment {


    RelativeLayout rl_edit_profile;
    TextView text_name, text_email, text_phone, text_about, text_relavent_experience, text_professional_experience;
    Context context;
    Bundle b;
    Vendor_homePage activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewProfile = inflater.inflate(R.layout.fragment_fragment_profile, container, false);

        b = getArguments();
        context = getActivity();
        activity = (Vendor_homePage) getActivity();
        rl_edit_profile = (RelativeLayout) viewProfile.findViewById(R.id.rl_edit_profile);
        text_relavent_experience = (TextView) viewProfile.findViewById(R.id.text_relavent_experience);
        text_professional_experience = (TextView) viewProfile.findViewById(R.id.text_professional_experience);
        text_name = (TextView) viewProfile.findViewById(R.id.text_name);
        text_email = (TextView) viewProfile.findViewById(R.id.text_email);
        text_phone = (TextView) viewProfile.findViewById(R.id.text_phone);
        text_about = (TextView) viewProfile.findViewById(R.id.text_about);
        NestedScrollView scrollView = (NestedScrollView) viewProfile.findViewById(R.id.nest_scrollview1);
        scrollView.setFillViewport(true);
        setData();

        rl_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getActivity(), VendorEdit_profile.class);
                in.putExtra("name", text_name.getText().toString());
                in.putExtra("email", text_email.getText().toString());
                in.putExtra("about", text_about.getText().toString());
                in.putExtra("phone", text_phone.getText().toString());
                in.putExtra("professional_experience", text_professional_experience.getText().toString());
                in.putExtra("relavent_experience", text_relavent_experience.getText().toString());

                startActivityForResult(in, 51);


            }
        });


        return viewProfile;
    }


    public void setData() {

        if (b != null) {

            String data = b.getString("profile");

            try {
                JSONObject ob = new JSONObject(data);

                text_name.setText(ob.getString("name"));
                text_email.setText(ob.getString("email"));
                text_about.setText(ob.getString("about"));
                text_phone.setText(ob.getString("phone"));
                text_professional_experience.setText(ob.getString("professional_experience"));
                text_relavent_experience.setText(ob.getString("relavent_experience"));

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 51 && resultCode == 513) {

            text_name.setText(data.getExtras().getString("name"));
            text_email.setText(data.getExtras().getString("email"));
            text_about.setText(data.getExtras().getString("about"));
            text_phone.setText(data.getExtras().getString("phone"));
            text_professional_experience.setText(data.getExtras().getString("professional_experience"));
            text_relavent_experience.setText(data.getExtras().getString("relavent_experience"));


            activity.updateData(0);

        }


    }
}
