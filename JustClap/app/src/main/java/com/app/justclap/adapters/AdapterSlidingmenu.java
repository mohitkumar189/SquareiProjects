package com.app.justclap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.justclap.R;

import com.app.justclap.interfaces.OnCustomItemClicListener;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterSlidingmenu extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;

    String[] detail;
    Integer[] img;
    OnCustomItemClicListener listener;


    public AdapterSlidingmenu(Context mContext,
                              String[] lis, Integer[] detailList, OnCustomItemClicListener list) {
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.mContext = mContext;
        this.detail = lis;
        this.img = detailList;
        this.listener = list;
    }


    public class ViewHolder {
        TextView service_name;
        ImageView image;

    }

    @Override
    public int getCount() {

        return detail.length;

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

            convertView = inflater.inflate(R.layout.row_sliding_menu, null, false);

            holder.service_name = (TextView) convertView.findViewById(R.id.text_items);
            holder.image = (ImageView) convertView.findViewById(R.id.image_items);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {

            holder.service_name.setText(detail[position]);

            holder.image.setImageResource(img[position]);
        /*  Picasso.with(mContext).load(img[position])
                    //  .placeholder(R.drawable.default_product)
                    .into(holder.image);*/
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

    private void getCustomOption() {


    }

}