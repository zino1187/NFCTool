package com.example.zino.nfctool;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/*
 * ViewPager 껍데기 이므로, 어댑터로 연결해야 한다
 */
public class MyPagerAdapter extends FragmentStatePagerAdapter{
    Fragment[] fragments=new Fragment[2];

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments[0] = new ReadFragment();
        fragments[1]=new WriteFragment();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

}
