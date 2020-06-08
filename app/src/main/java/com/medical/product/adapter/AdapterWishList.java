package com.medical.product.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.medical.product.Ui.FormulasCalculation;
import com.medical.product.Ui.MyWishList;
import com.medical.product.Ui.Product_detailActivity;
import com.medical.product.R;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.GatterGetMyWishListModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterWishList extends RecyclerView.Adapter<AdapterWishList.HorizontalRVViewHolder> {
    private Keystore store;
    Context context;
    ArrayList<GatterGetMyWishListModel> arrayList;

    public AdapterWishList(ArrayList<GatterGetMyWishListModel> arrayList, Context context) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @Override
    public HorizontalRVViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vertical_wishlist, parent, false);
        return new HorizontalRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HorizontalRVViewHolder holder, final int position) {

        final GatterGetMyWishListModel horizontalModelProducts = arrayList.get(position);
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

        holder.txtRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlis = ApiFileuri.ROOT_HTTP_URL + "product/remove-wishlist";
                WishList(horizontalModelProducts.getId(), urlis, position);
            }
        });

        if (horizontalModelProducts.getQty().equalsIgnoreCase("0") || horizontalModelProducts.getStore_status().equalsIgnoreCase("0")) {

            holder.txtAddtocart.setVisibility(View.GONE);
            holder.stock.setVisibility(View.VISIBLE);

        }

        if (horizontalModelProducts.getStore_status().equalsIgnoreCase("0")) {
            holder.offline.setVisibility(View.VISIBLE);
        }

        holder.txtAddtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                store = Keystore.getInstance(context);

                String result = ReuseMethod.addToCardDatabase(context, horizontalModelProducts.getId(), "1");

                if (context instanceof MyWishList) {
                    ((MyWishList) context).setvaluecart();
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

                        if (context instanceof MyWishList) {
                            ((MyWishList) context).setvaluecart();
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
        Log.i("as", "yyyyyyyyyy=======arrayListsize" + arrayList.size());
        return arrayList.size();
    }

    public class HorizontalRVViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, txtAddtocart, txtAddPrice, txtorignalprice, txtPriceOffer, txtRemove, offline,stock;
        ImageView ivThumb, ivThumbcopy;
        RelativeLayout relAddToCart;
        CardView cardcategory;
        LinearLayout layPriceoffer;

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
            relAddToCart = (RelativeLayout) itemView.findViewById(R.id.relAddToCart);
            txtRemove = (TextView) itemView.findViewById(R.id.txtRemove);
            offline = (TextView) itemView.findViewById(R.id.offline);
            stock = (TextView) itemView.findViewById(R.id.stock);

        }


    }

    private void WishList(final String pid, String uriis, final int pos) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uriis,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            String strstatus = obj.getString("status");


                            if (strstatus.equals("true")) {
                                Toast.makeText(context, obj.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                delete(pos);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("asdf", "sdfsadfsadfsfsafresponse====================" + response);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                store = Keystore.getInstance(context);

                params.put("product_id", pid);
                params.put("user_id", store.get("id"));
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public void delete(int position) {
        arrayList.remove(position);
        notifyItemRemoved(position);
        if (context instanceof MyWishList) {
            ((MyWishList) context).OnRemoveItem();
        }
    }


}
