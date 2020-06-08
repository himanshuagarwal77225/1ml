package com.medical.product.helpingFile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.medical.product.Interfaces.OfferServiceApi;
import com.medical.product.R;
import com.medical.product.Ui.CartActivity;
import com.medical.product.Ui.LabCartActivity;
import com.medical.product.Ui.LabTestMainActivity;
import com.medical.product.Ui.ProductActivity;
import com.medical.product.Ui.SearchProductActivity;
import com.medical.product.models.Duplicate;
import com.medical.product.models.EmailRequest;
import com.medical.product.models.EmailResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;


public class ReuseMethod {
    public static Ringtone ringtoneAlarm;
    public static Keystore store;
    public static String retstring = "";
    static SharedPreferences sharedPreferences;
    static SharedPreferences sharedPrfCartArray;
    static SharedPreferences sharedPrfLabCartArray;
    static SharedPreferences sharedPrfTotalCartItem;
    static Map<String, Object> user;
    static FirebaseFirestore db;
    static Boolean duplicate_found=false;
    static Boolean isDuplicate=true;
    static long childs;
    static String API_KEY = "oKtp1d0l0SMZ9FtdQmwP0agrI39tKJATf0unh8fpQNYcKLWvXyFzEw==";
    static DocumentReference documentReference;
    static SharedPreferences sharedPrfTotalLabCartItem;
    private static CompositeDisposable disposable = new CompositeDisposable();
    ProgressDialog progressDialog;

    public static void hideStatusbar(Activity activity) {
        //  activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    private static final String TAG = "ReuseMethod";
    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new MySpannable(false) {
                @Override
                public void onClick(View widget) {
                    if (viewMore && tv.getText().length() > 300) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "See Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, ".. See More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    public static void SharedPrefranceCart(Context context, String valuecount) {
        sharedPreferences = context.getSharedPreferences("AddToCart", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("numofvalue", valuecount);
        editor.commit();

    }

    public static void SharedPrefranceLabCart(Context context, String valuecount) {
        sharedPreferences = context.getSharedPreferences("AddToLabCart", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("numofvalue", valuecount);
        editor.commit();

    }

    public static String SharedPrefranceCartArray(Context context, String valuecount) {
        sharedPrfCartArray = context.getSharedPreferences("AddToCartArray", MODE_PRIVATE);

        String CartArray = sharedPrfCartArray.getString("CartArray", "");

        String[] temp = CartArray.split("#");

        String addornot = "";
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals(valuecount)) {
                addornot = "yes";
            }
        }


        return addornot;
    }

    public static String SharedPrefranceLabCartArray(Context context, String valuecount) {
        sharedPrfLabCartArray = context.getSharedPreferences("AddToLabCartArray", MODE_PRIVATE);

        String CartArray = sharedPrfLabCartArray.getString("LabCartArray", "");

        String[] temp = CartArray.split("#");

        String addornot = "";
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals(valuecount)) {
                addornot = "yes";
            }
        }


        return addornot;
    }


    public static void SetTotalCartItem(Context context, String value) {

        sharedPrfTotalCartItem = context.getSharedPreferences("TotalItemInCart", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrfTotalCartItem.edit();
        editor.putString("totalcartvalue", value);
        editor.apply();
    }

    public static void SetTotalLabCartItem(Context context, String value) {

        sharedPrfTotalLabCartItem = context.getSharedPreferences("TotalItemInLabCart", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrfTotalLabCartItem.edit();
        editor.putString("totalLabcartvalue", value);
        editor.apply();
    }

    public static String GetTotalCartItem(Context context) {
        sharedPrfTotalCartItem = context.getSharedPreferences("TotalItemInCart", MODE_PRIVATE);
        String CartTotal = sharedPrfTotalCartItem.getString("totalcartvalue", "");
        return CartTotal;
    }

    public static String GetTotalLabCartItem(Context context) {
        store = Keystore.getInstance(context);

        sharedPrfTotalLabCartItem = context.getSharedPreferences("TotalItemInLabCart", MODE_PRIVATE);
        String CartTotal = sharedPrfTotalLabCartItem.getString("totalLabcartvalue", "");
        database(context, store.get("phone"));
        return CartTotal;
    }

    public static String SharedPrefranceGetCartValue(Context context) {
        sharedPreferences = context.getSharedPreferences("AddToCart", MODE_PRIVATE);

        String value = sharedPreferences.getString("numofvalue", "");

        if (value.equals("") || value.equals(null)) {
            ReuseMethod.SharedPrefranceCart(context, "0");
            value = sharedPreferences.getString("numofvalue", "");
        }
        return value;
    }

    public static String SharedPrefranceGetLabCartValue(Context context) {
        sharedPreferences = context.getSharedPreferences("AddToLabCart", MODE_PRIVATE);

        String value = sharedPreferences.getString("numofvalue", "");

        if (value.equals("") || value.equals(null)) {
            ReuseMethod.SharedPrefranceLabCart(context, "0");
            value = sharedPreferences.getString("numofvalue", "");
        }
        return value;
    }

    public static void SharedPrefranceGetCartValueClear(Context context) {
        sharedPreferences = context.getSharedPreferences("AddToCart", MODE_PRIVATE);

        sharedPreferences.edit().clear().commit();

        sharedPrfCartArray = context.getSharedPreferences("AddToCartArray", MODE_PRIVATE);

        sharedPrfCartArray.edit().clear().commit();

    }

    public static void SharedPrefranceGetLabCartValueClear(Context context) {
        sharedPreferences = context.getSharedPreferences("AddToLabCart", MODE_PRIVATE);

        sharedPreferences.edit().clear().commit();

        sharedPrfLabCartArray = context.getSharedPreferences("AddToLabCartArray", MODE_PRIVATE);

        sharedPrfLabCartArray.edit().clear().commit();

    }

    public static void OpenCartActivity(Activity context) {
        context.startActivity(new Intent(context, CartActivity.class));
    }

    public static void OpenLabCartActivity(Activity context) {
        context.startActivity(new Intent(context, LabCartActivity.class));
    }

    public static void OpenSearchActivity(Activity context) {
        context.startActivity(new Intent(context, SearchProductActivity.class));
    }

    public static void setRoundedDrawable(Context context, View view, int backgroundColor, int borderColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(0);
        shape.setColor(backgroundColor);
        shape.setStroke((int) 2, borderColor);
        view.setBackgroundDrawable(shape);
    }

    public static void AlarmRingtoon(Context context) {
        Uri alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtoneAlarm = RingtoneManager.getRingtone(context, alarmTone);
        ringtoneAlarm.play();
    }

    public static void addOrder(final Context context,
                                final String flat,
                                final String address,
                                final String state,
                                final String city,
                                final String pincode, final String orderId,
                                final String email, final String phone,
                                final String amount, final String product,
                                final String ben, final String hardcopy,
                                final String fast, final String paymode,
                                final String appointment, final String bookingDate) {
        user = new HashMap<>();
        user.clear();
        user.put("orderId", orderId);
        user.put("flat", flat);
        user.put("address", address);
        user.put("state", state);
        user.put("city", city);
        user.put("pincode", pincode);
        user.put("email", email);
        user.put("phone", phone);
        user.put("amount", amount);
        user.put("products", product);
        user.put("benNames", ben);
        user.put("hardcopy", hardcopy);
        user.put("fasting", fast);
        user.put("paymode", paymode);
        user.put("appointment", appointment);
        user.put("bookingDate", bookingDate);
        db = FirebaseFirestore.getInstance();
        db.collection("+91" + store.get("phone"))
                .document("Checkout")
                .collection("1mlLab")
                .document(orderId)
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Order has been placed", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public static void addAddress(final Dialog dialog, final Context context, final ProgressDialog progressDialog, final String flat, final String address, final String state, final String city, final String pincode) {
        db = FirebaseFirestore.getInstance();
        db.collection("+91" + store.get("phone"))
                .document("Address")
                .collection("1mlLab")
                .document(pincode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (!documentSnapshot.exists()) {
                        user = new HashMap<>();
                        user.clear();
                        user.put("flat", flat);
                        user.put("address", address);
                        user.put("state", state);
                        user.put("city", city);
                        user.put("pincode", pincode);
                        db.collection("+91" + store.get("phone"))
                                .document("Address")
                                .collection("1mlLab")
                                .document(pincode)
                                .set(user)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context, "Address has been added", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        dialog.dismiss();
                                    }
                                });
                    } else {
                        Toast.makeText(context, "Address already exists with same Pincode number", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        dialog.dismiss();
                    }
                } else {
                    Toast.makeText(context, "Some error has occured, please try again later", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    progressDialog.dismiss();
                    dialog.dismiss();
                }
            }
        });


    }


    public static void addBeneficiary(final Dialog dialog, final Context context, final ProgressDialog progressDialog, final String firstName, final String lastName, final String gender, final String age) {
        db = FirebaseFirestore.getInstance();
        db.collection("+91" + store.get("phone"))
                .document("Beneficiary")
                .collection("1mlLab")
                .document(firstName + " " + lastName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (!documentSnapshot.exists()) {
                        user = new HashMap<>();
                        user.clear();
                        user.put("firstName", firstName);
                        user.put("lastName", lastName);
                        user.put("gender", gender);
                        user.put("age", age);
                        db.collection("+91" + store.get("phone"))
                                .document("Beneficiary")
                                .collection("1mlLab").document(firstName + " " + lastName)
                                .set(user)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context, "Beneficiary has been added", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        dialog.dismiss();
                                    }
                                });
                    } else {
                        Toast.makeText(context, "Beneficiary already exists with same name", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        dialog.dismiss();
                    }
                } else {
                    Toast.makeText(context, "Some error has occured, please try agani later", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    progressDialog.dismiss();
                    dialog.dismiss();
                }
            }
        });


    }
    public static void updateEmailSend(String emailSend,String orderId){
        deletecart();
        db = FirebaseFirestore.getInstance();
        db.collection("+91" + store.get("phone"))
                .document("Checkout")
                .collection("1mlLab")
                .document(orderId)
                .update("emailSent",emailSend);
    }


    public static void updateLabCartPlan(String multiple,String time,String additional_discount,final Context context,final String plan_status,final String planvalue, final ProgressDialog progressDialog,final String product){
        if(!progressDialog.isShowing()){
            progressDialog.show();
        }
        db = FirebaseFirestore.getInstance();
        db.collection("+91" + store.get("phone"))
                .document("Cart").collection("1mlLab")
                .document(product).update("plan_status",plan_status,"plan_value",planvalue,
                "additional_discount",additional_discount,"multiple",multiple).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    user = new HashMap<>();
                    user.clear();
                    user.put("plan_status", plan_status);
                    user.put("plan_value", planvalue);
                    user.put("product", product);
                    user.put("booking_time",time);
                    db = FirebaseFirestore.getInstance();
                    db.collection("+91" + store.get("phone"))
                            .document("ReOrder").collection("1mlLab")
                            .document(product).set(user);
                    Toast.makeText(context, "Plan Updated", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(context, "Some error has occured. Please try again later", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void addToLabCartDatabase(final Context context, final String product, final String price, final String pid
            , final String discount, final String mFasting, final ProgressDialog progressDialog,long child,String imageUrl) {

        duplicate_found =false;
        db = FirebaseFirestore.getInstance();
        db.collection("+91" + store.get("phone"))
                .document("Cart").collection("1mlLab").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    Log.d(TAG, "onComplete: successfully data retrieved first time");
                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                    String fasting = queryDocumentSnapshots.getDocuments().get(0).getString("fasting");
                    Log.d(TAG, "onComplete: fasting : "+fasting);
                    int j = queryDocumentSnapshots.getDocuments().size();
                    OfferServiceApi offerServiceApi = ApiUtils.getOfferService();
                    if(mFasting.equals(fasting)){
                        Log.d(TAG, "onComplete: successfully fasting checked");
                        for (int i = 0; i < j; i++) {
                            int c =i+1;
                            Log.d(TAG, "onComplete: value of duplicate : "+duplicate_found);
                            if (!duplicate_found) {
                                Log.d(TAG, "onComplete: value of not found duplicate if loop");

                                childs = (long) queryDocumentSnapshots.getDocuments().get(i).get("child");
                                String name1 = queryDocumentSnapshots.getDocuments().get(i).getString("product");
                                Log.d(TAG, "onComplete: no of child & childs : "+child+","+childs);

                                offerServiceApi.getDuplicateTests(API_KEY, name1 + "," + product)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Observer<Duplicate>() {
                                            @Override
                                            public void onSubscribe(Disposable d) {
                                                disposable.add(d);
                                            }

                                            @Override
                                            public void onNext(Duplicate duplicate) {
                                                if (duplicate.getREMARKS().equals("DUPLICATE TEST NOT FOUND")) {
                                                    Log.d(TAG, "onComplete: DUPLICATE test not found");
                                                    duplicate_found=false;
                                                    if(c==j) {
                                                        isDuplicate = false;
                                                        duplicatenotfound(context,product,price,pid,discount,mFasting,child,progressDialog,imageUrl);
                                                        Log.d(TAG, "onNext: c = : "  + c + "j = : "+j);
                                                        Log.d(TAG, "onNext: isDuplicate"+isDuplicate);
                                                    }
                                                } else {
                                                    duplicate_found = true;
                                                    Log.d(TAG, "onComplete: DUPLICATE test found");
                                                    if (child > childs) {

                                                        db.collection("+91" + store.get("phone"))
                                                                .document("Cart")
                                                                .collection("1mlLab").document(name1).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){

                                                                    user = new HashMap<>();
                                                                    user.clear();
                                                                    user.put("product", product);
                                                                    user.put("price", price);
                                                                    user.put("fasting", mFasting);
                                                                    user.put("pid", pid);
                                                                    user.put("discount", discount);
                                                                    user.put("date", DateFormat.getDateTimeInstance().format(new Date()));
                                                                    user.put("plan_status", "no");
                                                                    user.put("plan_value", "no");
                                                                    user.put("additional_discount", "00");
                                                                    user.put("multiple", "1");
                                                                    user.put("child", child);
                                                                    user.put("imageUrl",imageUrl);
                                                                    db.collection("+91" + store.get("phone"))
                                                                            .document("Cart")
                                                                            .collection("1mlLab").document(product)
                                                                            .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(progressDialog.isShowing()){
                                                                                progressDialog.dismiss();
                                                                            }
                                                                            Toast.makeText(context, "item upgraded", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });

                                                                }else{
                                                                    if(progressDialog.isShowing()){
                                                                        progressDialog.dismiss();
                                                                    }
                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        if(progressDialog.isShowing()){
                                                            progressDialog.dismiss();
                                                        }
                                                        Toast.makeText(context, "Duplicate item already present", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                if(progressDialog.isShowing()){
                                                    progressDialog.dismiss();
                                                }
                                                showAlert(e.getMessage(),context);
                                            }

                                            @Override
                                            public void onComplete() {

                                            }
                                        });

                            }
                        }
                    }else{
                        if(mFasting.equals("Yes")){
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }Log.d(TAG, "onComplete: fasting"+mFasting);
                            Toast.makeText(context, "Please add non fasting profiles", Toast.LENGTH_SHORT).show();
                        }else{if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }   Log.d(TAG, "onComplete: fasting"+mFasting);
                            Toast.makeText(context, "Please add fasting profiles", Toast.LENGTH_SHORT).show();
                        }

                    }


                    //for loop
                } else {
                    Log.d(TAG, "onComplete: first item");
                    db.collection("+91" + store.get("phone"))
                            .document("Cart").collection("1mlLab")
                            .document(product).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (!documentSnapshot.exists()) {
                                    user = new HashMap<>();
                                    user.clear();
                                    user.put("product", product);
                                    user.put("price", price);
                                    user.put("pid", pid);
                                    user.put("discount", discount);
                                    user.put("fasting", mFasting);
                                    user.put("plan_status","no");
                                    user.put("plan_value","no");
                                    user.put("additional_discount","00");
                                    user.put("multiple", "1");
                                    user.put("child",child);
                                    user.put("imageUrl",imageUrl);
                                    user.put("date", DateFormat.getDateTimeInstance().format(new Date()));
                                    db.collection("+91" + store.get("phone"))
                                            .document("Cart")
                                            .collection("1mlLab").document(product)
                                            .set(user)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    db.collection("+91" + store.get("phone"))
                                                            .document("Cart")
                                                            .collection("1mlLab").orderBy("child", Query.Direction.DESCENDING);
                                                    if (context instanceof ProductActivity) {
                                                        TextView textView = ((Activity) context).findViewById(R.id.textNotify);
                                                        textView.setText(Integer.toString(Integer.parseInt(textView.getText().toString()) + 1));
                                                    }
                                                    progressDialog.dismiss();
                                                    Toast.makeText(context, "Item has been added", Toast.LENGTH_SHORT).show();
                                                    SetTotalLabCartItem(context, String.valueOf(Integer.parseInt(GetTotalLabCartItem(context)) + 1));
                                                }
                                            });
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(context, "Item is already added", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Some error has occured, please try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }

    public static String addToCardDatabase(final Context context, final String pid, final String quentity) {
        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.ROOT_HTTP_URL + "product/addcart",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        retstring = response;
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(retstring);
                            String strstatus = obj.getString("status");

                            if (strstatus.equals("false")) {
                                Toast.makeText(context, obj.getString("product"), Toast.LENGTH_SHORT).show();
                            } else {
                                ReuseMethod.SetTotalCartItem(context, obj.getString("cart-total"));
                                Toast.makeText(context, obj.getString("product").toString(), Toast.LENGTH_SHORT).show();
                                String carttotal = obj.getString("cart-total");
                                SetTotalCartItem(context, carttotal);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError)
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                store = Keystore.getInstance(context);
                params.put("user_id", store.get("id"));
                params.put("productid", pid);
                params.put("quentity", quentity);
           /* params.put("email", stremail);
            params.put("phone", strphone);*/
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);

        return retstring;
    }


    public static String addToCardDatabaseProductDetails(final Context context, final String pid, String quentity, final String addoffer) {
        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.ROOT_HTTP_URL + "product/addcart",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        retstring = response;
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(retstring);
                            String strstatus = obj.getString("status");

                            if (strstatus.equals("false")) {
                                Toast.makeText(context, obj.getString("product"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, obj.getString("product"), Toast.LENGTH_SHORT).show();
                                String carttotal = obj.getString("cart-total");
                                SetTotalCartItem(context, carttotal);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError)

                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                store = Keystore.getInstance(context);
                params.put("user_id", store.get("id"));
                params.put("productid", pid);
                params.put("quentity", "1");
                params.put("additional_offer_id", addoffer);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);

        return retstring;
    }


    @SuppressLint("HardwareIds")
    public static String MacAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        return wInfo.getMacAddress();
    }

    private static void database(final Context context, final String phone) {
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("+91" + phone)
                .document("Cart").collection("1mlLab");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                SetTotalLabCartItem(context, String.valueOf(queryDocumentSnapshots.size()));

            }
        });
    }

    public static float additionalDiscout(double markedprice,double d,double multiple){

        double dis = 1-d/100;

        double amount = (markedprice*multiple)*dis;

        return (float)amount;


    }


    public static String discount_offer_strike(double markedprice, double d) {

        double dis=1-d/100;

        double amount = markedprice/dis;

        return String.valueOf("₹ "+amount);


    }

    private static void showAlert(String msg,Context context){
        try {
            new AlertDialog.Builder(context)
                    .setMessage(msg)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .setTitle("Alert")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ((Activity) context).finish();
                        }
                    }).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private static void duplicatenotfound(Context context,String product,String price,String pid,String discount
            ,String mFasting,long child,ProgressDialog progressDialog,String image){

        Log.d(TAG, "onComplete: after for loop duplicate nnot found");
        user = new HashMap<>();
        user.clear();
        user.put("product", product);
        user.put("price", price);
        user.put("pid", pid);
        user.put("discount", discount);
        user.put("fasting", mFasting);
        user.put("plan_status", "no");
        user.put("plan_value", "no");
        user.put("additional_discount", "00");
        user.put("multiple", "1");
        user.put("child", child);
        user.put("imageUrl",image);
        user.put("date", DateFormat.getDateTimeInstance().format(new Date()));
        db.collection("+91" + store.get("phone"))
                .document("Cart")
                .collection("1mlLab").document(product)
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        db.collection("+91" + store.get("phone"))
                                .document("Cart")
                                .collection("1mlLab").orderBy("child", Query.Direction.ASCENDING);
                        if (context instanceof ProductActivity) {
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            TextView textView = ((Activity) context).findViewById(R.id.textNotify);
                            textView.setText(Integer.toString(Integer.parseInt(textView.getText().toString()) + 1));
                        }
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(context, "Item has been added", Toast.LENGTH_SHORT).show();
                        SetTotalLabCartItem(context, String.valueOf(Integer.parseInt(GetTotalLabCartItem(context)) + 1));
                    }
                });

    }
    private static void deletecart(){
        db = FirebaseFirestore.getInstance();
        db.collection("+91" + store.get("phone"))
                .document("Cart").collection("1mlLab").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        db = FirebaseFirestore.getInstance();
                        db.collection("+91" + store.get("phone"))
                                .document("Cart").collection("1mlLab").document(document.get("product").toString()).delete();

                    }
                }
            }
        });

    }
    public static void deleteBen(String name){
        db = FirebaseFirestore.getInstance();
        db.collection("+91" + store.get("phone"))
                .document("Beneficiary")
                .collection("1mlLab").document(name).delete();

    }
    public static void deleteAdd(String pincode){
        db = FirebaseFirestore.getInstance();
        db.collection("+91" + store.get("phone"))
                .document("Address")
                .collection("1mlLab")
                .document(pincode).delete();

    }

   public static Boolean getPlan_choosen(String[] product,ProgressDialog progressDialog) {
       if(!progressDialog.isShowing()){
           progressDialog.show();
       }
       final Boolean[] result = {false};
       db = FirebaseFirestore.getInstance();
       for (int i = 0; i < product.length; i++) {
           if(result[0]==false){
           db.collection("+91" + store.get("phone"))
                   .document("Cart").collection("1mlLab")
                   .document(product[i]).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
               @Override
               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                   if (task.isSuccessful() && task.getResult().exists()) {
                       if (task.getResult().getString("plan_status").equals("Yes")){
                        result[0] = true;
                       }else{
                           result[0]=false;
                       }
                   }else{
                       progressDialog.dismiss();
                   }
               }
           });
       }else{ progressDialog.dismiss();
               break;
           }
       }
       return result[0];
   }
}







