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
import com.medical.product.Ui.CheckoutActivity;
import com.medical.product.Ui.Full_image;
import com.medical.product.Ui.UploadPrescriptionActivity;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.models.GatterGetPrescriptionImage;

import java.util.ArrayList;


public class AdapterPrescriptionImageorder extends RecyclerView.Adapter<AdapterPrescriptionImageorder.MyViewHolder> {

    SharedPreferences prefrance, prefranceid;
    String sharid;
    private ArrayList<Uri> recyclerModels; // this data structure carries our title and description
    Context context;

    public AdapterPrescriptionImageorder(ArrayList<Uri> recyclerModels, Context context) {
        this.recyclerModels = recyclerModels;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.prescription_image, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // update your data here
        final Uri getAddvertise = recyclerModels.get(position);

        holder.imgview.setImageURI(getAddvertise);
        holder.imgbtnCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(position);
            }
        });

        holder.imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Full_image.class).putExtra("img", getAddvertise).putExtra("type", "bitmap"));
            }
        });


    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
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
            imgview = (ImageView) itemView.findViewById(R.id.imgview);
            imgbtnCut = (ImageButton) itemView.findViewById(R.id.imgbtnCut);

        }

    }

   /* public void filterList(ArrayList<GatterGetAddressListModel> filterdNames) {
        this.recyclerModels = filterdNames;
        Log.i("ass","notifyDataSetChanged===============");
        notifyDataSetChanged();
    }*/


    public void delete(int position) {
        recyclerModels.remove(position);
        notifyItemRemoved(position);
        if (context instanceof UploadPrescriptionActivity) {
            ((CheckoutActivity) context).setArrayPrescription(position);
        }

    }


}
