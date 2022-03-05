package com.tallymarks.ffmapp.activities;


import static com.tallymarks.ffmapp.database.MyDatabaseHandler.TODAY_FARMER_JOURNEY_PLAN;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.ExpandableListAdapter;
import com.tallymarks.ffmapp.adapters.SalesPlanViewPagerAdapter;
import com.tallymarks.ffmapp.adapters.VisitCustomerViewPagerAdapter;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.database.ExtraHelper;
import com.tallymarks.ffmapp.database.MyDatabaseHandler;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.MenuModel;
import com.tallymarks.ffmapp.models.addfarmerinput.AddFarmerInput;
import com.tallymarks.ffmapp.models.addfarmerinput.CroppingPattern;
import com.tallymarks.ffmapp.models.addfarmerinput.LandProfile;
import com.tallymarks.ffmapp.models.addfarmerinput.MarketPlayer;
import com.tallymarks.ffmapp.models.addfarmerinput.ServingDealer;
import com.tallymarks.ffmapp.models.assignedsalespoint.AssignedSalesPointOutput;
import com.tallymarks.ffmapp.models.forgetpasswordinput.ForgetPasswordInput;
import com.tallymarks.ffmapp.models.getallFarmersplanoutput.Activity;
import com.tallymarks.ffmapp.models.getallFarmersplanoutput.FarmerCheckIn;
import com.tallymarks.ffmapp.models.getallFarmersplanoutput.OtherProduct;
import com.tallymarks.ffmapp.models.getallFarmersplanoutput.Recommendation;
import com.tallymarks.ffmapp.models.getallFarmersplanoutput.Sampling;
import com.tallymarks.ffmapp.models.getallcustomersplanoutput.GetAllCustomersOutput;
import com.tallymarks.ffmapp.models.listofallcrops.ListofallCropsOutput;
import com.tallymarks.ffmapp.models.loginoutput.LoginOutput;
import com.tallymarks.ffmapp.models.outletstatusesoutput.OutletStatusOutput;
import com.tallymarks.ffmapp.models.todayjourneyplaninput.Commitment;
import com.tallymarks.ffmapp.models.todayjourneyplaninput.Invoice;
import com.tallymarks.ffmapp.models.todayjourneyplaninput.MarketIntel;
import com.tallymarks.ffmapp.models.todayjourneyplaninput.Order;
import com.tallymarks.ffmapp.models.todayjourneyplaninput.PreviousStock;
import com.tallymarks.ffmapp.models.todayjourneyplaninput.PreviousStockSnapshot;
import com.tallymarks.ffmapp.models.todayjourneyplaninput.StockSnapshot;
import com.tallymarks.ffmapp.models.todayjourneyplaninput.StockSold__1;
import com.tallymarks.ffmapp.models.todayjourneyplaninput.TodayCustomerPostInput;
import com.tallymarks.ffmapp.tasks.GetAllProductBrandByCategory;
import com.tallymarks.ffmapp.tasks.GetAssignedSalesPoint;
import com.tallymarks.ffmapp.tasks.GetCompanHeldBrandBasicList;
import com.tallymarks.ffmapp.tasks.GetCustomerFarmerHierarchy;
import com.tallymarks.ffmapp.tasks.GetFatmerTodayJourneyPlan;
import com.tallymarks.ffmapp.tasks.GetListofAllBrands;
import com.tallymarks.ffmapp.tasks.GetListofAllCrops;
import com.tallymarks.ffmapp.tasks.GetListofAllDepths;
import com.tallymarks.ffmapp.tasks.GetListofallMarketPlayers;
import com.tallymarks.ffmapp.tasks.GetListofallProductCategories;
import com.tallymarks.ffmapp.tasks.GetOutletStatus;
import com.tallymarks.ffmapp.tasks.GetlistofAllDistrict;
import com.tallymarks.ffmapp.tasks.GetlistofAllFertTypes;
import com.tallymarks.ffmapp.tasks.GetlistofallGenders;
import com.tallymarks.ffmapp.tasks.LoadCustomersAllJourneyPlan;
import com.tallymarks.ffmapp.tasks.LoadCustomersTodayJourneyPlan;
import com.tallymarks.ffmapp.tasks.LoadFarmersAllJourneyPlan;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.DialougeManager;
import com.tallymarks.ffmapp.utils.GpsTracker;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    public DrawerLayout drawer;
    private TextView tvTopHeader;
    SharedPrefferenceHelper sHelper;
    ExtraHelper extraHelper;
    NavigationView navigationView;
    TextView userName,cartbadge,lastPosted;
    ImageView iv_Menu, iv_Notification;
    ImageView iv_filter;
    final static int REQUEST_LOCATION = 199;
    private GoogleApiClient googleApiClient;
    GpsTracker gpsTracker;
    String username = "";
    String rolename="";
    DatabaseHandler db;
    MyDatabaseHandler mydb;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();
    TextView txt_farmer_Demo, txt_soil_logs, txt_logout, txt_post_data, txt_refersh,txt_user_guide;
    ImageView headerImage;
    String statuCustomer = "";
    String journeyType;

    Button btn_add_plan;
    TabLayout tabLayout;
  HalfGauge halfGauge;
    ViewPager viewPager;
   SalesPlanViewPagerAdapter viewPagerAdapter;

    GetCompanHeldBrandBasicList task1;
    GetListofAllCrops task2;
    GetlistofAllFertTypes task3;
    GetAllProductBrandByCategory task4;
    GetListofAllDepths task5;
    GetListofallMarketPlayers task6;
    GetAssignedSalesPoint task7;
    LoadCustomersTodayJourneyPlan task8;
    LoadCustomersAllJourneyPlan task9;
    LoadFarmersAllJourneyPlan task10;
    GetFatmerTodayJourneyPlan task11;
    static {
        System.loadLibrary("native-lib");
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        //expandableListView = findViewById(R.id.expandableListView);
        // prepareMenuData();
        // populateExpandableList();
    }

    private void initView() {
        tvTopHeader = findViewById(R.id.tv_dashboard);
        iv_Menu = findViewById(R.id.iv_drawer);
        iv_Notification = findViewById(R.id.iv_notification);
        cartbadge = findViewById(R.id.cart_badge);
        txt_farmer_Demo = findViewById(R.id.produc_demo);
        txt_soil_logs = findViewById(R.id.soil_logs);
        headerImage = findViewById(R.id.profile_image);
        txt_logout = findViewById(R.id.logout);
        txt_post_data = findViewById(R.id.txt_post);
        txt_refersh = findViewById(R.id.tvRefersh);
        txt_user_guide = findViewById(R.id.txt_user_guide);
        btn_add_plan = findViewById(R.id.btn_new_plan);
        iv_Menu.setVisibility(View.VISIBLE);
        iv_Notification.setVisibility(View.VISIBLE);
        cartbadge.setVisibility(View.VISIBLE);
        iv_filter = findViewById(R.id.iv_notification);
        userName = findViewById(R.id.userName);
        lastPosted = findViewById(R.id.lastPosted);

        tabLayout = (TabLayout) findViewById(R.id.tabs_sales_plan);
        viewPager = (ViewPager) findViewById(R.id.viewPager_sales_plan);
        if(sHelper.getString(Constants.LAST_POSTED)!=null && !sHelper.getString(Constants.LAST_POSTED).equals(""))
        {
            lastPosted.setText("Last Posted on "+sHelper.getString(Constants.LAST_POSTED));
        }








        txt_user_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://drive.google.com/drive/folders/1ZMYDz4-yls9tZVPsMVoBZwChhzuXYw0N")); // only used based on your example.

                String title = "Open With";
// Create intent to show the chooser dialog
                Intent chooser = Intent.createChooser(intent, title);

// Verify the original intent will resolve to at least one activity
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                }
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/file/d/10Hc6QYr5MizjKaaZ4FqukWo1DLeVnz16/view"));
//                startActivity(Intent.createChooser(browserIntent, "Open with"));
            }
        });

        iv_filter.setVisibility(View.VISIBLE);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("DASHBOARD");
        sHelper = new SharedPrefferenceHelper(MainActivity.this);
        extraHelper = new ExtraHelper(MainActivity.this);
        Log.e("token", String.valueOf(sHelper.getString(Constants.ACCESS_TOKEN)));
        gpsTracker = new GpsTracker(MainActivity.this);
        db = new DatabaseHandler(MainActivity.this);
        mydb = new MyDatabaseHandler(MainActivity.this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        txt_farmer_Demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent farmer = new Intent(MainActivity.this, FarmerDemoActivity.class);
                startActivity(farmer);
            }
        });
        txt_post_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Helpers.isNetworkAvailable(MainActivity.this)) {
                    postCustomerData();
                    postAddFarmerData();
                    postFarmerData();

                } else {
                    Helpers.noConnectivityPopUp(MainActivity.this);
                }

            }
        });
        txt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawers();
                clearDataConfirmationPopUp();

            }
        });
        txt_refersh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //customer tables
                HashMap<String, String> filter = new HashMap<>();
                filter.put(db.KEY_TODAY_JOURNEY_IS_VISITED, "Not Visited");
                filter.put(db.KEY_TODAY_JOURNEY_IS_POSTED, "0");
                db.deleteData(db.TODAY_JOURNEY_PLAN,filter);
                HashMap<String, String> filterorders = new HashMap<>();
                db.deleteData(db.TODAY_JOURNEY_PLAN_ORDERS,filterorders);
                HashMap<String, String> filterinvoice = new HashMap<>();
                db.deleteData(db.TODAY_JOURNEY_PLAN_ORDERS_INVOICES,filterinvoice);
                HashMap<String, String> filtersnapshot= new HashMap<>();
                db.deleteData(db.TODAY_JOURNEY_PLAN_PREVIOUS_SNAPSHOT,filtersnapshot);
                HashMap<String, String> filterstock = new HashMap<>();
                db.deleteData(db.TODAY_JOURNEY_PLAN_PREVIOUS_STOCK,filterstock);
                HashMap<String, String> filterdarmerdownalod = new HashMap<>();
                mydb.deleteData(mydb.DOWNLOADED_FARMER_DATA,filterdarmerdownalod);

                //farmer tables
                HashMap<String, String> filter2 = new HashMap<>();
                filter2.put(mydb.KEY_TODAY_JOURNEY_IS_VISITED, "Not Visited");
                filter2.put(mydb.KEY_TODAY_JOURNEY_IS_POSTED, "0");
                mydb.deleteData(mydb.TODAY_FARMER_JOURNEY_PLAN,filter2);
                loadrefereshdata();
               // loadCustomerData();
               // loadFarmerData();


            }
        });
        txt_soil_logs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent soil = new Intent(MainActivity.this, SOILSamplinLogsActivity.class);
                startActivity(soil);
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        iv_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.END);
            }
        });

        // requestMultiplePermissions();
        loadLoginData();
        loadRoles();
        loadAllData();
//        if(task1!=null && task2!=null && task3!=null
//                && task4!=null && task5!=null && task8!=null
//                && task6!=null && task7!=null && task9!=null
//                && task10!=null && task11!=null) {
//            if (AsyncTask.Status.FINISHED == task1.getStatus() && AsyncTask.Status.FINISHED == task2.getStatus()
//                    && AsyncTask.Status.FINISHED == task3.getStatus() && AsyncTask.Status.FINISHED == task4.getStatus()
//                    && AsyncTask.Status.FINISHED == task5.getStatus() && AsyncTask.Status.FINISHED == task6.getStatus()
//                    && AsyncTask.Status.FINISHED == task7.getStatus() && AsyncTask.Status.FINISHED == task8.getStatus()
//                    && AsyncTask.Status.FINISHED == task9.getStatus() && AsyncTask.Status.FINISHED == task10.getStatus()
//                    && AsyncTask.Status.FINISHED == task11.getStatus()) {
//                viewPagerAdapter = new SalesPlanViewPagerAdapter(getSupportFragmentManager());
//                viewPager.setAdapter(viewPagerAdapter);
//                tabLayout.setupWithViewPager(viewPager);
//            }
//        }
//        else
//        {
            viewPagerAdapter = new SalesPlanViewPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(viewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
       // }

        if(rolename.equals("Field Force Team"))
        {

          //  Menu nav_Menu = navigationView.getMenu();
           // nav_Menu.findItem(R.id.nav_conversion).setVisible(false);

        }
        else if(rolename.equals("CSD SK Farmer"))
        {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_conversion).setVisible(true);
            txt_soil_logs.setEnabled(false);
            txt_soil_logs.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_baseline_lock_24, 0, 0,0);

        }
        else if(rolename.equals("FieldAssistant"))
        {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_visitcustomer).setVisible(false);
            nav_Menu.findItem(R.id.nav_customerlocation).setVisible(false);
            nav_Menu.findItem(R.id.nav_supervisorsnapshot).setVisible(false);
            nav_Menu.findItem(R.id.nav_addnewfarmer).setVisible(false);
            nav_Menu.findItem(R.id.nav_addfarmermeeting).setVisible(false);
            nav_Menu.findItem(R.id.nav_editfarmer).setVisible(false);
            //nav_Menu.findItem(R.id.nav_conversion).setVisible(false);
            txt_farmer_Demo.setEnabled(false);
            txt_farmer_Demo.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_baseline_lock_24, 0, 0,0);

        }


        //new GetOutletStatus(MainActivity.this).execute();


//        new GetlistofallGenders(MainActivity.this).execute();
//        new GetlistofAllDistrict(MainActivity.this).execute();

//        new GetListofallProductCategories(MainActivity.this).execute();
        //      new LoadCustomersAllJourneyPlan(MainActivity.this).execute();

//        new GetListofAllBrands(MainActivity.this).execute();

//        new GetCustomerFarmerHierarchy(MainActivity.this).execute();
        //new GetFatmerTodayJourneyPlan(MainActivity.this).execute();
    }

    @Override
    public void onBackPressed() {

        exitPopup();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_visitcustomer:
                gpsTracker = new GpsTracker(MainActivity.this);
                if (gpsTracker.canGetLocation()) {
                    Intent visitCustomer = new Intent(MainActivity.this, VisitCustomerActivity.class);
                    startActivity(visitCustomer);

                } else {
                    DialougeManager.gpsNotEnabledPopup(MainActivity.this);
                }


                drawer.closeDrawers();

                return true;
            case R.id.nav_visitfarmers:
                gpsTracker = new GpsTracker(MainActivity.this);
                if (gpsTracker.canGetLocation()) {
                    Intent farmvisit = new Intent(MainActivity.this, VisitFarmerActivity.class);
                    startActivity(farmvisit);
                } else {
                    DialougeManager.gpsNotEnabledPopup(MainActivity.this);
                }

                drawer.closeDrawers();

                return true;
            case R.id.nav_downlaod_farmersdata:
                Intent downlaodFarmer = new Intent(MainActivity.this, DownloadFarmersDataActivity.class);
                startActivity(downlaodFarmer);
                drawer.closeDrawers();

//                Intent downlaodFarmer = new Intent(MainActivity.this, QualityofSalesCallActivity.class);
//                startActivity(downlaodFarmer);
//                drawer.closeDrawers();
                return true;
            case R.id.nav_customerlocation:
                gpsTracker = new GpsTracker(MainActivity.this);
                if (gpsTracker.canGetLocation()) {
                    Intent farmerdemo = new Intent(MainActivity.this, CustomerLocationActivity.class);
                    startActivity(farmerdemo);
                } else {
                    DialougeManager.gpsNotEnabledPopup(MainActivity.this);
                }


                drawer.closeDrawers();
                return true;

            case R.id.nav_supervisorsnapshot:
                Intent soilsampling = new Intent(MainActivity.this, SubOrdinatesActivity.class);
                startActivity(soilsampling);
                drawer.closeDrawers();
                return true;

            case R.id.nav_addnewfarmer:
                Intent salescall = new Intent(MainActivity.this, AddNewFarmerActivity.class);
                startActivity(salescall);
                drawer.closeDrawers();
                return true;
            case R.id.nav_editfarmer:
                Intent editfarmerdetail = new Intent(MainActivity.this, EditFarmerDetailActivity.class);
                startActivity(editfarmerdetail);
                drawer.closeDrawers();
                return true;
            case R.id.nav_conversion:

                Intent conversionRetention= new Intent(MainActivity.this, ConversionRetentionActivity.class);
                startActivity(conversionRetention);
                drawer.closeDrawers();
                return true;


            case R.id.nav_addfarmermeeting:

                Intent farmerMeeting = new Intent(MainActivity.this, FarmerMeetingActivity.class);
                startActivity(farmerMeeting);
                drawer.closeDrawers();
                return true;


        }


        return false;
    }

    public void checkStorageCompanyHeldBrand() {

        HashMap<String, String> map = new HashMap<>();


        map.put(db.KEY_ENGRO_BRANCH_ID, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.ENGRO_BRANCH, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
            }
            while (cursor.moveToNext());
        } else {
            if (Helpers.isNetworkAvailable(MainActivity.this)) {
               task1 =  new GetCompanHeldBrandBasicList(MainActivity.this);
               task1.execute();
            } else {
                Helpers.noConnectivityPopUp(MainActivity.this);
            }

        }


    }

    public void checkStorageCrops() {

        HashMap<String, String> map = new HashMap<>();


        map.put(db.KEY_CROP_ID, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.CROPS_LIST, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {


            }
            while (cursor.moveToNext());
        } else {
            if (Helpers.isNetworkAvailable(MainActivity.this)) {
               task2  =  new GetListofAllCrops(MainActivity.this);
               task2.execute();

            } else {
                Helpers.noConnectivityPopUp(MainActivity.this);
            }

        }


    }
    public void loadrefereshdata()
    {
        if (Helpers.isNetworkAvailable(MainActivity.this)) {
           new LoadCustomersTodayJourneyPlan(MainActivity.this).execute();
           new LoadCustomersAllJourneyPlan(MainActivity.this).execute();
            new LoadFarmersAllJourneyPlan(MainActivity.this).execute();
            new GetFatmerTodayJourneyPlan(MainActivity.this).execute();
        } else {
            Helpers.noConnectivityPopUp(MainActivity.this);
        }

    }
    public void loadCustomerData()
    {
        checkLoadTodayCustomerJourneyPlan();
        checkLoadAllCustomerJourneyPlan();
    }
    public void loadFarmerData()
    {
        checkFarmerAllJourneyPlan();
        checkFarmerTodayJourneyPlan();
    }

    public void loadAllData() {
        checkStorageCompanyHeldBrand();
        checkStorageCrops();
        checkStorageFertTypes();
        checkProductBrandGrouopByCategory();
        checkStorageDepth();
        checkMarketPlayers();
        checkAssignedSalesPoint();
        if(!rolename.equals("FieldAssistant")) {
            if(sHelper.getString(Constants.CUSTOMER_TODAY_PLAN_NOT_FOUND).equals("2")) {
                checkLoadTodayCustomerJourneyPlan();
            }
            if(sHelper.getString(Constants.CUSTOMER_ALL_PLAN_NOT_FOUND).equals("2")) {
                checkLoadAllCustomerJourneyPlan();
            }

        }
        checkFarmerAllJourneyPlan();
        if(sHelper.getString(Constants.FARMER_TODAY_PLAN_NOT_FOUND).equals("2")) {
            checkFarmerTodayJourneyPlan();
        }

    }
    public void  loadRoles()
    {
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_ROLE_NAME, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.ROLES, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                rolename = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_ROLE_NAME)));
                while (cursor.moveToNext()) ;
            }
            while (cursor.moveToNext());


        }
        else {

            rolename = extraHelper.getString(Constants.ROLE);
        }




    }

    public void loadLoginData() {
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_NAME, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.LOGIN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                username = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_NAME)));
                while (cursor.moveToNext()) ;
            }
            while (cursor.moveToNext());
            userName.setText(username);

        }
        else
        {
            userName.setText(extraHelper.getString(Constants.NAME));
        }
    }

    private boolean isUnpostedDataExist() {

        boolean flag = false;
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_TODAY_JOURNEY_IS_POSTED, "2");
        Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                flag = true;
            }
            while (cursor.moveToNext());
        } else {
            flag = false;
        }
        return flag;
    }

    private boolean isUnpostedAddFarmerDataExist() {

        boolean flag = false;
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_ADD_FARMER_MOBILE_NUMBER, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_FARMER_IS_POSTED, "0");
        Cursor cursor = db.getData(db.ADD_NEW_FARMER_POST_DATA, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                flag = true;
            }
            while (cursor.moveToNext());
        } else {
            flag = false;
        }
        return flag;
    }

    private boolean isDataSaved() {

        boolean flag = false;
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_TODAY_JOURNEY_IS_VISITED, "Visited");
        Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                flag = true;
                //   statuCustomer = cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_IS_VISITED));;
            }
            while (cursor.moveToNext());
        } else {
            flag = false;
        }
        return flag;

    }

    private boolean isDataSavedNotAvailable() {

        boolean flag = false;
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_TODAY_JOURNEY_IS_VISITED, "Not Available");
        Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                flag = true;
                //  statuCustomer = cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_IS_VISITED));;
            }
            while (cursor.moveToNext());
        } else {
            flag = false;
        }
        return flag;

    }


    public void checkStorageDepth() {

        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_SOIL_ID, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.SOIL_DEPTHS, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {


            }
            while (cursor.moveToNext());
        } else {
            if (Helpers.isNetworkAvailable(MainActivity.this)) {
               task5 =  new GetListofAllDepths(MainActivity.this);
               task5.execute();
            } else {
                Helpers.noConnectivityPopUp(MainActivity.this);
            }

        }


    }

    public void checkMarketPlayers() {

        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_MARKET_PLAYER_ID, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.MARKET_PLAYERS, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {


            }
            while (cursor.moveToNext());
        } else {
            if (Helpers.isNetworkAvailable(MainActivity.this)) {
               task6 =  new GetListofallMarketPlayers(MainActivity.this);
               task6.execute();
            } else {
                Helpers.noConnectivityPopUp(MainActivity.this);
            }

        }


    }

    public void checkFarmerTodayJourneyPlan() {
        mydb = new MyDatabaseHandler(MainActivity.this);
        HashMap<String, String> map = new HashMap<>();
        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_ID, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(mydb.KEY_PLAN_TYPE, "TODAY");
        Cursor cursor = mydb.getData(mydb.TODAY_FARMER_JOURNEY_PLAN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {


            }
            while (cursor.moveToNext());
        } else {
            if(Helpers.isNetworkAvailable(MainActivity.this)) {
                task11 = new GetFatmerTodayJourneyPlan(MainActivity.this);
                    task11.execute();
            }
            else
            {
                Helpers.noConnectivityPopUp(MainActivity.this);
            }
        }

    }

    public void checkFarmerAllJourneyPlan() {

        mydb = new MyDatabaseHandler(MainActivity.this);
        HashMap<String, String> map = new HashMap<>();
        map.put(mydb.KEY_ALL_FARMER_JOURNEY_PLAN_SALES_POINT_CODE, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = mydb.getData(mydb.ALL_FARMER_JOURNEY_PLAN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {


            }
            while (cursor.moveToNext());
        } else {
            if(Helpers.isNetworkAvailable(MainActivity.this)) {
               task10 =  new LoadFarmersAllJourneyPlan(MainActivity.this);
               task10.execute();
            }
            else
            {
                Helpers.noConnectivityPopUp(MainActivity.this);
            }

        }


    }

    public void checkAssignedSalesPoint() {

        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_ASSIGNED_SALESPOINT_CODE, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.ASSIGNED_SALES_POINT, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {


            }
            while (cursor.moveToNext());
        } else {
            if (Helpers.isNetworkAvailable(MainActivity.this)) {
                task7 = new GetAssignedSalesPoint(MainActivity.this);
                task7.execute();
            } else {
                Helpers.noConnectivityPopUp(MainActivity.this);
            }

        }


    }

    public void checkProductBrandGrouopByCategory() {
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_PRODUCT_BRAND_CATEOGRY_NAME, "");

        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.PRODUCT_BRANDS_CATEGORY, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

            }
            while (cursor.moveToNext());
        } else {
            if (Helpers.isNetworkAvailable(MainActivity.this)) {
               task4  =  new GetAllProductBrandByCategory(MainActivity.this);
               task4.execute();
            } else {
                Helpers.noConnectivityPopUp(MainActivity.this);
            }

        }


    }

    public void checkStorageFertTypes() {

        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_FERT_ID, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.FERT_TYPES, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {


            }
            while (cursor.moveToNext());
        } else {
            if (Helpers.isNetworkAvailable(MainActivity.this)) {
               task3  =  new GetlistofAllFertTypes(MainActivity.this);
               task3.execute();

            } else {
                Helpers.noConnectivityPopUp(MainActivity.this);
            }


        }


    }

    public void checkLoadAllCustomerJourneyPlan() {

        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_TODAY_JOURNEY_TYPE, "all");
        Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

            }
            while (cursor.moveToNext());
        } else {
            if (Helpers.isNetworkAvailable(MainActivity.this)) {
               task9 =  new LoadCustomersAllJourneyPlan(MainActivity.this);
               task9.execute();
            } else {
                Helpers.noConnectivityPopUp(MainActivity.this);
            }

        }


    }

    private boolean isMyDataSavedNotAvailable() {

        boolean flag = false;
        HashMap<String, String> map = new HashMap<>();
        map.put(MyDatabaseHandler.KEY_TODAY_JOURNEY_FARMER_ID, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(mydb.KEY_TODAY_JOURNEY_IS_VISITED, "Not Available"); // not visited means 0
        Cursor cursor = mydb.getData(mydb.TODAY_FARMER_JOURNEY_PLAN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                flag = true;
                //  statuCustomer = cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_IS_VISITED));;
            }
            while (cursor.moveToNext());
        } else {
            flag = false;
        }
        return flag;

    }


    private boolean isMyDataSaved() {

        boolean flag = false;
        HashMap<String, String> map = new HashMap<>();
        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_ID, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(mydb.KEY_TODAY_JOURNEY_IS_VISITED, "Visited");
        Cursor cursor = mydb.getData(mydb.TODAY_FARMER_JOURNEY_PLAN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                flag = true;
            }
            while (cursor.moveToNext());
        } else {
            flag = false;
        }
        return flag;
    }

    private boolean isMyUnpostedDataExist() {

        boolean flag = false;
        HashMap<String, String> map = new HashMap<>();
        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_ID, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(mydb.KEY_TODAY_JOURNEY_IS_POSTED, "2");
        Cursor cursor = mydb.getData(mydb.TODAY_FARMER_JOURNEY_PLAN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                flag = true;
            }
            while (cursor.moveToNext());
        } else {
            flag = false;
        }
        return flag;
    }

    public void checkLoadTodayCustomerJourneyPlan() {

        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_TODAY_JOURNEY_TYPE, "today");
        Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {


            }
            while (cursor.moveToNext());
        } else {
            if (Helpers.isNetworkAvailable(MainActivity.this)) {
                task8 = new LoadCustomersTodayJourneyPlan(MainActivity.this);
                task8.execute();
            } else {
                Helpers.noConnectivityPopUp(MainActivity.this);
            }

        }


    }

    public void postFarmerData() {
        if (isMyUnpostedDataExist()) {
            if (isMyDataSaved() || isMyDataSavedNotAvailable())
                new PostSyncFarmer(statuCustomer).execute();
        }
    }


    public void postAddFarmerData() {
        if (isUnpostedAddFarmerDataExist()) {
            new PostAddFarmer().execute();
        }
    }

    public void postCustomerData() {
        if (isUnpostedDataExist()) {
            if (isDataSaved() || isDataSavedNotAvailable()) {
                new PostSyncCustomer(statuCustomer).execute();
            }
        }

//            else
//            {
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
//                alertDialogBuilder.setTitle(R.string.alert)
//                        .setMessage(R.string.postdataalert)
//                        .setCancelable(false)
//                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
//            }
//        } else {
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
//            alertDialogBuilder.setTitle(R.string.alert)
//                    .setMessage(R.string.postdataalert)
//                    .setCancelable(false)
//                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//            AlertDialog alertDialog = alertDialogBuilder.create();
//            alertDialog.show();
//
//
//        }
    }


    public void clearDataConfirmationPopUp() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder
                .setMessage(getResources().getString(R.string.clear_data_alert))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        new Logout().execute();
                       // Toast.makeText(MainActivity.this, "Clear Data Successfully", Toast.LENGTH_SHORT).show();


                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }
        );
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    //    private void prepareMenuData() {
//
//        MenuModel menuModel = new MenuModel("DASHBOARD", true, false); //Menu of Android Tutorial. No sub menus
//        headerList.add(menuModel);
//
//        if (!menuModel.hasChildren) {
//            childList.put(menuModel, null);
//        }
//        menuModel = new MenuModel("FIELD SALES", true, false); //Menu of Android Tutorial. No sub menus
//        headerList.add(menuModel);
//
//        if (!menuModel.hasChildren) {
//            childList.put(menuModel, null);
//        }
//
//        menuModel = new MenuModel("DEALERS", true, true); //Menu of Java Tutorials
//        headerList.add(menuModel);
//        List<MenuModel> childModelsList = new ArrayList<>();
//        MenuModel childModel = new MenuModel("DEALERS INFO", false, false);
//        childModelsList.add(childModel);
//        childModel = new MenuModel("ORDERS", false, false);
//        childModelsList.add(childModel);
//        childModel = new MenuModel("COMPLAINTS AND FEEDBACK", false, false);
//        childModelsList.add(childModel);
//        if (menuModel.hasChildren) {
//
//            childList.put(menuModel, childModelsList);
//        }
//
//        childModelsList = new ArrayList<>();
//        menuModel = new MenuModel("FARMERS", true, true); //Menu of Python Tutorials
//        headerList.add(menuModel);
//        childModel = new MenuModel("FARMERS INFO", false, false);
//        childModelsList.add(childModel);
//        childModel = new MenuModel("DEMO PLOTS", false, false);
//        childModelsList.add(childModel);
//        childModel = new MenuModel("SOIL ANALYSIS", false, false);
//        childModelsList.add(childModel);
//        if (menuModel.hasChildren) {
//            childList.put(menuModel, childModelsList);
//        }
//
//        menuModel = new MenuModel("SALES BOARD", true, false); //Menu of Android Tutorial. No sub menus
//        headerList.add(menuModel);
//
//        if (!menuModel.hasChildren) {
//            childList.put(menuModel, null);
//        }
//
//        menuModel = new MenuModel("INFO & SUPPORT", true, false); //Menu of Android Tutorial. No sub menus
//        headerList.add(menuModel);
//
//        if (!menuModel.hasChildren) {
//            childList.put(menuModel, null);
//        }
//
//    }
//
//    private void populateExpandableList() {
//
//        expandableListAdapter = new ExpandableListAdapter(this, headerList, childList);
//        expandableListView.setAdapter(expandableListAdapter);
//
//        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//
//                if (headerList.get(groupPosition).isGroup) {
//                    if (!headerList.get(groupPosition).hasChildren) {
//                        if(headerList.get(groupPosition).menuName.equals("SALES BOARD"))
//                        {
//                            Intent deal = new Intent(MainActivity.this,SalesBoard.class);
//                            startActivity(deal);
//                        }
//                        if(headerList.get(groupPosition).menuName.equals("INFO & SUPPORT"))
//                        {
//                            Intent deal = new Intent(MainActivity.this,SupportActivity.class);
//                            startActivity(deal);
//                        }
//                        if(headerList.get(groupPosition).menuName.equals("FIELD SALES"))
//                        {
//                            Intent deal = new Intent(MainActivity.this,FieldSalesActivity.class);
//                            startActivity(deal);
//                        }
//
//                        onBackPressed();
//                    }
//                }
//
//                return false;
//            }
//        });
//
//        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//
//                if (childList.get(headerList.get(groupPosition)) != null) {
//                    MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);
//                    if(model.menuName.equals("DEALERS INFO"))
//                    {
//                        Intent deal = new Intent(MainActivity.this,DealersActivity.class);
//                        startActivity(deal);
//                    }
//                    if(model.menuName.equals("FARMERS INFO"))
//                    {
//                        Intent deal = new Intent(MainActivity.this,FarmersActivity.class);
//                        startActivity(deal);
//                    }
//                    if(model.menuName.equals("SOIL ANALYSIS"))
//                    {
//                        Intent deal = new Intent(MainActivity.this,SoilAnalysis.class);
//                        startActivity(deal);
//                    }
//                    onBackPressed();
//
//                }
//
//                return false;
//            }
//        });
//    }
    private void requestMultiplePermissions() {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if (gpsTracker.canGetLocation()) {
                            } else {
                                enableLoc();
                            }

                            //Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(MainActivity.this).addApi(LocationServices.API).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {
                }

                @Override
                public void onConnectionSuspended(int i) {
                    googleApiClient.connect();
                }
            }).addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {
                    Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                }
            }).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(1 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);
            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    //final LocationSettingsStates state = result.getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can initialize location
                            // requests here.
                            //...
                            //GetLastLocation();
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(
                                        MainActivity.this,
                                        REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            //...
                            break;
                    }

                }
            });
        }
    }

    private void exitPopup() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder
                .setMessage(getResources().getString(R.string.exit_message))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.finishAffinity(MainActivity.this);
                        finish();
                        moveTaskToBack(true);
                        System.exit(0);

                    }
                }).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }
        );
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private class PostSyncCustomer extends AsyncTask<String, Void, Void> {

        String response = null;
        String status = "";
        String message = "";
        String errorMessage = "";
        ProgressDialog pDialog;
        private HttpHandler httpHandler;
        String customerId = "";
        String visitStatus;

        PostSyncCustomer(String status) {
            this.visitStatus = status;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected Void doInBackground(String... Url) {

            System.out.println("Post Outlet URl" + Constants.POST_TODAY_CUSTOMER_JOURNEY_PLAN);
            ArrayList<TodayCustomerPostInput> inputCollection = new ArrayList<>();
            Gson gson = new Gson();
            HashMap<String, String> headerParams = new HashMap<>();
            headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, "");
            headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_CODE, "");
            headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_NAME, "");
            headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_LONGITUDE, "");
            headerParams.put(db.KEY_TODAY_JOURNEY_IS_VISITED, "");
            headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_LATITUDE, "");
            headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_DAY_ID, "");
            headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_JOURNEYPLAN_ID, "");
            headerParams.put(db.KEY_TODAY_JOURNEY_CUSTOMER_SALES_POINT_NAME, "");
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_TODAY_JOURNEY_IS_POSTED, "2");
            Cursor cursor2 = db.getData(db.TODAY_JOURNEY_PLAN, headerParams, filter);
            if (cursor2.getCount() > 0) {
                cursor2.moveToFirst();
                do {
                    TodayCustomerPostInput inputParameters = new TodayCustomerPostInput();
                    inputParameters.setCustomerCode(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_CODE)));
                    inputParameters.setCustomerId(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_ID)));
                    inputParameters.setCustomerName("" + Helpers.clean(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_NAME))));
                    inputParameters.setLatitude(Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_LATITUDE))));
                    inputParameters.setLongtitude(Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_LONGITUDE))));

                    if (!cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_JOURNEYPLAN_ID)).equals("NA")) {
                        inputParameters.setJourneyPlanId(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_JOURNEYPLAN_ID))));
                    } else {
                        inputParameters.setJourneyPlanId(null);
                    }
                    if (!cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_DAY_ID)).equals("NA")) {
                        inputParameters.setDayId(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_DAY_ID))));
                    } else {
                        inputParameters.setDayId(null);
                    }
                    inputParameters.setSalePointName("" + Helpers.clean(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_SALES_POINT_NAME))));
                    customerId = cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_ID));
                    if (Helpers.clean(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_IS_VISITED))).equals("Visited")) {
                        loadStartActviiyResult(inputParameters, customerId);
                        //LoadOrderforPosting(inputParameters, customerId);
                        // LoadPreviousSnapShotforPosting(inputParameters, customerId);
                        loadFloorStockforPosting(inputParameters, customerId);
                        loadLocationlast(inputParameters, customerId);
                        loadFloorStockSold(inputParameters, customerId);
                        loadMarketIntel(inputParameters, customerId);
                        loadCommitments(inputParameters, customerId);
                        loadProductDicussed(inputParameters, customerId);
                    } else {
                        loadStartActviiyResultforNotAvailable(inputParameters, customerId);
                        loadLocationlastNotAvaolable(inputParameters, customerId);

                    }
                    inputCollection.add(inputParameters);

                }
                while (cursor2.moveToNext());
                httpHandler = new HttpHandler(MainActivity.this);
                HashMap<String, String> headerParams2 = new HashMap<>();
                if(sHelper.getString(Constants.ACCESS_TOKEN)!=null  && !sHelper.getString(Constants.ACCESS_TOKEN).equals("")) {
                    headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
                }
                else
                {
                    headerParams2.put(Constants.AUTHORIZATION, "Bearer " + extraHelper.getString(Constants.ACCESS_TOKEN));
                }
                //headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
                HashMap<String, String> bodyParams = new HashMap<>();
                String output = gson.toJson(inputCollection);
                Log.e("postoutput", String.valueOf(output));
                //output = gson.toJson(inputParameters, SaveWorkInput.class);
                try {
                    response = httpHandler.httpPost(Constants.POST_TODAY_CUSTOMER_JOURNEY_PLAN, headerParams2, bodyParams, output);
                    if (response != null) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            status = String.valueOf(jsonObj.getString("success"));
                            message = String.valueOf(jsonObj.getString("message"));
                            if (status.equals("true")) {
                                updateOutletStatus();
                                // updateOutletStatusById(Helpers.clean(JourneyPlanActivity.selectedOutletId));
                                // Helpers.displayMessage(MainActivity.this, true, message);
                            }
                        } catch (JSONException e) {
                            if (response.equals("")) {
                                Helpers.displayMessage(MainActivity.this, true, e.getMessage());
                                //showResponseDialog( mContext.getResources().getString(R.string.alert),exception.getMessage());
                                //pDialog.dismiss();
                            } else {
                                JSONObject json = null;
                                try {
                                    json = new JSONObject(response);
                                    errorMessage = json.getString("message");
                                    String status = json.getString("success");
                                    if (status.equals("false")) {
                                        Helpers.displayMessage(MainActivity.this, true, errorMessage);
                                    }
                                } catch (JSONException exception) {
                                    exception.printStackTrace();

                                }
                                //Helpers.displayMessage(LoginActivity.this, true, exception.getMessage());
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            pDialog.dismiss();
            if (status.equals("true")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle(R.string.alert)
                        .setMessage("Data Posted Successfully")
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm aaa");
                                String currentDateandTime = sdf.format(new Date());
                                sHelper.setString(Constants.LAST_POSTED,currentDateandTime);
                                lastPosted.setText("Last Posted on "+sHelper.getString(Constants.LAST_POSTED));
                                //new PostSyncOutlet().execute();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

        }
    }

    private class PostAddFarmer extends AsyncTask<String, Void, Void> {

        String response = null;
        String status = "";
        String message = "";
        String errorMessage = "";
        ProgressDialog pDialog;
        private HttpHandler httpHandler;
        String farmerMobileNumber = "";
        String description = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected Void doInBackground(String... Url) {

            System.out.println("Post Outlet URl" + Constants.POST_NEW_FARMER);
            ArrayList<AddFarmerInput> inputCollection = new ArrayList<>();
            Gson gson = new Gson();
            HashMap<String, String> headerParams = new HashMap<>();
            headerParams.put(db.KEY_ADD_FARMER_MOBILE_NUMBER, "");
            headerParams.put(db.KEY_ADD_FARMER_SALEPOINT_cODE, "");
            headerParams.put(db.KEY_ADD_FARMER_GENDER_ID, "");
            headerParams.put(db.KEY_ADD_FARMER_LAT, "");
            headerParams.put(db.KEY_ADD_FARMER_LNG, "");
            headerParams.put(db.KEY_ADD_FARMER_CNIC_NUMBER, "");
            headerParams.put(db.KEY_ADD_FARMER_LANDLINE_NUMBER, "");
            headerParams.put(db.KEY_ADD_FARMER_LAST_NAME, "");
            headerParams.put(db.KEY_FARMER_TRANSACTION_TYPE, "");
            headerParams.put(db.KEY_ADD_FARMER_EMAL, "");
            headerParams.put(db.KEY_ADD_FARMER_FIRST_NAME, "");
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_FARMER_IS_POSTED, "0");
            Cursor cursor2 = db.getData(db.ADD_NEW_FARMER_POST_DATA, headerParams, filter);
            if (cursor2.getCount() > 0) {
                cursor2.moveToFirst();
                do {
                    AddFarmerInput inputParameters = new AddFarmerInput();
                    inputParameters.setTransactionType(cursor2.getString(cursor2.getColumnIndex(db.KEY_FARMER_TRANSACTION_TYPE)));
                    inputParameters.setCnicNumber(cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_CNIC_NUMBER)));
                    inputParameters.setEmail(cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_EMAL)));
                    inputParameters.setFirstName("" + Helpers.clean(cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_FIRST_NAME))));
                    inputParameters.setLastName("" + Helpers.clean(cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_LAST_NAME))));
                    inputParameters.setMobileNumber(cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_MOBILE_NUMBER)));
                    inputParameters.setLandlineNumber(cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_LANDLINE_NUMBER)));
                    // inputParameters.setLandlineNumber(cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_LANDLINE_NUMBER)));

                    if (cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_GENDER_ID)).equals("Male")) {
                        inputParameters.setGenderId(1);
                    } else {
                        inputParameters.setGenderId(0);
                    }
                    farmerMobileNumber = cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_MOBILE_NUMBER));
                    inputParameters.setSalesPointCode(cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_SALEPOINT_cODE)));
                    inputParameters.setLat(Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_LAT))));
                    inputParameters.setLng(Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_LNG))));
                    loadlandprofile(inputParameters, farmerMobileNumber);
                    loadservingDealers(inputParameters, farmerMobileNumber);
                    inputCollection.add(inputParameters);

                }
                while (cursor2.moveToNext());
                httpHandler = new HttpHandler(MainActivity.this);
                HashMap<String, String> headerParams2 = new HashMap<>();
                if(sHelper.getString(Constants.ACCESS_TOKEN)!=null  && !sHelper.getString(Constants.ACCESS_TOKEN).equals("")) {
                    headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
                }
                else
                {
                    headerParams2.put(Constants.AUTHORIZATION, "Bearer " + extraHelper.getString(Constants.ACCESS_TOKEN));
                }
               // headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
                HashMap<String, String> bodyParams = new HashMap<>();
                String output = gson.toJson(inputCollection);
                Log.e("postoutput", String.valueOf(output));
                //output = gson.toJson(inputParameters, SaveWorkInput.class);
                try {
                    response = httpHandler.httpPost(Constants.POST_NEW_FARMER, headerParams2, bodyParams, output);
                    if (response != null) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            status = String.valueOf(jsonObj.getString("success"));
                            message = String.valueOf(jsonObj.getString("message"));
                            description = String.valueOf(jsonObj.getString("description"));
                            if (status.equals("true")) {
                                updateAddFarmerStatus();
                                // updateOutletStatusById(Helpers.clean(JourneyPlanActivity.selectedOutletId));
                                // Helpers.displayMessage(MainActivity.this, true, message);
                            }
                        } catch (JSONException e) {
                            if (response.equals("")) {
                                Helpers.displayMessage(MainActivity.this, true, e.getMessage());
                                //showResponseDialog( mContext.getResources().getString(R.string.alert),exception.getMessage());
                                //pDialog.dismiss();
                            } else {
                                JSONObject json = null;
                                try {
                                    json = new JSONObject(response);
                                    errorMessage = json.getString("message");
                                    String status = json.getString("success");
                                    if (status.equals("false")) {
                                        Helpers.displayMessage(MainActivity.this, true, errorMessage);
                                    }
                                } catch (JSONException exception) {
                                    exception.printStackTrace();

                                }
                                //Helpers.displayMessage(LoginActivity.this, true, exception.getMessage());
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            pDialog.dismiss();
            if (status.equals("true")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle(R.string.alert)
                        .setMessage(description)
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm aaa");
                                String currentDateandTime = sdf.format(new Date());
                                sHelper.setString(Constants.LAST_POSTED,currentDateandTime);
                                lastPosted.setText("Last Posted on "+sHelper.getString(Constants.LAST_POSTED));
                                //new PostSyncOutlet().execute();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle(R.string.alert)
                        .setMessage(description)
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //new PostSyncOutlet().execute();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

        }
    }

    private class PostSyncFarmer extends AsyncTask<String, Void, Void> {

        String response = null;
        String status = "";
        String message = "";
        String discription = "";
        ProgressDialog pDialog;
        String jsonObject = "";
        private HttpHandler httpHandler;
        String farmerId = "";
        String visitStatus;

        PostSyncFarmer(String status) {
            this.visitStatus = status;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected Void doInBackground(String... Url) {

            String farmerIdToupdate = "";
            mydb = new MyDatabaseHandler(MainActivity.this);

            System.out.println("Post Outlet URl" + Constants.FFM_POST_FARMER_TODAY_JOURNEY_PLAN);
            ArrayList<FarmerCheckIn> inputCollection = new ArrayList<>();
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<>();
            map.put(mydb.KEY_TODAY_JOURNEY_FARMER_ID, "");
            map.put(mydb.KEY_TODAY_JOURNEY_FARMER_JOURNEYPLAN_ID, "");
            map.put(mydb.KEY_TODAY_JOURNEY_FARMER_NAME, "");
            map.put(mydb.KEY_TODAY_FARMER_MOBILE_NO, "");
            map.put(mydb.KEY_TODAY_JOURNEY_FARMER_SALES_POINT_NAME, "");
            map.put(mydb.KEY_TODAY_JOURNEY_FARMER_CODE, "");
            map.put(mydb.KEY_TODAY_JOURNEY_IS_VISITED, "");
            map.put(mydb.KEY_TODAY_JOURNEY_FARMER_DAY_ID, "");
            map.put(mydb.KEY_TODAY_JOURNEY_FARMER_LATITUDE, "");
            map.put(mydb.KEY_TODAY_JOURNEY_FARMER_LONGITUDE, "");
            HashMap<String, String> filters = new HashMap<>();
            filters.put(mydb.KEY_TODAY_JOURNEY_IS_POSTED, "2");
            Cursor cursor2 = mydb.getData(mydb.TODAY_FARMER_JOURNEY_PLAN, map, filters);
            if (cursor2.getCount() > 0) {
                cursor2.moveToFirst();
                do {

                    FarmerCheckIn farmerCheckIn = new FarmerCheckIn();
                    farmerCheckIn.setFarmerId(Integer.valueOf(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_ID))));
                    farmerIdToupdate = cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_ID)); // to update status of record
                    farmerId = cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_ID));
                    if (!cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_DAY_ID)).equals("NA")) {
                        farmerCheckIn.setDayId(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_DAY_ID))));
                    } else {
                        farmerCheckIn.setDayId(0);
                    }

                    if (!cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_JOURNEYPLAN_ID)).equals("NA")) {
                        farmerCheckIn.setJourneyPlanId(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_JOURNEYPLAN_ID))));
                    } else {
                        farmerCheckIn.setJourneyPlanId(null);
                    }

                    farmerCheckIn.setFarmerName(Helpers.clean(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_NAME))));

                    if (!cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_LATITUDE)).equals("NA") && !cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_LATITUDE)).equals("null")) {
                        farmerCheckIn.setLatitude(Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_LATITUDE))));
                    } else {
                        farmerCheckIn.setLatitude(0.0);
                    }
                    if (!cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_LONGITUDE)).equals("NA") && !cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_LONGITUDE)).equals("null")) {
                        farmerCheckIn.setLongtitude(Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_LONGITUDE))));
                    } else {
                        farmerCheckIn.setLongtitude(0.0);
                    }

                    farmerCheckIn.setMobileNo(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_FARMER_MOBILE_NO)));

                    farmerCheckIn.setSalesPoint(Helpers.clean(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_SALES_POINT_NAME))));
                    //farmerCheckIn.setDistance(0);

                    if (Helpers.clean(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_IS_VISITED))).equals("Visited")) {


                        myloadStartActviiyResult(farmerCheckIn, farmerId);
                        loadLocationlastFarmer(farmerCheckIn, farmerId);
                        if(rolename.equals("Field Force Team") || rolename.equals("FieldAssistant"))
                        {
                          loadActivityRoleWise(farmerCheckIn, farmerId);
                        }
                        else
                        {
                            loadActivity(farmerCheckIn, farmerId);
                        }
                        //loadActivity(farmerCheckIn, farmerId);
                        if(rolename.equals("Field Force Team") ||rolename.equals("FieldAssistant") ) {
                            loadSampling(farmerCheckIn, farmerId);
                        }
                        loadRecommendations(farmerCheckIn, farmerId);

                    } else {
                        myloadStartActviiyResultforNotAvailable(farmerCheckIn, farmerId);
                        myloadLocationlastNotAvaolable(farmerCheckIn, farmerId);

                    }

                    //farmerCheckIn.setCheckOutLatitude("");
                    //farmerCheckIn.setCheckOutLongitude("");
                    farmerId = cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_ID));
                    inputCollection.add(farmerCheckIn);

                }
                while (cursor2.moveToNext());
                httpHandler = new HttpHandler(MainActivity.this);
                HashMap<String, String> headerParams2 = new HashMap<>();
                if(sHelper.getString(Constants.ACCESS_TOKEN)!=null  && !sHelper.getString(Constants.ACCESS_TOKEN).equals("")) {
                    headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
                }
                else
                {
                    headerParams2.put(Constants.AUTHORIZATION, "Bearer " + extraHelper.getString(Constants.ACCESS_TOKEN));
                }
               // headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
                HashMap<String, String> bodyParams = new HashMap<>();
                String output = gson.toJson(inputCollection);
                //output = gson.toJson(inputParameters, SaveWorkInput.class);
                try {
                    response = httpHandler.httpPost(Constants.FFM_POST_FARMER_TODAY_JOURNEY_PLAN, headerParams2, bodyParams, output);
                    if (response != null) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            status = String.valueOf(jsonObj.getString("success"));
                            message = String.valueOf(jsonObj.getString("message"));
                            //discription = String.valueOf(jsonObj.getString("description"));

                           // Helpers.displayMessage(MainActivity.this, true, status);
                            System.out.println(status + " ---- " + message);
                            System.out.println(output);

                            //Toast.makeText(getApplicationContext(), status +" "+ message, Toast.LENGTH_SHORT).show();
                            if (status.equals("true")) {
                                //updateOutletStatus();
                                myUpdateOutletStatus();
                                // Helpers.displayMessage(MainActivity.this, true, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            //Helpers.displayMessage(MainActivity.this, true, "No Data Available");

            mydb.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            pDialog.dismiss();
            if (status.equals("true")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle(R.string.alert)
                        .setMessage("Data Posted Successfully")
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm aaa");
                                String currentDateandTime = sdf.format(new Date());
                                sHelper.setString(Constants.LAST_POSTED,currentDateandTime);
                                lastPosted.setText("Last Posted on "+sHelper.getString(Constants.LAST_POSTED));
                                //new PostSyncOutlet().execute();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

        }
    }

    private void myloadLocationlastNotAvaolable(FarmerCheckIn farmerCheckIn, String farmerId) {
        HashMap<String, String> map = new HashMap<>();

        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_LATITUDE, "");
        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_LONGITUDE, "");
        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_TIMESTAMP, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(mydb.KEY_TODAY_FARMER_ID, farmerId);
        Cursor cursor2 = mydb.getData(mydb.TODAY_JOURNEY_PLAN_POST_DATA, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                farmerCheckIn.setCheckOutLatitude(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_LATITUDE)));
                farmerCheckIn.setCheckOutLongitude(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_LONGITUDE)));
                farmerCheckIn.setCheckOutTimeStamp(Long.parseLong(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_TIMESTAMP))));

            }
            while (cursor2.moveToNext());
        }

    }

    private void myloadStartActviiyResultforNotAvailable(FarmerCheckIn thisfarmerCheckIn, String thisfarmerId) {
        HashMap<String, String> mapForActivityResult = new HashMap<>();
        mapForActivityResult.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_TIME, "");
        mapForActivityResult.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_STATUS, "");
        mapForActivityResult.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_LATITUDE, "");
        mapForActivityResult.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_LONGITUDE, "");
        mapForActivityResult.put(mydb.KEY_PLAN_TYPE, "");

        HashMap<String, String> filtersforActivityResult = new HashMap<>();
        filtersforActivityResult.put(mydb.KEY_TODAY_FARMER_FARMER_ID, thisfarmerId);

        Cursor cursorActivity = mydb.getData(mydb.TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY, mapForActivityResult, filtersforActivityResult);
        if (cursorActivity.getCount() > 0) {
            cursorActivity.moveToFirst();
            do {
                journeyType = cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_PLAN_TYPE));
                thisfarmerCheckIn.setStatus(Integer.parseInt(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_STATUS))));
                thisfarmerCheckIn.setOutletStatusId(Integer.parseInt(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_STATUS))));
                thisfarmerCheckIn.setCheckInTimeStamp(Long.parseLong(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_TIME))));
                thisfarmerCheckIn.setCheckInLatitude(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_LATITUDE)));
                thisfarmerCheckIn.setCheckInLongitude(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_LONGITUDE)));

            }
            while (cursorActivity.moveToNext());
        }
    }

    private void myUpdateOutletStatus() {
        HashMap<String, String> params = new HashMap<>();
        params.put(mydb.KEY_TODAY_JOURNEY_IS_POSTED, "1");
        HashMap<String, String> filter = new HashMap<>();
        //filter.put(mydb.KEY_TODAY_JOURNEY_IS_POSTED, "0");
        filter.put(mydb.KEY_TODAY_JOURNEY_IS_POSTED, "2");
        mydb.updateData(mydb.TODAY_FARMER_JOURNEY_PLAN, params, filter);
    }

    private void updateOutletStatus() {
        HashMap<String, String> params = new HashMap<>();
        params.put(db.KEY_TODAY_JOURNEY_IS_POSTED, "1");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_IS_POSTED, "2");
        db.updateData(db.TODAY_JOURNEY_PLAN, params, filter);
    }

    private void updateAddFarmerStatus() {
        HashMap<String, String> params = new HashMap<>();
        params.put(db.KEY_FARMER_IS_POSTED, "1");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_FARMER_IS_POSTED, "0");
        db.updateData(db.ADD_NEW_FARMER_POST_DATA, params, filter);
    }

    private void LoadPreviousSnapShotforPosting(TodayCustomerPostInput inputParameters, String customerid) {
        int position;
        String categoryname = "";
        ArrayList<PreviousStockSnapshot> previoussnapshotList = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_SNAPSHOT_CATEGORY, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, customerid);
        filter.put(db.KEY_TODAY_JOURNEY_TYPE, journeyType);
        Cursor cursor2 = db.getData(db.TODAY_JOURNEY_PLAN_PREVIOUS_SNAPSHOT, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                PreviousStockSnapshot previousnapshot = new PreviousStockSnapshot();
                categoryname = cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_SNAPSHOT_CATEGORY));
                previousnapshot.setCategory("" + Helpers.clean(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_SNAPSHOT_CATEGORY))));
                previoussnapshotList.add(previousnapshot);
                position = cursor2.getPosition();
                previoussnapshotList.get(position).setPreviousStock(loadPreviousStockforPosting(categoryname));
                inputParameters.setPreviousStockSnapshot(previoussnapshotList);
            }
            while (cursor2.moveToNext());
        }
    }

    private void LoadOrderforPosting(TodayCustomerPostInput inputParameters, String customerid) {
        String orderid = "";
        int position;
        ArrayList<Order> orders = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TODAY_JOURNEY_ORDER_BRAND_NAME, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_NUMBER, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_ID, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_DATE, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_QUANTITY, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, customerid);
        filter.put(db.KEY_TODAY_JOURNEY_TYPE, journeyType);

        Cursor cursor2 = db.getData(db.TODAY_JOURNEY_PLAN_ORDERS, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                Order order = new Order();
                orderid = cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_ID));
                order.setId(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_ID))));
                order.setOrderNumber(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_NUMBER)));
                if (journeyType.equals("today")) {
                    order.setOrderDate(Long.parseLong(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_DATE))));
                } else {
                    //  order.setOrderDate(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_DATE)));
                }
                order.setOrderQuantity(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_QUANTITY))));
                order.setBrandName("" + Helpers.clean(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_BRAND_NAME))));
                orders.add(order);
                position = cursor2.getPosition();
                orders.get(position).setInvoices(loadInvociesforPosting(orderid));
                inputParameters.setOrders(orders);


            }
            while (cursor2.moveToNext());
        }
    }

    private ArrayList<PreviousStock> loadPreviousStockforPosting(String categoryname) {
        HashMap<String, String> map = new HashMap<>();

        ArrayList<PreviousStock> stockList = new ArrayList<>();
        map.put(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_NAME, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_COMPANYHELD, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_VISIT_DATE, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_ID, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_QUANTITY, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_SNAPSHOT_CATEGORY, categoryname);
        filter.put(db.KEY_TODAY_JOURNEY_TYPE, journeyType);
        Cursor cursor2 = db.getData(db.TODAY_JOURNEY_PLAN_PREVIOUS_STOCK, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                PreviousStock stock = new PreviousStock();
                stock.setCompanyHeld(Boolean.parseBoolean(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_COMPANYHELD))));
                stock.setId(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_ID))));
                stock.setName("" + Helpers.clean(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_NAME))));
                stock.setQuantity(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_QUANTITY))));
                stock.setVisitDate(Long.parseLong(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_PREVIOUS_STOCK_VISIT_DATE))));
                stockList.add(stock);
            }
            while (cursor2.moveToNext());
        }
        return stockList;
    }

    private ArrayList<Invoice> loadInvociesforPosting(String orderid) {
        HashMap<String, String> map = new HashMap<>();
        ArrayList<Invoice> invoiceList = new ArrayList<>();
        map.put(db.KEY_TODAY_JOURNEY_ORDER_INVOICE_NUMBER, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_INVOCIE_RATE, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_INVOICE_AVAILABLE_QUANITY, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_DISPATCH_DATE, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_DISPATCH_QUANTITY, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_ORDER_ID, orderid);
        filter.put(db.KEY_TODAY_JOURNEY_TYPE, journeyType);
        Cursor cursor2 = db.getData(db.TODAY_JOURNEY_PLAN_ORDERS_INVOICES, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                Invoice invoice = new Invoice();
                invoice.setInvoiceNumber(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_INVOICE_NUMBER)));
                if (journeyType.equals("today")) {
                    invoice.setDispatchDate(Long.parseLong(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_DISPATCH_DATE))));
                } else {

                    //   invoice.setDispatchDate(oncursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_DISPATCH_DATE))));
                }
                invoice.setDispatchQuantity(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_DISPATCH_QUANTITY))));
                invoice.setAvailableQuantity(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_INVOICE_AVAILABLE_QUANITY))));
                invoice.setInvoiceRate(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_INVOCIE_RATE)));
                invoiceList.add(invoice);
            }
            while (cursor2.moveToNext());
        }
        return invoiceList;
    }

    private void loadFloorStockforPosting(TodayCustomerPostInput inputParameters, String customerid) {
        HashMap<String, String> map = new HashMap<>();

        ArrayList<StockSnapshot> floorstockList = new ArrayList<>();
        map.put(db.KEY_TODAY_JOUNREY_PLAN_FLOOR_STOCK_INPUT_BRANDID, "");
        map.put(db.KEY_TODAY_JOUNREY_PLAN_FLOOR_STOCK_INPUT_BRANDQUANTITY, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, customerid);
        Cursor cursor2 = db.getData(db.TODAY_JOURNEY_PLAN_FLOOR_STOCK_INPUT, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                StockSnapshot stock = new StockSnapshot();
                stock.setBrandId(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOUNREY_PLAN_FLOOR_STOCK_INPUT_BRANDID))));
                Log.e("Quanity", String.valueOf(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOUNREY_PLAN_FLOOR_STOCK_INPUT_BRANDQUANTITY))));
                if (cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOUNREY_PLAN_FLOOR_STOCK_INPUT_BRANDQUANTITY)) != null && !cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOUNREY_PLAN_FLOOR_STOCK_INPUT_BRANDQUANTITY)).equals("")) {
                    stock.setQuantity(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOUNREY_PLAN_FLOOR_STOCK_INPUT_BRANDQUANTITY))));
                } else {
                    stock.setQuantity(0);
                }
                floorstockList.add(stock);
                inputParameters.setStockSnapshot(floorstockList);
            }
            while (cursor2.moveToNext());
        }

    }

    private void loadFloorStockSold(TodayCustomerPostInput inputParameters, String customerid) {
        HashMap<String, String> map = new HashMap<>();

        ArrayList<StockSold__1> stocksoldList = new ArrayList<>();
        map.put(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_QUANITYSOLD, "");
        map.put(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_NETSELLINGPRICE, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_NUMBER, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_INVOICE_NUMBER, "");
        map.put(db.KEY_TODAY_JOURNEY_ORDER_BRAND_NAME, "");
        map.put(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_SAMEINVOCIEE, "");
        map.put(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_OLD, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, customerid);
        Cursor cursor2 = db.getData(db.TODAY_JOURNEY_PLAN_MARKETPRICE_STOCK_SOLD, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                StockSold__1 stock = new StockSold__1();
                stock.setQuantitySold(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_QUANITYSOLD)));
                stock.setNetSellingPrice(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_NETSELLINGPRICE)));
                stock.setOrderNumber(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_NUMBER)));
                stock.setInvoiceNumber(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_INVOICE_NUMBER)));
                stock.setProductName("" + Helpers.clean(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_ORDER_BRAND_NAME))));
                if (cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_SAMEINVOCIEE)).equals("1")) {
                    stock.setSameInvoice(true);
                } else {
                    stock.setSameInvoice(false);
                }
                if (cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOUNREY_PLAN_MARKETPRICE_STOCK_SOLD_OLD)).equals("1")) {
                    stock.setOld(true);
                } else {
                    stock.setOld(false);
                }


                stocksoldList.add(stock);
                inputParameters.setStockSold(stocksoldList);
            }
            while (cursor2.moveToNext());
        }

    }

    private void loadMarketIntel(TodayCustomerPostInput inputParameters, String customerid) {
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_MARKET_INTEL_FORWARD, "");
        map.put(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_MARKET_INTETL_COMMENT, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, customerid);
        Cursor cursor2 = db.getData(db.TODAY_JOURNEY_PLAN_MARKET_INTEL, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                MarketIntel marketIntel = new MarketIntel();
                marketIntel.setComments(cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_MARKET_INTETL_COMMENT)));
                if (cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_MARKET_INTEL_FORWARD)).equals("1")) {
                    marketIntel.setDoForward(true);
                } else {
                    marketIntel.setDoForward(false);
                }

                inputParameters.setMarketIntel(marketIntel);
            }
            while (cursor2.moveToNext());
        }

    }

    private void loadCommitments(TodayCustomerPostInput inputParameters, String customerid) {
        HashMap<String, String> map = new HashMap<>();

        ArrayList<Commitment> commitmentList = new ArrayList<>();
        map.put(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_COMMITMENT_CONFIRMED, "");
        map.put(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_COMMITMENT_DELIVERY_DATE, "");
        map.put(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_COMMITMENT_QUANITY, "");
        map.put(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_COMMITMENT_RAND_ID, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, customerid);
        Cursor cursor2 = db.getData(db.TODAY_JOURNEY_PLAN_COMMITMENT_RECEIVED, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                Commitment commitment = new Commitment();
                commitment.setBrandId(cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_COMMITMENT_RAND_ID)));
                commitment.setQuantity(cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_COMMITMENT_QUANITY)));
                commitment.setDeliveryDate(cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_COMMITMENT_DELIVERY_DATE)));
                if (cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_COMMITMENT_CONFIRMED)).equals("1")) {
                    commitment.setConfirmed(true);
                } else {
                    commitment.setConfirmed(false);
                }
                commitmentList.add(commitment);
                inputParameters.setCommitments(commitmentList);
            }
            while (cursor2.moveToNext());
        }

    }

    private void loadProductDicussed(TodayCustomerPostInput inputParameters, String customerid) {
        HashMap<String, String> map = new HashMap<>();

        ArrayList<Integer> dicussedList = new ArrayList<>();
        map.put(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_PRODUCT_DICUSSED_ID, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, customerid);
        Cursor cursor2 = db.getData(db.TODAY_JOURNEY_PLAN_PRODUCT_DICUSSED, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                dicussedList.add(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_JOURNEY_PLAN_PRODUCT_DICUSSED_ID))));
                inputParameters.setProductsDiscussed(dicussedList);
            }
            while (cursor2.moveToNext());
        }

    }

    private void loadLocationlast(TodayCustomerPostInput inputParameters, String customerid) {
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKOUT_LATITUDE, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKOUT_LONGITUDE, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKOUT_TIMESTAMP, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_DISTANCE, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, customerid);
        Cursor cursor2 = db.getData(db.TODAY_JOURNEY_PLAN_POST_DATA, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                inputParameters.setCheckOutLatitude(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKOUT_LATITUDE)));
                inputParameters.setCheckOutLongitude(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKOUT_LONGITUDE)));
                inputParameters.setCheckOutTimeStamp(Long.parseLong(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKOUT_TIMESTAMP))));
                inputParameters.setDistance(Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_DISTANCE))));
            }
            while (cursor2.moveToNext());
        }

    }

    private void loadLocationlastNotAvaolable(TodayCustomerPostInput inputParameters, String customerid) {
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKOUT_LATITUDE, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKOUT_LONGITUDE, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKOUT_TIMESTAMP, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, customerid);
        Cursor cursor2 = db.getData(db.TODAY_JOURNEY_PLAN_POST_DATA, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                inputParameters.setCheckOutLatitude(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKOUT_LATITUDE)));
                inputParameters.setCheckOutLongitude(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKOUT_LONGITUDE)));
                inputParameters.setCheckOutTimeStamp(Long.parseLong(cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_CHECKOUT_TIMESTAMP))));

            }
            while (cursor2.moveToNext());
        }

    }

    private void getPlayer(String mobileNumber, ArrayList<ServingDealer> servingList, int position, String customerName) {
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_ADD_FARMER_SERVING_DEALER_MARKET_PLAYER_ENABLED, "");
        map.put(db.KEY_ADD_FARMER_SERVING_DEALER_MARKET_PLAYER_COMPANY_HELD, "");
        map.put(db.KEY_ADD_FARMER_SERVING_DEALER_MARKET_PLAYER_ID, "");
        map.put(db.KEY_ADD_FARMER_SERVING_DEALER_MARKET_PLAYER_CODE, "");
        map.put(db.KEY_ADD_FARMER_SERVING_DEALER_MARKET_PLAYER_DESCRIPTION, "");
        map.put(db.KEY_ADD_FARMER_SERVING_DEALER_MARKET_PLAYER_NAME, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_ADD_FARMER_MOBILE_NUMBER, mobileNumber);
        filter.put(db.KEY_ADD_FARMER_SERVING_DEALER_CUSTOMER_NAME, customerName);
        Cursor cursor2 = db.getData(db.ADD_NEW_FARMER_SERVING_DEALERS_MARKET_PLASYERS, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                MarketPlayer player = new MarketPlayer();
                player.setCompanyHeld(Boolean.parseBoolean(cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_SERVING_DEALER_MARKET_PLAYER_COMPANY_HELD))));
                player.setId(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_SERVING_DEALER_MARKET_PLAYER_ID))));
                player.setCode(cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_SERVING_DEALER_MARKET_PLAYER_CODE)));
                player.setName(Helpers.clean(cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_SERVING_DEALER_MARKET_PLAYER_NAME))));
                player.setDescription(Helpers.clean(cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_SERVING_DEALER_MARKET_PLAYER_DESCRIPTION))));
                if (cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_SERVING_DEALER_MARKET_PLAYER_ENABLED)).equals("NA")) {
                    player.setEnabled("");
                }
                servingList.get(position).setMarketPlayer(player);


            }
            while (cursor2.moveToNext());
        }

    }

    private ArrayList<CroppingPattern> loadlandprofileCropPattern(String mobileNumber,String salepointcode) {
        ArrayList<CroppingPattern> croppingPatternList = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_ADD_FARMER_LAND_PROFILE_CROPPING_PATTERN_CROP_ID, "");
        map.put(db.KEY_ADD_FARMER_LAND_PROFILE_CROPPING_PATTERN_LAND_HOLDING, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_ADD_FARMER_MOBILE_NUMBER, mobileNumber);
        filter.put(db.KEY_ADD_FARMER_LAND_PROFILE_SALES_POINT_CODE,salepointcode);
        Cursor cursor2 = db.getData(db.ADD_NEW_FARMER_LAND_PROFILE_CROPPING_PATTERN, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                CroppingPattern crop = new CroppingPattern();
                crop.setCropId(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_LAND_PROFILE_CROPPING_PATTERN_CROP_ID))));
                crop.setLandHolding(cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_LAND_PROFILE_CROPPING_PATTERN_LAND_HOLDING)));
                croppingPatternList.add(crop);

            }
            while (cursor2.moveToNext());
        }
        return croppingPatternList;
    }

    private void loadservingDealers(AddFarmerInput inputParameters, String mobileNumber) {
        String customerName = "";
        ArrayList<ServingDealer> servingList = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        int position;
        map.put(db.KEY_ADD_FARMER_SERVING_DEALER_CUSTOMER_CODE, "");
        map.put(db.KEY_ADD_FARMER_SERVING_DEALER_CUSTOMER_NAME, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_ADD_FARMER_MOBILE_NUMBER, mobileNumber);
        Cursor cursor2 = db.getData(db.ADD_NEW_FARMER_SERVING_DEALERS, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                ServingDealer serving = new ServingDealer();
                serving.setCustomerName(Helpers.clean(cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_SERVING_DEALER_CUSTOMER_NAME))));
                serving.setCustomerCode(cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_SERVING_DEALER_CUSTOMER_CODE)));
                customerName = Helpers.clean(cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_SERVING_DEALER_CUSTOMER_NAME)));
                servingList.add(serving);
                position = cursor2.getPosition();
                getPlayer(mobileNumber, servingList, position, customerName);
                // servingList.get(position).setMarketPlayer(getPlayer(mobileNumber));
                inputParameters.setServingDealers(servingList);

            }
            while (cursor2.moveToNext());
        }

    }

    private void loadlandprofile(AddFarmerInput inputParameters, String mobileNumber) {
        ArrayList<LandProfile> landProfileList = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        int position;
        String salePointCode = "";

        map.put(db.KEY_ADD_FARMER_LAND_PROFILE_SALES_POINT_NAME, "");
        map.put(db.KEY_ADD_FARMER_LAND_PROFILE_SIZE, "");
        map.put(db.KEY_ADD_FARMER_LAND_PROFILE_SALES_POINT_ID, "");
        map.put(db.KEY_ADD_FARMER_LAND_PROFILE_LANDMARKS, "");
        map.put(db.KEY_ADD_FARMER_LAND_PROFILE_OWNERSHIP, "");
        map.put(db.KEY_ADD_FARMER_LAND_PROFILE_WATERSOURCE, "");
        map.put(db.KEY_ADD_FARMER_LAND_PROFILE_SALES_POINT_CODE, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_ADD_FARMER_MOBILE_NUMBER, mobileNumber);
        Cursor cursor2 = db.getData(db.ADD_NEW_FARMER_LAND_PROFILE, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                LandProfile land = new LandProfile();
                land.setLandmark(Helpers.clean(cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_LAND_PROFILE_LANDMARKS))));
                land.setSize(cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_LAND_PROFILE_SIZE)));
                land.setSalesPointId(cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_LAND_PROFILE_SALES_POINT_ID)));
                land.setSalesPointName("" + Helpers.clean((cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_LAND_PROFILE_SALES_POINT_NAME)))));
                land.setOwnership(cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_LAND_PROFILE_OWNERSHIP)));
                land.setWaterSource(Helpers.clean(cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_LAND_PROFILE_WATERSOURCE))));
                land.setSalesPointCode(cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_LAND_PROFILE_SALES_POINT_CODE)));
                salePointCode = cursor2.getString(cursor2.getColumnIndex(db.KEY_ADD_FARMER_LAND_PROFILE_SALES_POINT_CODE));
                landProfileList.add(land);
                position = cursor2.getPosition();
                landProfileList.get(position).setCroppingPatterns(loadlandprofileCropPattern(mobileNumber,salePointCode));
                inputParameters.setLandProfiles(landProfileList);

            }
            while (cursor2.moveToNext());
        }
    }

    private void loadRecommendations(FarmerCheckIn thisFarmerCheckIn, String thisFarmerId) {
        // Mapping for recommendation
        //mydb = new MyDatabaseHandler(MainActivity.this);
        HashMap<String, String> mapForRecommendation = new HashMap<>();
        mapForRecommendation.put(mydb.KEY_TODAY_FARMMMER_ID, "");
        mapForRecommendation.put(mydb.KEY_TODAY_CROPID, "");
        mapForRecommendation.put(mydb.KEY_TODAY_FERTTYPE_ID, "");
        mapForRecommendation.put(mydb.KEY_TODAY_BRAND_ID, "");
        mapForRecommendation.put(mydb.KEY_TODAY_DOSAGE, "");

        HashMap<String, String> filtersforRecommendation = new HashMap<>();
        filtersforRecommendation.put(mydb.KEY_TODAY_FARMMMER_ID, thisFarmerId);

        ArrayList<Recommendation> recommendationList = new ArrayList<>();

        // for recommendations
        Cursor cursorRecommendations = mydb.getData(mydb.TODAY_FARMER_RECOMMENDATION, mapForRecommendation, filtersforRecommendation);
        if (cursorRecommendations.getCount() > 0) {
            cursorRecommendations.moveToFirst();
            do {
                // do for recommendation
                Recommendation recommendation = new Recommendation();
                recommendation.setCropId(cursorRecommendations.getString(cursorRecommendations.getColumnIndex(mydb.KEY_TODAY_CROPID)));
                recommendation.setFertAppTypeId(cursorRecommendations.getString(cursorRecommendations.getColumnIndex(mydb.KEY_TODAY_FERTTYPE_ID)));
                recommendation.setBrandId(cursorRecommendations.getString(cursorRecommendations.getColumnIndex(mydb.KEY_TODAY_BRAND_ID)));
                recommendation.setDosage(Integer.parseInt(cursorRecommendations.getString(cursorRecommendations.getColumnIndex(mydb.KEY_TODAY_DOSAGE))));
                recommendationList.add(recommendation);
            }
            while (cursorRecommendations.moveToNext());
        }

        thisFarmerCheckIn.setRecommendations(recommendationList);
        //return recommendationList;
    }

    private void loadSampling(FarmerCheckIn thisFarmerCheckIn, String thisFarmerId) {
        // Mapping for sampling
        HashMap<String, String> mapForSampleing = new HashMap<>();
        mapForSampleing.put(mydb.KEY_TODAY_FARMMMER_ID, "");
        mapForSampleing.put(mydb.KEY_TODAY_PREVIOUSCROP_ID, "");
        mapForSampleing.put(mydb.KEY_TODAY_DEPTH_ID, "");
        mapForSampleing.put(mydb.KEY_TODAY_CROP1_ID, "");
        mapForSampleing.put(mydb.KEY_TODAY_CROP2_ID, "");
        mapForSampleing.put(mydb.KEY_TODAY_PLOT_NUMBER, "");
        mapForSampleing.put(mydb.KEY_TODAY_BLOCK_NUMBER, "");
        mapForSampleing.put(mydb.KEY_TODAY_LATITUTE, "");
        mapForSampleing.put(mydb.KEY_TODAY_LONGITUTE, "");
        mapForSampleing.put(mydb.KEY_TODAY_REFRENCE, "");
        HashMap<String, String> filtersforSampling = new HashMap<>();
        filtersforSampling.put(mydb.KEY_TODAY_FARMMMER_ID, thisFarmerId);
        Sampling sampling;
        ArrayList<Sampling> samplingList = new ArrayList<>();
        Cursor cursorSampling = mydb.getData(mydb.TODAY_FARMER_SAMPLING, mapForSampleing, filtersforSampling);
        if (cursorSampling.getCount() > 0) {
            cursorSampling.moveToFirst();
            do {
                // do for sampling
                sampling = new Sampling();
                sampling.setPreviousCropId(cursorSampling.getString(cursorSampling.getColumnIndex(mydb.KEY_TODAY_PREVIOUSCROP_ID)));
                sampling.setDepthId(cursorSampling.getString(cursorSampling.getColumnIndex(mydb.KEY_TODAY_DEPTH_ID)));
                sampling.setCrop1Id(cursorSampling.getString(cursorSampling.getColumnIndex(mydb.KEY_TODAY_CROP1_ID)));
                sampling.setCrop2Id(cursorSampling.getString(cursorSampling.getColumnIndex(mydb.KEY_TODAY_CROP2_ID)));
                sampling.setPlotNumber(cursorSampling.getString(cursorSampling.getColumnIndex(mydb.KEY_TODAY_PLOT_NUMBER)));
                sampling.setBlockNumber(cursorSampling.getString(cursorSampling.getColumnIndex(mydb.KEY_TODAY_BLOCK_NUMBER)));
                sampling.setLatitude(cursorSampling.getString(cursorSampling.getColumnIndex(mydb.KEY_TODAY_LATITUTE)));
                sampling.setLongitude(cursorSampling.getString(cursorSampling.getColumnIndex(mydb.KEY_TODAY_LONGITUTE)));
                sampling.setReference(Helpers.clean(cursorSampling.getString(cursorSampling.getColumnIndex(mydb.KEY_TODAY_REFRENCE))));
                samplingList.add(sampling);
            }
            while (cursorSampling.moveToNext());
        }

        thisFarmerCheckIn.setSampling(samplingList);
        //return samplingList;
    }

    private void loadActivity(FarmerCheckIn thisfarmerCheckIn, String thisfarmerId) {
        HashMap<String, String> mapForActivity = new HashMap<>();
        mapForActivity.put(mydb.KEY_TODAY_FARMMMER_ID, "");
        mapForActivity.put(mydb.KEY_TODAY_CROPID, "");
        mapForActivity.put(mydb.KEY_TODAY_ADDRESS, "");
        mapForActivity.put(mydb.KEY_TODAY_MAIN_PRODUCT, "");
        mapForActivity.put(mydb.KEY_TODAY_REMARKS, "");
        mapForActivity.put(mydb.KEY_TODAY_CROP_DEF, "");
        mapForActivity.put(mydb.KEY_TODAY_CROP_ACE, "");
        mapForActivity.put(mydb.KEY_TODAY_OTHER_PRODUCT_LIQUIDATED, "");
        mapForActivity.put(mydb.KEY_TODAY_PACKS_LIQUIATED, "");
        mapForActivity.put(mydb.KEY_TODAY_SERVINGDEALERID, "");
        mapForActivity.put(mydb.KEY_TODAY_LATITUTE, "");
        mapForActivity.put(mydb.KEY_TODAY_LONGITUTE, "");

        HashMap<String, String> filtersforActivity = new HashMap<>();
        filtersforActivity.put(mydb.KEY_TODAY_FARMMMER_ID, thisfarmerId);

        Cursor cursorActivity = mydb.getData(mydb.TODAY_FARMER_ACTIVITY, mapForActivity, filtersforActivity);
        Activity activity = new Activity();
        if (cursorActivity.getCount() > 0) {
            cursorActivity.moveToFirst();
            do {
                // do for activity

                activity.setCropId(Integer.parseInt(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_CROPID))));
                activity.setAddress(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_ADDRESS)));
                activity.setMainProduct(Integer.parseInt(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_MAIN_PRODUCT))));
                activity.setRemarks(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_REMARKS)));
                activity.setLatitude(Double.parseDouble(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_LATITUTE))));
                activity.setLongitude(Double.parseDouble(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_LONGITUTE))));
                activity.setCropDeficiency(Integer.parseInt(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_CROP_DEF))));
                activity.setCropAcreage(Integer.parseInt(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_CROP_ACE))));
                activity.setCustomerId(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_SERVINGDEALERID)));
                activity.setPacksLiquidated(Integer.parseInt(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_PACKS_LIQUIATED))));
               // activity.setOtherPacksLiquidated(Integer.parseInt(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_OTHER_PRODUCT_LIQUIDATED))));
                activity.setOtherProducts(loadotherProducts(thisfarmerId));

                thisfarmerCheckIn.setActivity(activity);
            }
            while (cursorActivity.moveToNext());
        }

        //return activity;
    }

    private void loadActivityRoleWise(FarmerCheckIn thisfarmerCheckIn, String thisfarmerId) {
        HashMap<String, String> mapForActivity = new HashMap<>();
        mapForActivity.put(mydb.KEY_TODAY_FARMMMER_ID, "");
        mapForActivity.put(mydb.KEY_TODAY_CROPID, "");
        mapForActivity.put(mydb.KEY_TODAY_ADDRESS, "");
        mapForActivity.put(mydb.KEY_TODAY_MAIN_PRODUCT, "");
        mapForActivity.put(mydb.KEY_TODAY_REMARKS, "");

        mapForActivity.put(mydb.KEY_TODAY_LATITUTE, "");
        mapForActivity.put(mydb.KEY_TODAY_LONGITUTE, "");

        HashMap<String, String> filtersforActivity = new HashMap<>();
        filtersforActivity.put(mydb.KEY_TODAY_FARMMMER_ID, thisfarmerId);

        Cursor cursorActivity = mydb.getData(mydb.TODAY_FARMER_ACTIVITY, mapForActivity, filtersforActivity);
        Activity activity = new Activity();
        if (cursorActivity.getCount() > 0) {
            cursorActivity.moveToFirst();
            do {
                // do for activity

                activity.setCropId(Integer.parseInt(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_CROPID))));
                activity.setAddress(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_ADDRESS)));
                activity.setMainProduct(Integer.parseInt(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_MAIN_PRODUCT))));
                activity.setRemarks(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_REMARKS)));
                activity.setLatitude(Double.parseDouble(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_LATITUTE))));
                activity.setLongitude(Double.parseDouble(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_LONGITUTE))));
                thisfarmerCheckIn.setActivity(activity);
            }
            while (cursorActivity.moveToNext());
        }

        //return activity;
    }
    private ArrayList<OtherProduct> loadotherProducts(String farmerid) {
        ArrayList<OtherProduct> productsList = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();

        map.put(mydb.KEY_TODAY_OTHER_PACKS_ID, "");
        map.put(mydb.KEY_TODAY_OTHER_PACKS_LIQUIDATED, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(mydb.KEY_TODAY_FARMMER_ID, farmerid);
        Cursor cursor2 = mydb.getData(mydb.TODAY_FARMER_OTHERPACKS, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                OtherProduct prod = new OtherProduct();
                prod.setProductId(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_OTHER_PACKS_ID))));
                prod.setOtherPacksLiquidated(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_OTHER_PACKS_LIQUIDATED))));
                productsList.add(prod);

            }
            while (cursor2.moveToNext());
        }
        return productsList;
    }

    private void loadLocationlastFarmer(FarmerCheckIn thisfarmerCheckIn, String thisfarmerId) {
        HashMap<String, String> map = new HashMap<>();

        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_LATITUDE, "");
        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_LONGITUDE, "");
        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_TIMESTAMP, "");
        map.put(mydb.KEY_TODAY_JOURNEY_FARMER_DISTANCE, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(mydb.KEY_TODAY_FARMER_ID, thisfarmerId);
        Cursor cursor2 = mydb.getData(mydb.TODAY_JOURNEY_PLAN_POST_DATA, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                thisfarmerCheckIn.setCheckOutLatitude(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_LATITUDE)));
                thisfarmerCheckIn.setCheckOutLongitude(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_LONGITUDE)));
                thisfarmerCheckIn.setCheckOutTimeStamp(Long.parseLong(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_CHECKOUT_TIMESTAMP))));
                thisfarmerCheckIn.setDistance(Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(mydb.KEY_TODAY_JOURNEY_FARMER_DISTANCE))));
            }
            while (cursor2.moveToNext());
        }

    }


    private void myloadStartActviiyResult(FarmerCheckIn thisfarmerCheckIn, String thisfarmerId) {
        HashMap<String, String> mapForActivityResult = new HashMap<>();
        mapForActivityResult.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_TIME, "");

        mapForActivityResult.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_STATUS, "");
        mapForActivityResult.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_OBJECTIVE, "");
        mapForActivityResult.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_LATITUDE, "");
        mapForActivityResult.put(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_LONGITUDE, "");
        mapForActivityResult.put(mydb.KEY_PLAN_TYPE, "");

        HashMap<String, String> filtersforActivityResult = new HashMap<>();
        filtersforActivityResult.put(mydb.KEY_TODAY_FARMER_FARMER_ID, thisfarmerId);

        Cursor cursorActivity = mydb.getData(mydb.TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY, mapForActivityResult, filtersforActivityResult);
        if (cursorActivity.getCount() > 0) {
            cursorActivity.moveToFirst();
            do {
                journeyType = cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_PLAN_TYPE));
                thisfarmerCheckIn.setVisitObjective(Helpers.clean(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_OBJECTIVE))));
                thisfarmerCheckIn.setStatus(Integer.parseInt(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_STATUS))));
                thisfarmerCheckIn.setOutletStatusId(Integer.parseInt(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_STATUS))));
                thisfarmerCheckIn.setCheckInTimeStamp(Long.parseLong(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_TIME))));
                thisfarmerCheckIn.setCheckInLatitude(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_LATITUDE)));
                thisfarmerCheckIn.setCheckInLongitude(cursorActivity.getString(cursorActivity.getColumnIndex(mydb.KEY_TODAY_FARMER_JOURNEY_PLAN_START_ACTIVITY_LONGITUDE)));

            }
            while (cursorActivity.moveToNext());
        }
    }

    private void loadStartActviiyResult(TodayCustomerPostInput inputParameters, String customerid) {
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_TIME, "");
        map.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_STATUS, "");
        map.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_OBjECTIVE, "");
        map.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_LATITUDE, "");
        map.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_LONGITUDE, "");
        map.put(db.KEY_TODAY_JOURNEY_TYPE, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, customerid);
        Cursor cursor2 = db.getData(db.TODAY_JOURNEY_PLAN_START_ACTIVITY, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                journeyType = cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_TYPE));
                inputParameters.setCheckInLatitude(cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_LATITUDE)));
                inputParameters.setCheckInLongitude(cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_LONGITUDE)));
                inputParameters.setCheckInTimeStamp(Long.parseLong(cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_TIME))));
                inputParameters.setStatus(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_STATUS))));
                inputParameters.setVisitObjective("" + Helpers.clean(cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_OBjECTIVE))));
                inputParameters.setOutletStatusId(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_STATUS))));

            }
            while (cursor2.moveToNext());
        }

    }

    private void loadStartActviiyResultforNotAvailable(TodayCustomerPostInput inputParameters, String customerid) {
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_TIME, "");
        map.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_STATUS, "");
        map.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_LATITUDE, "");
        map.put(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_LONGITUDE, "");
        map.put(db.KEY_TODAY_JOURNEY_TYPE, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TODAY_JOURNEY_CUSTOMER_ID, customerid);
        Cursor cursor2 = db.getData(db.TODAY_JOURNEY_PLAN_START_ACTIVITY, map, filter);
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            do {
                journeyType = cursor2.getString(cursor2.getColumnIndex(db.KEY_TODAY_JOURNEY_TYPE));
                inputParameters.setCheckInLatitude(cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_LATITUDE)));
                inputParameters.setCheckInLongitude(cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_LONGITUDE)));
                inputParameters.setCheckInTimeStamp(Long.parseLong(cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_TIME))));
                inputParameters.setStatus(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_STATUS))));
                inputParameters.setOutletStatusId(Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(db.KEY_CUSTOMER_TODAY_PLAN_STARTACTIVITY_STATUS))));

            }
            while (cursor2.moveToNext());
        }

    }
    private class Logout extends AsyncTask<String, Void, Void> {

        String response = null;
        String status = "";
        String message = "";
        ProgressDialog pDialog;
        private HttpHandler httpHandler;
        String errorMessage = "";
        String email = "";
        //AlertDialog alertDialog;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected Void doInBackground(String... Url) {

            //System.out.println("Post Outlet URl" + Constants.POST_TODAY_CUSTOMER_JOURNEY_PLAN);
            Gson gson = new Gson();
            ForgetPasswordInput inputParameters = new ForgetPasswordInput();
            inputParameters.setEmail(email);
            httpHandler = new HttpHandler(MainActivity.this);
            HashMap<String, String> headerParams2 = new HashMap<>();
            if(sHelper.getString(Constants.ACCESS_TOKEN)!=null  && !sHelper.getString(Constants.ACCESS_TOKEN).equals("")) {
                headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
            }
            else
            {
                headerParams2.put(Constants.AUTHORIZATION, "Bearer " + extraHelper.getString(Constants.ACCESS_TOKEN));
            }
            //headerParams2.put(Constants.AUTHORIZATION, "Bearer " + sHelper.getString(Constants.ACCESS_TOKEN));
            HashMap<String, String> bodyParams = new HashMap<>();
            //String jsonObject = new Gson().toJson(inputParameters, ForgetPasswordInput.class);
           // Log.e("postoutput", String.valueOf(jsonObject));
            //output = gson.toJson(inputParameters, SaveWorkInput.class);
            try {
                response = httpHandler.httpPost(Constants.FFM_LOGOUT, headerParams2, bodyParams, null);
                if (response != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        status = String.valueOf(jsonObj.getString("success"));
                        message = String.valueOf(jsonObj.getString("message"));
                        if (status.equals("true")) {
                            MainActivity.this.deleteDatabase("FFMApplicationDataBasev5");
                            MainActivity.this.deleteDatabase("FFMAppDb_Zohaib_v5");
                            sHelper.clearPreferenceStore();
                            extraHelper.clearPreferenceStore();
                            Helpers.displayMessage(MainActivity.this, true, message);
                            Intent logout = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(logout);
                        }
                        else if(status.equals("false"))
                        {
                            Helpers.displayMessage(MainActivity.this, true, message);
                        }
                    } catch (JSONException e) {
                        if (response.equals("")) {
                            Helpers.displayMessage(MainActivity.this, true, e.getMessage());
                            pDialog.dismiss();
                            //showResponseDialog( mContext.getResources().getString(R.string.alert),exception.getMessage());
                            //pDialog.dismiss();
                        } else {
                            JSONObject json = null;
                            try {
                                json = new JSONObject(response);
                                errorMessage = json.getString("message");
                                String status = json.getString("success");
                                if (status.equals("false")) {
                                    Helpers.displayMessage(MainActivity.this, true, errorMessage);
                                    pDialog.dismiss();
                                }
                            } catch (JSONException exception) {
                                exception.printStackTrace();

                            }
                            //Helpers.displayMessage(LoginActivity.this, true, exception.getMessage());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            pDialog.dismiss();
            //alertDialog.dismiss();


        }
    }


    }