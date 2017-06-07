package com.app.justclap.fragment_types;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.justclap.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.app.justclap.interfaces.QuestionDatailInterface;
import com.app.justclap.models.ModelSelectDayType;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_select_Day extends Fragment {

    TextView text_ques, text_date, text_fullday;
    Spinner text_selectday;
    Bundle b;
    ArrayAdapter<String> adp;
    ModelSelectDayType quesDetail;
    ArrayList<ModelSelectDayType> quesList;
    ArrayList<ModelSelectDayType> daysArray;
    ArrayList<String> daysList;
    ModelSelectDayType model;
    int position;
    QuestionDatailInterface questionDatailInterface;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        b = getArguments();
        position = b.getInt("position");
        quesList = new ArrayList<>();
        daysArray = new ArrayList<>();
        daysList = new ArrayList<>();
        model = (ModelSelectDayType) questionDatailInterface.getPageDataModel(position);
        text_ques.setText(model.getQuestionText());
        model.setSelectedTime(text_selectday.getSelectedItem().toString());
        Log.e("getQuestionText", "**" + model.getQuestionText());

    }



    @Override
    public void onPause() {
        super.onPause();
        model.setQuestionText(model.getQuestionText());
        model.setSelectedTime(text_selectday.getSelectedItem().toString());
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

        View viewCategory = inflater.inflate(R.layout.fragment_select_day, container, false);
        text_ques = (TextView) viewCategory.findViewById(R.id.text_ques);
        text_selectday = (Spinner) viewCategory.findViewById(R.id.text_selectday);
        text_date = (TextView) viewCategory.findViewById(R.id.text_date);
        text_fullday = (TextView) viewCategory.findViewById(R.id.text_fullday);


        generateDateSpinnerData();

        return viewCategory;
    }

    public void generateDateSpinnerData() {
        SimpleDateFormat format = new SimpleDateFormat("EEEE - dd-MMM-yyyy");
        daysList = new ArrayList<>();

        for (int i = 0; i < 7; i++) {

            Calendar currentDate = Calendar.getInstance();
            currentDate.add(Calendar.DATE, i);
            String day = format.format(currentDate.getTime());
            Log.i("Days", day);
            daysList.add(day);

        }

        adp = new ArrayAdapter<String>(getActivity(), R.layout.row_spinner, R.id.text_time, daysList);
        text_selectday.setAdapter(adp);


    }


}
