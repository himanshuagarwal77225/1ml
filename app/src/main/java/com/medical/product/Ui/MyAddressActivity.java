package com.medical.product.Ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.medical.product.R;
import com.medical.product.Utils.Utlity;
import com.medical.product.adapter.AdapterAddressList;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.GatterGetAddressListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MyAddressActivity extends AppCompatActivity {
    private static TextView textNotifycart;
    ImageButton imgBtnEdit;
    private Keystore store;
    CircleImageView CimgivThumb;
    RecyclerView recycleviewaddress;
    Button btnAddNewAddress;
    private AdapterAddressList recyclerAdapter;
    private static final String TAG = "MyAddressActivity";
    private ArrayList<GatterGetAddressListModel> gatterMyOrderListModels;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Billing / Shipping Address");
        componentInitilization();
        loadDataProfile();
    }

    private void loadDataProfile() {
        store = Keystore.getInstance(getApplicationContext());
        //store.get("name")
        gatterMyOrderListModels = new ArrayList<>();
        recyclerAdapter = new AdapterAddressList(gatterMyOrderListModels,this);
        LinearLayoutManager recyclerManagertable=new LinearLayoutManager(this);
        recycleviewaddress.setLayoutManager(recyclerManagertable);
        recycleviewaddress.setHasFixedSize(true);
        recycleviewaddress.setAdapter(recyclerAdapter);
        getAddessList();
    }

    private void componentInitilization() {
        btnAddNewAddress=(Button)findViewById(R.id.btnAddNewAddress);
        recycleviewaddress = (RecyclerView) findViewById(R.id.recycleviewaddress);
    }


    public void clickFunction(View view) {
        switch (view.getId())
        {
            case R.id.btnAddNewAddress:
                Intent i=new Intent(getApplicationContext(),AddNew_Update_AddressActivity.class);
                i.putExtra("Addupdate","insert");
                startActivity(i);
                break;
        }
    }

    private void getAddessList() {

       StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.ROOT_HTTP_URL+"product/get_shipping_address",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            String strstatus=obj.getString("status");
                            if(strstatus.equals("false"))
                            {
                                Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Log.d(TAG, "onResponse: called");
                                JSONArray jsonArray = obj.getJSONArray("shipping-address");
                                Log.d(TAG, "onResponse: json array : "+jsonArray.toString());
                                gatterMyOrderListModels.clear();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject json = null;

                                        json = jsonArray.getJSONObject(i);

                                        gatterMyOrderListModels.add(new GatterGetAddressListModel(
                                                json.getString("id"),
                                                json.getString("user_id"),
                                                json.getString("first_name"),
                                                json.getString("last_name"),
                                                json.getString("email"),
                                                json.getString("phone_number"),
                                                json.getString("alternative_number"),
                                                json.getString("country"),
                                                json.getString("state"),
                                                json.getString("city"),
                                                json.getString("postal_code"),
                                                json.getString("address_type"),
                                                json.getString("address_num"),
                                                json.getString("address_area"),
                                                json.getString("landmark")
                                        ));
                                        recyclerAdapter.notifyDataSetChanged();
                                    }
                                    catch (JSONException e)
                                    {

                                    }

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
                        Log.d(TAG, "onErrorResponse: " + error.toString());
                    }
                })
       {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", store.get("id"));
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    public void backToCheckout(int position,String add) {
        Intent data = new Intent();
        data.putExtra("addid", gatterMyOrderListModels.get(position).getId());
        data.putExtra("fname", gatterMyOrderListModels.get(position).getFirst_name());
        data.putExtra("lname", gatterMyOrderListModels.get(position).getLast_name());
        data.putExtra("email", gatterMyOrderListModels.get(position).getEmail());
        data.putExtra("phonenumber", gatterMyOrderListModels.get(position).getPhone_number());
        data.putExtra("country", gatterMyOrderListModels.get(position).getCountry());
        data.putExtra("state", gatterMyOrderListModels.get(position).getState());
        data.putExtra("city", gatterMyOrderListModels.get(position).getCountry());
        data.putExtra("postalcode", gatterMyOrderListModels.get(position).getPostal_code());
        data.putExtra("homenumber", gatterMyOrderListModels.get(position).getAddress_num());
        data.putExtra("addarea", gatterMyOrderListModels.get(position).getAddress_area());
        data.putExtra("landmark", gatterMyOrderListModels.get(position).getLandmark());
        data.putExtra("alterphone", gatterMyOrderListModels.get(position).getAlternative_number());
        data.putExtra("addtype", gatterMyOrderListModels.get(position).getAddress_type());
        data.putExtra("addrerss_model",add);
        setResult(1, data);
        finish();
        Utlity.save_address(this, data.getStringExtra("addrerss_model"));
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddessList();
    }
}
