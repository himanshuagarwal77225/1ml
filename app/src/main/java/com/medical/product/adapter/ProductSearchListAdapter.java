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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.medical.product.Ui.Product_detailActivity;
import com.medical.product.R;
import com.medical.product.Ui.SearchProductListActivity;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.ModelProductSearchList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductSearchListAdapter extends RecyclerView.Adapter<ProductSearchListAdapter.HorizontalRVViewHolder> {
    private Keystore store;
    Context context;
    ArrayList<ModelProductSearchList> arrayList;

    public ProductSearchListAdapter(Context context, ArrayList<ModelProductSearchList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public HorizontalRVViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vertical_categoryproduct, parent, false);
        return new HorizontalRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HorizontalRVViewHolder holder, final int position) {

        final ModelProductSearchList horizontalModelProducts = arrayList.get(position);
        holder.textViewTitle.setText(horizontalModelProducts.getPd_brand_name());
        Glide.with(context).load(ApiFileuri.ROOT_URL_PRODUCT_IMAGE + horizontalModelProducts.getImage()).into(holder.ivThumb);
        Glide.with(context).load(ApiFileuri.ROOT_URL_PRODUCT_IMAGE + horizontalModelProducts.getImage()).into(holder.ivThumbcopy);

        String saleprice = horizontalModelProducts.getSale_price();
        Double salepricedbl = Double.parseDouble(saleprice);

        Double mrppricedbl = Double.parseDouble(horizontalModelProducts.getMrp());

        Double discperprice = FormulasCalculation.CalculateProductDiscount(mrppricedbl, salepricedbl);


        holder.txtAddPrice.setText(horizontalModelProducts.getMrp());
        holder.txtorignalprice.setText(horizontalModelProducts.getSale_price());

        holder.txtPriceOffer.setText(discperprice + " % Off");

        holder.chkwish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    String urlis = ApiFileuri.ROOT_HTTP_URL + "product/add-wishlist";
                    WishList("addwish", horizontalModelProducts.getId(), urlis);
                }
                if (isChecked == false) {
                    String urlis = ApiFileuri.ROOT_HTTP_URL + "product/remove-wishlist";
                    WishList("removeremove", horizontalModelProducts.getId(), urlis);
                }
            }
        });


        holder.txtAddtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = ReuseMethod.addToCardDatabase(context, horizontalModelProducts.getId(), "1");
                if (context instanceof SearchProductListActivity) {
                    ((SearchProductListActivity) context).setvaluecart();
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

                        if (context instanceof SearchProductListActivity) {
                            ((SearchProductListActivity) context).setvaluecart();
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
        TextView textViewTitle, txtAddPrice, txtorignalprice, txtPriceOffer;
        ImageView ivThumb, ivThumbcopy;
        RelativeLayout relAddToCart;
        LinearLayout txtAddtocart;
        CardView cardcategory;
        CheckBox chkwish;
        LinearLayout layPriceoffer;

        public HorizontalRVViewHolder(View itemView) {
            super(itemView);
            Typeface typeFace_Rupee = Typeface.createFromAsset(context.getAssets(), "fonts/Rupee_Foradian.ttf");
            textViewTitle = (TextView) itemView.findViewById(R.id.txtTitlehorizontal);
            ivThumb = (ImageView) itemView.findViewById(R.id.ivThumb);
            layPriceoffer = (LinearLayout) itemView.findViewById(R.id.layPriceoffer);
            ivThumbcopy = (ImageView) itemView.findViewById(R.id.ivThumbcopy);
            txtAddtocart = (LinearLayout) itemView.findViewById(R.id.txtAddtocart);
            txtAddPrice = (TextView) itemView.findViewById(R.id.txtAddPrice);
            txtPriceOffer = (TextView) itemView.findViewById(R.id.txtPriceOffer);
            txtorignalprice = (TextView) itemView.findViewById(R.id.txtorignalprice);
            relAddToCart = (RelativeLayout) itemView.findViewById(R.id.relAddToCart);
            chkwish = (CheckBox) itemView.findViewById(R.id.chkwish);
        }


    }

    private void WishList(final String wish, final String medid, String uriis) {
        store = Keystore.getInstance(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uriis,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            String strstatus = obj.getString("status");
                            if (strstatus.equals("true")) {
                                if (wish.equals("removeremove")) {
                                    Toast.makeText(context, obj.getString("message").toString(), Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(context, obj.getString("product").toString(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("asdf", "listwish==================" + error);

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                if (wish.equals("addwish")) {
                    params.put("product_id", medid);
                    params.put("user_id", store.get("id"));
                }

                if (wish.equals("removeremove")) {
                    params.put("product_id", medid);
                    params.put("user_id", store.get("id"));
                }
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }


}
