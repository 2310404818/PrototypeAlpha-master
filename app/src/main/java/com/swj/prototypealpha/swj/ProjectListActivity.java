package com.swj.prototypealpha.swj;

import android.content.Context;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.swj.prototypealpha.activity.NoticeListActivity;
import com.swj.prototypealpha.activity.NoticeResultListActivity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.swj.prototypealpha.R;
import com.swj.prototypealpha.swj.util.ItemBean;
import com.swj.prototypealpha.swj.util.OnItemClickListener;
import com.swj.prototypealpha.swj.util.RecyclerViewHelper.ItemAdapter;
import com.swj.prototypealpha.swj.util.RecyclerViewHelper.ItemDragAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 项目信息页面
 * 动态加载项目信息
 * 实现搜索栏功能
 */
public class ProjectListActivity extends AppCompatActivity  {
    private Toolbar tlb_pro_list;
    private ListView rv_project_list;
    private MyItemAdapter mAdapter;
    private List<ItemBean> itemList = new ArrayList<>();
    private List<ItemBean> old_Data = new ArrayList<>();
    /**
     * 搜索view
     */
    private SearchView searchView;

    /**
     * 初始化视图
     * 为搜索栏设置监听
     *
     */
    private void initViews() {
        searchView = (SearchView) findViewById(R.id.search_view_proj_info);
        searchView.setQueryHint("查询通知");
        searchView.setIconified(true);
        rv_project_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemBean itemBean = old_Data.get(position);
                Intent intent = new Intent(ProjectListActivity.this,ProjectInfoActivity.class);
                intent.putExtra("titile", itemBean.getTitle());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            //搜索栏键入搜索以后执行动作
            public boolean onQueryTextSubmit(String query) {
                if (searchView != null) {
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                    }
                    searchView.clearFocus();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (TextUtils.isEmpty(query)) {
                    mAdapter.getFilter().filter("");
                } else {
                    mAdapter.getFilter().filter(query);
                }
                return true;
            }
        });
    }

    /**
     * 初始化recycleView,ToolBar
     * 为ToolBar设置返回箭头
     */
    private void initUI()
    {
        tlb_pro_list = findViewById(R.id.tlb_pro_list);
        rv_project_list = findViewById(R.id.rv_project_list);
        setSupportActionBar( tlb_pro_list );
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);
        itemList=Update();
        old_Data = itemList;
        mAdapter = new MyItemAdapter();
        initUI();
        rv_project_list.setAdapter(mAdapter);
        initViews();

    }

    private List<ItemBean> Update()
    {
        ArrayList<ItemBean> myData =new ArrayList<>();
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
        ItemBean item8 = new ItemBean("万家丽路BRT中途停靠站建设项目2","万家丽路",leftImage,rightArrow);
        ItemBean item9 = new ItemBean("橘子洲大桥提质改造工程2","岳麓区",leftImage,rightArrow);
        ItemBean item10 = new ItemBean("湘府路快速化改造建设项目2","天心区湘府路段",leftImage,rightArrow);
        ItemBean item11= new ItemBean("湘府路快速化改造道路工程2","长沙市天心区",leftImage,rightArrow);
        ItemBean item12 = new ItemBean("湘府路快速化改造工程2","长托路与红旗路西南角",leftImage,rightArrow);
        ItemBean item13 = new ItemBean("市轨道交通洋湖垸消防站2","坪塘大道庵子冲",leftImage,rightArrow);
        ItemBean item14 = new ItemBean("市轨道交通车站公共区装饰装修工程2","星沙筑梦园，7个站点钢结构施工",leftImage,rightArrow);
        ItemBean item15 = new ItemBean("市轨道交通车站地面附属建筑施工项目2","长沙市开福区四方坪左岸春天18栋201室",leftImage,rightArrow);


        myData.add(item0);
        myData.add(item1);
        myData.add(item2);
        myData.add(item3);
        myData.add(item4);
        myData.add(item5);
        myData.add(item6);
        myData.add(item7);
        myData.add(item8);
        myData.add(item9);
        myData.add(item10);
        myData.add(item11);
        myData.add(item12);
        myData.add(item13);
        myData.add(item14);
        myData.add(item15);
        return myData;
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

    /**
     * 带模糊搜索的适配器
     */
    private class MyItemAdapter extends BaseAdapter implements Filterable {
        private MyFilterLaunch mFilter;
        @Override
        public int getCount() {
            return old_Data.size();
        }

        @Override
        public Object getItem(int position) {
            return old_Data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);
            ItemBean itemBean = old_Data.get(position);
            TextView title  = view.findViewById(R.id.item_title);
            title.setText(itemBean.getTitle());
            ImageView item_left_image = view.findViewById(R.id.item_left_image);
            item_left_image.setImageBitmap(itemBean. getLeft_image());
            ImageView item_right_arrow = view.findViewById(R.id.item_right_arrow);
            item_right_arrow.setImageBitmap(itemBean.getRight_image());
            return view;
        }

        @Override
        public Filter getFilter() {
            if (null == mFilter){
                mFilter = new MyFilterLaunch();
            }
            return mFilter;
        }

        public class MyFilterLaunch extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<ItemBean> newValues =new ArrayList<>();
                String filterString =constraint.toString().trim();
                if (TextUtils.isEmpty(filterString)){
                    newValues = itemList;
                }
                else {
                    for (ItemBean str:itemList){
                        if (str.getTitle().contains(filterString)){
                            newValues.add(str);
                        }
                    }
                }
                results.values = newValues;
                results.count =newValues.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                old_Data = (ArrayList<ItemBean>)results.values;
                if (results.count>0){
                    mAdapter.notifyDataSetChanged();
                }
                else {
                    mAdapter.notifyDataSetInvalidated();
                }

            }
        }
    }
}
