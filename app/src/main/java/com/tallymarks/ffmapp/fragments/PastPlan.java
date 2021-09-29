package com.tallymarks.ffmapp.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.models.PlanPast;

import java.util.ArrayList;
import java.util.List;

public class PastPlan extends Fragment {
    private List<PlanPast> planList = new ArrayList<>();
    private RecyclerView recyclerView;


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
//        prepareMovieData();
//        PastPlanAdapter adapter = new PastPlanAdapter(planList);
//        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);


    }
    private void prepareMovieData() {
        PlanPast plan = new  PlanPast ();
        plan.setApprovalstatus("Pending Approval");
        plan.setDealertime("7 Dealers 6 hours duration");
        plan.setPlandate("April 12, 2021");
        planList.add(plan);

        PlanPast  plan2 = new  PlanPast ();
        plan2.setApprovalstatus("Completed with Deviation");
        plan2.setDealertime("7 Dealers 6 hours duration");
        plan2.setPlandate("April 12, 2021");
        planList.add(plan2);

        PlanPast  plan3 = new  PlanPast ();
        plan3.setApprovalstatus("Pending Approval");
        plan3.setDealertime("7 Dealers 6 hours duration");
        plan3.setPlandate("April 12, 2021");
        planList.add(plan3);

        PlanPast  plan4 = new  PlanPast ();
        plan4.setApprovalstatus("Pending Approval");
        plan4.setDealertime("7 Dealers 6 hours duration");
        plan4.setPlandate("April 12, 2021");
        planList.add(plan4);




        // notify adapter about data set changes
        // so that it will render the list with new data

    }
}
