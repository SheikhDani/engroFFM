package com.tallymarks.ffmapp.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.TodayPlan;
import com.tallymarks.ffmapp.utils.DialougeManager;
import com.tallymarks.ffmapp.utils.GpsTracker;

import java.util.ArrayList;
import java.util.List;

public class AllPlans extends Fragment {
    private List<TodayPlan> planList = new ArrayList<>();
    private RecyclerView recyclerView;
    private static final String ARG_TEXT = "activityname";
    private String activity;
    DatabaseHandler db;
    GpsTracker gps;
    String currentlat;
    String  currentlng;
    SharedPrefferenceHelper sHelper;


    public static AllPlans newInstance(String activity) {
        AllPlans fragment = new AllPlans();
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
        db = new DatabaseHandler(getActivity());
        sHelper = new SharedPrefferenceHelper(getActivity());
        planList.clear();
        gps = new GpsTracker(getActivity());
        if (gps.canGetLocation()) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            currentlat = String.valueOf(gps.getLatitude());
            currentlng = String.valueOf(gps.getLongitude());
        }
        else
        {
            DialougeManager.gpsNotEnabledPopup(getActivity());
        }
        if(activity.equals("customers")) {
           // getTodayCustomerJourneyPlan();
        }
        else
        {
           // prepareMovieData(activity);
        }


    }

}
