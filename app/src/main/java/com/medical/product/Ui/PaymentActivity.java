package com.medical.product.Ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medical.product.R;
import com.medical.product.Utils.FilePath;
import com.medical.product.Utils.Utlity;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.GatterGetOrderProductListModel;
import com.medical.product.models.GatterGetPrescriptionImage;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.medical.product.helpingFile.ReuseMethod.hideStatusbar;

public class PaymentActivity extends AppCompatActivity {
    public static final String TAG = "PaymentActivity : ";
    private Keystore store;
    Context context = PaymentActivity.this;
    Button btnChooseAddNewAddress, btnProced;
    String addid = "";
    TextView txttotalPrice, txtGrandtotal, addtype, txtdeliverycharge, txtProductpricetitle, txtPayable, discount;
    private RadioGroup radioGrouppaymode;
    private RadioButton radioButtonmode, radCard, radCashOnDelivery;
    ScrollView scrollview;
    private boolean isDisableExitConfirmation = false;
    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private SharedPreferences userDetailsPreference;
    private AppPreference mAppPreference;
    String strpayprice = "";
    String strdeliCharge = "";
    String stritem = "";
    String strphone = "";
    String strusername = "";
    String stremail = "";

    String strsalt = "";
    String merchantkey = "YPAsyRXF";
    String merchantid = "";
    String discountstr = "";
    String allproduct = "";
    String shipping = "";
    String discount_code = "";
    String payment_type = "";
    String subtotal = "";
    String offer_id = "";
    ArrayList<GatterGetPrescriptionImage> gatterprescimg=new ArrayList<>();
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        hideStatusbar(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        store = Keystore.getInstance(getApplicationContext());
        setTitle("Payment");
        mAppPreference = new AppPreference();
        settings = getSharedPreferences("settings", MODE_PRIVATE);
        txttotalPrice = (TextView) findViewById(R.id.txttotalPrice);
        txtGrandtotal = (TextView) findViewById(R.id.txtGrandtotal);
        discount = (TextView) findViewById(R.id.discountCoupon);
        txtdeliverycharge = (TextView) findViewById(R.id.txtdeliverycharge);
        addtype = (TextView) findViewById(R.id.addtype);
        txtPayable = (TextView) findViewById(R.id.txtPayable);
        txtProductpricetitle = (TextView) findViewById(R.id.txtProductpricetitle);
        radioGrouppaymode = (RadioGroup) findViewById(R.id.radioGrouppaymode);
        btnProced = (Button) findViewById(R.id.btnProced);
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        btnChooseAddNewAddress = (Button) findViewById(R.id.btnChooseAddNewAddress);

        radCashOnDelivery = (RadioButton) findViewById(R.id.radCashOnDelivery);
        radCard = (RadioButton) findViewById(R.id.radCard);

        Intent intent = getIntent();
        if (intent != null) {
            strpayprice = intent.getStringExtra("net");
            stritem = intent.getStringExtra("totalitem");
            strphone = intent.getStringExtra("phone");
            strusername = intent.getStringExtra("username");
            stremail = intent.getStringExtra("email");
            discountstr = intent.getStringExtra("discount");
            allproduct = intent.getStringExtra("product");
            shipping = intent.getStringExtra("shipping");
            discount_code = intent.getStringExtra("discount_code");
            subtotal = intent.getStringExtra("ptotal");
            offer_id = intent.getStringExtra("offer_id");
            gatterprescimg=  gatterprescimg=new Gson().fromJson(intent.getStringExtra("img"), new TypeToken<List<GatterGetPrescriptionImage>>() {
            }.getType());
        }

        txtProductpricetitle.setText("Product Price (" + stritem + "items)");
        txttotalPrice.setText(getString(R.string.Rs) + subtotal);
        txtGrandtotal.setText(getString(R.string.Rs) + strpayprice);
        txtPayable.setText(getString(R.string.Rs) + strpayprice);
        discount.setText("-"+getString(R.string.Rs) + discountstr);
        txtdeliverycharge.setText("+"+getString(R.string.Rs) + shipping);

        getCredentials();

        addid = intent.getStringExtra("addid");
        radioGrouppaymode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (radCard.isChecked()) {
                    btnProced.setText("Continue");
                    payment_type = "payu";
                } else if (radCashOnDelivery.isChecked()) {
                    btnProced.setText("Place Order");
                    payment_type = "cod";
                }
            }
        });


    }

    private void getUserData() {
        store = Keystore.getInstance(getApplicationContext());
    }

    public void clickFunction(View view) {
        switch (view.getId()) {
            case R.id.btnProced:
                int selectedId = radioGrouppaymode.getCheckedRadioButtonId();
                radioButtonmode = (RadioButton) findViewById(selectedId);
                if (selectedId != -1 && radioButtonmode.isChecked()) {
                    String strmode = "";
                    strmode = radioButtonmode.getText().toString();
                    if (strmode.equals("Cash on Delivery")) {
                        if (addid.equals("")) {
                            Toast.makeText(getApplicationContext(), "Please select address", Toast.LENGTH_SHORT).show();
                        } else if (addid.equals("")) {
                            Toast.makeText(getApplicationContext(), "You are not login", Toast.LENGTH_SHORT).show();
                        } else {
                            uploadBitmap(gatterprescimg);
                        }
                    } else if (strmode.equals("Credit / Debit / ATM Card")) {
                        launchPayUMoneyFlow();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please select Payment mode", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void launchPayUMoneyFlow() {

        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();

        //Use this to set your custom text on result screen button
        payUmoneyConfig.setDoneButtonText("this is status");

        //Use this to set your custom title for the activity
        payUmoneyConfig.setPayUmoneyActivityTitle("this is title");

        payUmoneyConfig.disableExitConfirmation(isDisableExitConfirmation);

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();
        double amount = 0;
        try {
            amount = Double.parseDouble(txtGrandtotal.getText().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        String txnId = System.currentTimeMillis() + "";
        //String txnId = "TXNID720431525261327973";
        String phone = strphone;
        String productName = "production detail";
        String firstName = strusername;
        String email = stremail;
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";


        builder.setAmount(String.valueOf(amount))
                .setTxnId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")
                .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(true)
                .setKey(merchantkey)
                .setMerchantId(merchantid);

        try {
            mPaymentParams = builder.build();
            mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams);

            if (AppPreference.selectedTheme != -1) {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, PaymentActivity.this, AppPreference.selectedTheme, mAppPreference.isOverrideResultScreen());
            } else {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, PaymentActivity.this, R.style.AppTheme_default, mAppPreference.isOverrideResultScreen());
            }

        } catch (Exception e) {
            // some exception occurred
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            //payNowButton.setEnabled(true);
        }
    }

    private PayUmoneySdkInitializer.PaymentParam calculateServerSideHashAndInitiatePayment1(final PayUmoneySdkInitializer.PaymentParam paymentParam) {

        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> params = paymentParam.getParams();
        stringBuilder.append(params.get(PayUmoneyConstants.KEY) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.TXNID) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.AMOUNT) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.PRODUCT_INFO) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.FIRSTNAME) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.EMAIL) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF1) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF2) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF3) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF4) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF5) + "||||||");
        stringBuilder.append(strsalt);

        String hash = hashCal(stringBuilder.toString());
        paymentParam.setMerchantHash(hash);

        return paymentParam;
    }

    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    //Success Transaction
                } else {
                    //Failure Transaction
                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();

                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage("Payu's Data : " + payuResponse + "\n\n\n Merchant's Data: " + merchantResponse)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }).show();

            } else if (resultModel != null && resultModel.getError() != null) {

            } else {
                Log.d(TAG, "Both objects are null!");
            }
        }
    }

    private void getCredentials() {
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.GET, ApiFileuri.ROOT_HTTP_URL + "merchant", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "Both objects are null!---" + response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                } else if (error instanceof ClientError) {
                } else if (error instanceof ServerError) {
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                }
            }
        });
        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //adding the string request to request queue
        requestQueue.add(jsonObjRequest);
    }

    private void uploadBitmap(final ArrayList<GatterGetPrescriptionImage> gatterprescimg) {
        final Dialog dialog = Utlity.show_progress(this);
        dialog.show();
        String other="";
        MultipartBody.Builder builder = new MultipartBody.Builder();
        if (gatterprescimg != null && gatterprescimg.size() > 0) {
            for (int i = 0; i < gatterprescimg.size(); i++) {
                if (!gatterprescimg.get(i).getType().equals("uriimg")) {
                    File file = new File(FilePath.getPath(this, Uri.parse(gatterprescimg.get(i).getImage())));
                    try {
                        File now_file = new Compressor(this).compressToFile(file);
                        builder.addFormDataPart("files", now_file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), now_file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (TextUtils.isEmpty(other)) {
                        other = gatterprescimg.get(i).getImage();
                    } else {
                        other = other + "," + gatterprescimg.get(i).getImage();
                    }

                }
            }
        }
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("user_id", store.get("id"));
        builder.addFormDataPart("prescription_image", other);
        builder.addFormDataPart("address_id", addid);
        builder.addFormDataPart("counts", String.valueOf(gatterprescimg.size()));
        builder.addFormDataPart("products", allproduct);
        builder.addFormDataPart("payment_type", payment_type);
        builder.addFormDataPart("shipping_cahrage", shipping);
        builder.addFormDataPart("discount_id", offer_id);
        if (!TextUtils.isEmpty(discount_code)) {
            builder.addFormDataPart("discount_code", discount_code);
            builder.addFormDataPart("discount", discountstr);
        } else {
            builder.addFormDataPart("discount_code", "");
            builder.addFormDataPart("discount", "");
        }
        builder.addFormDataPart("grand_total", strpayprice);
        MultipartBody body = builder.build();
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(ApiFileuri.Checkout)
                .post(body)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.retryOnConnectionFailure();
        okHttpClient.connectTimeoutMillis();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Utlity.dismiss_dilog(PaymentActivity.this, dialog);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, final @NonNull okhttp3.Response response) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (response.isSuccessful()) {
                            Utlity.dismiss_dilog(PaymentActivity.this, dialog);
                            try {
                                String data = response.body().string();
                                JSONObject obj = new JSONObject(data);
                                if (obj.getBoolean("status")) {
                                    startActivity(new Intent(PaymentActivity.this, Thankyouorder.class).putExtra("id", obj.getString("orderid")));
                                    finishAffinity();
                                } else {
                                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }

        });
    }

}
