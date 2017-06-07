package com.app.veraxe.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.veraxe.R;
import com.app.veraxe.interfaces.OnCustomItemClicListener;
import com.app.veraxe.model.ModelStudent;

import java.util.ArrayList;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterSchoolFeedbackList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<ModelStudent> detail;
    Context mContext;
    OnCustomItemClicListener listener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public AdapterSchoolFeedbackList(Context context, OnCustomItemClicListener lis, ArrayList<ModelStudent> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_student_feedbacklist, parent, false);

            vh = new CustomViewHolder(v);

        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;

    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
            this.progressBar.getIndeterminateDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof CustomViewHolder) {

            ModelStudent m1 = (ModelStudent) detail.get(position);

            ((CustomViewHolder) holder).text_dapartment_label.setText(m1.getStudent_name());
            ((CustomViewHolder) holder).text_feedbak_type.setText(m1.getFeedback_type_label());
            ((CustomViewHolder) holder).text_date.setText(m1.getDatetime());
            ((CustomViewHolder) holder).text_message.setText(m1.getMessage_text());
            ((CustomViewHolder) holder).text_status.setText(m1.getStatus_name());

            if (m1.getFeedback_type_id().equalsIgnoreCase("3")) {
                ((CustomViewHolder) holder).text_feedbak_type.setBackgroundColor(mContext.getResources().getColor(R.color.complaint_color));
            } else if (m1.getFeedback_type_id().equalsIgnoreCase("1")) {
                ((CustomViewHolder) holder).text_feedbak_type.setBackgroundColor(mContext.getResources().getColor(R.color.feedback_color));
            } else if (m1.getFeedback_type_id().equalsIgnoreCase("2")) {
                ((CustomViewHolder) holder).text_feedbak_type.setBackgroundColor(mContext.getResources().getColor(R.color.complement_color));
            }


            if (m1.getStatus_id().equalsIgnoreCase("1")) {
                ((CustomViewHolder) holder).text_status.setBackgroundColor(mContext.getResources().getColor(R.color.text_color_lightblue));
            } else {
                ((CustomViewHolder) holder).text_status.setBackgroundColor(mContext.getResources().getColor(R.color.red_color));
            }


            ((CustomViewHolder) holder).card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClickListener(position, 1);
                }
            });


        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }


    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView text_dapartment_label, text_feedbak_type, text_status, text_date, text_message;
        CardView card_view;

        public CustomViewHolder(View view) {
            super(view);

            this.text_dapartment_label = (TextView) view.findViewById(R.id.text_dapartment_label);
            this.text_date = (TextView) view.findViewById(R.id.text_date);
            this.text_status = (TextView) view.findViewById(R.id.text_status);
            this.text_message = (TextView) view.findViewById(R.id.text_message);
            this.text_feedbak_type = (TextView) view.findViewById(R.id.text_feedbak_type);
            this.card_view = (CardView) view.findViewById(R.id.card_view);

        }
    }

    public void setFilter(ArrayList<ModelStudent> detailnew) {
        detail = new ArrayList<>();
        detail.addAll(detailnew);
        notifyDataSetChanged();
    }

    public ModelStudent getFilter(int i) {

        return detail.get(i);
    }

    @Override
    public int getItemViewType(int position) {
        ModelStudent m1 = (ModelStudent) detail.get(position);
        if (detail.get(position).getRowType() == 1) {
            return VIEW_ITEM;
        } else if (detail.get(position).getRowType() == 2) {
            return VIEW_PROG;
        }
        return -1;
    }
}

