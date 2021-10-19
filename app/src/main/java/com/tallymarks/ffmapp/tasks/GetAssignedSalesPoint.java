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
import com.tallymarks.ffmapp.activities.MainActivity;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.assignedsalespoint.AssignedSalesPointOutput;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetAssignedSalesPoint extends AsyncTask<String, Void, Void> {
    ProgressDialog pDialog;
    private HttpHandler httpHandler;
    Context mContext;
    String errorMessage = "";
    SharedPrefferenceHelper sHelper;
    DatabaseHandler db;
    public GetAssignedSalesPoint (Context context)
    {
        this.mContext = context;
        this.sHelper = new SharedPrefferenceHelper(mContext);
        this.db = new DatabaseHandler(mContext);
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
        String assignedSalesPoint = Constants.FFM_GET_ASSIGNED_SAELS_POINT;
        System.out.println("OUtlet Status URL : " + assignedSalesPoint);
        try {
            httpHandler = new HttpHandler();
            HashMap<String, String> headerParams = new HashMap<>();
            headerParams.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
            response = httpHandler.httpGet(assignedSalesPoint, headerParams);
            Log.e("Assigned Sales Point", assignedSalesPoint);
            Log.e("Response", response);
            Type journeycodeType = new TypeToken<ArrayList<AssignedSalesPointOutput>>() {
            }.getType();
            List<AssignedSalesPointOutput> journeycode = new Gson().fromJson(response, journeycodeType);
            //JourneyPlanOutPut journeycode = new Gson().fromJson(response, JourneyPlanOutPut.class);
            if (response != null) {
                if (journeycode.size() > 0) {
                    for (int j = 0; j < journeycode.size(); j++) {
                        HashMap<String, String> dbParams = new HashMap<>();
                        dbParams.put(db.KEY_ASSIGNED_SALESPOINT_CODE, journeycode.get(j).getSalesPointCode() == null || journeycode.get(j).getSalesPointCode().equals("")? mContext.getString(R.string.not_applicable) : journeycode.get(j).getSalesPointCode().toString());
                        dbParams.put(db.KEY_ASSIGNED_SALESPOINT_ID, journeycode.get(j).getId()== null || journeycode.get(j).getId().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getId().toString());
                        dbParams.put(db.KEY_ASSIGNED_SALESPOINT_HIERARCHY_ID, journeycode.get(j).getHierarchyId() == null || journeycode.get(j).getHierarchyId().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getHierarchyId().toString());
                        dbParams.put(db.KEY_ASSIGNED_SALESPOINT_NAME, journeycode.get(j).getSalesPointName()== null || journeycode.get(j).getSalesPointName().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getSalesPointName().toString());
                        dbParams.put(db.KEY_ASSIGNED_SALESPOINT_TEHSIL_CODE, journeycode.get(j).getTehsilCode() == null || journeycode.get(j).getTehsilCode().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getTehsilCode().toString());
                        dbParams.put(db.KEY_ASSIGNED_SALESPOINT_TERRIORITY_CODE, journeycode.get(j).getTerritoryCode() == null || journeycode.get(j).getTerritoryCode().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getTerritoryCode().toString());
                        db.addData(db.ASSIGNED_SALES_POINT, dbParams);
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
