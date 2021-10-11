package com.tallymarks.ffmapp.adapters;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tallymarks.ffmapp.fragments.AllPlans;
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
            fragment =  AllPlans.newInstance(activity);
        } else if (position == 2) {
           fragment = new PastPlan();
        }
        else if (position == 3) {
            fragment = new FuturePlan();
        }


        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "TODAY PLANS";

        } else if (position == 1) {
            title = "ALL PLANS";
        } else if (position == 2) {
            title = "PAST PLANS";
        }
        else if (position == 3) {
            title = "FUTURE PLANS";
        }
        return title;
    }

}
