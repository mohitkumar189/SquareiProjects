package com.app.justclap.fragment_types;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.app.justclap.R;

import java.util.ArrayList;

import com.app.justclap.interfaces.QuestionDatailInterface;
import com.app.justclap.models.ModelTextAreaType;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_TextArea extends Fragment  {

    TextView text_ques ;
    EditText text_desc;
    Bundle b;
    ModelTextAreaType quesDetail;
    ArrayList<ModelTextAreaType> quesList;
    ModelTextAreaType model;
    int position;
    QuestionDatailInterface questionDatailInterface;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        b = getArguments();
        position = b.getInt("position");
        quesList = new ArrayList<>();
        model = (ModelTextAreaType) questionDatailInterface.getPageDataModel(position);

        text_ques.setText(model.getQuestionText());
        text_desc.setHint(model.getPlaceholder());

        Log.e("getQuestionText", "**" + model.getQuestionText());

    }



    @Override
    public void onPause() {
        super.onPause();
        model.setQuestionText(model.getQuestionText());
        model.setInputAnswer(text_desc.getText().toString());
        text_desc.setText(model.getInputAnswer());
        questionDatailInterface.setPageDataModel(position, model);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        questionDatailInterface = (QuestionDatailInterface) activity;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        View viewCategory = inflater.inflate(R.layout.fragment_textarea, container, false);
        text_ques = (TextView) viewCategory.findViewById(R.id.text_ques);
        text_desc = (EditText) viewCategory.findViewById(R.id.text_desc);

        text_desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                model.setInputAnswer(s.toString());

                questionDatailInterface.setPageDataModel(position, model);

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        return viewCategory;
    }



}
