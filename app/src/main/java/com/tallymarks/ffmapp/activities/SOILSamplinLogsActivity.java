package com.tallymarks.ffmapp.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.SOILSamplingLogsAdapter;
import com.tallymarks.ffmapp.adapters.TodayPlanAdapter;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.database.ExtraHelper;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.SoilSamplingLogs;
import com.tallymarks.ffmapp.models.TodayPlan;
import com.tallymarks.ffmapp.models.assignedcustomersofsubordiantes.GetAssignedCustomerSubOrdinatesOutput;
import com.tallymarks.ffmapp.models.soilsamplingoutput.SoilSamplingLogsOutput;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SOILSamplinLogsActivity extends AppCompatActivity {
    private TextView tvTopHeader;
    ImageView iv_menu, iv_back;
    TextView txtDate;
    Button back, btnLogs;
    SharedPrefferenceHelper sHelper;
    DatabaseHandler db;
    private List<SoilSamplingLogs> planList = new ArrayList<>();
    private RecyclerView recyclerView;
    TextView tvTotalSample;
    Calendar myCalendar;
    String targetDate = "";
    ExtraHelper extraHelper;
    static {
        System.loadLibrary("native-lib");
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_sampling_logs);
        initView();

    }

    private void initView() {

        tvTopHeader = findViewById(R.id.tv_dashboard);
        back = findViewById(R.id.back);
        myCalendar = Calendar.getInstance();
        db = new DatabaseHandler(SOILSamplinLogsActivity.this);
        sHelper = new SharedPrefferenceHelper(SOILSamplinLogsActivity.this);
        extraHelper = new ExtraHelper(SOILSamplinLogsActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        tvTotalSample = findViewById(R.id.txt_total_sample);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SOILSamplinLogsActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        iv_menu = findViewById(R.id.iv_drawer);
        txtDate = findViewById(R.id.tvDate);
        btnLogs = findViewById(R.id.btn_get_logs);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("SOIL SAMPLING LOGS");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        txtDate.setText(currentDateandTime);

        if (Helpers.isNetworkAvailable(SOILSamplinLogsActivity.this)) {
            new GetSoilSamplingLogs(txtDate.getText().toString()).execute();
        } else {
            Helpers.noConnectivityPopUp(SOILSamplinLogsActivity.this);
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SOILSamplinLogsActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SOILSamplinLogsActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SOILSamplinLogsActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();

            }
        });
        btnLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!txtDate.getText().toString().equals("")) {
                    if (Helpers.isNetworkAvailable(SOILSamplinLogsActivity.this)) {
                        new GetSoilSamplingLogs(txtDate.getText().toString()).execute();
                    } else {
                        Helpers.noConnectivityPopUp(SOILSamplinLogsActivity.this);
                    }
                }

            }
        });
    }

    public class GetSoilSamplingLogs extends AsyncTask<String, Void, Void> {
        ProgressDialog pDialog;
        private HttpHandler httpHandler;
        String errorMessage = "";
        String date;

        GetSoilSamplingLogs(String date) {
            this.date = date;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SOILSamplinLogsActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.show();
            pDialog.setCancelable(false);

            //expandableListGroup.clear();

        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected Void doInBackground(String... Url) {
            planList.clear();
            String response = "";
            String getsupervsorsnapshot = Constants.FFM_GET_SOIL_SAMPLING_LOGS + "?dateFrom=" + date + "&dateTo=" + date;
            System.out.println("OUtlet Status URL : " + getsupervsorsnapshot);
            try {
                httpHandler = new HttpHandler(SOILSamplinLogsActivity.this);
                HashMap<String, String> headerParams = new HashMap<>();
                if(sHelper.getString(Constants.ACCESS_TOKEN)!=null  && !sHelper.getString(Constants.ACCESS_TOKEN).equals("")) {
                    headerParams.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
                }
                else
                {
                    headerParams.put(Constants.AUTHORIZATION, "Bearer " + extraHelper.getString(Constants.ACCESS_TOKEN));
                }
              //  headerParams.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
                response = httpHandler.httpGet(getsupervsorsnapshot, headerParams);
                Log.e("Assigned Sales Point", getsupervsorsnapshot);
                Log.e("Response", response);
                Type journeycodeType = new TypeToken<ArrayList<SoilSamplingLogsOutput>>() {
                }.getType();
                List<SoilSamplingLogsOutput> journeycode = new Gson().fromJson(response, journeycodeType);
                //JourneyPlanOutPut journeycode = new Gson().fromJson(response, JourneyPlanOutPut.class);
                if (response != null) {
                    if (journeycode.size() > 0) {
                        for (int j = 0; j < journeycode.size(); j++) {
                            SoilSamplingLogs plan4 = new SoilSamplingLogs();
                            plan4.setFarmerid(journeycode.get(j).getFarmerId() == null || journeycode.get(j).getFarmerId().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getFarmerId().toString());
                            plan4.setFarmername(journeycode.get(j).getFarmerName() == null || journeycode.get(j).getFarmerName().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getFarmerName().toString());
                            plan4.setReferecne(journeycode.get(j).getReference() == null || journeycode.get(j).getReference().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getReference().toString());
                            plan4.setCheckintime(journeycode.get(j).getCheckInTime() == null || journeycode.get(j).getCheckInTime().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getCheckInTime().toString());
                            plan4.setChecoutouttime(journeycode.get(j).getCheckOutTime() == null || journeycode.get(j).getCheckOutTime().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getCheckOutTime().toString());
                            planList.add(plan4);

                        }
                    } else {
                        Helpers.displayMessage(SOILSamplinLogsActivity.this, true, "no Logs Found");
                    }
                }
            } catch (Exception exception) {
                if (response.equals("")) {
                    Helpers.displayMessage(SOILSamplinLogsActivity.this, true, exception.getMessage());
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
            SOILSamplingLogsAdapter adapter = new SOILSamplingLogsAdapter(planList);
            recyclerView.setAdapter(adapter);
            tvTotalSample.setText("TOTAL SAMPLES  " + planList.size());

        }
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            // view.setMinDate(System.currentTimeMillis() - 1000);
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String monthString = String.valueOf(monthOfYear + 1);
            String dayString = String.valueOf(dayOfMonth);
            if (monthString.length() == 1) {
                monthString = "0" + monthString;
            }
            if (dayString.length() == 1) {
                dayString = "0" + dayString;
            }
            targetDate = year + "-" + monthString + "-" + dayString;
            txtDate.setText(targetDate);
            // Log.e("targetdate", String.valueOf(target_date));


        }


    };
    @Override
    public void onBackPressed() {
        Intent i = new Intent(SOILSamplinLogsActivity.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
