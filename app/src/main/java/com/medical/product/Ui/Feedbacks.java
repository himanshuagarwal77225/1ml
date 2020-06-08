package com.medical.product.Ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.medical.product.R;
import com.medical.product.adapter.AdapterFeedbacklist;
import com.medical.product.adapter.AdapterVenderslist;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.GatterGetVendersModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class Feedbacks extends AppCompatActivity {
    RecyclerView recycleviewdocument;
    protected AdapterFeedbacklist recyclerAdapter;
    Keystore store;
    private ArrayList<GatterGetVendersModel> gatterGetFamilyDocumentListModels;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedbacks);
        store = Keystore.getInstance(getApplicationContext());

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        recycleviewdocument = (RecyclerView) findViewById(R.id.list);
        gatterGetFamilyDocumentListModels = new ArrayList<>();
        recyclerAdapter = new AdapterFeedbacklist(gatterGetFamilyDocumentListModels, this);
        LinearLayoutManager recyclerlayoutManager = new LinearLayoutManager(getApplicationContext());
        recycleviewdocument.setLayoutManager(recyclerlayoutManager);
        recycleviewdocument.setHasFixedSize(true);
        recycleviewdocument.setAdapter(recyclerAdapter);
        getVenderList();

    }

    private void getVenderList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.feedback,
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
                                gatterGetFamilyDocumentListModels.clear();
                                JSONArray jsonArray = obj.getJSONArray("feedback");
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    gatterGetFamilyDocumentListModels.add(new GatterGetVendersModel(
                                            false,
                                            jsonArray.getJSONObject(j).getString("id"),
                                            jsonArray.getJSONObject(j).getString("order_id"),
                                            jsonArray.getJSONObject(j).getString("pharm_name"),
                                            jsonArray.getJSONObject(j).getString("comments"),
                                            jsonArray.getJSONObject(j).getString("reviews"),
                                            jsonArray.getJSONObject(j).getString("store_status"),""
                                    ));
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
                params.put("id", store.get("id"));
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
