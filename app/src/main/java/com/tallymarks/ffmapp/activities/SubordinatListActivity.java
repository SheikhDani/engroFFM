package com.tallymarks.ffmapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.SubOrdinatesAdapter;
import com.tallymarks.ffmapp.adapters.TodayPlanAdapter;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.Subordinates;
import com.tallymarks.ffmapp.models.TodayPlan;
import com.tallymarks.ffmapp.models.assignedcustomersofsubordiantes.GetAssignedCustomerSubOrdinatesOutput;
import com.tallymarks.ffmapp.models.assignedsalespoint.AssignedSalesPointOutput;
import com.tallymarks.ffmapp.models.supervisorsnapshotoutput.SuperVisorSnapShotOutput;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.HttpHandler;
import com.tallymarks.ffmapp.utils.ItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SubordinatListActivity extends AppCompatActivity implements ItemClickListener {
    private TextView tvTopHeader;
    ImageView iv_menu,iv_back;
    private List<TodayPlan> planList = new ArrayList<>();
    private RecyclerView recyclerView;
    SharedPrefferenceHelper sHelper;
    EditText et_Search;
    TodayPlanAdapter adapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subordinates_list);
        initView();

    }
    private void initView()
    {
        tvTopHeader = findViewById(R.id.tv_dashboard);
        iv_menu = findViewById(R.id.iv_drawer);
        et_Search = findViewById(R.id.et_Search);
        sHelper = new SharedPrefferenceHelper(SubordinatListActivity.this);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
//                Intent i = new Intent(SubordinatListActivity.this, SubOrdinatesActivity.class);
//                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        et_Search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if(et_Search.hasFocus()) {
                    adapter.filter(cs.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // Toast.makeText(getApplicationContext(),"before text change",Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                //Toast.makeText(getApplicationContext(),"after text change",Toast.LENGTH_LONG).show();
            }
        });

        tvTopHeader.setText(sHelper.getString(Constants.SUBORDINATE_NAME)+ " Customers");
        // prepareMovieData();
        if(Helpers.isNetworkAvailable(SubordinatListActivity.this)) {
            new GetAssignedCustomerforSubOrdiantes().execute();
        }
        else
        {
            Helpers.noConnectivityPopUp(SubordinatListActivity.this);
        }


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SubordinatListActivity.this);
        recyclerView.setLayoutManager(layoutManager);

    }
    private void prepareMovieData() {

        com.tallymarks.ffmapp.models.TodayPlan plan = new com.tallymarks.ffmapp.models.TodayPlan();
        plan.setSalespoint("Sales Point: Kot Ghulam Muhammad");
        plan.setTitle("Dr. Athar Rasheed");
        plan.setLocation("Location Code: Lahore");
        plan.setCustomercode("Customer Code: 0030120301230");

        planList.add(plan);

        com.tallymarks.ffmapp.models.TodayPlan plan2 = new com.tallymarks.ffmapp.models.TodayPlan();
        plan2.setSalespoint("Sales Point: Kot Ghulam Muhammad");
        plan2.setTitle("Dr. Athar Rasheed");

        plan2.setLocation("Location Code: Lahore");
        plan2.setCustomercode("Customer Code: 0030120301230");
        planList.add(plan2);

        com.tallymarks.ffmapp.models.TodayPlan plan3 = new com.tallymarks.ffmapp.models.TodayPlan();
        plan3.setSalespoint("Sales Point: Kot Ghulam Muhammad");
        plan3.setTitle("Dr. Athar Rasheed");
        plan3.setLocation("Location Code: Lahore");
        plan3.setCustomercode("Customer Code: 0030120301230");
        planList.add(plan3);

        com.tallymarks.ffmapp.models.TodayPlan plan4 = new com.tallymarks.ffmapp.models.TodayPlan();
        plan4.setSalespoint("Sales Point: Kot Ghulam Muhammad");
        plan4.setTitle("Dr. Athar Rasheed");
        plan4.setLocation("Location Code: Lahore");
        plan4.setCustomercode("Customer Code: 0030120301230");
        planList.add(plan4);


        // notify adapter about data set changes
        // so that it will render the list with new data

    }

    @Override
    public void onClick(View view, int position) {
        final TodayPlan city = planList.get(position);
        // Toast.makeText(getContext(), "" + planList.get(position).getTitle(), Toast.LENGTH_SHORT).show();

            Intent i = new Intent(SubordinatListActivity.this, SuperVisorSnapShotActivity.class);
             sHelper.setString(Constants.SUBORDINATE_CUSTOMER_NAME,city.getTitle());
             sHelper.setString(Constants.SUBORDINATE_CUSTOMER_ID,city.getCustomerID());
            startActivity(i);

    }
    public class GetAssignedCustomerforSubOrdiantes extends AsyncTask<String, Void, Void> {
        ProgressDialog pDialog;
        private HttpHandler httpHandler;
        String errorMessage = "";



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SubordinatListActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.show();
            pDialog.setCancelable(false);

            //expandableListGroup.clear();

        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected Void doInBackground(String... Url) {
            String response = "";
            String getsupervsorsnapshot = Constants.FFM_GET_ASSIGNED_CUSTOMER_SUPERVISOR + "?subordinateId=" + sHelper.getString(Constants.SUBORDINATE_ID) ;
            System.out.println("OUtlet Status URL : " + getsupervsorsnapshot);
            try {
                httpHandler = new HttpHandler();
                HashMap<String, String> headerParams = new HashMap<>();
                headerParams.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
                response = httpHandler.httpGet(getsupervsorsnapshot, headerParams);
                Log.e("Assigned Sales Point", getsupervsorsnapshot);
                Log.e("Response", response);
                Type journeycodeType = new TypeToken<ArrayList<GetAssignedCustomerSubOrdinatesOutput>>() {
                }.getType();
                List<GetAssignedCustomerSubOrdinatesOutput> journeycode = new Gson().fromJson(response, journeycodeType);
                //JourneyPlanOutPut journeycode = new Gson().fromJson(response, JourneyPlanOutPut.class);
                if (response != null) {

                    for (int j = 0; j < journeycode.size(); j++) {
                        TodayPlan plan4 = new TodayPlan();
                        plan4.setSalespoint(journeycode.get(j).getSalesPoint() == null || journeycode.get(j).getSalesPoint() .equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getSalesPoint() .toString());
                        plan4.setTitle(journeycode.get(j).getCustomerName() == null || journeycode.get(j).getCustomerName().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getCustomerName().toString());
                        plan4.setLocation(journeycode.get(j).getLocationCode() == null || journeycode.get(j).getLocationCode().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getLocationCode().toString());
                        plan4.setCustomercode(journeycode.get(j).getCustomerCode() == null || journeycode.get(j).getCustomerCode().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getCustomerCode().toString());
                        plan4.setCustomerID(journeycode.get(j).getId() == null || journeycode.get(j).getId().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getId().toString());
                        planList.add(plan4);

                    }
                }
            } catch (Exception exception) {
                if (response.equals("")) {
                    Helpers.displayMessage(SubordinatListActivity.this, true, exception.getMessage());
                    //showResponseDialog( mContext.getResources().getString(R.string.alert),exception.getMessage());
                    //pDialog.dismiss();
                } else {
                    JSONObject json = null;
                    try {
                        json = new JSONObject(response);
                        errorMessage = json.getString("message");
                        String status = json.getString("success");
                        if (status.equals("false")) {
                            // Helpers.displayMessage(JourneyPlanActivity.this, true, errorMessage);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                    //Helpers.displayMessage(LoginActivity.this, true, exception.getMessage());
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            pDialog.dismiss();
            adapter = new TodayPlanAdapter(planList, "subordinate");
            recyclerView.setAdapter(adapter);
            adapter.setClickListener(SubordinatListActivity.this);

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
