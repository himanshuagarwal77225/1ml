package com.medical.product.adapter;

import android.content.Context;
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
import com.medical.product.Ui.FormulasCalculation;
import com.medical.product.Ui.Product_detailActivity;
import com.medical.product.R;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.models.GatterGetSimilorList;

import java.util.ArrayList;


public class AdapterSimilarProduct extends RecyclerView.Adapter<AdapterSimilarProduct.MyViewHolder> {

    SharedPreferences prefrance, prefranceid;
    String sharid;
    private ArrayList<GatterGetSimilorList> recyclerModels; // this data structure carries our title and description
    Context context;

    public AdapterSimilarProduct(ArrayList<GatterGetSimilorList> recyclerModels, Context context) {
        this.recyclerModels = recyclerModels;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_similor_product, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // update your data here
        final GatterGetSimilorList getSimilorList = recyclerModels.get(position);
        holder.textViewTitle.setText(getSimilorList.getPd_brand_name());
        Glide.with(context).load(ApiFileuri.ROOT_URL_PRODUCT_IMAGE + getSimilorList.getImage()).into(holder.ivThumb);
        Glide.with(context).load(ApiFileuri.ROOT_URL_PRODUCT_IMAGE + getSimilorList.getImage()).into(holder.ivThumbcopy);

        String saleprice = getSimilorList.getSale_price();
        Double salepricedbl = Double.parseDouble(saleprice);

        Double mrppricedbl = Double.parseDouble(getSimilorList.getMrp());

        Double discperprice = FormulasCalculation.CalculateProductDiscount(mrppricedbl, salepricedbl);

        holder.txtAddPrice.setText(getSimilorList.getMrp());
        holder.txtorignalprice.setText(context.getString(R.string.Rs) + getSimilorList.getSale_price());
        //  holder.txtAddPrice.setText(getSimilorList.getSale_price());


        holder.txtPriceOffer.setText(discperprice + " % Off");

        holder.txtAddtocart.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof Product_detailActivity) {
                    ((Product_detailActivity) context).ReStartProduct(getSimilorList.getId());
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
        TextView textViewTitle, txtAddtocart, txtPrice, txtAddPrice, txtPriceOffer, txtorignalprice;
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
            relAddToCart = (RelativeLayout) itemView.findViewById(R.id.relAddToCart);
        }

    }

    public void filterList(ArrayList<GatterGetSimilorList> filterdNames) {
        this.recyclerModels = filterdNames;
        Log.i("ass", "notifyDataSetChanged===============");
        notifyDataSetChanged();
    }


}
