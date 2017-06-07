package com.app.justclap.fragment_types;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.R;
import com.app.justclap.adapter.AdapterCityList;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.interfaces.QuestionDatailInterface;
import com.app.justclap.models.ModelAddCityType;
import com.app.justclap.utils.AppUtils;

import java.util.ArrayList;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_AddCity extends Fragment implements OnCustomItemClicListener {

    TextView text_ques, text_addcity;
    AutoCompleteTextView text_desc;
    Bundle b;
    String city = "";
    RecyclerView recycler_city;
    ModelAddCityType quesDetail;
    ArrayList<ModelAddCityType> quesList;
    ModelAddCityType model;
    int position;
    QuestionDatailInterface questionDatailInterface;
    ArrayList<String> keywordName;
    ArrayList<String> keywordsArray = new ArrayList<String>();
    ArrayAdapter adapterKeyword;
    AdapterCityList adapterCityList;
    private Context context;
    private Button btn_next;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        View viewCategory = inflater.inflate(R.layout.fragment_addcity, container, false);
        text_ques = (TextView) viewCategory.findViewById(R.id.text_ques);
        text_desc = (AutoCompleteTextView) viewCategory.findViewById(R.id.text_desc);
        context = getActivity();
        btn_next = (Button) viewCategory.findViewById(R.id.btn_next);
        recycler_city = (RecyclerView) viewCategory.findViewById(R.id.recycler_city);
        recycler_city.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        keywordName = new ArrayList<>();

        return viewCategory;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        b = getArguments();
        position = b.getInt("position");
        quesList = new ArrayList<>();
        model = (ModelAddCityType) questionDatailInterface.getPageDataModel(position);

        text_ques.setText(model.getQuestionText());
        text_desc.setHint(model.getPlaceholder());

        setListener();

        keywordsArray = model.getOptionArray();

        adapterCityList = new AdapterCityList(context, this, keywordsArray);
        recycler_city.setAdapter(adapterCityList);
        adapterKeyword = new ArrayAdapter<String>(getActivity(),
                R.layout.row_spinner, R.id.text_time, keywordName);
        text_desc.setAdapter(adapterKeyword);
        text_desc.setThreshold(1);
        Log.e("getQuestionText", "**" + model.getQuestionText());

    }

    private void setListener() {

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (keywordsArray.size() > 0) {
                    questionDatailInterface.showNext(true);
                } else {
                    Toast.makeText(context, "Please enter skill", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ((Button) getView().findViewById(R.id.btn_plus)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (text_desc.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please input city first.", Toast.LENGTH_SHORT).show();
                } else {
                    if (keywordsArray.size() >= 9) {
                        Toast.makeText(getActivity(), "You can add maximum nine cities.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (keywordsArray.contains(text_desc.getText().toString().trim())) {
                            Toast.makeText(getActivity(), "Duplicate City.", Toast.LENGTH_SHORT).show();

                        } else {

                            AppUtils.onKeyBoardDown(getActivity());
                            keywordsArray.add(text_desc.getText().toString().trim());
                            text_desc.setText("");
                            adapterCityList.notifyDataSetChanged();
                            model.setInputAnswer(getKeyWords());
                            model.setOptionArray(keywordsArray);
                            questionDatailInterface.setPageDataModel(position, model);

                            Log.e("EnterKeywords", "*" + model.getInputAnswer());
                        }

                    }
                }
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        model.setQuestionText(model.getQuestionText());
        model.setInputAnswer(getKeyWords());
        //   text_desc.setText(model.getInputAnswer());
        questionDatailInterface.setPageDataModel(position, model);
    }

    private String getKeyWords() {
        String keyW = "";
        for (int i = 0; i < keywordsArray.size(); i++) {
            if (keyW.equalsIgnoreCase("")) {
                keyW = keywordsArray.get(i);
            } else {
                keyW = keyW + "," + keywordsArray.get(i);
            }
        }
        return keyW;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        questionDatailInterface = (QuestionDatailInterface) activity;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("onActivityResult", "*" + resultCode);
        if (requestCode == 22 && resultCode == 512) {

            Log.e("getExtras", "*" + data.getExtras().getString("location"));
            city = data.getExtras().getString("location");
            if (keywordsArray.size() >= 9) {
                Toast.makeText(getActivity(), "You can add maximum nine cities.", Toast.LENGTH_SHORT).show();
            } else {
                if (keywordsArray.contains(city.trim())) {

                    Toast.makeText(getActivity(), "Duplicate City.", Toast.LENGTH_SHORT).show();

                } else {

                    keywordsArray.add(city.trim());
                    text_desc.setText("");
                    adapterCityList.notifyDataSetChanged();
                    model.setInputAnswer(getKeyWords());
                    model.setOptionArray(keywordsArray);
                    questionDatailInterface.setPageDataModel(position, model);

                    Log.e("EnterKeywords", "*" + model.getInputAnswer());
                }
            }
        }
    }

    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 1) {
            keywordsArray.remove(position);
            adapterCityList.notifyDataSetChanged();
        }
    }
}
