package com.medical.product.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medical.product.R;
import com.medical.product.Ui.MyOrderDetailActivity;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.models.GatterGetCartList;
import com.medical.product.models.GatterGetMyOrderListModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class AdapterOrderList extends RecyclerView.Adapter<AdapterOrderList.MyViewHolder> {

    SharedPreferences prefrance, prefranceid;
    String sharid;
    private ArrayList<GatterGetMyOrderListModel> recyclerModels; // this data structure carries our title and description
    Context context;

    public AdapterOrderList(ArrayList<GatterGetMyOrderListModel> recyclerModels, Context context) {
        this.recyclerModels = recyclerModels;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // update your data here
        final GatterGetMyOrderListModel getAddvertise = recyclerModels.get(position);
        holder.date.setText(change_date_formate(getAddvertise.getAdd_date()));
        ArrayList<GatterGetCartList> filelist = new Gson().fromJson(getAddvertise.getProducts(), new TypeToken<List<GatterGetCartList>>() {}.getType());

        holder.advertTitle.setText(getAddvertise.getOrder_id());

        if (!TextUtils.isEmpty(filelist.get(0).getImage())) {
            Glide.with(context).load(ApiFileuri.ROOT_URL_PRODUCT_IMAGE + filelist.get(0).getImage()).into(holder.img);
        }

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
        public TextView advertTitle,status,date;
        public CardView cardOrderlist;
        public ImageView img;

        MyViewHolder(View view) {
            super(view);
            advertTitle = (TextView) itemView.findViewById(R.id.title);
            date = (TextView) itemView.findViewById(R.id.date);
            status = (TextView) itemView.findViewById(R.id.status);
            img = (ImageView) itemView.findViewById(R.id.img);
            cardOrderlist = (CardView) itemView.findViewById(R.id.cardOrderlist);
        }
    }

    public void filterList(ArrayList<GatterGetMyOrderListModel> filterdNames) {
        this.recyclerModels = filterdNames;
        notifyDataSetChanged();
    }

    private String change_date_formate(String date)
    {
        String strDateTime="";
        DateFormat iFormatter = new SimpleDateFormat("yyyy-MM-dd h:mm:s");
        DateFormat oFormatter = new SimpleDateFormat("E dd MMM,yyyy");
        try {
            strDateTime = oFormatter.format(iFormatter.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDateTime;
    }



}
