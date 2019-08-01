package com.swj.prototypealpha.activity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.PoiItem;
import com.autonavi.ae.gmap.gloverlay.GLRctRouteOverlay;
import com.swj.prototypealpha.R;
import com.swj.prototypealpha.swj.SignAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mardawang on 2017/7/5.
 * <p>
 * wy363681759@163.com
 */

public class NearbyAdapter extends BaseAdapter {
    private ArrayList<PoiItem> poiItems;
    private String Latitude, longitude, posName;
    private AMap mAmap;
    private static ArrayList<Marker>  markers = new ArrayList<>();
    Marker mark;
    Context mcontext;

    public NearbyAdapter(Context context, ArrayList<PoiItem> poilist, String posName, AMap mAmap, ArrayList<Marker> markers) {
        mcontext = context;
        this.poiItems = poilist;
        this.posName = posName;
        this.mAmap=mAmap;
        this.markers = markers;
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

    public String getPosName(){
        return posName;
    }
    public ArrayList getmarkers(){
        return markers;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.item_location_nearby, null);
        }
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_location_name);
        TextView tv_address = (TextView) convertView.findViewById(R.id.tv_address);
        //if(poiItems.get(position).getDistance()<=100) {
        tv_name.setText(poiItems.get(position).getTitle());
//        poiItems.get(position).getAdName() + poiItems.get(position).getBusinessArea()
        tv_address.setText(poiItems.get(position).getSnippet() + "-" + poiItems.get(position).getDistance() + "米");
       // }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mcontext, poiItems.get(position).getTitle() + "-"+poiItems.get(position).getDistance() + "米", Toast.LENGTH_SHORT).show();
                    posName = poiItems.get(position).getTitle();
                    System.out.println("markers的内容："+markers);
                    for(int i = 0; i < markers.size(); i ++){
                        Marker marker = markers.get(i);
                        marker.remove();
                    }
                    mark =  mAmap.addMarker(getMarkerOptions(poiItems,position));
                    markers.add(mark);
//                    for(int i=0;i<parent.getChildCount();i++){
//                        View view=parent.getChildAt(i);
//                        if(position==i){//当前选中的Item的背景颜色
//                            view.setBackgroundColor(Color.parseColor("#189ED7"));
//                            marker.remove();
//                            marker =  mAmap.addMarker(getMarkerOptions(poiItems,position));
//                            System.out.println("地名：  "+posName+"经纬度："+ poiItems.get(position).getLatLonPoint().getLatitude()+"  "+poiItems.get(position).getLatLonPoint().getLongitude());
//                        }else{
//                            view.setBackgroundColor(Color.parseColor("#00000000"));
//                        }
//                    }
                }
            });

        return convertView;
    }


    //自定义一个图钉，并且设置图标，当我们点击图钉时，显示设置的信息
    private MarkerOptions getMarkerOptions(ArrayList<PoiItem> poiItems,int position) {
        //设置图钉选项
        MarkerOptions options = new MarkerOptions();

        //图标
        //options.icon(BitmapDescriptorFactory.fromResource(R.drawable.mine_locate));
        //位置
        options.position(new LatLng(poiItems.get(position).getLatLonPoint().getLatitude(), poiItems.get(position).getLatLonPoint().getLongitude()));
        StringBuffer buffer = new StringBuffer();
        buffer.append(poiItems.get(position).getCityName());
        //标题
        options.title(buffer.toString());
        //子标题
//        options.snippet("这里好火");
        //设置多少帧刷新一次图片资源
//        options.period(60);
        return options;
    }
}
