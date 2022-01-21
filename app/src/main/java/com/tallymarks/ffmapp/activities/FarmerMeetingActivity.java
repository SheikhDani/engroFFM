package com.tallymarks.ffmapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.adapters.SalesPointAdapter;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.database.ExtraHelper;
import com.tallymarks.ffmapp.database.MyDatabaseHandler;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.SaelsPoint;
import com.tallymarks.ffmapp.models.farmerMeeting.local.Crop;
import com.tallymarks.ffmapp.models.farmerMeeting.local.Customer;
import com.tallymarks.ffmapp.models.farmerMeeting.local.Farmer;
import com.tallymarks.ffmapp.models.farmerMeeting.local.Product;
import com.tallymarks.ffmapp.models.farmerMeeting.post.CreateFarmerMeetingRequest;
import com.tallymarks.ffmapp.models.farmerMeeting.post.CreateFarmerMeetingResponse;
import com.tallymarks.ffmapp.models.farmerMeeting.post.Dealer;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.DateTimePicker;
import com.tallymarks.ffmapp.utils.DateUtil;
import com.tallymarks.ffmapp.utils.FarmerMeetingDbHelper;
import com.tallymarks.ffmapp.utils.Helpers;
import com.tallymarks.ffmapp.utils.NetworkManager;
import com.tallymarks.ffmapp.utils.RecyclerTouchListener;
import com.tallymarks.ffmapp.utils.network.ApiClient;
import com.tallymarks.ffmapp.utils.network.CreateFarmerMeetingInterface;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FarmerMeetingActivity extends AppCompatActivity {

    private TextView tvHeaderText;
    private ImageView ivMenu, ivBack, iv1, iv2, iv3, iv4, iv5, ivAddAttachment;
    private EditText etActivityDate, etEmployeesAttended, etChiefGuest, etTotalFarmers, etTargetCustomers, etExpenseIncurred, etMeetingAddress, etRemarks;

    private Button btnBack, btnCreate;
    TextView txtHostFarmer, txtActivityDate, txtCrop, txtProduct, txttotalfarmers, txttargetCustomers, txtMeetingAddress;
    private ProgressBar progressBar;
    private AutoCompleteTextView autoCrop,autoProduct,autoDealer,autofarmer;

    private DatabaseHandler databaseHandler;
    private MyDatabaseHandler myDatabaseHandler;
    private SharedPrefferenceHelper sharedPrefferenceHelper;
    private ExtraHelper extraHelper;
    private DateTimePicker datePicker;

    private ArrayList<Product> productArrayList;
    private ArrayList<Customer> dealersArrayList;
    private ArrayList<Crop> cropsArrayList;
    private ArrayList<Farmer> farmerArrayList;
    private String activityDate, hostFarmerID, dealerID, dealerName, productID, cropID;
    private ArrayList<String> attachments = new ArrayList<>();
    static {
        System.loadLibrary("native-lib");
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_meeting);
        initView();

    }

    private void initView() {
        tvHeaderText = findViewById(R.id.tv_dashboard);
        ivMenu = findViewById(R.id.iv_drawer);
        ivBack = findViewById(R.id.iv_back);
        ivAddAttachment = findViewById(R.id.iv_add_attachment);
        iv1 = findViewById(R.id.iv1);
        iv2 = findViewById(R.id.iv2);
        iv3 = findViewById(R.id.iv3);
        iv4 = findViewById(R.id.iv4);
        iv5 = findViewById(R.id.iv5);
        txtHostFarmer = findViewById(R.id.tvHostFarmer);
        txtActivityDate = findViewById(R.id.tvActivityDate);
        txtCrop = findViewById(R.id.tvCrop);
        txtProduct = findViewById(R.id.tvProduct);
        txttotalfarmers = findViewById(R.id.tvtotalFarmers);
        txttargetCustomers = findViewById(R.id.tvtargetCustomers);
        txtMeetingAddress = findViewById(R.id.tvmeetingAddress);


        etActivityDate = findViewById(R.id.et_activity_date);
        etEmployeesAttended = findViewById(R.id.et_employee_attended);
        etChiefGuest = findViewById(R.id.et_chief_guest);
        etTotalFarmers = findViewById(R.id.et_total_Farmers);
        etTargetCustomers = findViewById(R.id.et_target_customer);
        etExpenseIncurred = findViewById(R.id.et_expenses_incurred);
        etMeetingAddress = findViewById(R.id.et_meeting_address);
        etRemarks = findViewById(R.id.et_meeting_remarks);

       // spHostFarmer = findViewById(R.id.sp_host_farmer);
       // spDealers = findViewById(R.id.sp_dealers);
       // spProducts = findViewById(R.id.sp_product);
        autoCrop = findViewById(R.id.auto_crop);
        autoProduct = findViewById(R.id.auto_product);
        autoDealer = findViewById(R.id.auto_dealer);
        autofarmer = findViewById(R.id.auto_farmer);

        btnBack = findViewById(R.id.btn_back);
        btnCreate = findViewById(R.id.btn_create);

        progressBar = findViewById(R.id.progress_bar);

        ivBack.setVisibility(View.VISIBLE);
        ivMenu.setVisibility(View.GONE);
        tvHeaderText.setVisibility(View.VISIBLE);
        tvHeaderText.setText(getResources().getString(R.string.farmer_meeting));

        SpannableStringBuilder tvHostFarmer = setStarToLabel("Host Farmer");
        SpannableStringBuilder tvActivityDate = setStarToLabel("Activity Date");
        SpannableStringBuilder tvCrop = setStarToLabel("Crop");
        SpannableStringBuilder tvProduct = setStarToLabel("Product");
        SpannableStringBuilder tvtotalFarmers = setStarToLabel("Total Farmers");
        SpannableStringBuilder tvtargetCustomers = setStarToLabel("Target Customers");
        SpannableStringBuilder tvmeetingAddress = setStarToLabel("Meeting Address");

        txtHostFarmer.setText(tvHostFarmer);
        txtActivityDate.setText(tvActivityDate);
        txtCrop.setText(tvCrop);
        txtProduct.setText(tvProduct);
        txttotalfarmers.setText(tvtotalFarmers);
        txttargetCustomers.setText(tvtargetCustomers);
        txtMeetingAddress.setText(tvmeetingAddress);

        databaseHandler = new DatabaseHandler(FarmerMeetingActivity.this);
        myDatabaseHandler = new MyDatabaseHandler(FarmerMeetingActivity.this);
        sharedPrefferenceHelper = new SharedPrefferenceHelper(FarmerMeetingActivity.this);
        extraHelper = new ExtraHelper(FarmerMeetingActivity.this);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etTotalFarmers.getText().toString().trim().isEmpty()) {
                    etTotalFarmers.setError(getResources().getString(R.string.required));
                    Toast.makeText(FarmerMeetingActivity.this, getResources().getString(R.string.farmers_required), Toast.LENGTH_LONG).show();
                } else if (etTargetCustomers.getText().toString().trim().isEmpty()) {
                    etTargetCustomers.setError(getResources().getString(R.string.required));
                    Toast.makeText(FarmerMeetingActivity.this, getResources().getString(R.string.customers_required), Toast.LENGTH_LONG).show();
                } else if (etMeetingAddress.getText().toString().trim().isEmpty()) {
                    etMeetingAddress.setError(getResources().getString(R.string.required));
                    Toast.makeText(FarmerMeetingActivity.this, getResources().getString(R.string.meeting_address_required), Toast.LENGTH_LONG).show();
                } else {
                    if (NetworkManager.isNetworkAvailable(FarmerMeetingActivity.this)) {
                        String accessToken ;

                        if(sharedPrefferenceHelper.getString(Constants.ACCESS_TOKEN)!=null  && !sharedPrefferenceHelper.getString(Constants.ACCESS_TOKEN).equals("")) {
                             accessToken = sharedPrefferenceHelper.getString(Constants.ACCESS_TOKEN);

                        }
                        else
                        {
                           accessToken = extraHelper.getString(Constants.ACCESS_TOKEN);
                        }
                        String authorization = Constants.BEARER + " " + accessToken;
//                        String authorization="Bearer 9371e3e4-f427-4078-a8f4-7158f412717c";
                        createMeeting(getCreateMeetingRequest(), authorization);
                    } else {
                        Toast.makeText(FarmerMeetingActivity.this, getResources().getString(R.string.internet_available_msg), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        initData();
    }

    private void goBack() {
        Intent i = new Intent(FarmerMeetingActivity.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
            try {
                String base64Url = convertUriToBase64(Uri.parse(mPaths.get(0)));
                attachments.add(base64Url);
                if (attachments.size() == 1) {
                    iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.GONE);
                    iv3.setVisibility(View.GONE);
                    iv4.setVisibility(View.GONE);
                    iv5.setVisibility(View.GONE);
                    ivAddAttachment.setVisibility(View.VISIBLE);
                } else if (attachments.size() == 2) {
                    iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.GONE);
                    iv4.setVisibility(View.GONE);
                    iv5.setVisibility(View.GONE);
                    ivAddAttachment.setVisibility(View.VISIBLE);
                } else if (attachments.size() == 3) {
                    iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.VISIBLE);
                    iv4.setVisibility(View.GONE);
                    iv5.setVisibility(View.GONE);
                    ivAddAttachment.setVisibility(View.VISIBLE);
                } else if (attachments.size() == 4) {
                    iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.VISIBLE);
                    iv4.setVisibility(View.VISIBLE);
                    iv5.setVisibility(View.GONE);
                    ivAddAttachment.setVisibility(View.VISIBLE);
                } else if (attachments.size() == 5) {
                    iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.VISIBLE);
                    iv4.setVisibility(View.VISIBLE);
                    iv5.setVisibility(View.VISIBLE);
                    ivAddAttachment.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected String convertUriToBase64(Uri uri) throws Exception {
        try {
            Bitmap bitmap = null;
            String encodedString = "";
            ByteArrayOutputStream outputStream = null;
            try {
                bitmap = BitmapFactory.decodeFile(uri.getPath());
                outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                byte[] byteArray = outputStream.toByteArray();

                encodedString = Base64.encodeToString(byteArray, Base64.NO_WRAP);
            } catch (Exception e) {
                e.printStackTrace();
                encodedString = "";
            } finally {
                try {
                    bitmap.recycle();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.i("Base 64 String: ", encodedString);
            return encodedString;
        } catch (Exception e) {
            throw e;
        }
    }

    private void initData() {
        loadProducts(databaseHandler);
        loadDealers(databaseHandler);
        loadCrops(databaseHandler);
        loadFarmers(myDatabaseHandler);
        etActivityDate.setText(DateUtil.getCurrentDate(Constants.DATE_FORMAT));
        datePicker = new DateTimePicker();

        etActivityDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    datePicker.showDatePickerDialog(FarmerMeetingActivity.this, true, etActivityDate, Constants.DATE_FORMAT, activityDate, DateUtil.getPastDate(Constants.DATE_FORMAT, 0), DateUtil.getFutureDate(Constants.DATE_FORMAT, 365));
                    return true;
                }
                return false;
            }
        });

        etActivityDate.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                activityDate = etActivityDate.getText().toString();
            }
        });

//        spHostFarmer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//
//                hostFarmerID = farmerArrayList.get(position).getFarmerID();
////                Toast.makeText(FarmerMeetingActivity.this,hostFarmerID,Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//
//            }
//        });


//        spDealers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//
//                dealerID = dealersArrayList.get(position).getCustomerCode();
//                dealerName = dealersArrayList.get(position).getCustomerName();
////                Toast.makeText(FarmerMeetingActivity.this,dealerID,Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//
//            }
//        });
        autoCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDialouge(autoCrop,"autocrop");
            }
        });
        autoProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDialouge(autoProduct,"autoproduct");
            }
        });
        autoDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDialouge(autoDealer,"autodealer");
            }
        });
        autofarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDialouge(autofarmer,"autofarmer");
            }
        });

//        spCrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//
//                cropID = cropsArrayList.get(position).getCropID();
////                Toast.makeText(FarmerMeetingActivity.this,cropID,Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//
//            }
//        });

//        spProducts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//
//                productID = productArrayList.get(position).getBranchID();
////                Toast.makeText(FarmerMeetingActivity.this,productID,Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//
//            }
//        });

        ivAddAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }

    private void takePicture() {
        new ImagePicker.Builder(this)
                .mode(ImagePicker.Mode.GALLERY)
                .compressLevel(ImagePicker.ComperesLevel.HARD)
                .directory(ImagePicker.Directory.DEFAULT)
                .extension(ImagePicker.Extension.PNG)
                .scale(600, 600)
                .allowMultipleImages(false)
                .enableDebuggingMode(true)
                .build();
    }

    private void loadProducts(DatabaseHandler databaseHandler) {
        productArrayList = FarmerMeetingDbHelper.loadProductsFromDB(databaseHandler);
        //populateProductsSpinner();
    }

    private void populateProductsSpinner() {
//        ArrayList<String> productsList = new ArrayList<>();
//        for (int i = 0; i < productArrayList.size(); i++) {
//            productsList.add(productArrayList.get(i).getBranchName());
//        }
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(FarmerMeetingActivity.this, android.R.layout.simple_spinner_item, productsList);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spProducts.setAdapter(dataAdapter);

    }

    private void loadDealers(DatabaseHandler databaseHandler) {
        dealersArrayList = FarmerMeetingDbHelper.loadDealersFromDB(databaseHandler);
        //populateDealersSpinner();
    }

    private void populateDealersSpinner() {
//        ArrayList<String> dealersList = new ArrayList<>();
//        for (int i = 0; i < dealersArrayList.size(); i++) {
//            dealersList.add(dealersArrayList.get(i).getCustomerName());
//        }
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(FarmerMeetingActivity.this, android.R.layout.simple_spinner_item, dealersList);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spDealers.setAdapter(dataAdapter);
    }

    private void loadCrops(DatabaseHandler databaseHandler) {
        cropsArrayList = FarmerMeetingDbHelper.loadCropsFromDB(databaseHandler);
       // populateCropsSpinner();
    }

    private void populateCropsSpinner() {
//        ArrayList<String> cropsList = new ArrayList<>();
//        for (int i = 0; i < cropsArrayList.size(); i++) {
//            cropsList.add(cropsArrayList.get(i).getCropName());
//        }
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(FarmerMeetingActivity.this, android.R.layout.simple_spinner_item, cropsList);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spCrop.setAdapter(dataAdapter);
    }

    private void loadFarmers(MyDatabaseHandler databaseHandler) {
        farmerArrayList = FarmerMeetingDbHelper.loadFarmersFromDB(databaseHandler);
      // populateFarmersSpinner();
    }

    private void populateFarmersSpinner() {
//        ArrayList<String> farmersList = new ArrayList<>();
//        for (int i = 0; i < farmerArrayList.size(); i++) {
//            farmersList.add(farmerArrayList.get(i).getFarmerName());
//        }
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(FarmerMeetingActivity.this, android.R.layout.simple_spinner_item, farmersList);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spHostFarmer.setAdapter(dataAdapter);
    }

    public void selectDialouge(AutoCompleteTextView autoProduct, String from) {
        LayoutInflater li = LayoutInflater.from(FarmerMeetingActivity.this);
        View promptsView = li.inflate(R.layout.dialouge_sales_point, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FarmerMeetingActivity.this);
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
        if(from.equals("autocrop")) {
            title.setText("Select Crop");
        }
        else if(from.equals("autoproduct"))
        {
            title.setText("Select Product");
        }
        else if(from.equals("autodealer"))
        {
            title.setText("Select Dealer");
        }
        else if(from.equals("autofarmer"))
        {
            title.setText("Select Farmer");
        }


        final RecyclerView recyclerView = promptsView.findViewById(R.id.recyclerView);
        if(from.equals("autocrop")) {
            for (int i = 0; i < cropsArrayList.size(); i++) {
                SaelsPoint crop = new SaelsPoint();
                crop.setId(cropsArrayList.get(i).getCropID());
                crop.setPoint(cropsArrayList.get(i).getCropName());
                companyList.add(crop);
            }
            //prepareCropData(cropsList);
        }
        else if(from.equals("autoproduct"))
        {
            for (int i = 0; i < productArrayList.size(); i++) {
                SaelsPoint crop = new SaelsPoint();
                crop.setId(productArrayList.get(i).getBranchID());
                crop.setPoint(productArrayList.get(i).getBranchName());
                companyList.add(crop);
            }
        }
        else if(from.equals("autodealer"))
        {
            for (int i = 0; i < dealersArrayList.size(); i++) {
                SaelsPoint crop = new SaelsPoint();
                crop.setId(dealersArrayList.get(i).getCustomerCode());
                crop.setPoint(dealersArrayList.get(i).getCustomerName());
                companyList.add(crop);
            }

        }
        else if(from.equals("autofarmer"))
        {
            for (int i = 0; i < farmerArrayList.size(); i++) {
                SaelsPoint crop = new SaelsPoint();
                crop.setId(farmerArrayList.get(i).getFarmerID());
                crop.setPoint(farmerArrayList.get(i).getFarmerName());
                companyList.add(crop);
            }

        }

        final SalesPointAdapter mAdapter = new SalesPointAdapter(companyList,"salescall");

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
                autoProduct.setText(companyname.getPoint());
                if(from.equals("autocrop")) {
                    cropID = companyname.getId();
                }
                else if(from.equals("autoproduct"))
                {
                    productID = companyname.getId();
                }
                else if(from.equals("autodealer"))
                {
                    dealerID = companyname.getId();
                    dealerName = companyname.getPoint();
                }
                else if(from.equals("autofarmer"))
                {
                    hostFarmerID= companyname.getId();

                }




                // Toast.makeText(getApplicationContext(), movie.getPoint() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if(search.hasFocus()) {
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

    private void createMeeting(ArrayList<CreateFarmerMeetingRequest> createFarmerMeetingRequest, String authorization) {
        final CreateFarmerMeetingInterface createFarmerMeetingInterface = ApiClient.getClient().create(CreateFarmerMeetingInterface.class);
        Call<CreateFarmerMeetingResponse> call = createFarmerMeetingInterface.createMeeting(createFarmerMeetingRequest, authorization);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<CreateFarmerMeetingResponse>() {

            @Override
            public void onResponse(Call<CreateFarmerMeetingResponse> call, Response<CreateFarmerMeetingResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    goBack();
                    Toast.makeText(FarmerMeetingActivity.this, response.body().getDescription(), Toast.LENGTH_LONG).show();
                } else if (response.code() == 401) {
                    Toast.makeText(FarmerMeetingActivity.this, getResources().getString(R.string.invalid_token), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(FarmerMeetingActivity.this, getResources().getString(R.string.error_occured), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<CreateFarmerMeetingResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(FarmerMeetingActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private ArrayList<CreateFarmerMeetingRequest> getCreateMeetingRequest() {
        ArrayList<CreateFarmerMeetingRequest> createFarmerMeetingRequestsArrayList = new ArrayList<>();
        CreateFarmerMeetingRequest createFarmerMeetingRequest = new CreateFarmerMeetingRequest();
        createFarmerMeetingRequest.setActivityDate(Helpers.utcToAnyDateFormat(etActivityDate.getText().toString().trim(), Constants.DATE_FORMAT, Constants.YEAR_DATE_FORMAT));
        createFarmerMeetingRequest.setAttachements(attachments);
        createFarmerMeetingRequest.setChiefGuest(etChiefGuest.getText().toString().trim());
        createFarmerMeetingRequest.setCropId(Integer.parseInt(cropID));
        createFarmerMeetingRequest.setEmployeesAttended(etEmployeesAttended.getText().toString().trim());
        createFarmerMeetingRequest.setExpenses(etExpenseIncurred.getText().toString().trim());
        createFarmerMeetingRequest.setHostFarmerId(Integer.parseInt(hostFarmerID));
        createFarmerMeetingRequest.setMeetingAddress(etMeetingAddress.getText().toString().trim());
        createFarmerMeetingRequest.setProductId(Integer.parseInt(productID));
        createFarmerMeetingRequest.setRemarks(etRemarks.getText().toString().trim());
        createFarmerMeetingRequest.setTotalCustomers(Integer.parseInt(etTargetCustomers.getText().toString().trim()));
        createFarmerMeetingRequest.setTotalFarmers(Integer.parseInt(etTotalFarmers.getText().toString().trim()));
        createFarmerMeetingRequest.setDealers(getDealers());
        createFarmerMeetingRequestsArrayList.add(createFarmerMeetingRequest);
        return createFarmerMeetingRequestsArrayList;
    }

    private ArrayList<Dealer> getDealers() {
        ArrayList<Dealer> dealerArrayList = new ArrayList<>();
        Dealer dealer = new Dealer();
        dealer.setCustomerCode(dealerID);
        dealer.setCustomerName(dealerName);
        dealerArrayList.add(dealer);
        return dealerArrayList;
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
}
