package com.medical.product.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.medical.product.R;
import com.medical.product.Ui.AddNew_Update_AddressActivity;
import com.medical.product.Ui.MyAddressActivity;
import com.medical.product.models.GatterGetAddressListModel;

import java.util.ArrayList;


public class AdapterAddressList extends RecyclerView.Adapter<AdapterAddressList.MyViewHolder> {

    SharedPreferences prefrance, prefranceid;
    String sharid;
    private static final String TAG = "AdapterAddressList";
    private ArrayList<GatterGetAddressListModel> recyclerModels; // this data structure carries our title and description
    Context context;

    public AdapterAddressList(ArrayList<GatterGetAddressListModel> recyclerModels, Context context) {
        this.recyclerModels = recyclerModels;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shipping_address_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
        // update your data here
        final GatterGetAddressListModel getAddvertise = recyclerModels.get(position);


        holder.txtusername.setText(getAddvertise.getFirst_name() + " " + getAddvertise.getLast_name()+"   ("+getAddvertise.getAddress_type()+")");
        holder.txtaddress.setText((getAddvertise.getAddress_area() + " " + getAddvertise.getCity() + " " + getAddvertise.getState() + " - " + getAddvertise.getPostal_code()).toString());
        holder.txtPhone.setText(getAddvertise.getPhone_number());
        Log.d(TAG, "onBindViewHolder: getAddvertise  : "+getAddvertise.toString());
        holder.btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AddNew_Update_AddressActivity.class);
                i.putExtra("Addupdate", "update");
                i.putExtra("addid", getAddvertise.getId());
                i.putExtra("postal", getAddvertise.getPostal_code());
                i.putExtra("homenumber", getAddvertise.getAddress_num());
                i.putExtra("addarea", getAddvertise.getAddress_area());
                i.putExtra("city", getAddvertise.getCity());
                i.putExtra("state", getAddvertise.getState());
                i.putExtra("country", getAddvertise.getCountry());
                i.putExtra("landmark", getAddvertise.getLandmark());
                i.putExtra("fname", getAddvertise.getFirst_name());
                i.putExtra("lname", getAddvertise.getLast_name());
                i.putExtra("phone", getAddvertise.getPhone_number());
                i.putExtra("alterphone", getAddvertise.getAlternative_number());
                i.putExtra("email", getAddvertise.getEmail());
                i.putExtra("addtype", getAddvertise.getAddress_type());

                context.startActivity(i);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (context instanceof MyAddressActivity) {
                    ((MyAddressActivity) context).backToCheckout(position, new Gson().toJson(getAddvertise));
                }

            }
        });
        holder.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof MyAddressActivity) {
                    MyAddressActivity myAddressActivity = (MyAddressActivity) context;
                    myAddressActivity.backToCheckout(position, new Gson().toJson(getAddvertise));
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
        // return super.getItemViewType(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtusername, txtaddress, txtPhone, txtAddtype;
        ImageView btnedit;
        RadioButton select;

        MyViewHolder(View view) {
            super(view);
            txtusername = (TextView) itemView.findViewById(R.id.txtusername);
            txtPhone = (TextView) itemView.findViewById(R.id.txtPhone);
            btnedit = (ImageView) itemView.findViewById(R.id.btnedit);
            txtaddress = (TextView) itemView.findViewById(R.id.txtaddress);
            txtAddtype = (TextView) itemView.findViewById(R.id.txtAddtype);
            select = (RadioButton) itemView.findViewById(R.id.select);


        }

    }

    public void filterList(ArrayList<GatterGetAddressListModel> filterdNames) {
        this.recyclerModels = filterdNames;
        Log.i("ass", "notifyDataSetChanged===============");
        notifyDataSetChanged();
    }


}
