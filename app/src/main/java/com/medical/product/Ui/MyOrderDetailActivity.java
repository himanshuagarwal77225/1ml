package com.medical.product.Ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medical.product.R;
import com.medical.product.adapter.AdapterCartProductCheckout;
import com.medical.product.adapter.AdapterOrderList;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.GatterGetCartList;
import com.medical.product.models.GatterGetMyOrderListModel;
import com.medical.product.models.GatterGetOrderProductListModel;
import com.transferwise.sequencelayout.SequenceStep;

import org.json.JSONException;
import org.json.JSONObject;
import org.qap.ctimelineview.TimelineRow;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.medical.product.helpingFile.ReuseMethod.hideStatusbar;

public class MyOrderDetailActivity extends AppCompatActivity {
    private static TextView textNotifycart, txtOrderid, product, txtsellername, txtAddPrice, txtname, txtaddress;
    TextView stotal, sdiscount, shiping, gtotal, mode;
    ImageButton imgBtnEdit;
    private Keystore store;
    CircleImageView CimgivThumb;
    RecyclerView recyclevieworders, products;
    private AdapterOrderList recyclerAdapter;
    private ImageView img;
    private Button btn_cancel, needhelp;
    private ArrayList<TimelineRow> timelineRowsList = new ArrayList<>();
    ArrayAdapter<TimelineRow> myAdapter;
    private ArrayList<GatterGetMyOrderListModel> gatterMyOrderListModels;
    GatterGetOrderProductListModel orderProductListModel;
    LinearLayout llreview, action, ordercancel, lltrack;
    SequenceStep orded, packed, shipped, delilvered, orded1, canceled;
    String reson = "";
    EditText review;
    Button btnsubmit;
    RatingBar rating;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorderdetail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        hideStatusbar(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        componentInitilization();
        loadDataProfile();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Date getRandomDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = null;
        Date endDate = new Date();
        try {
            startDate = sdf.parse("02/09/2015");
            long random = ThreadLocalRandom.current().nextLong(startDate.getTime(), endDate.getTime());
            endDate = new Date(random);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return endDate;
    }

    private void loadDataProfile() {
        store = Keystore.getInstance(getApplicationContext());
    }

    private void componentInitilization() {
        orded = (SequenceStep) findViewById(R.id.orded);
        packed = (SequenceStep) findViewById(R.id.packed);
        ;
        shipped = (SequenceStep) findViewById(R.id.shipped);

        delilvered = (SequenceStep) findViewById(R.id.delilvered);

        orded1 = (SequenceStep) findViewById(R.id.orded1);
        canceled = (SequenceStep) findViewById(R.id.canceled);

        mode = (TextView) findViewById(R.id.mode);
        textNotifycart = (TextView) findViewById(R.id.textNotify);
        textNotifycart = (TextView) findViewById(R.id.textNotify);
        stotal = (TextView) findViewById(R.id.stotal);
        sdiscount = (TextView) findViewById(R.id.discount);
        shiping = (TextView) findViewById(R.id.shiping);
        gtotal = (TextView) findViewById(R.id.gtotal);
        txtname = (TextView) findViewById(R.id.txtname);
        txtaddress = (TextView) findViewById(R.id.txtaddress);
        imgBtnEdit = (ImageButton) findViewById(R.id.imgBtnEdit);
        CimgivThumb = (CircleImageView) findViewById(R.id.CimgivThumb);
        products = (RecyclerView) findViewById(R.id.products);
        txtOrderid = (TextView) findViewById(R.id.txtOrderid);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        needhelp = (Button) findViewById(R.id.needhelp);
        review = (EditText) findViewById(R.id.review);
        llreview = (LinearLayout) findViewById(R.id.llreview);
        btnsubmit = (Button) findViewById(R.id.btnsubmit);
        action = (LinearLayout) findViewById(R.id.action);
        ordercancel = (LinearLayout) findViewById(R.id.ordercancel);
        lltrack = (LinearLayout) findViewById(R.id.lltrack);
        rating = (RatingBar) findViewById(R.id.rating);
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(review.getText().toString()) && !TextUtils.isEmpty(String.valueOf(rating.getRating()))) {
                    submit_api(review.getText().toString(), String.valueOf(rating.getRating()));
                } else {
                    Toast.makeText(getApplicationContext(), "Please Give Review Or  Rating ", Toast.LENGTH_LONG).show();
                }
            }
        });
        ArrayList<GatterGetCartList> filelist = new ArrayList<>();
        if (getIntent() != null) {
            orderProductListModel = new Gson().fromJson(getIntent().getStringExtra("order_detail"), GatterGetOrderProductListModel.class);
            filelist = new Gson().fromJson(orderProductListModel.getProducts(), new TypeToken<List<GatterGetCartList>>() {
            }.getType());

            shiping.setText("+ "+getString(R.string.Rs) + orderProductListModel.getShipping_cahrage());
            if (!TextUtils.isEmpty(orderProductListModel.getDiscount())) {
                sdiscount.setText("- "+getString(R.string.Rs) + orderProductListModel.getDiscount());
            } else {
                sdiscount.setText("- "+getString(R.string.Rs) + "0");
            }
            gtotal.setText(getString(R.string.Rs) + orderProductListModel.getGrand_total());

            txtOrderid.setText(orderProductListModel.getOrder_id());
            if (!TextUtils.isEmpty(orderProductListModel.getFirst_name()) && !TextUtils.isEmpty(orderProductListModel.getLast_name())) {
                txtname.setText(orderProductListModel.getFirst_name().toUpperCase() + " " + orderProductListModel.getLast_name().toUpperCase() + "\n" + orderProductListModel.getPhone_number().toUpperCase());
                txtaddress.setText(orderProductListModel.getAddress_area() + "\n" + orderProductListModel.getCity() + "," + orderProductListModel.getState() + "," + orderProductListModel.getCountry() + "-" + orderProductListModel.getPostal_code());

            }
            if (orderProductListModel.getPayment_type().equalsIgnoreCase("cod")) {
                mode.setText("Cash On delivery");
            }


            LinearLayoutManager recyclerlayoutManager = new LinearLayoutManager(this);
            products.setLayoutManager(recyclerlayoutManager);
            products.setHasFixedSize(true);
            AdapterCartProductCheckout adapter = new AdapterCartProductCheckout(filelist, this);
            products.setAdapter(adapter);
        }

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel_order();
            }
        });
        needhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Needhelp();
            }
        });
        setvaluecart();
        calulate_prices(filelist);
        //set tracking
        set_tracker(orderProductListModel.getConfirmed());
    }

    private void submit_api(final String toString, final String valueOf) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.review,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            btnsubmit.setText("Edit Review");
                            Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
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
                params.put("order_id", orderProductListModel.getId());
                params.put("user_id", store.get("id"));
                params.put("reviews", valueOf);
                params.put("comments", toString);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void set_tracker(String confirm) {
        if (confirm.equalsIgnoreCase("0") || confirm.equalsIgnoreCase("1")) {
            if (orderProductListModel.getProcessed().equalsIgnoreCase("0")) {
                orded.setActive(true);
                orded.setSubtitle("Your order " + orderProductListModel.getOrder_id() + " is approved");
                orded.isActive();
            } else if (orderProductListModel.getProcessed().equalsIgnoreCase("1")) {
                packed.setActive(true);
                packed.setSubtitle("Your order " + orderProductListModel.getOrder_id() + " is packed !");
                packed.isActive();
            } else if (orderProductListModel.getProcessed().equalsIgnoreCase("2")) {
                shipped.setActive(true);
                shipped.setSubtitle("Order shipped to " + "\n" + orderProductListModel.getFirst_name().toUpperCase() + " " + orderProductListModel.getLast_name().toUpperCase() + "\n" + orderProductListModel.getPhone_number() + "\n" + orderProductListModel.getAddress_area() + orderProductListModel.getCity() + "," + orderProductListModel.getState() + "," + orderProductListModel.getCountry() + "-" + orderProductListModel.getPostal_code());
                shipped.isActive();
            } else {
                delilvered.setActive(true);
                delilvered.setSubtitle("Delivered successfully to " + orderProductListModel.getFirst_name().toUpperCase() + " " + orderProductListModel.getLast_name().toUpperCase());
                delilvered.isActive();
                llreview.setVisibility(View.VISIBLE);
                action.setVisibility(View.GONE);
                if (orderProductListModel.getReviews() != 0 && !TextUtils.isEmpty(orderProductListModel.getComments())) {
                    rating.setRating(Float.parseFloat(String.valueOf(orderProductListModel.getReviews())));
                    review.setText(orderProductListModel.getComments());
                    btnsubmit.setText("Edit Review");
                }
            }
        } else {
            lltrack.setVisibility(View.GONE);
            ordercancel.setVisibility(View.VISIBLE);
            action.setVisibility(View.GONE);
            llreview.setVisibility(View.GONE);

            canceled.setActive(true);
            canceled.setSubtitle("Your order " + orderProductListModel.getOrder_id() + " is cancelled");
            canceled.isActive();

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

    public void setvaluecart() {
        if (ReuseMethod.GetTotalCartItem(getApplicationContext()).equals("null") || ReuseMethod.GetTotalCartItem(getApplicationContext()).equals("")) {
        } else {
            textNotifycart.setText(ReuseMethod.GetTotalCartItem(getApplicationContext()));
        }
    }

    private void canceloder(final GatterGetOrderProductListModel orderProductListModel, final String reason, final AlertDialog dialog) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.cacnel,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("status")) {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                finish();
                                Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
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
                params.put("id", orderProductListModel.getId());
                params.put("cancel_reason", reason);
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

    @Override
    protected void onResume() {
        super.onResume();
        setvaluecart();
    }

    public void calulate_prices(ArrayList<GatterGetCartList> filelist) {
        DecimalFormat precision = new DecimalFormat("#.##");
        double subtotal = 0.0;
        for (GatterGetCartList gatterGetCartList : filelist) {
            subtotal = subtotal + (Double.parseDouble(gatterGetCartList.getSale_price()) * Integer.parseInt(gatterGetCartList.getQuentity()));
        }
        stotal.setText(getString(R.string.Rs) + precision.format(subtotal));
        //gtotal.setText(getString(R.string.Rs) + precision.format(subtotal + shipping + discount));
    }

    private void cancel_order() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.cancel_order, null);

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
                    reson = "Order Placed by mistake";
                } else if (checkedId == R.id.moredealy) {
                    other_reason.setVisibility(View.GONE);
                    reson = "Expected delivery time is too lete";
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
                    canceloder(orderProductListModel, reson, alertD);
                } else {
                    Toast.makeText(getApplicationContext(), "Please Select reason ", Toast.LENGTH_LONG).show();
                }
            }
        });

        alertD.setView(promptView);

        alertD.show();
    }

    //to show if  user need  help
    private void Needhelp() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.help, null);

        final AlertDialog alertD = new AlertDialog.Builder(this).create();

        Button btnsubmit = (Button) promptView.findViewById(R.id.btnsubmit);
        final Spinner reasonlist = (Spinner) promptView.findViewById(R.id.reasonlist);
        alertD.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        final RadioGroup group = (RadioGroup) promptView.findViewById(R.id.reason);
        final EditText other_reason = (EditText) promptView.findViewById(R.id.otherreason);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.other) {
                    reasonlist.setSelection(0);
                    other_reason.setVisibility(View.VISIBLE);
                } else {
                    other_reason.setText("");
                    other_reason.setVisibility(View.GONE);
                }
            }
        });
        reasonlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (reasonlist.getSelectedItemPosition() != 0) {
                    group.clearCheck();
                    other_reason.setVisibility(View.GONE);
                    reson = reasonlist.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                    Toast.makeText(getApplicationContext(), reson, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Please Select reason ", Toast.LENGTH_LONG).show();
                }

            }
        });

        alertD.setView(promptView);

        alertD.show();
    }
}
