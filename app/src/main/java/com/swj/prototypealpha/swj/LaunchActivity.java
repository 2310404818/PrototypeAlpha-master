package com.swj.prototypealpha.swj;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.swj.prototypealpha.R;
import com.swj.prototypealpha.activity.MainActivity;
import com.swj.prototypealpha.swj.util.ItemBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 移动检查后续界面
 * 项目列表
 * 搜索项目
 */
public class LaunchActivity extends AppCompatActivity
{
    private Toolbar tlb_launch;

    private ListView recvv_launch;

   // private ItemAdapter adapter;
    private MyItemAdapter mAdapter;
    private List<ItemBean> itemList = new ArrayList<>();
    private List<ItemBean> old_Data = new ArrayList<>();
    private static String TAG = LaunchActivity.class.getSimpleName();
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
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint("查询通知");
        searchView.setIconified(true);
        recvv_launch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemBean itemBean = old_Data.get(position);
                Intent intent = new Intent(LaunchActivity.this,CheckPerson.class);
                intent.putExtra("title", itemBean.getTitle());
                intent.putExtra("address",itemBean.getContent());
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
        tlb_launch = findViewById(R.id.tlb_choose);
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
        itemList = myJsonArray();
        initViews();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            Intent intent = new Intent(LaunchActivity.this,MainActivity.class);
            startActivity(intent);
            LaunchActivity.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * 服务器拉取数据
     */
    public List<ItemBean> myJsonArray(){
        final ArrayList<ItemBean> myData =new ArrayList<>();
        itemList.clear();

        RequestQueue requestQueue =Volley.newRequestQueue(this);
        String url = "http://47.102.119.140:8080/mobile_inspection_war/Project";
        JsonArrayRequest jsonArrayRequest =new JsonArrayRequest(Request.Method.POST,url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
            //    Log.d("已经调用了", "没错就是这样，你爸爸还是你爸爸");
                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject Project = (JSONObject) response.get(i);

                        Bitmap leftImage = BitmapFactory.decodeResource(getResources(),R.mipmap.project_item);
                        Bitmap rightArrow = BitmapFactory.decodeResource(getResources(),R.mipmap.add);
                        String name = Project.getString("ProjectName");
                        String address = Project.getString("Address");
                  //      Log.d(name+"    "+address,"这是名称");

                        ItemBean itemBean =new ItemBean(name,address,leftImage,rightArrow);
                        myData.add(itemBean);

                    }
                    old_Data = itemList;
                    mAdapter = new MyItemAdapter();
                    recvv_launch.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                  //  Log.d("你爸爸","你爸爸");
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG,"Error: "+error.getMessage());
             //   Toast.makeText(getApplicationContext(),"很嗲回答是大号",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
        return myData;
    }
    /**
     * 带模糊搜索的适配器
     */
    private class MyItemAdapter extends BaseAdapter implements Filterable{
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
            TextView item_content =view.findViewById(R.id.item_content);
            item_content.setText(itemBean.getContent());
            return view;
        }

        @Override
        public Filter getFilter() {
            if (null == mFilter){
                mFilter = new MyFilterLaunch();
            }
            return mFilter;
        }

        public class MyFilterLaunch extends Filter{

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
