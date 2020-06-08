package com.medical.product.Ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.medical.product.R;
import com.medical.product.Utils.Utlity;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.GatterGetAddressListModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.medical.product.helpingFile.ReuseMethod.hideStatusbar;

public class SelectAddressPrescriptionActivity extends AppCompatActivity {

    private Keystore store;
    RecyclerView recycleCartProduct;
    Context context = SelectAddressPrescriptionActivity.this;
    Button btnChooseAddNewAddress, btnContinue;

    TextView addUName, address, addemail, addPhone, txttotalSave;

    LinearLayout layAddress;

    String addid = "";


    TextView txttotalPrice, txttotalDiscountnProduct, shippingcharge, discountCoupon, txtGrandtotal, addtype, txtProductpricetitle;

    ScrollView scrollview;


    String insert_id = "", PrescriptionChoose = "", selectnum = "";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_prescription_address);
        hideStatusbar(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        store = Keystore.getInstance(getApplicationContext());
        setTitle("Select Address");

        Intent intent = getIntent();

        insert_id = intent.getStringExtra("insert_id");
        PrescriptionChoose = intent.getStringExtra("PrescriptionChoose");
        selectnum = intent.getStringExtra("selectnum");


        addtype = (TextView) findViewById(R.id.addtype);
        txttotalSave = (TextView) findViewById(R.id.txttotalSave);
        txtProductpricetitle = (TextView) findViewById(R.id.txtProductpricetitle);

        addUName = (TextView) findViewById(R.id.addUName);
        address = (TextView) findViewById(R.id.address);
        addemail = (TextView) findViewById(R.id.addemail);
        addPhone = (TextView) findViewById(R.id.addPhone);
        layAddress = (LinearLayout) findViewById(R.id.layAddress);
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        btnContinue = (Button) findViewById(R.id.btnContinue);
        btnChooseAddNewAddress = (Button) findViewById(R.id.btnChooseAddNewAddress);

        if (!TextUtils.isEmpty(Utlity.get_address(this))) {
            GatterGetAddressListModel addressListModel = new Gson().fromJson(Utlity.get_address(this), GatterGetAddressListModel.class);
            addUName.setText(addressListModel.getFirst_name() + " " + addressListModel.getLast_name());
            address.setText(addressListModel.getAddress_area());
            addemail.setText(addressListModel.getEmail());
            addPhone.setText(addressListModel.getPhone_number());
            addtype.setText(addressListModel.getAddress_type());
            layAddress.setVisibility(View.VISIBLE);
            addid = addressListModel.getId();
        }
    }

    public void clickFunction(View view) {
        switch (view.getId()) {
            case R.id.btnChooseAddNewAddress:
                addid = "";
                addUName.setText("");
                address.setText("");
                addemail.setText("");
                addPhone.setText("");
                layAddress.setVisibility(View.GONE);
                Intent i = new Intent(this, MyAddressActivity.class);
                startActivityForResult(i, 1);
                break;
            case R.id.btnContinue:
                if (!addid.equals("")) {
                    if (TextUtils.isEmpty(Utlity.selected_vendor)) {
                        Intent ii = new Intent(getApplicationContext(), SelectVenders.class);
                        ii.putExtra("insert_id", insert_id);
                        ii.putExtra("PrescriptionChoose", PrescriptionChoose);
                        ii.putExtra("selectnum", selectnum);
                        ii.putExtra("addid", addid);
                        startActivity(ii);
                    } else {
                        UploadUserPrescriptionDetails(Utlity.selected_vendor);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please select address", Toast.LENGTH_SHORT).show();
                }
                break;


        }
    }

    private void UploadUserPrescriptionDetails(final String venderidList) {
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
                                Intent i = new Intent(getApplicationContext(), OrderPrescriptionDetailActivity.class);
                                i.putExtra("insert_id", insert_id);
                                i.putExtra("perception_detail", PrescriptionChoose);
                                i.putExtra("perception_method", selectnum);
                                i.putExtra("perception_vendor", Utlity.selected_vendor);
                                i.putExtra("vendername", Utlity.selected_vendorname);
                                i.putExtra("perception_address_id", addid);
                                startActivity(i);
                                Utlity.selected_vendor = "";
                                Utlity.selected_vendorname = "";
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
                params.put("perception_vendor", venderidList);
                params.put("perception_address_id", addid);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1) {
            addUName.setText("");
            address.setText("");
            addemail.setText("");
            addPhone.setText("");
            addtype.setText("");
            addemail.setText("");
            layAddress.setVisibility(View.VISIBLE);
            addid = data.getStringExtra("addid");
            String ustrfname = data.getStringExtra("fname");
            String ustrlname = data.getStringExtra("lname");
            String ustremail = data.getStringExtra("email");
            String ustrphone = data.getStringExtra("phonenumber");
            String ustrcountry = data.getStringExtra("country");
            String ustrstate = data.getStringExtra("state");
            String ustrcity = data.getStringExtra("city");
            String ustrpostalcode = data.getStringExtra("postalcode");
            String ustrhomenumber = data.getStringExtra("homenumber");

            String ustraddarea = data.getStringExtra("addarea");
            String ustrlandmark = data.getStringExtra("landmark");
            String ustralterphone = data.getStringExtra("alterphone");
            String ustraddtype = data.getStringExtra("addtype");

            addtype.setText(ustraddtype);
            addUName.setText(ustrfname + " " + ustrlname);
            address.setText(ustrhomenumber + " " + ustraddarea + " " + ustrlandmark + " " + ustrcity + " " + ustrstate + " - " + ustrpostalcode);
            addemail.setText(ustremail);
            addPhone.setText(ustrphone);
            if (!addid.equals("")) {

                btnChooseAddNewAddress.setBackgroundColor(Color.parseColor("#17b7c8"));
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }


}
