package com.app.justclap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.app.justclap.R;

import java.util.ArrayList;

import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterSelectCategory extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    ArrayList<ModelService> detail;
    OnCustomItemClicListener listener;

    public AdapterSelectCategory(Context mContext,
                                 OnCustomItemClicListener lis, ArrayList<ModelService> detailList) {
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.mContext = mContext;
        this.detail = detailList;
        this.listener = lis;
    }


    public class ViewHolder {
        TextView category_name;
        CheckBox checked_item;

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

            convertView = inflater.inflate(R.layout.row_list_category, null, false);

            holder.category_name = (TextView) convertView.findViewById(R.id.text_category);
            holder.checked_item = (CheckBox) convertView.findViewById(R.id.check_box);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {

            holder.category_name.setText(detail.get(position).getCategoryName());
/*
            Picasso.with(mContext).load(detail.get(position).getImage())
                    //  .placeholder(R.drawable.default_product)
                    .skipMemoryCache()
                    .into(holder.item_image);*/
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