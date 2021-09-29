package com.tallymarks.ffmapp.adapters;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tallymarks.ffmapp.fragments.FuturePlan;
import com.tallymarks.ffmapp.fragments.PastPlan;
import com.tallymarks.ffmapp.fragments.TodaysPlan;

public class VisitCustomerViewPagerAdapter extends FragmentPagerAdapter {
    String activity;

    public VisitCustomerViewPagerAdapter(FragmentManager fm, String activity) {
        super(fm);
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = TodaysPlan.newInstance(activity);
        } else if (position == 1) {
            fragment = new FuturePlan();
        } else if (position == 2) {
           fragment = new PastPlan();
        }


        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "TODAY'S PLAN";
        } else if (position == 1) {
            title = "FUTURE PLAN";
        } else if (position == 2) {
            title = "PAST PLANS";
        }
        return title;
    }

}
