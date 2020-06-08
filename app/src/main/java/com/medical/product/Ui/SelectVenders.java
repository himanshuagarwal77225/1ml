package com.medical.product.Ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
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
import com.medical.product.adapter.AdapterVendersSelect;
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

public class SelectVenders extends AppCompatActivity {
    ImageView imageView;
    ProgressDialog dialog;
    EditText editTitle, editDescription;
    private JSONObject jsonObject;
    Intent intent;
    Keystore store;

    String userid = "", documenttype;

    Button buttonContinue;
    Bitmap bitmapmain = null;

    RecyclerView recycleviewdocument;
    private AdapterVendersSelect recyclerAdapter;
    LinearLayout layUploadimag;

    private ArrayList<GatterGetVendersModel> gatterGetFamilyDocumentListModels;

    String insert_id = "", PrescriptionChoose = "", selectnum = "", addid = "";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venders);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        hideStatusbar(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        store = Keystore.getInstance(getApplicationContext());
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog = new ProgressDialog(this);
        //initializing views
        imageView = (ImageView) findViewById(R.id.imageView);
        editTitle = (EditText) findViewById(R.id.editTitle);
        editDescription = (EditText) findViewById(R.id.editDescription);
        buttonContinue = (Button) findViewById(R.id.buttonContinue);
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
        recyclerAdapter = new AdapterVendersSelect(gatterGetFamilyDocumentListModels, this);
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
                                for (int j = 0; j < jsonArray.length(); j++) {

                                    gatterGetFamilyDocumentListModels.add(new GatterGetVendersModel(
                                            false,
                                            jsonArray.getJSONObject(j).getString("id"),
                                            jsonArray.getJSONObject(j).getString("name"),
                                            jsonArray.getJSONObject(j).getString("pharm_name"),
                                            jsonArray.getJSONObject(j).getString("store_address"),
                                            jsonArray.getJSONObject(j).getString("average_rating"),
                                            jsonArray.getJSONObject(j).getString("store_status"), ""
                                    ));
                                    recyclerAdapter.notifyDataSetChanged();

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
                        Log.i("dfsasfd", "profile error=========" + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("addid", addid);
                params.put("type", "1");
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }


    public void clickFunction(View view) {
        switch (view.getId()) {
            case R.id.buttonContinue:
                String venderarr = "",vendername="";
                for (int j = 0; j < gatterGetFamilyDocumentListModels.size(); j++) {
                    if (gatterGetFamilyDocumentListModels.get(j).isSelected()) {
                        venderarr = venderarr + gatterGetFamilyDocumentListModels.get(j).getId() + ",";
                        vendername = vendername +  gatterGetFamilyDocumentListModels.get(j).getPharm_name() + System.getProperty("line.separator");

                    }
                }
                String VenderidList = method(venderarr);
                if (!TextUtils.isEmpty(VenderidList)) {
                    Intent intent = new Intent(this, OrderPrescriptionDetailActivity.class);
                    intent.putExtra("insert_id", insert_id);
                    intent.putExtra("perception_detail", PrescriptionChoose);
                    intent.putExtra("perception_method", selectnum);
                    intent.putExtra("perception_vendor", VenderidList);
                    intent.putExtra("vendername", vendername);
                    intent.putExtra("perception_address_id", addid);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please select nearest vender", Toast.LENGTH_SHORT).show();
                }

                break;


        }
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

