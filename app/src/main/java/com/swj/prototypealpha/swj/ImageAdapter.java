package com.swj.prototypealpha.swj;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swj.prototypealpha.R;
import com.swj.prototypealpha.swj.util.ImageHodler;
import com.swj.prototypealpha.swj.util.OnItemPictureClickListener;

import java.util.List;

/**
 * 检查记录
 * 添加照片适配器
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageHodler>{

    private Context context;
    private List<Picture> list;
    private OnItemPictureClickListener listener;

    @NonNull
    @Override
    public ImageHodler onCreateViewHolder (@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.image_item,viewGroup,false);
        return new ImageHodler(view,listener);
    }

    @Override
    public void onBindViewHolder (@NonNull ImageHodler imageHodler, int position) {
        imageHodler.bindViewHolder(list,position);
    }

    @Override
    public int getItemCount () {
        return list==null?0:list.size();
    }

    public ImageAdapter(Context context, List<Picture> list, OnItemPictureClickListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }
    /*
    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
    */
}
