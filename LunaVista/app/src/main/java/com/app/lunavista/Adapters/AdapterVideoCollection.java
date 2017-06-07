package com.app.lunavista.Adapters;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.lunavista.Model.ModelVideo;
import com.app.lunavista.R;
import com.app.lunavista.interfaces.OnCustomItemClicListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterVideoCollection extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<ModelVideo> detail;
    Context mContext;
    OnCustomItemClicListener listener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;


    public AdapterVideoCollection(Context context, OnCustomItemClicListener lis, ArrayList<ModelVideo> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

     /*   View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_shipmentlist, null);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;*/

        RecyclerView.ViewHolder vh = null;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_myvideos, parent, false);

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
            this.progressBar.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof CustomViewHolder) {

            ModelVideo m1 = (ModelVideo) detail.get(position);

            ((CustomViewHolder) holder).text_song_title.setText(m1.getSongTitle());

            ((CustomViewHolder) holder).text_song_desc.setText(m1.getSongDescription());

            ((CustomViewHolder) holder).text_audio.setText(m1.getViewCount());
            if (m1.getIsFavorite().equalsIgnoreCase("1")) {
                ((CustomViewHolder) holder).image_favorite.setImageResource(R.drawable.favorite_red);
            } else {
                ((CustomViewHolder) holder).image_favorite.setImageResource(R.drawable.favorite);
            }

            Picasso.with(mContext)
                    .load(m1.getSongThumb())
                    .placeholder(R.drawable.vedio_placeholder)
                    .into(((CustomViewHolder) holder).image_song);

            ((CustomViewHolder) holder).image_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClickListener(position, 1);
                }
            });

            ((CustomViewHolder) holder).image_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClickListener(position, 2);
                }
            });
            ((CustomViewHolder) holder).rl_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClickListener(position, 3);
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

        TextView text_audio, text_song_title, text_song_desc;
        ImageView image_song, image_favorite, image_share;
        CardView card_view;
        RelativeLayout rl_header;

        public CustomViewHolder(View view) {
            super(view);

            this.text_audio = (TextView) view.findViewById(R.id.text_audio);
            this.text_song_title = (TextView) view.findViewById(R.id.text_song_title);
            this.text_song_desc = (TextView) view.findViewById(R.id.text_song_desc);
            this.image_song = (ImageView) view.findViewById(R.id.image_song);
            this.image_share = (ImageView) view.findViewById(R.id.image_share);
            this.image_favorite = (ImageView) view.findViewById(R.id.image_favorite);
            this.card_view = (CardView) view.findViewById(R.id.card_view);
            this.rl_header = (RelativeLayout) view.findViewById(R.id.rl_header);
        }


    }


    @Override
    public int getItemViewType(int position) {
        ModelVideo m1 = (ModelVideo) detail.get(position);
        if (detail.get(position).getRowType() == 1) {
            return VIEW_ITEM;
        } else if (detail.get(position).getRowType() == 2) {
            return VIEW_PROG;
        }
        return -1;
    }

    public void setFilter(ArrayList<ModelVideo> detailnew) {
        detail = new ArrayList<>();
        detail.addAll(detailnew);
        notifyDataSetChanged();
    }

    public ModelVideo getFilter(int i) {

        return detail.get(i);
    }
}

