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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.justclap.R;

import java.util.ArrayList;

import com.app.justclap.interfaces.QuestionDatailInterface;
import com.app.justclap.models.ModelSkillType;
import com.app.justclap.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_SkillType extends Fragment {

    TextView text_ques;
    AutoCompleteTextView text_desc;
    Bundle b;
    ModelSkillType quesDetail;
    ArrayList<ModelSkillType> quesList;
    ModelSkillType model;
    int position;
    ArrayAdapter adapterKeyword;
    QuestionDatailInterface questionDatailInterface;
    ArrayList<String> keywordName;
    RelativeLayout rlKey1, rlKey2, rlKey3, rlKey4, rlKey5, rlKey6, rlKey7, rlKey8, rlKey9;
    TextView btnKey1, btnKey2, btnKey3, btnKey4, btnKey5, btnKey6, btnKey7, btnKey8, btnKey9;
    ArrayList<String> keywordsArray = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        View viewCategory = inflater.inflate(R.layout.fragment_skilltype, container, false);
        text_ques = (TextView) viewCategory.findViewById(R.id.text_ques);
        text_desc = (AutoCompleteTextView) viewCategory.findViewById(R.id.text_desc);

        return viewCategory;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        b = getArguments();
        position = b.getInt("position");
        quesList = new ArrayList<>();
        keywordName = new ArrayList<>();
        model = (ModelSkillType) questionDatailInterface.getPageDataModel(position);

        text_ques.setText(model.getQuestionText());
        text_desc.setHint(model.getPlaceholder());

        initKeyWords();
        setListener();

        keywordsArray = model.getOptionArray();
        setKeyWordsLayout();

        String data = AppUtils.getSkills(getActivity());
        JSONArray skillsname = null;
        try {
            skillsname = new JSONArray(data);

            keywordName = new ArrayList<String>();

            for (int i = 0; i < skillsname.length(); i++) {

                keywordName.add(skillsname.getString(i));

            }

            adapterKeyword = new ArrayAdapter<String>(getActivity(),
                    R.layout.row_spinner, R.id.text_time, keywordName);
            text_desc.setAdapter(adapterKeyword);
            text_desc.setThreshold(1);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.e("getQuestionText", "**" + model.getQuestionText());

    }

    private void setListener() {

        text_desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        ((Button) getView().findViewById(R.id.btn_plus)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (text_desc.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please input keyword first.", Toast.LENGTH_SHORT).show();
                } else {
                    if (keywordsArray.size() >= 9) {
                        Toast.makeText(getActivity(), "You can add maximum nine keywords.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (keywordsArray.contains(text_desc.getText().toString().trim())) {

                            Toast.makeText(getActivity(), "Duplicate Skill.", Toast.LENGTH_SHORT).show();

                        } else {

                            AppUtils.onKeyBoardDown(getActivity());
                            keywordsArray.add(text_desc.getText().toString().trim());
                            text_desc.setText("");
                            setKeyWordsLayout();
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

    private void initKeyWords() {
        rlKey1 = (RelativeLayout) getView().findViewById(R.id.ll_cross1);
        rlKey2 = (RelativeLayout) getView().findViewById(R.id.ll_cross2);
        rlKey3 = (RelativeLayout) getView().findViewById(R.id.ll_cross3);
        rlKey4 = (RelativeLayout) getView().findViewById(R.id.ll_cross4);
        rlKey5 = (RelativeLayout) getView().findViewById(R.id.ll_cross5);
        rlKey6 = (RelativeLayout) getView().findViewById(R.id.ll_cross6);
        rlKey7 = (RelativeLayout) getView().findViewById(R.id.ll_cross7);
        rlKey8 = (RelativeLayout) getView().findViewById(R.id.ll_cross8);
        rlKey9 = (RelativeLayout) getView().findViewById(R.id.ll_cross9);


        btnKey1 = (TextView) getView().findViewById(R.id.btn_1);
        btnKey2 = (TextView) getView().findViewById(R.id.btn_2);
        btnKey3 = (TextView) getView().findViewById(R.id.btn_3);
        btnKey4 = (TextView) getView().findViewById(R.id.btn_4);
        btnKey5 = (TextView) getView().findViewById(R.id.btn_5);
        btnKey6 = (TextView) getView().findViewById(R.id.btn_6);
        btnKey7 = (TextView) getView().findViewById(R.id.btn_7);
        btnKey8 = (TextView) getView().findViewById(R.id.btn_8);
        btnKey9 = (TextView) getView().findViewById(R.id.btn_9);

        ((ImageView) getView().findViewById(R.id.btn_cross1)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                keywordsArray.remove(btnKey1.getText().toString());
                setKeyWordsLayout();
            }
        });


        ((ImageView) getView().findViewById(R.id.btn_cross2)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                keywordsArray.remove(btnKey2.getText().toString());
                setKeyWordsLayout();
            }
        });

        ((ImageView) getView().findViewById(R.id.btn_cross3)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                keywordsArray.remove(btnKey3.getText().toString());
                setKeyWordsLayout();
            }
        });

        ((ImageView) getView().findViewById(R.id.btn_cross4)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                keywordsArray.remove(btnKey4.getText().toString());
                setKeyWordsLayout();
            }
        });

        ((ImageView) getView().findViewById(R.id.btn_cross5)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                keywordsArray.remove(btnKey5.getText().toString());
                setKeyWordsLayout();
            }
        });

        ((ImageView) getView().findViewById(R.id.btn_cross6)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                keywordsArray.remove(btnKey6.getText().toString());
                setKeyWordsLayout();
            }
        });

        ((ImageView) getView().findViewById(R.id.btn_cross7)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                keywordsArray.remove(btnKey7.getText().toString());
                setKeyWordsLayout();
            }
        });

        ((ImageView) getView().findViewById(R.id.btn_cross8)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                keywordsArray.remove(btnKey8.getText().toString());
                setKeyWordsLayout();
            }
        });

        ((ImageView) getView().findViewById(R.id.btn_cross9)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                keywordsArray.remove(btnKey9.getText().toString());
                setKeyWordsLayout();
            }
        });
    }

    private void setKeyWordsLayout() {

        rlKey1.setVisibility(View.GONE);
        rlKey2.setVisibility(View.GONE);
        rlKey3.setVisibility(View.GONE);
        rlKey4.setVisibility(View.GONE);
        rlKey5.setVisibility(View.GONE);
        rlKey6.setVisibility(View.GONE);
        rlKey7.setVisibility(View.GONE);
        rlKey8.setVisibility(View.GONE);
        rlKey9.setVisibility(View.GONE);

        model.setInputAnswer(getKeyWords());
        model.setOptionArray(keywordsArray);
        questionDatailInterface.setPageDataModel(position, model);

        switch (keywordsArray.size()) {

            case 1:
                rlKey1.setVisibility(View.VISIBLE);

                btnKey1.setText(keywordsArray.get(0));
                break;

            case 2:
                rlKey1.setVisibility(View.VISIBLE);
                rlKey2.setVisibility(View.VISIBLE);

                btnKey1.setText(keywordsArray.get(0));
                btnKey2.setText(keywordsArray.get(1));
                break;


            case 3:
                rlKey1.setVisibility(View.VISIBLE);
                rlKey2.setVisibility(View.VISIBLE);
                rlKey3.setVisibility(View.VISIBLE);


                btnKey1.setText(keywordsArray.get(0));
                btnKey2.setText(keywordsArray.get(1));
                btnKey3.setText(keywordsArray.get(2));
                break;

            case 4:
                rlKey1.setVisibility(View.VISIBLE);
                rlKey2.setVisibility(View.VISIBLE);
                rlKey3.setVisibility(View.VISIBLE);
                rlKey4.setVisibility(View.VISIBLE);


                btnKey1.setText(keywordsArray.get(0));
                btnKey2.setText(keywordsArray.get(1));
                btnKey3.setText(keywordsArray.get(2));
                btnKey4.setText(keywordsArray.get(3));

                break;

            case 5:

                rlKey1.setVisibility(View.VISIBLE);
                rlKey2.setVisibility(View.VISIBLE);
                rlKey3.setVisibility(View.VISIBLE);
                rlKey4.setVisibility(View.VISIBLE);
                rlKey5.setVisibility(View.VISIBLE);

                btnKey1.setText(keywordsArray.get(0));
                btnKey2.setText(keywordsArray.get(1));
                btnKey3.setText(keywordsArray.get(2));
                btnKey4.setText(keywordsArray.get(3));
                btnKey5.setText(keywordsArray.get(4));
                break;

            case 6:
                rlKey1.setVisibility(View.VISIBLE);
                rlKey2.setVisibility(View.VISIBLE);
                rlKey3.setVisibility(View.VISIBLE);
                rlKey4.setVisibility(View.VISIBLE);
                rlKey5.setVisibility(View.VISIBLE);
                rlKey6.setVisibility(View.VISIBLE);


                btnKey1.setText(keywordsArray.get(0));
                btnKey2.setText(keywordsArray.get(1));
                btnKey3.setText(keywordsArray.get(2));
                btnKey4.setText(keywordsArray.get(3));
                btnKey5.setText(keywordsArray.get(4));
                btnKey6.setText(keywordsArray.get(5));

                break;

            case 7:
                rlKey1.setVisibility(View.VISIBLE);
                rlKey2.setVisibility(View.VISIBLE);
                rlKey3.setVisibility(View.VISIBLE);
                rlKey4.setVisibility(View.VISIBLE);
                rlKey5.setVisibility(View.VISIBLE);
                rlKey6.setVisibility(View.VISIBLE);
                rlKey7.setVisibility(View.VISIBLE);

                btnKey1.setText(keywordsArray.get(0));
                btnKey2.setText(keywordsArray.get(1));
                btnKey3.setText(keywordsArray.get(2));
                btnKey4.setText(keywordsArray.get(3));
                btnKey5.setText(keywordsArray.get(4));
                btnKey6.setText(keywordsArray.get(5));
                btnKey7.setText(keywordsArray.get(6));

                break;

            case 8:
                rlKey1.setVisibility(View.VISIBLE);
                rlKey2.setVisibility(View.VISIBLE);
                rlKey3.setVisibility(View.VISIBLE);
                rlKey4.setVisibility(View.VISIBLE);
                rlKey5.setVisibility(View.VISIBLE);
                rlKey6.setVisibility(View.VISIBLE);
                rlKey7.setVisibility(View.VISIBLE);
                rlKey8.setVisibility(View.VISIBLE);


                btnKey1.setText(keywordsArray.get(0));
                btnKey2.setText(keywordsArray.get(1));
                btnKey3.setText(keywordsArray.get(2));
                btnKey4.setText(keywordsArray.get(3));
                btnKey5.setText(keywordsArray.get(4));
                btnKey6.setText(keywordsArray.get(5));
                btnKey7.setText(keywordsArray.get(6));
                btnKey8.setText(keywordsArray.get(7));

                break;

            case 9:
                rlKey1.setVisibility(View.VISIBLE);
                rlKey2.setVisibility(View.VISIBLE);
                rlKey3.setVisibility(View.VISIBLE);
                rlKey4.setVisibility(View.VISIBLE);
                rlKey5.setVisibility(View.VISIBLE);
                rlKey6.setVisibility(View.VISIBLE);
                rlKey7.setVisibility(View.VISIBLE);
                rlKey8.setVisibility(View.VISIBLE);
                rlKey9.setVisibility(View.VISIBLE);

                btnKey1.setText(keywordsArray.get(0));
                btnKey2.setText(keywordsArray.get(1));
                btnKey3.setText(keywordsArray.get(2));
                btnKey4.setText(keywordsArray.get(3));
                btnKey5.setText(keywordsArray.get(4));
                btnKey6.setText(keywordsArray.get(5));
                btnKey7.setText(keywordsArray.get(6));
                btnKey8.setText(keywordsArray.get(7));
                btnKey9.setText(keywordsArray.get(8));

                break;


        }
    }

    @Override
    public void onPause() {
        super.onPause();
        model.setQuestionText(model.getQuestionText());
        model.setInputAnswer(getKeyWords());
        //  text_desc.setText(model.getInputAnswer());
        questionDatailInterface.setPageDataModel(position, model);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        questionDatailInterface = (QuestionDatailInterface) activity;

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


}
