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

import com.google.gson.Gson;
import com.medical.product.R;
import com.medical.product.Ui.AddNew_Update_AddressActivity;
import com.medical.product.Ui.MyAddressActivity;
import com.medical.product.models.Approved_data_detail;
import com.medical.product.models.GatterGetAddressListModel;

import java.util.ArrayList;


public class Preciption_order_Adpater extends RecyclerView.Adapter<Preciption_order_Adpater.MyViewHolder> {

    SharedPreferences prefrance, prefranceid;
    String sharid;
    private ArrayList<Approved_data_detail> recyclerModels; // this data structure carries our title and description
    Context context;

    public Preciption_order_Adpater(ArrayList<Approved_data_detail> recyclerModels, Context context) {
        this.recyclerModels = recyclerModels;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // update your data here
        final Approved_data_detail getAddvertise = recyclerModels.get(position);
        holder.price.setText(context.getString(R.string.Rs) +" "+ getAddvertise.getPrice());
        holder.product.setText(getAddvertise.getQuantity()+" x "+getAddvertise.getMedicine_name());
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
        public TextView product, price;

        MyViewHolder(View view) {
            super(view);
            product = (TextView) itemView.findViewById(R.id.product);
            price = (TextView) itemView.findViewById(R.id.price);
        }

    }

}
