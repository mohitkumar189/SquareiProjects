package com.app.justclap.vendoradapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.model.ModelVendor;
import com.app.justclap.utils.AppUtils;

import java.util.ArrayList;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterVendorOngoingleads extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<ModelVendor> detail;
    Context mContext;
    OnCustomItemClicListener listener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    int[] color = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e, R.drawable.f, R.drawable.g, R.drawable.h, R.drawable.i, R.drawable.j, R.drawable.k, R.drawable.l, R.drawable.m, R.drawable.n, R.drawable.o, R.drawable.p, R.drawable.q, R.drawable.r, R.drawable.s, R.drawable.t, R.drawable.u, R.drawable.v, R.drawable.w, R.drawable.x, R.drawable.y, R.drawable.z};

    public AdapterVendorOngoingleads(Context context, OnCustomItemClicListener lis, ArrayList<ModelVendor> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_vendor_ongoingleads, parent, false);

            vh = new CustomViewHolder(v);

        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;

    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
            this.progressBar.getIndeterminateDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof CustomViewHolder) {

            ModelVendor m1 = (ModelVendor) detail.get(position);

            ((CustomViewHolder) holder).text_name.setText(m1.getUserName());
            ((CustomViewHolder) holder).text_requestcode.setText(m1.getRequestCode());
            ((CustomViewHolder) holder).text_date.setText(m1.getRequestDate() + "  " + m1.getRequestTime());
            // ((CustomViewHolder) holder).text_distance.setText(m1.getDescription());

            try {
                if (detail.get(position).getUserName().length() > 0) {
                    char c = detail.get(position).getUserName().toUpperCase().charAt(0);
                    int pos = (int) c;
                    pos = pos % 65;
                    ((CustomViewHolder) holder).circle_image.setImageResource(color[pos]);
                    ((CustomViewHolder) holder).text_name_title.setText(detail.get(position).getUserName().toUpperCase().charAt(0) + "");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            ((CustomViewHolder) holder).card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClickListener(position, 1);
                }
            });
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }


    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView text_name, text_email, text_requestcode, text_date, text_name_title, text_distance,text_quote,text_quotevalue;
        ImageView circle_image;
        CardView card_view;

        public CustomViewHolder(View view) {
            super(view);

            this.text_name = (TextView) view.findViewById(R.id.text_name);
            this.text_name_title = (TextView) view.findViewById(R.id.text_name_title);
            this.text_date = (TextView) view.findViewById(R.id.text_date);
            this.text_distance = (TextView) view.findViewById(R.id.text_distance);
            this.text_requestcode = (TextView) view.findViewById(R.id.text_requestcode);
            this.text_email = (TextView) view.findViewById(R.id.text_email);
            this.text_quotevalue = (TextView) view.findViewById(R.id.text_quotevalue);
            this.text_quote = (TextView) view.findViewById(R.id.text_quote);
            this.card_view = (CardView) view.findViewById(R.id.card_view);
            this.circle_image = (ImageView) view.findViewById(R.id.circle_image);

            AppUtils.fontGotham_Medium(text_name, mContext);
            AppUtils.fontGotham_Book(text_date, mContext);
            AppUtils.fontGotham_Book(text_distance, mContext);
            AppUtils.fontGotham_Book(text_quotevalue, mContext);
            AppUtils.fontGotham_Book(text_quote, mContext);
            AppUtils.fontGotham_Book(text_name_title, mContext);
            AppUtils.fontGotham_Book(text_email, mContext);
            AppUtils.fontGotham_Book(text_requestcode, mContext);

        }
    }


    @Override
    public int getItemViewType(int position) {
        ModelVendor m1 = (ModelVendor) detail.get(position);
        if (detail.get(position).getRowType() == 1) {
            return VIEW_ITEM;
        } else if (detail.get(position).getRowType() == 2) {
            return VIEW_PROG;
        }
        return -1;
    }
}

