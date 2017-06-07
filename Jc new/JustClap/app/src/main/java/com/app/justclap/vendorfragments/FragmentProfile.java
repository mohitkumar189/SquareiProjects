package com.app.justclap.vendorfragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.vendor.EditVendorprofile;

public class FragmentProfile extends Fragment {

    private TextView text_personal_info, textName, textNameValue, textEmail, textEmailValue, textPhone, textPhoneValue, textProfessionalExp, textProfessionalExpValue, textRelevantExp,
            textRelevantExpValue, textAbout, textAboutValue;
    private ImageView image_edit;

    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        image_edit = (ImageView) view.findViewById(R.id.image_edit);
        text_personal_info = (TextView) view.findViewById(R.id.text_personal_info);
        textName = (TextView) view.findViewById(R.id.textName);
        textNameValue = (TextView) view.findViewById(R.id.textNameValue);
        textEmail = (TextView) view.findViewById(R.id.textEmail);
        textEmailValue = (TextView) view.findViewById(R.id.textEmailValue);
        textPhone = (TextView) view.findViewById(R.id.textPhone);
        textPhoneValue = (TextView) view.findViewById(R.id.textPhoneValue);
        textProfessionalExp = (TextView) view.findViewById(R.id.textProfessionalExp);
        textProfessionalExpValue = (TextView) view.findViewById(R.id.textProfessionalExpValue);
        textRelevantExp = (TextView) view.findViewById(R.id.textRelevantExp);
        textRelevantExpValue = (TextView) view.findViewById(R.id.textRelevantExpValue);
        textAbout = (TextView) view.findViewById(R.id.textAbout);
        textAboutValue = (TextView) view.findViewById(R.id.textAboutValue);

        AppUtils.fontGotham_Medium(textNameValue, context);
        AppUtils.fontGotham_Medium(textEmailValue, context);
        AppUtils.fontGotham_Medium(textPhoneValue, context);
        AppUtils.fontGotham_Medium(textProfessionalExpValue, context);
        AppUtils.fontGotham_Medium(textRelevantExpValue, context);
        AppUtils.fontGotham_Medium(textAboutValue, context);
        AppUtils.fontGotham_Book(text_personal_info, context);
        AppUtils.fontGotham_Book(textName, context);
        AppUtils.fontGotham_Book(textEmail, context);
        AppUtils.fontGotham_Book(textPhone, context);
        AppUtils.fontGotham_Book(textPhoneValue, context);
        AppUtils.fontGotham_Book(textRelevantExp, context);
        AppUtils.fontGotham_Book(textAbout, context);


        image_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, EditVendorprofile.class);
                startActivity(intent);
            }
        });

    }

}
