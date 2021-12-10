package com.tallymarks.ffmapp.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.database.ExtraHelper;
import com.tallymarks.ffmapp.database.MyDatabaseHandler;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.getFarmerTodayJourneyPlan.FarmerTodayJourneyPlan;
import com.tallymarks.ffmapp.models.getallcustomersplanoutput.GetAllCustomersOutput;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tallymarks.ffmapp.database.MyDatabaseHandler.TODAY_FARMER_JOURNEY_PLAN;

public class GetFatmerTodayJourneyPlan extends AsyncTask<String, Void, Void> {

    private HttpHandler httpHandler;
    ProgressDialog pDialog;
    Context mContext;
    SharedPrefferenceHelper sHelper;
    ExtraHelper extraHelper;
    MyDatabaseHandler db;

    String errorMessage = "";
    public GetFatmerTodayJourneyPlan (Context context)
    {
        this.mContext = context;
        sHelper = new SharedPrefferenceHelper(mContext);
        extraHelper = new ExtraHelper(mContext);
        this.db = new MyDatabaseHandler(mContext);
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
        String journeyPlanUrl = Constants.FFM_GET_FARMER_TODAY_JOURNEY_PLAN;
        System.out.println("JourneyPlan URL : " + journeyPlanUrl);
        try {
            httpHandler = new HttpHandler();
            db = new MyDatabaseHandler(mContext);
            HashMap<String, String> headerParams = new HashMap<>();
            if(sHelper.getString(Constants.ACCESS_TOKEN)!=null  && !sHelper.getString(Constants.ACCESS_TOKEN).equals("")) {
                headerParams.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
            }
            else
            {
                headerParams.put(Constants.AUTHORIZATION, "Bearer " + extraHelper.getString(Constants.ACCESS_TOKEN));
            }
           // headerParams.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
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
                        sHelper.setString(Constants.FARMER_TODAY_PLAN_NOT_FOUND,"1");
                        map.put(db.KEY_TODAY_JOURNEY_FARMER_CODE, journeycode.get(j).getFarmerCode()== null || journeycode.get(j).getFarmerCode().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getFarmerCode().toString());
                        map.put(db.KEY_TODAY_JOURNEY_FARMER_ID, journeycode.get(j).getFarmerId()== null || journeycode.get(j).getFarmerId().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getFarmerId().toString());
                        map.put(db.KEY_TODAY_JOURNEY_FARMER_NAME, journeycode.get(j).getFarmerName()== null || journeycode.get(j).getFarmerName().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getFarmerName());
                        map.put(db.KEY_TODAY_JOURNEY_FARMER_LATITUDE, journeycode.get(j).getLatitude()== null || journeycode.get(j).getLatitude().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getLatitude().toString());
                        map.put(db.KEY_TODAY_JOURNEY_FARMER_LONGITUDE, journeycode.get(j).getLongtitude()== null || journeycode.get(j).getLongtitude().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getLongtitude().toString());
                        map.put(db.KEY_TODAY_JOURNEY_FARMER_DAY_ID, journeycode.get(j).getDayId()== null || journeycode.get(j).getDayId().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getDayId().toString());
                        map.put(db.KEY_TODAY_JOURNEY_FARMER_JOURNEYPLAN_ID, journeycode.get(j).getJourneyPlanId()== null || journeycode.get(j).getJourneyPlanId().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getJourneyPlanId().toString());
                        map.put(db.KEY_TODAY_JOURNEY_FARMER_SALES_POINT_NAME, journeycode.get(j).getSalesPoint()== null || journeycode.get(j).getSalesPoint().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getSalesPoint());
                        map.put(db.KEY_TODAY_FARMER_MOBILE_NO, journeycode.get(j).getMobileNo()== null || journeycode.get(j).getMobileNo().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getMobileNo());
                        map.put(db.KEY_TODAY_JOURNEY_IS_VISITED, "Not Visited");
                        map.put(db.KEY_TODAY_JOURNEY_IS_POSTED, "0");
                        map.put(db.KEY_PLAN_TYPE, "TODAY");

                        db.addData(TODAY_FARMER_JOURNEY_PLAN , map);
                    }

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
                    errorMessage = json.getString("description");
                    String status = json.getString("success");
                    if (status.equals("false")) {
                        Helpers.displayMessage(mContext, true, errorMessage);
                        sHelper.setString(Constants.FARMER_TODAY_PLAN_NOT_FOUND,"0");
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


    }
}
