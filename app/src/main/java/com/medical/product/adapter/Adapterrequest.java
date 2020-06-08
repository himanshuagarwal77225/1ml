package com.medical.product.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.medical.product.R;
import com.medical.product.Ui.Accepts_orders;
import com.medical.product.Ui.AddNew_Update_AddressActivity;
import com.medical.product.Ui.MyAddressActivity;
import com.medical.product.Ui.Request_order;
import com.medical.product.models.GatterGetAddressListModel;
import com.medical.product.models.Prescription;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.medical.product.Utils.Utlity.date_conva;


public class Adapterrequest extends RecyclerView.Adapter<Adapterrequest.MyViewHolder> {

    SharedPreferences prefrance, prefranceid;
    String sharid;
    private ArrayList<Request_order> recyclerModels; // this data structure carries our title and description
    Context context;
    public Adapterrequest(ArrayList<Request_order> recyclerModels, Context context) {
        this.recyclerModels = recyclerModels;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_request, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // update your data here
        final Request_order getAddvertise = recyclerModels.get(position);
        holder.ordreid.setText(getAddvertise.getOrder_id());
        holder.date.setText(date_conva(getAddvertise.getAdd_date()));
        if (getAddvertise.getUser_approved().equalsIgnoreCase("1")) {
            holder.stauts.setText("Accepted");
            holder.stauts.setTextColor(context.getResources().getColor(R.color.green));
        } else if (getAddvertise.getUser_approved().equalsIgnoreCase("2")) {
            holder.stauts.setText("Cancled");
            holder.stauts.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            holder.stauts.setText("Pending");
            holder.stauts.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Accepts_orders.class).putExtra("id", getAddvertise.getOrder_id()));
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
        public TextView ordreid, stauts, date;
        public ImageView img;

        MyViewHolder(View view) {
            super(view);
            //for  type=1
            ordreid = (TextView) itemView.findViewById(R.id.ordreid);
            stauts = (TextView) itemView.findViewById(R.id.stauts);
            date = (TextView) itemView.findViewById(R.id.date);

        }

    }

    private String date_conva(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date sourceDate = null;
        try {
            sourceDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat targetFormat = new SimpleDateFormat("E MMM dd,yyyy hh:mm:ss");
        return String.valueOf(targetFormat.format(sourceDate));
    }


}
