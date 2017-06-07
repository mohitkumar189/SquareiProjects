package com.app.justclap.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.activities.UserProfile;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.app.justclap.utils.CircleTransform;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterFragmentVendorResponses extends RecyclerView.Adapter<AdapterFragmentVendorResponses.CustomViewHolder> {
    ArrayList<ModelService> detail;
    Context mContext;
    OnCustomItemClicListener listener;
    int pos = 0;
    ArrayList<HashMap<String, String>> subCatlist = new ArrayList<HashMap<String, String>>();
    String array = "";

    public AdapterFragmentVendorResponses(Context context, OnCustomItemClicListener lis, ArrayList<ModelService> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_requests, null);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;

    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {

        //  holder.text_response.setText(detail.get(position).getServicesCount() + " Response");
        holder.text_business.setText(detail.get(position).getServiceName());
        pos = position;
        Picasso.with(mContext)
                .load(detail.get(position).getServiceImage())

                .into(holder.image_background);

        subCatlist = new ArrayList<HashMap<String, String>>();

        holder.scroll.removeAllViews();
        array = detail.get(position).getVendorArray();

        JSONArray subCategory = null;

        try {
            subCategory = new JSONArray(array);
            Log.e("arrayVendor", "*" + subCategory);
            if (subCategory.length() > 0) {
                holder.text_response.setVisibility(View.GONE);

                subCatlist.removeAll(subCatlist);
                for (int i = 0; i < subCategory.length(); i++) {
                    JSONObject item = subCategory.getJSONObject(i);
                    HashMap<String, String> hm = new HashMap<String, String>();
                    hm.put("id", item.getString("userID"));
                    hm.put("name", item.getString("name"));
                    hm.put("image", mContext.getResources().getString(R.string.img_url) + item.getString("profileImage"));
                    subCatlist.add(hm);
                    addSubCategory(holder, i, pos);

                }
            } else {

                holder.text_response.setText("Your  request has been confirmed. You will get responses very soon.");
                holder.text_response.setVisibility(View.VISIBLE);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        holder.text_viewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClickListener(position, 4);

            }
        });
        holder.text_cancelrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClickListener(position, 5);

            }
        });

        holder.img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClickListener(position, 6);

            }
        });
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClickListener(position, 1);

            }
        });
    }


    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView text_response, text_business, text_viewDetail, text_cancelrequest;
        ImageView image_background, img_menu;
        LinearLayout scroll;
        CardView card_view;

        public CustomViewHolder(View view) {
            super(view);
            this.text_response = (TextView) view.findViewById(R.id.text_response);
            this.text_business = (TextView) view.findViewById(R.id.text_business);
            this.image_background = (ImageView) view.findViewById(R.id.image_background);
            this.text_viewDetail = (TextView) view.findViewById(R.id.text_viewDetail);
            this.text_cancelrequest = (TextView) view.findViewById(R.id.text_cancelrequest);
            this.scroll = (LinearLayout) view.findViewById(R.id.ll_scrol);
            this.img_menu = (ImageView) view.findViewById(R.id.img_menu);
            this.card_view = (CardView) view.findViewById(R.id.card_view);
        }


    }

    private void addSubCategory(CustomViewHolder holder, final int posit, final int rowPsoi) {


        Log.e("position", "**" + posit);
        HashMap<String, String> hm = subCatlist.get(posit);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_subcategory, null);
        view.setTag(posit);
        ImageView image = (ImageView) view.findViewById(R.id.image_vendor);

        try {

            Picasso.with(mContext)
                    .load(subCatlist.get(Integer.parseInt(view.getTag().toString())).get("image"))
                    //  .transform(new CircleTransform())
                    .placeholder(R.drawable.user)
                    .into(image);

        } catch (Exception e) {
            e.printStackTrace();
        }


        ((TextView) view.findViewById(R.id.text_quote)).setVisibility(View.VISIBLE);
        Log.e("text_quote", subCatlist.get(Integer.parseInt(view.getTag().toString())).get("name"));
        ((TextView) view.findViewById(R.id.text_quote)).setText(subCatlist.get(Integer.parseInt(view.getTag().toString())).get("name"));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int rowPosition;
                try {
                    LinearLayout paretView = (LinearLayout) view.getParent();
                    rowPosition = Integer.parseInt(paretView.getTag().toString());
                    subCatlist = new ArrayList<HashMap<String, String>>();

                    String array = detail.get(rowPosition).getVendorArray();
                    JSONArray subCategory = null;

                    subCategory = new JSONArray(array);

                    for (int i = 0; i < subCategory.length(); i++) {
                        JSONObject item = subCategory.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<String, String>();
                        hm.put("id", item.getString("userID"));
                        hm.put("name", item.getString("name"));
                        hm.put("email", item.getString("email"));
                        hm.put("mobile", item.getString("mobile"));
                        hm.put("image", mContext.getResources().getString(R.string.img_url) + item.getString("profileImage"));
                        subCatlist.add(hm);
                    }
                    listener.onItemClickListener(rowPosition, 5);

                    Intent in = new Intent(mContext, UserProfile.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("user_id", subCatlist.get(Integer.parseInt(view.getTag().toString())).get("id"));
                    in.putExtra("name", "");
                    in.putExtra("email", "");
                    in.putExtra("mobile", "");
                    in.putExtra("image", "");
                    mContext.startActivity(in);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        holder.scroll.addView(view);
        holder.scroll.setTag(rowPsoi);
    }


}

