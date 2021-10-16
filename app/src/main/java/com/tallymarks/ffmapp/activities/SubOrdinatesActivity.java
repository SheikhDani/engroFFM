package com.tallymarks.ffmapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.SalesCallAdapter;
import com.tallymarks.ffmapp.adapters.SubOrdinatesAdapter;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.DataModel;
import com.tallymarks.ffmapp.models.Subordinates;
import com.tallymarks.ffmapp.models.SuperVisorSnapShotChild;
import com.tallymarks.ffmapp.models.assignedsalespoint.AssignedSalesPointOutput;
import com.tallymarks.ffmapp.models.supervisorsnapshotoutput.SuperVisorSnapShotOutput;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.HttpHandler;

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

public class SubOrdinatesActivity extends AppCompatActivity {
    private TextView tvTopHeader;
    ImageView iv_menu, iv_back;
    ListView listView;
    private SubOrdinatesAdapter adapter;
    ArrayList<Subordinates> dataModels;
    SharedPrefferenceHelper sHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subordinates);
        initView();

    }

    private void initView() {
        tvTopHeader = findViewById(R.id.tv_dashboard);
        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("SUBORDINATES");
        sHelper = new SharedPrefferenceHelper(SubOrdinatesActivity.this);
        listView = findViewById(R.id.lv_subordinates);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SubOrdinatesActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        dataModels = new ArrayList();
//        Subordinates s = new Subordinates();
//        s.setName("Riaz Ul Haq");
//        dataModels.add(s);


        //get From Weekn And year
        SimpleDateFormat sdffrom = new SimpleDateFormat("dd/MM/yyyy");
        String datefrom = sdffrom.format(System.currentTimeMillis());
        Date myDatefrom = null;
        try {
            myDatefrom = sdffrom.parse(datefrom);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendarfrom = Calendar.getInstance();
        calendarfrom.setTime(myDatefrom);
        calendarfrom.add(Calendar.DAY_OF_YEAR, -21);
        Date newDatefrom = calendarfrom.getTime();
        String fromdate = sdffrom.format(newDatefrom);
        Calendar calfrom = Calendar.getInstance();
        Date myDatefrom2 = null;
        try {
            myDatefrom2 = sdffrom.parse(fromdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calfrom.setTime(myDatefrom2);
        int fromyear = calfrom.get(Calendar.YEAR);
        int fromweek = calfrom.get(Calendar.WEEK_OF_YEAR);

        Log.e("fromweek", String.valueOf(fromweek));
        Log.e("fromyear", String.valueOf(fromyear));


        //get From Weekn And year
        SimpleDateFormat sdfto = new SimpleDateFormat("dd/MM/yyyy");
        String dateto = sdfto.format(System.currentTimeMillis());
        Date myDateto = null;
        try {
            myDateto = sdfto.parse(dateto);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendarto = Calendar.getInstance();
        calendarto.setTime(myDateto);
        calendarto.add(Calendar.DAY_OF_YEAR, 28);
        Date newDateto = calendarto.getTime();
        String todate = sdfto.format(newDateto);
        Calendar calto = Calendar.getInstance();
        Date myDateto2 = null;
        try {
            myDateto2 = sdfto.parse(todate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calto.setTime(myDateto2);
        int toyear = calto.get(Calendar.YEAR);
        int toweek = calto.get(Calendar.WEEK_OF_YEAR);

        Log.e("toweek", String.valueOf(toweek));
        Log.e("toyear", String.valueOf(toyear));

        new GetSueprVisorSnapShot(fromweek, toweek, fromyear, toyear).execute();


    }

    public class GetSueprVisorSnapShot extends AsyncTask<String, Void, Void> {
        ProgressDialog pDialog;
        private HttpHandler httpHandler;
        String errorMessage = "";
        int fromweek, toweek, fromyear, toyear;

        public GetSueprVisorSnapShot(int fromweek, int toweek, int fromyear, int toyear) {
            this.fromweek = fromweek;
            this.toweek = toweek;
            this.toyear = toyear;
            this.fromyear = fromyear;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SubOrdinatesActivity.this);
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
            String getsupervsorsnapshot = Constants.FFM_GET_SUPERVISOR_SNAPSHOT + "?fromWeekNo=" + fromweek + "&toWeekNo=" + toweek + "&fromYear=" + fromyear + "&toYear=" + fromyear;
            System.out.println("OUtlet Status URL : " + getsupervsorsnapshot);
            try {
                httpHandler = new HttpHandler();
                HashMap<String, String> headerParams = new HashMap<>();
                headerParams.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
                response = httpHandler.httpGet(getsupervsorsnapshot, headerParams);
                Log.e("Assigned Sales Point", getsupervsorsnapshot);
                Log.e("Response", response);
                Type journeycodeType = new TypeToken<ArrayList<SuperVisorSnapShotOutput>>() {
                }.getType();
                List<SuperVisorSnapShotOutput> journeycode = new Gson().fromJson(response, journeycodeType);
                //JourneyPlanOutPut journeycode = new Gson().fromJson(response, JourneyPlanOutPut.class);
                if (response != null) {

                    for (int j = 0; j < journeycode.size(); j++) {
                        Subordinates s = new Subordinates();
                        s.setName(journeycode.get(j).getName() == null || journeycode.get(j).getName().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getName().toString());
                        s.setSubordianteid(journeycode.get(j).getSubordinateUserId() == null || journeycode.get(j).getSubordinateUserId().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getSubordinateUserId().toString());
                        s.setNoofjourneyplan(journeycode.get(j).getNumberOfJourneyPlans() == null || journeycode.get(j).getNumberOfJourneyPlans().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getNumberOfJourneyPlans().toString());
                        s.setHasjourneyplan(journeycode.get(j).getHasJourneyPlans() == null || journeycode.get(j).getHasJourneyPlans().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getHasJourneyPlans().toString());
                        dataModels.add(s);


                    }
                }
            } catch (Exception exception) {
                if (response.equals("")) {
                    Helpers.displayMessage(SubOrdinatesActivity.this, true, exception.getMessage());
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
            adapter = new SubOrdinatesAdapter(dataModels, getApplicationContext());
            listView.setAdapter(adapter);

        }
    }
}
