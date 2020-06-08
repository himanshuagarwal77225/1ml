package com.medical.product.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.medical.product.Ui.FormulasCalculation;
import com.medical.product.R;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.models.GatterGetCartList;

import java.util.ArrayList;


public class AdapterCartProductCheckout extends RecyclerView.Adapter<AdapterCartProductCheckout.MyViewHolder> {
    Keystore store;
    SharedPreferences prefrance,prefranceid;
    String sharid;
    ArrayList<GatterGetCartList> recyclerModels; // this data structure carries our title and description
    Context context;
    String qty="";
    public AdapterCartProductCheckout(ArrayList<GatterGetCartList> recyclerModels, Context context) {
        this.recyclerModels = recyclerModels;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_cart_checkout, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // update your data here
        final GatterGetCartList GatterGetCartList = recyclerModels.get(position);

        holder.txtTitlehorizontal.setText(GatterGetCartList.getMed_name());
        holder.txtsellername.setText(GatterGetCartList.getVendor_name());
        holder.companyname.setText(GatterGetCartList.getCompany_name());
        holder.txtQty.setText(GatterGetCartList.getQuentity());

        Double persent= FormulasCalculation.CalculateProductDiscount(Double.parseDouble(GatterGetCartList.getMrp()),Double.parseDouble(GatterGetCartList.getSale_price()));


        holder.txtPriceOffer.setText(String.valueOf(persent)+ "% off");

//        if(!(GatterGetCartList.getAdditional_offer_start().equals("") || GatterGetCartList.getAdditional_offer_start().equals("null") || GatterGetCartList.getAdditional_offer_start().equals(null)))
//        {
//            if (GatterGetCartList.getAdditional_offer_type().equalsIgnoreCase("percent")) {
//                Double quentity = Double.parseDouble(GatterGetCartList.getQuentity());
//                Double newpricecheck = Double.parseDouble(GatterGetCartList.getSale_price());
//                Double addoffer = Double.parseDouble(GatterGetCartList.getAdditional_offer_percentage());
//                Double price = newpricecheck * quentity;
//                Double newprice = ((price * addoffer) / 100);
//                holder.txtNewPrice.setText(context.getString(R.string.Rs) + String.valueOf(price - newprice));
//                holder.txtAddoffApplyed.setVisibility(View.VISIBLE);
//                holder.txtAddoffApplyed.setText(String.valueOf(addoffer) + " flat additional discount offer Applied");
//            } else {
//                Double quentity = Double.parseDouble(GatterGetCartList.getQuentity());
//                Double newpricecheck = Double.parseDouble(GatterGetCartList.getSale_price());
//                Double addoffer = Double.parseDouble(GatterGetCartList.getAdditional_offer_percentage());
//                Double price = newpricecheck * quentity;
//                holder.txtNewPrice.setText(context.getString(R.string.Rs) + String.valueOf(price - addoffer));
//                holder.txtAddoffApplyed.setVisibility(View.VISIBLE);
//                holder.txtAddoffApplyed.setText(String.valueOf(addoffer) + " % additional discount offer Applied");
//            }
////            Double quentity=Double.parseDouble(GatterGetCartList.getQuentity());
////            Double newpricecheck=Double.parseDouble(GatterGetCartList.getSale_price());
////
////            Double addoffer=Double.parseDouble(GatterGetCartList.getAdditional_offer_percentage());
////
////            Double price= newpricecheck * quentity;
////            Double newprice= ((price*addoffer)/100);
////            holder.txtNewPrice.setText(context.getString(R.string.Rs)+String.valueOf(price-newprice));
////
////            holder.txtAddoffApplyed.setVisibility(View.VISIBLE);
////            holder.txtAddoffApplyed.setText(String.valueOf(addoffer) + " % additional discount offer Applied");
//        }
//        else
//        {
            Double quentity=Double.parseDouble(GatterGetCartList.getQuentity());
            Double newpricecheck=Double.parseDouble(GatterGetCartList.getSale_price());
            Double newprice= newpricecheck*quentity;
            holder.txtNewPrice.setText(context.getString(R.string.Rs)+String.valueOf(newprice));
            holder.txtOldProce.setText(context.getString(R.string.Rs)+String.valueOf(Double.parseDouble(GatterGetCartList.getMrp())));
//        }

        Double quentity2=Double.parseDouble(GatterGetCartList.getQuentity());
        Double newpricecheck2=Double.parseDouble(GatterGetCartList.getMrp());
        Double newprice2= newpricecheck2*quentity2;

        holder.txtOldProce.setText(context.getString(R.string.Rs)+String.valueOf(newprice2));


        if(!GatterGetCartList.getImage().equals(""))
        {
            Glide.with(context).load(ApiFileuri.ROOT_URL_PRODUCT_IMAGE+GatterGetCartList.getImage()).into(holder.ivThumb);
        }




     /*   if(!GatterGetCartList.getImage().equals(""))
        {
            Glide.with(context).load(ApiFileuri.ROOT_URL_PRODUCT_IMAGE+GatterGetCartList.getImage()).into(holder.ivThumb);
        }

        if(GatterGetCartList.getOffer().equals(""))
        {
            methodGetPrice(holder,position);
        }
        else
        {
            holder.txtPriceOffer.setText(GatterGetCartList.getOffer()+"%Off");
            methodGetPrice(holder,position);
        }*/




    }

    /*private void methodGetPrice(MyViewHolder holder, int position) {
        if(recyclerModels.get(position).getOffer().equals("")){
            //int inttotprice= (Integer.parseInt(recyclerModels.get(position).getPrice()))*(Integer.parseInt(recyclerModels.get(position).getQuentity()));

            int inttotprice= (Integer.parseInt(recyclerModels.get(position).getPrice()));
            int intqty= (Integer.parseInt(recyclerModels.get(position).getQuentity()));

            String strt= String.valueOf(inttotprice*intqty);
            Log.i("less","lessprice=========inttotprice===="+strt);
            holder.txtNewPrice.setText(strt);
            holder.txtOldProce.setVisibility(View.GONE);
            //     holder.txtOldProce.setText("sadfasdfas");
            //   holder.txtNewPrice.setText("sadfsdafsdf");
        }


        else if( !recyclerModels.get(position).getOffer().equals(""))
        {
            int intoffer= Integer.parseInt(recyclerModels.get(position).getOffer());

            int inttotprice= (Integer.parseInt(recyclerModels.get(position).getPrice()))*(Integer.parseInt(recyclerModels.get(position).getQuentity()));

            int priceless = (int)(inttotprice*(intoffer/100.0f));

            int newprice=inttotprice-priceless;

            holder.txtOldProce.setText(String.valueOf(inttotprice));
            holder.txtNewPrice.setText(String.valueOf(newprice));
        }
    }*/


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
        TextView txtTitlehorizontal,companyname,txtCutRate,txtPriceOffer,txtAddPrice,txtQuantity,txtInStock,txtNewPrice,txtOldProce,txtsellername,txtQty,txtAddoffApplyed;
        Button BtnQuentityMinus,BtnQuentityPlus,removeProduct;
        LinearLayout layQuantity;
        ImageView ivThumb;
        MyViewHolder(View view) {
            super(view);
            txtTitlehorizontal=(TextView)itemView.findViewById(R.id.txtTitlehorizontal);
            ivThumb=(ImageView)itemView.findViewById(R.id.ivThumb);
            txtNewPrice=(TextView)itemView.findViewById(R.id.txtNewPrice);
            txtsellername=(TextView)itemView.findViewById(R.id.txtsellername);
            // txtCutRate=(TextView)itemView.findViewById(R.id.txtCutRate);
            txtPriceOffer=(TextView)itemView.findViewById(R.id.txtPriceOffer);
            txtOldProce=(TextView)itemView.findViewById(R.id.txtOldProce);
            txtInStock=(TextView)itemView.findViewById(R.id.txtInStock);
            txtQty=(TextView)itemView.findViewById(R.id.txtQty);
            layQuantity=(LinearLayout)itemView.findViewById(R.id.layQuantity);
            txtAddoffApplyed=(TextView)itemView.findViewById(R.id.txtAddoffApplyed);
            companyname=(TextView)itemView.findViewById(R.id.companynamee);
        }

    }

    public void filterList(ArrayList<GatterGetCartList> filterdNames) {
        this.recyclerModels = filterdNames;
        Log.i("ass","notifyDataSetChanged===============");
        notifyDataSetChanged();
    }
}
