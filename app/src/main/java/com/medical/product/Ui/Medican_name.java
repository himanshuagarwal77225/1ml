package com.medical.product.Ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.medical.product.R;
import com.medical.product.adapter.Product_name_adpater;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.GatterGetVendersModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Medican_name extends AppCompatActivity {
    ListView list_product;
    EditText edtname;
    Product_name_adpater adpater;
    ArrayList<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medican_name);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        getSupportActionBar().setTitle("Search");
        names = new ArrayList<>();
        list_product = (ListView) findViewById(R.id.list_product);
        adpater = new Product_name_adpater(Medican_name.this, names);
        list_product.setAdapter(adpater);
        edtname = (EditText) findViewById(R.id.edtname);

        edtname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adpater.getFilter().filter(s.toString());
                list_product.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        getVenderList();

    }


    private void getVenderList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.produ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {
                                names.clear();
                                JSONArray jsonArray = obj.getJSONArray("product");
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    JSONObject object = jsonArray.getJSONObject(j);
                                    names.add(object.getString("brand_name"));
                                }
                                adpater.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("dfsasfd", "profile error=========" + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", "0");
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
