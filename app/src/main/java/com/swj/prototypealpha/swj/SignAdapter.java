package com.swj.prototypealpha.swj;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swj.prototypealpha.R;

import java.util.List;

import static com.swj.prototypealpha.R.id.tx_sign;

public class SignAdapter extends RecyclerView.Adapter<SignAdapter.ViewHolder> {

  //  String TAG = "SignAdapter";

    private Context context;

    private List<Picture> signs;
    private List<ChargePerson> names;

    public SignAdapter(List<Picture> pictureList,List<ChargePerson> nameList)
    {
        this.signs = pictureList;
        this.names = nameList;
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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,final int i) {
        Picture picture = signs.get(i);
        ChargePerson Charge = names.get(i);
        viewHolder.textView.setText(Charge.getName());
        viewHolder.imageView.setImageBitmap(picture.getImageID());
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return signs.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public CardView cardView;
        public ImageView imageView;
        public TextView  textView;

        public ViewHolder(View view)
        {
            super(view);
            cardView = (CardView)view;
            imageView = view.findViewById(R.id.sign);
            textView = view.findViewById(R.id.tx_sign);
        }
    }

    public void removeItem(final int position) {
        final int pos=position;
        AlertDialog.Builder dialog=new AlertDialog.Builder(context);
        dialog.setTitle("照片");
        dialog.setMessage("确认删除吗？");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialog, int which) {
                signs.remove(pos);
                names.remove(pos);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialog, int which) {

            }
        });
        dialog.show();

    }
}
