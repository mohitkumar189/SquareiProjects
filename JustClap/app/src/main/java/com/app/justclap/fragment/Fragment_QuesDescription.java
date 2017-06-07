package com.app.justclap.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.justclap.R;

import java.util.ArrayList;

import com.app.justclap.adapters.AdaptercheckboxQues;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_QuesDescription extends Fragment implements OnCustomItemClicListener {

    AdaptercheckboxQues adaptercheckboxQues;
    TextView text_ques,text_loc;
    ModelService serviceDetail;
    ArrayList<ModelService> service_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        View viewCategory = inflater.inflate(R.layout.fragment_ques_description, container, false);
        text_ques= (TextView) viewCategory.findViewById(R.id.text_ques);
        text_loc= (TextView) viewCategory.findViewById(R.id.text_loc);
        service_list=new ArrayList<>();



        return viewCategory;
    }


    @Override
    public void onItemClickListener(int position, int flag) {


        if (flag == 1) {

        }

    }
}
