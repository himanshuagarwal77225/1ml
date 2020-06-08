package com.medical.product.Ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.medical.product.R;
import com.medical.product.adapter.AdapterWishList;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.GatterGetMyWishListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.medical.product.helpingFile.ReuseMethod.hideStatusbar;

public class MyWishList extends AppCompatActivity {
    private static TextView textNotifycart;
    ImageButton imgBtnEdit;
    private Keystore store;
    CircleImageView CimgivThumb;
    RecyclerView recyclevieworders;
    private AdapterWishList recyclerAdapter;

    LinearLayout layEmptyWish;
    Button shopnow;
    private ArrayList<GatterGetMyWishListModel> gatterMyWishListModels;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    String type = "";
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorders);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        layEmptyWish = (LinearLayout) findViewById(R.id.layEmptyWish);
        hideStatusbar(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        componentInitilization();
        loadDataProfile();
    }

    private void loadDataProfile() {
        store = Keystore.getInstance(getApplicationContext());
        //store.get("name")
        gatterMyWishListModels = new ArrayList<>();
        recyclerAdapter = new AdapterWishList(gatterMyWishListModels, this);
        LinearLayoutManager recyclerlayoutManager = new LinearLayoutManager(this);
        recyclevieworders.setLayoutManager(recyclerlayoutManager);
        recyclevieworders.setHasFixedSize(true);
        recyclevieworders.setAdapter(recyclerAdapter);
        getWishList();
    }

    private void componentInitilization() {
        textNotifycart = (TextView) findViewById(R.id.textNotify);
        title = (TextView) findViewById(R.id.title);
        imgBtnEdit = (ImageButton) findViewById(R.id.imgBtnEdit);
        CimgivThumb = (CircleImageView) findViewById(R.id.CimgivThumb);
        shopnow = (Button) findViewById(R.id.shopnow);
        if (getIntent() != null) {
            if (!TextUtils.isEmpty(getIntent().getStringExtra("type"))) {
                type = getIntent().getStringExtra("type");
                if (type.equals("0")) {
                    title.setVisibility(View.VISIBLE);
                } else {
                    title.setVisibility(View.GONE);
                }
            } else {
                title.setVisibility(View.GONE);
            }
        }
        shopnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyWishList.this, Dashbord.class));
            }
        });


        setvaluecart();

        recyclevieworders = (RecyclerView) findViewById(R.id.recyclevieworders);
    }


    public void clickFunction(View view) {
        switch (view.getId()) {
            case R.id.imgBtnCart:
                ReuseMethod.OpenCartActivity(this);
                break;
            case R.id.imgBtnSearch:
                getApplicationContext().startActivity(new Intent(getApplicationContext(), SearchProductActivity.class));
                break;

        }
    }

    private void getWishList() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.ROOT_HTTP_URL + "product/get-wishlist",
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
                                JSONArray jsonArray = obj.getJSONArray("wishlist-data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject json = null;
                                    json = jsonArray.getJSONObject(i);
                                    gatterMyWishListModels.add(new GatterGetMyWishListModel(
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
                                            json.getString("sale_price"),
                                            json.getString("wishlist_id"),
                                            json.getString("qty"),
                                            json.getString("store_status")
                                    ));

                                    recyclerAdapter.notifyDataSetChanged();
                                }
                            }
                            recyclerAdapter.notifyDataSetChanged();

                            if (gatterMyWishListModels.size() == 0) {
                                layEmptyWish.setVisibility(View.VISIBLE);
                                recyclevieworders.setVisibility(View.GONE);
                            } else {
                                recyclevieworders.setVisibility(View.VISIBLE);
                                layEmptyWish.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (gatterMyWishListModels.size() == 0) {
                            layEmptyWish.setVisibility(View.VISIBLE);
                            recyclevieworders.setVisibility(View.GONE);
                        } else {
                            recyclevieworders.setVisibility(View.VISIBLE);
                            layEmptyWish.setVisibility(View.GONE);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", store.get("id"));
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void setvaluecart() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String carttot = ReuseMethod.GetTotalCartItem(getApplicationContext());
                if (ReuseMethod.GetTotalCartItem(getApplicationContext()).equals("null") || ReuseMethod.GetTotalCartItem(getApplicationContext()).equals("")) {
                } else {
                    textNotifycart.setText(ReuseMethod.GetTotalCartItem(getApplicationContext()));
                }

            }
        }, 1000);


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


    public void OnRemoveItem() {
        recyclerAdapter.notifyDataSetChanged();
    }
}
