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
import com.medical.product.models.GatterGetOrderProductListModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AdapterOrderProductList extends RecyclerView.Adapter<AdapterOrderProductList.MyViewHolder> {

    SharedPreferences prefrance, prefranceid;
    String sharid;
    private ArrayList<GatterGetOrderProductListModel> recyclerModels; // this data structure carries our title and description
    Context context;

    public AdapterOrderProductList(ArrayList<GatterGetOrderProductListModel> recyclerModels, Context context) {
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
        final GatterGetOrderProductListModel getAddvertise = recyclerModels.get(position);

        ArrayList<GatterGetCartList> filelist = new Gson().fromJson(getAddvertise.getProducts(), new TypeToken<List<GatterGetCartList>>() {
        }.getType());
        holder.date.setText(date_conva(getAddvertise.getAdd_date()));
        holder.advertTitle.setText(getAddvertise.getOrder_id());
        if (!TextUtils.isEmpty(filelist.get(0).getImage())) {
            Glide.with(context).load(ApiFileuri.ROOT_URL_PRODUCT_IMAGE + filelist.get(0).getImage()).into(holder.imageView);
        }


        holder.cardOrderlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MyOrderDetailActivity.class).putExtra("order_detail", new Gson().toJson(getAddvertise)));
            }
        });
        if(getAddvertise.getConfirmed().equalsIgnoreCase("0")|| getAddvertise.getConfirmed().equalsIgnoreCase("1")) {
            if (getAddvertise.getProcessed().equalsIgnoreCase("0")) {
                holder.status.setText("Ordered");
                holder.imgstauts.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            } else if (getAddvertise.getProcessed().equalsIgnoreCase("1")) {
                holder.status.setText("Packed");
                holder.imgstauts.setBackgroundColor(context.getResources().getColor(R.color.green));
            } else if (getAddvertise.getProcessed().equalsIgnoreCase("2")) {
                holder.status.setText("Shipped");
                holder.imgstauts.setBackgroundColor(context.getResources().getColor(R.color.active_yellow));
            } else {
                holder.status.setText("Delivered");
                holder.imgstauts.setBackgroundColor(context.getResources().getColor(R.color.blue));
            }
        }
         else
        {
            holder.status.setText("Cancelled");
            holder.imgstauts.setBackgroundColor(context.getResources().getColor(R.color.red));
        }

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
        public TextView advertTitle, status,date,imgstauts;
        public CardView cardOrderlist;
        public ImageView imageView;

        MyViewHolder(View view) {
            super(view);
            advertTitle = (TextView) itemView.findViewById(R.id.title);
            status = (TextView) itemView.findViewById(R.id.status);
            imgstauts = (TextView) itemView.findViewById(R.id.imgstauts);
            date = (TextView) itemView.findViewById(R.id.date);
            imageView = (ImageView) itemView.findViewById(R.id.img);
            cardOrderlist = (CardView) itemView.findViewById(R.id.cardOrderlist);

        }

    }

    public void filterList(ArrayList<GatterGetOrderProductListModel> filterdNames) {
        this.recyclerModels = filterdNames;
        Log.i("ass", "notifyDataSetChanged===============");
        notifyDataSetChanged();
    }



    private String date_conva(String date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date sourceDate = null;
        try {
            sourceDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat targetFormat = new SimpleDateFormat("E MMM dd,yyyy hh:mm a");
        return String.valueOf(targetFormat.format(sourceDate));
    }


}
