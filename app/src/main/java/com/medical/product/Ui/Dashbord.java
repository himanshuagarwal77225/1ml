package com.medical.product.Ui;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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
import com.bumptech.glide.Glide;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.medical.product.R;
import com.medical.product.Utils.Utlity;
import com.medical.product.adapter.ExpandableListAdapter;
import com.medical.product.adapter.NoticeAdapter;
import com.medical.product.adapter.VerticalRecycleViewAdapter;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.BaseUtils;
import com.medical.product.helpingFile.ConnectDetector;
import com.medical.product.helpingFile.DemoConfiguration;
import com.medical.product.helpingFile.GPSTracker;
import com.medical.product.helpingFile.GlobalVariable;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.MyAdapter;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.Adf;
import com.medical.product.models.Banner;
import com.medical.product.models.ExpandedMenuModel;
import com.medical.product.models.HorizontalModelBrands;
import com.medical.product.models.HorizontalModelCategory;
import com.medical.product.models.HorizontalModelProducts;
import com.medical.product.models.HorizontalModelSaveCategory;
import com.medical.product.models.Notice;
import com.medical.product.models.VerticalModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.medical.product.helpingFile.ReuseMethod.hideStatusbar;

public class Dashbord extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    LinearLayout content, layMyOrder;
    DrawerLayout drawer;
    private static final String TAG = "Dashbord";
    TextView navopen, txtversioncode;
    NavigationView navigationView;
    ImageView imgCloseNav, thyro;
    LinearLayout laySearch, wallet, healthrecord;
    boolean isUp;
    private static final int REQUEST_CODE = 1234;
    static AutocompleteFilter filter;
    //recycleview
    ShimmerRecyclerView verticleRecycleView;
    VerticalRecycleViewAdapter adapter;
    ArrayList<VerticalModel> arrayListvertical;
    String reson = "";
    TextView textNotify, txtUserLocation;
    private int itemCounter = 0;
    static TextView textView;
    ImageButton imgBtnCart, imgBtnNotif;
    LinearLayout imgBtnSearch;
    Button btnUploadPrescription;
    static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    ExpandableListView expandableList;
    List<ExpandedMenuModel> listDataHeader;
    ExpandableListAdapter mMenuAdapter;
    HashMap<ExpandedMenuModel, List<String>> listDataChild;
    private NoticeAdapter adapterretro;
    private RecyclerView recyclerView;

    CircleImageView circleImage;
    TextView userName, useremail;
    FrameLayout slider;
    boolean doubleBackToExitPressedOnce = false;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    LocationManager locationManager;
    String provider;
    private FirebaseAuth mAuth;
    Geocoder geocoder;
    List<Address> addresses;

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
            Manifest.permission.ACCESS_WIFI_STATE,
    };

    ArrayList<String> myList;

    private ViewPager mPager;
    private int currentPage = 0;
    private static final String[] XMEN = {"dsf", "adfsdf", "adf"};

    private ArrayList<Banner> XMENArray = new ArrayList<>();
    Keystore store;
    ArrayList<HorizontalModelSaveCategory> arrayModelSaveCategory = new ArrayList<>();
    ArrayList<HorizontalModelBrands> arrayListHorizontal = new ArrayList<>();
    ArrayList<HorizontalModelProducts> arrayListHorizontalProducts = new ArrayList<>();
    ArrayList<Adf> arrayListHorizontalad = new ArrayList<>();
    Boolean isinternetpresent;
    ConnectDetector cd;

    SharedPreferences sharedPrfTotalCartItem;
    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    TextView notification;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    GPSTracker gpsTracker;
    LocationManager manager;
    boolean is_place = false;
    LinearLayout hospital, doctor, lab, medican;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbord);
        sharedPrfTotalCartItem = getSharedPreferences("TotalItemInCart", MODE_PRIVATE);
        sharedPrfTotalCartItem.edit().clear().apply();
        gpsTracker = new GPSTracker(this);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        store = Keystore.getInstance(getApplicationContext());
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        content = (LinearLayout) findViewById(R.id.content);
        thyro = findViewById(R.id.thyro);

        hideStatusbar(this);
        setSupportActionBar(toolbar);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        provider = locationManager.getBestProvider(new Criteria(), false);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);

        }

        isUp = true;
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        textView = (TextView) findViewById(R.id.textNotify);
        btnUploadPrescription = (Button) findViewById(R.id.btnUploadPrescription);
        txtUserLocation = (TextView) findViewById(R.id.txtUserLocation);
        imgBtnCart = (ImageButton) findViewById(R.id.imgBtnCart);
        imgBtnSearch = (LinearLayout) findViewById(R.id.imgBtnSearch);
        imgBtnNotif = (ImageButton) findViewById(R.id.imgBtnNotif);
        notification = (TextView) findViewById(R.id.notification);
        expandableList = (ExpandableListView) findViewById(R.id.navigationmenu);
        imgCloseNav = (ImageView) findViewById(R.id.imgCloseNav);
        doctor = (LinearLayout) findViewById(R.id.doctor);
        hospital = (LinearLayout) findViewById(R.id.hospital);
        lab = (LinearLayout) findViewById(R.id.lab);
        doctor.setOnClickListener(this);
        hospital.setOnClickListener(this);
        lab.setOnClickListener(this);

        if (!TextUtils.isEmpty(Utlity.get_notification(this))) {
            notification.setText(Utlity.get_notification(this));
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Utlity.displayLocationSettingsRequest(this);
        } else {
            txtUserLocation.setText(getAddressLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude()));
        }

        setGroupIndicatorToRight();

        drawer.setDrawerElevation(0);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getBackground().setAlpha(122);
        disableNavigationViewScrollbars(navigationView);
        arrayListvertical = new ArrayList<>();
        slider = (FrameLayout) findViewById(R.id.slider);
        circleImage = (CircleImageView) findViewById(R.id.circleImage);
        userName = (TextView) findViewById(R.id.userName);
        useremail = (TextView) findViewById(R.id.useremail);

        View header = navigationView.getHeaderView(0);
        circleImage = (CircleImageView) header.findViewById(R.id.circleImage);
        userName = (TextView) header.findViewById(R.id.userName);
        useremail = (TextView) header.findViewById(R.id.useremail);
        wallet = (LinearLayout) header.findViewById(R.id.layMyoneml);
        layMyOrder = (LinearLayout) header.findViewById(R.id.layMyOrder);
        layMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Order_Tab_Activity.class));
            }
        });
        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashbord.this, Coming_soon.class).putExtra("title", "1ml Cash"));
            }
        });
        healthrecord = header.findViewById(R.id.layMyHealthRecord);
        healthrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginUserDocument.class));
            }
        });
        final DemoConfiguration demoConfiguration = BaseUtils.getDemoConfiguration(0, this);
        setTheme(demoConfiguration.getStyleResource());
        /* setContentView(demoConfiguration.getLayoutResource());*/
        setTitle(demoConfiguration.getTitleResource());

        verticleRecycleView = (ShimmerRecyclerView) findViewById(R.id.recycleview);
        if (!Utlity.is_online(Dashbord.this)) {
            startActivity(new Intent(Dashbord.this, No_internet.class));
        } else {
            if (demoConfiguration.getItemDecoration() != null) {
                verticleRecycleView.addItemDecoration(demoConfiguration.getItemDecoration());
            }
            verticleRecycleView.setHasFixedSize(true);
            verticleRecycleView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            adapter = new VerticalRecycleViewAdapter(this, arrayListvertical);
            verticleRecycleView.setAdapter(adapter);
            verticleRecycleView.showShimmerAdapter();
            verticleRecycleView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getAllHomeData();
                    NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.nestedscroll);
                    nestedScrollView.smoothScrollTo(0, 0);
                }
            }, 3000);
        }
        // favourite
        prepareListData();

        mMenuAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild, expandableList);
        expandableList.setAdapter(mMenuAdapter);
        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                onBackPressed();
                if (i == 0 && i1 == 0) {
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                } else if (i == 0 && i1 == 1) {
                    startActivity(new Intent(getApplicationContext(), ReferAndEarnActivity.class));
                } else if (i == 0 && i1 == 2) {
                    startActivity(new Intent(getApplicationContext(), Order_Tab_Activity.class));
                } else if (i == 0 && i1 == 3) {
                    startActivity(new Intent(getApplicationContext(), Precption_requested_order.class));
                } else if (i == 0 && i1 == 4) {
                    startActivity(new Intent(getApplicationContext(), AddFamilyMamberActivity.class));
                } else if (i == 0 && i1 == 5) {
                    startActivity(new Intent(getApplicationContext(), MyWishList.class).putExtra("type", "0"));
                } else if (i == 1 && i1 == 0) {
                    cd = new ConnectDetector(getApplicationContext());
                    isinternetpresent = cd.isConnectToInternet();
                    if (isinternetpresent) {

                        startActivity(new Intent(getApplicationContext(), Nearest_vendor.class).putExtra("type", "1"));
                    } else {
                        Toast.makeText(getApplicationContext(), "Internet Not Present", Toast.LENGTH_SHORT).show();
                    }
                } else if (i == 1 && i1 == 1) {
                    cd = new ConnectDetector(getApplicationContext());
                    isinternetpresent = cd.isConnectToInternet();
                    if (isinternetpresent) {

                        startActivity(new Intent(getApplicationContext(), BloodBankListFragment.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Internet Not Present", Toast.LENGTH_SHORT).show();
                    }
                } else if (i == 2 && i1 == 0) {
                    startActivity(new Intent(getApplicationContext(), SearchProductActivity.class));
                } else if (i == 2 && i1 == 2) {
                    startActivity(new Intent(getApplicationContext(), UploadPrescriptionActivity.class).putExtra("type", 0));
                } else if (i == 2 && i1 == 3) {
                    startActivity(new Intent(getApplicationContext(), PillReminderActivity.class));
                } else if (i == 3 && i1 == 4) {
                    callDocsWebView();
                } else if (i == 4 && i1 == 0) {
                    startActivity(new Intent(getApplicationContext(), LabTestMainActivity.class));
                } else if (i == 4 && i1 == 2) {
                    startActivity(new Intent(getApplicationContext(), UploadDocumentActivity.class));
                } else if (i == 5 && i1 == 0) {
                    onBackPressed();
                    Needhelp();
                } /*else if (i == 5 && i1 == 1) {
                    startActivity(new Intent(getApplicationContext(), Feedbacks.class));
                }else if (i == 5 && i1 == 2) {
                    startActivity(new Intent(getApplicationContext(), Nearest_vendor.class).putExtra("type", "2"));
                }*/ else if (i == 5 && i1 == 7) {
                    startActivity(new Intent(getApplicationContext(), MyNotificationActivity.class));
                } else if (i == 5 && i1 == 3/*9*/) {
                    onBackPressed();
                    startActivity(new Intent(getApplicationContext(), Artical.class));
                } else if (i == 5 && i1 == 11) {
                    Intent intent = new Intent(getApplicationContext(), Accreditation.class);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(Dashbord.this, Coming_soon.class).putExtra("title", listDataHeader.get(i).getHeading1myaccount().get(i1)));
                }
                return false;
            }
        });
        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                //Log.d("DEBUG", "heading clicked");
                if (i == 6) {
                    finish();
                    ReuseMethod.SharedPrefranceGetCartValueClear(getApplicationContext());
                    store.clear();
                    Utlity.clear_reminder(Dashbord.this);
                    Utlity.clear_address(Dashbord.this);
                    if (currentUser != null) {
                        mAuth.signOut();
                        sendToAuth();
                    } else {
                        sendToAuth();
                    }


                }

                return false;
            }
        });
        if (!store.get("name").equals("")) {
            userName.setText(store.get("name"));
        }
        if (!store.get("email").equals("")) {
            useremail.setText(store.get("email"));

        }
        if (!(store.get("image").equals("") || store.get("name").equals(null))) {
            Glide.with(this).load(ApiFileuri.ROOT_URL_USER_IMAGE + store.get("image")).into(circleImage);
        }
        setvaluecart();
        thyro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashbord.this, LabTestMainActivity.class));
            }
        });

    }


    private void setGroupIndicatorToRight() {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                50, r.getDisplayMetrics());
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            expandableList.setIndicatorBounds(width - px, width);
        } else {
            expandableList.setIndicatorBoundsRelative(width - px, width);
        }
    }

    // Convert pixel to dip
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 250.5f);
    }


    /**
     * Method to generate List of notice using RecyclerView with custom adapter
     */
    private void generateNoticeList(ArrayList<Notice> noticeArrayList) {
        recyclerView = findViewById(R.id.recycler_view_notice_list);
        adapterretro = new NoticeAdapter(noticeArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Dashbord.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterretro);
    }

    public void SendAllCategory() {
        Intent intent = new Intent(this, All_CategoryActivity.class);
        intent.putExtra("FILES_TO_SEND", arrayModelSaveCategory);
        intent.putExtra("slug", "catgory");
        startActivity(intent);
    }

    public void SendAllbrands() {
        Intent intent = new Intent(this, All_CategoryActivity.class);
        intent.putExtra("FILES_TO_SEND", new Gson().toJson(arrayListHorizontal));
        intent.putExtra("slug", "brands");
        startActivity(intent);
    }

    public void SendAllproduct() {
        Intent intent = new Intent(this, All_CategoryActivity.class);
        intent.putExtra("FILES_TO_SEND", new Gson().toJson(arrayListHorizontalProducts));
        intent.putExtra("slug", "products");
        startActivity(intent);
    }

    public void SendAllad() {
        Intent intent = new Intent(this, All_CategoryActivity.class);
        intent.putExtra("FILES_TO_SEND", new Gson().toJson(arrayListHorizontalad));
        intent.putExtra("slug", "ad");
        startActivity(intent);
    }


    private void getAllHomeData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.ROOT_HTTP_URL + "home",
                response -> {

                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(response);
                        String strstatus = obj.getString("status");
                        if (strstatus.equals("false")) {
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } else {
                            ReuseMethod.SetTotalCartItem(getApplicationContext(), obj.getString("cart-total"));
                            setvaluecart();
                            Iterator<String> iter = obj.keys();

                            while (iter.hasNext()) {
                                String key = iter.next();
                                try {
                                    JSONArray jsonArray = new JSONArray(obj.getJSONArray(key).toString());
                                    VerticalModel mVerticalModel = new VerticalModel();
                                    mVerticalModel.setTitle(key);

                                    if (key.equals("brands")) {
                                        arrayListHorizontal = new ArrayList<>();
                                        for (int j = 0; j < jsonArray.length(); j++) {
                                            HorizontalModelBrands mHorizontalModel = new HorizontalModelBrands();
                                            mHorizontalModel.setId(jsonArray.getJSONObject(j).getString("id"));
                                            mHorizontalModel.setName(jsonArray.getJSONObject(j).getString("name"));
                                            mHorizontalModel.setSlug(jsonArray.getJSONObject(j).getString("slug"));
                                            mHorizontalModel.setImage(jsonArray.getJSONObject(j).getString("image"));
                                            arrayListHorizontal.add(mHorizontalModel);
                                        }
                                        mVerticalModel.setArrayList(arrayListHorizontal);
                                        arrayListvertical.add(mVerticalModel);
                                    } else if (key.equals("categories")) {
                                        ArrayList<HorizontalModelCategory> arrayListHorizontal = new ArrayList<>();
                                        for (int j = 0; j < jsonArray.length(); j++) {
                                            HorizontalModelCategory mHorizontalModel = new HorizontalModelCategory();
                                            mHorizontalModel.setId(jsonArray.getJSONObject(j).getString("id"));
                                            mHorizontalModel.setName(jsonArray.getJSONObject(j).getString("name"));
                                            mHorizontalModel.setSlug(jsonArray.getJSONObject(j).getString("slug"));
                                            mHorizontalModel.setImage(jsonArray.getJSONObject(j).getString("image"));
                                            arrayListHorizontal.add(mHorizontalModel);
                                            //for save list in array
                                            HorizontalModelSaveCategory mHorizontalModelSaveCategory = new HorizontalModelSaveCategory();
                                            mHorizontalModelSaveCategory.setId(jsonArray.getJSONObject(j).getString("id"));
                                            mHorizontalModelSaveCategory.setName(jsonArray.getJSONObject(j).getString("name"));
                                            mHorizontalModelSaveCategory.setSlug(jsonArray.getJSONObject(j).getString("slug"));
                                            mHorizontalModelSaveCategory.setImage(jsonArray.getJSONObject(j).getString("image"));
                                            arrayModelSaveCategory.add(mHorizontalModelSaveCategory);
                                        }
                                        mVerticalModel.setArrayListCategory(arrayListHorizontal);
                                        arrayListvertical.add(mVerticalModel);
                                    } else if (key.equals("products")) {
                                        arrayListHorizontalProducts = new ArrayList<>();
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
                                    } else if (key.equals("sponsor")) {
                                        arrayListHorizontalad = new ArrayList<>();
                                        for (int j = 0; j < jsonArray.length(); j++) {
                                            Adf ad = new Adf(
                                                    jsonArray.getJSONObject(j).getString("id"),
                                                    jsonArray.getJSONObject(j).getString("image"),
                                                    jsonArray.getJSONObject(j).getString("name")
                                            );
                                            arrayListHorizontalad.add(ad);
                                        }
                                        mVerticalModel.setArrayListad(arrayListHorizontalad);
                                        if (jsonArray.length() != 0) {
                                            arrayListvertical.add(mVerticalModel);
                                        }

                                    } else if (key.equals("slider")) {
                                        set_slider(jsonArray);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            adapter.notifyDataSetChanged();
                            verticleRecycleView.hideShimmerAdapter();
                            GlobalVariable.arraycategory = arrayModelSaveCategory;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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
                params.put("macid", ReuseMethod.MacAddress(getApplicationContext()));
                params.put("lat", String.valueOf(gpsTracker.getLatitude()));
                params.put("lng", String.valueOf(gpsTracker.getLongitude()));
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
                startActivity(new Intent(getApplicationContext(), SearchProductActivity.class));
                break;
            case R.id.txtUserLocation:
                getAddress();
                break;
            case R.id.layMyoneml:

                break;
            case R.id.layMyHealthRecord:

                break;
            case R.id.imgCloseNav:
                onBackPressed();
                break;
            case R.id.imgBtnNotif:
                startActivity(new Intent(getApplicationContext(), MyNotificationActivity.class));
                break;
            case R.id.btnUploadPrescription:
                startActivity(new Intent(getApplicationContext(), UploadPrescriptionActivity.class).putExtra("type", 0));
                //startActivity(new Intent(getApplicationContext(),Accepts_orders.class).putExtra("id","1ML1556010412PTN4"));
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                is_place = true;
                Place place = PlaceAutocomplete.getPlace(this, data);
                if (place != null) {
                    txtUserLocation.setText(place.getAddress());
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<ExpandedMenuModel>();

        listDataChild = new HashMap<ExpandedMenuModel, List<String>>();

        List<String> heading1myaccount = new ArrayList<String>();
        heading1myaccount.add(getString(R.string.menu_myprofile));
        heading1myaccount.add(getString(R.string.menu_referandearn));
        heading1myaccount.add("My Orders");
        heading1myaccount.add("My Prescription");
        heading1myaccount.add("Family Member");
        heading1myaccount.add(getString(R.string.whishlist));

        ExpandedMenuModel item1myaccount = new ExpandedMenuModel();
        item1myaccount.setIconName(getString(R.string.menu_main_myaccount));
        item1myaccount.setIconImg(R.drawable.ic_managepayment);
        item1myaccount.setHeading1myaccount(heading1myaccount);
        listDataHeader.add(item1myaccount);


        List<String> heading2services = new ArrayList<String>();
        heading2services.add(getString(R.string.menu_nearstorelocator));
        heading2services.add(getString(R.string.menu_blood));
        //heading2services.add(getString(R.string.menu_ambulance));
        heading2services.add(getString(R.string.menu_doctor));
        heading2services.add(getString(R.string.menu_lab));
        // heading2services.add(getString(R.string.menu_imagecenter));


        ExpandedMenuModel item2service = new ExpandedMenuModel();
        item2service.setIconName(getString(R.string.menu_main_sercices));
        item2service.setIconImg(R.drawable.ic_nearambulance);
        item2service.setHeading1myaccount(heading2services);
        listDataHeader.add(item2service);

        List<String> heading3medicine = new ArrayList<String>();
        heading3medicine.add(getString(R.string.menu_findmedicine));
        //heading3medicine.add(getString(R.string.menu_substitutes));
        heading3medicine.add(getString(R.string.menu_prescription));
        //heading3medicine.add(getString(R.string.menu_pillreminder));

        ExpandedMenuModel item3medicines = new ExpandedMenuModel();
        item3medicines.setIconName(getString(R.string.menu_main_medicines));
        item3medicines.setIconImg(R.drawable.ic_findmedicines);
        item3medicines.setHeading1myaccount(heading3medicine);
        listDataHeader.add(item3medicines);


        List<String> heading4doctirs = new ArrayList<String>();
        heading4doctirs.add(getString(R.string.menu_finddoctor));
        heading4doctirs.add(getString(R.string.menu_appointment));
        //heading4doctirs.add(getString(R.string.menu_doctorchat));
        //heading4doctirs.add(getString(R.string.menu_consultclinic));
        //heading4doctirs.add(getString(R.string.menu_textconsult));
        //heading4doctirs.add(getString(R.string.menu_phoneconsult));
        //heading4doctirs.add(getString(R.string.menu_videoconsult));
        //heading4doctirs.add(getString(R.string.menu_emargencyconsult));


        ExpandedMenuModel item4doctor = new ExpandedMenuModel();
        item4doctor.setIconName(getString(R.string.menu_main_doctor));
        item4doctor.setIconImg(R.drawable.ic_finddoctor);
        item4doctor.setHeading1myaccount(heading4doctirs);
        listDataHeader.add(item4doctor);

        List<String> heading5labtest = new ArrayList<String>();
        heading5labtest.add(getString(R.string.menu_booktabtest));
        //heading5labtest.add(getString(R.string.menu_bookimaging));

        ExpandedMenuModel item5labtest = new ExpandedMenuModel();
        item5labtest.setIconName(getString(R.string.menu_main_tabtest));
        item5labtest.setIconImg(R.drawable.ic_nearlab);
        item5labtest.setHeading1myaccount(heading5labtest);
        listDataHeader.add(item5labtest);

        List<String> heading6healthfile = new ArrayList<String>();
        heading6healthfile.add(getString(R.string.menu_treatments));
        heading6healthfile.add(getString(R.string.menu_prescriptions));
        heading6healthfile.add(getString(R.string.menu_Documents));

//        ExpandedMenuModel item6healthfile = new ExpandedMenuModel();
//        item6healthfile.setIconName(getString(R.string.menu_main_healthfiles));
//        item6healthfile.setIconImg(R.drawable.ic_folder);
//        item6healthfile.setHeading1myaccount(heading6healthfile);
//        listDataHeader.add(item6healthfile);


        List<String> heading7other = new ArrayList<String>();
        heading7other.add(getString(R.string.menu_needhelp));//0
        //heading7other.add(getString(R.string.menu_feedback));
        //heading7other.add(getString(R.string.menu_storeoffers));
        heading7other.add(getString(R.string.menu_requestproduct));//1
        //heading7other.add(getString(R.string.menu_reportissue));
        //heading7other.add(getString(R.string.menu_changelocator));
        heading7other.add(getString(R.string.menu_mycoupons));//2
        //heading7other.add(getString(R.string.menu_Notifications));
        //heading7other.add(getString(R.string.menu_howitworks));
        heading7other.add(getString(R.string.menu_articles));//3
        heading7other.add(getString(R.string.menu_aboutus));//4
        //heading7other.add("Accreditation");
        heading7other.add(getString(R.string.menu_privacypolicy));//5
        heading7other.add(getString(R.string.menu_returncancellation));//6
        //heading7other.add(getString(R.string.menu_setting));
        ExpandedMenuModel item7others = new ExpandedMenuModel();
        item7others.setIconName(getString(R.string.menu_main_others));
        item7others.setIconImg(R.drawable.ic_other);
        item7others.setHeading1myaccount(heading7other);
        listDataHeader.add(item7others);

        ExpandedMenuModel item8signout = new ExpandedMenuModel();
        item8signout.setIconName(getString(R.string.menu_signout));
        item8signout.setIconImg(R.drawable.ic_logout);
        listDataHeader.add(item8signout);


        // Header, Child data
        listDataChild.put(listDataHeader.get(0), heading1myaccount);
        listDataChild.put(listDataHeader.get(1), heading2services);
        listDataChild.put(listDataHeader.get(2), heading3medicine);
        listDataChild.put(listDataHeader.get(3), heading4doctirs);
        listDataChild.put(listDataHeader.get(4), heading5labtest);
//        listDataChild.put(listDataHeader.get(5), heading6healthfile);
        listDataChild.put(listDataHeader.get(5), heading7other);

    }


    private void getApplicationVersion() {

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            txtversioncode.setText(String.valueOf(version));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    private void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.dashbord, menu);
        //  getMenuInflater().inflate(R.menu.dashbord, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContact();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        gattingLocation();
                    } else {
                        if (!hasPermissions(this, PERMISSIONS)) {
                            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);

                        }
                    }

                }
                return;
            }

        }
    }

    private void SetUserLocation(final String addressLine, final String lati, final String longi, final String latlong) {
        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.ROOT_HTTP_URL + "user/edituser",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", store.get("id"));
                params.put("address", addressLine);
                params.put("lati", lati);
                params.put("longi", longi);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Dashbord.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                    return false;
                }
            }
        }
        return true;
    }


    private void gattingLocation() {
        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                // makeUseOfNewLocation(location);
                location.getLatitude();
                location.getLongitude();
                locationManager.removeUpdates(this);
                txtUserLocation.setText(getAddressLocation(location.getLatitude(), location.getLongitude()));
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    public String getAddressLocation(double latitude, double longitude) {
        geocoder = new Geocoder(this, Locale.getDefault());
        String address = "", lat = "", logt = "";
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            for (int i = 0; i < addresses.size(); i++) {
                address = addresses.get(i).getAddressLine(i); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(i).getLocality();
                String state = addresses.get(i).getAdminArea();
                String country = addresses.get(i).getCountryName();
                String postalCode = addresses.get(i).getPostalCode();
                String knownName = addresses.get(i).getFeatureName();
                lat = Double.toString(latitude);
                logt = Double.toString(longitude);
            }
            SetUserLocation(address, lat, logt, lat + logt);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setvaluecart();

        if (!store.get("name").equals("")) {
            userName.setText(store.get("name"));
        }
        if (!store.get("email").equals("")) {
            useremail.setText(store.get("email"));

        }
        if (!(store.get("image").equals("") || store.get("name").equals(null))) {
            Glide.with(this).load(ApiFileuri.ROOT_URL_USER_IMAGE + store.get("image")).into(circleImage);
        }

        notification.setText(Utlity.get_notification(this));
    }


    public void setvaluecart() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String carttot = ReuseMethod.GetTotalCartItem(getApplicationContext());
                if (ReuseMethod.GetTotalCartItem(getApplicationContext()).equals("null") || ReuseMethod.GetTotalCartItem(getApplicationContext()).equals("")) {
                } else {
                    textView.setText(ReuseMethod.GetTotalCartItem(getApplicationContext()));
                }

            }
        }, 1000);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lab:

                startActivity(new Intent(this, LabTestMainActivity.class).putExtra("title", "Lab Tests"));
                break;
            case R.id.doctor:
                callDocsWebView();
                break;
            case R.id.hospital:
                unavailable();
//                startActivity(new Intent(this, Coming_soon.class).putExtra("title", "Near Hospital"));
                break;
        }
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

    private void set_slider(JSONArray array) {
        if (array.length() != 0) {
            XMENArray.clear();

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


        } else {
            slider.setVisibility(View.GONE);
        }

        MyAdapter adapter = new MyAdapter(Dashbord.this, XMENArray);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(adapter);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
    }

    private void sendToAuth() {

        startActivity(new Intent(getApplicationContext(), LoginPanelActivity.class));
        finish();
    }

    public void unavailable() {
        Toast.makeText(this, "Not Available right now!!", Toast.LENGTH_SHORT).show();
    }

    public void callDocsWebView() {
        Intent intent = new Intent(Dashbord.this, DocsWebview.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", store.get("name"));
        bundle.putString("email", store.get("email"));
        bundle.putString("phone", store.get("phone"));
        bundle.putString("id", store.get("id"));
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
