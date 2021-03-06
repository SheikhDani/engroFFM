package com.tallymarks.engroffm.activities;

import android.app.ProgressDialog;
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
import com.tallymarks.engroffm.R;
import com.tallymarks.engroffm.adapters.LocationApprovalAdapter;
import com.tallymarks.engroffm.database.ExtraHelper;
import com.tallymarks.engroffm.database.SharedPrefferenceHelper;
import com.tallymarks.engroffm.models.ChangeLocation;
import com.tallymarks.engroffm.models.changecustomerlocationoutput.ChangeCustomerLocationOutput;
import com.tallymarks.engroffm.utils.Constants;
import com.tallymarks.engroffm.utils.Helpers;
import com.tallymarks.engroffm.utils.HttpHandler;
import com.tallymarks.engroffm.utils.ItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SupervisorLocationApprovalListActivity extends AppCompatActivity implements ItemClickListener {
    private TextView tvTopHeader;
    ImageView iv_menu,iv_back;
    private List<ChangeLocation> planList = new ArrayList<>();
    private RecyclerView recyclerView;
    SharedPrefferenceHelper sHelper;
    ExtraHelper extraHelper;
    EditText et_Search;
    LocationApprovalAdapter adapter;
    static {
        System.loadLibrary("native-lib");
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_approval_list);
        initView();

    }
    private void initView()
    {
        tvTopHeader = findViewById(R.id.tv_dashboard);
        iv_menu = findViewById(R.id.iv_drawer);
        et_Search = findViewById(R.id.et_Search);
        sHelper = new SharedPrefferenceHelper(SupervisorLocationApprovalListActivity .this);
        extraHelper = new ExtraHelper(SupervisorLocationApprovalListActivity .this);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // onBackPressed();
                Intent i = new Intent(SupervisorLocationApprovalListActivity.this, MainActivity.class);
                startActivity(i);
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

        tvTopHeader.setText("Customer Location Approval");
        // prepareMovieData();
        if(Helpers.isNetworkAvailable(SupervisorLocationApprovalListActivity.this)) {
            new GetAllCustomerLocation().execute();
        }
        else
        {
            Helpers.noConnectivityPopUp(SupervisorLocationApprovalListActivity.this);
        }


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SupervisorLocationApprovalListActivity.this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public void onClick(View view, int position) {
//        final ChangeLocation city = planList.get(position);
//        if(city.getLatitude()!=null && city.getLongitude()!=null
//                && !city.getLatitude().equals("0.0") && !city.getLongitude().equals("0.0") ) {
//            // Toast.makeText(getContext(), "" + planList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
//
//            Intent i = new Intent(SupervisorLocationApprovalListActivity.this, ChangeCoordinatesMapActivity.class);
//            i.putExtra("dealerlat",city.getOldlatitude());
//            i.putExtra("dealerlng",city.getOldlongitude());
//            i.putExtra("dealerlatnew", city.getLatitude());
//            i.putExtra("dealerlngnew", city.getLongitude());
//            i.putExtra("from", "approvallocation");
//            startActivity(i);
//        }
//        else
//        {
//            Toast.makeText(SupervisorLocationApprovalListActivity.this, "No Location Found", Toast.LENGTH_SHORT).show();
//        }


    }

    public class GetAllCustomerLocation extends AsyncTask<String, Void, Void> {
        ProgressDialog pDialog;
        private HttpHandler httpHandler;
        String errorMessage = "";



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SupervisorLocationApprovalListActivity.this);
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
            String getsupervsorsnapshot = Constants.FFM_GET_SUPERVISOR_LOCATIONS ;
            System.out.println("OUtlet Status URL : " + getsupervsorsnapshot);
            try {
                httpHandler = new HttpHandler(SupervisorLocationApprovalListActivity.this);
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
                Type journeycodeType = new TypeToken<ArrayList<ChangeCustomerLocationOutput>>() {
                }.getType();
                List<ChangeCustomerLocationOutput> journeycode = new Gson().fromJson(response, journeycodeType);
                //JourneyPlanOutPut journeycode = new Gson().fromJson(response, JourneyPlanOutPut.class);
                if (response != null) {

                    for (int j = 0; j < journeycode.size(); j++) {
                        ChangeLocation  plan4 = new ChangeLocation ();
                        plan4.setCode(journeycode.get(j).getCustomerCode()== null || journeycode.get(j).getCustomerCode().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getCustomerCode().toString());
                        plan4.setTso(journeycode.get(j).getTerritoryOfficierName() == null || journeycode.get(j).getTerritoryOfficierName().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getTerritoryOfficierName().toString());
                        plan4.setName(journeycode.get(j).getCustomerName() == null || journeycode.get(j).getCustomerName().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getCustomerName().toString());
                        plan4.setReason(journeycode.get(j).getReason() == null || journeycode.get(j).getReason().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getReason().toString());
                        plan4.setDistancedif(journeycode.get(j).getDifferenceInDistance() == null || journeycode.get(j).getDifferenceInDistance().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getDifferenceInDistance().toString());
                        plan4.setStatus(journeycode.get(j).getStatus()== null || journeycode.get(j).getStatus().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getStatus().toString());
                        plan4.setLatitude(String.valueOf(journeycode.get(j).getLatitude()));
                        plan4.setLongitude(String.valueOf(journeycode.get(j).getLongitude()));
                        plan4.setOldlatitude(String.valueOf(journeycode.get(j).getOldLatitude()));
                        plan4.setOldlongitude(String.valueOf(journeycode.get(j).getOldLongitude()));
                        String date = journeycode.get(j).getCreationDate()== null || journeycode.get(j).getCreationDate().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getCreationDate().toString();
                        plan4.setId(String.valueOf(journeycode.get(j).getId())==null || String.valueOf(journeycode.get(j).getId()).equals("") ? getString(R.string.not_applicable) : String.valueOf(journeycode.get(j).getId()));
                        plan4.setLastvisitcount(String.valueOf(journeycode.get(j).getLastVisitCount())==null || String.valueOf(journeycode.get(j).getLastVisitCount()).equals("") ? getString(R.string.not_applicable) : String.valueOf(journeycode.get(j).getLastVisitCount()));
                        plan4.setDate(Helpers.utcToAnyDateFormat(date,"yyyy-MM-dd'T'HH:mm:ss","MMM d yyyy"));
                        planList.add(plan4);
                        // plan4.setDate(journeycode.get(j).getCustomerId()== null || journeycode.get(j).getCustomerId().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getCustomerId().toString());



                    }
                }
            } catch (Exception exception) {
                if (response.equals("")) {
                    Helpers.displayMessage(SupervisorLocationApprovalListActivity.this, true, exception.getMessage());
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
            adapter = new LocationApprovalAdapter(planList,SupervisorLocationApprovalListActivity.this);
            recyclerView.setAdapter(adapter);
            adapter.setClickListener(SupervisorLocationApprovalListActivity.this);

        }
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(SupervisorLocationApprovalListActivity.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
