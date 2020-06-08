package com.medical.product.Ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.text.TextUtils;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.medical.product.R;
import com.medical.product.adapter.AdapterReletedProduct;
import com.medical.product.adapter.AdapterSimilarProduct;
import com.medical.product.adapter.SpinnerCustomQuantityAdapter;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.ConnectDetector;
import com.medical.product.helpingFile.HelperFunction;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.OneMLUserSpace;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.GatterGetCartList;
import com.medical.product.models.GatterGetReletedProductList;
import com.medical.product.models.GatterGetSimilorList;
import com.medical.product.models.UserModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.medical.product.helpingFile.ReuseMethod.store;

public class Product_detailActivity extends AppCompatActivity {

    public static final String EXTRA_PARAM_ID = "detail:_id";
    ProgressDialog dialog;
    // View name of the header image. Used for activity scene transitions
    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";

    // View name of the header title. Used for activity scene transitions
    public static final String VIEW_NAME_HEADER_TITLE = "detail:header:title";
    public static final String VIEW_NAME_CART = "detail:header:cart";
    public static final String VIEW_NAME_PRICE = "detail:header:price";
    private ImageView mHeaderImageView, imageview_header_cartFly;
    ImageButton imgBtnSearch;
    private TextView mHeaderTitle;
    private TextView txtorignalprice, txtAdditionalOfferDesc;

    String wishid = "";

    private TextView txtseemore, txtPriceOffer, txtpackageing, promocode, outof;
    private static TextView textNotifycart;
    private TextView txtAddtocart;
    String adofferid = "";

    String[] quantityarray = {"", "1", "2", "3", "4", "5", "more"};
    SpinnerCustomQuantityAdapter customAdapter;
    Button btnAddtocart;
    //similor product

    RecyclerView recyclesimilorprod;
    private ArrayList<GatterGetSimilorList> gatterGetSimilorLists;
    private AdapterSimilarProduct recyclerAdapter;

    // releted product

    RecyclerView recycleReletedProduct;
    private ArrayList<GatterGetReletedProductList> gatterGetReletedProductLists;
    private AdapterReletedProduct adapterReletedProduct;

    ScrollView scroll_view;

    RatingBar ratingbar;

    RelativeLayout cartRelativeLayout;
    private static int itemCounter = 0;
    String medid = "";
    String strimage = "", med_cat = "";
    Boolean isinternetpresent;
    ConnectDetector cd;
    TextView txtQty;
    TextView txtPrice, vendershopname, txtBrand;
    CardView cartAdditionaloffer;
    CheckBox chkadditionalOffercode, chkwish, share;
    String qty;
    Spinner spinnerProductQuantity;
    Double newprice = 0.0, orignalprice = 0.0, addoffer = 0.0, localoffer = 0.0, newpricecheck = 0.0;

    String addofferid = "";
    TextView txtUseIntraction, offline, txtCompositions, txtAlcohol_interaction, txtPregnancy_interaction, txtLactation_interaction, txtMedicine_interaction, txtDriving_interaction, txtKidney_interaction, txtLiver_interaction, txtSeverely_interacts, txtPrimary_use, txtCommon_side_effect, txtExpert_advice, txtFaq, txtHow_it_work, txtHow_to_use, txtHow_to_work, txtPrescription_required_not;
    TextView txtUseIntractionlable, offlinelable, txtCompositionslable, txtAlcohol_interactionlable, txtPregnancy_interactionlable, txtLactation_interactionlable, txtMedicine_interactionlable, txtDriving_interactionlable, txtKidney_interactionlable, txtLiver_interactionlable, txtSeverely_interactslable, txtPrimary_uselable, txtCommon_side_effectlable, txtExpert_advicelable, txtFaqlable, txtHow_it_worklable, txtHow_to_uselable, txtHow_to_worklable, txtPrescription_required_notlable;

    TextView txtReletedProduct, txtSimilor_product, ratingtv, txtvendor;

    LinearLayout layButtonbottom;

    BottomSheetDialog mBottomSheetDialog;

    ImageView imgCancel, imgFullimage;

    View sheetView;
    RatingBar ratingBar;
    Button btnbuy;
    String previes_values = "", vendorname = "", qtystr = "", store_satus = "";
    private ArrayList<GatterGetCartList> gatterGetCartLists;
    JSONObject Single_checkout = null;
    String type = "", vendor_id = "";
    JSONObject vendor = null;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ReuseMethod.hideStatusbar(Product_detailActivity.this);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        store = Keystore.getInstance(getApplicationContext());
        dialog = new ProgressDialog(this);
        // similor productlist
        gatterGetCartLists = new ArrayList<>();

        recyclesimilorprod = (RecyclerView) findViewById(R.id.recyclesimilorprod);
        gatterGetSimilorLists = new ArrayList<>();
        recyclerAdapter = new AdapterSimilarProduct(gatterGetSimilorLists, this);
        LinearLayoutManager recyclerlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclesimilorprod.setLayoutManager(recyclerlayoutManager);
        recyclesimilorprod.setHasFixedSize(true);
        recyclesimilorprod.setAdapter(recyclerAdapter);

        txtReletedProduct = (TextView) findViewById(R.id.txtReletedProduct);
        txtSimilor_product = (TextView) findViewById(R.id.txtSimilor_product);
        txtvendor = (TextView) findViewById(R.id.txtvendor);
        promocode = (TextView) findViewById(R.id.promocode);
        offline = (TextView) findViewById(R.id.offline);

        ratingbar = (RatingBar) findViewById(R.id.ratingbar);

        layButtonbottom = (LinearLayout) findViewById(R.id.layButtonbottom);

        scroll_view = (ScrollView) findViewById(R.id.scroll_view);

        //releted product list

        recycleReletedProduct = (RecyclerView) findViewById(R.id.recycleReletedProduct);
        gatterGetReletedProductLists = new ArrayList<>();
        adapterReletedProduct = new AdapterReletedProduct(gatterGetReletedProductLists, this);
        LinearLayoutManager recreletedmanager = new LinearLayoutManager(this);
        recycleReletedProduct.setLayoutManager(recreletedmanager);
        recycleReletedProduct.setHasFixedSize(true);
        recycleReletedProduct.setAdapter(adapterReletedProduct);


        mHeaderImageView = (ImageView) findViewById(R.id.imageview_header);
        //imgBtnCart = (ImageButton) findViewById(R.id.imgBtnCart);
        imgBtnSearch = (ImageButton) findViewById(R.id.imgBtnSearch);
        cartAdditionaloffer = (CardView) findViewById(R.id.cartAdditionaloffer);
        txtQty = (TextView) findViewById(R.id.txtQty);
        txtpackageing = (TextView) findViewById(R.id.txtpackageing);
        chkadditionalOffercode = (CheckBox) findViewById(R.id.chkadditionalOffercode);
        chkwish = (CheckBox) findViewById(R.id.chkwish);
        share = (CheckBox) findViewById(R.id.share);
        // layQuantity= (LinearLayout) findViewById(R.id.layQuantity);
        txtPrice = (TextView) findViewById(R.id.txtPrice);
        txtBrand = (TextView) findViewById(R.id.txtBrand);
        outof = (TextView) findViewById(R.id.outof);
        btnAddtocart = (Button) findViewById(R.id.btnAddtocart);
        btnbuy = (Button) findViewById(R.id.btnbuy);
        txtorignalprice = (TextView) findViewById(R.id.txtorignalprice);
        vendershopname = (TextView) findViewById(R.id.vendershopname);
        txtPriceOffer = (TextView) findViewById(R.id.txtPriceOffer);
        txtAdditionalOfferDesc = (TextView) findViewById(R.id.txtAdditionalOfferDesc);
        imageview_header_cartFly = (ImageView) findViewById(R.id.imageview_header_cartFly);
        mHeaderTitle = (TextView) findViewById(R.id.textview_title);
        textNotifycart = (TextView) findViewById(R.id.textNotifycart);
        txtAddtocart = (TextView) findViewById(R.id.txtAddtocart);

        //medicine details textview

        txtUseIntraction = (TextView) findViewById(R.id.txtUseIntraction);
        txtCompositions = (TextView) findViewById(R.id.txtCompositions);
        txtAlcohol_interaction = (TextView) findViewById(R.id.txtAlcohol_interaction);
        txtPregnancy_interaction = (TextView) findViewById(R.id.txtPregnancy_interaction);
        txtLactation_interaction = (TextView) findViewById(R.id.txtLactation_interaction);
        txtMedicine_interaction = (TextView) findViewById(R.id.txtMedicine_interaction);
        txtDriving_interaction = (TextView) findViewById(R.id.txtDriving_interaction);
        txtKidney_interaction = (TextView) findViewById(R.id.txtKidney_interaction);
        txtLiver_interaction = (TextView) findViewById(R.id.txtLiver_interaction);
        txtSeverely_interacts = (TextView) findViewById(R.id.txtSeverely_interacts);
        txtPrimary_use = (TextView) findViewById(R.id.txtPrimary_use);
        txtCommon_side_effect = (TextView) findViewById(R.id.txtCommon_side_effect);
        txtExpert_advice = (TextView) findViewById(R.id.txtExpert_advice);
        txtFaq = (TextView) findViewById(R.id.txtFaq);
        txtHow_it_work = (TextView) findViewById(R.id.txtHow_it_work);
        txtHow_to_use = (TextView) findViewById(R.id.txtHow_to_use);
        txtHow_to_work = (TextView) findViewById(R.id.txtHow_to_work);
        txtPrescription_required_not = (TextView) findViewById(R.id.txtPrescription_required_not);

        txtUseIntractionlable = (TextView) findViewById(R.id.txtUseIntractionlable);
        txtCompositionslable = (TextView) findViewById(R.id.txtCompositionslable);
        txtAlcohol_interactionlable = (TextView) findViewById(R.id.txtAlcohol_interactionlable);
        txtPregnancy_interactionlable = (TextView) findViewById(R.id.txtPregnancy_interactionlable);
        txtLactation_interactionlable = (TextView) findViewById(R.id.txtLactation_interactionlable);
        txtMedicine_interactionlable = (TextView) findViewById(R.id.txtMedicine_interactionlable);
        txtDriving_interactionlable = (TextView) findViewById(R.id.txtDriving_interactionlable);
        txtKidney_interactionlable = (TextView) findViewById(R.id.txtKidney_interactionlable);
        txtLiver_interactionlable = (TextView) findViewById(R.id.txtLiver_interactionlable);
        txtSeverely_interactslable = (TextView) findViewById(R.id.txtSeverely_interactslable);
        txtPrimary_uselable = (TextView) findViewById(R.id.txtPrimary_uselable);
        txtCommon_side_effectlable = (TextView) findViewById(R.id.txtCommon_side_effectlabel);
        txtExpert_advicelable = (TextView) findViewById(R.id.txtExpert_advicelable);
        txtFaqlable = (TextView) findViewById(R.id.txtFaqlable);
        txtHow_it_worklable = (TextView) findViewById(R.id.txtHow_it_worklable);
        txtHow_to_uselable = (TextView) findViewById(R.id.txtHow_to_uselable);
        txtHow_to_worklable = (TextView) findViewById(R.id.txtHow_to_worklable);
        txtPrescription_required_notlable = (TextView) findViewById(R.id.txtPrescription_required_notlable);

        //end medition details

        ///rating
        ratingtv = (TextView) findViewById(R.id.ratingtv);
        ratingBar = (RatingBar) findViewById(R.id.ratingbar);
        //end

        spinnerProductQuantity = (Spinner) findViewById(R.id.spinnerProductQuantity);

        spinnerProductQuantity.setPopupBackgroundResource(R.drawable.spinner_popup_background);
        spinnerProductQuantity.setPrompt("Select gender...");
        customAdapter = new SpinnerCustomQuantityAdapter(getApplicationContext(), quantityarray);
        spinnerProductQuantity.setAdapter(customAdapter);

        ViewCompat.setTransitionName(mHeaderImageView, VIEW_NAME_HEADER_IMAGE);
        ViewCompat.setTransitionName(mHeaderTitle, VIEW_NAME_HEADER_TITLE);
        cartRelativeLayout = (RelativeLayout) findViewById(R.id.cartRelativeLayout);
        Intent intent = getIntent();
        final String medname = intent.getStringExtra("medname");
        if (getIntent() != null) {
            medid = intent.getStringExtra("med_id");
            med_cat = intent.getStringExtra("med_cat");
            strimage = intent.getStringExtra("image");
        }


        btnbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendCartProductList();
            }
        });
        cd = new ConnectDetector(getApplicationContext());
        isinternetpresent = cd.isConnectToInternet();
        if (isinternetpresent) {
            getSingleProductDetail(medid);
            //getSimilorProduct(med_cat);
        } else {
            Toast.makeText(getApplicationContext(), "Internet Not Present", Toast.LENGTH_SHORT).show();
        }
        txtvendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vendor_info(vendor);
                // startActivity(new Intent(Product_detailActivity.this, Vendor_profile.class).putExtra("vendor_id", vendor_id).putExtra("name", vendorname));
            }
        });
        chkadditionalOffercode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    if (!TextUtils.isEmpty(type)) {
                        if (type.equalsIgnoreCase("percent")) {
                            Double quentity = Double.parseDouble(txtQty.getText().toString());
                            Double price = newpricecheck * quentity;
                            Double newprice = ((price * addoffer) / 100);
                            txtPrice.setText(getString(R.string.Rs) + String.valueOf(price - newprice));
                        } else {
                            Double quentity = Double.parseDouble(txtQty.getText().toString());
                            Double price = newpricecheck * quentity;
                            txtPrice.setText(getString(R.string.Rs) + String.valueOf(price - addoffer));
                        }
                    }

                    Toast.makeText(getApplicationContext(), "Additional offer applied", Toast.LENGTH_SHORT).show();
                } else {
                    Double quentity = Double.parseDouble(txtQty.getText().toString());
                    Double newprice = newpricecheck * quentity;
                    txtPrice.setText(getString(R.string.Rs) + String.valueOf(newprice));
                    Toast.makeText(getApplicationContext(), "Additional offer removed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        chkwish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                boolean pressedornot = buttonView.isPressed();
                if (pressedornot) {
                    if (isChecked) {
                        String urlis = ApiFileuri.ROOT_HTTP_URL + "product/add-wishlist";
                        WishList("addwish", urlis);
                    } else {
                        String urlis = ApiFileuri.ROOT_HTTP_URL + "product/remove-wishlist";
                        WishList("removeremove", urlis);

                    }
                }

            }
        });
        share.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, medname + "\n" + ApiFileuri.ROOT_URL_deep + "/product/" + medid);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });


        loadItem();

        setvaluecart();

        spinnerProductQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 6) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Product_detailActivity.this);
                    alertDialog.setMessage("Enter Quentity");

                    final EditText input = new EditText(Product_detailActivity.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    alertDialog.setView(input);
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    input.setText(txtQty.getText());
                    alertDialog.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String qtyii = input.getText().toString();
                                    txtQty.setText("Qty " + qtyii);

                                    Double price = newpricecheck * Double.parseDouble(qtyii);

                                    if (chkadditionalOffercode.isChecked()) {
                                        Double newprice = ((price * addoffer) / 100);
                                        txtPrice.setText(getString(R.string.Rs) + String.valueOf(price - newprice));
                                    } else {
                                        txtPrice.setText(getString(R.string.Rs) + String.valueOf(price));
                                    }

                                }
                            });

                    alertDialog.setNegativeButton("NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    alertDialog.show();

                } else if (position == 0) {

                } else {
                    txtQty.setText(quantityarray[position]);
                    Double price = newpricecheck * Double.parseDouble(quantityarray[position]);

                    if (chkadditionalOffercode.isChecked()) {
                        Double newprice = ((price * addoffer) / 100);
                        txtPrice.setText(getString(R.string.Rs) + String.valueOf(price - newprice));

                    } else {

                        txtPrice.setText(getString(R.string.Rs) + String.valueOf(price));
                    }
                }
                spinnerProductQuantity.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerProductQuantity.setSelection(0);
            }


        });


        scroll_view.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (scroll_view != null) {
                    if (scroll_view.getChildAt(0).getBottom() <= (scroll_view.getHeight() + scroll_view.getScrollY())) {
                        layButtonbottom.setVisibility(View.INVISIBLE);
                    } else {
                        if (!qtystr.equalsIgnoreCase("0") && !store_satus.equalsIgnoreCase("0")) {
                            layButtonbottom.setVisibility(View.VISIBLE);
                        }

                    }
                }
            }
        });


        mBottomSheetDialog = new BottomSheetDialog(this);
        sheetView = this.getLayoutInflater().inflate(R.layout.bottom_sheet_view_full_image, null);
        mBottomSheetDialog.setContentView(sheetView);

        imgCancel = (ImageView) sheetView.findViewById(R.id.imgCancel);
        imgFullimage = (ImageView) sheetView.findViewById(R.id.imgFullimage);

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBottomSheetDialog.dismiss();

            }
        });


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

    public void ReStartProduct(String medit) {
        medid = medit;
        getSingleProductDetail(medit);
    }


    private void getSingleProductDetail(final String medid) {

        dialog.setMessage("Fetching product details");
        dialog.setCancelable(false);
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.ROOT_HTTP_URL + "product/one",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // gatterMyOrderListModels.add(new GatterGetMyOrderListModel("",""));
                        // recyclerAdapter.notifyDataSetChanged();
                        JSONObject obj1 = null;
                        JSONObject obj = null;
                        try {
                            obj1 = new JSONObject(response);
                            obj = obj1.getJSONObject("top-product");
                            vendor = obj1.getJSONObject("vendor_data");
                            Single_checkout = obj;
                            if (!obj.getString("company_name").equals("") || !obj.getString("company_name").equals("null")) {
                                txtBrand.setText(obj.getString("company_name"));
                            } else {
                                if (!obj.getString("pd_company_name").equals("") || !obj.getString("pd_company_name").equals("null")) {
                                    txtBrand.setText(obj.getString("pd_company_name"));
                                }
                            }
                            mHeaderTitle.setText(obj.getString("brand_name"));
                            qtystr = obj.getString("qty");
                            store_satus = obj.getString("store_status");

                            if (qtystr.equalsIgnoreCase("0")) {
                                outof.setText("Out of stock");
                                outof.setTextColor(getResources().getColor(R.color.red));
                                layButtonbottom.setVisibility(View.GONE);
                            } else {
                                outof.setText("In stock (" + qtystr + ")");
                                outof.setTextColor(getResources().getColor(R.color.green));
                                layButtonbottom.setVisibility(View.VISIBLE);
                            }
                            vendorname = obj.getString("vendor_name");
                            vendor_id = obj.getString("vendor_id");

                            vendershopname.setText(vendorname);

                            if (store_satus.equalsIgnoreCase("0")) {
                                offline.setText("(Offline)");
                                offline.setTextColor(getResources().getColor(R.color.red));
                                layButtonbottom.setVisibility(View.GONE);
                            }

                            txtpackageing.setText(obj.getString("packaging_product"));

                            if (!TextUtils.isEmpty(obj.getString("use_interaction"))) {
                                txtUseIntraction.setVisibility(View.VISIBLE);
                                txtUseIntractionlable.setVisibility(View.VISIBLE);
                                txtUseIntraction.setText(obj.getString("use_interaction"));
                            }
                            if (!TextUtils.isEmpty(obj.getString("compositions"))) {
                                txtCompositions.setVisibility(View.VISIBLE);
                                txtCompositionslable.setVisibility(View.VISIBLE);
                                txtCompositions.setText(obj.getString("compositions"));
                            }

                            if (!TextUtils.isEmpty(obj.getString("alcohol_interaction"))) {
                                txtAlcohol_interaction.setVisibility(View.VISIBLE);
                                txtAlcohol_interactionlable.setVisibility(View.VISIBLE);
                                txtAlcohol_interaction.setText(obj.getString("alcohol_interaction"));
                            }

                            if (!TextUtils.isEmpty(obj.getString("pregnancy_interaction"))) {
                                txtPregnancy_interaction.setVisibility(View.VISIBLE);
                                txtPregnancy_interactionlable.setVisibility(View.VISIBLE);
                                txtPregnancy_interaction.setText(obj.getString("pregnancy_interaction"));
                            }
                            if (!TextUtils.isEmpty(obj.getString("lactation_interaction"))) {
                                txtLactation_interaction.setVisibility(View.VISIBLE);
                                txtLactation_interactionlable.setVisibility(View.VISIBLE);
                                txtLactation_interaction.setText(obj.getString("lactation_interaction"));
                            }

                            if (!TextUtils.isEmpty(obj.getString("medicine_interaction"))) {
                                txtMedicine_interaction.setVisibility(View.VISIBLE);
                                txtMedicine_interactionlable.setVisibility(View.VISIBLE);
                                txtMedicine_interaction.setText(obj.getString("medicine_interaction"));
                            }
                            if (!TextUtils.isEmpty(obj.getString("driving_interaction"))) {
                                txtDriving_interaction.setVisibility(View.VISIBLE);
                                txtDriving_interactionlable.setVisibility(View.VISIBLE);
                                txtDriving_interaction.setText(obj.getString("driving_interaction"));
                            }
                            if (!TextUtils.isEmpty(obj.getString("kidney_interaction"))) {
                                txtKidney_interaction.setVisibility(View.VISIBLE);
                                txtKidney_interactionlable.setVisibility(View.VISIBLE);
                                txtKidney_interaction.setText(obj.getString("kidney_interaction"));
                            }

                            if (!TextUtils.isEmpty(obj.getString("liver_interaction"))) {
                                txtLiver_interaction.setVisibility(View.VISIBLE);
                                txtLiver_interactionlable.setVisibility(View.VISIBLE);
                                txtLiver_interaction.setText(obj.getString("liver_interaction"));
                            }

                            if (!TextUtils.isEmpty(obj.getString("severely_interacts"))) {
                                txtSeverely_interacts.setVisibility(View.VISIBLE);
                                txtSeverely_interactslable.setVisibility(View.VISIBLE);
                                txtSeverely_interacts.setText(obj.getString("severely_interacts"));
                            }
                            if (!TextUtils.isEmpty(obj.getString("primary_use"))) {
                                txtPrimary_use.setVisibility(View.VISIBLE);
                                txtPrimary_uselable.setVisibility(View.VISIBLE);
                                txtPrimary_use.setText(obj.getString("primary_use"));
                            }
                            if (!TextUtils.isEmpty(obj.getString("common_side_effect"))) {
                                txtCommon_side_effect.setVisibility(View.VISIBLE);
                                txtCommon_side_effectlable.setVisibility(View.VISIBLE);
                                txtCommon_side_effect.setText(obj.getString("common_side_effect"));
                            }

                            if (!TextUtils.isEmpty(obj.getString("expert_advice"))) {
                                txtExpert_advice.setVisibility(View.VISIBLE);
                                txtExpert_advicelable.setVisibility(View.VISIBLE);
                                txtExpert_advice.setText(obj.getString("expert_advice"));
                            }

                            if (!TextUtils.isEmpty(obj.getString("faq"))) {
                                txtFaq.setVisibility(View.VISIBLE);
                                txtFaqlable.setVisibility(View.VISIBLE);
                                txtFaq.setText(obj.getString("faq"));
                            }
                            if (!TextUtils.isEmpty(obj.getString("how_it_work"))) {
                                txtHow_it_work.setVisibility(View.VISIBLE);
                                txtHow_it_worklable.setVisibility(View.VISIBLE);
                                txtHow_it_work.setText(obj.getString("how_it_work"));
                            }
                            if (!TextUtils.isEmpty(obj.getString("how_to_use"))) {
                                txtHow_to_use.setVisibility(View.VISIBLE);
                                txtHow_to_uselable.setVisibility(View.VISIBLE);
                                txtHow_to_use.setText(obj.getString("how_to_use"));
                            }
                            if (!TextUtils.isEmpty(obj.getString("how_to_work"))) {
                                txtHow_to_work.setVisibility(View.VISIBLE);
                                txtHow_to_worklable.setVisibility(View.VISIBLE);
                                txtHow_to_work.setText(obj.getString("how_to_work"));
                            }
                            if (!TextUtils.isEmpty(obj.getString("prescription_required_not"))) {
                                txtPrescription_required_not.setVisibility(View.VISIBLE);
                                txtPrescription_required_notlable.setVisibility(View.VISIBLE);
                                txtPrescription_required_not.setText(obj.getString("prescription_required_not"));
                            }

                            wishid = obj.getString("wishlist");

                            if (!TextUtils.isEmpty(wishid)) {
                                if (wishid.equalsIgnoreCase("1")) {
                                    chkwish.setChecked(true);
                                }

                            }
                            strimage = obj.getString("image");
                            if (!strimage.equals("") || !strimage.equals(null)) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && addTransitionListener()) {
                                    loadThumbnail();
                                } else {
                                    loadFullSizeImage();
                                }
                                Glide.with(getApplicationContext()).load(ApiFileuri.ROOT_URL_PRODUCT_IMAGE + strimage).into(imgFullimage);
                            }
                            if (!TextUtils.isEmpty(vendor.getString("average_rating"))) {
                                ratingbar.setRating(Float.parseFloat(vendor.getString("average_rating")));
                                ratingtv.setText(vendor.getString("average_rating"));
                            }
                            if (!obj.getString("sale_price").equals(obj.getString("mrp")) || !obj.getString("sale_price").equals("") || !obj.getString("sale_price").equals("null")) {
                                String saleprice = obj.getString("sale_price");
                                newpricecheck = Double.parseDouble(obj.getString("sale_price"));
                                Double salepricedbl = Double.parseDouble(saleprice);
                                Double qty = Double.valueOf(1);
                                if (!obj.getString("qty").equals("") || obj.getString("qty").equals("null")) {
                                    qty = Double.parseDouble(obj.getString("qty"));

                                }

                                Double mrppricedbl = Double.parseDouble(obj.getString("mrp"));

                                Double discperprice = FormulasCalculation.CalculateProductDiscount(mrppricedbl, salepricedbl);

                                Double multiplymrpprice = mrppricedbl;

                                Double multiplysaleprice = salepricedbl;


                                txtPriceOffer.setText(String.valueOf(discperprice) + " % off");
                                txtPrice.setText(getString(R.string.Rs) + String.valueOf(multiplysaleprice));
                                //txtorignalprice.setText(String.valueOf(multiplymrpprice));
                                Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/rupee.ttf");
                                txtorignalprice.setTypeface(tf);
                                txtorignalprice.setText(String.valueOf(multiplymrpprice));

                            } else {
                                txtPrice.setVisibility(View.GONE);

                                Double salepricedbl = Double.parseDouble(obj.getString("sale_price"));

                                Double qty = Double.parseDouble(obj.getString("qty"));

                                Double price = salepricedbl;

                                txtPrice.setText(getString(R.string.Rs) + String.valueOf(price));
                                orignalprice = price;
                                txtorignalprice.setVisibility(View.GONE);

                            }
                            newprice = Double.parseDouble(obj.getString("sale_price"));
                            orignalprice = Double.parseDouble(obj.getString("mrp"));
                            orignalprice = newprice;
                            if (obj.getString("additional_offer").equalsIgnoreCase("1")) {
                                cartAdditionaloffer.setVisibility(View.VISIBLE);
                                addoffer = (Double.parseDouble(obj.getString("additional_offer_percentage")));
                                addofferid = obj.getString("additional_offer_id");
                                type = obj.getString("additional_offer_type");
                                promocode.setText(obj.getString("additional_offer_code"));
                                txtAdditionalOfferDesc.setText(obj.getString("additional_offer_decription"));
                            }

                            gatterGetSimilorLists.clear();
                            gatterGetReletedProductLists.clear();

                            int similorlength = obj1.getJSONArray("similor-product").length();
                            int reletedlength = obj1.getJSONArray("related-product").length();

                            if (similorlength != 0) {
                                txtSimilor_product.setVisibility(View.VISIBLE);
                                getSimilorProduct(obj1.getJSONArray("similor-product"));
                            } else {
                                recyclerAdapter.notifyDataSetChanged();
                                txtSimilor_product.setVisibility(View.GONE);
                            }

                            if (reletedlength != 0) {
                                txtReletedProduct.setVisibility(View.VISIBLE);
                                getReletedProduct(obj1.getJSONArray("related-product"));
                            } else {
                                recyclerAdapter.notifyDataSetChanged();
                                txtReletedProduct.setVisibility(View.GONE);
                            }

                            dialog.dismiss();

                            scroll_view.fullScroll(View.FOCUS_UP);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", medid);
                OneMLUserSpace oneMLUserSpace = OneMLUserSpace.getInstance(Product_detailActivity.this);
                UserModel userModel = oneMLUserSpace.getUserProfile();
                if (userModel != null) {
                    if(HelperFunction.checkStringEmpty(userModel.getId())){
                        params.put("user_id", userModel.getId());
                    }
                }
                //params.put("user_id", store.get("id"));
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void WishList(final String wish, String uriis) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uriis,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            String strstatus = obj.getString("status");
                            if (strstatus.equals("true")) {
                                if (wish.equals("removeremove")) {
                                    Toast.makeText(getApplicationContext(), obj.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), obj.getString("product").toString(), Toast.LENGTH_SHORT).show();
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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if (wish.equals("addwish")) {
                    params.put("product_id", medid);
                    params.put("user_id", store.get("id"));
                }
                if (!wishid.equals("")) {
                    if (wish.equals("removeremove")) {
                        params.put("product_id", medid);
                        params.put("user_id", store.get("id"));
                    }
                }
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void clickFunctionProductDetail(View view) {
        switch (view.getId()) {
            case R.id.btnAddtocart:
                String result = "";
                String quentity = txtQty.getText().toString();
                if (chkadditionalOffercode.isChecked()) {
                    result = ReuseMethod.addToCardDatabaseProductDetails(getApplicationContext(), medid, quentity, addofferid);
                } else {
                    result = ReuseMethod.addToCardDatabase(getApplicationContext(), medid, quentity);
                }
                setvaluecart();
                if (!result.equals("")) {
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(result);
                        String strstatus = obj.getString("status");
                        if (strstatus.equals("false")) {
                            Toast.makeText(getApplicationContext(), obj.getString("product"), Toast.LENGTH_SHORT).show();
                        } else {
                            ReuseMethod.SetTotalCartItem(getApplicationContext(), obj.getString("cart-total"));
                            Toast.makeText(getApplicationContext(), obj.getString("product"), Toast.LENGTH_SHORT).show();
                            setvaluecart();
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                break;
            case R.id.imgBtnCart:
                ReuseMethod.OpenCartActivity(this);
                break;
            case R.id.imgBtnSearch:
                ReuseMethod.OpenSearchActivity(this);
                break;
            case R.id.txtQty:
                spinnerProductQuantity.performClick();
                break;
            case R.id.imageview_header_cartFly:
                startActivity(new Intent(this, Full_image.class).putExtra("img", ApiFileuri.ROOT_URL_PRODUCT_IMAGE + strimage));
                // mBottomSheetDialog.show();
                //imageview_header_cartFly
                break;
        }


    }

    private void loadItem() {
        // Set the title TextView to the item's name and author
        Intent intent = getIntent();
        String medname = intent.getStringExtra("medname");
        mHeaderTitle.setText(medname);
        textNotifycart.setText(ReuseMethod.SharedPrefranceGetCartValue(getApplicationContext()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && addTransitionListener()) {
            loadThumbnail();
        } else {
            loadFullSizeImage();
        }
    }

    private void loadThumbnail() {
        Glide.with(mHeaderImageView.getContext())
                .load(ApiFileuri.ROOT_URL_PRODUCT_IMAGE + strimage)
                .into(mHeaderImageView);

        Glide.with(imageview_header_cartFly.getContext())
                .load(ApiFileuri.ROOT_URL_PRODUCT_IMAGE + strimage)
                .into(imageview_header_cartFly);

    }

    private void loadFullSizeImage() {
        Glide.with(mHeaderImageView.getContext())
                .load(ApiFileuri.ROOT_URL_PRODUCT_IMAGE + strimage)
                .into(mHeaderImageView);

        Glide.with(imageview_header_cartFly.getContext())
                .load(ApiFileuri.ROOT_URL_PRODUCT_IMAGE + strimage)
                .into(imageview_header_cartFly);
    }

    private void getSimilorProduct(final JSONArray similorproduct) {
        try {
            JSONArray jsonArray = similorproduct;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = null;
                json = jsonArray.getJSONObject(i);
                gatterGetSimilorLists.add(new GatterGetSimilorList(
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        recyclerAdapter.notifyDataSetChanged();
    }

    private void getReletedProduct(final JSONArray similorproduct) {
        try {
            JSONArray jsonArray = similorproduct;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = null;
                json = jsonArray.getJSONObject(i);
                gatterGetReletedProductLists.add(new GatterGetReletedProductList(
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapterReletedProduct.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean addTransitionListener() {
        final Transition transition = getWindow().getSharedElementEnterTransition();
        if (transition != null) {
            // There is an entering shared element transition so add a listener to it
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    // As the transition has ended, we can now load the full-size image
                    loadFullSizeImage();
                    // Make sure we remove ourselves as a listener
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionStart(Transition transition) {
                    // No_internet-op
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    // Make sure we remove ourselves as a listener
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionPause(Transition transition) {
                    // No_internet-op
                }

                @Override
                public void onTransitionResume(Transition transition) {
                    // No_internet-op
                }
            });
            return true;
        }

        // If we reach here then we have not added a listener
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }


    public void SeemoreOnTextview()
    {
        ReuseMethod.makeTextViewResizable(txtUseIntraction,3,"See More",true);
        ReuseMethod.makeTextViewResizable(txtCompositions,3,"See More",true);
        ReuseMethod.makeTextViewResizable(txtAlcohol_interaction,3,"See More",true);
        ReuseMethod.makeTextViewResizable(txtPregnancy_interaction,3,"See More",true);
        ReuseMethod.makeTextViewResizable(txtLactation_interaction,3,"See More",true);
        ReuseMethod.makeTextViewResizable(txtMedicine_interaction,3,"See More",true);
        ReuseMethod.makeTextViewResizable(txtDriving_interaction,3,"See More",true);
        ReuseMethod.makeTextViewResizable(txtKidney_interaction,3,"See More",true);
        ReuseMethod.makeTextViewResizable(txtLiver_interaction,3,"See More",true);
        ReuseMethod.makeTextViewResizable(txtSeverely_interacts,3,"See More",true);
        ReuseMethod.makeTextViewResizable(txtPrimary_use,3,"See More",true);
        ReuseMethod.makeTextViewResizable(txtCommon_side_effect,3,"See More",true);
        ReuseMethod.makeTextViewResizable(txtExpert_advice,3,"See More",true);
        ReuseMethod.makeTextViewResizable(txtFaq,3,"See More",true);
        ReuseMethod.makeTextViewResizable(txtHow_it_work,3,"See More",true);
        ReuseMethod.makeTextViewResizable(txtHow_to_use,3,"See More",true);
        ReuseMethod.makeTextViewResizable(txtPrescription_required_not,3,"See More",true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setvaluecart();
    }

    public void SendCartProductList() {
        String id = " ", additional_offer_percentage = "", additional_offer_id = "", additional_offer_code = "", additional_offer_decription = "", additional_offer_start = "", additional_offer_expired = "";
        try {if(Single_checkout.getString("additional_offer_percentage")!=null){
            additional_offer_percentage = Single_checkout.getString("additional_offer_percentage");
        }   if(Single_checkout.getString("additional_offer_id")!=null) {
            additional_offer_id = Single_checkout.getString("additional_offer_id");
        }   if(Single_checkout.getString("additional_offer_code")!=null) {
            additional_offer_code = Single_checkout.getString("additional_offer_code");
        }   if(Single_checkout.getString("additional_offer_decription")!=null) {
            additional_offer_decription = Single_checkout.getString("additional_offer_decription");
        }   if(Single_checkout.getString("additional_offer_start")!=null) {
            additional_offer_start = Single_checkout.getString("additional_offer_start");
        }   if(Single_checkout.getString("additional_offer_expired")!=null) {
            additional_offer_expired = Single_checkout.getString("additional_offer_expired");
        }
            id = store.get("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(additional_offer_percentage)) {
            additional_offer_percentage = "";
        }
        if (TextUtils.isEmpty(additional_offer_id)) {
            additional_offer_id = "";
        }
        if (TextUtils.isEmpty(additional_offer_code)) {
            additional_offer_code = "";
        }
        if (TextUtils.isEmpty(additional_offer_decription)) {
            additional_offer_decription = "";
        }
        if (TextUtils.isEmpty(additional_offer_start)) {
            additional_offer_start = "";
        }
        if (TextUtils.isEmpty(additional_offer_expired)) {
            additional_offer_expired = "";
        }
        if (TextUtils.isEmpty(store.get("id"))) {
            id = "";
        }
        try {
            gatterGetCartLists.clear();
            if (Single_checkout.getString("additional_offer").equalsIgnoreCase("1") && chkadditionalOffercode.isChecked()) {
                Double final_price = 0.0;
                if (type.equalsIgnoreCase("percent")) {
                    Double quentity = Double.parseDouble(txtQty.getText().toString());
                    Double price = newpricecheck * quentity;
                    Double newprice = ((price * addoffer) / 100);
                    final_price = price - newprice;
                } else {
                    Double quentity = Double.parseDouble(txtQty.getText().toString());
                    Double price = newpricecheck * quentity;
                    final_price = price - addoffer;
                }
                txtPrice.setText(getString(R.string.Rs) + String.valueOf(final_price));
                gatterGetCartLists.add(new GatterGetCartList(
                        "",
                        id,
                        txtQty.getText().toString(),
                        Single_checkout.getString("vendor_name"),//
                        Single_checkout.getString("id"),
                        Single_checkout.getString("vendor_id"),
                        Single_checkout.getString("pd_brand_name"),
                        String.valueOf(final_price),
                        Single_checkout.getString("mrp"),
                        additional_offer_percentage,//additional_offer_percentage
                        additional_offer_id,//additional_offer_id
                        additional_offer_code,//additional_offer_code
                        //additional_offer_expired
                        strimage, Single_checkout.getString("company_name"),
                        Single_checkout.getString("additional_offer_type"),
                        Single_checkout.getString("additional_offer")//image
                ));
            } else {
                Double quentity = Double.parseDouble(txtQty.getText().toString());
                Double newprice = newpricecheck * quentity;
                txtPrice.setText(getString(R.string.Rs) + String.valueOf(newprice));
                gatterGetCartLists.add(new GatterGetCartList(
                        "",
                        id,
                        txtQty.getText().toString(),
                        Single_checkout.getString("vendor_name"),//
                        Single_checkout.getString("id"),
                        Single_checkout.getString("vendor_id"),
                        Single_checkout.getString("pd_brand_name"),
                        String.valueOf(newprice),
                        Single_checkout.getString("mrp"),
                        "",//additional_offer_percentage
                        "",//additional_offer_id
                        "",//additional_offer_code
                        strimage, Single_checkout.getString("company_name"),
                        "",
                        "0"//image
                ));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            Intent intent = new Intent(this, CheckoutActivity.class);
            intent.putExtra("FILES_TO_SEND", gatterGetCartLists);
            startActivity(intent);
        }
    }

    public void vendor_info(final JSONObject vendor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_vendor_profile, null);
        TextView seller = (TextView) view.findViewById(R.id.seller);
        TextView store = (TextView) view.findViewById(R.id.store);
        TextView storelic = (TextView) view.findViewById(R.id.storelic);
        TextView storeaddress = (TextView) view.findViewById(R.id.storeaddress);
        TextView dis = (TextView) view.findViewById(R.id.dis);
        ImageView profile = (ImageView) view.findViewById(R.id.profile);
        ImageView close = (ImageView) view.findViewById(R.id.close);
        String img = "";
        try {
            seller.setText(vendor.getString("vendor_name_main"));
            store.setText(vendor.getString("vendor_name"));
            storelic.setText(vendor.getString("dl_number"));
            storeaddress.setText(vendor.getString("store_address"));
            dis.setText(vendor.getString("area_km") + " Km");
            Glide.with(this)
                    .load(ApiFileuri.ROOT_VENDOR_IMAGE + vendor.getString("vendor_profile"))
                    .into(profile);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Product_detailActivity.this, Product_vendor.class).putExtra("id", vendor.getString("vendor_id")).putExtra("name", vendor.getString("vendor_name_main")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Product_detailActivity.this, Full_image.class).putExtra("img", ApiFileuri.ROOT_VENDOR_IMAGE + vendor.getString("vendor_profile")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        builder.setView(view);
        final AlertDialog dialog = builder.create();
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

