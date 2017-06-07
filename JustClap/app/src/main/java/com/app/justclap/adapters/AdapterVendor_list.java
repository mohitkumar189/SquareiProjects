package com.app.justclap.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.app.justclap.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterVendor_list extends RecyclerView.Adapter<AdapterVendor_list.CustomViewHolder> {

    ArrayList<ModelService> detail;
    Context mContext;
    OnCustomItemClicListener listener;

    public AdapterVendor_list(Context context, OnCustomItemClicListener lis, ArrayList<ModelService> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_service_detail, null);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;

    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {


      //  holder.mobile.setText(detail.get(position).getVendorMobile());
        if (detail.get(position).getQuote().equals("")){
            holder.text_quoted_detail.setVisibility(View.GONE);
        }else {
            holder.text_quoted_detail.setText("Quote : Rs. " + detail.get(position).getQuote());
        }

        holder.text_services_name.setText(detail.get(position).getVendorName());
      //  holder.email.setText(detail.get(position).getVendorEmail());

        Picasso.with(mContext)
                .load(detail.get(position).getVendorimage()).transform(new CircleTransform())

                .placeholder(R.drawable.user)
                .into(holder.image_viewers);


        holder.text_chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClickListener(position, 4);

            }
        });
        holder.text_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClickListener(position, 5);

            }
        });

        holder.text_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClickListener(position, 6);

            }
        });
    }


    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView text_service_name, text_services_name, email, text_quoted_detail, text_chats, text_phone, text_profile,mobile;
        ImageView image_viewers;
RelativeLayout relmain;
        public CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.text_quoted_detail = (TextView) view.findViewById(R.id.text_quoted_detail);
           // this.mobile = (TextView) view.findViewById(R.id.mobile);

            this.image_viewers = (ImageView) view.findViewById(R.id.image_viewers);
            this.text_services_name = (TextView) view.findViewById(R.id.text_services_name);
            this.text_chats = (TextView) view.findViewById(R.id.text_chats);
            this.text_phone = (TextView) view.findViewById(R.id.text_phone);
           // this.email = (TextView) view.findViewById(R.id.email);
            this.text_profile = (TextView) view.findViewById(R.id.text_profile);

        }

        @Override
        public void onClick(View v) {

            listener.onItemClickListener(getPosition(), 1);

        }

    }


}

