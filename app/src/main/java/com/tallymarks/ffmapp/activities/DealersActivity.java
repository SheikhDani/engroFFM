package com.tallymarks.ffmapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.database.DatabaseHandler;
import com.tallymarks.ffmapp.database.MyDatabaseHandler;
import com.tallymarks.ffmapp.models.Dealers;
import com.tallymarks.ffmapp.utils.Helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.tallymarks.ffmapp.database.MyDatabaseHandler.TODAY_FARMER_JOURNEY_PLAN;

public class DealersActivity extends AppCompatActivity {

    private Context mContext;
    private Activity mActivity;

    DatabaseHandler db;
    MyDatabaseHandler mydb;
    private RelativeLayout mRelativeLayout;
    private ListView mListView;
    private TextView mTextView;
    ArrayList<String> customerNameArraylist = new ArrayList<>();
    ArrayList<String> customerCodeArraylist = new ArrayList<>();

    ArrayList<String> selectedCustomerCode = new ArrayList<>();
    ArrayList<String> selectedCustomerName = new ArrayList<>();
    Button back;
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealers);

        // getting Dealerss form locla storage
        getDealersfromDatabase();

        db = new DatabaseHandler(DealersActivity.this);
        mydb = new MyDatabaseHandler(DealersActivity.this);
        // Get the application context
        mContext = getApplicationContext();
        back = findViewById(R.id.back);

        // Get the activity
        mActivity = DealersActivity.this;

        // Get the widgets reference from XML layout
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);
        mListView = (ListView) findViewById(R.id.lv);
        mTextView = (TextView) findViewById(R.id.tv);

        // Initializing a new list
        List<String> trees = Arrays.asList(
                "Alder",
                "Basswood",
                "Birch",
                "Buckeye",
                "Cedar",
                "Cherry",
                "Chestnut",
                "Hawthorn",
                "Cypress",
                "Honeylocust"
        );

        // Initialize a new ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter(
                mActivity,
                android.R.layout.simple_list_item_multiple_choice,
                customerNameArraylist
        );

        // Set the adapter for ListView
        mListView.setAdapter(adapter);

        // Set an item click listener for the ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SparseBooleanArray clickedItemPositions = mListView.getCheckedItemPositions();

                // Set the TextView text
                mTextView.setText("Checked items - ");
                Dealers dealers = new Dealers();

                for(int index=0;index<clickedItemPositions.size();index++){
                    // Get the checked status of the current item
                    boolean checked = clickedItemPositions.valueAt(index);

                    if(checked){
                        // If the current item is checked
                        int key = clickedItemPositions.keyAt(index);
                        String item = (String) mListView.getItemAtPosition(key);

                        selectedCustomerName.add(item);
                        selectedCustomerCode.add(customerCodeArraylist.get(key));

                        dealers.setCustomerCode(customerCodeArraylist.get(key));
                        dealers.setCustomerName(item);

                        // Display the checked items on TextView
                        mTextView.setText(mTextView.getText() + item + " | ");
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> map = new HashMap<>();
                for (int j = 0; j < selectedCustomerName.size(); j++) {
                    map.put(mydb.KEY_CUSTOMER_CODE, selectedCustomerCode.get(j));
                    map.put(mydb.KEY_CUSTOEMR_NAME, selectedCustomerName.get(j));
                    //map.put();

                    mydb.addData(TODAY_FARMER_JOURNEY_PLAN , map);
                }
            }
        });
    }

    public void getDealersfromDatabase(){
        HashMap<String, String> map = new HashMap<>();

        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_NAME, "");
        map.put(db.KEY_TODAY_JOURNEY_CUSTOMER_CODE, "");
        //map.put(db.KEY_IS_VALID_USER, "");
        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.TODAY_JOURNEY_PLAN, map, filters);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                customerNameArraylist.add(Helpers.clean(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_NAME))));
                customerCodeArraylist.add(cursor.getString(cursor.getColumnIndex(db.KEY_TODAY_JOURNEY_CUSTOMER_CODE)));
            }
            while (cursor.moveToNext());
        }
    }
}