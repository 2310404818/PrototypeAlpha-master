package com.swj.prototypealpha.swj.util.RecyclerViewHelper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swj.prototypealpha.R;
import com.swj.prototypealpha.swj.util.ItemBean;
import com.swj.prototypealpha.swj.util.OnItemClickListener;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> implements View.OnClickListener
{
    private List<ItemBean> itemAdapterList;

    private OnItemClickListener itemClickListener;

    public  ItemAdapter(List<ItemBean> itemBeans)
    {
        itemAdapterList = itemBeans;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder viewHolder, int i) {
        ItemBean itemBean = itemAdapterList.get(i);
        viewHolder.title.setText(itemBean.getTitle());
        viewHolder.content.setText(itemBean.getContent());
        viewHolder.left_image.setImageBitmap(itemBean.getLeft_image());
        viewHolder.right_image.setImageBitmap(itemBean.getRight_image());
        viewHolder.itemView.setTag(i);
    }

    @Override
    public int getItemCount() {
        return itemAdapterList.size();
    }

    @Override
    public void onClick(View v) {
        if(itemClickListener != null)
        {
            itemClickListener.onItemClick((Integer)v.getTag());
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener)
    {
        this.itemClickListener= itemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title;

        private TextView content;

        private ImageView left_image;

        private ImageView right_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            content = itemView.findViewById(R.id.item_content);
            left_image = itemView.findViewById(R.id.item_left_image);
            right_image = itemView.findViewById(R.id.item_right_arrow);
        }
    }
}
