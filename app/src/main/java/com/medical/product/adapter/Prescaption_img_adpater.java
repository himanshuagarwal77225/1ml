package com.medical.product.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.medical.product.R;
import com.medical.product.Ui.Accepts_orders;
import com.medical.product.Ui.Full_image;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.models.Prescription;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Prescaption_img_adpater extends RecyclerView.Adapter<Prescaption_img_adpater.MyViewHolder> {

    SharedPreferences prefrance, prefranceid;
    String sharid;
    private String[] recyclerModels; // this data structure carries our title and description
    Context context;
    Gson gson = new Gson();

    public Prescaption_img_adpater(String[] recyclerModels, Context context) {
        this.recyclerModels = recyclerModels;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_img, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // update your data here
        Glide.with(context)
                .load(ApiFileuri.ROOT_URL_USER_IMAGE + recyclerModels[position])
                .fitCenter()
                .into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Full_image.class).putExtra("img", ApiFileuri.ROOT_URL_USER_IMAGE + recyclerModels[position]));

            }
        });
    }


    @Override
    public int getItemCount() {
        return recyclerModels.length;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;

        MyViewHolder(View view) {
            super(view);
            //for  type=1
            img = (ImageView) itemView.findViewById(R.id.img);

        }

    }


}
