package com.medical.product.Ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.medical.product.R;
import com.medical.product.Utils.Utlity;
import com.medical.product.adapter.AdapterCartProduct;
import com.medical.product.adapter.AdapterLabCartProduct;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.GatterGetCartList;
import com.medical.product.models.GatterGetLabCartList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LabCartActivity extends AppCompatActivity {
    ImageButton imgBtnSearch;
    RecyclerView recyclecartitem;
    Keystore store;
    AdapterLabCartProduct adapterLabCartProduct;
    TextView txtaddmore,finalprice,savingprice,amountprice;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    private CollectionReference dbr;
    String no_item="0";
    RelativeLayout totalamount,totalsaving,relpricing;
    LinearLayout layEmptyCart, layCart, ch;
    Button proceed,btnNoTests;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labcart);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtaddmore=findViewById(R.id.txt_add_moretest);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        amountprice=findViewById(R.id.txtActPrice);
        btnNoTests=findViewById(R.id.btnNoTests);
        savingprice=findViewById(R.id.totalSavingamount);
        proceed=findViewById(R.id.btnConfirm);

        recyclecartitem=findViewById(R.id.recyclerview);
        finalprice=findViewById(R.id.txtFinalPrice);
        totalamount=findViewById(R.id.totalamount);
        totalsaving=findViewById(R.id.totalSaving);
        relpricing=findViewById(R.id.relPricing);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LabCartActivity.this,Select_beneficiary.class);
                Bundle bundle = new Bundle();
                bundle.putString("totalamount", finalprice.getText().toString().substring(3));
                bundle.putString("actualamount",amountprice.getText().toString().substring(3));
                bundle.putString("savingamount",savingprice.getText().toString().substring(3));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        // toolbar.setTitle("Cart");
        layEmptyCart=findViewById(R.id.linNoResult);
        layEmptyCart.setVisibility(View.VISIBLE);
        layCart=findViewById(R.id.linCart);
        layCart.setVisibility(View.GONE);
        store = Keystore.getInstance(getApplicationContext());
        no_item=ReuseMethod.GetTotalLabCartItem(this);
        if(!no_item.equals("0")){
            layCart.setVisibility(View.VISIBLE);
            layEmptyCart.setVisibility(View.GONE);
            totalamount.setVisibility(View.VISIBLE);
            relpricing.setVisibility(View.VISIBLE);
            totalsaving.setVisibility(View.VISIBLE);
        }else{
            layCart.setVisibility(View.GONE);
            totalamount.setVisibility(View.GONE);
            relpricing.setVisibility(View.GONE);
            totalsaving.setVisibility(View.GONE);
            layEmptyCart.setVisibility(View.VISIBLE);
        }
        btnNoTests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LabCartActivity.this,SearchActivity1.class);
                startActivity(intent);
            }
        });
        dbr=db.collection("+91"+store.get("phone")).document("Cart").collection("1mlLab");
        setuprecyclerview();
        txtaddmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LabCartActivity.this,SearchActivity1.class);
                startActivity(intent);
            }
        });


    }

//    public void CalculateGrandTotal() {
//
//        Double grandprice = 0.0;
//        Double grandnet = 0.0;
//
//        for (int i = 0; i < gatterGetCartLists.size(); i++) {
//            Double newpricecheck = Double.parseDouble(gatterGetCartLists.get(i).getSale_price());
//            Double price = newpricecheck;
//            grandprice = grandprice + price;
//
//        }
//
//        txtGrandTotal.setText(getString(R.string.Rs) + String.valueOf(grandprice));
//
//        if (gatterGetCartLists.size() == 0) {
//            layEmptyCart.setVisibility(View.VISIBLE);
//            layCart.setVisibility(View.GONE);
//            ch.setVisibility(View.GONE);
//        } else {
//            layCart.setVisibility(View.VISIBLE);
//            layEmptyCart.setVisibility(View.GONE);
//            ch.setVisibility(View.VISIBLE);
//        }
//
//
//    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }
private void setuprecyclerview(){
    Query query = dbr.orderBy("date", Query.Direction.DESCENDING);
    FirestoreRecyclerOptions<GatterGetLabCartList> options = new FirestoreRecyclerOptions.Builder<GatterGetLabCartList>()
            .setQuery(query,GatterGetLabCartList.class).build();
    adapterLabCartProduct = new AdapterLabCartProduct(options, this,"+91"+store.get("phone"));
    recyclecartitem.setLayoutManager(new LinearLayoutManager(this));
    recyclecartitem.setAdapter(adapterLabCartProduct);
    adapterLabCartProduct.setOnItemClickListener(new AdapterLabCartProduct.OnItemClickListener() {
        @Override
        public void onItemClick(DocumentSnapshot documentSnapshot, int position, String product) {
            Toast.makeText(LabCartActivity.this, product+" has been removed", Toast.LENGTH_SHORT).show();
            ReuseMethod.SetTotalLabCartItem(getApplicationContext(), String.valueOf(Integer.parseInt(ReuseMethod.GetTotalLabCartItem(getApplicationContext()))-1));
            no_item=ReuseMethod.GetTotalLabCartItem(getApplicationContext());
            if(!no_item.equals("0")){
                layCart.setVisibility(View.VISIBLE);
                layEmptyCart.setVisibility(View.GONE);
                totalamount.setVisibility(View.VISIBLE);
                relpricing.setVisibility(View.VISIBLE);
                totalsaving.setVisibility(View.VISIBLE);
            }else{
                layCart.setVisibility(View.GONE);
                totalamount.setVisibility(View.GONE);
                relpricing.setVisibility(View.GONE);
                totalsaving.setVisibility(View.GONE);
                layEmptyCart.setVisibility(View.VISIBLE);
            }
        }
    });

}
    @Override
    protected void onResume() {
        super.onResume();
        adapterLabCartProduct.startListening();
        no_item=ReuseMethod.GetTotalLabCartItem(this);
        if(!no_item.equals("0")){
            layCart.setVisibility(View.VISIBLE);
            layEmptyCart.setVisibility(View.GONE);
            totalamount.setVisibility(View.VISIBLE);
            relpricing.setVisibility(View.VISIBLE);
            totalsaving.setVisibility(View.VISIBLE);
        }else{
            layCart.setVisibility(View.GONE);
            totalamount.setVisibility(View.GONE);
            relpricing.setVisibility(View.GONE);
            totalsaving.setVisibility(View.GONE);
            layEmptyCart.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onStart(){
        super.onStart();
        adapterLabCartProduct.startListening();
    }
    @Override
    protected void onStop(){
        super.onStop();
        adapterLabCartProduct.stopListening();
    }
    @Override
    protected void onPause(){
        super.onPause();
        adapterLabCartProduct.stopListening();
    }

}
