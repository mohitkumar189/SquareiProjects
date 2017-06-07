package com.app.justclap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
public class AdapterQuestionpage extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;

    ArrayList<ModelService> detail;


    OnCustomItemClicListener listener;


    public AdapterQuestionpage(Context mContext,
                               OnCustomItemClicListener lis, ArrayList<ModelService> detailList) {
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.mContext = mContext;
        this.detail = detailList;
        this.listener = lis;
    }


    public class ViewHolder {
        TextView service_name;
        ImageView img_service;

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

            convertView = inflater.inflate(R.layout.row_questionlist, null, false);

            holder.service_name = (TextView) convertView.findViewById(R.id.text_category);
            holder.img_service = (ImageView) convertView.findViewById(R.id.imag_service);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            holder.service_name.setText(detail.get(position).getIconText());
            Picasso.with(mContext)
                    .load(mContext.getResources().getString(R.string.img_url)
                            + detail.get(position).getIconImage1())
                            .transform(new CircleTransform())
                   // .placeholder(R.drawable.profile_about_user)
                    .into(holder.img_service);


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