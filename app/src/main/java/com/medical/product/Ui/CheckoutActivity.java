package com.medical.product.Ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medical.product.R;
import com.medical.product.Utils.Utlity;
import com.medical.product.adapter.AdapterCartProductCheckout;
import com.medical.product.adapter.AdapterPrescriptionImage;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.GatterGetAddressListModel;
import com.medical.product.models.GatterGetCartList;
import com.medical.product.models.GatterGetPrescriptionImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.medical.product.helpingFile.ReuseMethod.hideStatusbar;

public class CheckoutActivity extends AppCompatActivity {

    private Keystore store;
    RecyclerView recycleCartProduct;
    Context context = CheckoutActivity.this;
    private ArrayList<GatterGetCartList> filelist;
    Button btnChooseAddNewAddress, btnProced;
    private ArrayList<Vendor_wise> vendor_wises;
    TextView addUName, address, addemail, addPhone, txttotalSave, gtotal;
    LinearLayout layAddress;
    String addid = "";
    Double grandprice = 0.0, discount = 0.0, shipping = 0.0, subtotal = 0.0, net_amount = 0.0;
    TextView txttotalPrice, txttotalDiscountnProduct, shippingcharge, discountCoupon, txtGrandtotal, addtype, txtProductpricetitle;
    private RadioGroup radioGrouppaymode;
    private RadioButton radioButtonmode;
    ScrollView scrollview;
    TextView msg;
    EditText coupan;
    Button apply;
    String strcoupan = "";
    String coupan_id = "";
    private Uri imageUri;
    Bitmap bitmapmain = null;
    File now_file;
    int PERMISSION_ALL = 1;
    private AdapterPrescriptionImage recyclerAdapter;
    String temp[] = null;
    ImageView imgadd;
    String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
    };
    String[] PERMISSIONS_CAMERA = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    ArrayList<Uri> encodedImageList = new ArrayList<>();
    boolean is_prescaption = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    RecyclerView recyclePrescImage;
    private ArrayList<GatterGetPrescriptionImage> gatterprescimg = new ArrayList<>();
    private ArrayList<GatterGetPrescriptionImage> tempfiles = new ArrayList<>();
    LinearLayout layimg;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        hideStatusbar(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        filelist = (ArrayList<GatterGetCartList>) getIntent().getSerializableExtra("FILES_TO_SEND");
        store = Keystore.getInstance(getApplicationContext());

        setTitle("Checkout");


        txttotalPrice = (TextView) findViewById(R.id.txttotalPrice);
        imgadd = (ImageView) findViewById(R.id.imgadd);
        layimg = (LinearLayout) findViewById(R.id.layimg);
        recyclePrescImage = (RecyclerView) findViewById(R.id.recyclePrescImage);
        txttotalDiscountnProduct = (TextView) findViewById(R.id.txttotalDiscountnProduct);
        shippingcharge = (TextView) findViewById(R.id.shippingcharge);
        discountCoupon = (TextView) findViewById(R.id.discountCoupon);
        txtGrandtotal = (TextView) findViewById(R.id.txtGrandtotal);
        addtype = (TextView) findViewById(R.id.addtype);
        txttotalSave = (TextView) findViewById(R.id.txttotalSave);
        txtProductpricetitle = (TextView) findViewById(R.id.txtProductpricetitle);
        radioGrouppaymode = (RadioGroup) findViewById(R.id.radioGrouppaymode);
        btnProced = (Button) findViewById(R.id.btnProced);
        gtotal = (TextView) findViewById(R.id.gtotal);
        msg = (TextView) findViewById(R.id.msg);
        coupan = (EditText) findViewById(R.id.coupan);
        apply = (Button) findViewById(R.id.apply);
        addUName = (TextView) findViewById(R.id.addUName);
        address = (TextView) findViewById(R.id.address);
        addemail = (TextView) findViewById(R.id.addemail);
        addPhone = (TextView) findViewById(R.id.addPhone);
        layAddress = (LinearLayout) findViewById(R.id.layAddress);
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strcoupan = coupan.getText().toString();
                if (!TextUtils.isEmpty(strcoupan)) {
                    apply_coupan(strcoupan);
                } else {
                    Toast.makeText(getApplicationContext(), "Enter Coupan Code", Toast.LENGTH_LONG).show();
                }
            }
        });

        recyclerAdapter = new AdapterPrescriptionImage(gatterprescimg, this);
        LinearLayoutManager recyclerManagertable = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclePrescImage.setLayoutManager(recyclerManagertable);
        recyclePrescImage.setHasFixedSize(true);
        recyclePrescImage.setAdapter(recyclerAdapter);


        recycleCartProduct = (RecyclerView) findViewById(R.id.recycleCartProduct);
        LinearLayoutManager recyclerlayoutManager = new LinearLayoutManager(this);
        recycleCartProduct.setLayoutManager(recyclerlayoutManager);
        recycleCartProduct.setHasFixedSize(true);
        AdapterCartProductCheckout adapter = new AdapterCartProductCheckout(filelist, context);
        recycleCartProduct.setAdapter(adapter);

        btnChooseAddNewAddress = (Button) findViewById(R.id.btnChooseAddNewAddress);


        CalculateGrandTotal();
        if (!TextUtils.isEmpty(Utlity.get_address(CheckoutActivity.this))) {
            GatterGetAddressListModel addressListModel = new Gson().fromJson(Utlity.get_address(CheckoutActivity.this), GatterGetAddressListModel.class);
            layAddress.setVisibility(View.VISIBLE);
            addid = addressListModel.getId();
            addtype.setText(addressListModel.getAddress_type());
            addUName.setText(addressListModel.getFirst_name() + " " + addressListModel.getLast_name());
            address.setText(addressListModel.getAlternative_number() + " " + addressListModel.getAddress_area() + " " + addressListModel.getLandmark() + " " + addressListModel.getCity() + " " + addressListModel.getState() + " - " + addressListModel.getPostal_code());
            addemail.setText(addressListModel.getEmail());
            addPhone.setText(addressListModel.getPhone_number());
            if (!TextUtils.isEmpty(addid)) {
                getShippingCharge();
            }
        } else {
            layAddress.setVisibility(View.GONE);
        }

        imgadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckoutActivity.this, UploadPrescriptionActivity.class);
                intent.putExtra("type", 1);
                startActivityForResult(intent, 2);
            }
        });
    }

    private void apply_coupan(final String strcoupan) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.get_discount,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("status")) {
                                JSONObject msg = jsonObject.getJSONObject("message");
                                discount = Double.parseDouble(msg.getString("discount_price"));
                                discountCoupon.setText("-" + getString(R.string.Rs) + discount);
                                coupan_id = msg.getString("discnt_id");
                                txtGrandtotal.setText(getString(R.string.Rs) + ((grandprice + shipping) - discount));
                                gtotal.setText(getString(R.string.Rs) + String.valueOf((grandprice + shipping) - discount));
                                net_amount = (grandprice + shipping) - discount;
                                Toast.makeText(getApplicationContext(), "Offer Code applyed", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        String message = null;
                        if (volleyError instanceof NetworkError) {
                            message = "NetworkErrorCannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ServerError) {
                            message = "ServerErrorThe server could not be found. Please try again after some time!!";
                        } else if (volleyError instanceof AuthFailureError) {
                            message = "AuthFailureErrorCannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ParseError) {
                            message = "ParseErrorParsing error! Please try again after some time!!";
                        } else if (volleyError instanceof NoConnectionError) {
                            message = "NoConnectionErrorCannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof TimeoutError) {
                            message = "TimeoutErrorConnection TimeOut! Please check your internet connection.";
                        }

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", store.get("id"));
                params.put("code", strcoupan);
                params.put("price", String.valueOf(grandprice));
                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    public void CalculateGrandTotal() {

        Double grandDiscount = 0.0;
        grandprice = 0.0;
        for (int i = 0; i < filelist.size(); i++) {

            Double newpricecheck = Double.parseDouble(filelist.get(i).getSale_price());
            Double price = newpricecheck * Double.parseDouble(filelist.get(i).getQuentity());
            grandprice = grandprice + price;
        }
        txtProductpricetitle.setText("Product Price (" + filelist.size() + "item)");
        txttotalPrice.setText(getString(R.string.Rs) + String.valueOf(Double.parseDouble(new DecimalFormat("##.##").format(grandprice))));
        txtGrandtotal.setText(getString(R.string.Rs) + String.valueOf(Double.parseDouble(new DecimalFormat("##.##").format(grandprice + shipping))));
        gtotal.setText(getString(R.string.Rs) + String.valueOf(Double.parseDouble(new DecimalFormat("##.##").format(grandprice + shipping))));

        if (grandprice < 100) {
            msg.setVisibility(View.VISIBLE);
            btnProced.setBackgroundColor(getResources().getColor(R.color.gray));
            btnProced.setEnabled(false);
        } else {
            msg.setVisibility(View.GONE);
            btnProced.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            btnProced.setEnabled(true);
        }

        txttotalSave.setText("You will save " + getString(R.string.Rs) + " " + String.valueOf(Double.parseDouble(new DecimalFormat("##.##").format(grandDiscount))) + " on this order");

        scrollview.fullScroll(scrollview.FOCUS_UP);


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
            case R.id.btnProced:
                if (!TextUtils.isEmpty(addid)) {
                    Intent intentpayment = new Intent(getApplicationContext(), PaymentActivity.class);
                    intentpayment.putExtra("net", String.valueOf(net_amount));
                    intentpayment.putExtra("ptotal", String.valueOf(grandprice));
                    intentpayment.putExtra("discount", String.valueOf(discount));
                    intentpayment.putExtra("shipping", String.valueOf(shipping));
                    intentpayment.putExtra("totalitem", String.valueOf(filelist.size()));
                    intentpayment.putExtra("product", get_vendor_wise(filelist));
                    intentpayment.putExtra("addid", addid);
                    intentpayment.putExtra("phone", addPhone.getText());
                    intentpayment.putExtra("username", addUName.getText());
                    intentpayment.putExtra("email", addemail.getText());
                    intentpayment.putExtra("discount_code", strcoupan);
                    intentpayment.putExtra("offer_id", coupan_id);
                    intentpayment.putExtra("img", new Gson().toJson(gatterprescimg));
                    if (is_prescaption && gatterprescimg.size() == 0) {
                        Toast.makeText(getApplicationContext(), "Please Upload Precaption", Toast.LENGTH_SHORT).show();

                    } else {
                        startActivity(intentpayment);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please select address", Toast.LENGTH_SHORT).show();
                    scrollview.fullScroll(View.FOCUS_UP);
                }
                break;
        }
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
            Utlity.save_address(CheckoutActivity.this, data.getStringExtra("addrerss_model"));
            if (!addid.equals("")) {
                getShippingCharge();
            }
        }
        if (requestCode == 2 && data != null) {
            String message = data.getStringExtra("data");
            tempfiles = new Gson().fromJson(message, new TypeToken<List<GatterGetPrescriptionImage>>() {
            }.getType());
            for (GatterGetPrescriptionImage gatterGetPrescriptionImage : tempfiles) {
                gatterprescimg.add(gatterGetPrescriptionImage);
            }
            recyclerAdapter.notifyDataSetChanged();
            layimg.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void getShippingCharge() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.get_shiping,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("status")) {

                                if (object.getInt("prescription_required") == 1) {
                                    layimg.setVisibility(View.VISIBLE);
                                    Intent intent = new Intent(CheckoutActivity.this, UploadPrescriptionActivity.class);
                                    intent.putExtra("type", 1);
                                    startActivityForResult(intent, 2);
                                    is_prescaption = true;
                                }
                                shipping = Double.parseDouble(object.getString("amount"));
                                shippingcharge.setText("+" + getString(R.string.Rs) + shipping);
                                net_amount = grandprice + shipping;
                                txtGrandtotal.setText(getString(R.string.Rs) + String.valueOf(Double.parseDouble(new DecimalFormat("##.##").format(grandprice + shipping))));
                                gtotal.setText(getString(R.string.Rs) + String.valueOf(Double.parseDouble(new DecimalFormat("##.##").format(grandprice + shipping))));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        String message = null;
                        if (volleyError instanceof NetworkError) {
                            message = "NetworkErrorCannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ServerError) {
                            message = "ServerErrorThe server could not be found. Please try again after some time!!";
                        } else if (volleyError instanceof AuthFailureError) {
                            message = "AuthFailureErrorCannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ParseError) {
                            message = "ParseErrorParsing error! Please try again after some time!!";
                        } else if (volleyError instanceof TimeoutError) {
                            message = "TimeoutErrorConnection TimeOut! Please check your internet connection.";
                        }
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", store.get("id"));
                params.put("addressid", addid);
                params.put("vendors", get_vendor_wise(filelist));
                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public String get_vendor_wise(ArrayList<GatterGetCartList> filelist) {
        vendor_wises = new ArrayList<>();
        for (GatterGetCartList product : filelist) {
            if (vendor_wises.size() > 0) {
                if (is_vendor(vendor_wises, product.getVendor_id())) {
                    for (int i = 0; i < vendor_wises.size(); i++) {
                        if (vendor_wises.get(i).getVendor_id().equalsIgnoreCase(product.getVendor_id())) {
                            ArrayList<GatterGetCartList> old = vendor_wises.get(i).filelist;
                            old.add(product);
                            Vendor_wise wise = new Vendor_wise(product.getVendor_id(), old);
                            vendor_wises.set(i, wise);
                            break;
                        }
                    }

                } else {
                    ArrayList<GatterGetCartList> newlist = new ArrayList<>();
                    newlist.add(product);
                    Vendor_wise wise = new Vendor_wise(product.getVendor_id(), newlist);
                    vendor_wises.add(wise);
                }
            } else {
                ArrayList<GatterGetCartList> newlist = new ArrayList<>();
                newlist.add(product);
                Vendor_wise wise = new Vendor_wise(product.getVendor_id(), newlist);
                vendor_wises.add(wise);

            }
        }
        return new Gson().toJson(vendor_wises);
    }

    public boolean is_vendor(ArrayList<Vendor_wise> vendor_wises, String id) {
        boolean is_vendor = false;
        for (int i = 0; i < vendor_wises.size(); i++) {
            if (id.equalsIgnoreCase(vendor_wises.get(i).vendor_id)) {
                is_vendor = true;
                break;
            }
        }
        return is_vendor;
    }


    public void setArrayPrescription(int pos) {
        encodedImageList.remove(pos);
        recyclerAdapter.notifyDataSetChanged();
    }


}
