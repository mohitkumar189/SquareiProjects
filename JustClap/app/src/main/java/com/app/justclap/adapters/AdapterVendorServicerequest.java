package com.app.justclap.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterVendorServicerequest extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<ModelService> detail;
    Context mContext;
    OnCustomItemClicListener listener;
    int pos = 0;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public AdapterVendorServicerequest(Context context, OnCustomItemClicListener lis, ArrayList<ModelService> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_vendor_service_request, parent, false);

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

            ModelService m1 = (ModelService) detail.get(position);
            ((CustomViewHolder) holder).text_business.setText(m1.getServiceName());
            ((CustomViewHolder) holder).text_date.setText(m1.getCreatedDate());
            ((CustomViewHolder) holder).text_service_name.setText(m1.getServiceName());
            ((CustomViewHolder) holder).text_reuestid.setText(m1.getRequestId());
            pos = position;
            Picasso.with(mContext)
                    .load(m1.getServiceImage())

                    .into(((CustomViewHolder) holder).image_background);
            //   if (Integer.parseInt(m1.getServicesCount()) > 0) {
            ((CustomViewHolder) holder).text_response.setText(m1.getServicesCount() + " response");
            if (Integer.parseInt(m1.getServicesCount()) > 0) {
                if (Integer.parseInt(m1.getServicesCount()) == 1) {
                    ((CustomViewHolder) holder).text_response.setText(m1.getServicesCount() + " User Found");
                } else {
                    ((CustomViewHolder) holder).text_response.setText(m1.getServicesCount() + " Users Found");
                }
                ((CustomViewHolder) holder).text_response.setVisibility(View.VISIBLE);

            } else {

                ((CustomViewHolder) holder).text_response.setVisibility(View.GONE);
                ((CustomViewHolder) holder).text_response.setText("Your  request has been confirmed. You will get responses very soon.");
            }

            //   }
            if (m1.getNewVendorCount().equalsIgnoreCase("0")) {
                ((CustomViewHolder) holder).text_new_usercount.setVisibility(View.GONE);
            } else {
                ((CustomViewHolder) holder).text_new_usercount.setVisibility(View.VISIBLE);
                ((CustomViewHolder) holder).text_new_usercount.setText(m1.getNewVendorCount().trim());
            }

            ((CustomViewHolder) holder).img_cancelrequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClickListener(position, 6);

                }
            });

            ((CustomViewHolder) holder).img_view_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClickListener(position, 7);

                }
            });
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

        TextView text_response, text_business, text_new_usercount, text_reuestid, text_service_name, text_date;
        ImageView image_background, img_cancelrequest,img_view_detail;
        CardView card_view;

        public CustomViewHolder(View view) {
            super(view);
            this.text_response = (TextView) view.findViewById(R.id.text_response);
            this.text_business = (TextView) view.findViewById(R.id.text_business);
            this.image_background = (ImageView) view.findViewById(R.id.image_background);
            this.text_reuestid = (TextView) view.findViewById(R.id.text_reuestid);
            this.text_new_usercount = (TextView) view.findViewById(R.id.text_new_usercount);
            this.img_cancelrequest = (ImageView) view.findViewById(R.id.img_menu);
            this.img_view_detail = (ImageView) view.findViewById(R.id.img_view_detail);
            this.text_date = (TextView) view.findViewById(R.id.text_date);
            this.card_view = (CardView) view.findViewById(R.id.card_view);
            this.text_service_name = (TextView) view.findViewById(R.id.text_service_name);
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

