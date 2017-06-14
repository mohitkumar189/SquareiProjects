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
import com.app.schoolapp.model.ModelData;

import java.util.ArrayList;

/**
 * Created by Hemanta on 21-04-2017.
 */
public class AdapterConnectTeacherList extends RecyclerView.Adapter<AdapterConnectTeacherList.CustomViewHolder> {

    ArrayList<ModelData> detail;
    Context mContext;
    OnCustomItemClicListener listener;

    public AdapterConnectTeacherList(Context context, OnCustomItemClicListener lis, ArrayList<ModelData> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_connect_teacher_list, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;

    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {
        Log.e("onBindViewHolder", "onBindViewHolderCalled");
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

        TextView text_teacher_name, text_subject_name;
        ImageView image_message, image_call, image_videocall,image_user;
        CardView card_view;

        public CustomViewHolder(View view) {
            super(view);
            Log.e("CustomViewHolder", "CustomViewHolderCalled");

            this.text_teacher_name = (TextView) view.findViewById(R.id.text_teacher_name);
            this.text_subject_name = (TextView) view.findViewById(R.id.text_subject_name);
            this.image_videocall = (ImageView) view.findViewById(R.id.image_videocall);
            this.image_message = (ImageView) view.findViewById(R.id.image_message);
            this.image_call = (ImageView) view.findViewById(R.id.image_call);
            this.image_user = (ImageView) view.findViewById(R.id.image_user);

            this.card_view = (CardView) view.findViewById(R.id.card_view);
        }

    }

}