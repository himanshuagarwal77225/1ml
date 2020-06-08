package com.medical.product.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
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
import com.medical.product.R;
import com.medical.product.Ui.FormulasCalculation;
import com.medical.product.Ui.ProductCategory;
import com.medical.product.Ui.Product_detailActivity;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.HorizontalModelProducts;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static com.medical.product.helpingFile.ReuseMethod.store;
import static java.lang.Integer.parseInt;

public class HorizontalCategoryProductAdapter extends RecyclerView.Adapter<HorizontalCategoryProductAdapter.HorizontalRVViewHolder> {

    static Context context;
    static ArrayList<HorizontalModelProducts> arrayList;

    static ArrayList<HorizontalModelProducts> arrayListsold;


    public HorizontalCategoryProductAdapter(Context context, ArrayList<HorizontalModelProducts> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        arrayListsold = arrayList;

    }

    @Override
    public HorizontalRVViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vertical_categoryproduct, parent, false);

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

        if (horizontalModelProducts.getQty().equalsIgnoreCase("0") || horizontalModelProducts.getStore_status().equalsIgnoreCase("0")) {
            holder.txtAddtocart.setVisibility(View.GONE);
        }
        if (horizontalModelProducts.getQty().equalsIgnoreCase("0")) {
            holder.stock.setVisibility(View.VISIBLE);
        }
        holder.txtAddtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String result = ReuseMethod.addToCardDatabase(context, horizontalModelProducts.getId(), "1");
                JSONObject obj = null;
                try {
                    obj = new JSONObject(result);
                    String strstatus = obj.getString("status");

                    if (context instanceof ProductCategory) {
                        ((ProductCategory) context).setvaluecart();
                    }

                    if (strstatus.equals("false")) {
                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        ReuseMethod.SetTotalCartItem(context, obj.getString("cart-total"));

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

        holder.detail.setOnClickListener(new View.OnClickListener() {
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

    public class HorizontalRVViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, txtPrice, txtAddPrice, txtorignalprice, txtPriceOffer, stock;
        ImageView ivThumb, ivThumbcopy;
        RelativeLayout relAddToCart;
        CardView cardcategory;
        CheckBox chkwish;
        LinearLayout detail, txtAddtocart;

        public HorizontalRVViewHolder(View itemView) {
            super(itemView);
            textViewTitle = (TextView) itemView.findViewById(R.id.txtTitlehorizontal);
            ivThumb = (ImageView) itemView.findViewById(R.id.ivThumb);
            ivThumbcopy = (ImageView) itemView.findViewById(R.id.ivThumbcopy);
            txtAddtocart = (LinearLayout) itemView.findViewById(R.id.txtAddtocart);
            txtPrice = (TextView) itemView.findViewById(R.id.txtPrice);

            txtAddPrice = (TextView) itemView.findViewById(R.id.txtAddPrice);
            txtorignalprice = (TextView) itemView.findViewById(R.id.txtorignalprice);
            txtPriceOffer = (TextView) itemView.findViewById(R.id.txtPriceOffer);
            stock = (TextView) itemView.findViewById(R.id.stock);
            chkwish = (CheckBox) itemView.findViewById(R.id.chkwish);
            detail = (LinearLayout) itemView.findViewById(R.id.detail);


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

        } else if (strordertype.equals("oldlist")) {
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









