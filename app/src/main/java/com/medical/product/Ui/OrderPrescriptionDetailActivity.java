package com.medical.product.Ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.medical.product.R;
import com.medical.product.Utils.Utlity;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.GatterGetAddressListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.medical.product.helpingFile.ReuseMethod.hideStatusbar;

public class OrderPrescriptionDetailActivity extends AppCompatActivity {

    private Keystore store;
    Button btnConfirmOrder;
    TextView txtdeliveryaddress, txtOptionSelected, txtselectedOptionDetail, txtVenderList, txtaddresstype, txtname;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    String orderid = "";

    String insert_id = "", PrescriptionChoose = "", selectnum = "", VenderidList = "", addid = "",vendername="";
    LinearLayout layPrescImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_order_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        hideStatusbar(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        store=Keystore.getInstance(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btnConfirmOrder = (Button) findViewById(R.id.btnConfirmOrder);

        txtdeliveryaddress = (TextView) findViewById(R.id.txtdeliveryaddress);
        txtname = (TextView) findViewById(R.id.txtname);
        txtOptionSelected = (TextView) findViewById(R.id.txtOptionSelected);
        txtselectedOptionDetail = (TextView) findViewById(R.id.txtselectedOptionDetail);
        txtVenderList = (TextView) findViewById(R.id.txtVenderList);
        txtaddresstype = (TextView) findViewById(R.id.txtaddresstype);

        layPrescImage = (LinearLayout) findViewById(R.id.layPrescImage);
        Intent intent = getIntent();
        insert_id = intent.getStringExtra("insert_id");
        PrescriptionChoose = intent.getStringExtra("perception_detail");
        selectnum = intent.getStringExtra("perception_method");
        VenderidList = intent.getStringExtra("perception_vendor");
        addid = intent.getStringExtra("perception_address_id");
        vendername = intent.getStringExtra("vendername");


        if (!(insert_id.equals("") || insert_id.equals(null))) {
            if (!TextUtils.isEmpty(Utlity.get_address(OrderPrescriptionDetailActivity.this))) {
                GatterGetAddressListModel addressListModel = new Gson().fromJson(Utlity.get_address(OrderPrescriptionDetailActivity.this), GatterGetAddressListModel.class);
                txtname.setText(addressListModel.getFirst_name() + " " + addressListModel.getLast_name());
                txtaddresstype.setText(addressListModel.getAddress_type());
                txtdeliveryaddress.setText(addressListModel.getAddress_area() + " " + addressListModel.getLandmark() + " " + addressListModel.getCity() + "" + addressListModel.getState() + " " + addressListModel.getPostal_code());
            }


            if (selectnum.equals("0")) {
                txtOptionSelected.setText("Order everything as per prescription");
                txtselectedOptionDetail.setText(PrescriptionChoose);
            } else if (selectnum.equals("1")) {
                txtOptionSelected.setText("You specified the following");
                txtselectedOptionDetail.setText(PrescriptionChoose);

            } else if (selectnum.equals("2")) {
                txtOptionSelected.setText("We will call you for order details");
                txtselectedOptionDetail.setText(store.get("phone"));

            }
            txtVenderList.setText(vendername);
            UploadedPrescriptionDetail(insert_id);
        }

    }


    private void UploadedPrescriptionDetail(final String insert_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.ROOT_HTTP_URL + "get-perception",
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
                                JSONObject objmessage = null;
                                objmessage = obj.getJSONObject("perception");
                                orderid = objmessage.getString("order_id");
                                String jsonArrayimage = objmessage.getString("perception_images");
                                final JSONArray json = new JSONArray(jsonArrayimage);
                                for (int i = 0; i < json.length(); i++) {
                                    final int k = i;
                                    ImageView imageView = new ImageView(getApplicationContext());
                                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    lp.setMargins(5, 5, 5, 5);
                                    lp.weight = 100;
                                    lp.height = 150;
                                    imageView.setLayoutParams(lp);
                                    // imageView.setScaleType(VIEW);
                                    Glide.with(getApplicationContext()).load(ApiFileuri.ROOT_URL_USER_IMAGE + json.get(i)).into(imageView);
                                    imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            try {
                                                startActivity(new Intent(OrderPrescriptionDetailActivity.this, Full_image.class).putExtra("img", ApiFileuri.ROOT_URL_USER_IMAGE + json.get(k)));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                                    imageView.setAdjustViewBounds(true);
                                    layPrescImage.addView(imageView);
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
                params.put("id", insert_id);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }


    private void UploadUserPrescriptionDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.ROOT_HTTP_URL + "add-perception-data",
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
                                Intent i = new Intent(getApplicationContext(), OrderPrescriptionThankyouActivity.class);
                                i.putExtra("orderid", orderid);
                                startActivity(i);
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
                params.put("id", insert_id);
                params.put("perception_detail", PrescriptionChoose);
                params.put("perception_method", selectnum);
                params.put("perception_vendor", VenderidList);
                params.put("perception_address_id", addid);
                params.put("status", "1");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    public void clickFunction(View view) {
        switch (view.getId()) {
            case R.id.btnConfirmOrder:
                UploadUserPrescriptionDetails();
                break;

        }
    }


    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
