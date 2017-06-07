package com.app.justclap.fragment_types;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.justclap.R;

import java.util.ArrayList;

import com.app.justclap.interfaces.QuestionDatailInterface;
import com.app.justclap.models.ModelAntiType;
import com.squareup.picasso.Picasso;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_AntiTypeQues extends Fragment {

    Bundle b;
    RelativeLayout rl_top, rl_bottom;
    TextView text_top, text_bottom;
    ModelAntiType quesDetail;
    ImageView image_top, image_bottom;
    ImageView img_selectedbottom, img_selectedtop;
    ArrayList<ModelAntiType> quesList;
    ModelAntiType model;
    int position;
    ArrayList<String> optionArray = new ArrayList<String>();
    ArrayList<String> optionImage = new ArrayList<String>();
    QuestionDatailInterface questionDatailInterface;
    int selectdItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        View viewCategory = inflater.inflate(R.layout.fragment_antitype, container, false);

        text_top = (TextView) viewCategory.findViewById(R.id.text_top);
        text_bottom = (TextView) viewCategory.findViewById(R.id.text_bottom);
        img_selectedtop = (ImageView) viewCategory.findViewById(R.id.img_selectedtop);
        img_selectedbottom = (ImageView) viewCategory.findViewById(R.id.img_selectedbottom);
        image_bottom = (ImageView) viewCategory.findViewById(R.id.image_bottom);
        image_top = (ImageView) viewCategory.findViewById(R.id.image_top);
        rl_top = (RelativeLayout) viewCategory.findViewById(R.id.rl_top);
        rl_bottom = (RelativeLayout) viewCategory.findViewById(R.id.rl_bottom);

        return viewCategory;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        b = getArguments();
        position = b.getInt("position");
        quesList = new ArrayList<>();
        model = (ModelAntiType) questionDatailInterface.getPageDataModel(position);

        optionArray = model.getOptionArray();
        optionImage = model.getOptionImage();

        if (optionImage.size() > 0) {

            if (!optionImage.get(0).equalsIgnoreCase("")) {
                Picasso.with(getActivity())
                        .load(getResources().getString(R.string.img_url) + optionImage.get(0))
                        // .transform(new CircleTransform())
                        // .placeholder(R.drawable.profile_about_user)
                        .into(image_top);
            }
            if (!optionImage.get(1).equalsIgnoreCase("")) {
                Picasso.with(getActivity())
                        .load(getResources().getString(R.string.img_url) + optionImage.get(1))
                        // .transform(new CircleTransform())
                        // .placeholder(R.drawable.profile_about_user)
                        .into(image_bottom);
            }
        }

        try {
            text_top.setText(optionArray.get(0) + " >");
            text_bottom.setText(optionArray.get(1) + " >");
            selectdItem = model.getSelectedOption();

            if (selectdItem == 0) {
                img_selectedtop.setVisibility(View.VISIBLE);
                img_selectedbottom.setVisibility(View.GONE);
            }
            if (selectdItem == 1) {
                img_selectedtop.setVisibility(View.GONE);
                img_selectedbottom.setVisibility(View.VISIBLE);
            }

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
                img_selectedtop.setVisibility(View.VISIBLE);
                img_selectedbottom.setVisibility(View.GONE);
                questionDatailInterface.setPageDataModel(position, model);
            }
        });

        text_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                model.setSelectedOption(1);
                selectdItem = 1;
                img_selectedtop.setVisibility(View.GONE);
                img_selectedbottom.setVisibility(View.VISIBLE);
                questionDatailInterface.setPageDataModel(position, model);
            }
        });

        rl_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                model.setSelectedOption(0);
                selectdItem = 0;
                img_selectedtop.setVisibility(View.VISIBLE);
                img_selectedbottom.setVisibility(View.GONE);
                questionDatailInterface.setPageDataModel(position, model);
            }
        });

        rl_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                model.setSelectedOption(1);
                selectdItem = 1;
                img_selectedtop.setVisibility(View.GONE);
                img_selectedbottom.setVisibility(View.VISIBLE);
                questionDatailInterface.setPageDataModel(position, model);
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
