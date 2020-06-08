package com.medical.product.Ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.medical.product.R;
import com.medical.product.Utils.Utlity;
import com.medical.product.adapter.ItemAdapter;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.models.Child;
import com.medical.product.models.Item;
import com.medical.product.models.SubItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;



public class ProductActivity extends AppCompatActivity {
    private static final String TAG = "ProductActivity";
    private Toolbar toolbar;
    private List<Child> childList;
    private ArrayList<String> item = new ArrayList<>();
    private String name;
    private String image;
    private String testcount;
    private String fasting;
    private String price;
    private TextView tvname;
    private TextView tvtestcount;
    ProgressDialog dialog;
    private TextView tvfasting;
    private Button addTOCart;
    private TextView tvprice;
    private ImageView tvimage;
    private Map<String,List<SubItem>> stringSubItemMap = new HashMap<>();
    public ItemAdapter adapter;
    ProgressDialog progressDialog;
    private String product_id;
    private List<Item> items = new ArrayList<>();
    ImageButton imgBtnNotif,imgBtnCart;
    TextView notification,buynow;
    private TextView textView1;
    TextView strike;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);
        tvname = findViewById(R.id.test_name);
        tvfasting = findViewById(R.id.fasting);
        tvprice = findViewById(R.id.price);
        tvtestcount = findViewById(R.id.no_of_tests);
        tvimage = findViewById(R.id.image);
        textView1=findViewById(R.id.textNotify);
        imgBtnCart=findViewById(R.id.imgBtnCart);
        addTOCart=findViewById(R.id.add_to_cart);
        strike=findViewById(R.id.strike_price);
        buynow =findViewById(R.id.buyNow);
        Bundle bundle = getIntent().getExtras();
        childList = bundle.getParcelableArrayList("child");
        product_id = bundle.getString("productId");
        name = bundle.getString("name");
        image = bundle.getString("image");
        testcount = bundle.getString("test_count");
        fasting = bundle.getString("fasting");
        price = bundle.getString("price");
        tvname.setText(name);

        if (fasting.equals("CF")) {
            fasting = "Yes";
        } else {
            fasting = "No";
        }

        tvfasting.append(fasting);
        tvtestcount.append(testcount + " Tests");
        tvprice.append(price);
        strike.setText(ReuseMethod.discount_offer_strike(Double.parseDouble(price),Double.parseDouble("20")));
        strike.setPaintFlags(strike.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        Glide.with(this)
                .load(image)
                .apply(new RequestOptions().error(R.mipmap.ic_launcher))
                .into(tvimage);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProductActivity.this, "Back clicked", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgBtnNotif = (ImageButton) findViewById(R.id.imgBtnNotif);
        notification = (TextView) findViewById(R.id.notification);
        if (!TextUtils.isEmpty(Utlity.get_notification(this))) {
            notification.setText(Utlity.get_notification(this));
        }
        imgBtnNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyNotificationActivity.class));
            }
        });
        getData();
        addTOCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(image!=null) {
                    addCart(name, price, product_id, testcount, image);
                }else{
                    addCart(name, price, product_id, testcount, "No");
                }
            }
        });
        setvaluecart();
        imgBtnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductActivity.this,LabCartActivity.class);
                startActivity(intent);
            }
        });
        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductActivity.this,LabCartActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        adapter = new ItemAdapter(items);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        adapter.onRestoreInstanceState(savedInstanceState);
    }
    public void getData(){
        rxInit(childList);

    }
    private void rxInit(final List<Child> mChild) {
        Log.d(TAG, "rxInit: created");
        final Observable<Child> childObservable = Observable
                .fromIterable(mChild)
                .filter(new Predicate<Child>() {
                    @Override
                    public boolean test(Child child) throws Exception {
                        return child.getType().equals("TEST");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        childObservable.subscribe(new Observer<Child>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(Child child) {
                Log.d(TAG, "onNext: " + child.getGroupName());
                addProduct(child.getGroupName(),child.getName());


            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                for (Map.Entry<String, List<SubItem>> entry : stringSubItemMap.entrySet()) {
                    String key = entry.getKey();
                    List<SubItem> value = entry.getValue();
                    String x = String.valueOf(value.size());
                    // now work with key and value...
                    items.add(new Item(key+"("+x+")",value));
                }

                initRecyclerView();
            }
        });
    }
    private void addProduct(String item, String subItem){

        List<SubItem> subiteminfo = stringSubItemMap.get(item);
        SubItem subItem1 = new SubItem(subItem);
        if(subiteminfo==null){
            subiteminfo = new ArrayList<>();
        }subiteminfo.add(subItem1);
        stringSubItemMap.put(item,subiteminfo);


    }
    private void addCart(String product_name, String product_price,String product_code,String child,String imageUrl){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait, Adding to cart");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        ReuseMethod.addToLabCartDatabase(this, product_name, product_price, product_code, "20",fasting,
                progressDialog,Long.parseLong(child),imageUrl);
    }
    public  void setvaluecart() {
        String value1 = ReuseMethod.GetTotalLabCartItem(getApplicationContext());
        if (!(value1.equals("null") ||
                value1.equals(""))) {
            textView1.setText(value1);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

}
