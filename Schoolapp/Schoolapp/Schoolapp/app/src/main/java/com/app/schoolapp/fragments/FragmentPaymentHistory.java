package com.app.schoolapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.schoolapp.R;
import com.app.schoolapp.adapter.AdapterPaymentHistory;
import com.app.schoolapp.interfaces.OnCustomItemClicListener;
import com.app.schoolapp.model.ModelTimeTable;

import java.util.ArrayList;

public class FragmentPaymentHistory extends Fragment implements OnCustomItemClicListener {


    private RecyclerView recyclerView;
    private ArrayList<ModelTimeTable> listAppointments;
    private ModelTimeTable model;
    private AdapterPaymentHistory adapterPaymentHistory;
    private Context context;
    private ArrayList<String> periodName;
    private ArrayList<String> daysList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        listAppointments = new ArrayList<>();
        periodName = new ArrayList<>();
        daysList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        setListener();
        setData();

    }

    private void setListener() {


    }

    private void setData() {
        model = new ModelTimeTable();
        model.setDaysArray("");

        listAppointments.add(model);

        listAppointments.add(model);
        listAppointments.add(model);
        listAppointments.add(model);
        listAppointments.add(model);
        listAppointments.add(model);
        listAppointments.add(model);

        adapterPaymentHistory = new AdapterPaymentHistory(context, this, listAppointments, periodName, daysList);
        recyclerView.setAdapter(adapterPaymentHistory);

    }


    @Override
    public void onItemClickListener(int position, int flag) {

    }
}
