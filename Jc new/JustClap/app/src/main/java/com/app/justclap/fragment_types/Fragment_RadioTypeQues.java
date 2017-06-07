package com.app.justclap.fragment_types;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.interfaces.QuestionDatailInterface;
import com.app.justclap.models.ModelRadioType;

import java.util.ArrayList;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_RadioTypeQues extends Fragment {


    TextView text_ques;
    ModelRadioType model;
    RadioGroup optionGroup;
    int position;
    QuestionDatailInterface questionDatailInterface;
    ArrayList<String> optionArray = new ArrayList<String>();
    private static final String PAGER_TAG = "PagerActivity.PAGER_TAG";
    Bundle b;
    private Button btn_next;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        View viewCategory = inflater.inflate(R.layout.fragment_radio_ques, container, false);

        text_ques = (TextView) viewCategory.findViewById(R.id.text_ques);
        optionGroup = (RadioGroup) viewCategory.findViewById(R.id.radioGroup);
        return viewCategory;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        b = getArguments();
        position = b.getInt("position");
        //   getQuestionlist();

        model = (ModelRadioType) questionDatailInterface.getPageDataModel(position);
        optionArray = model.getOptionArray();
        text_ques.setText(model.getQuestionText());

        btn_next = (Button) view.findViewById(R.id.btn_next);
        setRadioButton();
        Log.e("getQuestionText", "**" + model.getQuestionText());

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (model.getSelectedOption() != -1) {
                    questionDatailInterface.showNext(true);
                } else {

                }
            }
        });

    }

    private void setRadioButton() {
        int selectdItem = model.getSelectedOption();
        optionGroup.removeAllViews();
        for (int i = 0; i < optionArray.size(); i++) {
            RadioButton rb = new RadioButton(getActivity());
            rb.setText(optionArray.get(i));
            rb.setTag(i);
            rb.setTextSize(getResources().getDimension(R.dimen.dp8));
            rb.setTextColor(getResources().getColor(R.color.textcolordark));
            if (selectdItem == i) {
                rb.setChecked(true);
            }
            rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        int tag = Integer.parseInt(((RadioButton) buttonView).getTag().toString());
                        model.setSelectedOption(tag);
                    }
                    setRadioButton();
                }
            });
            optionGroup.addView(rb);
        }

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
        questionDatailInterface = (QuestionDatailInterface) activity;

    }


}
