package com.app.justclap.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.utils.AppUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.app.justclap.utils.CircleTransform;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterVendorCompletedLeads extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<ModelService> detail;
    Context mContext;
    OnCustomItemClicListener listener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public AdapterVendorCompletedLeads(Context context, OnCustomItemClicListener lis, ArrayList<ModelService> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_vendor_completed_leads, parent, false);

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {

        if (holder instanceof CustomViewHolder) {

            ModelService m1 = (ModelService) detail.get(i);
            ((CustomViewHolder) holder).text_services_name.setText(m1.getUsername());
            ((CustomViewHolder) holder).text_email.setText(m1.getUserEmail());
            ((CustomViewHolder) holder).text_quoted_detail.setText(m1.getQuote());
            ((CustomViewHolder) holder).review_count.setText(m1.getCreatedDate());
            ((CustomViewHolder) holder).text_requestId.setText(m1.getRequestId());
            SpannableString content = new SpannableString(AppUtils.convertToTwoDecimalDigit(m1.getVendorDistance()) + " Km ");
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

            ((CustomViewHolder) holder).distance.setText(content);
            Log.e("user_Image", "*" + m1.getVendorimage());
            Picasso.with(mContext)
                    .load(m1.getVendorimage())
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.user)
                    .into(((CustomViewHolder) holder).image_viewers);

            ((CustomViewHolder) holder).text_chats.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClickListener(i, 4);
                }
            });
            ((CustomViewHolder) holder).text_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClickListener(i, 2);
                }
            });
            ((CustomViewHolder) holder).text_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClickListener(i, 3);
                }
            });


            ((CustomViewHolder) holder).distance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClickListener(i, 11);
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

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text_services_name,distance, text_email,text_requestId, text_quoted_detail, review_count, text_chats, text_phone, text_profile;
        ImageView image_viewers;
        RatingBar rating;


        public CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.image_viewers = (ImageView) view.findViewById(R.id.image_viewers);
            this.text_services_name = (TextView) view.findViewById(R.id.text_services_name);
            this.text_email = (TextView) view.findViewById(R.id.text_email);
            this.text_quoted_detail = (TextView) view.findViewById(R.id.text_quoted_detail);
            this.review_count = (TextView) view.findViewById(R.id.review_count);
            this.text_chats = (TextView) view.findViewById(R.id.text_chats);
            this.text_phone = (TextView) view.findViewById(R.id.text_phone);
            this.text_profile = (TextView) view.findViewById(R.id.text_profile);
            this.text_requestId = (TextView) view.findViewById(R.id.text_requestId);
            this.rating = (RatingBar) view.findViewById(R.id.rating);
            this.distance = (TextView) view.findViewById(R.id.distance);
        }

        @Override
        public void onClick(View v) {

            listener.onItemClickListener(getPosition(), 1);

        }

    }

    @Override
    public int getItemViewType(int position) {
        ModelService m1 = (ModelService) detail.get(position);
        if (detail.get(position).getRowType() == 1) {
            return VIEW_ITEM;
        } else if (detail.get(position).getRowType() == 2) {
            return VIEW_PROG;
        }
        return -1;
    }

}


