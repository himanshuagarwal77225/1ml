package com.medical.product.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.medical.product.Ui.FormulasCalculation;
import com.medical.product.Ui.ProductCategory;
import com.medical.product.Ui.Product_detailActivity;
import com.medical.product.R;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.models.HorizontalModelProducts;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static java.lang.Integer.parseInt;

public class HorizontalProductsCatehoryRecycleViewAdapter extends RecyclerView.Adapter<HorizontalProductsCatehoryRecycleViewAdapter.HorizontalRVViewHolder> {

    Context context;
    static ArrayList<HorizontalModelProducts> arrayList;


    public HorizontalProductsCatehoryRecycleViewAdapter(Context context, ArrayList<HorizontalModelProducts> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public HorizontalRVViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_similor_product, parent, false);
        return new HorizontalRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HorizontalRVViewHolder holder, final int position) {

        final HorizontalModelProducts horizontalModelProducts = arrayList.get(position);

        holder.textViewTitle.setText(horizontalModelProducts.getPd_brand_name());

        Glide.with(context).load(ApiFileuri.ROOT_URL_PRODUCT_IMAGE + horizontalModelProducts.getImage()).into(holder.ivThumb);
        Glide.with(context).load(ApiFileuri.ROOT_URL_PRODUCT_IMAGE + horizontalModelProducts.getImage()).into(holder.ivThumbcopy);


        String saleprice = horizontalModelProducts.getSale_price();
        Double salepricedbl = Double.parseDouble(saleprice);

        Double mrppricedbl = Double.parseDouble(horizontalModelProducts.getMrp());

        Double discperprice = FormulasCalculation.CalculateProductDiscount(mrppricedbl, salepricedbl);


        holder.txtAddPrice.setText(horizontalModelProducts.getMrp());
        holder.txtorignalprice.setText(context.getString(R.string.Rs) + horizontalModelProducts.getSale_price());

        holder.txtPriceOffer.setText(discperprice + " % Off");

        if (horizontalModelProducts.getQty().equalsIgnoreCase("0")||horizontalModelProducts.getStore_status().equalsIgnoreCase("0")) {
            holder.txtAddtocart.setVisibility(View.GONE);
        }

        // methodGetPrice(holder,position);
        holder.txtAddtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String result = ReuseMethod.addToCardDatabase(context, horizontalModelProducts.getId(), "1");

                Log.i("asd", "asdfasdf====asdf===result===" + result);

                JSONObject obj = null;
                try {
                    obj = new JSONObject(result);
                    String strstatus = obj.getString("status");

                    if (strstatus.equals("false")) {
                        Toast.makeText(context, obj.getString("product"), Toast.LENGTH_SHORT).show();
                    } else {
                        ReuseMethod.SetTotalCartItem(context, obj.getString("cart-total"));

                        Toast.makeText(context, obj.getString("product"), Toast.LENGTH_SHORT).show();

                        // ReuseMethod.setCartNotifiy(context);

                        if (context instanceof ProductCategory) {
                            ((ProductCategory) context).setvaluecart();
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

                Intent intent = new Intent(context, Product_detailActivity.class);
                intent.putExtra("medname", horizontalModelProducts.getPd_brand_name());
                intent.putExtra("med_id", horizontalModelProducts.getId());
                intent.putExtra("image", horizontalModelProducts.getImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class HorizontalRVViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, txtAddtocart, txtAddPrice, txtorignalprice, txtPriceOffer;
        ImageView ivThumb, ivThumbcopy;
        RelativeLayout relAddToCart;
        CardView cardcategory;
        LinearLayout layPriceoffer;

        public HorizontalRVViewHolder(View itemView) {
            super(itemView);
            textViewTitle = (TextView) itemView.findViewById(R.id.txtTitlehorizontal);
            ivThumb = (ImageView) itemView.findViewById(R.id.ivThumb);
            layPriceoffer = (LinearLayout) itemView.findViewById(R.id.layPriceoffer);
            ivThumbcopy = (ImageView) itemView.findViewById(R.id.ivThumbcopy);
            txtAddtocart = (TextView) itemView.findViewById(R.id.txtAddtocart);
            txtAddPrice = (TextView) itemView.findViewById(R.id.txtAddPrice);
            txtorignalprice = (TextView) itemView.findViewById(R.id.txtorignalprice);
            txtPriceOffer = (TextView) itemView.findViewById(R.id.txtPriceOffer);
            relAddToCart = (RelativeLayout) itemView.findViewById(R.id.relAddToCart);

        }


    }

    public static void getFilter(String strordertype) {
        ArrayList<HorizontalModelProducts> arrayLists = new ArrayList<>();
            if (strordertype.equals("asc")) {
                Collections.sort(arrayList, new Comparator<HorizontalModelProducts>() {
                    @Override
                    public int compare(HorizontalModelProducts p1, HorizontalModelProducts p2) {
                        return parseInt(p1.getMrp()) - parseInt(p2.getMrp()); // Ascending
                    }
                });

            } else if (strordertype.equals("desc")) {
                Collections.sort(arrayList, new Comparator<HorizontalModelProducts>() {
                    @Override
                    public int compare(HorizontalModelProducts p1, HorizontalModelProducts p2) {
                        return parseInt(p2.getMrp()) - parseInt(p1.getMrp()); // Ascending
                    }
                });

            }
            else if (strordertype.equals("oldlist")) {
                Collections.sort(arrayList, new Comparator<HorizontalModelProducts>() {
                    @Override
                    public int compare(HorizontalModelProducts p1, HorizontalModelProducts p2) {

                        return parseInt(p2.getMrp()) - parseInt(p1.getMrp()); // Ascending

                    }
                });

            }

        VerticalRecycleProdCategAdapter.notufy();

    }

}
