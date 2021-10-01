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
import com.tallymarks.ffmapp.models.getallcustomersplanoutput.GetAllCustomersOutput;
import com.tallymarks.ffmapp.models.todayjourneyplanoutput.TodayJourneyPlanOutput;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoadCustomersTodayJourneyPlan extends AsyncTask<String, Void, Void> {

    private HttpHandler httpHandler;
    ProgressDialog pDialog;
    Context mContext;
    SharedPrefferenceHelper sHelper;
    DatabaseHandler db;

    String errorMessage = "";
    public LoadCustomersTodayJourneyPlan(Context context)
    {
        this.mContext = context;
        sHelper = new SharedPrefferenceHelper(mContext);
        db = new DatabaseHandler(mContext);
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
        String journeyPlanUrl = Constants.FFM_GET_TODAY_CUSTOMERS;
        System.out.println("JourneyPlan URL : " + journeyPlanUrl);
        try {
            httpHandler = new HttpHandler();
            HashMap<String, String> headerParams = new HashMap<>();
            headerParams.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
            response = httpHandler.httpGet(journeyPlanUrl, headerParams);
            Log.e("lOGIN Url", journeyPlanUrl);
            Log.e("Response", response);
            Type journeycodeType = new TypeToken<ArrayList<TodayJourneyPlanOutput>>() {
            }.getType();
            List<TodayJourneyPlanOutput> journeycode = new Gson().fromJson(response, journeycodeType);
            //JourneyPlanOutPut journeycode = new Gson().fromJson(response, JourneyPlanOutPut.class);
            if (response != null) {
                if (journeycode.size() > 0) {
                    HashMap<String, String> map = new HashMap<>();
                    for (int i = 0; i < journeycode.size(); i++) {
                        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_CODE, journeycode.get(i).getCustomerCode()== null || journeycode.get(i).getCustomerCode().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(i).getCustomerCode());
                        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, journeycode.get(i).getCustomerId()== null || journeycode.get(i).getCustomerId().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(i).getCustomerId());
                        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_NAME, journeycode.get(i).getCustomerName()== null || journeycode.get(i).getCustomerName().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(i).getCustomerName());
                        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_LATITUDE, journeycode.get(i).getLatitude()== null || journeycode.get(i).getLatitude().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(i).getLatitude().toString());
                        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_LONGITUDE, journeycode.get(i).getLongtitude()== null || journeycode.get(i).getLongtitude().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(i).getLongtitude().toString());
                        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_DAY_ID, journeycode.get(i).getDayId()== null || journeycode.get(i).getDayId().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(i).getDayId().toString());
                        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_JOURNEYPLAN_ID, journeycode.get(i).getJourneyPlanId()== null || journeycode.get(i).getJourneyPlanId().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(i).getJourneyPlanId().toString());
                        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_SALES_POINT_NAME, journeycode.get(i).getSalePointName()== null || journeycode.get(i).getSalePointName().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(i).getSalePointName());
                        if (journeycode.get(i).getOrders().size() > 0) {
                            for (int j = 0; j < journeycode.get(i).getOrders().size(); j++) {
                                HashMap<String, String> dbParams = new HashMap<>();
                                dbParams.put(db.KEY_TODAY_JOURNEY_ORDER_ID, journeycode.get(i).getOrders().get(j).getId() == null || journeycode.get(i).getOrders().get(j).getId().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(i).getOrders().get(j).getId().toString());
                                dbParams.put(db.KEY_TODAY_JOURNEY_ORDER_BRAND_NAME, journeycode.get(i).getOrders().get(j).getBrandName() == null || journeycode.get(i).getOrders().get(j).getBrandName().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(i).getOrders().get(j).getBrandName().toString());
                                dbParams.put(db.KEY_TODAY_JOURNEY_ORDER_NUMBER, journeycode.get(i).getOrders().get(j).getOrderNumber() == null || journeycode.get(i).getOrders().get(j).getOrderNumber().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(i).getOrders().get(j).getOrderNumber().toString());
                                dbParams.put(db.KEY_TODAY_JOURNEY_ORDER_DATE, journeycode.get(i).getOrders().get(j).getOrderDate() == null || journeycode.get(i).getOrders().get(j).getOrderDate().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(i).getOrders().get(j).getOrderDate().toString());
                                dbParams.put(db.KEY_TODAY_JOURNEY_ORDER_QUANTITY, journeycode.get(i).getOrders().get(j).getOrderQuantity() == null || journeycode.get(i).getOrders().get(j).getOrderQuantity().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(i).getOrders().get(j).getOrderQuantity().toString());
                                db.addData(db.TODAY_JOURNEY_PLAN_ORDERS, dbParams);
                                for (int k = 0; k < journeycode.get(i).getOrders().get(j).getInvoices().size(); k++) {
                                    HashMap<String, String> dbParamsinvoice = new HashMap<>();
                                    dbParamsinvoice.put(db.KEY_TODAY_JOURNEY_ORDER_INVOICE_NUMBER, journeycode.get(i).getOrders().get(j).getInvoices().get(k).getInvoiceNumber() == null || journeycode.get(i).getOrders().get(j).getInvoices().get(k).getInvoiceNumber().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(i).getOrders().get(j).getInvoices().get(k).getInvoiceNumber().toString());
                                    dbParamsinvoice.put(db.KEY_TODAY_JOURNEY_ORDER_DISPATCH_DATE, journeycode.get(i).getOrders().get(j).getInvoices().get(k).getDispatchDate() == null || journeycode.get(i).getOrders().get(j).getInvoices().get(k).getDispatchDate().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(i).getOrders().get(j).getInvoices().get(k).getDispatchDate().toString());
                                    dbParamsinvoice.put(db.KEY_TODAY_JOURNEY_ORDER_DISPATCH_QUANTITY, journeycode.get(i).getOrders().get(j).getInvoices().get(k).getDispatchQuantity() == null || journeycode.get(i).getOrders().get(j).getInvoices().get(k).getDispatchQuantity().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(i).getOrders().get(j).getInvoices().get(k).getDispatchQuantity().toString());
                                    dbParamsinvoice.put(db.KEY_TODAY_JOURNEY_ORDER_INVOICE_AVAILABLE_QUANITY, journeycode.get(i).getOrders().get(j).getInvoices().get(k).getAvailableQuantity() == null || journeycode.get(i).getOrders().get(j).getInvoices().get(k).getAvailableQuantity().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(i).getOrders().get(j).getInvoices().get(k).getAvailableQuantity().toString());
                                    dbParamsinvoice.put(db.KEY_TODAY_JOURNEY_ORDER_INVOCIE_RATE, journeycode.get(i).getOrders().get(j).getInvoices().get(k).getInvoiceRate() == null || journeycode.get(i).getOrders().get(j).getInvoices().get(k).getInvoiceRate().equals("") ? mContext.getString(R.string.not_applicable) : journeycode.get(i).getOrders().get(j).getInvoices().get(k).getInvoiceRate().toString());
                                    db.addData(db.TODAY_JOURNEY_PLAN_ORDERS_INVOICES, dbParamsinvoice);

                                }

                            }
                        }
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
                        Helpers.displayMessage(mContext, true, errorMessage);
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
