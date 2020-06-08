package com.medical.product.Ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.medical.product.R;
import com.medical.product.adapter.SpinnerCustomAdapter;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.medical.product.helpingFile.ReuseMethod.hideStatusbar;

public class EditProfileActivity extends AppCompatActivity {
    EditText edtUsername, edtUseremail, edtUserphone, edtUseraddress, edtUserWeight, edtUserEContactnumber, edtUserEContactname, edtUserHeight, edtCity;
    TextView txtDateofbirth;
    String[] gendername = {"Gender", "Male", "Female", "Other"};
    int genderimage[] = {1, R.drawable.maleicon, R.drawable.femaleicon, R.drawable.othergender};
    Button btnSubmit;
    SpinnerCustomAdapter customAdapter;
    Spinner spinnergender, spinnerBloodGroup, spinnerMarriageStatus;
    final Calendar myCalendar = Calendar.getInstance();

    private Keystore store;
    int GET_TEXT_REQUEST_CODE = 99;

    Button btnverifiyphone;
    String phoneverification = "";


    Context context = EditProfileActivity.this;

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        hideStatusbar(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ComponentInitialization();

        getUserData();


    }

       private void getUserData() {

        store = Keystore.getInstance(getApplicationContext());
        txtDateofbirth.setText(store.get("dob"));
        edtUsername.setText(store.get("name"));
        edtUseremail.setText(store.get("email"));
        edtUserphone.setText(store.get("phone"));
        edtUseraddress.setText(store.get("address"));
        edtUserHeight.setText(store.get("height"));
        edtUserWeight.setText(store.get("weight"));
        edtCity.setText(store.get("city"));
        edtUserEContactnumber.setText(store.get("em_co_nu"));
        edtUserEContactname.setText(store.get("em_co_na"));

        final String[] bloodgrouparr = getResources().getStringArray(R.array.bloodgroup_array);
        if (store.get("b_group") != null) {
            for (int x = 0; x < bloodgrouparr.length; x++) {
                if (store.get("b_group").equals(bloodgrouparr[x])) {
                    spinnerBloodGroup.setSelection(x);
                }
            }
        }

        final String[] marriagearr = getResources().getStringArray(R.array.marriagestatus_array);
        if (store.get("mar_status") != null) {
            for (int x = 0; x < marriagearr.length; x++) {
                if (store.get("mar_status").equals(marriagearr[x])) {
                    spinnerMarriageStatus.setSelection(x);
                }
            }
        }
        final String[] gendername = getResources().getStringArray(R.array.gender_array);
        for (int i = 0; i < gendername.length; i++) {
            if (gendername[i].equals(store.get("gender"))) {
                spinnergender.setSelection(i);
            }
        }

    }

    private void ComponentInitialization() {
        btnverifiyphone = (Button) findViewById(R.id.btnverifiyphone);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        txtDateofbirth = (TextView) findViewById(R.id.txtDateofbirth);

        spinnergender = (Spinner) findViewById(R.id.spinnergender);
        spinnerBloodGroup = (Spinner) findViewById(R.id.spinnerBloodGroup);
        spinnerMarriageStatus = (Spinner) findViewById(R.id.spinnerMarriageStatus);
        spinnerBloodGroup.setPrompt("Blood Group...");
        spinnerBloodGroup.setPopupBackgroundResource(R.drawable.spinner_popup_background);

        spinnerMarriageStatus.setPrompt("Marriage...");

        spinnergender.setPopupBackgroundResource(R.drawable.spinner_popup_background);
        spinnergender.setPrompt("Select gender...");

        edtUsername = (EditText) findViewById(R.id.edtUsername);

        edtUseremail = (EditText) findViewById(R.id.edtUseremail);

        edtUserphone = (EditText) findViewById(R.id.edtUserphone);

        edtUseraddress = (EditText) findViewById(R.id.edtUseraddress);

        edtUserHeight = (EditText) findViewById(R.id.edtUserHeight);

        edtUserWeight = (EditText) findViewById(R.id.edtUserWeight);

        edtUserEContactnumber = (EditText) findViewById(R.id.edtUserEContactnumber);

        edtUserEContactname = (EditText) findViewById(R.id.edtUserEContactname);

        edtCity = (EditText) findViewById(R.id.edtCity);


        edtUserHeight.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String height = edtUserHeight.getText().toString();
            }
        });

        edtUserphone.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (edtUserphone.getText().toString().length() == 10) {
                    if (!s.toString().equals(store.get("phone"))) {
                        btnverifiyphone.setVisibility(View.VISIBLE);
                    }
                    if (s.toString().equals(store.get("phone"))) {
                        btnverifiyphone.setVisibility(View.GONE);
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }
        });


    }


    private void updateLabel() {
        String myFormat = "LLLL dd yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        txtDateofbirth.setText(sdf.format(myCalendar.getTime()));
    }

    public void clickFunction(View view) {
        switch (view.getId()) {
            case R.id.txtDateofbirth:
                new DatePickerDialog(EditProfileActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.btnSubmit:
                UpdateUserData();
                break;
            case R.id.btnverifiyphone:
                sendOtptoPhonenumber(edtUserphone.getText().toString());
                break;
            case R.id.txtQty:

                break;
        }
    }

    private void sendOtptoPhonenumber(String s) {
        Intent i = new Intent(this, OtpPanelActivity.class);
        i.putExtra("mobileno", s);
        startActivityForResult(i, 1);
    }

    private void UpdateUserData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.ROOT_HTTP_URL + "user/edituser",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);


                            String strstatus = obj.getString("status");

                            if (strstatus.equals("false")) {
                                Toast.makeText(getApplicationContext(), obj.getString("user_data"), Toast.LENGTH_SHORT).show();
                            } else {
                                store = Keystore.getInstance(getApplicationContext());//Creates or Gets our key pairs.  You MUST have access to current context!
                                store.put("name", edtUsername.getText().toString());
                                store.put("email", edtUseremail.getText().toString());
                                store.put("phone", edtUserphone.getText().toString());
                                store.put("address", edtUseraddress.getText().toString());
                                store.put("dob", txtDateofbirth.getText().toString());
                                store.put("gender", spinnergender.getSelectedItem().toString());
                                store.put("b_group", spinnerBloodGroup.getSelectedItem().toString());
                                store.put("mar_status", spinnerMarriageStatus.getSelectedItem().toString());
                                store.put("height", edtUserHeight.getText().toString());
                                store.put("weight", edtUserWeight.getText().toString());
                                store.put("em_co_nu", edtUserEContactnumber.getText().toString());
                                store.put("em_co_na", edtUserEContactname.getText().toString());
                                store.put("city", edtCity.getText().toString());

                                Toast.makeText(getApplicationContext(), obj.getString("user_data"), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                params.put("id", store.get("id"));

                if (!edtUsername.getText().toString().equals(store.get("name"))) {
                    params.put("name", edtUsername.getText().toString());
                }
                params.put("phone", edtUserphone.getText().toString());

                if (!edtUserphone.getText().toString().equals(store.get("phone"))) {
                    params.put("phone", edtUserphone.getText().toString());
                }

                if (!edtUseremail.getText().toString().equals(store.get("email"))) {
                    params.put("email", edtUseremail.getText().toString());
                }

                if (!edtUseraddress.getText().toString().equals(store.get("address"))) {
                    params.put("address", edtUseraddress.getText().toString());
                }
                if (!txtDateofbirth.getText().toString().equals(store.get("dob"))) {
                    params.put("dob", txtDateofbirth.getText().toString());
                }

                if (!edtUserHeight.getText().toString().equals(store.get("height"))) {
                    params.put("height", edtUserHeight.getText().toString());
                }
                if (!edtUserWeight.getText().toString().equals(store.get("weight"))) {
                    params.put("weight", edtUserWeight.getText().toString());
                }

                if (!edtUserEContactnumber.getText().toString().equals(store.get("em_co_nu"))) {
                    params.put("em_co_nu", edtUserEContactnumber.getText().toString());
                }
                if (!edtUserEContactname.getText().toString().equals(store.get("em_co_na"))) {
                    params.put("em_co_na", edtUserEContactname.getText().toString());// edtUserEContactname.getText().toString()
                }
                //  params.put("em_co_na"," edtUserEContactname.getText().toString()");
                params.put("city", edtCity.getText().toString());
                //txtCity.getText().toString()
             /* if(!txtCity.getText().toString().equals( store.get("city")))
                {
                    params.put("city", txtCity.getText().toString());
                }*/

                if (!spinnerBloodGroup.getSelectedItem().toString().equals(store.get("b_group"))) {
                    params.put("b_group", spinnerBloodGroup.getSelectedItem().toString());
                }
                if (!spinnerMarriageStatus.getSelectedItem().toString().equals(store.get("mar_status"))) {
                    params.put("mar_status", spinnerMarriageStatus.getSelectedItem().toString());
                }
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String input = data.getStringExtra("TAG");
        if (input.equals("vetified")) {
            phoneverification = input;
            Toast.makeText(getApplicationContext(), input, Toast.LENGTH_SHORT).show();
            btnverifiyphone.setVisibility(View.GONE);
            edtUserphone.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_iconfinder_true, 0);
        } else {
            edtUserphone.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_iconfinder_false, 0);
            phoneverification = input;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }
}
