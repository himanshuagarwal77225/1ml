package com.medical.product.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.SharedPreferences;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.medical.product.R;
import com.medical.product.Ui.LoginPanelActivity;
import com.medical.product.Ui.Reminder;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.models.Banner;
import com.medical.product.models.GatterGetOrderProductListModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utlity {
    //to save address in  local
    private static LocationManager locationManager;
    private static Location location;
    //db variables
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static SharedPreferences local_db;
    private static SharedPreferences.Editor tbl_user;
    public static Gson gson = new Gson();
    private static String Custom_Security = "hT$e?~nsemPxCE{-UrhT$e?~nsemPxCE{-Ur";
    private static String Purchase_code = "ba127b0b-d3b8-47ac-ab0f-edcb26e3ef09";
    public static int login = 2;
    public static Socket socket;
    // for user details
    public  static String selected_vendor="";
    public  static String selected_vendorname="";
    private static String Profile_data = "Profile_data";

    //to call okkhtpp
    public static Request post(HashMap<String, String> keys, String api_name) {
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        FormBody.Builder body = new FormBody.Builder();
        for (Object key : keys.keySet()) {
            String value = (String) keys.get(key);
            body.add(key.toString(), value);
        }

        RequestBody parmetrs = body.build();
        Request request = new Request.Builder()
                .addHeader("custom-security", Custom_Security)
                .url("" + api_name)
                .post(parmetrs)
                .build();
        return request;
    }

    public static Request loginapi(String object, String apidata) {
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        FormBody.Builder body = new FormBody.Builder();
        body.add("user_login", object);
        RequestBody parmetrs = body.build();
        final Request request = new Request.Builder()
                .addHeader("custom-security", Custom_Security)
                .url("" + apidata)
                .post(parmetrs)
                .build();
        return request;
    }

    public static Request get(String api_name) {
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Request request = new Request.Builder()
                .url(api_name)
                .get()
                .build();
        return request;
    }

    // to show  full  screen activity
    public static void full_screen(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    //    //to show progress
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static Dialog show_progress(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.custom_progress);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        return dialog;
    }

    //to dismis dilog
    public static void dismiss_dilog(Activity activity, Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static String getlatlngstate(Activity activity, Double MyLat, Double MyLong) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(MyLat, MyLong, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses.get(0).getAdminArea();
    }

    //to show toast
    public static void show_toast(Activity activity, String message) {
        View view = activity.getCurrentFocus();
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    //to check text is empty or not
    public static boolean is_empty(String value) {
        return TextUtils.isEmpty(value);
    }

    //to check is  network is available or not
    public static boolean is_online(Context activity) {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
            //we are connected to a network
            connected = true;
        else
            connected = false;
        return connected;
    }

    //to set image in image view from url

    public static void Set_image(String url, ImageView img) {
        if (!TextUtils.isEmpty(url))
            Glide.with(img.getContext()).load(url).error(R.drawable.all_product).into(img);
    }

    //for chk validation
//    public static boolean isValidMobile(String phone) {
//        return android.util.Patterns.PHONE.matcher(phone).matches();
//        android.util.Patterns.PHONE.matcher(input).matches();
//    }

    public static boolean isValidMobile(String phone) {
        String regEx = "^[0-9]{10,10}$";
        return phone.matches(regEx);
    }

    public static boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // display location
    public static void displayLocationSettingsRequest(final Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("", "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            status.startResolutionForResult((Activity) context, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    // current city name
    public static String get_city(Activity activity, Double lat, Double lng) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            return addresses.get(0).getLocality();

        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    //to check to value is  equal
    public static boolean is_equal(String value1, String value2) {
        boolean is_valid = false;
        if (value1.equals(value2)) {
            is_valid = true;
        }
        return is_valid;
    }
    // to show toast 1

    //to show toast1
    public static void show_toast1(Activity activity, String message, View view) {
        /* View view=activity.getCurrentFocus();*/
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    // to save user  detail
    public static void user_db(Activity activity, String user_info) {
        local_db = activity.getSharedPreferences("user_db", Context.MODE_PRIVATE);
        tbl_user = local_db.edit();
        tbl_user.putString(Profile_data, user_info);
        tbl_user.apply();
    }

    public static String single(Activity activity, String key) throws JSONException {
        JSONObject object = new JSONObject(get_detail(activity));
        return object.getJSONObject("data").getString(key);
    }

    //to get user info details
    public static String get_detail(Activity activity) {
        local_db = activity.getSharedPreferences("user_db", Context.MODE_PRIVATE);
        String user_detail = local_db.getString(Profile_data, "");
        return user_detail;
    }

    //to clear user  db
    public static void clear_db(Activity activity) {
        local_db = activity.getSharedPreferences("user_db", Context.MODE_PRIVATE);
        tbl_user = local_db.edit();
        tbl_user.clear();
        tbl_user.apply();
    }

    //to set user is login
    public static void set_login(Activity activity, boolean value) {
        local_db = activity.getSharedPreferences("login_db", Context.MODE_PRIVATE);
        tbl_user = local_db.edit();
        tbl_user.putBoolean("is_login", value);
        tbl_user.apply();
    }

    //to set user is login
    public static void save_notification(Context activity) {
        local_db = activity.getSharedPreferences("notifi", Context.MODE_PRIVATE);
        tbl_user = local_db.edit();
        tbl_user.putString("notifi", String.valueOf(Integer.parseInt(get_notification(activity)) + 1));
        tbl_user.apply();
    }

    //to set user is login
    public static String get_notification(Context activity) {
        local_db = activity.getSharedPreferences("notifi", Context.MODE_PRIVATE);
        if (!TextUtils.isEmpty(local_db.getString("notifi", "")))
            return local_db.getString("notifi", "");
        else
            return "0";
    }

    public static void clear_notification(Context activity) {
        local_db = activity.getSharedPreferences("notifi", Context.MODE_PRIVATE);
        tbl_user = local_db.edit();
        tbl_user.putString("notifi", "0");
        tbl_user.apply();
    }

    //    // to check is  user login  or not
    public static boolean get_login(Activity activity) {
        local_db = activity.getSharedPreferences("login_db", Context.MODE_PRIVATE);
        return local_db.getBoolean("is_login", false);
    }

    // to call logout
    public static void Logout(Activity activity) {
        set_login(activity, false);
        clear_db(activity);
        activity.startActivity(new Intent(activity, LoginPanelActivity.class));
        activity.finishAffinity();
    }

    public static String getlatlngcity(Activity activity, Double MyLat, Double MyLong) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(MyLat, MyLong, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses.get(0).getLocality();
    }


    public static Location getLocationFromAddress(Activity activity, String strAddress) {

        Geocoder coder = new Geocoder(activity);
        List<Address> address;
        Location location_ = new Location("");
        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            location_.setLatitude(location.getLatitude());
            location_.setLongitude(location.getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return location_;
    }

    public static boolean isGPSEnabled(Activity mContext) {
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String date_conva(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date sourceDate = null;
        try {
            sourceDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat targetFormat = new SimpleDateFormat("E MMM dd,yyyy hh:mm a");
        return String.valueOf(targetFormat.format(sourceDate));
    }

    public static String hour_convart(String hour) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
        Date sourceDate = null;
        try {
            sourceDate = dateFormat.parse(hour);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat targetFormat = new SimpleDateFormat("hh");
        return String.valueOf(targetFormat.format(sourceDate));
    }

    public static void save_address(Activity activity, String address) {
        SharedPreferences preferences = activity.getSharedPreferences("save_address", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("address", address);
        editor.apply();
    }
    public static void clear_address(Context activity) {
        SharedPreferences preferences = activity.getSharedPreferences("save_address", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    //to  get  last saved address
    public static ArrayList<Banner> get_banner(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences("banner", Context.MODE_PRIVATE);
        if (!TextUtils.isEmpty(preferences.getString("banner", "")))
            return gson.fromJson(preferences.getString("banner", ""), new TypeToken<List<Banner>>() {
            }.getType());
        else
            return new ArrayList<>();
    }

    public static void save_banner(Activity activity, String list) {
        SharedPreferences preferences = activity.getSharedPreferences("banner", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("banner", list);
        editor.apply();
    }

    //to  get  last saved address
    public static String get_address(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences("save_address", Context.MODE_PRIVATE);
        return preferences.getString("address", "");
    }

    public static void save_alarm(Context context, Reminder reminder) {
        ArrayList<Reminder> list = get_remider(context);
        list.add(reminder);
        SharedPreferences preferences = context.getSharedPreferences("reminder", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("reminder", new Gson().toJson(list));
        editor.apply();
    }

    public static void save_alarm2(Context context, ArrayList<Reminder> list) {
        SharedPreferences preferences = context.getSharedPreferences("reminder", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("reminder", new Gson().toJson(list));
        editor.apply();
    }

    public static void clear_reminder(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("reminder", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public static ArrayList<Reminder> get_remider(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("reminder", Context.MODE_PRIVATE);
        if (!TextUtils.isEmpty(preferences.getString("reminder", null)))
            return new Gson().fromJson(preferences.getString("reminder", null), new TypeToken<List<Reminder>>() {
            }.getType());
        else
            return new ArrayList<>();
    }

    private static void alaram_toogle(Context context, String id, boolean active) {
        ArrayList<Reminder> list = get_remider(context);
        for (Reminder reminder : list) {
            if (reminder.getId().equalsIgnoreCase(id)) {
                reminder.setActivie(active);
                break;
            }
        }
        SharedPreferences preferences = context.getSharedPreferences("reminder", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("reminder", new Gson().toJson(list));
        editor.apply();
    }

    public static Reminder get_reminder(Context context, String id) {
        ArrayList<Reminder> list = get_remider(context);
        Reminder returenrimnder = null;
        for (Reminder reminder : list) {
            if (reminder.getId().equalsIgnoreCase(id)) {
                returenrimnder = reminder;
                break;
            }
        }
        return returenrimnder;
    }

    public static int getRandomNumber(int min, int max) {
        return (new Random()).nextInt((max - min) + 1) + min;
    }
}
