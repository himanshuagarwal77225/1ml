package com.medical.product.Ui;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.medical.product.R;
import com.medical.product.Utils.FilePath;
import com.medical.product.adapter.SpinnerCustomAdapter;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.helpingFile.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static android.media.CamcorderProfile.get;
import static com.medical.product.helpingFile.ReuseMethod.hideStatusbar;

public class ProfileActivity extends AppCompatActivity {
    private static LinearLayout txtfamily, txtOrders, txtWishList, txtmyhealthrecord, singout, txtprecption, myprofile;
    TextView txtusername, txtuserphone, txtuseremail, textNotifycart;
    ImageButton imgBtnEdit;
    ImageView imgeditimage;
    private Keystore store;
    CircleImageView CimgivThumb;
    ArrayList<String> encodedImageList;
    Bitmap selectedImageBitmap;
    private JSONObject jsonObject;
    Bitmap bitmapmain = null;
    ProgressDialog dialog;
    BottomSheetDialog mBottomSheetDialog;
    ImageView imgCancel, imgFullimage;
    LinearLayout progress;
    View sheetView;
    private Uri imageUri;
    File now_file;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
    };
    String[] PERMISSIONS_CAMERA = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    Context context;
    EditText edtUsername, edtUseremail, edtUserphone, edtUseraddress, edtUserWeight, edtUserEContactnumber, edtUserEContactname, edtUserHeight, edtCity;
    TextView txtDateofbirth;
    String[] gendername = {"Gender", "Male", "Female", "Other"};
    int genderimage[] = {1, R.drawable.maleicon, R.drawable.femaleicon, R.drawable.othergender};
    Button btnSubmit;
    SpinnerCustomAdapter customAdapter;
    Spinner spinnergender, spinnerBloodGroup, spinnerMarriageStatus;
    final Calendar myCalendar = Calendar.getInstance();
    int GET_TEXT_REQUEST_CODE = 99;
    Button btnverifiyphone;
    String phoneverification = "";

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
        setContentView(R.layout.activity_profile);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        hideStatusbar(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialog = new ProgressDialog(this);
        jsonObject = new JSONObject();


        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        componentInitilization();

        mBottomSheetDialog = new BottomSheetDialog(this);
        sheetView = this.getLayoutInflater().inflate(R.layout.bottom_sheet_view_full_image, null);
        mBottomSheetDialog.setContentView(sheetView);

        imgCancel = (ImageView) sheetView.findViewById(R.id.imgCancel);
        imgFullimage = (ImageView) sheetView.findViewById(R.id.imgFullimage);

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBottomSheetDialog.dismiss();

            }
        });


        loadDataProfile();

        getUserData();

    }


    private void componentInitilization() {
        textNotifycart = (TextView) findViewById(R.id.textNotify);
        txtusername = (TextView) findViewById(R.id.txtusername);
        textNotifycart = (TextView) findViewById(R.id.textNotify);
        imgBtnEdit = (ImageButton) findViewById(R.id.imgBtnEdit);
        CimgivThumb = (CircleImageView) findViewById(R.id.CimgivThumb);
        imgeditimage = (ImageView) findViewById(R.id.imgeditimage);
        progress = (LinearLayout) findViewById(R.id.progress);

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


        edtUsername.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String sf = s.toString();
                if (!sf.equals(s.toString().toUpperCase())) {
                    edtUsername.setText(s.toString().toUpperCase());
                    edtUsername.setSelection(edtUsername.getText().length());
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

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

        setvaluecart();
    }

    private void loadDataProfile() {
        store = Keystore.getInstance(getApplicationContext());
        String image = store.get("image");
        if (!(image.equals("") || image.equals(""))) {
            Glide.with(this).load(ApiFileuri.ROOT_URL_USER_IMAGE + image).into(CimgivThumb);
            Glide.with(this).load(ApiFileuri.ROOT_URL_USER_IMAGE + image).into(imgFullimage);

        }
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


    public void setvaluecart() {
        if (ReuseMethod.GetTotalCartItem(getApplicationContext()).equals("null") || ReuseMethod.GetTotalCartItem(getApplicationContext()).equals("")) {

        } else {
            textNotifycart.setText(ReuseMethod.GetTotalCartItem(getApplicationContext()));
        }

    }

    private void updateLabel() {
        String myFormat = "LLLL dd yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        txtDateofbirth.setText(sdf.format(myCalendar.getTime()));
    }

    public void clickFunction(View view) {
        switch (view.getId()) {
            case R.id.txtDateofbirth:
                DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

                break;
            case R.id.btnSubmit:
                UpdateUserData();
                break;
            case R.id.btnverifiyphone:
                sendOtptoPhonenumber(edtUserphone.getText().toString());
                break;
            case R.id.txtQty:

                break;
            case R.id.imgBtnCart:
                ReuseMethod.OpenCartActivity(this);
                break;
            case R.id.imgBtnSearch:
                getApplicationContext().startActivity(new Intent(getApplicationContext(), SearchProductActivity.class));
                break;
            case R.id.imgBtnEdit:
                startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
                break;
            case R.id.CimgivThumb:
                startActivity(new Intent(this, Full_image.class).putExtra("img", ApiFileuri.ROOT_URL_USER_IMAGE + store.get("image")));
                break;


            case R.id.imgeditimage:


                addpermission();

                LayoutInflater layoutInflater = LayoutInflater.from(this);
                View promptView = layoutInflater.inflate(R.layout.prompt, null);

                final AlertDialog alertD = new AlertDialog.Builder(this).create();

                Button btnCamera = (Button) promptView.findViewById(R.id.btnCamera);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                Button btnGallery = (Button) promptView.findViewById(R.id.btnGallery);
                ImageView close = (ImageView) promptView.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertD.dismiss();
                    }
                });

                btnCamera.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        String permission = android.Manifest.permission.CAMERA;

                        boolean checkper = checkWriteExternalPermission(permission);


                        if (checkper) {
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, "New Picture");
                            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                            imageUri = getContentResolver().insert(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(cameraIntent, 33);
                            // btnAdd1 has been clicked
                            alertD.dismiss();
                        } else {
                            addpermission();

                        }

                    }
                });

                btnGallery.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        String permission = android.Manifest.permission.CAMERA;

                        boolean checkper = checkWriteExternalPermission(permission);


                        if (checkper) {
                            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, 100);
                            alertD.dismiss();
                        } else {
                            addpermission();

                        }


                    }
                });

                alertD.setView(promptView);

                alertD.show();
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

    public void addpermission() {
        if (!hasPermissions(this, PERMISSIONS_CAMERA)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_CAMERA, PERMISSION_ALL);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            bitmapmain = null;
            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                bitmapmain = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                uploadBitmap(imageUri);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 33 && imageUri != null) {
            bitmapmain = null;
            try {
                bitmapmain = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageUri);
                uploadBitmap(imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
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
        } else {
            Toast.makeText(getApplicationContext(), "No_internet image selected", Toast.LENGTH_SHORT).show();
            bitmapmain = null;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean checkWriteExternalPermission(String permission) {
        int res = getApplicationContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }


    private void uploadBitmap(Uri bitmap) {
        final long imagename = System.currentTimeMillis();
        //our custom volley request
        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading Profile....");
        dialog.setCancelable(false);
        dialog.show();

        MultipartBody.Builder builder = new MultipartBody.Builder();

        File file = new File(FilePath.getPath(this, bitmap));
        try {
            now_file = new Compressor(this).compressToFile(file);
            builder.addFormDataPart("userfile", now_file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), now_file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("user_id", store.get("id"));
        MultipartBody body = builder.build();
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(ApiFileuri.ROOT_HTTP_URL + "user/edit-user-image")
                .post(body)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.retryOnConnectionFailure();
        okHttpClient.connectTimeoutMillis();
        okHttpClient.newCall(request).

                enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                dialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NonNull Call call, final @NonNull okhttp3.Response response) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (response.isSuccessful()) {
                                    try {
                                        if (dialog != null) {
                                            dialog.dismiss();
                                        }
                                        JSONObject obj = new JSONObject(response.body().string());
                                        String status = obj.getString("status");
                                        if (status.equals("true")) {
                                            Toast.makeText(getApplicationContext(), obj.getString("user_data"), Toast.LENGTH_SHORT).show();
                                            CimgivThumb.setImageBitmap(bitmapmain);
                                            store.put("image", now_file.getName());
                                            imgFullimage.setImageBitmap(bitmapmain);
                                        } else {
                                            Toast.makeText(getApplicationContext(), obj.getString("user_data"), Toast.LENGTH_SHORT).show();

                                        }
                                        bitmapmain = null;
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), "please try after some time!..", Toast.LENGTH_SHORT).show();
                                        bitmapmain = null;
                                        dialog.dismiss();

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }

                });
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    protected void onResume() {
        setvaluecart();
        super.onResume();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }

            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }


        // other 'case' lines to check for other
        // permissions this app might request

    }
}
