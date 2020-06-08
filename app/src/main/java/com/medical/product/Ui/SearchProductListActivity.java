package com.medical.product.Ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.medical.product.R;
import com.medical.product.adapter.ProductSearchListAdapter;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.ModelProductSearchList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.medical.product.helpingFile.ReuseMethod.hideStatusbar;

public class SearchProductListActivity extends AppCompatActivity {
    private static TextView textNotifycart;
    ImageButton imgBtnEdit;
    private Keystore store;
    CircleImageView CimgivThumb;
    RecyclerView recyclevieworders;
    private ProductSearchListAdapter recyclerAdapter;

    String searchid="",keyword="",type="",check="";

    private ArrayList<ModelProductSearchList> modelProductSearchLists;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    LinearLayout noresult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorders);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        hideStatusbar(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        componentInitilization();
        loadDataProfile();
    }

    private void loadDataProfile() {
        store = Keystore.getInstance(getApplicationContext());
        //store.get("name")
        modelProductSearchLists = new ArrayList<>();
        recyclerAdapter = new ProductSearchListAdapter(this,modelProductSearchLists);
        LinearLayoutManager recyclerlayoutManager=new LinearLayoutManager(this);
        recyclevieworders.setLayoutManager(recyclerlayoutManager);
        recyclevieworders.setHasFixedSize(true);
        recyclevieworders.setAdapter(recyclerAdapter);
        getCategory();
    }

    private void componentInitilization() {
        textNotifycart  = (TextView) findViewById(R.id.textNotify);
        imgBtnEdit  = (ImageButton) findViewById(R.id.imgBtnEdit);
        noresult=(LinearLayout)findViewById(R.id.noresult);
        CimgivThumb  = (CircleImageView) findViewById(R.id.CimgivThumb);
        setvaluecart();
        recyclevieworders = (RecyclerView) findViewById(R.id.recyclevieworders);

        Intent intent = getIntent();



        check = intent.getStringExtra("ckeck").toString();
        searchid = intent.getStringExtra("id").toString();
        keyword = intent.getStringExtra("keyword_search").toString();
        type = intent.getStringExtra("type");
    }


    public void setvaluecart()
    {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String carttot=ReuseMethod.GetTotalCartItem(getApplicationContext());
                if(ReuseMethod.GetTotalCartItem(getApplicationContext()).equals("null") || ReuseMethod.GetTotalCartItem(getApplicationContext()).equals(""))
                {

                }
                else{
                    textNotifycart.setText( ReuseMethod.GetTotalCartItem(getApplicationContext()));
                }

            }
        },1000);



    }


    public void clickFunction(View view) {
        switch (view.getId())
        {
            case R.id.imgBtnCart:
                ReuseMethod.OpenCartActivity(this);
                break;
            case R.id.imgBtnSearch:
                getApplicationContext().startActivity(new Intent(getApplicationContext(), SearchProductActivity.class));
                break;

        }
    }

    private void getCategory() {
        final String cattype=type;
        final String catsearch=searchid;
        final String catkeyword=keyword;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.ROOT_HTTP_URL+"product/productwhere",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);

                                JSONArray jsonArray = obj.getJSONArray("product");
                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                     JSONObject json = null;
                                    json = jsonArray.getJSONObject(i);
                                    modelProductSearchLists.add(new ModelProductSearchList(
                                            json.getString("id"),
                                            json.getString("unique_code"),
                                            json.getString("vendor_id"),
                                            json.getString("vendor_name"),
                                            json.getString("pd_brand_name"),
                                            json.getString("pd_company_name"),
                                            json.getString("image"),
                                            json.getString("brand_name"),
                                            json.getString("company_name"),
                                            json.getString("mrp"),
                                            json.getString("sale_price")
                                    ));
                                }

                            if (jsonArray.length() == 0) {
                                noresult.setVisibility(View.VISIBLE);
                                recyclevieworders.setVisibility(View.GONE);
                            } else {
                                noresult.setVisibility(View.GONE);
                                recyclevieworders.setVisibility(View.VISIBLE);
                            }

                            recyclerAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("dfsasfd","profile error========="+error.toString());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", searchid);
                params.put("search_keyword", keyword);
                params.put("get_type", type);
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
    protected void onResume() {
        super.onResume();
        setvaluecart();
    }
}
