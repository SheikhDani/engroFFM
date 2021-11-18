package com.tallymarks.ffmapp.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.FarmersAdapter;
import com.tallymarks.ffmapp.adapters.SalesPointAdapter;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.models.Farmes;
import com.tallymarks.ffmapp.models.MarketPlayers;
import com.tallymarks.ffmapp.models.MarketPrice;
import com.tallymarks.ffmapp.models.SaelsPoint;
import com.tallymarks.ffmapp.models.addfarmerinput.MarketPlayer;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.DialougeManager;
import com.tallymarks.ffmapp.utils.GpsTracker;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddNewFarmerActivity extends AppCompatActivity {
    private TextView tvTopHeader, tvSelectedMarket, tvLat,tvLong;
    ImageView iv_menu, iv_back;
    ImageView img_add_serving, img_add_profile;
    LinearLayout linearLayoutList, LinearLayoutList2;
    EditText et_firstname, et_lastname, et_email, et_mobile, et_landline, et_cnic;
    Button btn_back;
    AutoCompleteTextView auto_gender;

    Button btn_creat;
    TextView txtFirstName, txtMobileNumber, txtMarket, txtSaveLocaton;
    GpsTracker gps;
    Button btnMarket;
    DatabaseHandler db;
    Button btnLocation;
    String currentlat = "", currentLng = "";
    EditText auto_company;
    EditText et_dealer;
    EditText et_size;
    TextView txt_selected_market;
    EditText et_land_holding;
    AutoCompleteTextView auto_crop;
    String playerid = "", playerdescriptipn= "", playercompanyheld="", playerenaled="";
    String saelspointcode= "", cropid= "" , farmersalespointcode="";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_farmer);
        initView();

    }

    private void initView() {
        tvTopHeader = findViewById(R.id.tv_dashboard);
        tvSelectedMarket = findViewById(R.id.txt_selected_market);
        auto_gender = findViewById(R.id.auto_gender);
        tvLat = findViewById(R.id.txt_lat);
        tvLong= findViewById(R.id.txt_lng);
        db = new DatabaseHandler(AddNewFarmerActivity.this);
        et_email = findViewById(R.id.et_email);
        et_firstname = findViewById(R.id.et_firstname);
        btnLocation = findViewById(R.id.btn_location);
        et_lastname = findViewById(R.id.et_lastname);
        btn_creat = findViewById(R.id.btn_create);
        et_mobile = findViewById(R.id.et_number);
        et_landline = findViewById(R.id.et_landline);
        et_cnic = findViewById(R.id.et_cnic);
        txtFirstName = findViewById(R.id.txt_first_name);
        txtMobileNumber = findViewById(R.id.txt_mobile_number);
        txtMarket = findViewById(R.id.txt_market);
        txtSaveLocaton = findViewById(R.id.txt_location);
        btnMarket = findViewById(R.id.btn_market);
        final String arraylist[] = {"Male", "Female"};
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.select_dialog_item, arraylist);
        auto_gender.setAdapter(arrayAdapter);
        auto_gender.setCursorVisible(false);
        auto_gender.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                auto_gender.showDropDown();
                String selection = arraylist[position];
                //auto_gender.setText(selection);
            }
        });
        auto_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                auto_gender.showDropDown();
            }
        });
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent map = new Intent(AddNewFarmerActivity.this, CustomMap.class);
//                startActivity(map);
                gps = new GpsTracker(AddNewFarmerActivity.this);
                if (gps.canGetLocation()) {
                    if (ActivityCompat.checkSelfPermission(AddNewFarmerActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddNewFarmerActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    currentlat = String.valueOf(gps.getLatitude());
                    currentLng = String.valueOf(gps.getLongitude());
                    tvLat.setText("Selected Lat:" + currentlat);
                    tvLong.setText("Selected Long:" + currentLng);

                } else {
                    tvLat.setText("Selected Lat: 0.0");
                    tvLong.setText("Selected Long: 0.0");
                }
            }
        });

       // tvLatLng.setText("Selected Lat, Lng: " + currentlat + "," + currentLng);
        img_add_serving = findViewById(R.id.img_add_serving);
        img_add_profile = findViewById(R.id.img_add_profile);
        linearLayoutList = (LinearLayout) findViewById(R.id.linear_layout_dynamic_serving);
        LinearLayoutList2 = (LinearLayout) findViewById(R.id.linear_layout_dynamic_profile);
        iv_menu = findViewById(R.id.iv_drawer);
        btn_back = findViewById(R.id.back);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.GONE);
        tvTopHeader.setVisibility(View.VISIBLE);
        tvTopHeader.setText("ADD NEW FARMER");
        SpannableStringBuilder firstName = setStarToLabel("First Name");
        SpannableStringBuilder mobileNumber = setStarToLabel("Mobile Number");
        SpannableStringBuilder market = setStarToLabel("Market");
        SpannableStringBuilder savelcoation = setStarToLabel("Save Location");
        txtFirstName.setText(firstName);
        txtMobileNumber.setText(mobileNumber);
        txtMarket.setText(market);
        txtSaveLocaton.setText(savelcoation);
        btnMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMarketDialouge(tvSelectedMarket);
            }
        });


        img_add_serving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addView();
            }
        });
        img_add_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addView2();
            }
        });
        btn_creat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInputs();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddNewFarmerActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddNewFarmerActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

    }

    @NonNull
    private SpannableStringBuilder setStarToLabel(String text) {
        String simple = text;
        String colored = "*";
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(simple);
        int start = builder.length();
        builder.append(colored);
        int end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    private void addView() {

        final View addView = getLayoutInflater().inflate(R.layout.list_serving, null, false);
       et_dealer = (EditText) addView.findViewById(R.id.et_dealer);
        auto_company = addView.findViewById(R.id.auto_select_company);
        TextView txt_company = (TextView) addView.findViewById(R.id.txt_select_company);
        TextView txt_dealer = (TextView) addView.findViewById(R.id.txt_dealer);
        TextView txt_headr = (TextView) addView.findViewById(R.id.tv_option);
        auto_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSalesPoint(auto_company);
            }
        });
        SpannableStringBuilder company = setStarToLabel("Select Company");
        txt_company.setText(company);

        SpannableStringBuilder dealer = setStarToLabel("Dealer Name");
        txt_dealer.setText(dealer);

        ImageView imgdel = (ImageView) addView.findViewById(R.id.img_commitment);

        imgdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(addView);
            }
        });
        linearLayoutList.addView(addView);
    }

    private void removeView(View v) {
        linearLayoutList.removeView(v);
    }

    private void addView2() {

        final View addView = getLayoutInflater().inflate(R.layout.list_profile, null, false);

        et_size = (EditText) addView.findViewById(R.id.et_size);
        EditText et_landmark = (EditText) addView.findViewById(R.id.et_landmark);
        AutoCompleteTextView auto_ownership = addView.findViewById(R.id.auto_owner_ship);
        AutoCompleteTextView auto_water_source = addView.findViewById(R.id.auto_water_soruce);
        Button btn_cropping = addView.findViewById(R.id.btn_cropping_pattern);
        Button btn_sales_point = addView.findViewById(R.id.btn_sales_point);
        final LinearLayout linear_crop = addView.findViewById(R.id.linear_layout_dynamic_cropping_pattern);
        TextView txt_size = (TextView) addView.findViewById(R.id.txt_size);
        TextView txt_water_source = (TextView) addView.findViewById(R.id.txt_water_Source);
        TextView txt_ownership = (TextView) addView.findViewById(R.id.txt_ownership);
        TextView txt_sales_point = (TextView) addView.findViewById(R.id.txt_sales_point);
        TextView txt_land_makr = (TextView) addView.findViewById(R.id.txt_landmarl);
        txt_selected_market = (TextView) addView.findViewById(R.id.txt_selected_market);

        SpannableStringBuilder size = setStarToLabel("Size(Acre)");
        txt_size.setText(size);

        SpannableStringBuilder salespoint = setStarToLabel("Sales Point");
        txt_sales_point.setText(salespoint);

        final String arraylist[] = {"Lease", "Own"};
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.select_dialog_item, arraylist);

        auto_ownership.setAdapter(arrayAdapter);
        auto_ownership.setCursorVisible(false);
        auto_ownership.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                auto_ownership.showDropDown();


            }
        });
        auto_ownership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                auto_ownership.showDropDown();
            }
        });

        final String arraylistwatersource[] = {"Canal", "Tube Well", "Both"};
        final ArrayAdapter<String> arrayAdapterwatersource = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, arraylistwatersource);
        auto_water_source.setAdapter(arrayAdapterwatersource);
        auto_water_source.setCursorVisible(false);
        auto_water_source.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                auto_water_source.showDropDown();


            }
        });
        auto_water_source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                auto_water_source.showDropDown();
            }
        });

        TextView txt_headr = (TextView) addView.findViewById(R.id.tv_option);


        ImageView imgdel = (ImageView) addView.findViewById(R.id.img_delete);

        imgdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView2(addView);
            }
        });
        btn_cropping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addView3(linear_crop);
            }
        });
        btn_sales_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMarketDialouge(txt_selected_market);
            }
        });
        LinearLayoutList2.addView(addView);
    }

    private void removeView2(View v) {
        LinearLayoutList2.removeView(v);
    }

    private void addView3(final LinearLayout linear) {

        final View addView = getLayoutInflater().inflate(R.layout.list_cropping, null, false);

        et_land_holding = (EditText) addView.findViewById(R.id.et_land_holding);
        auto_crop = addView.findViewById(R.id.auto_crop);
        TextView txt_crop = (TextView) addView.findViewById(R.id.txt_crop);
        TextView txt_land_holding = (TextView) addView.findViewById(R.id.txt_land_holding);
        TextView txt_headr = (TextView) addView.findViewById(R.id.tv_option);

        SpannableStringBuilder crop = setStarToLabel("Crop");
        txt_crop.setText(crop);

        SpannableStringBuilder land = setStarToLabel("Land Holding");
        txt_land_holding.setText(land);
        auto_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCrops(auto_crop);
            }
        });

        ImageView imgdel = (ImageView) addView.findViewById(R.id.img_delete);

        imgdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView3(addView, linear);
            }
        });
        linear.addView(addView);
    }

    private void removeView3(View v, LinearLayout linear) {
        linear.removeView(v);
    }

    public void openSalesPoint(EditText auto) {
        LayoutInflater li = LayoutInflater.from(AddNewFarmerActivity.this);
        View promptsView = li.inflate(R.layout.dialouge_sales_point, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddNewFarmerActivity.this);
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        final List<SaelsPoint> movieList = new ArrayList<>();
        final TextView title = promptsView.findViewById(R.id.tv_option);

        title.setText("Select Company");
        final EditText search = promptsView.findViewById(R.id.et_Search);
        final ImageView ivClsoe = promptsView.findViewById(R.id.iv_Close);
        ivClsoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        final RecyclerView recyclerView = promptsView.findViewById(R.id.recyclerView);
        prepareSalesData(movieList);
        final SalesPointAdapter mAdapter = new SalesPointAdapter(movieList, "farmerchild");
        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                SaelsPoint movie = movieList.get(position);
                auto.setText(movie.getPoint());
                alertDialog.dismiss();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (search.hasFocus()) {
                    mAdapter.filter(cs.toString());
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

        // prepareMovieData(mAdapter, movieList, salespoint);
        //ImageView ivClose = promptsView.findViewById(R.id.iv_close);

        alertDialogBuilder.setCancelable(false);
        alertDialog.show();
    }

    public void openCrops(AutoCompleteTextView auto) {
        LayoutInflater li = LayoutInflater.from(AddNewFarmerActivity.this);
        View promptsView = li.inflate(R.layout.dialouge_sales_point, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddNewFarmerActivity.this);
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        final List<SaelsPoint> movieList = new ArrayList<>();
        final TextView title = promptsView.findViewById(R.id.tv_option);

        title.setText("Crop");
        final EditText search = promptsView.findViewById(R.id.et_Search);
        final ImageView ivClsoe = promptsView.findViewById(R.id.iv_Close);
        ivClsoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        final RecyclerView recyclerView = promptsView.findViewById(R.id.recyclerView);
        prepareCropData(movieList);
        final SalesPointAdapter mAdapter = new SalesPointAdapter(movieList, "farmerchild");
        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                SaelsPoint movie = movieList.get(position);
                auto.setText(movie.getPoint());
                alertDialog.dismiss();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (search.hasFocus()) {
                    mAdapter.filter(cs.toString());
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

        // prepareMovieData(mAdapter, movieList, salespoint);
        //ImageView ivClose = promptsView.findViewById(R.id.iv_close);

        alertDialogBuilder.setCancelable(false);
        alertDialog.show();
    }

    private void prepareMovieData(SalesPointAdapter mAdapter, List<SaelsPoint> movieList, String from) {
        SaelsPoint movie = new SaelsPoint();
        if (from.equals("sales")) {
            movie.setPoint("ZOOO80-Adda Bonda Hayat_SP");
        } else {
            movie.setPoint("Engro Fetilizer Limited");
        }
        movieList.add(movie);

        SaelsPoint movie2 = new SaelsPoint();
        if (from.equals("sales")) {
            movie2.setPoint("ZOOO80-Adda Bonda Hayat_SP");
        } else {
            movie2.setPoint("Engro Fetilizer Limited");
        }
        movieList.add(movie2);

        SaelsPoint movie3 = new SaelsPoint();
        if (from.equals("sales")) {
            movie3.setPoint("ZOOO80-Adda Bonda Hayat_SP");
        } else {
            movie3.setPoint("Engro Fetilizer Limited");
        }
        movieList.add(movie3);

        SaelsPoint movie4 = new SaelsPoint();
        if (from.equals("sales")) {
            movie4.setPoint("ZOOO80-Adda Bonda Hayat_SP");
        } else {
            movie4.setPoint("Engro Fetilizer Limited");
        }
        movieList.add(movie4);


        // notify adapter about data set changes
        // so that it will render the list with new data
        mAdapter.notifyDataSetChanged();
    }

    private void validateInputs() {
        if (!(Helpers.isEmpty(getApplicationContext(), et_firstname))
                && !(Helpers.isEmpty(getApplicationContext(), et_mobile))
                && !(Helpers.isEmptyTextview(getApplicationContext(), tvSelectedMarket))
                && !currentlat.equals("") && !currentLng.equals("")
                && et_dealer!=null
                && auto_company!=null
                && et_size!=null
                && txt_selected_market!=null
                && et_land_holding!=null
                && auto_crop!=null
                && !(Helpers.isEmpty(getApplicationContext(), et_dealer))
                && !(Helpers.isEmpty(getApplicationContext(), auto_company))
                && !(Helpers.isEmpty(getApplicationContext(),  et_size))
                && !(Helpers.isEmptyTextview(getApplicationContext(),  txt_selected_market))
                && !(Helpers.isEmpty(getApplicationContext(),  et_land_holding))
                && !(Helpers.isEmptyAutoTextview(getApplicationContext(),  auto_crop))

        ) {
            customerSavedConfirmationPopUp();
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddNewFarmerActivity.this);
            alertDialogBuilder
                    .setMessage(getResources().getString(R.string.field_required_message))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();


                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
    public void customerSavedConfirmationPopUp() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddNewFarmerActivity.this);
        alertDialogBuilder
                .setMessage(AddNewFarmerActivity.this.getString(R.string.save_farmer))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     saveFarmer();
                     clearInputs();

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

    private void saveFarmer() {
        HashMap<String, String> headerParamsParent = new HashMap<>();
        headerParamsParent.put(db.KEY_ADD_FARMER_FIRST_NAME,et_firstname.getText().toString());
        headerParamsParent.put(db.KEY_FARMER_IS_POSTED,"0");
        headerParamsParent.put(db.KEY_ADD_FARMER_EMAL,et_email.getText().toString());
        headerParamsParent.put(db.KEY_ADD_FARMER_LAST_NAME,et_lastname.getText().toString());
        headerParamsParent.put(db.KEY_ADD_FARMER_MOBILE_NUMBER,et_mobile.getText().toString());
        headerParamsParent.put(db.KEY_ADD_FARMER_LANDLINE_NUMBER,et_landline.getText().toString());
        headerParamsParent.put(db.KEY_ADD_FARMER_CNIC_NUMBER,et_cnic.getText().toString());
        headerParamsParent.put(db.KEY_ADD_FARMER_GENDER_ID, auto_gender.getText().toString());
        getFarmerSalesPointData(txt_selected_market.getText().toString());
        headerParamsParent.put(db.KEY_ADD_FARMER_SALEPOINT_cODE,farmersalespointcode);
        headerParamsParent.put(db.KEY_ADD_FARMER_LAT,currentlat);
        headerParamsParent.put(db.KEY_FARMER_TRANSACTION_TYPE,"create");
        headerParamsParent.put(db.KEY_ADD_FARMER_LNG,currentLng);
        db.addData2(db.ADD_NEW_FARMER_POST_DATA, headerParamsParent);


        for (int i = 0; i < linearLayoutList.getChildCount(); i++) {
            Log.d("Child Count: ", String.valueOf(linearLayoutList.getChildCount()));
            View addView = linearLayoutList.getChildAt(i);
            EditText et_dealer = (EditText) addView.findViewById(R.id.et_dealer);
            HashMap<String, String> headerParams = new HashMap<>();
            headerParams.put(db.KEY_ADD_FARMER_SERVING_DEALER_CUSTOMER_CODE, null);
            headerParams.put(db.KEY_ADD_FARMER_EMAL, et_email.getText().toString());
            headerParams.put(db.KEY_ADD_FARMER_MOBILE_NUMBER, et_mobile.getText().toString());
            headerParams.put(db.KEY_ADD_FARMER_SERVING_DEALER_CUSTOMER_NAME, et_dealer.getText().toString());
            //for (int j = 0; j < linearLayoutList.getChildCount(); j++) {
   //             Log.d("Child Count: ", String.valueOf(linearLayoutList.getChildCount()));
   //             View addView2 = linearLayoutList.getChildAt(j);
                EditText auto_company = (EditText) addView.findViewById(R.id.auto_select_company);
                HashMap<String, String> headerParams2 = new HashMap<>();
                headerParams2.put(db.KEY_ADD_FARMER_EMAL, et_email.getText().toString());
                headerParams2.put(db.KEY_ADD_FARMER_SERVING_DEALER_CUSTOMER_NAME,et_dealer.getText().toString());
                headerParams2.put(db.KEY_ADD_FARMER_MOBILE_NUMBER, et_mobile.getText().toString());
                headerParams2.put(db.KEY_ADD_FARMER_SERVING_DEALER_MARKET_PLAYER_NAME, auto_company.getText().toString());
                getPlayersData(auto_company.getText().toString());
                headerParams2.put(db.KEY_ADD_FARMER_SERVING_DEALER_MARKET_PLAYER_CODE, "");
                headerParams2.put(db.KEY_ADD_FARMER_SERVING_DEALER_MARKET_PLAYER_ID, playerid);
                headerParams2.put(db.KEY_ADD_FARMER_SERVING_DEALER_MARKET_PLAYER_DESCRIPTION, playerdescriptipn);
                headerParams2.put(db.KEY_ADD_FARMER_SERVING_DEALER_MARKET_PLAYER_COMPANY_HELD, playercompanyheld);
                headerParams2.put(db.KEY_ADD_FARMER_SERVING_DEALER_MARKET_PLAYER_ENABLED, playerenaled);
                db.addData(db.ADD_NEW_FARMER_SERVING_DEALERS_MARKET_PLASYERS, headerParams2);
            //}
            db.addData(db.ADD_NEW_FARMER_SERVING_DEALERS, headerParams);
        }

        for (int i = 0; i < LinearLayoutList2.getChildCount(); i++) {
            Log.d("Child Count: ", String.valueOf(LinearLayoutList2.getChildCount()));

            View addView = LinearLayoutList2.getChildAt(i);
            EditText et_size = (EditText) addView.findViewById(R.id.et_size);
            EditText et_land = (EditText) addView.findViewById(R.id.et_landmark);
            AutoCompleteTextView auto_owner = addView.findViewById(R.id.auto_owner_ship);
            LinearLayout crop = (LinearLayout) addView.findViewById(R.id.linear_layout_dynamic_cropping_pattern);
            AutoCompleteTextView auto_watersource = addView.findViewById(R.id.auto_water_soruce);
            TextView txt_selected_Market = addView.findViewById(R.id.txt_selected_market);
            HashMap<String, String> headerParams = new HashMap<>();
            headerParams.put(db.KEY_ADD_FARMER_LAND_PROFILE_LANDMARKS, et_land.getText().toString());
            headerParams.put(db.KEY_ADD_FARMER_EMAL, et_email.getText().toString());
            headerParams.put(db.KEY_ADD_FARMER_MOBILE_NUMBER, et_mobile.getText().toString());
            headerParams.put(db.KEY_ADD_FARMER_LAND_PROFILE_WATERSOURCE, auto_watersource.getText().toString());
            headerParams.put(db.KEY_ADD_FARMER_LAND_PROFILE_OWNERSHIP, auto_owner.getText().toString());
            headerParams.put(db.KEY_ADD_FARMER_LAND_PROFILE_SALES_POINT_NAME, txt_selected_Market.getText().toString());
            headerParams.put(db.KEY_ADD_FARMER_LAND_PROFILE_SIZE, et_size.getText().toString());
            headerParams.put(db.KEY_ADD_FARMER_LAND_PROFILE_SALES_POINT_ID, null);
            getLandProfileData(txt_selected_Market.getText().toString());
            headerParams.put(db.KEY_ADD_FARMER_LAND_PROFILE_SALES_POINT_CODE, saelspointcode);
            db.addData(db.ADD_NEW_FARMER_LAND_PROFILE, headerParams);

            for (int j = 0; j < crop.getChildCount(); j++) {
                Log.d("Child Count: ", String.valueOf(crop.getChildCount()));
                View addView2 = crop.getChildAt(j);
                EditText et_land_holding = (EditText) addView2.findViewById(R.id.et_land_holding);
                AutoCompleteTextView auto_crop = addView2.findViewById(R.id.auto_crop);
                HashMap<String, String> headerParams2 = new HashMap<>();
                headerParams2.put(db.KEY_ADD_FARMER_EMAL, et_email.getText().toString());
                headerParams2.put(db.KEY_ADD_FARMER_MOBILE_NUMBER, et_mobile.getText().toString());
                getCropData(auto_crop.getText().toString());
                headerParams2.put(db.KEY_ADD_FARMER_LAND_PROFILE_SALES_POINT_CODE, saelspointcode);
                headerParams2.put(db.KEY_ADD_FARMER_LAND_PROFILE_CROPPING_PATTERN_CROP_ID, cropid);
                headerParams2.put(db.KEY_ADD_FARMER_LAND_PROFILE_CROPPING_PATTERN_LAND_HOLDING, et_land_holding.getText().toString());
                db.addData(db.ADD_NEW_FARMER_LAND_PROFILE_CROPPING_PATTERN, headerParams2);
            }
        }


    }
    private void getPlayersData(String name)
    {

        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_MARKET_PLAYER_ID, "");
        map.put(db.KEY_MARKET_PLAYER_DESCRIPTION, "");
        map.put(db.KEY_MARKET_PLAYER_COMPANY_HELD, "");
        map.put(db.KEY_MARKET_PLAYER_ENABLED, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_MARKET_PLAYER_NAME, name);
        Cursor cursor = db.getData(db.MARKET_PLAYERS, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                playerid = cursor.getString(cursor.getColumnIndex(db.KEY_MARKET_PLAYER_ID));
                playerdescriptipn = cursor.getString(cursor.getColumnIndex(db.KEY_MARKET_PLAYER_DESCRIPTION));
                playercompanyheld= cursor.getString(cursor.getColumnIndex(db.KEY_MARKET_PLAYER_COMPANY_HELD));
                playerenaled = cursor.getString(cursor.getColumnIndex(db.KEY_MARKET_PLAYER_ENABLED));


            }
            while (cursor.moveToNext());
        }
    }

    private void getLandProfileData(String name)
    {

        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_ASSIGNED_SALESPOINT_CODE, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_ASSIGNED_SALESPOINT_NAME, name);
        Cursor cursor = db.getData(db.ASSIGNED_SALES_POINT, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                saelspointcode = cursor.getString(cursor.getColumnIndex(db.KEY_ASSIGNED_SALESPOINT_CODE));
            }
            while (cursor.moveToNext());
        }
    }

    private void getFarmerSalesPointData(String name)
    {
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_ASSIGNED_SALESPOINT_CODE, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_ASSIGNED_SALESPOINT_NAME, name);
        Cursor cursor = db.getData(db.ASSIGNED_SALES_POINT, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                farmersalespointcode = cursor.getString(cursor.getColumnIndex(db.KEY_ASSIGNED_SALESPOINT_CODE));
            }
            while (cursor.moveToNext());
        }
    }


    private void getCropData(String name)
    {

        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_CROP_ID, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        filters.put(db.KEY_CROP_NAME, name);
        Cursor cursor = db.getData(db.CROPS_LIST, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                cropid = cursor.getString(cursor.getColumnIndex(db.KEY_CROP_ID));
            }
            while (cursor.moveToNext());
        }
    }

    private void clearInputs() {
        Toast.makeText(AddNewFarmerActivity.this, "Farmer Saved Successfully", Toast.LENGTH_SHORT).show();
      Intent move = new Intent(AddNewFarmerActivity.this,MainActivity.class);
      startActivity(move);

    }

    public void selectMarketDialouge(TextView tvSelectedMarket) {
        LayoutInflater li = LayoutInflater.from(AddNewFarmerActivity.this);
        View promptsView = li.inflate(R.layout.dialouge_sales_point, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddNewFarmerActivity.this);
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        final List<SaelsPoint> companyList = new ArrayList<>();
        final TextView title = promptsView.findViewById(R.id.tv_option);
        final EditText search = promptsView.findViewById(R.id.et_Search);
        final ImageView ivClsoe = promptsView.findViewById(R.id.iv_Close);
        ivClsoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        title.setText("SALES");
        final RecyclerView recyclerView = promptsView.findViewById(R.id.recyclerView);
        prepareCompanyData(companyList);
        final SalesPointAdapter mAdapter = new SalesPointAdapter(companyList, "farmer");

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                SaelsPoint companyname = companyList.get(position);
                alertDialog.dismiss();
                tvSelectedMarket.setText(companyname.getPoint());

                // Toast.makeText(getApplicationContext(), movie.getPoint() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (search.hasFocus()) {
                    mAdapter.filter(cs.toString());
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

        //ImageView ivClose = promptsView.findViewById(R.id.iv_close);

        alertDialogBuilder.setCancelable(true);
        alertDialog.show();
    }

    private void prepareCompanyData(List<SaelsPoint> movieList) {
        movieList.clear();
        String salesPointName = "", SalesPoitnCode = "";
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_ASSIGNED_SALESPOINT_CODE, "");
        map.put(db.KEY_ASSIGNED_SALESPOINT_NAME, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.ASSIGNED_SALES_POINT, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                SaelsPoint companyname = new SaelsPoint();
                salesPointName = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_ASSIGNED_SALESPOINT_NAME)));
                SalesPoitnCode = cursor.getString(cursor.getColumnIndex(db.KEY_ASSIGNED_SALESPOINT_CODE));
                companyname.setPoint(salesPointName);
                companyname.setId(SalesPoitnCode);
                movieList.add(companyname);
            }
            while (cursor.moveToNext());
        }


        // notify adapter about data set changes
        // so that it will render the list with new data
        // mAdapter.notifyDataSetChanged();
    }

    private void prepareSalesData(List<SaelsPoint> movieList) {
        movieList.clear();
        String salesPointName = "", SalesPoitnCode = "";
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_MARKET_PLAYER_ID, "");
        map.put(db.KEY_MARKET_PLAYER_NAME, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.MARKET_PLAYERS, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                SaelsPoint companyname = new SaelsPoint();
                salesPointName = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_MARKET_PLAYER_NAME)));
                SalesPoitnCode = cursor.getString(cursor.getColumnIndex(db.KEY_MARKET_PLAYER_ID));
                companyname.setPoint(salesPointName);
                companyname.setId(SalesPoitnCode);
                movieList.add(companyname);
            }
            while (cursor.moveToNext());
        }


        // notify adapter about data set changes
        // so that it will render the list with new data
        // mAdapter.notifyDataSetChanged();
    }

    private void prepareCropData(List<SaelsPoint> movieList) {
        movieList.clear();
        String salesPointName = "", SalesPoitnCode = "";
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_CROP_ID, "");
        map.put(db.KEY_CROP_NAME, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.CROPS_LIST, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                SaelsPoint companyname = new SaelsPoint();
                salesPointName = "" + Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_CROP_NAME)));
                SalesPoitnCode = cursor.getString(cursor.getColumnIndex(db.KEY_CROP_ID));
                companyname.setPoint(salesPointName);
                companyname.setId(SalesPoitnCode);
                movieList.add(companyname);
            }
            while (cursor.moveToNext());
        }


        // notify adapter about data set changes
        // so that it will render the list with new data
        // mAdapter.notifyDataSetChanged();
    }


}

