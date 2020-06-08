package com.medical.product.Ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.medical.product.Interfaces.OfferServiceApi;
import com.medical.product.R;
import com.medical.product.Utils.Utlity;
import com.medical.product.helpingFile.ApiUtils;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.models.Pincode;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class Availability extends AppCompatActivity {
    private static final String TAG = "Pincode";
    private Toolbar toolbar;
    private ProgressDialog dialog;
    private String stringPincode;
    private EditText pincode;
    private ImageView checkButton;
    private LinearLayout status,booktest;
    private Button btnbooktest;
    private TextView txtstatus,textView1;
    ImageButton imgBtnNotif,imgBtnCart;
    TextView notification;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability);
        toolbar = findViewById(R.id.availability_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pincode=findViewById(R.id.edt_pincode);
        status=findViewById(R.id.llStatus);
        booktest=findViewById(R.id.linbooktest);
        btnbooktest=findViewById(R.id.btnbookTests);
        textView1=findViewById(R.id.textNotify);
        imgBtnCart=findViewById(R.id.imgBtnCart);
        txtstatus=findViewById(R.id.txt_Availabity_status);
        status.setVisibility(View.GONE);
        booktest.setVisibility(View.GONE);
        btnbooktest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        checkButton=findViewById(R.id.btn_checkavailability);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(validity(pincode.getText().toString())){
                   stringPincode = pincode.getText().toString();
                   dialog = new ProgressDialog(Availability.this);
                   dialog.setMessage("Please wait, Fetching response");
                   dialog.setCanceledOnTouchOutside(false);
                   dialog.show();
                   getData();
               }else {
                   pincode.setText("");
                   status.setVisibility(View.VISIBLE);
                   txtstatus.setText("Please Enter Valid Input");
                   txtstatus.setTextColor(Color.RED);
                   Toast.makeText(Availability.this, "Please enter valid pincode", Toast.LENGTH_SHORT).show();
               }
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
        setvaluecart();
        imgBtnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Availability.this,LabCartActivity.class);
                startActivity(intent);
            }
        });
    }
    private void getData(){
        Log.d(TAG, "getData: created");
        OfferServiceApi offerServiceApi = ApiUtils.getOfferService();
        offerServiceApi.getAvailability(stringPincode).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Pincode>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Pincode pincode) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                     if(pincode.getResId().equals("RESTSP_Y") && pincode.getStatus().equals("Y")){
                          status.setVisibility(View.VISIBLE);
                          booktest.setVisibility(View.VISIBLE);
                          txtstatus.setText("Service is available at pincode : " + stringPincode);
                          txtstatus.setTextColor(Color.GREEN);
                     }else{
                         status.setVisibility(View.VISIBLE);
                         booktest.setVisibility(View.GONE);
                         txtstatus.setText("Service is not available at pincode : " + stringPincode);
                         txtstatus.setTextColor(Color.RED);
                     }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        status.setVisibility(View.VISIBLE);
                        booktest.setVisibility(View.GONE);
                        txtstatus.setText("Error please retry after some time");
                        txtstatus.setTextColor(Color.RED);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public Boolean validity(String pincode){
        String validpincode = "^[1-9][0-9]{5}$";
        if(pincode.trim().length()>6){
            return false;
        }else return pincode.trim().matches(validpincode);

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
