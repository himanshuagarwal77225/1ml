package com.medical.product.Ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.product.Interfaces.OfferServiceApi;
import com.medical.product.R;
import com.medical.product.Utils.Utlity;
import com.medical.product.adapter.ViewAllAdapter;
import com.medical.product.helpingFile.ApiUtils;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.models.All;
import com.medical.product.usersession.UserSession;

import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.SSLHandshakeException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class ViewAll extends AppCompatActivity {
    private static final String TAG = "ViewAll";
    private ProgressDialog dialog;
    private RecyclerView recyclerView;
    private ViewAllAdapter viewAllAdapter;
    private Toolbar toolbar;
    private String toolname;
    private TextView txtUserLocation;
    ImageButton imgBtnNotif,imgBtnCart;
    TextView notification,textView1;
    private UserSession userSession;
    private String API_KEY;
    private CompositeDisposable disposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: started.");
        setContentView(R.layout.view_all);
        userSession = new UserSession(this);
        HashMap<String, String> hashMap = userSession.getUserApiKey();
        API_KEY = hashMap.get("api_key");
        toolname=getIntent().getStringExtra("toolbar");
        recyclerView=findViewById(R.id.vl_most_booked_rv);
        viewAllAdapter=new ViewAllAdapter(this);
        textView1=findViewById(R.id.textNotify);
        imgBtnCart=findViewById(R.id.imgBtnCart);
        toolbar=findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtUserLocation= findViewById(R.id.txtUserLocation);
        txtUserLocation.setText(toolname);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait, Fetching response");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
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
        initRecyclerView();
        getBestOffer();
        setvaluecart();
        imgBtnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAll.this,LabCartActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        recyclerView.setAdapter(viewAllAdapter);
        recyclerView.setLayoutManager(new
                GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

    }
    private void getBestOffer(){
        Log.d(TAG, "getBestOffer: started.");
        OfferServiceApi offerServiceApi = ApiUtils.getOfferService();
        offerServiceApi.getAll(API_KEY).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<All>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: called");
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(All all) {
                        Log.d(TAG, "onNext: called.");
                        if(all.getRESPONSE().equals("SUCCESS")){
                                viewAllAdapter.setOffer(all.getMASTERS().getPROFILE());
                               if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }else{
                            showAlert("Wrong api key");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "onError: called"+e.toString());

                            showAlert(e.getMessage().trim());
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: called");
                    }
                });
    }
    private void showAlert(String msg){
        try {
            new AlertDialog.Builder(this)
                    .setMessage(msg)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .setTitle("Alert")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        disposable.clear();
    }

    public  void setvaluecart() {
        String value1 = ReuseMethod.GetTotalLabCartItem(getApplicationContext());
        if (!(value1.equals("null") ||
                value1.equals(""))) {
            textView1.setText(value1);
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        setvaluecart();
    }
    // Activity's overrided method used to perform click events on menu items
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }
}
