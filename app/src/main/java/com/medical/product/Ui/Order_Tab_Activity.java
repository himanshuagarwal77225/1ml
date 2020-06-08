package com.medical.product.Ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medical.product.R;
import com.medical.product.Utils.Utlity;
import com.medical.product.adapter.AdapterOrderProductList;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.HelperFunction;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.OneMLUserSpace;
import com.medical.product.helpingFile.PagerOrderType;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.GatterGetOrderProductListModel;
import com.medical.product.models.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class Order_Tab_Activity extends AppCompatActivity {
    Intent intent;
    private Keystore store;
    Context context;
    View rootview;
    RecyclerView recycleviewprescription;
    private AdapterOrderProductList recyclerAdapter;
    private ArrayList<GatterGetOrderProductListModel> gatterMyOrderListModels;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    PagerOrderType adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order_list);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        getSupportActionBar().setTitle("My Orders");

        recycleviewprescription = (RecyclerView) findViewById(R.id.recycleviewprescription);
        store = Keystore.getInstance(this);
        getCategory();
    }


    private void getCategory() {
        final Dialog dialog = Utlity.show_progress(this);
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.ROOT_HTTP_URL + "product/user-order-list",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Utlity.dismiss_dilog(Order_Tab_Activity.this, dialog);
                            Gson gson = new Gson();
                            JSONObject object = new JSONObject(response);
                            String data = object.getJSONArray("order").toString();
                            gatterMyOrderListModels = gson.fromJson(data, new TypeToken<List<GatterGetOrderProductListModel>>() {
                            }.getType());
                            recyclerAdapter = new AdapterOrderProductList(gatterMyOrderListModels, Order_Tab_Activity.this);
                            LinearLayoutManager recyclerlayoutManager = new LinearLayoutManager(Order_Tab_Activity.this);
                            recycleviewprescription.setLayoutManager(recyclerlayoutManager);
                            recycleviewprescription.setHasFixedSize(true);
                            recycleviewprescription.setAdapter(recyclerAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utlity.dismiss_dilog(Order_Tab_Activity.this, dialog);

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //params.put("user_id", store.get("id"));
                OneMLUserSpace oneMLUserSpace = OneMLUserSpace.getInstance(Order_Tab_Activity.this);
                UserModel userModel = oneMLUserSpace.getUserProfile();
                if (userModel != null) {
                    if(HelperFunction.checkStringEmpty(userModel.getId())){
                        params.put("user_id", userModel.getId());
                    }
                }
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCategory();
    }
}
