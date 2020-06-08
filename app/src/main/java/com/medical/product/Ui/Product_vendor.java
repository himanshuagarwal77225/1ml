package com.medical.product.Ui;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.medical.product.R;
import com.medical.product.Utils.Utlity;
import com.medical.product.adapter.HorizontalCategoryProductAdapter;
import com.medical.product.adapter.HorizontalProductsCatehoryRecycleViewAdapter;
import com.medical.product.adapter.NoticeAdapter;
import com.medical.product.adapter.VerticalRecycleProdCategAdapter;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.BaseUtils;
import com.medical.product.helpingFile.DemoConfiguration;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.MyAdapter;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.Banner;
import com.medical.product.models.ExpandedMenuModel;
import com.medical.product.models.HorizontalModelProducts;
import com.medical.product.models.HorizontalModelSaveCategory;
import com.medical.product.models.VerticalModelCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class Product_vendor extends AppCompatActivity {
    LinearLayout content, layMyOrder;

    TextView navopen, txtversioncode, txtNoDatafound;
    NavigationView navigationView;
    LinearLayout laySearch;
    boolean isUp;
    private static final int REQUEST_CODE = 1234;
    static AutocompleteFilter filter;
    //recycleview
    ShimmerRecyclerView verticleRecycleView;
    VerticalRecycleProdCategAdapter adapter;
    ArrayList<VerticalModelCategory> arrayListvertical;


    //end recycle view

    TextView textNotify, txtUserLocation;
    private int itemCounter = 0;
    static TextView textView, txtCategoryName, textNotifycart;
    ImageButton imgBtnCart, imgBtnSearch;
    Button btnUploadPrescription;
    static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    com.medical.product.adapter.ExpandableListAdapter mMenuAdapter;
    HashMap<ExpandedMenuModel, List<String>> listDataChild;
    private NoticeAdapter adapterretro;
    private RecyclerView recyclerView;
    Button btnSortBy, btnFilter;
    String catgory = "", brand = "";

    //permission

    int PERMISSION_REQUEST_CONTACT = 100;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_SMS,
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CONTACTS,
    };

    ArrayList<String> myList;

    private ViewPager mPager;
    private static int currentPage = 0;
    private static final String[] XMEN = {"dsf", "adfsdf", "adf"};

    private ArrayList<Banner> XMENArray;
    Keystore store;
    ArrayList<HorizontalModelSaveCategory> arrayModelSaveCategory = new ArrayList<>();
    BottomSheetDialog mBottomSheetDialog;
    View sheetView;
    LinearLayout laylowtohigh, layhightolow, layReverce;
    String categoryid = "", categoryname = "";
    String searchtype = "";
    String strimage = "";
    NestedScrollView nestedscroll;

    LinearLayout laySortFilter, noproduct;
    FrameLayout main_frame;
    Button shopnow;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    String vendor_id = "";
    String vendor_name = "";
    String min = "", max = "";
    FrameLayout slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_vendor);
        store = Keystore.getInstance(getApplicationContext());

        vendor_id = getIntent().getStringExtra("id");
        vendor_name = getIntent().getStringExtra("name");
        Utlity.selected_vendor = vendor_id;
        Utlity.selected_vendorname = vendor_name;

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        getSupportActionBar().setTitle(vendor_name);


        content = (LinearLayout) findViewById(R.id.content);
        laySortFilter = (LinearLayout) findViewById(R.id.laySortFilter);
        noproduct = (LinearLayout) findViewById(R.id.noproduct);
        txtNoDatafound = (TextView) findViewById(R.id.txtNoDatafound);
        main_frame = (FrameLayout) findViewById(R.id.mainframe);
        btnSortBy = (Button) findViewById(R.id.btnSortBy);
        btnFilter = (Button) findViewById(R.id.btnFilter);
        shopnow = (Button) findViewById(R.id.shopnow);
        btnUploadPrescription = (Button) findViewById(R.id.btnUploadPrescription);
        slider = (FrameLayout) findViewById(R.id.slider);

        nestedscroll = (NestedScrollView) findViewById(R.id.nestedscroll);
        Intent intent = getIntent();
        shopnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        nestedscroll.fullScroll(View.FOCUS_UP);

        if (searchtype.equals("category")) {
            txtCategoryName.setText(intent.getStringExtra("name"));
            txtNoDatafound.setVisibility(View.GONE);
        } else if (searchtype.equals("brand")) {
            txtCategoryName.setText(intent.getStringExtra("name"));
            txtNoDatafound.setVisibility(View.GONE);
        }

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        isUp = true;

        layMyOrder = (LinearLayout) findViewById(R.id.layMyOrder);
        arrayListvertical = new ArrayList<>();
        final DemoConfiguration demoConfiguration = BaseUtils.getDemoConfiguration(0, this);
        setTheme(demoConfiguration.getStyleResource());
        /* setContentView(demoConfiguration.getLayoutResource());*/
        setTitle(demoConfiguration.getTitleResource());
        verticleRecycleView = (ShimmerRecyclerView) findViewById(R.id.recycleview);
        if (demoConfiguration.getItemDecoration() != null) {
            verticleRecycleView.addItemDecoration(demoConfiguration.getItemDecoration());
        }

        verticleRecycleView.setHasFixedSize(true);
        verticleRecycleView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new VerticalRecycleProdCategAdapter(this, arrayListvertical);
        verticleRecycleView.setAdapter(adapter);
        verticleRecycleView.showShimmerAdapter();
        verticleRecycleView.postDelayed(new Runnable() {
            @Override
            public void run() {
                NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.nestedscroll);
                nestedScrollView.smoothScrollTo(0, 0);
            }
        }, 3000);

        mBottomSheetDialog = new BottomSheetDialog(this);
        sheetView = this.getLayoutInflater().inflate(R.layout.bottom_sheet_short, null);
        mBottomSheetDialog.setContentView(sheetView);


        laylowtohigh = (LinearLayout) sheetView.findViewById(R.id.laylowtohigh);
        layhightolow = (LinearLayout) sheetView.findViewById(R.id.layhightolow);
        layReverce = (LinearLayout) sheetView.findViewById(R.id.layReverce);
        laylowtohigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                HorizontalCategoryProductAdapter.getFilter("asc");

                HorizontalProductsCatehoryRecycleViewAdapter.getFilter("asc");

                //  adapter. notufy();
                adapter.notifyDataSetChanged();

                mBottomSheetDialog.dismiss();

            }
        });

        layhightolow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HorizontalCategoryProductAdapter.getFilter("desc");
                HorizontalProductsCatehoryRecycleViewAdapter.getFilter("desc");

                adapter.notifyDataSetChanged();

                mBottomSheetDialog.dismiss();
            }
        });
        layReverce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HorizontalCategoryProductAdapter.getFilter("reverce");
                HorizontalProductsCatehoryRecycleViewAdapter.getFilter("reverce");

                adapter.notifyDataSetChanged();

                mBottomSheetDialog.dismiss();
            }
        });

        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.i("asdf", "asd bottt" + "bottom sheet dismissed");
                // Do something
            }
        });

        //setvaluecart();

        getAllHomeData();
    }

    private void getAllHomeData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.seller,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            //  String strstatus = obj.getString("status");
                            String strstatus = "true";
                            if (strstatus.equals("false")) {
                                main_frame.setVisibility(View.GONE);
                                noproduct.setVisibility(View.VISIBLE);
                            } else {
                                arrayListvertical.clear();
                                Iterator<String> iter = obj.keys();
                                while (iter.hasNext()) {
                                    String key = iter.next();
                                    try {
                                        JSONArray jsonArray = new JSONArray(obj.getJSONArray(key).toString());
                                        VerticalModelCategory mVerticalModel = new VerticalModelCategory();
                                        mVerticalModel.setTitle(key);

                                        if (jsonArray.length() == 0) {
                                            main_frame.setVisibility(View.GONE);
                                            noproduct.setVisibility(View.VISIBLE);
                                        }
                                        if (key.equals("hotdeals")) {
                                            ArrayList<HorizontalModelProducts> arrayListHorizontalProducts = new ArrayList<>();
                                            for (int j = 0; j < jsonArray.length(); j++) {
                                                HorizontalModelProducts mHorizontalModelProducts = new HorizontalModelProducts(
                                                        jsonArray.getJSONObject(j).getString("id"),
                                                        jsonArray.getJSONObject(j).getString("unique_code"),
                                                        jsonArray.getJSONObject(j).getString("vendor_id"),
                                                        jsonArray.getJSONObject(j).getString("vendor_name"),
                                                        jsonArray.getJSONObject(j).getString("pd_brand_name"),
                                                        jsonArray.getJSONObject(j).getString("pd_company_name"),
                                                        jsonArray.getJSONObject(j).getString("image"),
                                                        jsonArray.getJSONObject(j).getString("brand_name"),
                                                        jsonArray.getJSONObject(j).getString("company_name"),
                                                        jsonArray.getJSONObject(j).getString("mrp"),
                                                        jsonArray.getJSONObject(j).getString("sale_price"),
                                                        jsonArray.getJSONObject(j).getString("qty"), jsonArray.getJSONObject(j).getString("store_status")
                                                );
                                                arrayListHorizontalProducts.add(mHorizontalModelProducts);
                                            }
                                            mVerticalModel.setArrayListProducts(arrayListHorizontalProducts);
                                            arrayListvertical.add(mVerticalModel);
                                        }

                                        if (key.equals("product")) {
                                            ArrayList<HorizontalModelProducts> arrayListHorizontalProducts = new ArrayList<>();
                                            for (int j = 0; j < jsonArray.length(); j++) {
                                                HorizontalModelProducts mHorizontalModelProducts = new HorizontalModelProducts(
                                                        jsonArray.getJSONObject(j).getString("id"),
                                                        jsonArray.getJSONObject(j).getString("unique_code"),
                                                        jsonArray.getJSONObject(j).getString("vendor_id"),
                                                        jsonArray.getJSONObject(j).getString("vendor_name"),
                                                        jsonArray.getJSONObject(j).getString("pd_brand_name"),
                                                        jsonArray.getJSONObject(j).getString("pd_company_name"),
                                                        jsonArray.getJSONObject(j).getString("image"),
                                                        jsonArray.getJSONObject(j).getString("brand_name"),
                                                        jsonArray.getJSONObject(j).getString("company_name"),
                                                        jsonArray.getJSONObject(j).getString("mrp"),
                                                        jsonArray.getJSONObject(j).getString("sale_price"),
                                                        jsonArray.getJSONObject(j).getString("qty"),
                                                        jsonArray.getJSONObject(j).getString("store_status")
                                                );
                                                arrayListHorizontalProducts.add(mHorizontalModelProducts);
                                            }
                                            mVerticalModel.setArrayListProducts(arrayListHorizontalProducts);
                                            arrayListvertical.add(mVerticalModel);
                                        } else if (key.equals("slider")) {
                                            set_slider(jsonArray);
                                        }
                                    } catch (JSONException e) {
                                        Log.i("asdf", "namenamename====================" + e);
                                        // Something went wrong!
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                verticleRecycleView.hideShimmerAdapter();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        verticleRecycleView.hideShimmerAdapter();
                        main_frame.setVisibility(View.GONE);
                        noproduct.setVisibility(View.VISIBLE);

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", vendor_id);
                params.put("filter_catgory", catgory);
                params.put("filter_brand", brand);
                params.put("minamount", min);
                params.put("maxamount", max);
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
            case R.id.imgBtnCart:
                ReuseMethod.OpenCartActivity(this);
                break;
            case R.id.imgBtnSearch:
                getApplicationContext().startActivity(new Intent(getApplicationContext(), SearchProductActivity.class));
                break;
            case R.id.txtUserLocation:

                break;
            case R.id.btnSortBy:
                mBottomSheetDialog.show();
                break;
            case R.id.btnFilter:
                Intent intent = new Intent(this, FilterActivity.class);
                startActivityForResult(intent, 5);
                break;
            case R.id.btnUploadPrescription:
                startActivity(new Intent(getApplicationContext(), UploadPrescriptionActivity.class));
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.dashbord, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private void getContact() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));

                        Log.i("contact", "get contact list========name=" + name + "number==" + phoneNo);

                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContact();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }


        // other 'case' lines to check for other
        // permissions this app might request

    }

    private void OnSearchAddressChange(final String searchaddress) {
        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.ROOT_HTTP_URL + "user/set",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "No_internet data found", Toast.LENGTH_SHORT).show();
                        //Log.i("sdfads","profile error========="+error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("onSearchAddress", searchaddress);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 5) {
            searchtype = "";
            if (!TextUtils.isEmpty(data.getStringExtra("catgory")) || !TextUtils.isEmpty(data.getStringExtra("brands"))) {
                catgory = data.getStringExtra("catgory");
                brand = data.getStringExtra("brands");
                getAllHomeData();
            } else if (!TextUtils.isEmpty(data.getStringExtra("amountmin")) || !TextUtils.isEmpty(data.getStringExtra("amountmax"))) {
                min = data.getStringExtra("amountmin");
                max = data.getStringExtra("amountmax");
                getAllHomeData();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void set_slider(JSONArray array) {
        if (array.length()!= 0) {
            XMENArray = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject object = array.getJSONObject(i);
                    Banner banner = new Banner();
                    banner.setId(object.getString("id"));
                    banner.setCategory_id(object.getString("category_id"));
                    banner.setProduct_id(object.getString("product_id"));
                    banner.setBanner(ApiFileuri.ROOT_URL_advertisement_IMAGE + object.getString("image"));
                    XMENArray.add(banner);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            MyAdapter adapter = new MyAdapter(Product_vendor.this, XMENArray);
            mPager = (ViewPager) findViewById(R.id.pager);
            mPager.setAdapter(adapter);
            CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
            indicator.setViewPager(mPager);
        } else {
            slider.setVisibility(View.GONE);
        }

    }


}
