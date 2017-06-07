package com.app.justclap.fragment_types;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.interfaces.QuestionDatailInterface;
import com.app.justclap.models.ModelSelectTimeType;

import java.util.ArrayList;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_select_Time extends Fragment {

    TextView text_ques, text_date;
    Spinner text_selectday;
    Bundle b;
    ModelSelectTimeType quesDetail;
    ModelSelectTimeType timeList;
    ArrayAdapter<String> adp;
    ArrayList<ModelSelectTimeType> quesList;
    ArrayList<String> timeArray;
    ModelSelectTimeType model;
    int position;
    QuestionDatailInterface questionDatailInterface;
    private Button btn_next;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        b = getArguments();

        position = b.getInt("position");
        quesList = new ArrayList<>();
        timeArray = new ArrayList<>();
        model = (ModelSelectTimeType) questionDatailInterface.getPageDataModel(position);
        text_ques.setText(model.getQuestionText());
        btn_next = (Button) view.findViewById(R.id.btn_next);

        adp = new ArrayAdapter<String>(getActivity(), R.layout.row_spinner, R.id.text_time, model.getOptionArray());
        text_selectday.setAdapter(adp);
        //model.setSelectedTimeId(text_selectday.getSelectedItemPosition());
        Log.e("getQuestionText", "**" + model.getQuestionText());
        text_selectday.setSelection(model.getSelectedTimeId());
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                questionDatailInterface.showNext(true);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        model.setQuestionText(model.getQuestionText());
        model.setSelectedTimeId(text_selectday.getSelectedItemPosition());
        Log.e("ItemSelected", text_selectday.getSelectedItemPosition() + "");
        text_selectday.setSelection(model.getSelectedTimeId());
        Log.e("modelitemposition", model.getSelectedTimeId() + "");
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

        View viewCategory = inflater.inflate(R.layout.fragment_select_time, container, false);
        text_ques = (TextView) viewCategory.findViewById(R.id.text_ques);
        text_selectday = (Spinner) viewCategory.findViewById(R.id.text_selectday);
        text_date = (TextView) viewCategory.findViewById(R.id.text_date);


        text_selectday.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return viewCategory;
    }


}
