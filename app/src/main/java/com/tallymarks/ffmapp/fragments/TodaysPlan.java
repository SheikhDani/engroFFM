package com.tallymarks.ffmapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.activities.FarmersStartActivity;
import com.tallymarks.ffmapp.activities.StartActivity;
import com.tallymarks.ffmapp.adapters.DealersAdapter;
import com.tallymarks.ffmapp.adapters.FarmersAdapter;
import com.tallymarks.ffmapp.adapters.TodayPlanAdapter;
import com.tallymarks.ffmapp.models.Farmes;
import com.tallymarks.ffmapp.models.TodayPlan;
import com.tallymarks.ffmapp.utils.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class TodaysPlan extends Fragment implements ItemClickListener {

    private List<TodayPlan> planList = new ArrayList<>();
    private RecyclerView recyclerView;
    private static final String ARG_TEXT = "activityname";
    private String activity;

    public static TodaysPlan newInstance(String activity) {
        TodaysPlan fragment = new TodaysPlan();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, activity);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            activity = getArguments().getString(ARG_TEXT);

        }
        prepareMovieData(activity);
        TodayPlanAdapter adapter = new TodayPlanAdapter(planList, activity);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);


    }

    private void prepareMovieData(String activity) {
        if (activity.equals("customers")) {
            com.tallymarks.ffmapp.models.TodayPlan plan = new com.tallymarks.ffmapp.models.TodayPlan();
            plan.setCustomercode("Customer Code: 8134560");
            plan.setMemebrship("Visited");
            plan.setSalespoint("Sales Point: Kot Ghulam Muhammad");
            plan.setTime("9:00 AM");
            plan.setTitle("Aqib Ali");
            planList.add(plan);

            com.tallymarks.ffmapp.models.TodayPlan plan2 = new com.tallymarks.ffmapp.models.TodayPlan();
            plan2.setCustomercode("Customer Code: 8134560");
            plan2.setMemebrship("Not Visited");
            plan2.setSalespoint("Sales Point: Kot Ghulam Muhammad");
            plan2.setTime("9:00 AM");
            plan2.setTitle("786 Fertilizer");
            planList.add(plan2);

            com.tallymarks.ffmapp.models.TodayPlan plan3 = new com.tallymarks.ffmapp.models.TodayPlan();
            plan3.setCustomercode("Customer code: 8134560");
            plan3.setMemebrship("Not Visited");
            plan3.setSalespoint("Sales Point: Kot Ghulam Muhammad");
            plan3.setTime("9:00 AM");
            plan3.setTitle("Aamir Behzad");
            planList.add(plan3);

            com.tallymarks.ffmapp.models.TodayPlan plan4 = new com.tallymarks.ffmapp.models.TodayPlan();
            plan4.setCustomercode("Customer Code: 8134560");
            plan4.setMemebrship("Visited");
            plan4.setSalespoint("Sales Point: Kot Ghulam Muhammad");
            plan4.setTime("9:00 AM");
            plan4.setTitle("Aamir Shafiq and Brothers");
            planList.add(plan4);

            com.tallymarks.ffmapp.models.TodayPlan plan5 = new com.tallymarks.ffmapp.models.TodayPlan();
            plan5.setCustomercode("Customer Code: 8134560");
            plan5.setMemebrship("Not Visited");
            plan5.setSalespoint("Sales Point: Kot Ghulam Muhammad");
            plan5.setTime("9:00 AM");
            plan5.setTitle("illahi Bukhsh");
            planList.add(plan5);

            com.tallymarks.ffmapp.models.TodayPlan plan6 = new com.tallymarks.ffmapp.models.TodayPlan();
            plan6.setCustomercode("Customer Code: 8134560");
            plan6.setMemebrship("Visited");
            plan6.setSalespoint("Sales Point: Kot Ghulam Muhammad");
            plan6.setTime("9:00 AM");
            plan6.setTitle("Abid Hussain &  Brothers");
            planList.add(plan6);

            com.tallymarks.ffmapp.models.TodayPlan plan7 = new com.tallymarks.ffmapp.models.TodayPlan();
            plan7.setCustomercode("Customer Code: 8134560");
            plan7.setMemebrship("Not Visited");
            plan7.setSalespoint("Sales Point: Kot Ghulam Muhammad");
            plan7.setTime("9:00 AM");
            plan7.setTitle("Abid Hussain &  Brothers");
            planList.add(plan7);
            com.tallymarks.ffmapp.models.TodayPlan plan8 = new com.tallymarks.ffmapp.models.TodayPlan();
            plan8.setCustomercode("Customer Code: 8134560");
            plan8.setMemebrship("Visited");
            plan8.setSalespoint("Sales Point: Kot Ghulam Muhammad");
            plan8.setTime("9:00 AM");
            plan8.setTitle("Abid Hussain &  Brothers");
            planList.add(plan8);
        } else {

            com.tallymarks.ffmapp.models.TodayPlan plan = new com.tallymarks.ffmapp.models.TodayPlan();
            plan.setSalespoint("Sales Point: Kot Ghulam Muhammad");
            plan.setTitle("Dr. Athar Rasheed");
            plan.setLocation("Lahore");
            plan.setMemebrship("Visited");
            plan.setMobilenumber("023123421312");
            planList.add(plan);

            com.tallymarks.ffmapp.models.TodayPlan plan2 = new com.tallymarks.ffmapp.models.TodayPlan();
            plan2.setSalespoint("Sales Point: Kot Ghulam Muhammad");
            plan2.setTitle("Dr. Athar Rasheed");
            plan2.setMemebrship("Visited");
            plan2.setLocation("Lahore");
            plan2.setMobilenumber("023123421312");
            planList.add(plan2);

            com.tallymarks.ffmapp.models.TodayPlan plan3 = new com.tallymarks.ffmapp.models.TodayPlan();
            plan3.setSalespoint("Sales Point: Kot Ghulam Muhammad");
            plan3.setTitle("Dr. Athar Rasheed");
            plan3.setLocation("Lahore");
            plan3.setMemebrship("Visited");
            plan3.setMobilenumber("023123421312");
            planList.add(plan3);

            com.tallymarks.ffmapp.models.TodayPlan plan4 = new com.tallymarks.ffmapp.models.TodayPlan();
            plan4.setSalespoint("Sales Point: Kot Ghulam Muhammad");
            plan4.setTitle("Dr. Athar Rasheed");
            plan4.setLocation("Lahore");
            plan4.setMemebrship("Visited");
            plan4.setMobilenumber("023123421312");
            planList.add(plan4);


        }

        // notify adapter about data set changes
        // so that it will render the list with new data

    }

    @Override
    public void onClick(View view, int position) {
        final TodayPlan city = planList.get(position);
       // Toast.makeText(getContext(), "" + planList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
        if(activity.equals("customers")) {
            Intent i = new Intent(getActivity(), StartActivity.class);
            startActivity(i);
        }
        else
        {
            Intent i = new Intent(getActivity(), FarmersStartActivity.class);
            startActivity(i);
        }
    }
}
