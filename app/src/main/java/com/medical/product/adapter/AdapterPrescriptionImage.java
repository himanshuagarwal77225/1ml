package com.medical.product.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.medical.product.R;
import com.medical.product.Ui.Full_image;
import com.medical.product.Ui.UploadPrescriptionActivity;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.models.GatterGetPrescriptionImage;

import java.util.ArrayList;


public class AdapterPrescriptionImage extends RecyclerView.Adapter<AdapterPrescriptionImage.MyViewHolder> {

    SharedPreferences prefrance,prefranceid;
    String sharid;
    private ArrayList<GatterGetPrescriptionImage> recyclerModels; // this data structure carries our title and description
    Context context;
    public AdapterPrescriptionImage(ArrayList<GatterGetPrescriptionImage> recyclerModels, Context context) {
        this.recyclerModels = recyclerModels;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.prescription_image, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // update your data here
        final GatterGetPrescriptionImage getAddvertise = recyclerModels.get(position);

        if(getAddvertise.getType().equals("uriimg"))
        {
            Glide.with(context).load(ApiFileuri.ROOT_URL_USER_IMAGE+getAddvertise.getImage()).into(holder.imgview);
        }

        if(getAddvertise.getType().equals("bitmap"))
        {
            holder.imgview.setImageURI(Uri.parse(getAddvertise.getImage()));
        }

       holder.imgbtnCut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
         delete(position,getAddvertise.getType(),getAddvertise.getArrid());
         }
        });

        holder.imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!getAddvertise.getType().equals("bitmap")) {
                    context.startActivity(new Intent(context, Full_image.class).putExtra("img", ApiFileuri.ROOT_URL_USER_IMAGE + getAddvertise.getImage()).putExtra("type", getAddvertise.getType()));
                }
                else
                {
                    context.startActivity(new Intent(context, Full_image.class).putExtra("img", getAddvertise.getImage()).putExtra("type", getAddvertise.getType()));

                }

            }
        });




    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
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

          ImageView imgview;
            ImageButton imgbtnCut;

        MyViewHolder(View view) {
            super(view);
            imgview=(ImageView) itemView.findViewById(R.id.imgview);
            imgbtnCut=(ImageButton) itemView.findViewById(R.id.imgbtnCut);

        }

    }

   /* public void filterList(ArrayList<GatterGetAddressListModel> filterdNames) {
        this.recyclerModels = filterdNames;
        Log.i("ass","notifyDataSetChanged===============");
        notifyDataSetChanged();
    }*/


    public void delete(int position, String type, String arrid) {
        recyclerModels.remove(position);
        notifyItemRemoved(position);
        if(context instanceof UploadPrescriptionActivity){
            ((UploadPrescriptionActivity)context).setArrayPrescription(type,arrid);
        }

       // ReuseMethod.SetTotalCartItem(context,String.valueOf(recyclerModels.size()));
    }


}
