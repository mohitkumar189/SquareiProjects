package com.app.justclap.fragment_types;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.justclap.R;

import java.util.ArrayList;

import com.app.justclap.interfaces.QuestionDatailInterface;
import com.app.justclap.models.ModelDescriptionType;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_Description extends Fragment   {

    TextView text_ques, text_desc;
    Bundle b;
    ModelDescriptionType quesDetail;
    ArrayList<ModelDescriptionType> quesList;
    ModelDescriptionType model;
    int position;

    QuestionDatailInterface questionDatailInterface;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        b = getArguments();
        position = b.getInt("position");
        quesList = new ArrayList<>();

        model=(ModelDescriptionType) questionDatailInterface.getPageDataModel(position);
        text_ques.setText(model.getQuestionText());
        text_desc.setText(Html.fromHtml(model.getBodyText()));
        Log.e("getQuestionText","**"+model.getQuestionText());

    }


    @Override
    public void onPause() {
        super.onPause();
        model.setQuestionText(model.getQuestionText());
        questionDatailInterface.setPageDataModel(position, model);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        questionDatailInterface=(QuestionDatailInterface) activity;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        View viewCategory = inflater.inflate(R.layout.fragment_description_text, container, false);
        text_ques = (TextView) viewCategory.findViewById(R.id.text_ques);
        text_desc = (TextView) viewCategory.findViewById(R.id.text_desc);

        return viewCategory;
    }



}
