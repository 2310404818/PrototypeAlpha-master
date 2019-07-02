package com.swj.prototypealpha.swj.util;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class SpinnerAdapter extends ArrayAdapter<String>
{
    private Context mContext;
    private List<String> mListArray;

    public SpinnerAdapter(Context context, List<String> list)
    {
        super(context,android.R.layout.simple_spinner_item,list);
        mContext = context;
        mListArray = list;
    }

    @Override
    public View getDropDownView(int position, View convertView,  ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item,parent,false);
        }
        TextView tv = (TextView)convertView.findViewById(android.R.id.text1);
        tv.setText(mListArray.get(position));
        //tv.setTextSize(22f);
        tv.setTextColor(Color.parseColor("#189ed7"));
        return convertView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_spinner_item,parent,false);
        }
        TextView tv = (TextView)convertView.findViewById(android.R.id.text1);
        tv.setText(mListArray.get(position));
        //tv.setTextSize(22);
        tv.setTextColor(Color.parseColor("#189ed7"));
        return convertView;
    }
}
