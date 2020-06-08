package com.medical.product.Ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medical.product.R;
import com.medical.product.adapter.AdapterOrderProductList;
import com.medical.product.adapter.AdapterVendors;
import com.medical.product.adapter.Preciption_order_Adpater;
import com.medical.product.adapter.Prescaption_img_adpater;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.Approved_data_detail;
import com.medical.product.models.GatterGetOrderProductListModel;
import com.medical.product.models.Prescription;
import com.medical.product.models.Vendors;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class Accepts_orders extends AppCompatActivity implements View.OnClickListener {
    ImageView profile;
    TextView name, address, stotal, shiping, gtotal, type, options;
    RatingBar ratingbar;
    RecyclerView product_detail, sellar_detail, imgs;
    Button btnaccept, btnreject, track;
    Prescription prescription;
    Preciption_order_Adpater recyclerAdapter;
    AdapterVendors adapterVendors;
    Prescaption_img_adpater img_adpater;
    String reson = "";
    CardView payment, llmedican, ll_prescption, ll_shipping, ll_seller;
    LinearLayout llaction;
    Keystore keystore;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepts_orders);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        getSupportActionBar().setTitle("Prescaption Detail");

        if (getIntent() != null) {
            prescription = new Gson().fromJson(getIntent().getStringExtra("details"), Prescription.class);
        }
        keystore = Keystore.getInstance(this);
        inti();
    }

    private void inti() {

        stotal = (TextView) findViewById(R.id.stotal);
        shiping = (TextView) findViewById(R.id.shiping);
        gtotal = (TextView) findViewById(R.id.gtotal);
        address = (TextView) findViewById(R.id.address);
        name = (TextView) findViewById(R.id.name);
        type = (TextView) findViewById(R.id.type);
        options = (TextView) findViewById(R.id.options);

        btnaccept = (Button) findViewById(R.id.btnaccept);
        btnreject = (Button) findViewById(R.id.btnreject);
        track = (Button) findViewById(R.id.track);

        product_detail = (RecyclerView) findViewById(R.id.product_detail);
        sellar_detail = (RecyclerView) findViewById(R.id.sellers);
        imgs = (RecyclerView) findViewById(R.id.imgs);

        llaction = (LinearLayout) findViewById(R.id.llaction);
        payment = (CardView) findViewById(R.id.payment);
        ll_shipping = (CardView) findViewById(R.id.ll_shipping);
        ll_prescption = (CardView) findViewById(R.id.ll_prescption);
        llmedican = (CardView) findViewById(R.id.llmedican);
        ll_seller = (CardView) findViewById(R.id.ll_seller);

        if (prescription.getApproved().equalsIgnoreCase("1") && !prescription.getUser_approved().equalsIgnoreCase("1")) {
            llaction.setVisibility(View.VISIBLE);
        }
        if (prescription.getUser_approved().equalsIgnoreCase("1")) {
            track.setVisibility(View.VISIBLE);
        }

        if (prescription.getPerception_images().length > 0) {
            img_adpater = new Prescaption_img_adpater(prescription.getPerception_images(), Accepts_orders.this);
            imgs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            imgs.setHasFixedSize(true);
            imgs.setAdapter(img_adpater);
        } else {
            ll_prescption.setVisibility(View.GONE);
        }

        if (prescription.getApproved_data_detail().size() > 0) {
            recyclerAdapter = new Preciption_order_Adpater(prescription.getApproved_data_detail(), Accepts_orders.this);
            LinearLayoutManager recyclerlayoutManager = new LinearLayoutManager(Accepts_orders.this);
            product_detail.setLayoutManager(recyclerlayoutManager);
            product_detail.setHasFixedSize(true);
            product_detail.setAdapter(recyclerAdapter);
        } else {
            llmedican.setVisibility(View.GONE);
        }

        if (prescription.getVendors().size() > 0) {
            adapterVendors = new AdapterVendors(prescription.getVendors(), prescription.getFor_vendor(), Accepts_orders.this, Accepts_orders.this);
            LinearLayoutManager recyclerlayoutManager = new LinearLayoutManager(Accepts_orders.this);
            sellar_detail.setLayoutManager(recyclerlayoutManager);
            sellar_detail.setHasFixedSize(true);
            sellar_detail.setAdapter(adapterVendors);
        } else {
            ll_seller.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(prescription.getPrice_total())) {
            stotal.setText(getString(R.string.Rs) + prescription.getPrice_total());
            shiping.setText(getString(R.string.Rs) + prescription.getShipping_charge());
            gtotal.setText(getString(R.string.Rs) + Double.parseDouble(new DecimalFormat("##.##").format(Double.parseDouble(prescription.getPrice_total()) + Double.parseDouble(prescription.getShipping_charge()))));
        } else {
            payment.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(prescription.getShipping_address().getFirst_name())) {
            name.setText(prescription.getShipping_address().getFirst_name() + " " + prescription.getShipping_address().getLast_name());
            type.setText(prescription.getShipping_address().getAddress_type());
            address.setText(prescription.getShipping_address().getAlternative_number() + " " + prescription.getShipping_address().getAddress_area() + " " + prescription.getShipping_address().getLandmark() + " " + prescription.getShipping_address().getCity() + " " + prescription.getShipping_address().getState() + " - " + prescription.getShipping_address().getPostal_code());
        } else {
            ll_shipping.setVisibility(View.GONE);
        }
        String method = "";
        if (prescription.getPerception_method().equalsIgnoreCase("0")) {
            method = "Order everything as per prescription";
            options.setText(method + "\n" + prescription.getPerception_detail());

        } else if (prescription.getPerception_method().equalsIgnoreCase("1")) {
            method = "Let me specify medicines and  quantity";
            options.setText(method + "\n" + prescription.getPerception_detail());

        } else {
            options.setText(prescription.getPerception_detail() + " " + keystore.get("phone"));

        }

        btnaccept.setOnClickListener(this);
        btnreject.setOnClickListener(this);
        track.setOnClickListener(this);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    private String date_conva(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date sourceDate = null;
        try {
            sourceDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat targetFormat = new SimpleDateFormat("E MMM dd,yyyy");
        return String.valueOf(targetFormat.format(sourceDate));
    }

    private void cancel_order() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.cancel_order2, null);

        final AlertDialog alertD = new AlertDialog.Builder(this).create();

        Button btnsubmit = (Button) promptView.findViewById(R.id.btnsubmit);
        alertD.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        RadioGroup group = (RadioGroup) promptView.findViewById(R.id.reason);
        final EditText other_reason = (EditText) promptView.findViewById(R.id.otherreason);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.other) {
                    other_reason.setVisibility(View.VISIBLE);
                    reson = other_reason.getText().toString();
                } else if (checkedId == R.id.delay) {
                    other_reason.setVisibility(View.GONE);
                    reson = "The Delivery is delayed";
                } else if (checkedId == R.id.mistak) {
                    other_reason.setVisibility(View.GONE);
                    reson = "Order Placed by mistaks";
                } else if (checkedId == R.id.moredealy) {
                    other_reason.setVisibility(View.GONE);
                    reson = "Expected delivery time too lete";
                } else if (checkedId == R.id.bugut) {
                    other_reason.setVisibility(View.GONE);
//                    alert_diloag();
                    reson = "This order is out of budget";
                } else if (checkedId == R.id.add) {
                    other_reason.setVisibility(View.GONE);
                    reson = "Need to change  shipping address";
                }
            }
        });
        other_reason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                reson = s.toString();
            }
        });
        Button btncancel = (Button) promptView.findViewById(R.id.btncancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.dismiss();
            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(reson)) {
                    order_accept(prescription.getOrder_id(), reson, "0");
                } else {
                    Toast.makeText(getApplicationContext(), "Please Select reason ", Toast.LENGTH_LONG).show();
                }
            }
        });

        alertD.setView(promptView);

        alertD.show();
    }

    private void alert_diloag() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do you want to reorder?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                        api_reorder();
                    }
                });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void api_reorder() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.ROOT_HTTP_URL + "approve-perception",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Gson gson = new Gson();
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("status")) {

                                Toast.makeText(Accepts_orders.this, object.getString("message"), Toast.LENGTH_LONG).show();

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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", prescription.getOrder_id());
                return params;

            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void order_accept(final String id, final String reason, final String type) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.accept_perception,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Gson gson = new Gson();
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("status")) {
                                if (type.equalsIgnoreCase("1")) {
                                    startActivity(new Intent(Accepts_orders.this, Order_Tab_Activity.class));
                                    finish();
                                } else {
                                    startActivity(new Intent(Accepts_orders.this, Dashbord.class));
                                    finishAffinity();
                                }
                                Toast.makeText(Accepts_orders.this, object.getString("message"), Toast.LENGTH_LONG).show();
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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("reason", reason);
                params.put("type", type);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnaccept:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Do you want to proccess it ?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.dismiss();
                                order_accept(prescription.getOrder_id(), reson, "1");
                            }
                        });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            case R.id.btnreject:
                cancel_order();
                break;
            case R.id.track:
                startActivity(new Intent(this, Order_Tab_Activity.class));
                break;
        }
    }

    public void vendor_info(final Vendors vendors) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_vendor_profile, null);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView seller = (TextView) view.findViewById(R.id.seller);
        TextView store = (TextView) view.findViewById(R.id.store);
        TextView storelic = (TextView) view.findViewById(R.id.storelic);
        TextView storeaddress = (TextView) view.findViewById(R.id.storeaddress);
        TextView dis = (TextView) view.findViewById(R.id.dis);
        ImageView profile = (ImageView) view.findViewById(R.id.profile);
        ImageView close = (ImageView) view.findViewById(R.id.close);
        String img = "";

        name.setText(vendors.getVendor_name());
        seller.setText(vendors.getVendor_name_main());
        store.setText(vendors.getVendor_name());
        storelic.setText(vendors.getDl_number());
        storeaddress.setText(vendors.getStore_address());
        dis.setText(vendors.getArea_km() + " Km");
        Glide.with(this)
                .load(ApiFileuri.ROOT_VENDOR_IMAGE + vendors.getVendor_profile())
                .into(profile);
        seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Accepts_orders.this, Product_vendor.class).putExtra("id", vendors.getVendor_id()).putExtra("name", vendors.getVendor_name()));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Accepts_orders.this, Full_image.class).putExtra("img", ApiFileuri.ROOT_VENDOR_IMAGE + vendors.getVendor_profile()));
            }
        });

        builder.setView(view);
        final android.app.AlertDialog dialog = builder.create();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }


}
