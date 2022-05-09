package com.tallymarks.engroffm.fragments;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.engroffm.R;
import com.tallymarks.engroffm.adapters.DealersAdapter;
import com.tallymarks.engroffm.models.DealInfo;

import java.util.ArrayList;
import java.util.List;

public class Mentions extends Fragment {

    RecyclerView recyclerView;
    private List<DealInfo> planList = new ArrayList<>();

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

        prepareMovieData();

        DealersAdapter adapter = new DealersAdapter(planList);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
    private void prepareMovieData() {
        DealInfo plan = new DealInfo();
        plan.setTitle("Parvez Mughal");
        plan.setMemebrship("FOLLOW UP DUE");
        plan.setStatus("Last Update on 12 Sep");
        planList.add(plan);

        DealInfo plan2 = new DealInfo();
        plan2.setTitle("Sattar Bukhsh");
        plan2.setMemebrship("FOLLOW UP DUE");
        plan2.setStatus("Last Update on 21 Sep");
        planList.add(plan2);

        DealInfo plan3 = new DealInfo();
        plan3.setTitle("Sattar Bukhsh");
        plan3.setMemebrship("IN PROGRESS");
        plan3.setStatus("Last Update on 21 Sep");
        planList.add(plan3);



        DealInfo plan4 = new DealInfo();
        plan4.setTitle("Sattar Bukhsh");
        plan4.setMemebrship("PRODUCT SOLD");
        plan4.setStatus("Last Update on 21 Sep");
        planList.add(plan4);



        DealInfo plan5 = new DealInfo();
        plan5.setTitle("Sattar Bukhsh");
        plan5.setMemebrship("UPSELL");
        plan5.setStatus("Last Update on 21 Sep");
        planList.add(plan5);

        DealInfo plan6 = new DealInfo();
        plan6.setTitle("Sattar Bukhsh");
        plan6.setMemebrship("UPSELL");
        plan6.setStatus("Last Update on 21 Sep");
        planList.add(plan6);


        DealInfo plan7 = new DealInfo();
        plan7.setTitle("Sattar Bukhsh");
        plan7.setMemebrship("PRODUCT SOLD");
        plan7.setStatus("Last Update on 21 Sep");
        planList.add(plan7);

        DealInfo plan8 = new DealInfo();
        plan8.setTitle("Sattar Bukhsh");
        plan8.setMemebrship("PRODUCT SOLD");
        plan8.setStatus("Last Update on 21 Sep");
        planList.add(plan8);

        // notify adapter about data set changes
        // so that it will render the list with new data

    }
}