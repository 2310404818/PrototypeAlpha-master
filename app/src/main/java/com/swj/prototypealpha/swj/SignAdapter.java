package com.swj.prototypealpha.swj;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.swj.prototypealpha.R;

import java.util.List;

public class SignAdapter extends RecyclerView.Adapter<SignAdapter.ViewHolder> {

    String TAG = "SignAdapter";

    private Context context;

    private List<Picture> signs;

    public SignAdapter(List<Picture> pictureList)
    {
        this.signs = pictureList;
    }
    @NonNull
    @Override
    public SignAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(context == null)
        {
            context = viewGroup.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.sign_item,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Picture picture = signs.get(i);
        viewHolder.imageView.setImageBitmap(picture.getImageID());
    }

    @Override
    public int getItemCount() {
        return signs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public CardView cardView;
        public ImageView imageView;

        public ViewHolder(View view)
        {
            super(view);
            cardView = (CardView)view;
            imageView = view.findViewById(R.id.sign);
        }
    }

    public void removeItem(int position) {
        Log.e(TAG, "removeItem: "+"size"+getItemCount());
        Log.e(TAG, "removeItem: "+ position);
        signs.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
}
