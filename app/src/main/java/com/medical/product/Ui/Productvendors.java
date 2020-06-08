package com.medical.product.Ui;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.view.ViewTreeObserver;
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
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.medical.product.R;
import com.medical.product.Utils.Utlity;
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
import com.medical.product.models.HorizontalModelCategory;
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
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.medical.product.helpingFile.ReuseMethod.hideStatusbar;

public class Productvendors extends AppCompatActivity {
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
    String catgory = "", brand = "", min = "", max = "",company="";

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
    private static ViewPager mPager;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);
        store = Keystore.getInstance(getApplicationContext());
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        content = (LinearLayout) findViewById(R.id.content);
        laySortFilter = (LinearLayout) findViewById(R.id.laySortFilter);
        noproduct = (LinearLayout) findViewById(R.id.noproduct);
        txtNoDatafound = (TextView) findViewById(R.id.txtNoDatafound);
        main_frame = (FrameLayout) findViewById(R.id.mainframe);
        btnSortBy = (Button) findViewById(R.id.btnSortBy);
        btnFilter = (Button) findViewById(R.id.btnFilter);
        shopnow = (Button) findViewById(R.id.shopnow);
        textNotifycart = (TextView) findViewById(R.id.textNotify);
        nestedscroll = (NestedScrollView) findViewById(R.id.nestedscroll);
        textNotifycart.setText(ReuseMethod.SharedPrefranceGetCartValue(getApplicationContext()));
        hideStatusbar(this);
        setSupportActionBar(toolbar);
        txtCategoryName = (TextView) findViewById(R.id.txtCategoryName);
        Intent intent = getIntent();
        shopnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        nestedscroll.fullScroll(View.FOCUS_UP);


        searchtype = intent.getStringExtra("searching");

        if (searchtype.equals("category")) {
            txtCategoryName.setText(intent.getStringExtra("name"));
            txtNoDatafound.setVisibility(View.GONE);
            getAllHomeData(intent.getStringExtra("id"));
        } else if (searchtype.equals("brand")) {
            txtCategoryName.setText(intent.getStringExtra("name"));
            txtNoDatafound.setVisibility(View.GONE);
            getAllHomeData(intent.getStringExtra("id"));
        }


        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        init();
        isUp = true;

        textView = (TextView) findViewById(R.id.textNotify);
        textView.setText(ReuseMethod.SharedPrefranceGetCartValue(getApplicationContext()));
        txtUserLocation = (TextView) findViewById(R.id.txtUserLocation);

        //  imgBtnCart = (ImageButton) findViewById(R.id.imgBtnCart);
        //  imgBtnSearch= (ImageButton) findViewById(R.id.imgBtnSearch);

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
                setData();
                NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.nestedscroll);
                nestedScrollView.smoothScrollTo(0, 0);
            }
        }, 3000);


        nestedscroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (nestedscroll != null) {
                    if (nestedscroll.getChildAt(0).getBottom() <= (nestedscroll.getHeight() + nestedscroll.getScrollY())) {
                        laySortFilter.setVisibility(View.INVISIBLE);
                    } else {
                        laySortFilter.setVisibility(View.VISIBLE);

                    }
                }
            }
        });

        mBottomSheetDialog = new BottomSheetDialog(this);
        sheetView = this.getLayoutInflater().inflate(R.layout.bottom_sheet_short, null);
        mBottomSheetDialog.setContentView(sheetView);


        laylowtohigh = (LinearLayout) sheetView.findViewById(R.id.laylowtohigh);
        layhightolow = (LinearLayout) sheetView.findViewById(R.id.layhightolow);
        layReverce = (LinearLayout) sheetView.findViewById(R.id.layReverce);
        laylowtohigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                HorizontalCategoryProductAdapter.getFilter("asc");
//
//                HorizontalProductsCatehoryRecycleViewAdapter.getFilter("asc");
//
//                //  adapter. notufy();
//                adapter.notifyDataSetChanged();
//
//                mBottomSheetDialog.dismiss();

            }
        });

        layhightolow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                HorizontalCategoryProductAdapter.getFilter("desc");
//                HorizontalProductsCatehoryRecycleViewAdapter.getFilter("desc");
//
//                adapter.notifyDataSetChanged();
//
//                mBottomSheetDialog.dismiss();
            }
        });
        layReverce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                HorizontalCategoryProductAdapter.getFilter("reverce");
//                HorizontalProductsCatehoryRecycleViewAdapter.getFilter("reverce");
//
//                adapter.notifyDataSetChanged();
//
//                mBottomSheetDialog.dismiss();
            }
        });

        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.i("asdf", "asd bottt" + "bottom sheet dismissed");
                // Do something
            }
        });

        setvaluecart();

    }

    private void init() {
        XMENArray = Utlity.get_banner(Productvendors.this);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new MyAdapter(Productvendors.this, XMENArray));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMENArray.size()) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 5000, 5000);
    }


    private void setData() {

    }

    public void SendAllCategory() {
        ArrayList<HorizontalModelCategory> fileList = new ArrayList<HorizontalModelCategory>();
        Intent intent = new Intent(this, All_CategoryActivity.class);
        intent.putExtra("FILES_TO_SEND", arrayModelSaveCategory);
        startActivity(intent);
        /*

        Intent intent = new Intent(this, All_CategoryActivity.class);
        intent.putExtra("array_list", arrayModelSaveCategory);
        startActivity(intent);*/
    }

    private void getAllHomeData(final String categoryid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.ROOT_HTTP_URL + "product/productwhere",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            //  String strstatus = obj.getString("status");

                            String strstatus = "true";

                            if (strstatus.equals("false")) {
                                txtNoDatafound.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
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
                                                        jsonArray.getJSONObject(j).getString("qty"),
                                                        jsonArray.getJSONObject(j).getString("store_status")

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
                                        }


                                    } catch (JSONException e) {
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
                        txtNoDatafound.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "No_internet Data found", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", categoryid);
                if (searchtype.equals("category")) {
                    params.put("get_type", "category");
                }
                if (searchtype.equals("brand")) {
                    params.put("get_type", "brand");
                }
                if(searchtype.equalsIgnoreCase("company"))
                {
                    params.put("get_type","company");
                }
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
                getAddress();
                break;
            case R.id.btnSortBy:
                mBottomSheetDialog.show();
                break;
            case R.id.btnFilter:
                getApplicationContext().startActivity(new Intent(getApplicationContext(), FilterActivity.class));
                break;


        }
    }

    public void setvaluecart() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String carttot = ReuseMethod.GetTotalCartItem(getApplicationContext());

                Log.i("asdfs", "shared cart item=============" + carttot);

                if (ReuseMethod.GetTotalCartItem(getApplicationContext()).equals("null") || ReuseMethod.GetTotalCartItem(getApplicationContext()).equals("")) {

                } else {
                    textNotifycart.setText(ReuseMethod.GetTotalCartItem(getApplicationContext()));
                }

            }
        }, 1000);

       /* if(ReuseMethod.GetTotalCartItem(getApplicationContext()).equals("null") || ReuseMethod.GetTotalCartItem(getApplicationContext()).equals(""))
        {

        }
        else{
            textNotifycart.setText( ReuseMethod.GetTotalCartItem(getApplicationContext()));
        }*/
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                txtUserLocation.setText(place.getName());
                OnSearchAddressChange(place.getName().toString());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        if (requestCode == 5) {
            if (!TextUtils.isEmpty(data.getStringExtra("catgory")) || !TextUtils.isEmpty(data.getStringExtra("brands"))) {
                catgory = data.getStringExtra("catgory");
                brand = data.getStringExtra("brands");
                company = data.getStringExtra("company");
                min = data.getStringExtra("amountmin");
                max = data.getStringExtra("amountmax");
                searchtype = "";
                getAllHomeData("");
            }

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {

            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


    public void getAddress() {
        filter = new AutocompleteFilter.Builder()
                .setCountry("IN")
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setFilter(filter)
                            .build(this);

            this.startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
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
    protected void onResume() {
        super.onResume();
        setvaluecart();
    }


}
