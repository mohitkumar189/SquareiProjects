package com.app.justclap.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.justclap.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterDashBoardService extends RecyclerView.Adapter<AdapterDashBoardService.CustomViewHolder> {
    ArrayList<ModelService> detail;
    Context mContext;
    OnCustomItemClicListener listener;
    private int lastPosition = -1;

    public AdapterDashBoardService(Context context, OnCustomItemClicListener lis, ArrayList<ModelService> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_dashboard_service, null);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;

    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {

        Animation animation = AnimationUtils.loadAnimation(mContext, (i > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        customViewHolder.card_view.startAnimation(animation);
        lastPosition = i;
        // setAnimation(customViewHolder.card_view, i);

     /*   ObjectAnimator anim=ObjectAnimator.ofFloat(customViewHolder.card_view,"translationY",0,50);
        anim.setDuration(3000);
        anim.start();*/


        customViewHolder.service_name.setText(detail.get(i).getCategoryName());
        if (detail.get(i).getIs_uniDirectional().equalsIgnoreCase("1")) {
            customViewHolder.service_count.setVisibility(View.VISIBLE);
            if (detail.get(i).getServicesCount().equalsIgnoreCase("1")) {
                customViewHolder.service_count.setText(detail.get(i).getServicesCount() + " service");
            } else {
                customViewHolder.service_count.setText(detail.get(i).getServicesCount() + " services");
            }
        }
        else if (detail.get(i).getIs_uniDirectional().equalsIgnoreCase("2")) {
            customViewHolder.service_count.setVisibility(View.GONE);
            customViewHolder.service_count.setText("");

        }

        Picasso.with(mContext)
                .load(mContext.getResources().getString(R.string.img_url)
                        + detail.get(i).getCategoryBGImage())
                        // .transform(new CircleTransform())
                        .placeholder(R.drawable.default_image)
                .into(customViewHolder.image);

    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView service_name, service_count;
        ImageView image;
        CardView card_view;


        public CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.service_count = (TextView) view.findViewById(R.id.text_services_count);
            this.service_name = (TextView) view.findViewById(R.id.text_services_name);
            this.image = (ImageView) view.findViewById(R.id.image_background);
            this.card_view = (CardView) view.findViewById(R.id.card_view);

        }

        public void clearAnimation() {
            card_view.clearAnimation();
        }


        @Override
        public void onClick(View v) {


            listener.onItemClickListener(getPosition(), 1);


        }

           /* Log.e("position", getPosition() + "");
            listener.onItemClickListener(getPosition(), 1);*/
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(CustomViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        ((CustomViewHolder) holder).clearAnimation();
    }
}