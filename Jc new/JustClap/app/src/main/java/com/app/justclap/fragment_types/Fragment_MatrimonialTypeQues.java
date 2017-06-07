package com.app.justclap.fragment_types;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.interfaces.QuestionDatailInterface;
import com.app.justclap.models.ModelMatimonialType;
import com.app.justclap.utils.AppUtils;

import java.util.ArrayList;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_MatrimonialTypeQues extends Fragment {

    Bundle b;
    TextView text_top;
    ModelMatimonialType quesDetail;
    ArrayList<ModelMatimonialType> quesList;
    ModelMatimonialType model;
    private ImageView image_left, image_right;
    int position;
    ArrayList<String> optionArray = new ArrayList<String>();
    ArrayList<String> optionImage = new ArrayList<String>();
    QuestionDatailInterface questionDatailInterface;
    int selectdItem;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        View viewCategory = inflater.inflate(R.layout.fragment_matrimonialtype, container, false);
        context = getActivity();
        text_top = (TextView) viewCategory.findViewById(R.id.text_top);
        image_left = (ImageView) viewCategory.findViewById(R.id.image_left);
        image_right = (ImageView) viewCategory.findViewById(R.id.image_right);
        AppUtils.fontGotham_Medium(text_top, context);
        return viewCategory;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        b = getArguments();
        position = b.getInt("position");
        quesList = new ArrayList<>();
        model = (ModelMatimonialType) questionDatailInterface.getPageDataModel(position);

        optionArray = model.getOptionArray();
        optionImage = model.getOptionImage();

        try {
            text_top.setText(optionArray.get(0) + " >");
            selectdItem = model.getSelectedOption();

        } catch (Exception e) {
            e.printStackTrace();
        }
        setListener();
    }

    private void setListener() {
        image_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                model.setSelectedOption(0);
                selectdItem = 0;
                questionDatailInterface.setPageDataModel(position, model);
                questionDatailInterface.showNext(true);
            }
        });

        image_right.setOnClickListener(new View.OnClickListener() {
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
