package com.app.justclap.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.justclap.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_About extends Fragment {


    ImageView image_profile;
    TextView vendor_name, text_professional, text_qualification, text_introduction;
    private Bitmap bitmap = null;
    Bundle b;
    Context context;
    RatingBar rating;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        View view_about = inflater.inflate(R.layout.fragment_about, container, false);
        context = getActivity();
        b = getArguments();
        rating = (RatingBar) view_about.findViewById(R.id.rating);
        image_profile = (ImageView) view_about.findViewById(R.id.image_profile);
        vendor_name = (TextView) view_about.findViewById(R.id.vendor_name);
        text_professional = (TextView) view_about.findViewById(R.id.text_professional);
        text_qualification = (TextView) view_about.findViewById(R.id.text_qualification);
        text_introduction = (TextView) view_about.findViewById(R.id.text_introduction);

        rating.setEnabled(false);
        setData();
        NestedScrollView scrollView = (NestedScrollView) view_about.findViewById(R.id.nest_scrollview1);
        scrollView.setFillViewport(true);
        return view_about;
    }

    public void setData() {

        if (b != null) {

            String data = b.getString("profile");

            try {
                JSONObject ob = new JSONObject(data);


                vendor_name.setText(ob.getString("vendorName"));
                text_introduction.setText(ob.getString("about"));
                text_professional.setText(ob.getString("professional_experience"));
                text_qualification.setText(ob.getString("relavent_experience"));


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }


}
