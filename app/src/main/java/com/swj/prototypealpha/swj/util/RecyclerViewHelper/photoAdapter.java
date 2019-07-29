package com.swj.prototypealpha.swj.util.RecyclerViewHelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.swj.prototypealpha.Enity.photoEntity;
import com.swj.prototypealpha.R;

import java.util.ArrayList;

public class photoAdapter extends RecyclerView.Adapter<photoAdapter.ViewHolder> {


    private Context mContext;
    public ArrayList<photoEntity> photoEntities;
    private OnItemClickListener listener;
    public photoAdapter(Context context,ArrayList<photoEntity> photoEntities){
        this.mContext =context;
        this.photoEntities = photoEntities;

    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        if(mContext == null)
        {
            mContext = viewGroup.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.photo_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

            Glide.with(mContext)
                    .load(photoEntities.get(i).getPath())
                    .into(viewHolder.imageView);
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        listener.onClick(i);
                    }
                }
            });
    }
    @Override
    public int getItemCount() {
        return photoEntities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public CardView  cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView =(CardView)itemView;
            imageView = itemView.findViewById(R.id.bird_image);

        }
    }
    public interface OnItemClickListener{
        void onClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
    }
    public void remove(int i){
        photoEntities.remove(i);
        notifyItemRemoved(i);
    }

}
