package com.tallymarks.ffmapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.ConversionRetentionAdapter;
import com.tallymarks.ffmapp.database.ExtraHelper;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.Conversion;
import com.tallymarks.ffmapp.models.getconversionretention.GetConversionRetentionOutput;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.HttpHandler;
import com.tallymarks.ffmapp.utils.ItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConversionRetentionActivity extends AppCompatActivity implements ItemClickListener {
    private TextView tvTopHeader;
    ImageView iv_menu, iv_back ,iv_add_retention;
    private List<Conversion> planList = new ArrayList<>();
    private RecyclerView recyclerView;
    private EditText et_Search;
    SharedPrefferenceHelper sHelper;
    TextView txt_no_data;
    ConversionRetentionAdapter adapter;
    ExtraHelper extraHelper;
    static {
        System.loadLibrary("native-lib");
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion_retention);
        initView();

    }
    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        txt_no_data = findViewById(R.id.empty_view);
        sHelper = new SharedPrefferenceHelper(ConversionRetentionActivity.this);
      extraHelper = new ExtraHelper(ConversionRetentionActivity.this);
        tvTopHeader = findViewById(R.id.tv_dashboard);
        tvTopHeader.setVisibility(View.VISIBLE);
        et_Search = findViewById(R.id.et_Search);
        tvTopHeader.setText("CONVERSION RETENTION");
        iv_menu = findViewById(R.id.iv_drawer);
        iv_add_retention = findViewById(R.id.img_add_retention);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        planList.clear();


        recyclerView.setHasFixedSize(true);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ConversionRetentionActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        iv_add_retention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addretention = new Intent(ConversionRetentionActivity.this,ConverionRetentionFormActivity.class);
                sHelper.setString(Constants.FARMER_CONVERSION_RETENTION,"add");
                startActivity(addretention);

            }
        });
       // prepareMovieData();
        if(Helpers.isNetworkAvailable(ConversionRetentionActivity.this)) {
           new  GetConversionRetention().execute();
        }
        else
        {
            Helpers.noConnectivityPopUp(ConversionRetentionActivity.this);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ConversionRetentionActivity.this);
        recyclerView.setLayoutManager(layoutManager);


        et_Search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if(et_Search.hasFocus()) {
                    adapter.filter(cs.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // Toast.makeText(getApplicationContext(),"before text change",Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                //Toast.makeText(getApplicationContext(),"after text change",Toast.LENGTH_LONG).show();
            }
        });


    }
    private void prepareMovieData() {
        Conversion c = new Conversion();

        c.setDate("16-Nov-2021");
        c.setStatus("Pending");
        c.setFarmername("Asif naveed");
        c.setParentactivity("FarmVisit");

        Conversion c2 = new Conversion();

        c2.setDate("16-Nov-2021");
        c2.setStatus("Closed");
        c2.setFarmername("Asif naveed");
        c2.setParentactivity("FarmVisit");

        Conversion c3= new Conversion();

        c3.setDate("16-Nov-2021");
        c3.setStatus("Pending");
        c3.setFarmername("Asif naveed");
        c3.setParentactivity("FarmVisit");
        planList.add(c);
        planList.add(c2);
        planList.add(c3);
    }

    @Override
    public void onClick(View view, int position) {
        final Conversion plan = planList.get(position);
        Intent addretention = new Intent(ConversionRetentionActivity.this,ConverionRetentionFormActivity.class);
        if(plan.getStatus().equals("Pending")) {
            sHelper.setString(Constants.FARMER_CONVERSION_RETENTION, "edit");
        }
        else if(plan.getStatus().equals("Closed"))
        {
            sHelper.setString(Constants.FARMER_CONVERSION_RETENTION, "close");
        }
        sHelper.setString(Constants.FARMER_CONVERSION_PARENT_ACTIVITY,plan.getParentactivity());
        sHelper.setString(Constants.FARMER_CONVERSION_FARMER_NAME,plan.getFarmername());
        sHelper.setString(Constants.FARMER_CONVERSION_PARENT_ACTIVITY_ID,plan.getActivityid());
        sHelper.setString(Constants.FARMER_CONVERSION_PARENT_CROP,plan.getCrop());
        sHelper.setString(Constants.FARMER_CONVERSION_PARENT_PRODUCT,plan.getProduct());
        sHelper.setString(Constants.FARMER_CONVERSION_PARENT_ZERKHAIZ_USER,plan.getZerkhaiz_user());
        sHelper.setString(Constants.FARMER_CONVERSION_PARENT_TOTAL_FARM_SIZE,plan.getTotal_farm_size());
        sHelper.setString(Constants.FARMER_CONVERSION_PARENT_CROP_ACREAGE,plan.getCrop_Acerage());
        sHelper.setString(Constants.FARMER_CONVERSION_PARENT_RECOMMENDED_DOSAGE,plan.getRec_dose());
        sHelper.setString(Constants.FARMER_CONVERSION_PARENT_RECOMMENDED_ACERAGE,plan.getRec_acerage());
        sHelper.setString(Constants.FARMER_CONVERSION_PARENT_RECOMMENDE_PRODUCT_QUANITY,plan.getRec_product_quantity());
        sHelper.setString(Constants.FARMER_CONVERSION_PARENT_ACTIVITY_DATE,plan.getDate());
        sHelper.setString(Constants.FARMER_CONVERSION_PARENT_REMAKRS,plan.getRemarks());
        sHelper.setString(Constants.FARMER_CONVERSION_PARENT_ADDRESS,plan.getAddress());
        sHelper.setString(Constants.FARMER_CONVERSION_PARENT_STATUS,plan.getStatus());
        sHelper.setString(Constants.FARMER_CONVERSION_PARENT_ACTUAL_DOSAGE_PER_ACRE,plan.getActualdosageperacre());
        sHelper.setString(Constants.FARMER_CONVERSION_PARENT_ACTUAL_ACREAGE,plan.getActualacerage());
        sHelper.setString(Constants.FARMER_CONVERSION_PARENT_ACTUAL_PRODUCT_QUANTITY,plan.getActualproductQuantity());
        sHelper.setString(Constants.FARMER_CONVERSION_PARENT_ACERAGE_RETENTION,plan.getAcre_ret());
        sHelper.setString(Constants.FARMER_CONVERSION_PARENT_ACERAGE_RETENTION_PERCENT,plan.getAcre_ret_percent());
        sHelper.setString(Constants.FARMER_CONVERSION_PARENT_REASON,plan.getReason());
        sHelper.setString(Constants.FARMER_CONVERSION_ID,plan.getId());
        sHelper.setString(Constants.FARMER_CONVERSION_PARENT_SALES_POINT_CODE,plan.getSalespointcode());
        sHelper.setString(Constants.FARMER_CONVERSION_PARENT_ACTIVITY_NO,plan.getActivityno());
        startActivity(addretention);
        //Toast.makeText(ConversionRetentionActivity.this, ""+plan.getFarmername(), Toast.LENGTH_SHORT).show();
    }
    public class GetConversionRetention extends AsyncTask<String, Void, Void> {
        ProgressDialog pDialog;
        private HttpHandler httpHandler;
        String errorMessage = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ConversionRetentionActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.show();
            pDialog.setCancelable(false);

            //expandableListGroup.clear();

        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected Void doInBackground(String... Url) {
            planList.clear();
            String response = "";
            String getsupervsorsnapshot = Constants.FFM_FARMER_GET_CONVERSION_RETENTION;
            System.out.println("OUtlet Status URL : " + getsupervsorsnapshot);
            try {
                httpHandler = new HttpHandler(ConversionRetentionActivity.this);
                HashMap<String, String> headerParams = new HashMap<>();
                if(sHelper.getString(Constants.ACCESS_TOKEN)!=null  && !sHelper.getString(Constants.ACCESS_TOKEN).equals("")) {
                    headerParams.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
                }
                else
                {
                    headerParams.put(Constants.AUTHORIZATION, "Bearer " + extraHelper.getString(Constants.ACCESS_TOKEN));
                }
               // headerParams.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
                response = httpHandler.httpGet(getsupervsorsnapshot, headerParams);
                Log.e("Assigned Sales Point", getsupervsorsnapshot);
                Log.e("Response", response);
                Type journeycodeType = new TypeToken<ArrayList<GetConversionRetentionOutput>>() {
                }.getType();
                List<GetConversionRetentionOutput> journeycode = new Gson().fromJson(response, journeycodeType);
                //JourneyPlanOutPut journeycode = new Gson().fromJson(response, JourneyPlanOutPut.class);
                if (response != null) {
                    if (journeycode.size() > 0) {
                        for (int j = 0; j < journeycode.size(); j++) {
                            Conversion c = new Conversion();
                            c.setParentactivity(journeycode.get(j).getParentActivity() == null || journeycode.get(j).getParentActivity().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getParentActivity().toString());
                            c.setFarmername(journeycode.get(j).getFarmerName() == null || journeycode.get(j).getFarmerName().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getFarmerName().toString());
                            c.setStatus(journeycode.get(j).getStatus() == null || journeycode.get(j).getStatus().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getStatus().toString());
                            c.setDate(journeycode.get(j).getActivityDate() == null || journeycode.get(j).getActivityDate().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getActivityDate().toString());
                            c.setActivityid(journeycode.get(j).getActivityId()== null || journeycode.get(j).getActivityId().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getActivityId().toString());
                            c.setCrop(journeycode.get(j).getCropName() == null || journeycode.get(j).getCropName().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getCropName().toString());
                            c.setProduct(journeycode.get(j).getProductName() == null || journeycode.get(j).getProductName().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getProductName().toString());
                            c.setZerkhaiz_user(journeycode.get(j).getZarkhezUser() == null || journeycode.get(j).getZarkhezUser().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getZarkhezUser().toString());
                            c.setTotal_farm_size(journeycode.get(j).getTotalFarmSize() == null || journeycode.get(j).getTotalFarmSize().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getTotalFarmSize().toString());
                            c.setCrop_Acerage(journeycode.get(j).getCropAcreage() == null || journeycode.get(j).getCropAcreage().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getCropAcreage().toString());
                            c.setRec_dose(journeycode.get(j).getRecommendedDosage() == null || journeycode.get(j).getRecommendedDosage().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getRecommendedDosage().toString());
                            c.setRec_acerage(journeycode.get(j).getRecommendedAcreage() == null || journeycode.get(j).getRecommendedAcreage().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getRecommendedAcreage().toString());
                            c.setRec_product_quantity(journeycode.get(j).getRecommendedProductQty() == null || journeycode.get(j).getRecommendedProductQty().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getRecommendedProductQty().toString());
                            c.setRemarks(journeycode.get(j).getRemarks() == null || journeycode.get(j).getRemarks().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getRemarks().toString());
                            c.setAddress(journeycode.get(j).getAddress() == null || journeycode.get(j).getAddress().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getAddress().toString());
                            c.setActualdosageperacre(journeycode.get(j).getActualDosagePerAcre() == null || journeycode.get(j).getActualDosagePerAcre().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getActualDosagePerAcre().toString());
                            c.setActualacerage(journeycode.get(j).getActualAcreage() == null || journeycode.get(j).getActualAcreage().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getActualAcreage().toString());
                            c.setActualproductQuantity(journeycode.get(j).getActualProductQty() == null || journeycode.get(j).getActualProductQty().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getActualProductQty().toString());
                            c.setAcre_ret(journeycode.get(j).getAcreageForRetention() == null || journeycode.get(j).getAcreageForRetention().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getAcreageForRetention().toString());
                            c.setAcre_ret_percent(journeycode.get(j).getAcreageRetentionPercentage() == null || journeycode.get(j).getAcreageRetentionPercentage().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getAcreageRetentionPercentage().toString());
                            c.setReason(journeycode.get(j).getReasonForDeviation() == null || journeycode.get(j).getReasonForDeviation().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getReasonForDeviation().toString());
                            c.setId(journeycode.get(j).getId() == null || journeycode.get(j).getId().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getId().toString());
                            c.setSalespointcode(journeycode.get(j).getFarmersalespointcode() == null || journeycode.get(j).getFarmersalespointcode().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getFarmersalespointcode().toString());
                            c.setActivityno(journeycode.get(j).getActivityCode() == null || journeycode.get(j).getActivityCode().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getActivityCode().toString());
                            planList.add(c);

                        }
                    } else {
                        Helpers.displayMessage(ConversionRetentionActivity.this, true, "no Data Found");
                    }
                }
            } catch (Exception exception) {
                if (response.equals("")) {
                    Helpers.displayMessage(ConversionRetentionActivity.this, true, exception.getMessage());
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
            if(planList.size()>0) {
                adapter = new ConversionRetentionAdapter(planList);
                recyclerView.setAdapter(adapter);
                adapter.setClickListener(ConversionRetentionActivity.this);
            }
            if (planList.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                txt_no_data.setVisibility(View.VISIBLE);

            } else {
                recyclerView.setVisibility(View.VISIBLE);
                txt_no_data.setVisibility(View.GONE);
            }



        }
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(ConversionRetentionActivity.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

}
