package com.app.schoolapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.schoolapp.R;
import com.app.schoolapp.interfaces.OnCustomItemClicListener;
import com.app.schoolapp.model.NavigationViewModel;

import java.util.List;

/**
 * Created by mohit kumar on 6/2/2017.
 */

public class NavigationViewAdapter extends RecyclerView.Adapter<NavigationViewAdapter.CustomViewHolder> {

    List<NavigationViewModel> detail;
    Context mContext;
    OnCustomItemClicListener listener;
    private Boolean isWardsList = false;

    public NavigationViewAdapter(Context context, OnCustomItemClicListener lis, List<NavigationViewModel> list, Boolean isWardsList) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;
        this.isWardsList = isWardsList;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_navigation_view, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {

        if (detail.get(i).getNavText() != null)
            customViewHolder.tvNavText.setText(detail.get(i).getNavText());

        if (detail.get(i).getNavImage() != null)
            customViewHolder.navIcon.setImageDrawable(detail.get(i).getNavImage());

        customViewHolder.relative1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWardsList)
                    listener.onItemClickListener(i, 2);
                else listener.onItemClickListener(i, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView tvNavText;
        ImageView navIcon;
        RelativeLayout relative1;

        public CustomViewHolder(View view) {
            super(view);
            this.tvNavText = (TextView) view.findViewById(R.id.tvNavText);
            this.navIcon = (ImageView) view.findViewById(R.id.navIcon);
            this.relative1 = (RelativeLayout) view.findViewById(R.id.relative1);
        }
    }
}