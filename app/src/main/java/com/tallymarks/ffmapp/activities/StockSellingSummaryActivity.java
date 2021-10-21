package com.tallymarks.ffmapp.activities;

import static com.tallymarks.ffmapp.utils.Helpers.getDatefromMilis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.TodayPlanAdapter;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.SoilSamplingCrops;
import com.tallymarks.ffmapp.models.StockSellingSummary;
import com.tallymarks.ffmapp.models.TodayPlan;
import com.tallymarks.ffmapp.models.assignedcustomersofsubordiantes.GetAssignedCustomerSubOrdinatesOutput;
import com.tallymarks.ffmapp.models.stockseliingsummaroutput.StockSellingSummaryOutput;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StockSellingSummaryActivity extends AppCompatActivity {
    private TextView tvTopHeader;
    Button btn_next;
    ImageView iv_menu, iv_back;
    private TableLayout mTableLayout;
    ArrayList<StockSellingSummary> arraylist = new ArrayList<StockSellingSummary>();
    SharedPrefferenceHelper sHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_selling_summary);
        initView();
    }
    private void initView()
    {
        tvTopHeader = findViewById(R.id.tv_dashboard);
        mTableLayout = (TableLayout) findViewById(R.id.displayLinear);
        sHelper = new SharedPrefferenceHelper(StockSellingSummaryActivity.this);
        iv_menu = findViewById(R.id.iv_drawer);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);

        tvTopHeader.setText("STOCK SELLING SUMMARY");
      //  prepareRecommendationData();

        if(Helpers.isNetworkAvailable(StockSellingSummaryActivity.this)) {
            new GetlastVisitStockSale().execute();
        }
        else
        {
            Helpers.noConnectivityPopUp(StockSellingSummaryActivity.this);
        }


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              onBackPressed();
//                Intent i = new Intent(StockSellingSummaryActivity.this, SuperVisorSnapShotActivity.class);
//                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
    @Override
    public void onBackPressed() {
       super.onBackPressed();
    }
    public void drawRecommendationTable()
    {
        int cursorIndex=0;
        mTableLayout.removeAllViews();
        TableRow row = new TableRow(this);
        row.setGravity(Gravity.CENTER_HORIZONTAL);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        TextView column1 = new TextView(this);
        column1.setText("Order#");
        //column1.setBackgroundResource(R.drawable.table_row);
        column1.setGravity(Gravity.CENTER);
        column1.setTextSize(12);
        column1.setPadding(2, 2, 2, 2);
        column1.setTextColor(getResources().getColor(R.color.colorPrimary));
        column1.setTypeface(Typeface.DEFAULT_BOLD);
        column1.setBackgroundColor(getResources().getColor(R.color.green));
        TextView column2 = new TextView(this);
        column2.setText("Invoice#");
        column2.setTextSize(12);
        column2.setGravity(Gravity.CENTER);
        //column2.setBackgroundResource(R.drawable.table_row);
        column2.setPadding(2, 2, 2, 2);
        column2.setTextColor(getResources().getColor(R.color.colorPrimary));
        column2.setTypeface(Typeface.DEFAULT_BOLD);
        column2.setBackgroundColor(getResources().getColor(R.color.green));

        TextView column3 = new TextView(this);
        column3.setText("Products");
        column3.setGravity(Gravity.CENTER);
        column3.setTextSize(12);
        //column3.setBackgroundResource(R.drawable.table_row);
        column3.setPadding(2, 2, 2, 2);
        column3.setBackgroundColor(getResources().getColor(R.color.green));
        column3.setTextColor(getResources().getColor(R.color.colorPrimary));
        column3.setTypeface(Typeface.DEFAULT_BOLD);
        TextView column4 = new TextView(this);
        column4.setText("Quantity Sold");
        column4.setTextSize(12);
        column4.setGravity(Gravity.CENTER);
        //column4.setBackgroundResource(R.drawable.table_row);
        column4.setPadding(2, 2, 2, 2);
        column4.setBackgroundColor(getResources().getColor(R.color.green));
        column4.setTextColor(getResources().getColor(R.color.colorPrimary));
        column4.setTypeface(Typeface.DEFAULT_BOLD);
        TextView column5 = new TextView(this);
        column5.setText("NSP");
        column5.setTextSize(12);
        column5.setGravity(Gravity.CENTER);
        //column4.setBackgroundResource(R.drawable.table_row);
        column5.setPadding(2, 2, 2, 2);
        column5.setBackgroundColor(getResources().getColor(R.color.green));
        column5.setTextColor(getResources().getColor(R.color.colorPrimary));
        column5.setTypeface(Typeface.DEFAULT_BOLD);

        TextView column6 = new TextView(this);
        column6.setText("Visit Date");
        column6.setTextSize(12);
        column6.setGravity(Gravity.CENTER);
        //column4.setBackgroundResource(R.drawable.table_row);
        column6.setPadding(2, 2, 2, 2);
        column6.setBackgroundColor(getResources().getColor(R.color.green));
        column6.setTextColor(getResources().getColor(R.color.colorPrimary));
        column6.setTypeface(Typeface.DEFAULT_BOLD);

//        row.addView(column1);
//        row.addView(column2);
//        row.addView(column3);
//        row.addView(column4);
        row.addView(column1, new TableRow.LayoutParams(0, 150, 0.60f));
        row.addView(column2, new TableRow.LayoutParams(0, 150, 0.60f));
        row.addView(column3, new TableRow.LayoutParams(0, 150, 0.60f));
        row.addView(column4, new TableRow.LayoutParams(0, 150, 0.80f));
        row.addView(column5, new TableRow.LayoutParams(0, 150, 0.40f));
        row.addView(column6, new TableRow.LayoutParams(0, 150, 0.80f));


        mTableLayout.addView(row);

        View vline = new View(this);
        vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2));
        mTableLayout.addView(vline);

        for(int i=0;i<arraylist.size();i++) {
            cursorIndex++;

            TableRow row2 = new TableRow(this);

            TextView orderno = new TextView(this);
            orderno.setText(arraylist.get(i).getOrderno());
            orderno.setTextSize(10);
            //startDate.setBackgroundResource(R.drawable.table_row);
            orderno.setGravity(Gravity.CENTER);
            orderno.setPadding(2, 2, 2, 2);
            TextView invocie = new TextView(this);
            invocie.setText(arraylist.get(i).getInvocieno());
            invocie.setGravity(Gravity.CENTER);
            invocie.setTextSize(10);
            //name.setBackgroundResource(R.drawable.table_row);
            invocie.setPadding(2, 2, 2, 2);

            TextView  products = new TextView(this);
            products.setText(arraylist.get(i).getProdutcs());
            products.setGravity(Gravity.CENTER);

            products.setTextSize(10);
            //status.setBackgroundResource(R.drawable.table_row);
            products.setPadding(2, 2, 2, 2);

            TextView quanitysold= new TextView(this);
            quanitysold.setText(arraylist.get(i).getQuanitysold());
            quanitysold.setGravity(Gravity.CENTER);
            quanitysold.setTextSize(10);
            //srNo.setBackgroundResource(R.drawable.table_row);
            quanitysold.setPadding(2, 2, 2, 2);

            TextView nsp= new TextView(this);
            nsp.setText(arraylist.get(i).getNsp());
            nsp.setGravity(Gravity.CENTER);
            nsp.setTextSize(10);
            //srNo.setBackgroundResource(R.drawable.table_row);
            nsp.setPadding(2, 2, 2, 2);

            TextView visitdate= new TextView(this);
            visitdate.setText(arraylist.get(i).getVisitdate());
            visitdate.setGravity(Gravity.CENTER);
            visitdate.setTextSize(10);
            //srNo.setBackgroundResource(R.drawable.table_row);
            visitdate.setPadding(2, 2, 2, 2);

//            row2.addView(srNo);
//            row2.addView(name);
//            row2.addView(startDate);
//            row2.addView(status);
            row2.addView(orderno, new TableRow.LayoutParams(0, 150, 0.60f));
            row2.addView(invocie, new TableRow.LayoutParams(0, 150, 0.60f));
            row2.addView(products, new TableRow.LayoutParams(0, 150, 0.60f));
            row2.addView(quanitysold, new TableRow.LayoutParams(0, 150, 0.80f));
            row2.addView(nsp, new TableRow.LayoutParams(0, 150, 0.40f));
            row2.addView(visitdate, new TableRow.LayoutParams(0, 150, 0.80f));


            mTableLayout.addView(row2);

            vline = new View(this);
            vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2));
            mTableLayout.addView(vline);
        }
           /* }
            while (cursor.moveToNext());
        }*/

    }
    private void prepareRecommendationData() {
       StockSellingSummary prod = new  StockSellingSummary();
        prod .setInvocieno("2312312312");
        prod .setNsp("dansih");
        prod .setProdutcs("Wheat");
        prod .setVisitdate("16 June ,2021");
        prod.setQuanitysold("250");
        prod.setOrderno("23123123");
        arraylist.add(prod);




        // notify adapter about data set changes
        // so that it will render the list with new data

    }
    public class GetlastVisitStockSale extends AsyncTask<String, Void, Void> {
        ProgressDialog pDialog;
        private HttpHandler httpHandler;
        String errorMessage = "";
        String date="";



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StockSellingSummaryActivity.this);
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
            String getsupervsorsnapshot = Constants.FFM_GET_LAST_VISIT_STOCK_SALE + "?subordinateId=" + sHelper.getString(Constants.SUBORDINATE_ID) ;
            System.out.println("OUtlet Status URL : " + getsupervsorsnapshot);
            try {
                httpHandler = new HttpHandler();
                HashMap<String, String> headerParams = new HashMap<>();
                headerParams.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
                response = httpHandler.httpGet(getsupervsorsnapshot, headerParams);
                Log.e("Assigned Sales Point", getsupervsorsnapshot);
                Log.e("Response", response);
                Type journeycodeType = new TypeToken<ArrayList<StockSellingSummaryOutput>>() {
                }.getType();
                List<StockSellingSummaryOutput> journeycode = new Gson().fromJson(response, journeycodeType);
                //JourneyPlanOutPut journeycode = new Gson().fromJson(response, JourneyPlanOutPut.class);
                if (response != null) {
                    for (int j = 0; j < journeycode.size(); j++) {
                        StockSellingSummary prod = new  StockSellingSummary();
                        date  = journeycode.get(j).getVisitDate() == null || journeycode.get(j).getVisitDate().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getVisitDate().toString();
                        prod.setVisitdate(getDatefromMilis(date));
                        prod.setInvocieno(journeycode.get(j).getInvoiceNumber() == null || journeycode.get(j).getInvoiceNumber().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getInvoiceNumber());
                        prod.setQuanitysold(journeycode.get(j).getQuantitySold() == null || journeycode.get(j).getQuantitySold().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getQuantitySold().toString());
                        prod.setOrderno(journeycode.get(j).getOrderNumber() == null || journeycode.get(j).getOrderNumber().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getOrderNumber());
                        prod.setNsp(journeycode.get(j).getNetSellingPrice() == null || journeycode.get(j).getNetSellingPrice().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getNetSellingPrice().toString());
                        prod.setProdutcs(journeycode.get(j).getBrandName()== null || journeycode.get(j).getBrandName().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getBrandName());
                        arraylist.add(prod);




                    }
                }
            } catch (Exception exception) {
                if (response.equals("")) {
                    Helpers.displayMessage(StockSellingSummaryActivity.this, true, exception.getMessage());
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
            drawRecommendationTable();
        }
    }
}
