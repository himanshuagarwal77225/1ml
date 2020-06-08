package com.medical.product.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;

import com.medical.product.R;
import com.medical.product.models.GatterGetVendersModel;

import java.util.ArrayList;


public class AdapterFeedbacklist extends RecyclerView.Adapter<AdapterFeedbacklist.MyViewHolder> {
    private boolean zoomOut = false;
    SharedPreferences prefrance, prefranceid;
    String sharid;
    private ArrayList<GatterGetVendersModel> recyclerModels; // this data structure carries our title and description
    Context context;

    public AdapterFeedbacklist(ArrayList<GatterGetVendersModel> recyclerModels, Context context) {
        this.recyclerModels = recyclerModels;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // update your data here
        final GatterGetVendersModel getAddvertise = recyclerModels.get(position);
        //  holder.advertTitle.setText(getAddvertise.getTitle());
        holder.txtpharm_name.setText(getAddvertise.getPharm_name());
        holder.comments.setText(getAddvertise.getStore_address());
        holder.orderid.setText(getAddvertise.getName());
        float rate = 0.0f;
        try {
            rate = Float.parseFloat(getAddvertise.getAverage_rating());
        } catch (Exception e) {
            // or some value to mark this field is wrong. or make a function validates field first ...
        }
        holder.ratingbar.setRating(rate);
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
        public TextView txtpharm_name,comments,orderid;
        RatingBar ratingbar;
        MyViewHolder(View view) {
            super(view);
            txtpharm_name = (TextView) itemView.findViewById(R.id.txtpharm_name);
            comments = (TextView) itemView.findViewById(R.id.comments);
            orderid = (TextView) itemView.findViewById(R.id.orderid);
            ratingbar = (RatingBar) itemView.findViewById(R.id.ratingbar);
        }


    }

    /*public void filterList(ArrayList<GatterGetFamilyDocumentListModel> filterdNames) {
        this.recyclerModels = filterdNames;
        Log.i("ass","notifyDataSetChanged===============");
        notifyDataSetChanged();
    }*/


}
