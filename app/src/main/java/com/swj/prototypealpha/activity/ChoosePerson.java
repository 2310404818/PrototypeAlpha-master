package com.swj.prototypealpha.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.swj.prototypealpha.MyApplication;
import com.swj.prototypealpha.R;
import com.swj.prototypealpha.swj.util.searchView.ClearEditText;
import com.swj.prototypealpha.swj.util.searchView.PinyinComparator;
import com.swj.prototypealpha.swj.util.searchView.PinyinUtils;
import com.swj.prototypealpha.swj.util.searchView.SortAdapter;
import com.swj.prototypealpha.swj.util.searchView.SortModel;
import com.swj.prototypealpha.swj.util.searchView.TitleItemDecoration;
import com.swj.prototypealpha.swj.util.searchView.WaveSideBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 添加检查人的界面
 * 检查人列表
 * 完成类似于联系人列表的界面
 * @author ql
 * @date 2019/7
 */
public class ChoosePerson extends AppCompatActivity {
    private static final String TAG = "ChoosePerson";
    private RecyclerView mRecyclerView;
    private WaveSideBar mSideBar;
    private SortAdapter mAdapter;
    private ClearEditText mClearEditText;
    private LinearLayoutManager manager;
    private Toolbar tlb_person;
    private List<SortModel> mDateList;
    private List<String> mDate;
    private TitleItemDecoration mDecoration;
    MyApplication myApplication;



    /**
     * 根据拼音来排列RecyclerView里面的数据类
     */
    private PinyinComparator mComparator;


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_person);
        mDate =  new ArrayList<>();
        myJsonArray();
        initViews();
        initUI();
    }
    private void initUI()
    {
        tlb_person = findViewById(R.id.tlb_person);
        setSupportActionBar(tlb_person);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
    private void intiadapterUI(){

        mDateList = filledData(mDate);
        mComparator = new PinyinComparator();
        // 根据a-z进行排序源数据
        Collections.sort(mDateList, mComparator);
        //RecyclerView设置manager
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new SortAdapter(this, mDateList);
        mRecyclerView.setAdapter(mAdapter);
        //头部悬停
        mDecoration = new TitleItemDecoration(this, mDateList);
        //如果add两个，那么按照先后顺序，依次渲染。
        mRecyclerView.addItemDecoration(mDecoration);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(ChoosePerson.this, DividerItemDecoration.VERTICAL));
        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    private void initViews() {
        mSideBar = (WaveSideBar) findViewById(R.id.sideBar);
        //设置右侧SideBar触摸监听
        mSideBar.setOnTouchLetterChangeListener(new WaveSideBar.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                //该字母首次出现的位置
                int position = mAdapter.getPositionForSection(letter.charAt(0));
                if (position != -1) {
                    manager.scrollToPositionWithOffset(position, 0);
                }
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
    }


    /**
     * 为RecyclerView填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(List<String> date) {
        List<SortModel> mSortList = new ArrayList<>();
        for (int i = 0; i < date.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date.get(i));
            //汉字转换成拼音
            String pinyin = PinyinUtils.getPingYin(date.get(i));
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setLetters(sortString.toUpperCase());
            } else {
                sortModel.setLetters("#");
            }
            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新RecyclerView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<>();
            if (TextUtils.isEmpty(filterStr)) {
            filterDateList = filledData(mDate);
        } else {
            filterDateList.clear();
            for (SortModel sortModel : mDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1 ||
                        PinyinUtils.getFirstSpell(name).startsWith(filterStr.toString())
                        //不区分大小写
                        || PinyinUtils.getFirstSpell(name).toLowerCase().startsWith(filterStr.toString())
                        || PinyinUtils.getFirstSpell(name).toUpperCase().startsWith(filterStr.toString())
                        ) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, mComparator);
        mDateList.clear();
        mDateList.addAll(filterDateList);
        mAdapter.notifyDataSetChanged();
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
     * 服务器拉取数据
     */

    public void myJsonArray(){
        final List<String> myData =new ArrayList<>();
        RequestQueue requestQueue =Volley.newRequestQueue(getApplicationContext());
        myApplication = (MyApplication) getApplication();
        String name = myApplication.getTell();
        String url = "http://47.102.119.140:8080/mobile_inspection_war/Users?tell="+name;
        JsonArrayRequest jsonArrayRequest =new JsonArrayRequest(Request.Method.POST,url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject Project = (JSONObject) response.get(i);
                        String name = Project.getString("UserName");
                        myData.add(name);
                    }
                    mDate = myData;
                    intiadapterUI();
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG,"Error: "+error.getMessage());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

}
