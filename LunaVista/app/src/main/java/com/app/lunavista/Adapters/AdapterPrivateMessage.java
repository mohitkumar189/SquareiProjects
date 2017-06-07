package com.app.lunavista.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.lunavista.AppUtils.CircleTransform;
import com.app.lunavista.Model.ModelVideo;
import com.app.lunavista.R;
import com.app.lunavista.interfaces.OnCustomItemClicListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterPrivateMessage extends RecyclerView.Adapter<AdapterPrivateMessage.CustomViewHolder> {
    ArrayList<ModelVideo> detail;
    Context mContext;
    OnCustomItemClicListener listener;


    public AdapterPrivateMessage(Context context, OnCustomItemClicListener lis, ArrayList<ModelVideo> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_private_message, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int position) {


        customViewHolder.text_name.setText(detail.get(position).getSongTitle());
        customViewHolder.text_comment.setText(detail.get(position).getSongTitle());
        customViewHolder.text_date.setText(detail.get(position).getSongDescription());
        Picasso.with(mContext)
                .load(detail.get(position).getSongThumb())
                 .transform(new CircleTransform())
                 .placeholder(R.drawable.placeholder)
                .into(customViewHolder.image_user);


    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text_name, text_comment, text_date;
        ImageView image_user;


        public CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.text_name = (TextView) view.findViewById(R.id.text_name);
            this.text_date = (TextView) view.findViewById(R.id.text_date);
            this.text_comment = (TextView) view.findViewById(R.id.text_comment);

            this.image_user = (ImageView) view.findViewById(R.id.image_user);

        }

        @Override
        public void onClick(View v) {

            listener.onItemClickListener(getPosition(), 21);

        }

    }

    public void setFilter(ArrayList<ModelVideo> detailnew) {
        detail = new ArrayList<>();
        detail.addAll(detailnew);
        notifyDataSetChanged();
    }

    public ModelVideo getFilter(int i) {

        return detail.get(i);
    }

}