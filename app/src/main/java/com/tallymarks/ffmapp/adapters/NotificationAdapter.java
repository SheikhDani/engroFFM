package com.tallymarks.ffmapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tallymarks.ffmapp.models.NotificationModel;

import java.util.ArrayList;
import com.tallymarks.ffmapp.R;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    // variable for our array list and context.
    private ArrayList<NotificationModel> userModalArrayList;
    private Context context;

    // creating a constructor.
    public NotificationAdapter(ArrayList<NotificationModel> userModalArrayList, Context context) {
        this.userModalArrayList = userModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout file on below line.
        View view = LayoutInflater.from(context).inflate(R.layout.notification_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // getting data from our array list in our modal class.
        NotificationModel notificationModel = userModalArrayList.get(position);

        // on below line we are setting data to our text view.
        holder.title.setText(notificationModel .getTitle());
        holder.body.setText(notificationModel .getBody());
        holder.createddate.setText(notificationModel.getCreated_date());


    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return userModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating a variable for our text view and image view.
        private TextView title, body, createddate;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing our variables.
           title = itemView.findViewById(R.id.txt_title);
            body = itemView.findViewById(R.id.txt_body);
            createddate = itemView.findViewById(R.id.txt_createddate);

        }
    }
}
