package com.tallymarks.engroffm.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tallymarks.engroffm.fragments.Dealers;
import com.tallymarks.engroffm.fragments.Farmers;

public class SalesPlanViewPagerAdapter extends FragmentPagerAdapter {
    public SalesPlanViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new Dealers();

        } else if (position == 1) {
            fragment = new Farmers();

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
            title = "DEALER";

        } else if (position == 1) {
            title = "FARMER";
        }
        return title;
    }
}
