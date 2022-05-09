package com.tallymarks.engroffm.adapters;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tallymarks.engroffm.fragments.Mentions;
import com.tallymarks.engroffm.fragments.MyDealers;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new MyDealers();
        } else if (position == 1) {
            fragment = new Mentions();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "MY DEALERS";
        } else if (position == 1) {
            title = "MENTIONS";
        }
        return title;
    }
}
