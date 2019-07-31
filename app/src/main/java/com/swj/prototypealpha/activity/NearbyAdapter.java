package com.swj.prototypealpha.activity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.PoiItem;
import com.swj.prototypealpha.R;

import java.util.ArrayList;

/**
 * Created by mardawang on 2017/7/5.
 * <p>
 * wy363681759@163.com
 */

public class NearbyAdapter extends BaseAdapter {
    private ArrayList<PoiItem> poiItems;
    Context mcontext;

    public NearbyAdapter(Context context, ArrayList<PoiItem> poilist) {
        mcontext = context;
        this.poiItems = poilist;
    }

//    //接口回调
//    public interface onClickMyTextView{
//        public void myTextViewClick(int id);
//    }
//    public void setOnClickMyTextView(onClickMyTextView onClickMyTextView) {
//        this.onClickMyTextView = onClickMyTextView;
//    }


    @Override
    public int getCount() {
        return poiItems == null ? 0 : poiItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.item_location_nearby, null);
        }
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_location_name);
        TextView tv_address = (TextView) convertView.findViewById(R.id.tv_address);
        if(poiItems.get(position).getDistance()<100) {
            tv_name.setText(poiItems.get(position).getTitle());
//        poiItems.get(position).getAdName() + poiItems.get(position).getBusinessArea()
            tv_address.setText(poiItems.get(position).getSnippet() + "  " + poiItems.get(position).getDistance() + "米");
        }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mcontext, poiItems.get(position).getTitle() + "  "+poiItems.get(position).getDistance() + "米", Toast.LENGTH_SHORT).show();

                    for(int i=0;i<parent.getChildCount();i++){
                        View view=parent.getChildAt(i);
                        if(position==i){//当前选中的Item的背景颜色
                            view.setBackgroundColor(Color.parseColor("#999999"));
                        }else{
                            view.setBackgroundColor(Color.parseColor("#00000000"));
                        }
                    }
                }
            });

        return convertView;
    }
}
