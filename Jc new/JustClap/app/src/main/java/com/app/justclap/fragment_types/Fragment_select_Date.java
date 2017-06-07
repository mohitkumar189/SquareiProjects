package com.app.justclap.fragment_types;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.R;
import com.app.justclap.interfaces.QuestionDatailInterface;
import com.app.justclap.models.ModelSelectDateType;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_select_Date extends Fragment implements DatePickerDialog.OnDateSetListener {

    TextView text_ques, text_date, text_selectdate;
    Bundle b;
    ArrayList<ModelSelectDateType> quesList;
    ModelSelectDateType model;
    int position;
    QuestionDatailInterface questionDatailInterface;
    private Button btn_next;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        b = getArguments();
        position = b.getInt("position");
        quesList = new ArrayList<>();
        model = (ModelSelectDateType) questionDatailInterface.getPageDataModel(position);
        text_ques.setText(model.getQuestionText());
        btn_next = (Button) view.findViewById(R.id.btn_next);
        text_selectdate.setText(model.getSelectedDate());
        Log.e("getQuestionText", "**" + model.getQuestionText());
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!text_selectdate.getText().toString().equalsIgnoreCase("")) {
                    questionDatailInterface.showNext(true);
                } else {
                    Toast.makeText(getActivity(), "Please select date", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        model.setQuestionText(model.getQuestionText());
        text_selectdate.setText(model.getSelectedDate());
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

        View viewCategory = inflater.inflate(R.layout.fragment_select_date, container, false);
        text_ques = (TextView) viewCategory.findViewById(R.id.text_ques);
        text_selectdate = (TextView) viewCategory.findViewById(R.id.text_selectdate);
        text_date = (TextView) viewCategory.findViewById(R.id.text_date);

        Calendar now = Calendar.getInstance();
        int month = now.get(Calendar.MONTH) + 1;
        text_selectdate.setText(now.get(Calendar.DAY_OF_MONTH) + "/" + month + "/" + now.get(Calendar.YEAR));
        text_selectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
       /*     datePicker = (DatePicker) findViewById(R.id.datePicker);
              datePicker.setMinDate(System.currentTimeMillis());*/
                    Calendar now = Calendar.getInstance();


                    DatePickerDialog dpd = DatePickerDialog.newInstance(Fragment_select_Date.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                    dpd.setMinDate(now);

                    dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        return viewCategory;
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {


        int month = monthOfYear + 1;
        model.setSelectedDate(dayOfMonth + "/" + month + "/" + year);
        text_selectdate.setText(model.getSelectedDate());


    }
}
