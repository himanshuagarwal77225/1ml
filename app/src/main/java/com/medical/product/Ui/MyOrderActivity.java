package com.medical.product.Ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.medical.product.adapter.AdapterOrderList;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.HelperFunction;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.OneMLUserSpace;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.GatterGetMyOrderListModel;
import com.medical.product.models.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.medical.product.helpingFile.ReuseMethod.hideStatusbar;

public class MyOrderActivity extends AppCompatActivity {
    private static TextView textNotifycart;
    ImageButton imgBtnEdit;
    private Keystore store;
    CircleImageView CimgivThumb;
    RecyclerView recyclevieworders;
    private AdapterOrderList recyclerAdapter;

    private ArrayList<GatterGetMyOrderListModel> gatterMyOrderListModels;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorders);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        hideStatusbar(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      //  this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        componentInitilization();
        loadDataProfile();
    }

    private void loadDataProfile() {
        store = Keystore.getInstance(getApplicationContext());
        //store.get("name")
        gatterMyOrderListModels = new ArrayList<>();
        recyclerAdapter = new AdapterOrderList(gatterMyOrderListModels, this);
        LinearLayoutManager recyclerlayoutManager = new LinearLayoutManager(this);
        recyclevieworders.setLayoutManager(recyclerlayoutManager);
        recyclevieworders.setHasFixedSize(true);
        recyclevieworders.setAdapter(recyclerAdapter);
        getCategory();
    }

    private void componentInitilization() {
        textNotifycart = (TextView) findViewById(R.id.textNotify);
        imgBtnEdit = (ImageButton) findViewById(R.id.imgBtnEdit);
        CimgivThumb = (CircleImageView) findViewById(R.id.CimgivThumb);

        setvaluecart();

        recyclevieworders = (RecyclerView) findViewById(R.id.recyclevieworders);
    }


    public void setvaluecart() {
        if (ReuseMethod.GetTotalCartItem(getApplicationContext()).equals("null") || ReuseMethod.GetTotalCartItem(getApplicationContext()).equals("")) {

        } else {
            textNotifycart.setText(ReuseMethod.GetTotalCartItem(getApplicationContext()));
        }

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

    private void getCategory() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.ROOT_HTTP_URL + "/product/user-order-list",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            gatterMyOrderListModels = new Gson().fromJson(object.getJSONArray("order").toString(), new TypeToken<List<GatterGetMyOrderListModel>>() {
                            }.getType());
                            recyclerAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                //params.put("user_id", store.get("id"));
                OneMLUserSpace oneMLUserSpace = OneMLUserSpace.getInstance(MyOrderActivity.this);
                UserModel userModel = oneMLUserSpace.getUserProfile();
                if (userModel != null) {
                    if(HelperFunction.checkStringEmpty(userModel.getId())){
                        params.put("user_id", userModel.getId());
                    }
                }
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
