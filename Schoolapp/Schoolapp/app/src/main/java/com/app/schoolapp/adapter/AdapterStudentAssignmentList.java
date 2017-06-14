package com.app.schoolapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.schoolapp.R;
import com.app.schoolapp.interfaces.OnCustomItemClicListener;
import com.app.schoolapp.model.ModelDataAssignment;

import java.util.List;

/**
 * Created by mohit kumar on 6/2/2017.
 */

public class AdapterStudentAssignmentList extends RecyclerView.Adapter<AdapterStudentAssignmentList.CustomViewHolder> {

    List<ModelDataAssignment> detail;
    Context mContext;
    OnCustomItemClicListener listener;

    public AdapterStudentAssignmentList(Context context, OnCustomItemClicListener lis, List<ModelDataAssignment> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_assignment, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {
        Log.e("onBindViewHolder", "onBindViewHolderCalled");
        // customViewHolder.test_name.setText(detail.get(i).getDoctorName());
        customViewHolder.text_title.setText(detail.get(i).getTitle());
        customViewHolder.text_name.setText(detail.get(i).getWrittenBy());
        customViewHolder.text_class.setText("Class:"+detail.get(i).getAssignedClass()+"("+detail.get(i).getAssignedSection()+")");
        customViewHolder.text_subject.setText(detail.get(i).getSubject());
        customViewHolder.text_description.setText(detail.get(i).getContent());
        customViewHolder.text_date.setText(detail.get(i).getCreated());
        customViewHolder.text_submissiondate.setText(detail.get(i).getSubmissiondate());
        customViewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClickListener(i, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView text_title, text_name, text_class, text_subject, text_description, text_date, text_submissiondate;
        CardView card_view;

        public CustomViewHolder(View view) {
            super(view);
            Log.e("CustomViewHolder", "CustomViewHolderCalled");

            this.text_title = (TextView) view.findViewById(R.id.text_title);
            this.text_name = (TextView) view.findViewById(R.id.text_name);
            this.text_class = (TextView) view.findViewById(R.id.text_class);
            this.text_subject = (TextView) view.findViewById(R.id.text_subject);
            this.text_description = (TextView) view.findViewById(R.id.text_description);
            this.text_date = (TextView) view.findViewById(R.id.text_date);
            this.text_submissiondate = (TextView) view.findViewById(R.id.text_submissiondate);
            this.card_view = (CardView) view.findViewById(R.id.card_view);
        }
    }
}

