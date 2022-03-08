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
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.listofallcrops.ListofallCropsOutput;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetListofAllCrops extends AsyncTask<String, Void, Void> {
    ProgressDialog pDialog;
    private HttpHandler httpHandler;
    Context mContext;
    String errorMessage = "";
    SharedPrefferenceHelper sHelper;
    ExtraHelper extraHelper;
    DatabaseHandler db;
    public GetListofAllCrops(Context context)
    {
        this.mContext = context;
        this.sHelper = new SharedPrefferenceHelper(mContext);
        this.db = new DatabaseHandler(mContext);
        this.extraHelper = new ExtraHelper(mContext);
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
        String listofAllCrops = Constants.FFM_GET_LIST_ALL_CROPS;
        System.out.println("OUtlet Status URL : " + listofAllCrops);
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
           // headerParams.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
            response = httpHandler.httpGet(listofAllCrops, headerParams);
            Log.e("list Crops", listofAllCrops);
            Log.e("Response", response);
            Type journeycodeType = new TypeToken<ArrayList<ListofallCropsOutput>>() {
            }.getType();
            List<ListofallCropsOutput> journeycode = new Gson().fromJson(response, journeycodeType);
            //JourneyPlanOutPut journeycode = new Gson().fromJson(response, JourneyPlanOutPut.class);
            if (response != null) {
                if (journeycode.size() > 0) {
                    for (int j = 0; j < journeycode.size(); j++) {
                        HashMap<String, String> dbParams = new HashMap<>();
                        dbParams.put(db.KEY_CROP_ID, journeycode.get(j).getId() == null || journeycode.get(j).getId() == 0 ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getId().toString());
                        dbParams.put(db.KEY_CROP_LONG_DESCRIPTION, journeycode.get(j).getLongDescription() == null || journeycode.get(j).getLongDescription().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getLongDescription().toString());
                        dbParams.put(db.KEY_CROP_SHORT_DESCRIPTION, journeycode.get(j).getShortDescription() == null || journeycode.get(j).getShortDescription().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getShortDescription().toString());
                        dbParams.put(db.KEY_CROP_NAME, journeycode.get(j).getName() == null || journeycode.get(j).getName().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(j).getName().toString());
                        db.addData(db.CROPS_LIST, dbParams);
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
