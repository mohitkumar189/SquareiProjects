package com.app.justclap.fragment_types;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.interfaces.QuestionDatailInterface;
import com.app.justclap.models.ModelFlatType;

import java.util.ArrayList;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_FlatTypeQues extends Fragment {

    Bundle b;
    TextView text_top, text_bottom, text_title;
    ModelFlatType quesDetail;
    ArrayList<ModelFlatType> quesList;
    ModelFlatType model;
    int position;
    ArrayList<String> optionArray = new ArrayList<String>();
    ArrayList<String> optionImage = new ArrayList<String>();
    QuestionDatailInterface questionDatailInterface;
    int selectdItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        View viewCategory = inflater.inflate(R.layout.fragment_flattype, container, false);

        text_top = (TextView) viewCategory.findViewById(R.id.text_top);
        text_bottom = (TextView) viewCategory.findViewById(R.id.text_bottom);
        text_title = (TextView) viewCategory.findViewById(R.id.text_title);

        return viewCategory;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        b = getArguments();
        position = b.getInt("position");
        quesList = new ArrayList<>();
        model = (ModelFlatType) questionDatailInterface.getPageDataModel(position);

        optionArray = model.getOptionArray();
        optionImage = model.getOptionImage();

        if (optionImage.size() > 0) {

            if (!optionImage.get(0).equalsIgnoreCase("")) {

            }
            if (!optionImage.get(1).equalsIgnoreCase("")) {

            }
        }

        try {
            text_top.setText(optionArray.get(0) + " >");
            text_bottom.setText(optionArray.get(1) + " >");
            selectdItem = model.getSelectedOption();

        } catch (Exception e) {
            e.printStackTrace();
        }
        setListener();
    }

    private void setListener() {
        text_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                model.setSelectedOption(0);
                selectdItem = 0;
                questionDatailInterface.setPageDataModel(position, model);
                questionDatailInterface.showNext(true);

            }
        });

        text_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                model.setSelectedOption(1);
                selectdItem = 1;
                questionDatailInterface.setPageDataModel(position, model);
                questionDatailInterface.showNext(true);
            }
        });


    }

    @Override
    public void onPause() {
        super.onPause();
        model.setQuestionText(model.getQuestionText());
        model.setSelectedOption(selectdItem);
        questionDatailInterface.setPageDataModel(position, model);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        questionDatailInterface = (QuestionDatailInterface) activity;

    }


}
