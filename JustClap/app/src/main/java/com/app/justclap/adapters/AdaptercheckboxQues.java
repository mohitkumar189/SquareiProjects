package com.app.justclap.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.app.justclap.R;

import java.util.ArrayList;

import com.app.justclap.interfaces.OnCustomItemClicListener;

/**
 * Created by admin on 26-11-2015.
 */
public class AdaptercheckboxQues extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;

    ArrayList<String> detail;
    ArrayList<Boolean> getSelectedItem;
    OnCustomItemClicListener listener;


    public AdaptercheckboxQues(Context mContext,
                               OnCustomItemClicListener lis, ArrayList<String> detailList, ArrayList<Boolean> getSelectedItem) {
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.mContext = mContext;
        this.detail = detailList;
        this.listener = lis;
        this.getSelectedItem = getSelectedItem;
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

            convertView = inflater.inflate(R.layout.row_checkbox_ques, null, false);

            holder.category_name = (TextView) convertView.findViewById(R.id.text_category);
            holder.checked_item = (CheckBox) convertView.findViewById(R.id.check_box);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {

            //  holder.category_name.setText(Html.fromHtml(detail.get(position)));
            holder.checked_item.setText(Html.fromHtml(detail.get(position)));
            holder.checked_item.setChecked(getSelectedItem.get(position));

        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.checked_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClickListener(position, 2);

            }
        });


        return convertView;
    }


}