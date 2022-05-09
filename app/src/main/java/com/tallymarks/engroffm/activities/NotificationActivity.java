package com.tallymarks.engroffm.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.tallymarks.engroffm.R;
import com.tallymarks.engroffm.adapters.NotificationAdapter;
import com.tallymarks.engroffm.database.ExtraHelper;
import com.tallymarks.engroffm.database.SharedPrefferenceHelper;
import com.tallymarks.engroffm.models.NotificationModel;
import com.tallymarks.engroffm.models.getnotifications.NotificationOutput;
import com.tallymarks.engroffm.utils.Constants;
import com.tallymarks.engroffm.utils.Helpers;
import com.tallymarks.engroffm.utils.HttpHandler;
import com.tallymarks.engroffm.utils.ItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class NotificationActivity extends AppCompatActivity implements ItemClickListener {
    private TextView tvTopHeader, cartbadge;
    ImageView iv_menu, iv_back, iv_Notification;
    private ArrayList<NotificationModel> planList = new ArrayList<>();
    private RecyclerView recyclerView;
    SharedPrefferenceHelper sHelper;
    ExtraHelper extraHelper;
    ImageView iv_filter;
    EditText et_Search;
    NotificationAdapter adapter;
    private ProgressBar loadingPB;
    private NestedScrollView nestedSV;
    int page = 1, limit = 25;

    static {
        System.loadLibrary("native-lib");
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initView();

    }

    private void initView() {
        tvTopHeader = findViewById(R.id.tv_dashboard);
        cartbadge = findViewById(R.id.cart_badge);
        iv_Notification = findViewById(R.id.iv_notification);
        iv_menu = findViewById(R.id.iv_drawer);
        et_Search = findViewById(R.id.et_Search);
        sHelper = new SharedPrefferenceHelper(NotificationActivity.this);
        extraHelper = new ExtraHelper(NotificationActivity.this);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        iv_Notification.setVisibility(View.VISIBLE);
        cartbadge.setVisibility(View.VISIBLE);


        loadingPB = findViewById(R.id.idPBLoading);
        nestedSV = findViewById(R.id.idNestedSV);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // onBackPressed();
                Intent i = new Intent(NotificationActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        // adding on scroll change listener method for our nested scroll view.
        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    page++;
                    loadingPB.setVisibility(View.VISIBLE);
                    getDataFromAPI(page, limit);
                }
            }
        });


        tvTopHeader.setText("Notifications");
        // prepareMovieData();
        if (Helpers.isNetworkAvailable(NotificationActivity.this)) {
            getDataFromAPI(page,limit);

        } else {
            Helpers.noConnectivityPopUp(NotificationActivity.this);
        }


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(NotificationActivity.this);
        recyclerView.setLayoutManager(layoutManager);

    }
    private void getDataFromAPI(int page, int limit) {
        if(page>limit)
        {
            //Toast.makeText(this, "That's all the data..", Toast.LENGTH_SHORT).show();

            // hiding our progress bar.
            loadingPB.setVisibility(View.GONE);
            return;

        }
        new GetAllNotifications().execute();

    }


    @Override
    public void onClick(View view, int position) {
        final NotificationModel city = planList.get(position);


    }

    public class GetAllNotifications extends AsyncTask<String, Void, Void> {
        ProgressDialog pDialog;
        private HttpHandler httpHandler;
        String errorMessage = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(NotificationActivity.this);
//            pDialog.setMessage(getResources().getString(R.string.loading));
//            pDialog.setIndeterminate(false);
//            pDialog.show();
//            pDialog.setCancelable(false);

            //expandableListGroup.clear();

        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected Void doInBackground(String... Url) {
            String response = "";
            String getsupervsorsnapshot = Constants.FFM_GET_NOTIFICATIONS + "?page=" + page + "&size=" + limit;
            System.out.println("OUtlet Status URL : " + getsupervsorsnapshot);
            try {
                httpHandler = new HttpHandler(NotificationActivity.this);
                HashMap<String, String> headerParams = new HashMap<>();
                if (sHelper.getString(Constants.ACCESS_TOKEN) != null && !sHelper.getString(Constants.ACCESS_TOKEN).equals("")) {
                    headerParams.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
                } else {
                    headerParams.put(Constants.AUTHORIZATION, "Bearer " + extraHelper.getString(Constants.ACCESS_TOKEN));
                }
                // headerParams.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
                response = httpHandler.httpGet(getsupervsorsnapshot, headerParams);
                Log.e("Assigned Sales Point", getsupervsorsnapshot);
                Log.e("Response", response);
//                Type journeycodeType = new TypeToken<ArrayList<ChangeCustomerLocationOutput>>() {
//                }.getType();
//                List<ChangeCustomerLocationOutput> journeycode = new Gson().fromJson(response, journeycodeType);
                NotificationOutput journeycode = new Gson().fromJson(response, NotificationOutput.class);
                if (journeycode != null) {
                    cartbadge.setText(String.valueOf(journeycode.getTotalElements()));
                    if (journeycode.getData().size() > 0) {
                        for (int j = 0; j < journeycode.getData().size(); j++) {
                            NotificationModel plan4 = new NotificationModel();
                            plan4.setTitle(journeycode.getData().get(j).getC2a() == null || journeycode.getData().get(j).getC2a().equals("") ? getString(R.string.not_applicable) : journeycode.getData().get(j).getC2a() .toString());
                            plan4.setBody(journeycode.getData().get(j).getText() == null || journeycode.getData().get(j).getText() .equals("") ? getString(R.string.not_applicable) : journeycode.getData().get(j).getText() .toString());
                            String date = journeycode.getData().get(j).getCreatedDate()== null || journeycode.getData().get(j).getCreatedDate().equals("") ? getString(R.string.not_applicable) : journeycode.getData().get(j).getCreatedDate().toString();
                            plan4.setCreated_date(Helpers.getDatefromMilis2(date));
                            planList.add(plan4);
                            // plan4.setDate(journeycode.get(j).getCustomerId()== null || journeycode.get(j).getCustomerId().equals("") ? getString(R.string.not_applicable) : journeycode.get(j).getCustomerId().toString());


                        }
                    }
                }


            } catch (Exception exception) {
                if (response.equals("")) {
                    Helpers.displayMessage(NotificationActivity.this, true, exception.getMessage());
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

          loadingPB.setVisibility(View.GONE);
            adapter = new NotificationAdapter(planList, NotificationActivity.this);
            recyclerView.setAdapter(adapter);


        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(NotificationActivity.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
