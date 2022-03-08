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
import com.tallymarks.ffmapp.database.ExtraHelper;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.listofallrands.ListofAllBrandsOutput;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetListofAllBrands extends AsyncTask<String, Void, Void> {

    private HttpHandler httpHandler;
    ProgressDialog pDialog;
    String status2 = "";
    String errorMessage = "";
    private SharedPrefferenceHelper sHelper;
    private ExtraHelper extraHelper;
    private Context mContext;
    public GetListofAllBrands (Context context)
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
        String journeyPlanUrl = Constants.FFM_GET_LIST_OF_ALL_BRANDS + "?companyHeld=" + "true" +"&enabled=" + "true";
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
            //headerParams.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));

            response = httpHandler.httpGet(journeyPlanUrl, headerParams);
            Log.e("lOGIN Url", journeyPlanUrl);
            Log.e("Response", response);
            Type journeycodeType = new TypeToken<ArrayList<ListofAllBrandsOutput>>() {
            }.getType();
            List<ListofAllBrandsOutput> journeycode = new Gson().fromJson(response, journeycodeType);
            //JourneyPlanOutPut journeycode = new Gson().fromJson(response, JourneyPlanOutPut.class);
            if (response != null) {
                if (journeycode.size() > 0) {
                    for (int j = 0; j < journeycode.size(); j++) {
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
