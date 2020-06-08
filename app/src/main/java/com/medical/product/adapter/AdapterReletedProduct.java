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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.medical.product.R;
import com.medical.product.Ui.FormulasCalculation;
import com.medical.product.Ui.Product_detailActivity;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.models.GatterGetReletedProductList;

import java.util.ArrayList;


public class AdapterReletedProduct extends RecyclerView.Adapter<AdapterReletedProduct.MyViewHolder> {

    SharedPreferences prefrance, prefranceid;
    String sharid;
    private ArrayList<GatterGetReletedProductList> recyclerModels; // this data structure carries our title and description
    Context context;

    public AdapterReletedProduct(ArrayList<GatterGetReletedProductList> recyclerModels, Context context) {
        this.recyclerModels = recyclerModels;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_releted_product, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // update your data here
        final GatterGetReletedProductList getSimilorList = recyclerModels.get(position);
        holder.textViewTitle.setText(getSimilorList.getPd_brand_name());
        Glide.with(context).load(ApiFileuri.ROOT_URL_PRODUCT_IMAGE + getSimilorList.getImage()).into(holder.ivThumb);
        Glide.with(context).load(ApiFileuri.ROOT_URL_PRODUCT_IMAGE + getSimilorList.getImage()).into(holder.ivThumbcopy);

        String saleprice = getSimilorList.getSale_price();
        Double salepricedbl = 0.0, mrppricedbl=0.0, discperprice=0.0;

        try {
            salepricedbl = Double.parseDouble(saleprice);

            mrppricedbl = Double.parseDouble(getSimilorList.getMrp());

            discperprice = FormulasCalculation.CalculateProductDiscount(mrppricedbl, salepricedbl);
        } catch (Exception e) {
            // or some value to mark this field is wrong. or make a function validates field first ...
        }


        holder.txtAddPrice.setText(getSimilorList.getMrp());
        holder.txtorignalprice.setText(context.getString(R.string.Rs)+getSimilorList.getSale_price());

      //  holder.txtVenderName.setText(getSimilorList.getVendor_name());

        holder.txtPriceOffer.setText(discperprice + " % Off");


        holder.ivThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (context instanceof Product_detailActivity) {
                    ((Product_detailActivity) context).ReStartProduct(getSimilorList.getId());
                }

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Product_detailActivity.class);
                intent.putExtra("medname",getSimilorList.getPd_brand_name());
                intent.putExtra("med_id",getSimilorList.getId());
                intent.putExtra("image",getSimilorList.getImage());
                context.startActivity(intent);
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
        TextView textViewTitle, txtAddtocart, txtPrice, txtAddPrice, txtPriceOffer, txtorignalprice, txtVenderName;
        ImageView ivThumb, ivThumbcopy;
        RelativeLayout relAddToCart;
        CardView cardcategory;


        MyViewHolder(View view) {
            super(view);
            textViewTitle = (TextView) itemView.findViewById(R.id.txtTitlehorizontal);
            ivThumb = (ImageView) itemView.findViewById(R.id.ivThumb);
            ivThumbcopy = (ImageView) itemView.findViewById(R.id.ivThumbcopy);
            txtAddtocart = (TextView) itemView.findViewById(R.id.txtAddtocart);
            txtPrice = (TextView) itemView.findViewById(R.id.txtPrice);
            txtAddPrice = (TextView) itemView.findViewById(R.id.txtAddPrice);
            txtPriceOffer = (TextView) itemView.findViewById(R.id.txtPriceOffer);
            txtorignalprice = (TextView) itemView.findViewById(R.id.txtorignalprice);

            txtVenderName = (TextView) itemView.findViewById(R.id.txtVenderName);

            relAddToCart = (RelativeLayout) itemView.findViewById(R.id.relAddToCart);
        }

    }

    public void filterList(ArrayList<GatterGetReletedProductList> filterdNames) {
        this.recyclerModels = filterdNames;
        Log.i("ass", "notifyDataSetChanged===============");
        notifyDataSetChanged();
    }


}
