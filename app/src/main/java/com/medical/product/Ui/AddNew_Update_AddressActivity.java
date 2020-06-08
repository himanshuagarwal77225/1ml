package com.medical.product.Ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.medical.product.R;
import com.medical.product.Utils.Utlity;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.GPSTracker;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.GatterGetAddressListModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class AddNew_Update_AddressActivity extends AppCompatActivity {
    EditText edtpostalcode, edtHomenoBuildname, edtRoadnameAreaColony, edtcity, edtstate, edtcountry, edtLandmark, edtFirstname, edtLastname, edtPhoneNumber, edtAlterPhoneNumber, edtemail;
    Button btnAddchooseNewAddress;
    String struserfor = "";
    private Keystore store;
    Intent intent;

    private RadioGroup radioGroupaddtype;
    private RadioButton radioButtonaddtype, radhome, radoffice;

    LocationManager manager;
    String provider;
    GPSTracker tracker;
    String longituted = "", latitude = "";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ComponentInitialization();

        intent = getIntent();
        struserfor = intent.getStringExtra("Addupdate");
        tracker = new GPSTracker(this);

        if (struserfor.equals("update")) {

            setTitle("Edit Billing / Shipping Address");

            edtFirstname.setText(intent.getStringExtra("fname"));
            edtLastname.setText(intent.getStringExtra("lname"));
            edtemail.setText(intent.getStringExtra("email"));
            edtcountry.setText(intent.getStringExtra("country"));
            edtPhoneNumber.setText(intent.getStringExtra("phone"));
            edtstate.setText(intent.getStringExtra("state"));
            edtcity.setText(intent.getStringExtra("city"));
            edtpostalcode.setText(intent.getStringExtra("postal"));

            edtHomenoBuildname.setText(intent.getStringExtra("homenumber"));
            edtRoadnameAreaColony.setText(intent.getStringExtra("addarea"));
            edtLandmark.setText(intent.getStringExtra("landmark"));
            edtAlterPhoneNumber.setText(intent.getStringExtra("alterphone"));
            String addtype = intent.getStringExtra("addtype");
            if (addtype.equals("Home")) {
                radhome.setChecked(true);
            } else if (addtype.equals("Office")) {
                radoffice.setChecked(true);

            }
            btnAddchooseNewAddress.setText("Update Address");
        } else {
            setTitle("New Billing / Shipping Address");
            btnAddchooseNewAddress.setText("Add Address");
            edtFirstname.setText(store.get("fname"));
            edtLastname.setText(intent.getStringExtra("lname"));
            edtemail.setText(store.get("email"));
            edtPhoneNumber.setText(store.get("phone"));
        }
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = manager.getBestProvider(new Criteria(), false);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            getAddressLocation(tracker.getLatitude(), tracker.getLongitude());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            getAddressLocation(tracker.getLatitude(), tracker.getLongitude());
        }
    }

    private void ComponentInitialization() {
        edtpostalcode = (EditText) findViewById(R.id.edtpostalcode);
        edtHomenoBuildname = (EditText) findViewById(R.id.edtHomenoBuildname);
        edtRoadnameAreaColony = (EditText) findViewById(R.id.edtRoadnameAreaColony);
        edtcity = (EditText) findViewById(R.id.edtcity);
        edtstate = (EditText) findViewById(R.id.edtstate);
        edtcountry = (EditText) findViewById(R.id.edtcountry);
        edtLandmark = (EditText) findViewById(R.id.edtLandmark);
        edtFirstname = (EditText) findViewById(R.id.edtFirstname);
        edtLastname = (EditText) findViewById(R.id.edtLastname);
        edtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);
        edtAlterPhoneNumber = (EditText) findViewById(R.id.edtAlterPhoneNumber);
        edtemail = (EditText) findViewById(R.id.edtemail);
        radioGroupaddtype = (RadioGroup) findViewById(R.id.radioGroupaddtype);

        radhome = (RadioButton) findViewById(R.id.radhome);
        radoffice = (RadioButton) findViewById(R.id.radoffice);
        btnAddchooseNewAddress = (Button) findViewById(R.id.btnAddchooseNewAddress);

        store = Keystore.getInstance(getApplicationContext());
    }

    public void clickFunction(View view) {
        switch (view.getId()) {


            case R.id.btnAddchooseNewAddress:
                String straddtype = "";
                Toast.makeText(this, store.get("id"), Toast.LENGTH_SHORT).show();
                int selectedId = radioGroupaddtype.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioButtonaddtype = (RadioButton) findViewById(selectedId);


                if (selectedId != -1 && radioButtonaddtype.isChecked()) {

                    straddtype = radioButtonaddtype.getText().toString();
                }


                String strpostalcode = edtpostalcode.getText().toString();
                String strhomenumber = edtHomenoBuildname.getText().toString();
                String strroadnamearea = edtRoadnameAreaColony.getText().toString();
                String strcity = edtcity.getText().toString();
                String strstate = edtstate.getText().toString();
                String strcountry = edtcountry.getText().toString();
                String strlandmark = edtLandmark.getText().toString();
                String strfirstname = edtFirstname.getText().toString();
                String strlastname = edtLastname.getText().toString();
                String strphonenumber = edtPhoneNumber.getText().toString();
                String stralterphone = edtAlterPhoneNumber.getText().toString();
                String stremail = edtemail.getText().toString();

                ArrayList<EditText> edit = new ArrayList<>();
                edit.add(edtpostalcode);
                edit.add(edtHomenoBuildname);
                edit.add(edtRoadnameAreaColony);
                edit.add(edtcity);
                edit.add(edtstate);
                edit.add(edtFirstname);
                edit.add(edtLastname);
                edit.add(edtPhoneNumber);
                edit.add(edtemail);
                edit.add(edtRoadnameAreaColony);
                if (validation(edit)) {
                    if (!TextUtils.isEmpty(straddtype)) {
                        if (struserfor.equals("insert")) {
                            String apifor = "new";
                            String uriadd = ApiFileuri.ROOT_HTTP_URL + "product/add_shipping_address";
                            AddressEditNewMethod(straddtype, strpostalcode, strhomenumber, strroadnamearea, strcity, strstate, strcountry, strlandmark, strfirstname, strlastname, strphonenumber, stralterphone, stremail, uriadd, apifor);
                        } else if (struserfor.equals("update")) {
                            String apifor = "edit";
                            String uriadd = ApiFileuri.ROOT_HTTP_URL + "product/update_shipping_address";
                            AddressEditNewMethod(straddtype, strpostalcode, strhomenumber, strroadnamearea, strcity, strstate, strcountry, strlandmark, strfirstname, strlastname, strphonenumber, stralterphone, stremail, uriadd, apifor);

                        }
                    } else {
                        Toast.makeText(this, "Please Select Address Type", Toast.LENGTH_LONG).show();
                    }
                }
                break;

        }
    }

    private boolean validation(ArrayList<EditText> edit) {
        boolean is_right = true;
        for (EditText editText : edit) {
            if (TextUtils.isEmpty(editText.getText().toString())) {
                editText.setError(editText.getHint() + " is Required !");
                editText.requestFocus();
                is_right = false;
                break;
            }
        }
        return is_right;
    }

    private void AddressEditNewMethod(final String straddtype, final String strpostalcode,
                                      final String strhomenumber, final String strroadnamearea,
                                      final String strcity, final String strstate, final String strcountry,
                                      final String strlandmark, final String strfirstname, final String strlastname,
                                      final String strphonenumber, final String stralterphone, final String stremail,
                                      final String uriadd, final String addapifor) {
        //if everything is fine

        final Dialog dialog = Utlity.show_progress(this);
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uriadd,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj = null;
                        try {
                            Utlity.dismiss_dilog(AddNew_Update_AddressActivity.this, dialog);
                            obj = new JSONObject(response);
                            String strstatus = obj.getString("status");
                            if (strstatus.equals("true")) {
                                Toast.makeText(getApplicationContext(), obj.getString("product"), Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("product"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utlity.dismiss_dilog(AddNew_Update_AddressActivity.this, dialog);
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if (addapifor.equals("edit")) {
                    params.put("id", intent.getStringExtra("addid"));
                } else if (addapifor.equals("new")) {
                    params.put("user_id", store.get("id"));
                }
                params.put("postal_code", strpostalcode);
                params.put("address_area", strroadnamearea);
                params.put("city", strcity);
                params.put("state", strstate);
                params.put("country", strcountry);
                params.put("landmark", strlandmark);
                params.put("first_name", strfirstname);
                params.put("last_name", strlastname);
                params.put("phone_number", strphonenumber);
                params.put("alternative_number", stralterphone);
                params.put("email", stremail);
                params.put("address_type", straddtype);
                params.put("latitude", String.valueOf(getLocationFromAddress(strroadnamearea + "," + strcity + "," + strstate + "," + strcountry).latitude));
                params.put("longitude", String.valueOf(getLocationFromAddress(strroadnamearea + "," + strcity + "," + strstate + "," + strcountry).longitude));
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

    private boolean isValidMail(String email) {
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(email);
        check = m.matches();

        if (!check) {

            // Toast.makeText(getApplicationContext(),"Not Valid Email",Toast.LENGTH_SHORT).show();

        }
        return check;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.dismiss();
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        final AlertDialog alert = builder.create();
        if (!alert.isShowing())
            alert.show();
    }

    public String getAddressLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String address = "", lat = "", logt = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            for (int i = 0; i < addresses.size(); i++) {
                address = addresses.get(i).getAddressLine(i); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(i).getLocality();
                String state = addresses.get(i).getAdminArea();
                String country = addresses.get(i).getCountryName();
                String postalCode = addresses.get(i).getPostalCode();
                String knownName = addresses.get(i).getFeatureName();
                edtpostalcode.setText(postalCode);
                edtcity.setText(city);
                edtstate.setText(state);
                edtRoadnameAreaColony.setText(address);
                if (!TextUtils.isEmpty(Utlity.get_address(AddNew_Update_AddressActivity.this))) {
                    GatterGetAddressListModel addressListModel = new Gson().fromJson(Utlity.get_address(AddNew_Update_AddressActivity.this), GatterGetAddressListModel.class);
                    edtFirstname.setText(addressListModel.getFirst_name());
                    edtLastname.setText(addressListModel.getLast_name());
                    edtLandmark.setText(addressListModel.getLandmark());
                    edtPhoneNumber.setText(addressListModel.getPhone_number());
                    edtAlterPhoneNumber.setText(addressListModel.getAlternative_number());
                    edtemail.setText(addressListModel.getEmail());

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    public LatLng getLocationFromAddress(String strAddress) {
        LatLng latLng = new LatLng(0.0, 0.0);
        Geocoder coder = new Geocoder(this);
        List<Address> address;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            return latLng;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return latLng;
    }
}
