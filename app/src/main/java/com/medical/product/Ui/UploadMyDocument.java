package com.medical.product.Ui;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.medical.product.R;
import com.medical.product.adapter.AdapterMyDocument;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.VolleyMultipartRequest;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.GatterGetFamilyDocumentListModel;

import org.json.JSONArray;
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

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.medical.product.helpingFile.ReuseMethod.hideStatusbar;

public class UploadMyDocument extends AppCompatActivity {
    //ImageView to display image selected
    ImageView imageView;
    ProgressDialog dialog;
    //edittext for getting the tags input
    EditText editTitle, editDescription, edtdr,editprovider;
    File img_file=null;
    Intent intent;
    Keystore store;

    String userid = "", documenttype;

    Button btnServerupload;
    FloatingActionButton buttonUploadImage;
    Bitmap bitmapmain = null;

    RecyclerView recycleviewdocument;
    private AdapterMyDocument recyclerAdapter;
    LinearLayout layUploadimag, layemptydocument,progress;
    final Calendar myCalendar = Calendar.getInstance();
    View sheetView;

    private ArrayList<GatterGetFamilyDocumentListModel> gatterGetFamilyDocumentListModels;
    BottomSheetDialog mBottomSheetDialog;

    ImageView imgCancel, imgFullimage;


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_SMS,
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.ACCESS_WIFI_STATE,
    };

    String[] PERMISSIONS_CAMERA = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,

    };

    String[] WRITE_EXTERNAL = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,

    };
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
        setContentView(R.layout.activity_upload_family_document);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        hideStatusbar(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        store = Keystore.getInstance(getApplicationContext());
        //  this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog = new ProgressDialog(this);
        //initializing views
        imageView = (ImageView) findViewById(R.id.imageView);
        editTitle = (EditText) findViewById(R.id.editTitle);
        edtdr = (EditText) findViewById(R.id.edtdr);
        editprovider = (EditText) findViewById(R.id.editprovider);
        editDescription = (EditText) findViewById(R.id.editDescription);
        editDescription.setInputType(InputType.TYPE_NULL);

        buttonUploadImage = (FloatingActionButton) findViewById(R.id.buttonUploadImage);
        btnServerupload = (Button) findViewById(R.id.btnServerupload);
        recycleviewdocument = (RecyclerView) findViewById(R.id.recycleviewdocument);
        layUploadimag = (LinearLayout) findViewById(R.id.layUploadimag);
        progress = (LinearLayout) findViewById(R.id.progress);
        layemptydocument = (LinearLayout) findViewById(R.id.layemptydocument);
        intent = getIntent();
        documenttype = intent.getStringExtra("documenttype");

        userid = store.get("id");
        if (documenttype.equals("0")) {
            toolbar.setTitle("Lab Document");
        } else if (documenttype.equals("1")) {
            toolbar.setTitle("XRAY Document");
        } else if (documenttype.equals("2")) {
            toolbar.setTitle("Other Document");
        } else if (documenttype.equals("3")) {
            toolbar.setTitle("Prescription Document");
        }

        gatterGetFamilyDocumentListModels = new ArrayList<>();
        recyclerAdapter = new AdapterMyDocument(gatterGetFamilyDocumentListModels, this);
        LinearLayoutManager recyclerlayoutManager = new LinearLayoutManager(getApplicationContext());
        recycleviewdocument.setLayoutManager(recyclerlayoutManager);
        recycleviewdocument.setHasFixedSize(true);
        recycleviewdocument.setAdapter(recyclerAdapter);

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);

        }

        getFamilyDocument();

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

        editDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_datepicker();
            }
        });

    }

    private void open_datepicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(UploadMyDocument.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void updateLabel() {
        String myFormat = "dd/mm/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editDescription.setText(sdf.format(myCalendar.getTime()));
    }

    public void ViewImageFull(String uriimg) {
        Glide.with(this).load(uriimg).into(imgFullimage);
        mBottomSheetDialog.show();
    }


    private void getFamilyDocument() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.ROOT_HTTP_URL + "get-user-document",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            String strstatus = obj.getString("status");
                            if (strstatus.equals("false")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {

                                gatterGetFamilyDocumentListModels.clear();

                                JSONArray jsonArray = obj.getJSONArray("message");
                                if (jsonArray.length() == 0) {

                                }

                                for (int j = 0; j < jsonArray.length(); j++) {


                                    gatterGetFamilyDocumentListModels.add(new GatterGetFamilyDocumentListModel(
                                            jsonArray.getJSONObject(j).getString("id"),
                                            jsonArray.getJSONObject(j).getString("user_id"),
                                            jsonArray.getJSONObject(j).getString("document_image"),
                                            jsonArray.getJSONObject(j).getString("document_title"),
                                            jsonArray.getJSONObject(j).getString("document_description"),
                                            jsonArray.getJSONObject(j).getString("dr_name"),
                                            jsonArray.getJSONObject(j).getString("document_type")
                                    ));
                                    recyclerAdapter.notifyDataSetChanged();

                                }

                                if (gatterGetFamilyDocumentListModels.size() == 0) {
                                    layemptydocument.setVisibility(View.VISIBLE);
                                    recycleviewdocument.setVisibility(View.GONE);
                                } else {
                                    recycleviewdocument.setVisibility(View.VISIBLE);
                                    layemptydocument.setVisibility(View.GONE);
                                }

                            }

                            if (gatterGetFamilyDocumentListModels.size() == 0) {
                                layemptydocument.setVisibility(View.VISIBLE);
                                recycleviewdocument.setVisibility(View.GONE);
                            } else {
                                recycleviewdocument.setVisibility(View.VISIBLE);
                                layemptydocument.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (gatterGetFamilyDocumentListModels.size() == 0) {
                            layemptydocument.setVisibility(View.VISIBLE);
                            recycleviewdocument.setVisibility(View.GONE);
                        } else {
                            recycleviewdocument.setVisibility(View.VISIBLE);
                            layemptydocument.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (gatterGetFamilyDocumentListModels.size() == 0) {
                            layemptydocument.setVisibility(View.VISIBLE);
                            recycleviewdocument.setVisibility(View.GONE);
                        } else {
                            recycleviewdocument.setVisibility(View.VISIBLE);
                            layemptydocument.setVisibility(View.GONE);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userid);
                params.put("document_type", documenttype);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    public void clickFunction(View view) {
        switch (view.getId()) {
            case R.id.buttonUploadImage:
                LayoutInflater layoutInflater = LayoutInflater.from(this);
                View promptView = layoutInflater.inflate(R.layout.prompt, null);

                final AlertDialog alertD = new AlertDialog.Builder(this).create();
                ImageView close = (ImageView) promptView.findViewById(R.id.close);

                Button btnCamera = (Button) promptView.findViewById(R.id.btnCamera);
               // alertD.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                Button btnGallery = (Button) promptView.findViewById(R.id.btnGallery);

                btnCamera.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        String permission = android.Manifest.permission.CAMERA;
                        boolean checkper = checkWriteExternalPermission(permission);
                        boolean checkperexter = checkWriteExternalPermissionasdf(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (checkper) {
                            if (checkperexter) {
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, 33);
                                // btnAdd1 has been clicked
                                alertD.dismiss();
                            } else {
                                addpermissionexternal();
                            }

                        } else {
                            addpermission();
                        }

                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertD.dismiss();
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

                //if the tags edittext is empty
                //we will throw input error

                break;

            case R.id.btnServerupload:
                if (!(userid.equals("") || userid.equals(null))) {
                    if (bitmapmain!=null) {
                        uploadBitmap(bitmapmain);

                    } else {
                        Toast.makeText(getApplicationContext(), "Please Select image !", Toast.LENGTH_SHORT).show();

                    }
                } else {

                    Toast.makeText(getApplicationContext(), "Please first Create mamber or select mamber", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            bitmapmain = null;
            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                bitmapmain = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                //displaying selected image to imageview
                imageView.setImageBitmap(bitmapmain);
                img_file=new File(getRealPathFromURI(imageUri));
                layUploadimag.setVisibility(View.VISIBLE);
                layemptydocument.setVisibility(View.GONE);
                //calling the method uploadBitmap to upload image
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 33) {
            if (data != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri tempUri = getImageUri(getApplicationContext(), photo);

                Toast.makeText(this, "Image" + tempUri,
                        Toast.LENGTH_LONG).show();
                File finalFile = new File(getRealPathFromURI(tempUri));

                try {
                    bitmapmain = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.fromFile(finalFile));
                    img_file=new File(getRealPathFromURI(tempUri));
                    imageView.setImageBitmap(bitmapmain);
                    layUploadimag.setVisibility(View.VISIBLE);
                    layemptydocument.setVisibility(View.GONE);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }

    }

    public void addpermission() {
        if (!hasCamerapermission(this, PERMISSIONS_CAMERA)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_CAMERA, PERMISSION_ALL);
        }
    }

    public void addpermissionexternal() {
        if (!hasexternal(this, WRITE_EXTERNAL)) {
            ActivityCompat.requestPermissions(this, WRITE_EXTERNAL, PERMISSION_ALL);
        }
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


    private boolean checkWriteExternalPermission(String permission) {

        int res = getApplicationContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private boolean checkWriteExternalPermissionasdf(String permission) {

        int res = getApplicationContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }


    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {
        progress.setVisibility(View.VISIBLE);
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ApiFileuri.ROOT_HTTP_URL + "add-user-document",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            progress.setVisibility(View.GONE);
                            JSONObject obj = new JSONObject(new String(response.data));
                            editTitle.setText("");
                            editDescription.setText("");
                            edtdr.setText("");
                            editprovider.setText("");
                            bitmapmain = null;
                            layUploadimag.setVisibility(View.GONE);
                            getFamilyDocument();
                            Toast.makeText(getApplicationContext(), obj.getString("user_data"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "please try after some time!..", Toast.LENGTH_SHORT).show();
                            progress.setVisibility(View.GONE);
                            editTitle.setText("");
                            editDescription.setText("");
                            editprovider.setText("");
                            edtdr.setText("");
                            bitmapmain = null;
                            layUploadimag.setVisibility(View.GONE);

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
                params.put("user_id", userid);
                params.put("document_title", editTitle.getText().toString());
                params.put("document_description", editDescription.getText().toString());
                params.put("dr_name", edtdr.getText().toString());
                params.put("provider", editprovider.getText().toString());
                params.put("document_type", documenttype);
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("userfile", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
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
                    ActivityCompat.requestPermissions(UploadMyDocument.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                    return false;
                }
            }
        }
        return true;
    }

    public boolean hasCamerapermission(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UploadMyDocument.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                    return false;
                }
            }
        }
        return true;
    }

    public boolean hasexternal(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UploadMyDocument.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                    return false;
                }
            }
        }
        return true;
    }


}

