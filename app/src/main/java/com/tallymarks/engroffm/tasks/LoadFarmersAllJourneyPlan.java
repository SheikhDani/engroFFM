package com.tallymarks.engroffm.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tallymarks.engroffm.R;
import com.tallymarks.engroffm.database.ExtraHelper;
import com.tallymarks.engroffm.database.MyDatabaseHandler;
import com.tallymarks.engroffm.database.SharedPrefferenceHelper;
import com.tallymarks.engroffm.models.getAllFarmer.GetAllFarmers;
import com.tallymarks.engroffm.utils.Constants;
import com.tallymarks.engroffm.utils.Helpers;
import com.tallymarks.engroffm.utils.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tallymarks.engroffm.database.MyDatabaseHandler.ALL_FARMER_JOURNEY_PLAN;

public class LoadFarmersAllJourneyPlan extends AsyncTask<String, Void, Void> {

    private HttpHandler httpHandler;
    ProgressDialog pDialog;
    String status2 = "";
    String errorMessage = "";
    String discription = "";
    private SharedPrefferenceHelper sHelper;
    private ExtraHelper extraHelper;
    private MyDatabaseHandler db;
    private Context mContext;
    public LoadFarmersAllJourneyPlan(Context context){
        this.mContext = context;
        this.sHelper = new SharedPrefferenceHelper(mContext);
        this.extraHelper = new ExtraHelper(mContext);
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
        String journeyPlanUrl = Constants.FFM_GET_ALL_FARMERS;
        System.out.println("JourneyPlan URL : " + journeyPlanUrl);
        try {
            httpHandler = new HttpHandler(mContext);
            HashMap<String, String> headerParams = new HashMap<>();
            if(sHelper.getString(Constants.ACCESS_TOKEN)!=null  && !sHelper.getString(Constants.ACCESS_TOKEN).equals("")) {
                headerParams.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
            }
            else
            {
                headerParams.put(Constants.AUTHORIZATION, "Bearer " + extraHelper.getString(Constants.ACCESS_TOKEN));
            }
            response = httpHandler.httpGet(journeyPlanUrl, headerParams);
            Log.e("lOGIN Url", journeyPlanUrl);
            Log.e("Response", response);
            Type journeycodeType = new TypeToken<ArrayList<GetAllFarmers>>() {
            }.getType();
            List<GetAllFarmers> journeycode = new Gson().fromJson(response, journeycodeType);
            //JourneyPlanOutPut journeycode = new Gson().fromJson(response, JourneyPlanOutPut.class);
            if (response != null) {
                if (journeycode.size() > 0) {
                    HashMap<String, String> map = new HashMap<>();
                    for (int j = 0; j < journeycode.size(); j++) {
                        map.put(db.KEY_ALL_FARMER_JOURNEY_PLAN_SALES_POINT_CODE, journeycode.get(j).getSalesPointCode()== null || journeycode.get(j).getSalesPointCode().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getSalesPointCode());
                        map.put(db.KEY_ALL_FARMER_JOURNEY_PLAN_SALES_POINT_NAME, journeycode.get(j).getSalesPointName()== null || journeycode.get(j).getSalesPointName().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getSalesPointName());

                        db.addData(ALL_FARMER_JOURNEY_PLAN , map);
                    }

                }else {
                    JSONObject json = null;
                    json = new JSONObject(response);
                    discription = json.getString("description");
                    Helpers.displayMessage(mContext, true, discription);
                    String status = json.getString("success");

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

    }
}
