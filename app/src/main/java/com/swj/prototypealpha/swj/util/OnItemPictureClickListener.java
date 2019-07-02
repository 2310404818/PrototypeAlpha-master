package com.swj.prototypealpha.swj.util;

import android.widget.ImageView;

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
public interface OnItemPictureClickListener {
    void onItemPictureClick(int itemPostion, int i, Picture url, List<Picture> urlList, ImageView imageView);
}
