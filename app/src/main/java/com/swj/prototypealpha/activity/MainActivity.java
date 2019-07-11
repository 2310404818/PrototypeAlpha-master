package com.swj.prototypealpha.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.swj.prototypealpha.R;

public class MainActivity extends AppCompatActivity
{
    TextView tv_title;
    private BottomNavigationView navigation;
    private Fragment leftFragment;
    private Fragment rightFragment;

    private Fragment[] fragments;

    private int lastFrament;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    tv_title.setText("长沙智慧住建执法平台");

                    if(lastFrament != 0)
                    {
                        switchFragment(lastFrament,0);
                        lastFrament = 0;
                    }
                    return true;

                case R.id.navigation_notifications:
                    tv_title.setText("个人中心");
                    if(lastFrament != 1)
                    {
                        switchFragment(lastFrament,1);
                        lastFrament = 1;
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI()
    {
        tv_title = findViewById(R.id.mainTitle);
        leftFragment = new MainFragment_l();
        rightFragment = new MainFragment_R();
        fragments = new Fragment[]{leftFragment,rightFragment};
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fagement,leftFragment).show(leftFragment).commit();
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        lastFrament = 0;
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
}
