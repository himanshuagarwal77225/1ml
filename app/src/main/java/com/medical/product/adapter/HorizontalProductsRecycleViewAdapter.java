package com.medical.product.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.medical.product.R;
import com.medical.product.Ui.Dashbord;
import com.medical.product.Ui.FormulasCalculation;
import com.medical.product.Ui.ProductCategory;
import com.medical.product.Ui.Product_detailActivity;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.models.HorizontalModelProducts;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HorizontalProductsRecycleViewAdapter extends RecyclerView.Adapter<HorizontalProductsRecycleViewAdapter.HorizontalRVViewHolder> {
    private Keystore store;
    Context context;
    ArrayList<HorizontalModelProducts> arrayList;


    public HorizontalProductsRecycleViewAdapter(Context context, ArrayList<HorizontalModelProducts> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public HorizontalRVViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_product, parent, false);
        return new HorizontalRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HorizontalRVViewHolder holder, final int position) {

        final HorizontalModelProducts horizontalModelProducts = arrayList.get(position);
        holder.textViewTitle.setText(horizontalModelProducts.getPd_brand_name());
        Glide.with(context).load(ApiFileuri.ROOT_URL_PRODUCT_IMAGE + horizontalModelProducts.getImage()).into(holder.ivThumb);
        Glide.with(context).load(ApiFileuri.ROOT_URL_PRODUCT_IMAGE + horizontalModelProducts.getImage()).into(holder.ivThumbcopy);

        String saleprice = horizontalModelProducts.getSale_price();
        Double salepricedbl=0.0, mrppricedbl=0.0, discperprice=0.0;
        try {
            salepricedbl = Double.parseDouble(saleprice);

            mrppricedbl = Double.parseDouble(horizontalModelProducts.getMrp());

            discperprice = FormulasCalculation.CalculateProductDiscount(mrppricedbl, salepricedbl);
        } catch (Exception e) {
            // or some value to mark this field is wrong. or make a function validates field first ...
        }

        if(horizontalModelProducts.getQty().equalsIgnoreCase("0")||horizontalModelProducts.getStore_status().equalsIgnoreCase("0"))
        {
            holder.llcart.setVisibility(View.GONE);
        }

        holder.txtAddPrice.setText(horizontalModelProducts.getMrp());
        holder.txtorignalprice.setText(horizontalModelProducts.getSale_price());
        holder.txtAddPrice.setPaintFlags(holder.txtAddPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.txtPriceOffer.setText(discperprice + " % Off");


        //    methodGetPrice(holder,position);

       /* if(horizontalModelProducts.getOffer().equals(""))
        {
            methodGetPrice(holder,position);

        }
        else
        {

            methodGetPrice(holder,position);


        }*/


        holder.txtAddtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = ReuseMethod.addToCardDatabase(context, horizontalModelProducts.getId(), "1");
                if (context instanceof Dashbord) {
                    ((Dashbord) context).setvaluecart();
                }
                JSONObject obj = null;
                try {
                    obj = new JSONObject(result);

                    String strstatus = obj.getString("status");

                    if (strstatus.equals("false")) {
                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        ReuseMethod.SetTotalCartItem(context, obj.getString("cart-total"));
                        // ReuseMethod.setCartNotifiy(context);
                        if (context instanceof Dashbord) {
                            ((Dashbord) context).setvaluecart();
                        }
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(context, Product_detailActivity.class);
                    intent.putExtra("medname", horizontalModelProducts.getPd_brand_name());
                    intent.putExtra("med_id", horizontalModelProducts.getId());
                    intent.putExtra("image", horizontalModelProducts.getImage());
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class HorizontalRVViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, txtAddtocart, txtAddPrice, txtorignalprice, txtPriceOffer,cartout;
        ImageView ivThumb, ivThumbcopy,img;
        FrameLayout relAddToCart;
        CardView cardcategory;
        LinearLayout layPriceoffer,llcart;

        public HorizontalRVViewHolder(View itemView) {
            super(itemView);
            Typeface typeFace_Rupee = Typeface.createFromAsset(context.getAssets(), "fonts/Rupee_Foradian.ttf");
            textViewTitle = (TextView) itemView.findViewById(R.id.txtTitlehorizontal);
            ivThumb = (ImageView) itemView.findViewById(R.id.ivThumb);
            layPriceoffer = (LinearLayout) itemView.findViewById(R.id.layPriceoffer);
            ivThumbcopy = (ImageView) itemView.findViewById(R.id.ivThumbcopy);
            txtAddtocart = (TextView) itemView.findViewById(R.id.txtAddtocart);
            txtAddPrice = (TextView) itemView.findViewById(R.id.txtAddPrice);
            txtPriceOffer = (TextView) itemView.findViewById(R.id.txtPriceOffer);
            txtorignalprice = (TextView) itemView.findViewById(R.id.txtorignalprice);
            relAddToCart = (FrameLayout) itemView.findViewById(R.id.relAddToCart);
            llcart = (LinearLayout) itemView.findViewById(R.id.llcart);

        }


    }


}
