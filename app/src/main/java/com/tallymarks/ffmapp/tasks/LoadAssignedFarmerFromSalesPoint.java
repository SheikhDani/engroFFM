package com.tallymarks.ffmapp.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.FarmersAdapter;
import com.tallymarks.ffmapp.database.MyDatabaseHandler;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.getFarmerTodayJourneyPlan.FarmerTodayJourneyPlan;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoadAssignedFarmerFromSalesPoint extends AsyncTask<String, Void, Void> {

    private HttpHandler httpHandler;
    ProgressDialog pDialog;
    String status2 = "";
    String errorMessage = "";
    String salesPointCode = "";
    private SharedPrefferenceHelper sHelper;
    private MyDatabaseHandler mydb;
    private Context mContext;
    FarmersAdapter farmersAdapter;
    public LoadAssignedFarmerFromSalesPoint(Context context, String salesPoint, FarmersAdapter adapter){
        this.mContext = context;
        this.sHelper = new SharedPrefferenceHelper(mContext);
        this.mydb = new MyDatabaseHandler(mContext);
        this.salesPointCode = salesPoint;
        this.farmersAdapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage(mContext.getResources().getString(R.string.loading));
        pDialog.setIndeterminate(false);
        pDialog.show();
        pDialog.setCancelable(false);

        //expandableListGroup.clear();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected Void doInBackground(String... Url) {
        String response = "";
        String journeyPlanUrl = Constants.FFM_GET_ASSIGNED_FARMERS + "?salePointCode=" + salesPointCode + "&enabled=true";
        System.out.println("JourneyPlan URL : " + journeyPlanUrl);
        try {
            httpHandler = new HttpHandler();
            HashMap<String, String> headerParams = new HashMap<>();
            headerParams.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
            response = httpHandler.httpGet(journeyPlanUrl, headerParams);
            Log.e("lOGIN Url", journeyPlanUrl);
            Log.e("Response", response);
            Type journeycodeType = new TypeToken<ArrayList<FarmerTodayJourneyPlan>>() {
            }.getType();
            List<FarmerTodayJourneyPlan> journeycode = new Gson().fromJson(response, journeycodeType);
            //JourneyPlanOutPut journeycode = new Gson().fromJson(response, JourneyPlanOutPut.class);
            if (response != null) {
                if (journeycode.size() > 0) {
                    HashMap<String, String> map = new HashMap<>();
                    for (int j = 0; j < journeycode.size(); j++) {
                        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_CODE, journeycode.get(j).getFarmerCode()== null || journeycode.get(j).getFarmerCode().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getFarmerCode().toString());
                        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_ID, journeycode.get(j).getFarmerId()== null || journeycode.get(j).getFarmerId().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getFarmerId().toString());
                        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_NAME, journeycode.get(j).getFarmerName()== null || journeycode.get(j).getFarmerName().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getFarmerName());
                        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_LATITUDE, journeycode.get(j).getLatitude()== null || journeycode.get(j).getLatitude().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getLatitude().toString());
                        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_LONGITUDE, journeycode.get(j).getLongtitude()== null || journeycode.get(j).getLongtitude().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getLongtitude().toString());
                        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_DAY_ID, journeycode.get(j).getDayId()== null || journeycode.get(j).getDayId().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getDayId().toString());
                        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_JOURNEYPLAN_ID, journeycode.get(j).getJourneyPlanId()== null || journeycode.get(j).getJourneyPlanId().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getJourneyPlanId().toString());
                        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_SALES_POINT_NAME, journeycode.get(j).getSalesPoint()== null || journeycode.get(j).getSalesPoint().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getSalesPoint());
                        map.put(mydb.KEY_TODAY_FARMER_MOBILE_NO, journeycode.get(j).getMobileNo()== null || journeycode.get(j).getMobileNo().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getMobileNo());
                        map.put(mydb.KEY_TODAY_JOURNEY_IS_VISITED, "Not Visited");
                        map.put(mydb.KEY_TODAY_JOURNEY_IS_POSTED, "0");
                        map.put(mydb.KEY_PLAN_TYPE, "ALL");
                        mydb.addData(mydb.TODAY_FARMER_JOURNEY_PLAN , map);

                    }

                    HashMap<String, String> map2 = new HashMap<>();
                    map2.put(mydb.KEY_DOWNLOADED_FARMER_SALES_POINT_CODE,salesPointCode);
                    mydb.addData(mydb.DOWNLOADED_FARMER_DATA,map2);
                    pDialog.dismiss();
                    Helpers.displayMessage(mContext, true, "Download Successfully");


                }
                JSONObject json = null;
                try {
                    json = new JSONObject(response);
                    String message = json.getString("description");
                    Helpers.displayMessage(mContext, true, message);

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        } catch (Exception exception) {
            if (response.equals("")) {
                Helpers.displayMessage(mContext, true, exception.getMessage());
                //showResponseDialog( mContext.getResources().getString(R.string.alert),exception.getMessage());
                //pDialog.dismiss();
            } else {
                JSONObject json = null;
                try {
                    json = new JSONObject(response);
                    String message = json.getString("description");
                    Helpers.displayMessage(mContext, true, message);

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
        farmersAdapter.notifyDataSetChanged();
       // Toast.makeText(mContext, "Farmer Downloaded Successfully", Toast.LENGTH_SHORT).show();

    }

}
