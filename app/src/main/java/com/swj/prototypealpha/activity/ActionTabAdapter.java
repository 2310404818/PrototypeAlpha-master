package com.swj.prototypealpha.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * <pre>
 *     author : marin
 *     time   : 2019/04/06
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ActionTabAdapter extends FragmentPagerAdapter {
    private FragmentManager mFragmentManager;
    private List<Fragment>  mList;

    public ActionTabAdapter(FragmentManager fm, List<Fragment> list){
        super(fm);
        this.mFragmentManager=fm;
        this.mList = list;
    }


    @Override
    public Fragment getItem (int i) {
        return mList.get(i);
    }

    @Override
    public int getCount () {
        return mList.size();
    }
}
