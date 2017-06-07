package com.app.justclap.fragment_types;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.justclap.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelSummaryType;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_Summary extends Fragment implements OnCustomItemClicListener {

    TextView text_ques, text_desc;
    Bundle b;
    ModelSummaryType quesDetail;
    ArrayList<ModelSummaryType> quesList;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        b = getArguments();
        quesList = new ArrayList<>();
       // getQuestionlist();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        View viewCategory = inflater.inflate(R.layout.fragment_summary, container, false);
        text_ques = (TextView) viewCategory.findViewById(R.id.text_ques);
        text_desc = (TextView) viewCategory.findViewById(R.id.text_desc);

        return viewCategory;
    }

    @Override
    public void onItemClickListener(int position, int flag) {


        if (flag == 1) {


        } else if (flag == 2) {

            /*for (int i = 0; i < quesList.size(); i++) {


                Log.e("size_alldetail", quesList.size() + "");

                if (getSelectedItem.contains(quesList.get(i).getOptionID())) {
                    getSelectedItem.remove(quesList.get(i).getOptionID());
                    quesList.get(i).setSelection_position(0);
                } else {
                    quesList.get(i).setSelection_position(1);
                    getSelectedItem.add(quesList.get(position).getOptionID());
                }
                adaptercheckboxQues.notifyDataSetChanged();
            }

*/
        }

    }

    private void getQuestionlist() {


        if (b != null) {

            String data = b.getString("ques");

            try {

                JSONObject jo = new JSONObject(data);
                Log.e("jo", "**" + jo);

                quesDetail = new ModelSummaryType();

                //  quesDetail.setQuestionArray(jo.toString());
              /*  quesDetail.setIsMandatory(jo.getString("isMandatory"));
                quesDetail.setQuestionText(jo.getString("questionText"));
                text_ques.setText(jo.getString("questionText"));
                quesDetail.setQuestionID(jo.getString("questionID"));
                quesDetail.setQuestionTypeID(jo.getString("questionTypeID"));
                quesDetail.setQuestionType(jo.getString("questionType"));
                quesDetail.setPlaceholder(jo.getString("placeHolder"));
                quesDetail.setOptionArray(jo.getJSONArray("options").toString());
                JSONObject options = jo.getJSONObject("options");
*/
                text_ques.setText(jo.getString("questionText"));
                text_desc.setHint(jo.getString("placeHolder"));
                //  Log.e("QuesList11", "**" + quesList.size() + quesList.get(1).getQuestionTypeID());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


}
