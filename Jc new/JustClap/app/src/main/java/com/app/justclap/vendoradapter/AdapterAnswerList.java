package com.app.justclap.vendoradapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.model.ModelVendor;
import com.app.justclap.utils.AppUtils;

import java.util.ArrayList;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterAnswerList extends RecyclerView.Adapter<AdapterAnswerList.CustomViewHolder> {
    ArrayList<ModelVendor> detail;
    Context mContext;
    OnCustomItemClicListener listener;

    public AdapterAnswerList(Context context, OnCustomItemClicListener lis, ArrayList<ModelVendor> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_ques_ans, viewGroup, false);

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
            this.text_ques = (TextView) view.findViewById(R.id.text_question);
            this.text_ans = (TextView) view.findViewById(R.id.text_answer);
            AppUtils.fontGotham_Book(text_ans, mContext);
            AppUtils.fontGotham_Medium(text_ques, mContext);

        }

        @Override
        public void onClick(View v) {


            listener.onItemClickListener(getPosition(), 1);


        }

           /* Log.e("position", getPosition() + "");
            listener.onItemClickListener(getPosition(), 1);*/
    }

}