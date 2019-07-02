package com.swj.prototypealpha.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import com.swj.prototypealpha.R;

import java.util.ArrayList;

public class PolicyLableActivity extends AppCompatActivity {
    private Toolbar             mToolbar;
    private ListView            mListlView;
    private ArrayList<String[]> policies            = new ArrayList<>();
    private ArrayList<String[]> old_filter_policies = new ArrayList<>();
    private SearchView          mSearchView;
    private MyAdapter           myAdapter;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_lable);
        mToolbar = findViewById(R.id.toolbarPolicy);
        mListlView = findViewById(R.id.lv_policy);
        mSearchView = findViewById(R.id.sv_notice);
        initUI();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    private void initUI () {
        mToolbar.inflateMenu(R.menu.toolbar_menu);

        myAdapter = new MyAdapter();

        mListlView.setAdapter(myAdapter);
        mListlView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PolicyLableActivity.this, PolicyinfoActivity.class);
                startActivity(intent);
            }
        });
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.setIconified(false);
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit (String s) {
                if (mSearchView != null) {
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
                    }
                    mSearchView.clearFocus();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange (String newText) {
                if (TextUtils.isEmpty(newText)) {
                    myAdapter.getFilter().filter("");
                } else {
                    myAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
        mSearchView.setSubmitButtonEnabled(false);
        mSearchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mSearchView.setQueryHint("搜一哈？");
        mListlView.setTextFilterEnabled(true);
    }

    private void initData () {
        policies.add(new String[]{"中华人民共和国水法", "2016-07-02"});
        policies.add(new String[]{"中华人民共和国行政强制法", "2011-06-30"});
        policies.add(new String[]{"中华人民共和国城乡规划法", "2007-10-28"});
        policies.add(new String[]{"中华人民共和国物权法", "2007-03-16"});
        old_filter_policies = policies;
    }

    private class MyAdapter extends BaseAdapter implements Filterable {
        private MyFilter mFilter;

        @Override
        public int getCount () {
            return old_filter_policies.size();
        }

        @Override
        public Object getItem (int position) {
            return old_filter_policies.get(position);
        }

        @Override
        public long getItemId (int position) {
            return 0;
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
                view = inflater.inflate(R.layout.item_policy, null);
            } else {
                view = convertView;
            }
            String[] m = old_filter_policies.get(position);
            TextView policy_name = view.findViewById(R.id.policy_name);
            policy_name.setText(m[0]);
            policy_name.setTextSize(15);

            TextView policy_time = (TextView) view.findViewById(R.id.policy_time);
            policy_time.setText(m[1]);
            policy_time.setTextSize(15);

            return view;
        }

        @Override
        public Filter getFilter () {
            if (null == mFilter) {
                mFilter = new MyFilter();
            }
            return mFilter;
        }

        class MyFilter extends Filter {

            @Override
            protected FilterResults performFiltering (CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<String[]> newValues = new ArrayList<>();
                String filterString = constraint.toString().trim();

                if (TextUtils.isEmpty(filterString)) {
                    newValues = policies;
                } else {
                    for (String[] str : policies) {
                        if (str[0].contains(filterString)) {
                            newValues.add(str);
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();

                return results;
            }

            @Override
            protected void publishResults (CharSequence constraint, FilterResults results) {
                old_filter_policies = (ArrayList<String[]>) results.values;

                if (results.count > 0) {
                    myAdapter.notifyDataSetChanged();
                } else {
                    myAdapter.notifyDataSetInvalidated();
                }
            }
        }
    }
}
