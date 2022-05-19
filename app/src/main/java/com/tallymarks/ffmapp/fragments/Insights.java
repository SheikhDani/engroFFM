package com.tallymarks.ffmapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.CommitmentHistoryAdapter;
import com.tallymarks.ffmapp.adapters.DealersAdapter;
import com.tallymarks.ffmapp.models.CommitmentHistory;
import com.tallymarks.ffmapp.models.DealInfo;

import java.util.ArrayList;
import java.util.List;

public class Insights extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recyclerView;
    private List<CommitmentHistory> planList = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public Insights() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Insights newInstance() {
        Insights fragment = new Insights();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dealers_insights, container, false);

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareMovieData();

       CommitmentHistoryAdapter adapter = new CommitmentHistoryAdapter(planList);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
    private void prepareMovieData() {
        CommitmentHistory plan = new CommitmentHistory();
        plan.setName("Engro");
        plan.setDate("29-Nov-2022");
        plan.setProduct("DAP");
        plan.setWeight("50kg | 100 Bags");
        plan.setPrice("Rs 2400 / Bag");
        planList.add(plan);
        CommitmentHistory plan2 = new CommitmentHistory();
        plan2.setName("Engro");
        plan2.setDate("30-Nov-2022");
        plan2.setProduct("Zarkezh");
        plan2.setWeight("30kg | 100 Bags");
        plan2.setPrice("Rs 2500 / Bag");
        planList.add(plan2);


        // notify adapter about data set changes
        // so that it will render the list with new data

    }
}
