package com.app.justclap.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.app.justclap.R;

import java.util.ArrayList;

import com.app.justclap.interfaces.OnCustomItemClicListener;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterRadiobuttonQues extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;

    ArrayList<String> detail;
    boolean mCurrentlyCheckedRB;

    OnCustomItemClicListener listener;


    public AdapterRadiobuttonQues(Context mContext,
                                  OnCustomItemClicListener lis, ArrayList<String> detailList) {
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.mContext = mContext;
        this.detail = detailList;
        this.listener = lis;
    }


    public class ViewHolder {
        TextView category_name;
        RadioButton radio_button;

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
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();

            v = inflater.inflate(R.layout.row_radio_ques, null, false);

            holder.category_name = (TextView) v.findViewById(R.id.text_category);
            holder.radio_button = (RadioButton) v.findViewById(R.id.radio_button);

            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }

        try {

            holder.category_name.setText(Html.fromHtml(detail.get(position)));
       /*     if (detail.get(position).getSelection_position() == 1) {
                holder.radio_button.setChecked(true);
            } else
            {
                holder.radio_button.setChecked(false);
            }
*/

            // checks if we already have a checked radiobutton. If we don't, we can assume that
// the user clicked the current one to check it, so we can remember it.



/*
            Picasso.with(mContext).load(detail.get(position).getImage())
                    //  .placeholder(R.drawable.default_product)
                    .skipMemoryCache()
                    .into(holder.item_image);*/

        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        }


        v.setOnClickListener(new View.OnClickListener()

                             {
                                 @Override
                                 public void onClick(View v) {

                                     listener.onItemClickListener(position, 1);
                                 }
                             }

        );
        holder.radio_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClickListener(position, 2);

            }
        });



        return v;
    }

    private void getCustomOption() {


    }

}