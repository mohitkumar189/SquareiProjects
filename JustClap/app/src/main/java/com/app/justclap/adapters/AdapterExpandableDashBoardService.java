package com.app.justclap.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterExpandableDashBoardService extends BaseAdapter {

    ArrayList<ModelService> detail;
    Context mContext;
    OnCustomItemClicListener listener;
    private int lastPosition = -1;
    private LayoutInflater inflater;

    public AdapterExpandableDashBoardService(Context context, OnCustomItemClicListener lis, ArrayList<ModelService> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public class ViewHolder {
        TextView service_name, service_count;
        ImageView image;
        CardView card_view;

    }

    @Override
    public int getCount() {

        return detail.size();

    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = inflater.inflate(R.layout.row_dashboard_service, null, false);


            holder.service_count = (TextView) convertView.findViewById(R.id.text_services_count);
            holder.service_name = (TextView) convertView.findViewById(R.id.text_services_name);
            holder.image = (ImageView) convertView.findViewById(R.id.image_background);
            holder.card_view = (CardView) convertView.findViewById(R.id.card_view);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {


            holder.service_name.setText(detail.get(position).getCategoryName());
            if (detail.get(position).getIs_uniDirectional().equalsIgnoreCase("1")) {
                holder.service_count.setVisibility(View.VISIBLE);
                if (detail.get(position).getServicesCount().equalsIgnoreCase("1")) {
                    holder.service_count.setText(detail.get(position).getServicesCount() + " service");
                } else {
                    holder.service_count.setText(detail.get(position).getServicesCount() + " services");
                }
            } else if (detail.get(position).getIs_uniDirectional().equalsIgnoreCase("2")) {
                holder.service_count.setVisibility(View.GONE);
                holder.service_count.setText("");

            }

            Picasso.with(mContext)
                    .load(mContext.getResources().getString(R.string.img_url)
                            + detail.get(position).getCategoryBGImage())
                    // .transform(new CircleTransform())
                    .placeholder(R.drawable.default_image)
                    .into(holder.image);

          /*  Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            convertView.startAnimation(animation);
            lastPosition = position;*/

            Animation fadeInAnimation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.rotateimage : R.anim.rotateimage);
            convertView.startAnimation(fadeInAnimation);
            lastPosition = position;

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClickListener(position, 1);

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClickListener(position, 1);
            }
        });


        return convertView;
    }

}