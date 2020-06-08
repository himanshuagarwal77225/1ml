package com.medical.product.Ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.medical.product.R;
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

import static com.medical.product.helpingFile.ReuseMethod.hideStatusbar;

public class Nearest_vendor extends AppCompatActivity {
    ImageView imageView;
    ProgressDialog dialog;
    EditText editTitle, editDescription;
    private JSONObject jsonObject;
    Intent intent;
    Keystore store;

    String userid = "", documenttype;


    Bitmap bitmapmain = null;

    RecyclerView recycleviewdocument;
    private AdapterVenderslist recyclerAdapter;
    LinearLayout layUploadimag;

    private ArrayList<GatterGetVendersModel> gatterGetFamilyDocumentListModels;

    String insert_id = "", PrescriptionChoose = "", selectnum = "", addid = "";

    String type="";
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venders_stores);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        hideStatusbar(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getIntent()!=null)
        {
            type=getIntent().getStringExtra("type");
        }

        if(type.equals("1")) {
            getSupportActionBar().setTitle("Nearest Store");
        }
        else
        {
            getSupportActionBar().setTitle("Store Offer");
        }
        store = Keystore.getInstance(getApplicationContext());
        dialog = new ProgressDialog(this);
        //initializing views
        imageView = (ImageView) findViewById(R.id.imageView);
        editTitle = (EditText) findViewById(R.id.editTitle);
        editDescription = (EditText) findViewById(R.id.editDescription);

        recycleviewdocument = (RecyclerView) findViewById(R.id.recycleviewdocument);
        layUploadimag = (LinearLayout) findViewById(R.id.layUploadimag);

        jsonObject = new JSONObject();

        userid = store.get("id");
        Intent intent = getIntent();
        insert_id = intent.getStringExtra("insert_id");
        PrescriptionChoose = intent.getStringExtra("PrescriptionChoose");
        selectnum = intent.getStringExtra("selectnum");
        addid = intent.getStringExtra("addid");


        gatterGetFamilyDocumentListModels = new ArrayList<>();
        recyclerAdapter = new AdapterVenderslist(gatterGetFamilyDocumentListModels, this);
        LinearLayoutManager recyclerlayoutManager = new LinearLayoutManager(getApplicationContext());
        recycleviewdocument.setLayoutManager(recyclerlayoutManager);
        recycleviewdocument.setHasFixedSize(true);
        recycleviewdocument.setAdapter(recyclerAdapter);
        getVenderList();
    }

    private void getVenderList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.ROOT_HTTP_URL + "fix-area-vendor",
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
                                JSONArray jsonArray = obj.getJSONArray("message");
                                if (type.equals("1")) {
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        gatterGetFamilyDocumentListModels.add(new GatterGetVendersModel(
                                                false,
                                                jsonArray.getJSONObject(j).getString("id"),
                                                jsonArray.getJSONObject(j).getString("name"),
                                                jsonArray.getJSONObject(j).getString("pharm_name"),
                                                jsonArray.getJSONObject(j).getString("store_address"),
                                                jsonArray.getJSONObject(j).getString("average_rating"),
                                                jsonArray.getJSONObject(j).getString("store_status"),
                                                ""
                                        ));
                                    }
                                }
                                else
                                {
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        gatterGetFamilyDocumentListModels.add(new GatterGetVendersModel(
                                                false,
                                                jsonArray.getJSONObject(j).getString("id"),
                                                jsonArray.getJSONObject(j).getString("name"),
                                                jsonArray.getJSONObject(j).getString("pharm_name"),
                                                jsonArray.getJSONObject(j).getString("store_address"),
                                                jsonArray.getJSONObject(j).getString("average_rating"),
                                                jsonArray.getJSONObject(j).getString("store_status"),
                                                jsonArray.getJSONObject(j).getString("offer")

                                        ));

                                    }
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
                params.put("addid", "");
                params.put("type", type);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    public String method(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

