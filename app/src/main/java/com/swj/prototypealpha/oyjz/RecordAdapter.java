package com.swj.prototypealpha.oyjz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.swj.prototypealpha.R;

import java.util.List;

/**
 * 录音功能实现
 * 适配器
 */
public class RecordAdapter extends ArrayAdapter<Record> {
    private int resourceId;
    public RecordAdapter(Context context, int textViewResourceId, List<Record> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Record record=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView recordImage = (ImageView)view.findViewById(R.id.record_image);
        TextView recordName = (TextView) view.findViewById(R.id.record_name);
        recordName.setText(record.getName());
        recordImage.setImageResource(R.mipmap.ic_launcher);
        return view;
    }
}
