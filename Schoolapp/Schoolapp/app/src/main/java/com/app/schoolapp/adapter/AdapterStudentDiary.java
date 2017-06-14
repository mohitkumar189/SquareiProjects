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
import com.app.schoolapp.model.ModelStudentDiary;

import java.util.List;

/**
 * Created by SQ3 on 5/22/2017.
 */

public class AdapterStudentDiary extends RecyclerView.Adapter<AdapterStudentDiary.CustomViewHolder> {

    List<ModelStudentDiary> detail;
    Context mContext;
    OnCustomItemClicListener listener;

    public AdapterStudentDiary(Context context, OnCustomItemClicListener lis, List<ModelStudentDiary> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_student_diary, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;

    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {
        Log.e("onBindViewHolder", "onBindViewHolderCalled");
        customViewHolder.textViewTitle.setText(detail.get(i).getTitle());
        customViewHolder.textViewDescription.setText(detail.get(i).getContent());
        customViewHolder.textViewTeacherName.setText("By: " + detail.get(i).getPostedBy());
        customViewHolder.textViewDate.setText(detail.get(i).getSentDate().substring(0, 10));
        customViewHolder.textViewSubject.setText(detail.get(i).getSubject());
        // customViewHolder.test_name.setText(detail.get(i).getDoctorName());
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

        TextView textViewTitle, textViewDescription, textViewTeacherName, textViewDate, textViewSubject;
        CardView card_view;

        public CustomViewHolder(View view) {
            super(view);
            Log.e("CustomViewHolder", "CustomViewHolderCalled");
            this.textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
            this.textViewDescription = (TextView) view.findViewById(R.id.textViewQuestion);
            this.textViewTeacherName = (TextView) view.findViewById(R.id.textViewTeacherName);
            this.textViewDate = (TextView) view.findViewById(R.id.textViewDate);
            this.textViewSubject = (TextView) view.findViewById(R.id.textViewSubject);
            this.card_view = (CardView) view.findViewById(R.id.card_view);
        }

    }

}
