package com.swj.prototypealpha.swj;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.swj.prototypealpha.R;
import com.swj.prototypealpha.swj.util.ItemBean;
import com.swj.prototypealpha.swj.util.OnItemClickListener;
import com.swj.prototypealpha.swj.util.RecyclerViewHelper.ItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class LaunchActivity extends AppCompatActivity implements OnItemClickListener
{
    private Toolbar tlb_launch;

    private RecyclerView recvv_launch;

    private ItemAdapter adapter;

    private List<ItemBean> itemList = new ArrayList<>();

    /**
     * 搜索view
     */
    private SearchView searchView;

    /**
     * 初始化视图
     */
    private void initViews() {
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint("查询通知");
        searchView.setIconified(true);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void initUI()
    {
        tlb_launch = findViewById(R.id.tlb_lanuch);
        recvv_launch = findViewById(R.id.recv_launch);
        setSupportActionBar(tlb_launch);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        initUI();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recvv_launch.setLayoutManager(layoutManager);
        adapter = new ItemAdapter(itemList);

        recvv_launch.setAdapter(adapter);
        adapter.setItemClickListener(this);
        Update();

        initViews();

    }

    private void Update()
    {
        itemList.clear();
        Bitmap leftImage = BitmapFactory.decodeResource(getResources(),R.mipmap.check);
        Bitmap rightArrow = BitmapFactory.decodeResource(getResources(),R.mipmap.add);
        ItemBean item0 = new ItemBean("万家丽路BRT中途停靠站建设项目","万家丽路",leftImage,rightArrow);
        ItemBean item1 = new ItemBean("橘子洲大桥提质改造工程","岳麓区",leftImage,rightArrow);
        ItemBean item3 = new ItemBean("湘府路快速化改造建设项目","天心区湘府路段",leftImage,rightArrow);
        ItemBean item2 = new ItemBean("湘府路快速化改造道路工程","长沙市天心区",leftImage,rightArrow);
        ItemBean item6 = new ItemBean("湘府路快速化改造工程","长托路与红旗路西南角",leftImage,rightArrow);
        ItemBean item4 = new ItemBean("市轨道交通洋湖垸消防站","坪塘大道庵子冲",leftImage,rightArrow);
        ItemBean item5 = new ItemBean("市轨道交通车站公共区装饰装修工程","星沙筑梦园，7个站点钢结构施工",leftImage,rightArrow);
        ItemBean item7 = new ItemBean("市轨道交通车站地面附属建筑施工项目","长沙市开福区四方坪左岸春天18栋201室",leftImage,rightArrow);

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
        itemList.add(item7);
        adapter.notifyItemChanged(7);

        adapter.notifyItemChanged(0,itemList.size());
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(LaunchActivity.this,CheckPerson.class);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position) {

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
