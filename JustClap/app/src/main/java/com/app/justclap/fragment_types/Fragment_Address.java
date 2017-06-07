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
import com.app.justclap.models.ModelAddressType;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_Address extends Fragment {

    TextView text_ques;
    Bundle b;
    EditText text_desc;
    ModelAddressType quesDetail;
    ArrayList<ModelAddressType> quesList;
    ModelAddressType model;
    int position;
    QuestionDatailInterface questionDatailInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        View viewCategory = inflater.inflate(R.layout.fragment_address, container, false);
        text_ques = (TextView) viewCategory.findViewById(R.id.text_ques);
        text_desc = (EditText) viewCategory.findViewById(R.id.text_desc);

        return viewCategory;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        b = getArguments();

        position = b.getInt("position");
        quesList = new ArrayList<>();
        model = (ModelAddressType) questionDatailInterface.getPageDataModel(position);
        text_ques.setText(model.getQuestionText());
        text_desc.setHint(model.getPlaceholder());
        Log.e("getQuestxtaddress", "**" + model.getAnswer());

        text_desc.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                model.setAnswer(s.toString());
                questionDatailInterface.setPageDataModel(position, model);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });


    }


    @Override
    public void onPause() {
        super.onPause();
        model.setQuestionText(model.getQuestionText());
        model.setAnswer(text_desc.getText().toString());
        text_desc.setText(model.getAnswer());
        Log.e("getQuestxtaddressPause", "**" + model.getAnswer());
        questionDatailInterface.setPageDataModel(position, model);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        questionDatailInterface = (QuestionDatailInterface) activity;

    }


}
