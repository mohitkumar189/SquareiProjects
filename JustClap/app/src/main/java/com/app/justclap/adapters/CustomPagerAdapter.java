package com.app.justclap.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.justclap.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;

/**
 * Created by admin on 10-12-2015.
 */
public class CustomPagerAdapter extends PagerAdapter {


    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<ModelService> categoryDataImages;
    OnCustomItemClicListener listener;

    public CustomPagerAdapter(Context context, OnCustomItemClicListener lis, ArrayList<ModelService> categoryDataImages) {
        mContext = context;
        this.categoryDataImages = categoryDataImages;
        this.listener = lis;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return categoryDataImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.row_pager_adapter, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.images_flipper);
        try {


            Picasso.with(mContext)
                    .load(categoryDataImages.get(position).getOfferImage())
                            // .skipMemoryCache()
                    .placeholder(R.drawable.default_product)
                    .into(imageView);
        } catch (Exception e) {

        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClickListener(position, 11);

            }
        });

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

}
