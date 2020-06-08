package com.medical.product.Ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
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
import com.medical.product.adapter.Adapterrequest;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.Prescription;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Precption_requested extends Fragment {
    Adapterrequest recyclerAdapter;
    Keystore keystore;
    ArrayList<Request_order> orders;
    RecyclerView list;
    ImageView no_order;
    View rootview;
    public Precption_requested() {
    }
    ArrayList<Prescription> list_data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.activity_precption_requested, container, false);
        list = (RecyclerView) rootview.findViewById(R.id.list);
        no_order = (ImageView) rootview.findViewById(R.id.no_order);
        keystore = Keystore.getInstance(getActivity());
        orders = new ArrayList<>();
        list_data = new ArrayList<>();
        getorder_requested();
        return rootview;
    }

    private void getorder_requested() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.perception_orders,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Gson gson = new Gson();
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("status")) {
                                orders = gson.fromJson(object.getJSONArray("perception").toString(), new TypeToken<List<Request_order>>() {
                                }.getType());
                                recyclerAdapter = new Adapterrequest(orders, getActivity());
                                LinearLayoutManager recyclerlayoutManager = new LinearLayoutManager(getActivity());
                                list.setLayoutManager(recyclerlayoutManager);
                                list.setHasFixedSize(true);
                                list.setAdapter(recyclerAdapter);
                                if (orders.size() == 0) {
                                    no_order.setVisibility(View.VISIBLE);
                                    list.setVisibility(View.GONE);
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
                        if (orders.size() > 0) {
                            no_order.setVisibility(View.VISIBLE);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", keystore.get("id"));
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}
