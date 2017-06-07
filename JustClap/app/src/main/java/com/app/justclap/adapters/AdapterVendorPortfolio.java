package com.app.justclap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.app.justclap.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterVendorPortfolio extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    ArrayList<ModelService> detail;
    OnCustomItemClicListener listener;


    public AdapterVendorPortfolio(Context mContext,
                                  OnCustomItemClicListener lis, ArrayList<ModelService> detailList) {
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.mContext = mContext;
        this.detail = detailList;
        this.listener = lis;
    }


    public class ViewHolder {
        ImageView image_portfolio, img_delete;

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

            convertView = inflater.inflate(R.layout.row_vendor_portfolio, null, false);
            holder.img_delete = (ImageView) convertView.findViewById(R.id.img_delete);
            holder.image_portfolio = (ImageView) convertView.findViewById(R.id.image_portfolio);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {

            Picasso.with(mContext).load(detail.get(position).getPorfolioImage())
                    .placeholder(R.drawable.placholder_profile)
                    //.skipMemoryCache()
                    // .transform(new CircleTransform())
                    .into(holder.image_portfolio);
        } catch (Exception e) {
            e.printStackTrace();
        }


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClickListener(position, 1);
            }
        });

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClickListener(position, 3);
            }
        });


        return convertView;
    }


}