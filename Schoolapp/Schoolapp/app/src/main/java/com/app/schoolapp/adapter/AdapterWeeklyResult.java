package com.app.schoolapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.schoolapp.R;
import com.app.schoolapp.interfaces.OnCustomItemClicListener;
import com.app.schoolapp.model.ModelTimeTable;

import java.util.ArrayList;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterWeeklyResult extends RecyclerView.Adapter<AdapterWeeklyResult.CustomViewHolder> {
    ArrayList<ModelTimeTable> detail;
    Context mContext;
    OnCustomItemClicListener listener;
    int pos = 0;
    private ModelTimeTable model;
    ArrayList<String> periodName;
    ArrayList<String> daysList;

    public AdapterWeeklyResult(Context context, OnCustomItemClicListener lis, ArrayList<ModelTimeTable> list, ArrayList<String> periodName, ArrayList<String> daysList) {

        this.mContext = context;
        this.listener = lis;
        this.periodName = periodName;
        this.daysList = daysList;
        this.detail = list;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_weekly_result, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;

    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {

        //   holder.text_periodname.setText(periodName.get(position));

        //  setDaysData(holder, position);

        if (position == 0) {

            holder.text_periodname.setBackgroundColor(mContext.getResources().getColor(R.color.text_bg_color));
            holder.text_marks_week4.setBackgroundColor(mContext.getResources().getColor(R.color.text_bg_color));
            holder.text_marks_week3.setBackgroundColor(mContext.getResources().getColor(R.color.text_bg_color));
            holder.text_marks_week2.setBackgroundColor(mContext.getResources().getColor(R.color.text_bg_color));
            holder.text_marks_week1.setBackgroundColor(mContext.getResources().getColor(R.color.text_bg_color));

        } else {

            holder.text_periodname.setText("Math");
            holder.text_marks_week4.setText("7/10");
            holder.text_marks_week3.setText("6/10");
            holder.text_marks_week2.setText("9/10");
            holder.text_marks_week1.setText("8/10");
        }

    }

    private void setDaysData(CustomViewHolder holder, int position) {
      /*  try {
            detail = new ArrayList<>();
            String daysdata = daysList.get(position);

            JSONArray days = new JSONArray(daysdata);
            for (int j = 0; j < days.length(); j++) {

                JSONObject jo = days.getJSONObject(j);
                model = new ModelTimeTable();

                model.setDaysArray(jo.toString());
                model.setDay(jo.getString("day"));
                model.setClassname(jo.getString("class"));
                model.setSubject(jo.getString("subject"));
                model.setTime(jo.getString("time"));
                model.setTeacher_name(jo.getString("teacher_name"));

                detail.add(model);

            }
            for (int i = 0; i < detail.size(); i++) {


                if (detail.get(i).getDay().equalsIgnoreCase("mon")) {

                    holder.text_marks_week1.setText(detail.get(i).getClassname());

                } else if (detail.get(i).getDay().equalsIgnoreCase("tue")) {

                    holder.text_marks_week2.setText(detail.get(i).getClassname());

                } else if (detail.get(i).getDay().equalsIgnoreCase("wed")) {

                    holder.text_marks_week3.setText(detail.get(i).getClassname());

                } else if (detail.get(i).getDay().equalsIgnoreCase("thu")) {

                    holder.text_marks_week4.setText(detail.get(i).getClassname());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }


    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView text_periodname, text_marks_week1, text_marks_week3, text_marks_week2, text_marks_week4;

        public CustomViewHolder(View view) {
            super(view);

            this.text_periodname = (TextView) view
                    .findViewById(R.id.text_periodname);
            this.text_marks_week1 = (TextView) view
                    .findViewById(R.id.text_marks_week1);
            this.text_marks_week3 = (TextView) view.findViewById(R.id.text_marks_week3);
            this.text_marks_week2 = (TextView) view.findViewById(R.id.text_marks_week2);
            this.text_marks_week4 = (TextView) view.findViewById(R.id.text_marks_week4);


        }


    }


}

