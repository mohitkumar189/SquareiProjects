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
import com.app.schoolapp.model.PeriodDetails;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mohit kumar on 6/12/2017.
 */

public class StudentTimeTableAdapter extends RecyclerView.Adapter<StudentTimeTableAdapter.CustomViewHolder> {

    private ArrayList<ArrayList<ModelTimeTable>> modelTimeTables;
    private Context ctx;
    OnCustomItemClicListener listener;

    private HashMap<String, HashMap<String, PeriodDetails>> periods;
    HashMap<String, PeriodDetails> periodsDetails;

    public StudentTimeTableAdapter(Context ctx, OnCustomItemClicListener listener, HashMap<String, HashMap<String, PeriodDetails>> periods) {
        this.ctx = ctx;
        this.listener = listener;
        this.periods = periods;
        System.out.println("received values====" + periods);
    }

    public StudentTimeTableAdapter(ArrayList<ArrayList<ModelTimeTable>> modelTimeTables, Context ctx, OnCustomItemClicListener listener) {
        this.modelTimeTables = modelTimeTables;
        this.ctx = ctx;
        this.listener = listener;

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_timetable, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        setTimeTableData(holder, position);
    }

    private void setTimeTableData(CustomViewHolder holder, int pos) {
        int position = pos + 1;
        //periodsDetails = periods.get(String.valueOf(position));
        System.out.println("position returned +" + position);
        if (periods.containsKey(String.valueOf(position))) {
            System.out.println("===data at position " + position + periods.get(String.valueOf(position)));
            holder.text_periodname.setText(String.valueOf(position));
            periodsDetails = periods.get(String.valueOf(position));
            if (periodsDetails.containsKey("Mon")) {
                // System.out.println("===content found mon" + periodsDetails.get("Mon"));
                holder.text_subject_mon.setText(periodsDetails.get("Mon").getSubject());
                holder.text_time_mon.setText(periodsDetails.get("Mon").getTeacher());
            }
            if (periodsDetails.containsKey("Tue")) {
                //System.out.println("===content found tue");
                holder.text_subject_tue.setText(periodsDetails.get("Tue").getSubject());
                holder.text_time_tue.setText(periodsDetails.get("Tue").getTeacher());
            }
            if (periodsDetails.containsKey("Wed")) {
                // System.out.println("===content found wed");
                holder.text_subject_wed.setText(periodsDetails.get("Wed").getSubject());
                holder.text_time_wed.setText(periodsDetails.get("Wed").getTeacher());
            }
            if (periodsDetails.containsKey("Thu")) {
                // System.out.println("===content found thu");
                holder.text_subject_thur.setText(periodsDetails.get("Thu").getSubject());
                holder.text_time_thur.setText(periodsDetails.get("Thu").getTeacher());
            }
            if (periodsDetails.containsKey("Fri")) {
                //  System.out.println("===content found fri");
                holder.text_subject_fri.setText(periodsDetails.get("Fri").getSubject());
                holder.text_time_fri.setText(periodsDetails.get("Fri").getTeacher());
            }
            if (periodsDetails.containsKey("Sat")) {
                //  System.out.println("===content found sat");
                holder.text_subject_sat.setText(periodsDetails.get("Sat").getSubject());
                holder.text_time_sat.setText(periodsDetails.get("Sat").getTeacher());
            }



         /*   System.out.println("periods details======" + periodsDetails.toString());
            switch (day) {
                case "Mon":
                    holder.text_subject_mon.setText(subject);
                    holder.text_time_mon.setText(teacher);
                    break;
                case "Tue":
                    holder.text_subject_tue.setText(subject);
                    holder.text_time_tue.setText(teacher);
                    break;
                case "Wed":
                    holder.text_subject_wed.setText(subject);
                    holder.text_time_wed.setText(teacher);
                    break;
                case "Thu":
                    holder.text_subject_thur.setText(subject);
                    holder.text_time_thur.setText(teacher);
                    break;
                case "Fri":
                    holder.text_subject_fri.setText(subject);
                    holder.text_time_fri.setText(teacher);
                    break;
                case "Sat":
                    holder.text_subject_sat.setText(subject);
                    holder.text_time_sat.setText(teacher);
                    break;
            }*/
        }


       /* ArrayList<ModelTimeTable> al = modelTimeTables.get(position);
        for (int i = 0; i < modelTimeTables.size(); i++) {
            //  System.out.println("============" + (modelTimeTables.get(i)).toString());
        }
        //  System.out.println("=====size======" + al.get(position).getPeriodDetails().size() + "==data==" + al.get(position).getPeriodDetails().get(0).getSubject() + al.get(position).getPeriodDetails().get(0).getTeacher());
        //  String subject = al.get(position).getPeriodDetails().get(0).getSubject();
        //  String teacher = "(" + al.get(position).getPeriodDetails().get(0).getTeacher() + ")";

        //   System.out.println("teacher name :" + teacher + " subject: " + subject);
        // String day = al.get(position).getDay();
        //  System.out.println("teacher name :" + teacher + " subject: " + subject + " day: " + day);
        int length = al.size();
        for (int i = 0; i < length; i++) {
            String subject = al.get(i).getPeriodDetails().get(0).getSubject();
            String teacher = "(" + al.get(i).getPeriodDetails().get(0).getTeacher() + ")";
            String day = al.get(i).getDay();
            System.out.println("teacher name :" + teacher + " subject: " + subject + " day: " + day);
            // al.get(i).getDay();

            switch (day) {
                case "Mon":
                    holder.text_subject_mon.setText(subject);
                    holder.text_time_mon.setText(teacher);
                    break;
                case "Tue":
                    holder.text_subject_tue.setText(subject);
                    holder.text_time_tue.setText(teacher);
                    break;
                case "Wed":
                    holder.text_subject_wed.setText(subject);
                    holder.text_time_wed.setText(teacher);
                    break;
                case "Thu":
                    holder.text_subject_thur.setText(subject);
                    holder.text_time_thur.setText(teacher);
                    break;
                case "Fri":
                    holder.text_subject_fri.setText(subject);
                    holder.text_time_fri.setText(teacher);
                    break;
                case "Sat":
                    holder.text_subject_sat.setText(subject);
                    holder.text_time_sat.setText(teacher);
                    break;
            }
        }*/
    }

    @Override
    public int getItemCount() {
        // System.out.println("data size==" + modelTimeTables.size());
        return periods.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView text_periodname, text_subject_mon, text_class_mon, text_time_mon, text_subject_tue, text_class_tue, text_time_tue,
                text_subject_wed, text_class_wed, text_time_wed, text_subject_thur, text_class_thur, text_time_thur, text_subject_fri,
                text_class_fri, text_time_fri, text_subject_sat, text_class_sat, text_time_sat;

        public CustomViewHolder(View view) {
            super(view);

            this.text_periodname = (TextView) view
                    .findViewById(R.id.text_periodname);
            this.text_subject_mon = (TextView) view.findViewById(R.id.text_subject_mon);
            this.text_class_mon = (TextView) view
                    .findViewById(R.id.text_class_mon);
            this.text_time_mon = (TextView) view.findViewById(R.id.text_time_mon);
            this.text_subject_tue = (TextView) view.findViewById(R.id.text_subject_tue);
            this.text_class_tue = (TextView) view.findViewById(R.id.text_class_tue);
            this.text_time_tue = (TextView) view.findViewById(R.id.text_time_tue);
            this.text_subject_wed = (TextView) view.findViewById(R.id.text_subject_wed);
            this.text_class_wed = (TextView) view.findViewById(R.id.text_class_wed);

            this.text_time_wed = (TextView) view.findViewById(R.id.text_time_wed);
            this.text_class_thur = (TextView) view.findViewById(R.id.text_class_thur);
            this.text_subject_thur = (TextView) view.findViewById(R.id.text_subject_thur);
            this.text_time_thur = (TextView) view.findViewById(R.id.text_time_thur);
            this.text_subject_fri = (TextView) view.findViewById(R.id.text_subject_fri);
            this.text_class_fri = (TextView) view.findViewById(R.id.text_class_fri);
            this.text_time_fri = (TextView) view.findViewById(R.id.text_time_fri);
            this.text_subject_sat = (TextView) view.findViewById(R.id.text_subject_sat);
            this.text_class_sat = (TextView) view.findViewById(R.id.text_class_sat);
            this.text_time_sat = (TextView) view.findViewById(R.id.text_time_sat);

        }


    }

}
