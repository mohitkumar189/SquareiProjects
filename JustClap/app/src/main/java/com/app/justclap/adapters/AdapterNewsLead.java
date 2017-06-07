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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.justclap.R;

import java.util.ArrayList;

import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelVendorData;
import com.app.justclap.utils.AppUtils;
import com.app.justclap.utils.CircleTransform;
import com.squareup.picasso.Picasso;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterNewsLead extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<ModelVendorData> detail;
    Context mContext;
    OnCustomItemClicListener listener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public AdapterNewsLead(Context context, OnCustomItemClicListener lis, ArrayList<ModelVendorData> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_newslead, parent, false);

            vh = new CustomViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof CustomViewHolder) {

            ModelVendorData m1 = (ModelVendorData) detail.get(position);

            ((CustomViewHolder) holder).text_responses.setText(m1.getResponseCount());

            ((CustomViewHolder) holder).text_responses_day.setText(m1.getResponseTime());

            ((CustomViewHolder) holder).text_date.setText(m1.getBookinTime());
            ((CustomViewHolder) holder).text_servicetype.setText(m1.getServiceType());

            ((CustomViewHolder) holder).text_address.setText(m1.getAddress());
            ((CustomViewHolder) holder).text_requestId.setText(m1.getRequestId());
            SpannableString content = new SpannableString(AppUtils.convertToTwoDecimalDigit(m1.getVendorDistance()) + " Km ");
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

            ((CustomViewHolder) holder).distance.setText(content);
            ((CustomViewHolder) holder).text_name.setText(m1.getVendorname());
            ((CustomViewHolder) holder).text_service.setText(m1.getSeervicename());
            Picasso.with(mContext).load(mContext.getResources().getString(R.string.img_url) + m1.getProfileImage())
                    .placeholder(R.drawable.placeholder)
                    .transform(new CircleTransform())
                    .into(((CustomViewHolder) holder).profile_image);


            if (m1.getIs_hire().equalsIgnoreCase("1")) {
                ((CustomViewHolder) holder).rl_chat.setVisibility(View.GONE);
                ((CustomViewHolder) holder).text_customer.setText("Customer hire another vendor for his service.");

            } else {
                ((CustomViewHolder) holder).text_customer.setText("Customer has shared phone number");
                ((CustomViewHolder) holder).rl_chat.setVisibility(View.GONE);
            }
            if (m1.getIs_quoted().equalsIgnoreCase("1")) {
                ((CustomViewHolder) holder).text_address.setVisibility(View.VISIBLE);
                ((CustomViewHolder) holder).rl_chat.setVisibility(View.VISIBLE);
            } else {
                ((CustomViewHolder) holder).text_address.setVisibility(View.GONE);
                ((CustomViewHolder) holder).rl_chat.setVisibility(View.VISIBLE);
            }
            ((CustomViewHolder) holder).image_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClickListener(position, 2);

                }
            });
            ((CustomViewHolder) holder).text_chats.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClickListener(position, 3);

                }
            });


            ((CustomViewHolder) holder).distance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClickListener(position, 11);
                }
            });

            ((CustomViewHolder) holder).text_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClickListener(position, 4);

                }
            });
            ((CustomViewHolder) holder).text_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClickListener(position, 5);

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


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
            this.progressBar.getIndeterminateDrawable().setColorFilter(Color.rgb(255, 133, 24), PorterDuff.Mode.MULTIPLY);
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView text_customer, text_responses, distance, text_requestId, text_chats, text_phone, text_profile, text_responses_day, text_date, text_servicetype, text_name, text_service, text_address;
        ImageView image_delete, profile_image;
        RelativeLayout rl_chat;

        public CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.text_customer = (TextView) view.findViewById(R.id.text_customer);
            this.text_responses = (TextView) view.findViewById(R.id.text_responses);
            this.text_responses_day = (TextView) view.findViewById(R.id.text_responses_day);
            this.text_requestId = (TextView) view.findViewById(R.id.text_requestId);
            this.text_date = (TextView) view.findViewById(R.id.text_date);
            this.text_servicetype = (TextView) view.findViewById(R.id.text_servicetype);
            this.text_name = (TextView) view.findViewById(R.id.text_name);
            this.text_service = (TextView) view.findViewById(R.id.text_service);
            this.distance = (TextView) view.findViewById(R.id.distance);
            this.text_address = (TextView) view.findViewById(R.id.text_address);
            this.image_delete = (ImageView) view.findViewById(R.id.image_delete);
            this.profile_image = (ImageView) view.findViewById(R.id.profile_image);
            this.rl_chat = (RelativeLayout) view.findViewById(R.id.rl_chat);
            this.text_chats = (TextView) view.findViewById(R.id.text_chats);
            this.text_phone = (TextView) view.findViewById(R.id.text_phone);
            this.text_profile = (TextView) view.findViewById(R.id.text_profile);
        }

        @Override
        public void onClick(View v) {

            listener.onItemClickListener(getPosition(), 1);

        }

    }

    @Override
    public int getItemViewType(int position) {
        ModelVendorData m1 = (ModelVendorData) detail.get(position);
        if (detail.get(position).getRowType() == 1) {
            return VIEW_ITEM;
        } else if (detail.get(position).getRowType() == 2) {
            return VIEW_PROG;
        }
        return -1;
    }

}