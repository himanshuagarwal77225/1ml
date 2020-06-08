package com.medical.product.Ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.medical.product.Interfaces.OfferServiceApi;
import com.medical.product.R;
import com.medical.product.helpingFile.ApiUtils;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.models.EmailRequest;
import com.medical.product.models.EmailResponse;
import com.medical.product.models.FixAppointmentModelRequest;
import com.medical.product.models.FixAppointmentModelResponse;
import com.medical.product.models.PostOrderRequestModel;
import com.medical.product.models.PostOrderResponseModel;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LabCheckOut extends AppCompatActivity implements PaymentResultWithDataListener {
    private static final String TAG = "LabCheckOut";
    Date currentTime;
    TextView productName,reporte,fasting,benificiary,addresse,phone,email,subtotal,discount,total,appointments,hardcopy,benCount;
    Button online,later;
    String flat,address,state,city,pincode,amount,fast;
    String appointment;
    String actualamount,savingamount,report;
    ArrayList<String> benName,benAge,benGender;
    FirebaseFirestore db;
    String API_KEY = "oKtp1d0l0SMZ9FtdQmwP0agrI39tKJATf0unh8fpQNYcKLWvXyFzEw==";
    StringBuffer s=new StringBuffer();
    StringBuffer k= new StringBuffer();
    StringBuffer l = new StringBuffer();
    Keystore store;
    String appt;
    Boolean plan_choosen = false;
    String hardcopywant;
    final static String refCode="9999626489";
    private CompositeDisposable disposable = new CompositeDisposable();
    ProgressDialog dialog;
    String orderId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_activity);
        Checkout.preload(getApplicationContext());
        db = FirebaseFirestore.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialog=new ProgressDialog(LabCheckOut.this);
        dialog.setMessage("Please Wait, Making summary");
        dialog.setCancelable(false);
        dialog.show();

        benCount=findViewById(R.id.txtBen);
        productName = findViewById(R.id.finalproductsname);
        reporte = findViewById(R.id.txt_hc_charge);
        fasting = findViewById(R.id.txt_is_fasting);
        appointments=findViewById(R.id.txtDateTime1);
        benificiary = findViewById(R.id.finalBenName);
        addresse = findViewById(R.id.tvAddress);
        phone = findViewById(R.id.tvmob_no);
        email = findViewById(R.id.tv_email_id);
        subtotal = findViewById(R.id.txtActPrice);
        discount = findViewById(R.id.txtDiscount);
        total = findViewById(R.id.txtTotalAmount);
        hardcopy=findViewById(R.id.txtHardCopyCharge);
        online=findViewById(R.id.btnOnline);
        later=findViewById(R.id.btnLater);
        store=Keystore.getInstance(getApplicationContext());
        getData();
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
        currentTime = Calendar.getInstance().getTime();
        actualamount=bundle.getString("actualamount");
        savingamount=bundle.getString("savingamount");
        appointment=bundle.getString("time")+" "+bundle.getString("date");
        appointments.setText(appointment);
        report=bundle.getString("report");
        benCount.setText(Integer.toString(benName.size()));
        if(report.equals("Yes")){
            hardcopywant = "Y";
            hardcopy.setText("Rs. 75");
        }else{
            hardcopywant = "N";
            hardcopy.setText("Rs. 0");
        }
        reporte.setText(report);
        fasting.setText("NO");
        for(int i=0;i<benName.size();i++){
            int t=i+1;
            l.append(t+". "+benName.get(i)+"\n").trimToSize();

        }
        orderId = "1mlLab"+getAlphaNumericString(6);
        benificiary.setText(l.toString().trim());
        addresse.setText(flat+","+address+","+city+","+state+","+pincode);
        phone.setText(store.get("phone"));
        email.setText(store.get("email"));
        subtotal.setText("Rs. "+actualamount);
        discount.setText("Rs. "+savingamount);
        total.setText("Rs. "+amount);
        appt =bundle.getString("date")+" "+averagetime(bundle.getString("time"));
        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: called");
                dialog=new ProgressDialog(LabCheckOut.this);
                dialog.setMessage("Please Wait, Making summary");
                dialog.setCancelable(false);
                dialog.show();
                Log.d(TAG, "onClick: orderId "+orderId);

                fixAppointmentSlot(new FixAppointmentModelRequest(API_KEY,orderId,pincode,appt));
            }
        });

        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: online");
                startPayment();

            }
        });

    }

    private void startPayment() {
Checkout checkout = new Checkout();
checkout.setImage(R.drawable.logo);
        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", "1ML HealthCare Solutions");

            /**
             * Description can be anything
             * eg: Order #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             */

            options.put("description", "Thankyou for choosing us!!");
            options.put("currency", "INR");

            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
            options.put("amount", Float.parseFloat(amount)*100);
            JSONObject preFill = new JSONObject();
            preFill.put("email", email.getText().toString());
            preFill.put("contact", phone.getText().toString());

            options.put("prefill", preFill);

            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    public void fixAppointmentSlot(FixAppointmentModelRequest fixAppointmentModelRequest){
        Log.d(TAG, "fixAppointmentSlot: called");
        OfferServiceApi offerServiceApi= ApiUtils.postFixAppointment();
        offerServiceApi.fixAppointment(fixAppointmentModelRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FixAppointmentModelResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(FixAppointmentModelResponse fixAppointmentModelResponse) {
                        if(fixAppointmentModelResponse.getRESPONSE().equals("SUCCESS")){
                            Log.d(TAG, "onNext: Success");
                            getbendetails(benName,benAge,benGender);

                        }else{
                            if(dialog.isShowing()){
                                dialog.dismiss();
                            }
                            showAlert("something went wrong please try again");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showAlert(e.getMessage().trim());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void getData(){
        db.collection("+91"+store.get("phone")).document("Cart").collection("1mlLab")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    int i=1;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        s.append(i+". "+document.get("product")+"\n").trimToSize();
                        k.append(document.get("product")).append(",");
                        fast=document.get("fasting").toString();
                        Log.d(TAG, "onComplete: plan choosen before loop: "+plan_choosen);
                        if(!plan_choosen) {
                            Log.d(TAG, "onComplete: plan choosen before: "+plan_choosen);
                            plan_choosen = document.get("plan_status").equals("yes");
                            Log.d(TAG, "onComplete: plan choosen after: "+plan_choosen);
                            if(plan_choosen) {
                                online.setVisibility(View.VISIBLE);
                                later.setVisibility(View.GONE);
                            }else{
                                online.setVisibility(View.VISIBLE);
                                later.setVisibility(View.VISIBLE);

                            }
                        }else{
                            online.setVisibility(View.VISIBLE);
                            later.setVisibility(View.VISIBLE);
                        }

                        i++;
                    }
                    productName.setText(s.toString().trim());
                    fasting.setText(fast);
                    if(dialog.isShowing()){
                        dialog.dismiss();
                    }
                }else{
                    Toast.makeText(LabCheckOut.this, "something went wrong please try again later", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
    static String getAlphaNumericString(int n)
    {

        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
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

    private String averagetime(String time){
        Log.d(TAG, "averagetime: called");
        String[] split = time.split("-");
        long sum = 0L;

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sfk = new SimpleDateFormat("hh:mm:ss a");
        for (int i = 0; i < split.length; i++)
        {
            try {
                sum += sdf.parse(split[i].trim()).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
                showAlert(e.getMessage().trim());
            }
        }
        Date avgDate = new Date((sum/split.length));
        Log.d(TAG, "averagetime:"+sfk.format(avgDate));
        return  sfk.format(avgDate);
    }
    private void postorderData(PostOrderRequestModel postOrderRequestModel,ProgressDialog progressDialog){
        Log.d(TAG, "postorderData: "+postOrderRequestModel.toString());
        OfferServiceApi offerServiceApi = ApiUtils.postPostOrderData();
        offerServiceApi.postorderata(postOrderRequestModel).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PostOrderResponseModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(PostOrderResponseModel postOrderResponseModel) {
                        Log.d(TAG, "onNext: respone : "+postOrderResponseModel.getRESPONSE());
                        if(postOrderResponseModel.getRESPONSE().equals("SUCCESS")){
                            ReuseMethod.addOrder(getApplicationContext(),flat,address,state,city,pincode,orderId,email.getText().toString(),phone.getText().toString()
                                    ,amount,s.toString(),l.toString(),hardcopy.getText().toString(),fasting.getText().toString()
                                    ,"pay later",appointment,currentTime.toString());
                            String content = getResources().getString(R.string.content,store.get("name"),orderId,flat+" "+address+" "+state+" "+city+" "+pincode,
                                    s.toString().trim(),l.toString().trim(),email.getText().toString(),
                                    fasting.getText().toString(),
                                    phone.getText().toString(),
                                    appointment
                            ,hardcopy.getText().toString()
                            ,currentTime.toString()
                            ,"Rs " +amount);
                            postEmail(new EmailRequest("manojz1@1ml.co.in",email.getText().toString(),"1mlpharmacy@gmail.com"
                                    ,getResources().getString(R.string.subject),content),orderId);

                        }else{
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            Toast.makeText(LabCheckOut.this, "Some error has occured, please try again later", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        showAlert(e.getMessage().trim());
                    }

                    @Override
                    public void onComplete() {


                    }
                });





    }
    public void getbendetails(ArrayList<String> benName,ArrayList<String> benAge,ArrayList<String> benGender){
        int n = benName.size();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<NewDataSet>");
        for (int i=0;i<n;i++){
            String gender = benGender.get(i);
            if(gender.equals("Male")){
                gender ="M";
            }else{
                gender = "F";
            }
            stringBuffer.append("<Ben_details><Name>"+benName.get(i)+"</Name>");
            stringBuffer.append("<Age>"+benAge.get(i)+"</Age>");
            stringBuffer.append("<Gender>"+gender+"</Gender></Ben_details>");
        }
        stringBuffer.append("</NewDataSet>");
        Log.d(TAG, "getbendetails: "+stringBuffer.toString());
        PostOrderRequestModel postOrderRequestModel= new PostOrderRequestModel(API_KEY,orderId,flat+"//,"+address+"//,"+city+"//,"+state,pincode
                ,k.deleteCharAt(k.length()-1).toString(),store.get("phone"),store.get("email"),"H",store.get("name"),
                amount,"0",appt,hardcopywant,refCode,"postpaid",String.valueOf(benName.size()),stringBuffer.toString());
        postOrderRequestModel.toString();
        postorderData(postOrderRequestModel,dialog);


    }
    public void postEmail(EmailRequest emailRequest,String OrderId){
        OfferServiceApi offerServiceApi = ApiUtils.postEmail();
        offerServiceApi.postEmail(emailRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EmailResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(EmailResponse emailResponse) {
                        Log.d(TAG, "onNext: called emailresponse");
                        ReuseMethod.updateEmailSend(emailResponse.getStatus().getDescription(),OrderId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                        showAlert(e.getMessage().trim());
                    }

                    @Override
                    public void onComplete() {
                        Intent intent = new Intent(LabCheckOut.this, LabTestMainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Checkout.clearUserData(this);
        return true;
    }



    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Toast.makeText(this, "Your Payment is successful", Toast.LENGTH_SHORT).show();
        dialog=new ProgressDialog(LabCheckOut.this);
        dialog.setMessage("Please Wait, Making summary");
        dialog.setCancelable(false);
        dialog.show();
        fixAppointmentSlot(new FixAppointmentModelRequest(API_KEY,orderId,pincode,appt));
        Log.d(TAG, "onPaymentSuccess: string "+s);
        Log.d(TAG, "onPaymentSuccess: : \n+"+paymentData.toString());


    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Toast.makeText(this, "Your Payment Failed", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onPaymentError: i = "+i+"\n");
        Log.d(TAG, "onPaymentError: string "+s);
        Log.d(TAG, "onPaymentError: : \n+"+paymentData.toString());

    }
}
