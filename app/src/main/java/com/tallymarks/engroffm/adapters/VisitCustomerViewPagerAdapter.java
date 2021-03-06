package com.tallymarks.engroffm.adapters;



import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tallymarks.engroffm.fragments.AllPlans;
import com.tallymarks.engroffm.fragments.FuturePlan;
import com.tallymarks.engroffm.fragments.PastPlan;
import com.tallymarks.engroffm.fragments.TodaysPlan;

public class VisitCustomerViewPagerAdapter extends FragmentPagerAdapter {
    String activity;
    EditText et_search;



    public VisitCustomerViewPagerAdapter(FragmentManager fm, String activity, EditText et) {
        super(fm);
        this.activity = activity;
        this.et_search = et;


    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = TodaysPlan.newInstance(activity,et_search);

        } else if (position == 1) {
            fragment =  AllPlans.newInstance(activity,et_search);

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
