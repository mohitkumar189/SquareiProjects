package com.app.schoolapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.schoolapp.R;
import com.app.schoolapp.interfaces.OnCustomItemClicListener;
import com.app.schoolapp.model.ModelData;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Hemanta on 21-04-2017.
 */
public class AdapterEventCalendar extends RecyclerView.Adapter<AdapterEventCalendar.CustomViewHolder> {

    ArrayList<ModelData> detail;
    Context mContext;
    OnCustomItemClicListener listener;

    public AdapterEventCalendar(Context context, OnCustomItemClicListener lis, ArrayList<ModelData> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_event_calendar_list, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;

    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {
        Log.e("onBindViewHolder", "onBindViewHolderCalled");
        // customViewHolder.test_name.setText(detail.get(i).getDoctorName());

        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        customViewHolder.text_date.setBackgroundColor(color);
        customViewHolder.text_month.setBackgroundColor(color);

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

        TextView text_event_desc, text_event_title,text_date,text_month;
        CardView card_view;

        public CustomViewHolder(View view) {
            super(view);
            Log.e("CustomViewHolder", "CustomViewHolderCalled");

            this.text_event_desc = (TextView) view.findViewById(R.id.text_event_desc);
            this.text_event_title = (TextView) view.findViewById(R.id.text_event_title);
            this.text_date = (TextView) view.findViewById(R.id.text_date);
            this.text_month = (TextView) view.findViewById(R.id.text_month);

            this.card_view = (CardView) view.findViewById(R.id.card_view);
        }

    }

}