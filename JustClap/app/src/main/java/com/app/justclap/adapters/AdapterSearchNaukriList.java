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
import android.widget.RelativeLayout;
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
public class AdapterSearchNaukriList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<ModelService> detail;
    Context mContext;
    OnCustomItemClicListener listener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;


    public AdapterSearchNaukriList(Context context, OnCustomItemClicListener lis, ArrayList<ModelService> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_serarch_naukri_list, parent, false);

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
            this.progressBar.getIndeterminateDrawable().setColorFilter(Color.rgb(255, 133, 24), PorterDuff.Mode.MULTIPLY);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {

        if (holder instanceof CustomViewHolder) {

            final ModelService m1 = (ModelService) detail.get(i);

            ((CustomViewHolder) holder).text_services_name.setText(m1.getVendorName());
            ((CustomViewHolder) holder).text_email.setText(m1.getUserEmail());
            ((CustomViewHolder) holder).text_ph.setText(m1.getMobileNumber());
            ((CustomViewHolder) holder).rating.setEnabled(false);
            Picasso.with(mContext)
                    .load(m1.getVendorimage())
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.user)
                    .into(((CustomViewHolder) holder).image_viewers);
            SpannableString content = new SpannableString(AppUtils.convertToTwoDecimalDigit(m1.getVendorDistance()) + " Km ");
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

            ((CustomViewHolder) holder).distance.setText(content);

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
            if (Integer.parseInt(m1.getTotalreview()) > 25) {

                ((CustomViewHolder) holder).rating.setRating(Float.parseFloat(m1.getRating()));
            } else {

                    ((CustomViewHolder) holder).rating.setRating(4);

            }


            if (m1.getIsvendor().equalsIgnoreCase("1")) {

                ((CustomViewHolder) holder).rl_main.setBackgroundColor(mContext.getResources().getColor(
                        R.color.text_white));

                ((CustomViewHolder) holder).text_rateUs.setVisibility(View.VISIBLE);
                if (m1.getIssaved().equalsIgnoreCase("0")) {
                    //  ((CustomViewHolder) holder).text_apply.setText("Apply");
                    //   ((CustomViewHolder) holder).rl_chat.setVisibility(View.GONE);
                    ((CustomViewHolder) holder).text_chats.setVisibility(View.GONE);
                    ((CustomViewHolder) holder).text_email.setVisibility(View.GONE);
                    ((CustomViewHolder) holder).text_ph.setVisibility(View.GONE);
                    ((CustomViewHolder) holder).text_rateUs.setVisibility(View.GONE);
                    ((CustomViewHolder) holder).text_phone.setVisibility(View.GONE);
                    ((CustomViewHolder) holder).rl_rating.setVisibility(View.VISIBLE);
                    ((CustomViewHolder) holder).text_usertype.setText("Type" + " : " + "Provider");


                } else {
                    //   ((CustomViewHolder) holder).rl_chat.setVisibility(View.VISIBLE);
                    ((CustomViewHolder) holder).text_chats.setVisibility(View.VISIBLE);
                    ((CustomViewHolder) holder).text_phone.setVisibility(View.VISIBLE);
                    ((CustomViewHolder) holder).text_email.setVisibility(View.VISIBLE);
                    ((CustomViewHolder) holder).text_rateUs.setVisibility(View.VISIBLE);
                    ((CustomViewHolder) holder).text_ph.setVisibility(View.VISIBLE);
                    ((CustomViewHolder) holder).rl_rating.setVisibility(View.GONE);
                    ((CustomViewHolder) holder).text_usertype.setText("Type" + " : " + "Provider");
                    // ((CustomViewHolder) holder).text_apply.setText("Applied");
                }

                ((CustomViewHolder) holder).text_apply.setVisibility(View.GONE);

            } else {

                ((CustomViewHolder) holder).text_rateUs.setVisibility(View.GONE);
                ((CustomViewHolder) holder).rl_main.setBackgroundColor(mContext.getResources().getColor(
                        R.color.text_white));

                // ((CustomViewHolder) holder).rl_chat.setVisibility(View.VISIBLE);
                ((CustomViewHolder) holder).text_chats.setVisibility(View.VISIBLE);
                ((CustomViewHolder) holder).text_phone.setVisibility(View.VISIBLE);
                ((CustomViewHolder) holder).text_email.setVisibility(View.VISIBLE);
                ((CustomViewHolder) holder).text_ph.setVisibility(View.VISIBLE);
                ((CustomViewHolder) holder).rl_rating.setVisibility(View.GONE);
                ((CustomViewHolder) holder).text_usertype.setText("Type" + " : " + "User");
                if (m1.getIssaved().equalsIgnoreCase("0")) {
                    //   ((CustomViewHolder) holder).text_apply.setText("Save");
                } else {
                    //  ((CustomViewHolder) holder).text_apply.setText("Saved");
                }

            }


            ((CustomViewHolder) holder).text_rateUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClickListener(i, 7);
                }
            });

            ((CustomViewHolder) holder).distance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClickListener(i, 11);
                }
            });
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

            ((CustomViewHolder) holder).text_apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (m1.getIsvendor().equalsIgnoreCase("1")) {
                        listener.onItemClickListener(i, 5);
                    } else {
                        listener.onItemClickListener(i, 6);
                    }
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
        TextView text_services_name, distance, text_rateUs, text_usertype, text_apply, text_serviceAssigned, text_quoted_detail, review_count, text_chats, text_phone, text_profile, text_email, text_ph;
        ImageView image_viewers;
        RatingBar rating;
        // RelativeLayout rl_chat;
        RelativeLayout rl_rating, rl_main;

        public CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.image_viewers = (ImageView) view.findViewById(R.id.image_viewers);
            this.text_services_name = (TextView) view.findViewById(R.id.text_services_name);
            this.text_ph = (TextView) view.findViewById(R.id.text_ph);
            this.text_usertype = (TextView) view.findViewById(R.id.text_usertype);
            this.text_email = (TextView) view.findViewById(R.id.text_email);
            this.text_rateUs = (TextView) view.findViewById(R.id.text_rateUs);
            this.text_serviceAssigned = (TextView) view.findViewById(R.id.text_serviceAssigned);
            this.text_quoted_detail = (TextView) view.findViewById(R.id.text_quoted_detail);
            this.review_count = (TextView) view.findViewById(R.id.review_count);
            this.distance = (TextView) view.findViewById(R.id.distance);
            this.text_chats = (TextView) view.findViewById(R.id.text_chats);
            this.text_phone = (TextView) view.findViewById(R.id.text_phone);
            this.text_profile = (TextView) view.findViewById(R.id.text_profile);
            this.rating = (RatingBar) view.findViewById(R.id.rating);
            this.text_apply = (TextView) view.findViewById(R.id.text_apply);
            this.rl_rating = (RelativeLayout) view.findViewById(R.id.rl_rating);
            this.rl_main = (RelativeLayout) view.findViewById(R.id.rl_main);
            // this.rl_chat = (RelativeLayout) view.findViewById(R.id.rl_chat);
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


