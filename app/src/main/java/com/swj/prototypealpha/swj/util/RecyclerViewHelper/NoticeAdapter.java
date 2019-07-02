package com.swj.prototypealpha.swj.util.RecyclerViewHelper;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.swj.prototypealpha.Enity.NoticeEntity;
import com.swj.prototypealpha.R;
import com.swj.prototypealpha.activity.NoticeInfoActivity;
import java.util.ArrayList;
import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> implements Filterable {

    private List<NoticeEntity> mnotice ;
    private List<NoticeEntity> old_notice ;
    public NoticeAdapter(List<NoticeEntity> mData) {
        mnotice = mData;
        old_notice = mData;
    }
    private MyFilter mFilter;

    @Override
    public Filter getFilter() {
        if (null == mFilter) {
            mFilter = new MyFilter();
        }
        return mFilter;
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView text ;
        //TextView time;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            title = view.findViewById(R.id.tv_titlenotice);
            //time = view.findViewById(R.id.tv_time);
            text = view.findViewById(R.id.tv_content);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notice,viewGroup,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                NoticeEntity noticeEntity = mnotice.get(position);
                //Toast.makeText(view.getContext(),"监听到点击",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), NoticeInfoActivity.class);
                intent.putExtra("noticeinfo",noticeEntity);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);

            }

        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        NoticeEntity noticeEntity = mnotice.get(i);
        viewHolder.text.setText(noticeEntity.getText());
        //viewHolder.time.setText(noticeEntity.getTime());
        viewHolder.title.setText(noticeEntity.getTitle());

    }


    @Override
    public int getItemCount() {
        return mnotice.size();
    }
    public  class MyFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<NoticeEntity> newValues = new ArrayList<NoticeEntity>();
            String filterString = constraint.toString().trim();

            if (TextUtils.isEmpty(filterString)) {
                newValues = mnotice;
            } else {
                for (NoticeEntity str : mnotice) {
                    if (str.getTitle().contains(filterString)) {
                        newValues.add(str);
                    }
                }
            }

            results.values = newValues;
            results.count = newValues.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            old_notice = (ArrayList<NoticeEntity>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                NoticeAdapter.this.notify();
            }
        }
    }

}
