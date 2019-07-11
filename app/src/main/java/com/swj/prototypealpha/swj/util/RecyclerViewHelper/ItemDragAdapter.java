package com.swj.prototypealpha.swj.util.RecyclerViewHelper;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.swj.prototypealpha.R;

import java.util.List;

/**
 * Created by luoxw on 2016/6/20.
 */
public class ItemDragAdapter extends BaseItemDraggableAdapter<String, BaseViewHolder> {
    public ItemDragAdapter(List data) {
        super(R.layout.item_draggable_view, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, String item) {
        switch (helper.getLayoutPosition() % 3) {
            case 0:
                helper.setImageResource(R.id.iv_head, R.mipmap.project_item);
                break;
            case 1:
                helper.setImageResource(R.id.iv_head, R.mipmap.project_item);
                break;
            case 2:
                helper.setImageResource(R.id.iv_head, R.mipmap.project_item);
                break;
            default:
                break;
        }
        helper.setText(R.id.tv, item);
    }
}
