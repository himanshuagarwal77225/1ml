package com.medical.product.Ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.product.Interfaces.OfferServiceApi;
import com.medical.product.R;
import com.medical.product.adapter.SlotAdapter;
import com.medical.product.helpingFile.ApiUtils;
import com.medical.product.models.Slot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SlotActivity extends AppCompatActivity {
    String flat,address,state,city,pincode,amount;
    TextView total;
    ProgressDialog dialog;
    SlotAdapter slotAdapter;
    private CompositeDisposable disposable = new CompositeDisposable();
    RecyclerView recyclerView;
    String selectedTime="0";
    String date="0";
    String actualamount,savingamount,report;
    ArrayList<String> benName,benAge,benGender;
    Button confirm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_date__selection);
        confirm = findViewById(R.id.btn_Confirm);
        Bundle bundle = getIntent().getExtras();
        flat=bundle.getString("flat");
        address=bundle.getString("address");
        state=bundle.getString("state");
        city=bundle.getString("city");
        pincode=bundle.getString("pincode");
        amount=bundle.getString("totalamount");
        benName=bundle.getStringArrayList("benName");
        benAge=bundle.getStringArrayList("benAge");
        benGender=bundle.getStringArrayList("benGender");
        actualamount=bundle.getString("actualamount");
        savingamount=bundle.getString("savingamount");
        report=bundle.getString("report");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        total = findViewById(R.id.tv_subTotal1);
        total.setText("Total : "+amount+" Rs/-");
        recyclerView=findViewById(R.id.recycle_AppointDates);
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE,1);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);
        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar Cdate, int position) {
                date=new SimpleDateFormat("yyyy-MM-dd").format(Cdate.getTime());
                getData(date,pincode);
            }
        });
        slotAdapter = new SlotAdapter(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait, Fetching response");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        initRecyclerView();
        date=new SimpleDateFormat("yyyy-MM-dd").format(startDate.getTime());
        getData(date,pincode);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedTime.equals("0") || date.equals("0")){
                    Toast.makeText(SlotActivity.this, "Please select time", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(SlotActivity.this,LabCheckOut.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("flat",flat);
                    bundle1.putString("address",address);
                    bundle1.putString("state",state);
                    bundle1.putString("city",city);
                    bundle1.putString("pincode",pincode);
                    bundle1.putString("time",selectedTime);
                    bundle1.putString("date",date);
                    bundle1.putString("totalamount",amount);
                    bundle1.putStringArrayList("benName",benName);
                    bundle1.putStringArrayList("benAge",benAge);
                    bundle1.putStringArrayList("benGender",benGender);
                    bundle1.putString("actualamount",actualamount);
                    bundle1.putString("savingamount",savingamount);
                    bundle1.putString("report",report);
                    intent.putExtras(bundle1);
                    startActivity(intent);
                }
            }
        });
    }
    private void initRecyclerView(){
        recyclerView.setAdapter(slotAdapter);
        slotAdapter.setValues(new SlotAdapter.OnItemValue() {
            @Override
            public void onItemValue(String time) {
                selectedTime = time;
            }
        });
        recyclerView.setLayoutManager(new
                GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

    }
    private void getData(String date,String pincode){
        OfferServiceApi offerServiceApi = ApiUtils.getOfferService();
        offerServiceApi.getSlotAvailability(pincode,date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Slot>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(Slot slot) {
                        if(slot.getLSlotDataRes().size()!=0){
                        slotAdapter.setOffer(slot.getLSlotDataRes());
                        }else{
                            showSlot("no slot available for "+date + " Please Choose another date");
                        }
                        if (dialog.isShowing()) {
                            dialog.dismiss();

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                        showAlert(e.getMessage().trim());
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onComplete() {

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
    private void showSlot(String msg){
        try {
            new AlertDialog.Builder(this)
                    .setMessage(msg)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .setTitle("No Slot Available")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
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
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }
}
