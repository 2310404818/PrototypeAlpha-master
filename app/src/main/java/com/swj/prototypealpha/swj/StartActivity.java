package com.swj.prototypealpha.swj;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.swj.prototypealpha.R;

/**
 * 发起检查界面
 * 为项目信息、照片、笔录、审阅fragment提供activity挂靠
 */
public class StartActivity extends AppCompatActivity {
    TextView tv_title;

    private BottomNavigationView navigation;
    private Fragment proFragment;
    private Fragment pictFragment;
    private Fragment lookupFragment;
    private Fragment wordFragment;
    private android.support.v7.widget.Toolbar toolbar;
    private Fragment[] fragments;
    private String ProjectName,address;
    private int lastFrament;


    public void setToolBar()
    {
        toolbar = findViewById(R.id.toolbar_start);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initUI()
    {
        tv_title = findViewById(R.id.mainTitle_start);
        proFragment = new ProjFragment();
        pictFragment = new PictureFragment();
        lookupFragment = new LookupFragment();
        wordFragment = new WordFragment();
        fragments = new Fragment[]{proFragment,pictFragment,wordFragment,lookupFragment};
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fagement,proFragment).show(proFragment).commit();
        navigation = findViewById(R.id.navigation_start);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        lastFrament = 0;
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.project_info:
                    tv_title.setText("项目信息");

                    if(lastFrament != 0)
                    {
                        switchFragment(lastFrament,0);
                        lastFrament = 0;
                    }
                    return true;

                case R.id.picture_info:
                    tv_title.setText("照片");
                    if(lastFrament != 1)
                    {
                        switchFragment(lastFrament,1);
                        lastFrament = 1;
                    }
                    return true;
                case R.id.word_info:
                    tv_title.setText("笔录");
                    if(lastFrament != 2)
                    {
                        switchFragment(lastFrament,2);
                        lastFrament = 2;
                    }
                    return true;
                case R.id.lookup:
                    tv_title.setText("审阅");
                    if(lastFrament != 3)
                    {
                        switchFragment(lastFrament,3);
                        lastFrament = 3;
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Intent intent =getIntent();
        ProjectName = intent.getStringExtra("title");
        address =intent.getStringExtra("address");
        initUI();
        setToolBar();
    }

    private void switchFragment(int lastfragment,int index)
    {
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastfragment]);//隐藏上个Fragment

        if(!fragments[index].isAdded())
        {
            transaction.add(R.id.main_fagement,fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
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
