package com.medical.product.Ui;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
import com.medical.product.adapter.AdapterOrderProductList;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.HelperFunction;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.OneMLUserSpace;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.GatterGetOrderProductListModel;
import com.medical.product.models.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Fragment_Prescription_Order_List extends Fragment {

    private Keystore store;


    Context context = getActivity();
    View rootview;

    RecyclerView recycleviewprescription;
    private AdapterOrderProductList recyclerAdapter;
    private ArrayList<GatterGetOrderProductListModel> gatterMyOrderListModels;


    LinearLayout layemptypreorder;

    public Fragment_Prescription_Order_List() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_prescription_order_list, container, false);

        recycleviewprescription = (RecyclerView) rootview.findViewById(R.id.recycleviewprescription);

        gatterMyOrderListModels = new ArrayList<>();

        layemptypreorder = (LinearLayout) rootview.findViewById(R.id.layemptypreorder);
        store = Keystore.getInstance(getActivity());
        getCategory();
        return rootview;

    }

    private void getCategory() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.ROOT_HTTP_URL + "product/user-order-list",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Gson gson = new Gson();
                            JSONObject object = new JSONObject(response);
                            String data = object.getJSONArray("order").toString();
                            gatterMyOrderListModels = gson.fromJson(data, new TypeToken<List<GatterGetOrderProductListModel>>() {}.getType());
                            recyclerAdapter = new AdapterOrderProductList(gatterMyOrderListModels,getActivity());
                            LinearLayoutManager recyclerlayoutManager=new LinearLayoutManager(getActivity());
                            recycleviewprescription.setLayoutManager(recyclerlayoutManager);
                            recycleviewprescription.setHasFixedSize(true);
                            recycleviewprescription.setAdapter(recyclerAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (gatterMyOrderListModels.size() == 0) {
                            layemptypreorder.setVisibility(View.VISIBLE);
                            recycleviewprescription.setVisibility(View.GONE);
                        } else {
                            recycleviewprescription.setVisibility(View.VISIBLE);
                            layemptypreorder.setVisibility(View.GONE);
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
               // params.put("user_id", store.get("id"));

                OneMLUserSpace oneMLUserSpace = OneMLUserSpace.getInstance(getActivity());
                UserModel userModel = oneMLUserSpace.getUserProfile();
                if (userModel != null) {
                    if(HelperFunction.checkStringEmpty(userModel.getId())){
                        params.put("user_id", userModel.getId());
                    }
                }
                params.put("type", "1");
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }


}
