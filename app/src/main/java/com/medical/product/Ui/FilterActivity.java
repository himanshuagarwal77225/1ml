package com.medical.product.Ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appyvet.materialrangebar.RangeBar;
import com.medical.product.R;
import com.medical.product.adapter.AdapterAddressList;
import com.medical.product.helpingFile.GlobalVariable;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.GatterGetAddressListModel;
import com.medical.product.models.HorizontalModelBrands;
import com.medical.product.models.HorizontalModelCategory;
import com.medical.product.models.HorizontalModelSaveCategory;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Result;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener {
    private static TextView textNotifycart, selctecatgior, selectedbrand, selectedcompnay;
    ImageButton imgBtnEdit;
    private Keystore store;
    CircleImageView CimgivThumb;
    RecyclerView recycleviewaddress;
    Button btnAddNewAddress;
    LinearLayout layCategory, layproduct, laydiscount, laybrand, laycompany;
    public static ArrayList<HorizontalModelSaveCategory> catarray;
    public static ArrayList<HorizontalModelBrands> brandarray;
    ArrayList arrcat;
    String catgorys = "", brands = "", catgorysstr = "", brandsstr = "", minamount = "", maxamount = "", company = "";
    Button apply;
    RangeBar rangebar;
    TextView min, max;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        getSupportActionBar().setTitle("Filter");

        componentInitilization();
    }


    private void componentInitilization() {
        //  btnAddNewAddress=(Button)findViewById(R.id.btnAddNewAddress);
        recycleviewaddress = (RecyclerView) findViewById(R.id.recycleviewaddress);
        layCategory = (LinearLayout) findViewById(R.id.layCategory);
        layproduct = (LinearLayout) findViewById(R.id.layproduct);
        laydiscount = (LinearLayout) findViewById(R.id.laydiscount);
        laybrand = (LinearLayout) findViewById(R.id.laybrand);
        laycompany = (LinearLayout) findViewById(R.id.laycompany);
        selctecatgior = (TextView) findViewById(R.id.selctecatgior);
        selectedbrand = (TextView) findViewById(R.id.selectedbrand);
        selectedcompnay = (TextView) findViewById(R.id.selectedcompnay);
        min = (TextView) findViewById(R.id.min);
        max = (TextView) findViewById(R.id.max);
        apply = (Button) findViewById(R.id.apply);
        catarray = GlobalVariable.arraycategory;
        brandarray = GlobalVariable.brandarray;

        layCategory.setOnClickListener(this);
        layproduct.setOnClickListener(this);
        laydiscount.setOnClickListener(this);
        laybrand.setOnClickListener(this);
        laycompany.setOnClickListener(this);
        apply.setOnClickListener(this);

        rangebar = (RangeBar) findViewById(R.id.rangebar);

        rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                minamount = leftPinValue;
                maxamount = rightPinValue;
                min.setText(getString(R.string.Rs) + " " + minamount);
                max.setText(getString(R.string.Rs) + " " + maxamount);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layCategory:
                Intent intent = new Intent(this, Filetr_selector.class);
                intent.putExtra("get_type", "categories");
                intent.putExtra("selected", catgorys);
                intent.putExtra("resultcode", 2);
                startActivityForResult(intent, 2);
                break;
            case R.id.laybrand:
                Intent laybrand = new Intent(this, Filetr_selector.class);
                laybrand.putExtra("get_type", "brands");
                laybrand.putExtra("selected", brands);
                laybrand.putExtra("resultcode", 3);
                startActivityForResult(laybrand, 3);
                break;
            case R.id.laycompany:
                Intent laycompany = new Intent(this, Filetr_selector.class);
                laycompany.putExtra("get_type", "company");
                laycompany.putExtra("selected", company);
                laycompany.putExtra("resultcode", 4);
                startActivityForResult(laycompany, 4);
                break;
            case R.id.apply:
                if (TextUtils.isEmpty(catgorys)) {
                    catgorys = "";
                }
                if (TextUtils.isEmpty(brands)) {
                    brands = "";
                }
                Intent apply = new Intent();
                apply.putExtra("catgory", catgorys);
                apply.putExtra("brands", brands);
                apply.putExtra("amountmin", minamount);
                apply.putExtra("amountmax", maxamount);
                setResult(5, apply);
                finish();
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent apply = new Intent();
        apply.putExtra("catgory", "");
        apply.putExtra("brands", "");
        apply.putExtra("amountmin", "");
        apply.putExtra("amountmax", "");
        setResult(5, apply);
        finish();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
//                Intent apply = new Intent();
//                apply.putExtra("catgory", "");
//                apply.putExtra("brands", "");
//                apply.putExtra("amountmin", "");
//                apply.putExtra("amountmax", "");
//                apply.putExtra("company", "");
//                setResult(5, apply);
                finish();
                break;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            catgorys = data.getStringExtra("data");
            selctecatgior.setText(data.getStringExtra("datastr"));
        } else if (requestCode == 4) {
            company = data.getStringExtra("data");
            selectedcompnay.setText(data.getStringExtra("datastr"));
        } else {
            brands = data.getStringExtra("data");
            selectedbrand.setText(data.getStringExtra("datastr"));
        }

    }


}
