package com.medical.product.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medical.product.Ui.MyOrderDetailActivity;
import com.medical.product.R;
import com.medical.product.models.GatterGetMyPreasriptionOrderListModel;

import java.util.ArrayList;


public class AdapterOrderPrescriptionList extends RecyclerView.Adapter<AdapterOrderPrescriptionList.MyViewHolder> {

    SharedPreferences prefrance,prefranceid;
    String sharid;
    private ArrayList<GatterGetMyPreasriptionOrderListModel> recyclerModels; // this data structure carries our title and description
    Context context;
    public AdapterOrderPrescriptionList(ArrayList<GatterGetMyPreasriptionOrderListModel> recyclerModels, Context context) {
        this.recyclerModels = recyclerModels;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // update your data here
        final GatterGetMyPreasriptionOrderListModel getAddvertise = recyclerModels.get(position);


      //  holder.advertTitle.setText(getAddvertise.getTitle());

        holder.cardOrderlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MyOrderDetailActivity.class));
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
          public TextView advertTitle;
            public CardView cardOrderlist;

        MyViewHolder(View view) {
            super(view);
          //  advertTitle =(TextView) itemView.findViewById(R.id.advertTitle);
            cardOrderlist=(CardView) itemView.findViewById(R.id.cardOrderlist);

        }

    }

    public void filterList(ArrayList<GatterGetMyPreasriptionOrderListModel> filterdNames) {
        this.recyclerModels = filterdNames;
        Log.i("ass","notifyDataSetChanged===============");
        notifyDataSetChanged();
    }



}
