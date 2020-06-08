package com.medical.product.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medical.product.R;
import com.medical.product.Ui.Order_Tab_Activity;
import com.medical.product.Utils.Utlity;
import com.medical.product.models.GatterGetMyNotificationListModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class AdapterNotificationList extends RecyclerView.Adapter<AdapterNotificationList.MyViewHolder> {

    SharedPreferences prefrance, prefranceid;
    String sharid;
    private ArrayList<GatterGetMyNotificationListModel> recyclerModels; // this data structure carries our title and description
    Context context;

    public AdapterNotificationList(ArrayList<GatterGetMyNotificationListModel> recyclerModels, Context context) {
        this.recyclerModels = recyclerModels;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final GatterGetMyNotificationListModel getAddvertise = recyclerModels.get(position);
        holder.txtnotification.setText(getAddvertise.getMessage());
        holder.txtdate.setText(Utlity.date_conva(getAddvertise.getAdd_date()));
        if (getAddvertise.getNotification_type().equalsIgnoreCase("order_status")) {
            holder.type.setText("Order by Products");

        } else {
            holder.type.setText("Order by Perception");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getAddvertise.getNotification_type().equalsIgnoreCase("order_status")||getAddvertise.getNotification_type().equalsIgnoreCase("perception_status"))
                {
                    context.startActivity(new Intent(context, Order_Tab_Activity.class));
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return recyclerModels.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtnotification, txtdate, type;

        MyViewHolder(View view) {
            super(view);
            txtnotification = (TextView) itemView.findViewById(R.id.txtnotification);
            txtdate = (TextView) itemView.findViewById(R.id.txtdate);
            type = (TextView) itemView.findViewById(R.id.type);
        }

    }

}
