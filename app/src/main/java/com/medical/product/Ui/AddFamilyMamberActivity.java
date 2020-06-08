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
import com.medical.product.R;
import com.medical.product.adapter.AdapterFamilyMamberList;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.HelperFunction;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.OneMLUserSpace;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.GatterGetFamilyMamberListModel;
import com.medical.product.models.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class AddFamilyMamberActivity extends AppCompatActivity {
    private static TextView textNotifycart;
    ImageButton imgBtnEdit;
    private Keystore store;
    CircleImageView CimgivThumb;
    RecyclerView recycleviewaddress;
    Button btnAddNewMamber;
    private AdapterFamilyMamberList recyclerAdapter;
    private ArrayList<GatterGetFamilyMamberListModel> gatterGetFamilyMamberListModels;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_mamber_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        componentInitilization();
        loadDataProfile();
    }

    private void loadDataProfile() {
        store = Keystore.getInstance(getApplicationContext());
        //store.get("name")
        gatterGetFamilyMamberListModels = new ArrayList<>();
        recyclerAdapter = new AdapterFamilyMamberList(gatterGetFamilyMamberListModels,this);
        LinearLayoutManager recyclerManagertable=new LinearLayoutManager(this);
        recycleviewaddress.setLayoutManager(recyclerManagertable);
        recycleviewaddress.setHasFixedSize(true);
        recycleviewaddress.setAdapter(recyclerAdapter);
        getFamilyMamberList();
    }

    private void componentInitilization() {
        btnAddNewMamber=(Button)findViewById(R.id.btnAddNewMamber);
        recycleviewaddress = (RecyclerView) findViewById(R.id.recycleviewaddress);
    }


    public void clickFunction(View view) {
        switch (view.getId())
        {
            case R.id.btnAddNewMamber:
                Intent i=new Intent(getApplicationContext(), Family_Mamber_Activity.class);
                i.putExtra("memberid","");
                startActivity(i);
                break;
        }
    }

    private void createMamber(final String membername, final String memberphone) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.ROOT_HTTP_URL+"add-family",
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

                                Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                                getFamilyMamberList();
                            }


                            //   txtPrice,vendershopname

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
                //params.put("user_id", store.get("id"));
                OneMLUserSpace oneMLUserSpace = OneMLUserSpace.getInstance(AddFamilyMamberActivity.this);
                UserModel userModel = oneMLUserSpace.getUserProfile();
                if (userModel != null) {
                    if(HelperFunction.checkStringEmpty(userModel.getId())){
                        params.put("user_id", userModel.getId());
                    }
                }
                params.put("member_name", membername);
                params.put("contact_number", memberphone);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    private void getFamilyMamberList() {

       StringRequest stringRequest = new StringRequest(Request.Method.POST,ApiFileuri.ROOT_HTTP_URL+"get-family",
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

                                JSONArray jsonArray = obj.getJSONArray("message");

                                gatterGetFamilyMamberListModels.clear();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject json = null;

                                        json = jsonArray.getJSONObject(i);

                                        gatterGetFamilyMamberListModels.add(new GatterGetFamilyMamberListModel(
                                                json.getString("id"),
                                                json.getString("member_name"),
                                                json.getString("contact_number"),json.getString("realtion")
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
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
       getFamilyMamberList();
    }
}
