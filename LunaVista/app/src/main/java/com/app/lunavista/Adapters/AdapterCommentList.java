package com.app.lunavista.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
public class AdapterCommentList extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    ArrayList<String> item_weight = new ArrayList<String>();
    ArrayAdapter<String> product_adapter;
    ArrayList<ModelVideo> detail;
    OnCustomItemClicListener listener;

    public AdapterCommentList(Context context, OnCustomItemClicListener lis, ArrayList<ModelVideo> listProductsOrdered) {

        this.mContext = context;
        this.detail = listProductsOrdered;
        this.listener = lis;
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    public class ViewHolder {
        TextView text_name, text_comment, text_date;
        ImageView image_user, image_like;
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
            convertView = inflater.inflate(R.layout.row_comment, null, false);
            holder.text_name = (TextView) convertView
                    .findViewById(R.id.text_name);
            holder.text_comment = (TextView) convertView.findViewById(R.id.text_comment);
            holder.text_date = (TextView) convertView
                    .findViewById(R.id.text_date);
            holder.image_user = (ImageView) convertView.findViewById(R.id.image_user);
            holder.image_like = (ImageView) convertView.findViewById(R.id.image_like);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            holder.text_name.setText(detail.get(position).getName());
            holder.text_comment.setText(detail.get(position).getComment());
            holder.text_date.setText(detail.get(position).getDate());

            if (detail.get(position).getIsFavorite().equalsIgnoreCase("1")) {
               holder.image_like.setImageResource(R.drawable.thumb_red);
            } else {
                holder.image_like.setImageResource(R.drawable.like);
            }

            Picasso.with(mContext)
                    .load(detail.get(position).getSongThumb())
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.placeholder)
                    .into((holder.image_user));


            holder.image_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("image", "likecomment");
                    listener.onItemClickListener(position, 1);
                }
            });


        } catch (Exception e) {

        }


        return convertView;
    }
}