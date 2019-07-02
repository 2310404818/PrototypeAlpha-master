package com.swj.prototypealpha.swj;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.swj.prototypealpha.R;
import com.swj.prototypealpha.swj.util.ItemBean;
import com.swj.prototypealpha.swj.util.RecyclerViewHelper.ItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class HistoryAccordActivity extends AppCompatActivity implements View.OnClickListener
{
    Toolbar tlb_history;

    RecyclerView recv_hitory;

    private ItemAdapter adapter;

    private List<ItemBean> itemList = new ArrayList<>();

    Button btn_hitory;

    private void InitUI()
    {
        tlb_history = findViewById(R.id.tlb_history);
        recv_hitory = findViewById(R.id.recv_history);
        btn_hitory = findViewById(R.id.btn_history);

        setSupportActionBar(tlb_history);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_accord);

        InitUI();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recv_hitory.setLayoutManager(layoutManager);

        adapter = new ItemAdapter(itemList);
        recv_hitory.setAdapter(adapter);
        btn_hitory.setOnClickListener(this);
        Update();
    }

    private void Update()
    {
        itemList.clear();
        Bitmap leftImage = BitmapFactory.decodeResource(getResources(),R.mipmap.detail);
        Bitmap rightArrow = BitmapFactory.decodeResource(getResources(),R.mipmap.right_arrow);
        ItemBean item0 = new ItemBean("2019.02.27","跟踪复查，对建设方、施工方因未取得施工许可擅自施工下达了《责令改正通知书》各1份",leftImage,rightArrow);
        ItemBean item1 = new ItemBean("2019.01.03","执法服务",leftImage,rightArrow);
        ItemBean item3 = new ItemBean("2018.11.29","收到质安介入延期至2019年2月24日",leftImage,rightArrow);
        ItemBean item2 = new ItemBean("2018.11.16","跟踪复查；督促责任主体办理质安介入延期",leftImage,rightArrow);
        ItemBean item6 = new ItemBean("2018.11.13","跟踪复查，督促责任主体及时办理质安介入延期",leftImage,rightArrow);
        ItemBean item4 = new ItemBean("2018.10.18","跟踪复查",leftImage,rightArrow);
        ItemBean item5 = new ItemBean("2018.08.31","执法检查",leftImage,rightArrow);

        itemList.add(item0);
        adapter.notifyItemChanged(0);
        itemList.add(item1);
        adapter.notifyItemChanged(1);
        itemList.add(item2);
        adapter.notifyItemChanged(2);
        itemList.add(item3);
        adapter.notifyItemChanged(3);
        itemList.add(item4);
        adapter.notifyItemChanged(4);
        itemList.add(item5);
        adapter.notifyItemChanged(5);
        itemList.add(item6);
        adapter.notifyItemChanged(6);

        adapter.notifyItemChanged(0,itemList.size());
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(HistoryAccordActivity.this,CheckPerson.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
