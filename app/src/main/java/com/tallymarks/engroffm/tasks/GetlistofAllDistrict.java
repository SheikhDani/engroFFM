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
import com.tallymarks.engroffm.database.SharedPrefferenceHelper;
import com.tallymarks.engroffm.models.listofallDistrict.ListofAllDistrictOutput;
import com.tallymarks.engroffm.utils.Constants;
import com.tallymarks.engroffm.utils.Helpers;
import com.tallymarks.engroffm.utils.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetlistofAllDistrict extends AsyncTask<String, Void, Void> {
    ProgressDialog pDialog;
    private HttpHandler httpHandler;
    String errorMessage = "";
    private Context mContext;
    ExtraHelper extraHelper;
    SharedPrefferenceHelper sHelper;
    public  GetlistofAllDistrict(Context context)
    {
        this.mContext = context;
        this.sHelper = new SharedPrefferenceHelper(mContext);
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
        String listofAllCrops = Constants.FFM_GET_LIST_OF_ALL_DICTRICT;
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
            Type journeycodeType = new TypeToken<ArrayList<ListofAllDistrictOutput>>() {
            }.getType();
            List<ListofAllDistrictOutput> journeycode = new Gson().fromJson(response, journeycodeType);
            //JourneyPlanOutPut journeycode = new Gson().fromJson(response, JourneyPlanOutPut.class);
            if (response != null) {
                if (journeycode.size() > 0) {

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
