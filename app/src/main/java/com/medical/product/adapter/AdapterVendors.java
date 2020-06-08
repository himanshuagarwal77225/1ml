package com.medical.product.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.medical.product.R;
import com.medical.product.Ui.Accepts_orders;
import com.medical.product.Ui.Full_image;
import com.medical.product.Ui.Product_detailActivity;
import com.medical.product.Ui.Product_vendor;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.models.GatterGetVendersModel;
import com.medical.product.models.Vendors;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AdapterVendors extends RecyclerView.Adapter<AdapterVendors.MyViewHolder> {
    String id;
    private ArrayList<Vendors> recyclerModels; // this data structure carries our title and description
    Context context;
    Accepts_orders accepts_orders;

    public AdapterVendors(ArrayList<Vendors> recyclerModels, String id, Context context, Accepts_orders accepts_orders) {
        this.recyclerModels = recyclerModels;
        this.context = context;
        this.id = id;
        this.accepts_orders = accepts_orders;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_single, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // update your data here
        final Vendors getAddvertise = recyclerModels.get(position);

        holder.store_name.setText(getAddvertise.getVendor_name());
        if (id.equalsIgnoreCase(getAddvertise.getVendor_id())) {
            holder.status.setText("Approved");
            holder.status.setTextColor(context.getResources().getColor(R.color.green));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accepts_orders.vendor_info(getAddvertise);
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
        // return super.getItemViewType(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView store_name, status;

        MyViewHolder(View view) {
            super(view);
            store_name = (TextView) itemView.findViewById(R.id.store_name);
            status = (TextView) itemView.findViewById(R.id.status);
        }


    }


}
