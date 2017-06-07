package com.app.justclap.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
public class AdapterReviews extends RecyclerView.Adapter<AdapterReviews.CustomViewHolder> {
    ArrayList<ModelService> detail;
    Context mContext;
    OnCustomItemClicListener listener;


    public AdapterReviews(Context context, OnCustomItemClicListener lis, ArrayList<ModelService> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_reviews, null);
        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {

        customViewHolder.text_name.setText(detail.get(i).getReviewName());
        if (!detail.get(i).getReview().equalsIgnoreCase("null")) {
            customViewHolder.text_review.setText(detail.get(i).getReview());
        } if (!detail.get(i).getReviewDate().equalsIgnoreCase("null")) {
            customViewHolder.text_date.setText(detail.get(i).getReviewDate());
        }
        Picasso.with(mContext)
                .load(detail.get(i).getReviewimage())
                         .transform(new CircleTransform())
                        .placeholder(R.drawable.user)
                .into(customViewHolder.image_review);



    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView text_name,text_review,text_date;
        ImageView image_review;


        public CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            this.text_name = (TextView) view.findViewById(R.id.text_name);
            this.text_review = (TextView) view.findViewById(R.id.text_review);
            this.image_review = (ImageView) view.findViewById(R.id.image_review);
            this.text_date= (TextView) view.findViewById(R.id.text_date);
        }

        @Override
        public void onClick(View v) {


            listener.onItemClickListener(getPosition(), 1);


        }

           /* Log.e("position", getPosition() + "");
            listener.onItemClickListener(getPosition(), 1);*/
    }

}

