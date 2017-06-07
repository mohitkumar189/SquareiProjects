package com.app.justclap.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.justclap.R;

import java.util.ArrayList;

/*****
 * Adapter class extends with ArrayAdapter
 ******/
public class ServiceSpinnerAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private ArrayList data;
    public Resources res;
    LayoutInflater inflater;

    public ServiceSpinnerAdapter(Context mContext, ArrayList objects) {
        super(mContext, 0, objects);

        this.mContext = mContext;
        data = objects;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        int count = super.getCount();
        return count > 0 ? count - 1 : count;
        //return count;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.row_spinner, parent, false);
        String string = (String) data.get(position);
        TextView textView = (TextView) row.findViewById(R.id.text_time);
        textView.setText(string);
        return row;
    }
}
