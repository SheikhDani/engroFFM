package com.tallymarks.ffmapp.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.tallymarks.ffmapp.R;
import com.tallymarks.ffmapp.activities.ConverionRetentionFormActivity;
import com.tallymarks.ffmapp.activities.ConversionRetentionActivity;
import com.tallymarks.ffmapp.activities.QualityofSalesCallActivity;
import com.tallymarks.ffmapp.activities.SupervisorLocationApprovalListActivity;
import com.tallymarks.ffmapp.database.ExtraHelper;
import com.tallymarks.ffmapp.database.SharedPrefferenceHelper;
import com.tallymarks.ffmapp.models.ChangeLocation;
import com.tallymarks.ffmapp.models.CustomerSnapShotParent;
import com.tallymarks.ffmapp.models.addapprovalrequest.AddLocationApproval;
import com.tallymarks.ffmapp.models.addconversioninput.AddConversioninput;
import com.tallymarks.ffmapp.utils.Constants;
import com.tallymarks.ffmapp.utils.HttpHandler;
import com.tallymarks.ffmapp.utils.ItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class LocationApprovalAdapter extends RecyclerView.Adapter<LocationApprovalAdapter.MyViewHolder> {

    private List<ChangeLocation> planList;
    private List<ChangeLocation> headerList;
    private Context mContext;

    private ItemClickListener clickListener;
    SharedPrefferenceHelper sHelper;
    ExtraHelper extraHelper;




    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener  {
        public TextView title, member, customecode, lat, lng,reason,createdate,approve,reject,lastvisitcount;
        ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textView_name);
            member = (TextView) view.findViewById(R.id.textView_subcription);
            image = (ImageView) view.findViewById(R.id.imageView);
            customecode = (TextView) view.findViewById(R.id.customer_code);
            lat = (TextView) view.findViewById(R.id.latitude);
            lng = (TextView) view.findViewById(R.id.longitude);
            reason= (TextView) view.findViewById(R.id.reason);
            createdate= (TextView) view.findViewById(R.id.creation_date);
            approve= (TextView) view.findViewById(R.id.textView_approve);
            reject= (TextView) view.findViewById(R.id.textView_reject);
            lastvisitcount = (TextView) view.findViewById(R.id.lastvisitcount);

            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null)
                clickListener.onClick(view, getAdapterPosition());
        }

    }


    public LocationApprovalAdapter(List<ChangeLocation> moviesList,Context c) {
        this.planList = moviesList;
        this.headerList = new ArrayList<ChangeLocation>();
        this.headerList.addAll(planList);
        this.mContext = c;
        this.sHelper = new SharedPrefferenceHelper(mContext);
        this.extraHelper = new ExtraHelper(mContext);


    }

    @Override
    public LocationApprovalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recylerview_locationapproval_item, parent, false);

        return new LocationApprovalAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(LocationApprovalAdapter.MyViewHolder holder, int position) {
        ChangeLocation movie = planList.get(position);
        holder.title.setText(movie.getName());

        if (movie.getStatus().equals("Completed")) {
            holder.member.setBackgroundColor(Color.parseColor("#159356"));
            holder.member.setText(movie.getStatus());
        }
        else
        {
            holder.member.setBackgroundColor(Color.GRAY);
            holder.member.setText(movie.getStatus());
        }

        holder.customecode.setText(movie.getCode());
        holder.lat.setText(movie.getLatitude());
        holder.lng.setText(movie.getLongitude());
        holder.reason.setText(movie.getReason());
        holder.createdate.setText("Creation Date: "+movie.getDate());
        holder.lastvisitcount.setText("Last Visit Count: "+movie.getLastvisitcount());
        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialouge("Approved",movie.getId());
            }
        });
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialouge("Rejected",movie.getId());

            }
        });

    }

    @Override
    public int getItemCount() {
        return planList.size();
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        planList.clear();
        if (charText.length() == 0) {
            planList.addAll(headerList);
        } else {

            ArrayList<ChangeLocation> filteredList = new ArrayList<>();
            for (ChangeLocation pl :headerList) {

                if (pl.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    planList.add(pl);
                }

            }
//            headerList = filteredList;
        }

        notifyDataSetChanged();
    }
    private void openDialouge(String status,String id) {

        LayoutInflater li = LayoutInflater.from(mContext);
        View promptsView = li.inflate(R.layout.dialouge_supervisor_comments, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();


        EditText et_review = promptsView.findViewById(R.id.et_Remarks);

        ImageView ivClsoe = promptsView.findViewById(R.id.ivClose);
        ivClsoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });


        Button btnYes = promptsView.findViewById(R.id.btn_add);
        // ivClose.setVisibility(View.GONE);


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
                new AddLocationApprovalComment(status,et_review.getText().toString(),id).execute();
//                Intent salescall = new Intent(FarmVisitActivity.this,QualityofSalesCallActivity.class);
//                startActivity(salescall);

            }


        });
        alertDialogBuilder.setCancelable(true);
        alertDialog.show();
    }
    private class AddLocationApprovalComment extends AsyncTask<String, Void, Void> {

        String response = null;
        String status = "";
        String message = "";
        String approvalstatus = "";
        String approvalcomments= "";
        String approvalid =  "";
        ProgressDialog pDialog;
        private HttpHandler httpHandler;

        AddLocationApprovalComment(String status,String comment,String id)
        {
            this.approvalstatus= status;
            this.approvalcomments = comment;
            this.approvalid = id;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(mContext);
            pDialog.setMessage(mContext.getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected Void doInBackground(String... Url) {


            String editConverion = Constants.FFM_SUPERVISOR_LOCATION_APPROVAL;
            Gson gson = new Gson();
            AddLocationApproval createConversion = new AddLocationApproval();
            createConversion.setStatus(approvalstatus);
            createConversion.setSupervisorComments(approvalcomments);
            createConversion.setId(Integer.parseInt(approvalid));
            httpHandler = new HttpHandler(mContext);
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
            String output = gson.toJson(createConversion);
            //output = gson.toJson(inputParameters, SaveWorkInput.class);
            try {
                response = httpHandler.httpPut(editConverion, headerParams2, bodyParams, output);
                if (response != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        status = String.valueOf(jsonObj.getString("success"));
                        message = String.valueOf(jsonObj.getString("description"));
                        //discription = String.valueOf(jsonObj.getString("description"));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Helpers.displayMessage(MainActivity.this, true, "No Data Available");


            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            pDialog.dismiss();
            if (status.equals("true")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                alertDialogBuilder.setTitle(R.string.alert)
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent i = new Intent(mContext,SupervisorLocationApprovalListActivity.class);
                                mContext.startActivity(i);
                                ((Activity)mContext).finish();

                                //new SupervisorLocationApprovalListActivity.GetAllCustomerLocation().execute();

                                //new PostSyncOutlet().execute();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

        }
    }
}
