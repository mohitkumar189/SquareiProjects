package com.app.justclap.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
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
import java.util.HashMap;

import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.app.justclap.utils.CircleTransform;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterCompletedBooking extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<ModelService> detail;
    Context mContext;
    OnCustomItemClicListener listener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;


    public AdapterCompletedBooking(Context context, OnCustomItemClicListener lis, ArrayList<ModelService> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_booking_completed, parent, false);

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

            ((CustomViewHolder) holder).rating.setEnabled(false);
            ((CustomViewHolder) holder).text_servname.setText(m1.getServiceName());
            ((CustomViewHolder) holder).text_services_name.setText(m1.getVendorName());
            ((CustomViewHolder) holder).text_serviceAssigned.setText(m1.getHiredcount());
            ((CustomViewHolder) holder).text_quoted_detail.setText(m1.getQuote());
            ((CustomViewHolder) holder).text_requestId.setText(m1.getRequestId());
            SpannableString content = new SpannableString(AppUtils.convertToTwoDecimalDigit(m1.getVendorDistance()) + " Km ");
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

            ((CustomViewHolder) holder).distance.setText(content);
            //  ((CustomViewHolder) holder).review_count.setText("review: "+m1.getTotalreview());
            if (!m1.getTotalreview().equalsIgnoreCase("")) {
                if (Integer.parseInt(m1.getTotalreview()) > 1) {
                    ((CustomViewHolder) holder).review_count.setText(m1.getTotalreview() + " reviews");
                } else {
                    if (Integer.parseInt(m1.getTotalreview()) == 0) {
                        ((CustomViewHolder) holder).review_count.setText("No review");
                    } else {
                        ((CustomViewHolder) holder).review_count.setText(m1.getTotalreview() + " review");
                    }
                }
            } else {

                ((CustomViewHolder) holder).review_count.setText("No review");
            }
            ((CustomViewHolder) holder).text_services_name.setText(m1.getVendorName());

            if (Integer.parseInt(m1.getTotalreview()) > 25) {

                ((CustomViewHolder) holder).rating.setRating(Float.parseFloat(m1.getRating()));
            } else {
                ((CustomViewHolder) holder).rating.setRating(4);
            }

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
        TextView text_services_name, distance,text_requestId, text_serviceAssigned, text_servname, text_quoted_detail, review_count, text_chats, text_phone, text_profile;
        ImageView image_viewers;
        RatingBar rating;


        public CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.image_viewers = (ImageView) view.findViewById(R.id.image_viewers);
            this.text_services_name = (TextView) view.findViewById(R.id.text_services_name);
            this.text_serviceAssigned = (TextView) view.findViewById(R.id.text_serviceAssigned);
            this.text_requestId = (TextView) view.findViewById(R.id.text_requestId);
            this.text_quoted_detail = (TextView) view.findViewById(R.id.text_quoted_detail);
            this.review_count = (TextView) view.findViewById(R.id.review_count);
            this.text_chats = (TextView) view.findViewById(R.id.text_chats);
            this.text_phone = (TextView) view.findViewById(R.id.text_phone);
            this.text_profile = (TextView) view.findViewById(R.id.text_profile);
            this.rating = (RatingBar) view.findViewById(R.id.rating);
            this.distance = (TextView) view.findViewById(R.id.distance);
            this.text_servname = (TextView) view.findViewById(R.id.text_servname);
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

