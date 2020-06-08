package com.medical.product.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.medical.product.R;
import com.medical.product.Ui.Accepts_orders;
import com.medical.product.Ui.Request_order;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.models.Prescription;
import com.medical.product.models.Vendors;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Adapterrequest2 extends RecyclerView.Adapter<Adapterrequest2.MyViewHolder> {

    SharedPreferences prefrance, prefranceid;
    String sharid;
    private ArrayList<Prescription> recyclerModels; // this data structure carries our title and description
    Context context;
    Gson gson = new Gson();

    public Adapterrequest2(ArrayList<Prescription> recyclerModels, Context context) {
        this.recyclerModels = recyclerModels;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_request2, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // update your data here
        final Prescription prescription = recyclerModels.get(position);
        Glide.with(context)
                .load(ApiFileuri.ROOT_URL_USER_IMAGE + prescription.getPerception_images()[0])
                .fitCenter()
                .into(holder.img);
        holder.date.setText(date_conva(prescription.getAdd_date()));
        String method = "";
        if (prescription.getPerception_method().equalsIgnoreCase("0")) {
            method = "Order everything as per prescription";
            holder.prescaptionoption.setText(method + "\n" + prescription.getPerception_detail());

        } else if (prescription.getPerception_method().equalsIgnoreCase("1")) {
            method = "Let me specify medicines and  quantity";
            holder.prescaptionoption.setText(method + "\n" + prescription.getPerception_detail());

        } else {
            holder.prescaptionoption.setText(prescription.getPerception_detail());
        }

        if (prescription.getApproved().equalsIgnoreCase("0")) {
            holder.order_satus.setText("Prescription Request for review");
        } else {
            if (!TextUtils.isEmpty(get_store_name(prescription))) {
                holder.order_satus.setText("Order approved by " + get_store_name(prescription));
            } else {
                holder.order_satus.setText("Order approved pendding");
            }

            holder.order_satus.setTextColor(context.getResources().getColor(R.color.sky_blue));
        }

        if (prescription.getUser_approved().equalsIgnoreCase("1")) {
            holder.order_satus.setText("Order Accepted");
            holder.order_satus.setTextColor(context.getResources().getColor(R.color.green));
        }
        if (prescription.getUser_approved().equalsIgnoreCase("2")) {
            holder.order_satus.setText("Order Canceled");
            holder.order_satus.setTextColor(context.getResources().getColor(R.color.red));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Accepts_orders.class).putExtra("details", gson.toJson(prescription)));
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
        public TextView date, prescaptionoption, order_satus;
        public ImageView img;

        MyViewHolder(View view) {
            super(view);
            //for  type=1
            prescaptionoption = (TextView) itemView.findViewById(R.id.prescaptionoption);
            date = (TextView) itemView.findViewById(R.id.date);
            order_satus = (TextView) itemView.findViewById(R.id.order_satus);
            img = (ImageView) itemView.findViewById(R.id.img);

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

    public String get_store_name(Prescription prescription) {
        String name = "";
        for (Vendors vendors : prescription.getVendors()) {
            if (vendors.getVendor_id().equalsIgnoreCase(prescription.getFor_vendor())) {
                name = vendors.getVendor_name();
                break;
            }
        }
        return name;
    }
}
