package com.medical.product.Ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.medical.product.R;
import com.medical.product.Utils.Utlity;
import com.medical.product.adapter.AdapterCartProduct;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.GatterGetCartList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
    ImageButton imgBtnSearch;
    RecyclerView recyclecartitem;
    private ArrayList<GatterGetCartList> gatterGetCartLists;
    private AdapterCartProduct recyclerAdapter;
    Keystore store;
    Button btnCheckout, btnAddchooseNewAddress, shopnow;
    TextView txtGrandTotal;
    LinearLayout layEmptyCart, layCart, ch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // toolbar.setTitle("Cart");

        store = Keystore.getInstance(getApplicationContext());
        imgBtnSearch = (ImageButton) findViewById(R.id.imgBtnSearch);

        txtGrandTotal = (TextView) findViewById(R.id.txtGrandTotal);
        btnCheckout = (Button) findViewById(R.id.btnCheckout);
        shopnow = (Button) findViewById(R.id.shopnow);
        btnAddchooseNewAddress = (Button) findViewById(R.id.btnAddchooseNewAddress);
        recyclecartitem = (RecyclerView) findViewById(R.id.recyclecartitem);
        layEmptyCart = (LinearLayout) findViewById(R.id.layEmptyCart);
        ch = (LinearLayout) findViewById(R.id.ch);
        layCart = (LinearLayout) findViewById(R.id.layCart);
        gatterGetCartLists = new ArrayList<>();
        recyclerAdapter = new AdapterCartProduct(gatterGetCartLists, this);
        LinearLayoutManager recyclerlayoutManager = new LinearLayoutManager(this);
        recyclecartitem.setLayoutManager(recyclerlayoutManager);
        recyclecartitem.setHasFixedSize(true);
        recyclecartitem.setAdapter(recyclerAdapter);

        shopnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, Dashbord.class));
            }
        });

        getCartItem(store.get("id"), ReuseMethod.MacAddress(this));
    }

    public void clickFunction(View view) {
        switch (view.getId()) {
            case R.id.imgBtnSearch:
                ReuseMethod.OpenSearchActivity(this);
                break;
            case R.id.btnCheckout:
                if (store.get("id").equals("")) {
                    Intent i = new Intent(getApplicationContext(), LoginPanelActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else {
                    SendCartProductList();
                }
                break;
            case R.id.btnAddchooseNewAddress:
                Intent i = new Intent(getApplicationContext(), MyAddressActivity.class);
                startActivity(i);
                break;


        }
    }

    public void SendCartProductList() {
        Intent intent = new Intent(this, CheckoutActivity.class);
        intent.putExtra("FILES_TO_SEND", gatterGetCartLists);
        startActivity(intent);
    }

    private void getCartItem(final String userid, final String medid) {
        final Dialog dialog = Utlity.show_progress(this);
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.ROOT_HTTP_URL + "product/getcart",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj = null;
                        try {
                            Utlity.dismiss_dilog(CartActivity.this, dialog);
                            obj = new JSONObject(response);
                            String strstatus = obj.getString("status");
                            if (strstatus.equals("false")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {

                                JSONArray jsonArray = obj.getJSONArray("cart-data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject json = null;
                                        json = jsonArray.getJSONObject(i);
                                        String offer = json.getString("additional_offer");
                                        if (offer.equalsIgnoreCase("1")) {
                                            gatterGetCartLists.add(new GatterGetCartList(

                                                    json.getString("cart_id"),
                                                    json.getString("user_id"),
                                                    json.getString("quentity"),
                                                    json.getString("vendor_name"),//
                                                    json.getString("prdct_id"),
                                                    json.getString("vendor_id"),
                                                    json.getString("pd_brand_name"),
                                                    json.getString("sale_price"),
                                                    json.getString("mrp"),

                                                    json.getString("additional_offer_percentage"),//additional_offer_percentage
                                                    json.getString("additional_offer_id"),//additional_offer_id
                                                    json.getString("additional_offer_code"),//additional_offer_code
                                                    json.getString("image"),
                                                    json.getString("company_name")
                                                    , json.getString("additional_offer_type"), offer
                                            ));
                                        } else {
                                            gatterGetCartLists.add(new GatterGetCartList(
                                                    json.getString("cart_id"),
                                                    json.getString("user_id"),
                                                    json.getString("quentity"),
                                                    json.getString("vendor_name"),//
                                                    json.getString("prdct_id"),
                                                    json.getString("vendor_id"),
                                                    json.getString("pd_brand_name"),
                                                    json.getString("sale_price"),
                                                    json.getString("mrp"),
                                                    "",//additional_offer_percentage
                                                    "",//additional_offer_id
                                                    "",//additional_offer_code
                                                    json.getString("image"),
                                                    json.getString("company_name")
                                                    , "", offer
                                            ));

                                        }
                                        recyclerAdapter.notifyDataSetChanged();
                                    } catch (JSONException e) {

                                    }

                                }

                                CalculateGrandTotal();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (gatterGetCartLists.size() == 0) {
                            layEmptyCart.setVisibility(View.VISIBLE);
                            layCart.setVisibility(View.GONE);
                            ch.setVisibility(View.GONE);
                        } else {
                            layCart.setVisibility(View.VISIBLE);
                            layEmptyCart.setVisibility(View.GONE);
                            ch.setVisibility(View.VISIBLE);
                        }
                        Utlity.dismiss_dilog(CartActivity.this, dialog);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", store.get("id"));
                params.put("macid", medid);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void CalculateGrandTotal() {

        Double grandprice = 0.0;
        Double grandnet = 0.0;

        for (int i = 0; i < gatterGetCartLists.size(); i++) {
            Double newpricecheck = Double.parseDouble(gatterGetCartLists.get(i).getSale_price());
            Double price = newpricecheck * Double.parseDouble(gatterGetCartLists.get(i).getQuentity());
            grandprice = grandprice + price;

        }

        txtGrandTotal.setText(getString(R.string.Rs) + String.valueOf(grandprice));

        if (gatterGetCartLists.size() == 0) {
            layEmptyCart.setVisibility(View.VISIBLE);
            layCart.setVisibility(View.GONE);
            ch.setVisibility(View.GONE);
        } else {
            layCart.setVisibility(View.VISIBLE);
            layEmptyCart.setVisibility(View.GONE);
            ch.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

}
