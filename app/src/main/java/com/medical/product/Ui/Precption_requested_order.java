package com.medical.product.Ui;

import android.os.Bundle;


import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

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
import com.medical.product.adapter.Adapterrequest2;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.HelperFunction;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.OneMLUserSpace;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.Prescription;
import com.medical.product.models.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Precption_requested_order extends AppCompatActivity {
    Adapterrequest2 recyclerAdapter;
    Keystore keystore;
    RecyclerView list;
    ImageView no_order;
    ArrayList<Prescription> list_data;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_precption_requested);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        getSupportActionBar().setTitle("My Prescaption");

        list = (RecyclerView) findViewById(R.id.list);
        no_order = (ImageView) findViewById(R.id.no_order);
        keystore = Keystore.getInstance(this);
        gson = new Gson();
        list_data=new ArrayList<>();
        getorder_requested();
    }


    private void getorder_requested() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.prescription,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("status")) {
                                list_data = gson.fromJson(object.getJSONArray("prescription").toString(), new TypeToken<List<Prescription>>() {
                                }.getType());
                                recyclerAdapter = new Adapterrequest2(list_data, Precption_requested_order.this);
                                LinearLayoutManager recyclerlayoutManager = new LinearLayoutManager(Precption_requested_order.this);
                                list.setLayoutManager(recyclerlayoutManager);
                                list.setHasFixedSize(true);
                                list.setAdapter(recyclerAdapter);
                                if (list_data.size() == 0) {
                                    no_order.setVisibility(View.VISIBLE);
                                    list.setVisibility(View.GONE);
                                } else {
                                    no_order.setVisibility(View.GONE);
                                    list.setVisibility(View.VISIBLE);
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
                        if (list_data.size() > 0) {
                            no_order.setVisibility(View.VISIBLE);
                            list.setVisibility(View.GONE);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //params.put("id", keystore.get("id"));
                OneMLUserSpace oneMLUserSpace = OneMLUserSpace.getInstance(Precption_requested_order.this);
                UserModel userModel = oneMLUserSpace.getUserProfile();
                if (userModel != null) {
                    if(HelperFunction.checkStringEmpty(userModel.getId())){
                        params.put("id", userModel.getId());
                    }
                }
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

}
