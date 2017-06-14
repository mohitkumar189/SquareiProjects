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
import com.app.schoolapp.model.ModelPublicForum;

import java.util.ArrayList;

/**
 * Created by SQ3 on 5/22/2017.
 */

public class AdapterforumAnswerList extends RecyclerView.Adapter<AdapterforumAnswerList.CustomViewHolder> {

    ArrayList<ModelPublicForum> detail;
    Context mContext;
    OnCustomItemClicListener listener;

    public AdapterforumAnswerList(Context context, OnCustomItemClicListener lis, ArrayList<ModelPublicForum> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_forum_answerlist, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {
        Log.e("onBindViewHolder", "onBindViewHolderCalled");
        customViewHolder.textViewQuestionDetail.setText(detail.get(i).getContent());
        customViewHolder.textViewWriter.setText(detail.get(i).getWrittenBy() + " (" + detail.get(i).getSubject() + ")");
        customViewHolder.textViewDate.setText(detail.get(i).getCreated().substring(0, 10));
      /*  customViewHolder.textViewAnswers.setText("Answers: " + detail.get(i).getAnswers());
        customViewHolder.textViewAnswer.setText("Subject: " + detail.get(i).getSubject());*/
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

        TextView textViewQuestionDetail, textViewDate, textViewWriter;
        CardView card_view;
        ImageView image_user;

        public CustomViewHolder(View view) {
            super(view);
            Log.e("CustomViewHolder", "CustomViewHolderCalled");

            this.textViewQuestionDetail = (TextView) view.findViewById(R.id.textViewQuestionDetail);
            this.textViewWriter = (TextView) view.findViewById(R.id.textViewWriter);
            this.textViewDate = (TextView) view.findViewById(R.id.textViewDate);
            this.card_view = (CardView) view.findViewById(R.id.card_view);
            this.image_user = (ImageView) view.findViewById(R.id.image_user);
        }
    }
}
