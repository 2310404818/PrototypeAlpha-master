package com.swj.prototypealpha.swj.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.swj.prototypealpha.R;
import com.swj.prototypealpha.swj.Picture;

import java.util.List;

/**
 * <pre>
 *     author : marin
 *     time   : 2019/05/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ImageHodler extends RecyclerView.ViewHolder {
    public  CardView                   cardView;
    private NineGridLayout             nineGridLayout;
    private OnItemPictureClickListener listener;

    public ImageHodler (@NonNull View itemView, OnItemPictureClickListener listener) {
        super(itemView);
        initView(itemView);
        cardView = (CardView)itemView;
        this.listener = listener;
    }

    private void initView (View itemView) {
        nineGridLayout = itemView.findViewById(R.id.nineTestlayout);

    }

    public void setUrlList (List<Picture> urlList) {
        nineGridLayout.setUrlList(urlList);
    }

    public void bindViewHolder (List<Picture> list, int position) {
        setUrlList(list);
    }
}
