package com.swj.prototypealpha.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.swj.prototypealpha.Enity.NoticeEntity;
import com.swj.prototypealpha.R;
import com.swj.prototypealpha.swj.util.RecyclerViewHelper.NoticeAdapter;

import java.util.ArrayList;
import java.util.List;

public class NoticeResultListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_result_list);
        RecyclerView mRecyclerView = findViewById(R.id.rc_result);
        NoticeEntity entity = (NoticeEntity) getIntent().getSerializableExtra("NoticeResult");
        final List<NoticeEntity> mData = new ArrayList<NoticeEntity>();
        mData.add(entity);
        NoticeAdapter mAdapter = new NoticeAdapter(mData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
