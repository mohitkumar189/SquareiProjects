package com.app.justclap.fragment_types;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.app.justclap.R;

import java.util.ArrayList;

import com.app.justclap.adapters.AdaptercheckboxQues;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.interfaces.QuestionDatailInterface;
import com.app.justclap.models.ModelCheckboxType;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_CheckboxTypeQues extends Fragment implements OnCustomItemClicListener {

    ListView list_Quescheck;
    AdaptercheckboxQues adaptercheckboxQues;
    TextView text_ques;
    ArrayList<Boolean> getSelectedItem = new ArrayList<>();
    ModelCheckboxType quesDetail;
    QuestionDatailInterface questionDatailInterface;
    ArrayList<ModelCheckboxType> quesList;
    Bundle b;
    ModelCheckboxType model;
    int position;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        b = getArguments();
        position = b.getInt("position");
        quesList = new ArrayList<>();
        model = (ModelCheckboxType) questionDatailInterface.getPageDataModel(position);
        text_ques.setText(model.getQuestionText());
        getSelectedItem = model.getOptionSelectionArray();
        adaptercheckboxQues = new AdaptercheckboxQues(getActivity(), this, model.getOptionArray(), getSelectedItem);
        list_Quescheck.setAdapter(adaptercheckboxQues);
        Log.e("getQuestionText", "**" + model.getQuestionText());

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        View viewCategory = inflater.inflate(R.layout.fragment_checkbox_ques, container, false);
        list_Quescheck = (ListView) viewCategory.findViewById(R.id.list_Quescheck);
        text_ques = (TextView) viewCategory.findViewById(R.id.text_ques);

        return viewCategory;
    }

    @Override
    public void onItemClickListener(int pos, int flag) {

        if (flag == 2) {

            if (getSelectedItem.get(pos)) {
                getSelectedItem.set(pos, false);
                model.setOptionSelectionArray(getSelectedItem);
                questionDatailInterface.setPageDataModel(position, model);
                adaptercheckboxQues.notifyDataSetChanged();

            } else {
                getSelectedItem.set(pos, true);
                model.setOptionSelectionArray(getSelectedItem);
                questionDatailInterface.setPageDataModel(position, model);
                adaptercheckboxQues.notifyDataSetChanged();

            }

        }

    }


}
