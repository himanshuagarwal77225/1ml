package com.medical.product.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.medical.product.Ui.Family_Mamber_Activity;
import com.medical.product.R;
import com.medical.product.models.GatterGetFamilyMamberListModel;

import java.util.ArrayList;


public class AdapterFamilyMamberList extends RecyclerView.Adapter<AdapterFamilyMamberList.MyViewHolder> {

    SharedPreferences prefrance,prefranceid;
    String sharid;
    private ArrayList<GatterGetFamilyMamberListModel> recyclerModels; // this data structure carries our title and description
    Context context;
    public AdapterFamilyMamberList(ArrayList<GatterGetFamilyMamberListModel> recyclerModels, Context context) {
        this.recyclerModels = recyclerModels;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shipping_address_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // update your data here
        final GatterGetFamilyMamberListModel getAddvertise = recyclerModels.get(position);



        holder.txtusername.setText(getAddvertise.getMember_name());


        holder.txtPhone.setText(getAddvertise.getRealtion());
        holder.select.setVisibility(View.GONE);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("asdf","memberid======="+getAddvertise.getId());

                Intent i=new Intent(context, Family_Mamber_Activity.class);
                i.putExtra("memberid",getAddvertise.getId());
                context.startActivity(i);

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
          public TextView txtusername,txtaddress,txtPhone,txtAddtype;
          Button btnedit;
          RadioButton select;

        MyViewHolder(View view) {
            super(view);
            txtusername =(TextView) itemView.findViewById(R.id.txtusername);
            txtPhone =(TextView) itemView.findViewById(R.id.txtPhone);
            select =(RadioButton) itemView.findViewById(R.id.select);
        }

    }

    public void filterList(ArrayList<GatterGetFamilyMamberListModel> filterdNames) {
        this.recyclerModels = filterdNames;
        Log.i("ass","notifyDataSetChanged===============");
        notifyDataSetChanged();
    }





}
