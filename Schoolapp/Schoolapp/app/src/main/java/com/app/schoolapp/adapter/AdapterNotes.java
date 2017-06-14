package com.app.schoolapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.schoolapp.R;
import com.app.schoolapp.interfaces.OnCustomItemClicListener;
import com.app.schoolapp.model.NotesDataModal;

import java.util.List;

/**
 * Created by mohit kumar on 6/1/2017.
 */

public class AdapterNotes extends RecyclerView.Adapter<AdapterNotes.CustomViewHolder> {

    List<NotesDataModal> detail;
    Context mContext;
    OnCustomItemClicListener listener;

    public AdapterNotes(Context context, OnCustomItemClicListener lis, List<NotesDataModal> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_notes, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {
        Log.e("onBindViewHolder", "onBindViewHolderCalled");
        customViewHolder.textViewTitle.setText(detail.get(i).getTitle());
        customViewHolder.textViewDescription.setText(detail.get(i).getContent());
        customViewHolder.textViewTeacherName.setText("By: " + detail.get(i).getWrittenBy());
        customViewHolder.textViewDate.setText(detail.get(i).getCreated().substring(0, 10));
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

        TextView textViewTitle, textViewDescription, textViewTeacherName, textViewDate, noteSubject;
        CardView card_view;
        ImageView ivNoteImage;

        public CustomViewHolder(View view) {
            super(view);
            Log.e("CustomViewHolder", "CustomViewHolderCalled");
            this.textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
            this.textViewDescription = (TextView) view.findViewById(R.id.textViewDescription);
            this.textViewTeacherName = (TextView) view.findViewById(R.id.textViewTeacherName);
            this.textViewDate = (TextView) view.findViewById(R.id.textViewDate);
            this.noteSubject = (TextView) view.findViewById(R.id.noteSubject);
            this.card_view = (CardView) view.findViewById(R.id.card_view);
            this.ivNoteImage = (ImageView) view.findViewById(R.id.ivNoteImage);
        }

    }

}
