package com.swj.prototypealpha.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import com.swj.prototypealpha.Enity.NoticeEntity;
import com.swj.prototypealpha.R;
import com.swj.prototypealpha.swj.util.RecyclerViewHelper.NoticeAdapter;

import java.util.ArrayList;
import java.util.List;

public class NoticeListActivity extends AppCompatActivity {
    SearchView searchView;
    List<NoticeEntity> mData;
    List<NoticeEntity> old_Data;
    MyAdapter mAdapter;

    private static final String TAG = NoticeListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);
        ListView mRecyclerView = findViewById(R.id.notice_list);
        mData = generateData(2);
        old_Data =mData;
        mAdapter = new MyAdapter();
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        searchView = findViewById(R.id.sv_notice);
        searchView.setQueryHint("查询通知");
        searchView.setIconified(true);
        mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                NoticeEntity noticeEntity = old_Data.get(position);
                Intent intent = new Intent(NoticeListActivity.this, NoticeInfoActivity.class);
                intent.putExtra("noticeinfo",noticeEntity);
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
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (searchView != null) {
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                    }
                    searchView.clearFocus();
                }
                return true;

            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)) {
                    mAdapter.getFilter().filter("");
                } else {
                    mAdapter.getFilter().filter(s);
                }
                return true;
            }
        });





    }

    private class MyAdapter extends BaseAdapter implements Filterable{
        private MyFilter mFilter;

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
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice,parent,false);
//            View view = null;
//            if (convertView == null) {
//                LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
//                view = inflater.inflate(R.layout.item_policy, null);
//            } else {
//                view = convertView;
//            }

            NoticeEntity mnoticeEntity = old_Data.get(position);
            TextView text  = view.findViewById(R.id.tv_content);
            text.setText(mnoticeEntity.getFromer());
            TextView title = view.findViewById(R.id.tv_titlenotice);
            title.setText(mnoticeEntity.getTitle());
            TextView time = view.findViewById(R.id.textView_time_notice);
            time.setText(mnoticeEntity.getTime());
            return view;
        }

        @Override
        public Filter getFilter() {
            if (null == mFilter) {
                mFilter = new MyFilter();
            }
            return mFilter;
        }
        public  class MyFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<NoticeEntity> newValues = new ArrayList<>();
                String filterString = constraint.toString().trim();

                if (TextUtils.isEmpty(filterString)) {
                    newValues = mData;
                } else {
                    for (NoticeEntity str : mData) {
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
                old_Data = (ArrayList<NoticeEntity>) results.values;
                if (results.count > 0) {
                    mAdapter.notifyDataSetChanged();
                } else {
                    mAdapter.notifyDataSetInvalidated();


                }
            }
        }
    }



    public List<NoticeEntity> generateData(int size) {
            ArrayList<NoticeEntity> noticeEntities = new ArrayList<NoticeEntity>();
            noticeEntities.add(new NoticeEntity("关于公布全市房产新政咨询电话的公告", "2017-10-16 00:00", "发布人：市住建委", "稿件来源：市住建委", "  为方便广大市民咨询有关房地产市场管理政策，现将全市房产新政咨询电话公告如下：\n" +
                    "\n" +
                    "一、市房产交易管理中心信息科咨询电话：84662152。地址：芙蓉区马王堆中路248号。\n" +
                    "\n" +
                    "二、驻房产交易大楼政务窗口房产新政咨询电话：84662717。地址：芙蓉区马王堆中路248号。\n" +
                    "\n" +
                    "三、驻市不动产登记中心大楼政务窗口新政咨询电话：84529160。地址：芙蓉区晚报大道150号。\n" +
                    "\n" +
                    "四、驻河西市政务中心窗口房产新政咨询电话：88665171。地址：岳麓区岳麓大道218号市政府二办政务大厅\n"));
            noticeEntities.add(new NoticeEntity("住房城乡建设领域涉黑涉恶线索举报平台", "2019-03-25 16:32", "发布人：市住建委 ", "稿件来源：市住建局信访处", "一、举报须知。\n" +
                    "\n" +
                    "    为充分保护举报人的合法权益，并最大限度地发挥网站举报栏目的效能和作用，请您在提交举报信息前仔细阅读以下须知：\n" +
                    "\n" +
                    "    （一）温馨提示：\n" +
                    "    1.本网站只接受住房城乡建设领域涉黑涉恶线索举报。\n" +
                    "    2.为方便及时开展核查工作并及时反馈结果，请如实填写内容并留下必要的联系方式，网站将严格保密举报相关信息。对非实名或无实质性内容的信息，网站工作人员有权将其作为无效信息处理。\n" +
                    "    3.为提高信息处理质量和效率，请不要重复提交相同内容的信息。\n" +
                    "    4.如需提供照片等附件资料，请作为附件上传。\n" +
                    "\n" +
                    "    （二）举报主要内容：\n" +
                    "    1.黑恶势力恶意竞标、强揽工程，在项目建设过程中煽动闹事，在施工渣土运输中阻拦施工、强揽生意等问题；\n" +
                    "    2.房屋建筑建设过程中的涉黑涉恶问题；房地产中介公司采取威胁恐吓等暴力手段驱逐租户、强制上涨或恶意克扣租金押金，物业公司勾结社会闲杂人员滋扰业主，以暴力手段阻碍物业项目正常交接等问题；\n" +
                    "    3.骗取农村危房改造补助资格和补助资金，诱骗、逼迫农户上缴回扣；\n" +
                    "    4.实施违法建设，以暴力手段阻碍行政执法，为违法建设等充当“保护伞”等群众反映强烈、深恶痛绝的涉黑涉恶问题。\n" +
                    "\n" +
                    "    二、举报方式\n" +
                    "\n" +
                    "    举报邮箱：cxszjwxfc@163.com\n" +
                    "\n" +
                    "    举报电话：0731—88665923\n" +
                    "\n" +
                    "    工作时间：周一至周五 上午9：00―12：00     下午1：00―5：00\n" +
                    "\n" +
                    "    通讯地址：长沙市岳麓大道218号市政府二办1040办公室\n" +
                    "\n" +
                    "    邮政编码：410013\n"));
            return noticeEntities;

        }

}
