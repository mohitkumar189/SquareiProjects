package com.app.justclap.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.justclap.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.app.justclap.utils.CircleTransform;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterRequeststatusHireProf extends RecyclerView.Adapter<AdapterRequeststatusHireProf.CustomViewHolder> {
    ArrayList<ModelService> detail;
    Context mContext;
    OnCustomItemClicListener listener;


    public AdapterRequeststatusHireProf(Context context, OnCustomItemClicListener lis, ArrayList<ModelService> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_service_detail, null);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);


        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {

        customViewHolder.rating.setEnabled(false);
        customViewHolder.text_services_name.setText(detail.get(i).getVendorName());
        customViewHolder.text_serviceAssigned.setText(detail.get(i).getHiredcount());
        customViewHolder.text_quoted_detail.setText(detail.get(i).getQuote());
        if (Integer.parseInt(detail.get(i).getTotalreview())>1) {
            customViewHolder.review_count.setText(detail.get(i).getTotalreview()+" reviews");
        }
        else {
            if (Integer.parseInt(detail.get(i).getTotalreview()) == 0) {
                customViewHolder.review_count.setText("No review");
            } else {
                customViewHolder.review_count.setText(detail.get(i).getTotalreview() + " review");
            }
        }
        customViewHolder.text_services_name.setText(detail.get(i).getVendorName());
        if (Integer.parseInt(detail.get(i).getTotalreview()) > 25) {

            customViewHolder.rating.setRating(Float.parseFloat(detail.get(i).getRating()));
        } else {
            customViewHolder.rating.setRating(4);
        }

        Picasso.with(mContext)
                .load(detail.get(i).getVendorimage())
                .transform(new CircleTransform())
                .placeholder(R.drawable.user)
                .into(customViewHolder.image_viewers);

        customViewHolder.text_chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClickListener(i, 4);
            }
        });
        customViewHolder.text_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClickListener(i, 2);
            }
        });
        customViewHolder.text_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClickListener(i, 3);
            }
        });
        customViewHolder.text_hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClickListener(i, 5);
            }
        });
    }


    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text_services_name, text_serviceAssigned, text_quoted_detail, review_count, text_chats, text_phone, text_profile,text_hire;
        ImageView image_viewers;
        RatingBar rating;


        public CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.image_viewers = (ImageView) view.findViewById(R.id.image_viewers);
            this.text_hire = (TextView) view.findViewById(R.id.text_hire);
            this.text_services_name = (TextView) view.findViewById(R.id.text_services_name);
            this.text_serviceAssigned = (TextView) view.findViewById(R.id.text_serviceAssigned);
            this.text_quoted_detail = (TextView) view.findViewById(R.id.text_quoted_detail);
            this.review_count = (TextView) view.findViewById(R.id.review_count);
            this.text_chats = (TextView) view.findViewById(R.id.text_chats);
            this.text_phone = (TextView) view.findViewById(R.id.text_phone);
            this.text_profile = (TextView) view.findViewById(R.id.text_profile);
            this.rating = (RatingBar) view.findViewById(R.id.rating);

        }

        @Override
        public void onClick(View v) {

            listener.onItemClickListener(getPosition(), 1);

        }



    }


}


