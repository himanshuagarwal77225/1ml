package com.medical.product.Ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;
import com.medical.product.R;
import com.medical.product.adapter.AdapterFeedbacklist;
import com.medical.product.adapter.Filter_adpater;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.Filter_data;
import com.medical.product.models.GatterGetVendersModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class Filetr_selector extends AppCompatActivity {
    ArrayList<Filter_data> data;
    RecyclerView datalist;
    Filter_adpater recyclerAdapter;
    String type = "";
    String selected="",selectedstr="";
    Button apply;
    int resultcode = 0;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filetr_selector);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        getSupportActionBar().setTitle("Filter");

        datalist = (RecyclerView) findViewById(R.id.datalist);
        apply = (Button) findViewById(R.id.apply);

        if (getIntent() != null) {
            type = getIntent().getStringExtra("get_type");
            selected = getIntent().getStringExtra("selected");
            resultcode = getIntent().getIntExtra("resultcode", 0);
        }

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("data", selected);
                intent.putExtra("datastr", selectedstr);
                setResult(resultcode, intent);
                finish();
            }
        });

        data = new ArrayList<>();


        recyclerAdapter = new Filter_adpater(data, Filetr_selector.this, Filetr_selector.this);
        LinearLayoutManager recyclerlayoutManager = new LinearLayoutManager(getApplicationContext());
        datalist.setLayoutManager(recyclerlayoutManager);
        datalist.setHasFixedSize(true);
        datalist.setAdapter(recyclerAdapter);


        getVenderList();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void getVenderList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.filter,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            String strstatus = obj.getString("status");
                            if (strstatus.equals("false")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                JSONArray jsonArray = obj.getJSONArray("filter");
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    JSONObject object = jsonArray.getJSONObject(j);
                                    data.add(new Filter_data(object.getString("id"), object.getString("name"), false));
                                }
                                recyclerAdapter.notifyDataSetChanged();
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
                params.put("get_type", type);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    public void selected_value(String id,String name) {
        if (!TextUtils.isEmpty(selected)) {
            selected = selected + "," + id;
            selectedstr=selectedstr+","+name;
        } else {
            selected = id;
            selectedstr=name;
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra("data", "");
                intent.putExtra("datastr", "");
                setResult(resultcode, intent);
                finish();
                break;
        }
        return (super.onOptionsItemSelected(menuItem));
    }


}
