package com.app.justclap.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
public class AdapterVendorQuoteList extends RecyclerView.Adapter<AdapterVendorQuoteList.CustomViewHolder> {
    ArrayList<ModelService> detail;
    Context mContext;
    OnCustomItemClicListener listener;


    public AdapterVendorQuoteList(Context context, OnCustomItemClicListener lis, ArrayList<ModelService> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_vendor_quote_list, null);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);


        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {


        customViewHolder.text_services_name.setText(detail.get(i).getUsername());
        customViewHolder.text_email.setText(detail.get(i).getUserEmail());
        customViewHolder.text_quoted_detail.setText(detail.get(i).getQuote());
        customViewHolder.review_count.setText(detail.get(i).getCreatedDate());
        customViewHolder.text_requestId.setText("RequestId : " + detail.get(i).getRequestId());

        Log.e("user_Image", "*" + detail.get(i).getVendorimage());
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
    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text_services_name, text_email, text_quoted_detail, review_count, text_chats, text_phone, text_requestId, text_profile;
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

        }

        @Override
        public void onClick(View v) {

            listener.onItemClickListener(getPosition(), 1);

        }

    }


}


