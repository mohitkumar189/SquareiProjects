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
import com.app.schoolapp.adapter.AdapterWeeklyResult;
import com.app.schoolapp.interfaces.OnCustomItemClicListener;
import com.app.schoolapp.model.ModelTimeTable;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

public class FragmentWeeklyResult extends Fragment implements OnCustomItemClicListener {


    private RecyclerView recyclerView;
    private ArrayList<ModelTimeTable> listAppointments;
    private ModelTimeTable model;
    private AdapterWeeklyResult adapterFixedAppointment;
    private Context context;
    private ArrayList<String> periodName;
    private ArrayList<String> daysList;
    private GraphView graph;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weekly_result, container, false);
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

        graph = (GraphView) view.findViewById(R.id.graph);

        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, 5),
                new DataPoint(2, 3),
                new DataPoint(4, 6)
        });
       // series.setColor(getResources().getColor(R.color.red_color));

        BarGraphSeries<DataPoint> series1 = new BarGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(1, 2),
                new DataPoint(3, 4),
        });
     //   series.setColor(getResources().getColor(R.color.blue_color));
        graph.addSeries(series);
        graph.addSeries(series1);

    }

    private void setListener() {


    }

    private void setData() {
/*        model = new ModelTimeTable();
        model.setDaysArray("");

        listAppointments.add(model);

        listAppointments.add(model);
        listAppointments.add(model);
        listAppointments.add(model);
        listAppointments.add(model);
        listAppointments.add(model);
        listAppointments.add(model);

        adapterFixedAppointment = new AdapterWeeklyResult(context, this, listAppointments, periodName, daysList);
        recyclerView.setAdapter(adapterFixedAppointment);*/

    }


    @Override
    public void onItemClickListener(int position, int flag) {

    }
}
