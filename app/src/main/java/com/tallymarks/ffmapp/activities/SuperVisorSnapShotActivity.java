package com.tallymarks.ffmapp.activities;

import static com.tallymarks.ffmapp.utils.Helpers.getDatefromMilis;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.CustomerSnapShotAdapter;
import com.tallymarks.ffmapp.adapters.SuperVisorSnapShotAdapter;
import com.tallymarks.ffmapp.adapters.TodayPlanAdapter;
import com.tallymarks.ffmapp.database.ExtraHelper;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.CustomerSnapShot;
import com.tallymarks.ffmapp.models.CustomerSnapShotParent;
import com.tallymarks.ffmapp.models.SuperVisorSnapShotChild;
import com.tallymarks.ffmapp.models.SupervisorSnapshotParent;
import com.tallymarks.ffmapp.models.TodayPlan;
import com.tallymarks.ffmapp.models.assignedcustomersofsubordiantes.GetAssignedCustomerSubOrdinatesOutput;
import com.tallymarks.ffmapp.models.assignedsalespoint.AssignedSalesPointOutput;
import com.tallymarks.ffmapp.models.snapshotforsupervisoroutput.PreviousSnapShotSupervisorOutput;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SuperVisorSnapShotActivity extends AppCompatActivity {
    private TextView tvTopHeader;

    ImageView iv_menu, iv_back;
    final ArrayList<SupervisorSnapshotParent> supervisorStock = new ArrayList<SupervisorSnapshotParent>();
    Button btnSumamry;
    SharedPrefferenceHelper sHelper;
    ExpandableListView elv;
    ExtraHelper extraHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_snapshot);
        initView();
    }

    private void initView() {
        tvTopHeader = findViewById(R.id.tv_dashboard);
        iv_menu = findViewById(R.id.iv_drawer);
        sHelper = new SharedPrefferenceHelper(SuperVisorSnapShotActivity.this);
        extraHelper = new ExtraHelper(SuperVisorSnapShotActivity.this);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        btnSumamry = findViewById(R.id.sales_summary);
        tvTopHeader.setTextSize(12);
        tvTopHeader.setText("Stock SnapShot for " + sHelper.getString(Constants.SUBORDINATE_CUSTOMER_NAME));
        btnSumamry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n = new Intent(SuperVisorSnapShotActivity.this, StockSellingSummaryActivity.class);
                startActivity(n);

            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
//                Intent i = new Intent(SuperVisorSnapShotActivity.this, SubordinatListActivity.class);
//                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        elv = (ExpandableListView) findViewById(R.id.expandableListView);

        if(Helpers.isNetworkAvailable(SuperVisorSnapShotActivity.this)) {
            new GetSnapshotforSubOrdiantes().execute();
        }
        else
        {
            Helpers.noConnectivityPopUp(SuperVisorSnapShotActivity.this);
        }

       // final ArrayList<SupervisorSnapshotParent> team = getData();


        //SET ONCLICK LISTENER
        elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPos,
                                        int childPos, long id) {
                SuperVisorSnapShotChild e = supervisorStock.get(groupPos).players.get(childPos);
                // Toast.makeText(SuperVisorSnapShotActivity.this, ""+e.getQuantity(), Toast.LENGTH_SHORT).show();

                // Toast.makeText(getApplicationContext(), (CharSequence) team.get(groupPos).players.get(childPos), Toast.LENGTH_SHORT).show();

                return false;
            }
        });
    }

    private ArrayList<SupervisorSnapshotParent> getData() {


        SupervisorSnapshotParent t1 = new SupervisorSnapshotParent("Brand Name: Urea");
        SuperVisorSnapShotChild e = new SuperVisorSnapShotChild();
        e.setName("Engro Urea");
        e.setQuantity("250");
        e.setVisitdate("June 19, 2021");
        t1.players.add(e);

        SuperVisorSnapShotChild e2 = new SuperVisorSnapShotChild();
        e2.setName("Sona Urea");
        e2.setVisitdate("220");
        e2.setVisitdate("June 12 , 2022");
        t1.players.add(e2);

        SupervisorSnapshotParent t2 = new SupervisorSnapshotParent("Brand Name: SSP");
        SuperVisorSnapShotChild e11 = new SuperVisorSnapShotChild();
        e11.setName("Engro Urea");
        e11.setQuantity("250");
        e11.setVisitdate("June 19, 2021");
        t2.players.add(e11);

        SuperVisorSnapShotChild e22 = new SuperVisorSnapShotChild();
        e22.setName("Sona Urea");
        e22.setVisitdate("220");
        e22.setVisitdate("June 12 , 2022");
        t2.players.add(e2);


        SupervisorSnapshotParent t3 = new SupervisorSnapshotParent("Brand Name:  DAP");
        SuperVisorSnapShotChild e33 = new SuperVisorSnapShotChild();
        e33.setName("Engro Urea");
        e33.setQuantity("250");
        e33.setVisitdate("June 19, 2021");
        t3.players.add(e33);

        SuperVisorSnapShotChild e34 = new SuperVisorSnapShotChild();
        e34.setName("Sona Urea");
        e34.setVisitdate("220");
        e34.setVisitdate("June 12 , 2022");
        t3.players.add(e34);

        ArrayList<SupervisorSnapshotParent> allTeams = new ArrayList<SupervisorSnapshotParent>();
        allTeams.add(t1);
        allTeams.add(t2);
        allTeams.add(t3);


        return allTeams;
    }

    public class GetSnapshotforSubOrdiantes extends AsyncTask<String, Void, Void> {
        ProgressDialog pDialog;
        private HttpHandler httpHandler;
        String errorMessage = "";
        String categoryname = "";
        String date="";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SuperVisorSnapShotActivity.this);
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
            String getsupervsorsnapshot = Constants.FFM_GET_PREVIOUS_SNAPSHOT_SUPERVISOR + "?customerId=" + sHelper.getString(Constants.SUBORDINATE_CUSTOMER_ID);
            System.out.println("OUtlet Status URL : " + getsupervsorsnapshot);
            try {
                httpHandler = new HttpHandler();
                HashMap<String, String> headerParams = new HashMap<>();
                if(sHelper.getString(Constants.ACCESS_TOKEN)!=null  && !sHelper.getString(Constants.ACCESS_TOKEN).equals("")) {
                    headerParams.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
                }
                else
                {
                    headerParams.put(Constants.AUTHORIZATION, "Bearer " + extraHelper.getString(Constants.ACCESS_TOKEN));
                }
               // headerParams.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
                response = httpHandler.httpGet(getsupervsorsnapshot, headerParams);
                Log.e("Assigned Sales Point", getsupervsorsnapshot);
                Log.e("Response", response);
                Type journeycodeType = new TypeToken<ArrayList<PreviousSnapShotSupervisorOutput>>() {
                }.getType();
                List<PreviousSnapShotSupervisorOutput> journeycode = new Gson().fromJson(response, journeycodeType);
                //JourneyPlanOutPut journeycode = new Gson().fromJson(response, JourneyPlanOutPut.class);
                if (response != null) {
                    for (int j = 0; j < journeycode.size(); j++) {
                        categoryname = journeycode.get(j).getCategory() == null || journeycode.get(j).getCategory().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getCategory().toString();
                        SupervisorSnapshotParent t1 = new SupervisorSnapshotParent(categoryname);
                        for(int i=0 ;i< journeycode.get(j).getPreviousStock().size();i++) {
                            SuperVisorSnapShotChild child = new SuperVisorSnapShotChild();
                            child.setName(journeycode.get(j).getPreviousStock().get(i).getName() == null || journeycode.get(j).getPreviousStock().get(i).getName() .equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getPreviousStock().get(i).getName() .toString());
                            child.setQuantity(journeycode.get(j).getPreviousStock().get(i).getQuantity() == null || journeycode.get(j).getPreviousStock().get(i).getQuantity().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getPreviousStock().get(i).getQuantity().toString());
                            date  = journeycode.get(j).getPreviousStock().get(i).getVisitDate() == null || journeycode.get(j).getPreviousStock().get(i).getVisitDate().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getPreviousStock().get(i).getVisitDate().toString();
                            child.setVisitdate(getDatefromMilis(date));
                           t1.players.add(child);
                        }
                        supervisorStock.add(t1);

                    }
                }
            } catch (Exception exception) {
                if (response.equals("")) {
                    Helpers.displayMessage(SuperVisorSnapShotActivity.this, true, exception.getMessage());
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
            SuperVisorSnapShotAdapter adapter = new SuperVisorSnapShotAdapter(SuperVisorSnapShotActivity.this,supervisorStock );
            elv.setAdapter(adapter);


        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
