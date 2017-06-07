package com.app.justclap.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.justclap.R;

import java.util.ArrayList;

import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelVendorData;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterAnswerList extends RecyclerView.Adapter<AdapterAnswerList.CustomViewHolder> {
    ArrayList<ModelVendorData> detail;
    Context mContext;
    OnCustomItemClicListener listener;


    public AdapterAnswerList(Context context, OnCustomItemClicListener lis, ArrayList<ModelVendorData> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_answer_list, null);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);


        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {


        customViewHolder.text_ans.setText(detail.get(i).getAnswer());
        customViewHolder.text_ques.setText(detail.get(i).getQuestion());

    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView text_ques, text_ans;


        public CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.text_ques = (TextView) view.findViewById(R.id.text_ques);
            this.text_ans = (TextView) view.findViewById(R.id.text_ans);


        }

        @Override
        public void onClick(View v) {


            listener.onItemClickListener(getPosition(), 1);


        }

           /* Log.e("position", getPosition() + "");
            listener.onItemClickListener(getPosition(), 1);*/
    }

}