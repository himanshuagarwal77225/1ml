package com.medical.product.Ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.medical.product.R;
import com.medical.product.adapter.AdapterMyPrescriptionSelect;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.HelperFunction;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.OneMLUserSpace;
import com.medical.product.helpingFile.VolleyMultipartRequest;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.GatterGetPrescriptionModel;
import com.medical.product.models.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.medical.product.helpingFile.ReuseMethod.hideStatusbar;

public class SelectPrescription extends AppCompatActivity {
    //ImageView to display image selected
    ImageView imageView;
    ProgressDialog dialog;
    //edittext for getting the tags input
    EditText editTitle, editDescription;
    Intent intent;
    Keystore store;
    String userid = "", documenttype;
    FloatingActionButton buttonUploadImage, btnServerupload;
    Bitmap bitmapmain = null;
    RecyclerView recycleviewdocument;
    private AdapterMyPrescriptionSelect recyclerAdapter;
    LinearLayout layUploadimag;

    private ArrayList<GatterGetPrescriptionModel> gatterGetFamilyDocumentListModels;

    private ArrayList<GatterGetPrescriptionModel> gatterGetSend;

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
   //     this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog = new ProgressDialog(this);
        //initializing views
        imageView = (ImageView) findViewById(R.id.imageView);
        editTitle = (EditText) findViewById(R.id.editTitle);
        editDescription = (EditText) findViewById(R.id.editDescription);
        buttonUploadImage = (FloatingActionButton) findViewById(R.id.buttonUploadImage);
        recycleviewdocument = (RecyclerView) findViewById(R.id.recycleviewdocument);
        layUploadimag = (LinearLayout) findViewById(R.id.layUploadimag);

        userid = store.get("id");

        gatterGetFamilyDocumentListModels = new ArrayList<>();
        recyclerAdapter = new AdapterMyPrescriptionSelect(gatterGetFamilyDocumentListModels, this);
        LinearLayoutManager recyclerlayoutManager = new GridLayoutManager(this, 3);
        recycleviewdocument.setLayoutManager(recyclerlayoutManager);
        recycleviewdocument.setHasFixedSize(true);
        recycleviewdocument.setAdapter(recyclerAdapter);

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        getFamilyDocument();

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
                                for (int j = 0; j < 10; j++) {

                                    gatterGetFamilyDocumentListModels.add(new GatterGetPrescriptionModel(
                                            false,
                                            jsonArray.getJSONObject(j).getString("id"),
                                            jsonArray.getJSONObject(j).getString("user_id"),
                                            jsonArray.getJSONObject(j).getString("document_image"),
                                            jsonArray.getJSONObject(j).getString("document_title"),
                                            jsonArray.getJSONObject(j).getString("document_description"),
                                            jsonArray.getJSONObject(j).getString("document_type")
                                    ));
                                    recyclerAdapter.notifyDataSetChanged();

                                }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("dfsasfd", "profile error=========" + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //params.put("user_id", userid);
                OneMLUserSpace oneMLUserSpace = OneMLUserSpace.getInstance(SelectPrescription.this);
                UserModel userModel = oneMLUserSpace.getUserProfile();
                if (userModel != null) {
                    if(HelperFunction.checkStringEmpty(userModel.getId())){
                        params.put("user_id", userModel.getId());
                    }
                }
                params.put("document_type", "3");
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }


    public void clickFunction(View view) {
        switch (view.getId()) {
            case R.id.buttonUploadImage:
                String strarr = "";
                for (int i = 0; i < gatterGetFamilyDocumentListModels.size(); i++) {

                    Boolean b = gatterGetFamilyDocumentListModels.get(i).isSelected();
                    if (b) {
                        strarr = strarr + gatterGetFamilyDocumentListModels.get(i).getDocument_image() + "#";
                    }
                }
                if (!TextUtils.isEmpty(strarr)) {
                    Intent intent = new Intent(this, UploadPrescriptionActivity.class);
                    intent.putExtra("FILES_TO_SEND", strarr);
                    setResult(77, intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Select Prescription", Toast.LENGTH_LONG).show();
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

                layUploadimag.setVisibility(View.VISIBLE);
                //calling the method uploadBitmap to upload image


            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 33) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri tempUri = getImageUri(getApplicationContext(), photo);

            Toast.makeText(this, "Image" + tempUri,
                    Toast.LENGTH_LONG).show();
            File finalFile = new File(getRealPathFromURI(tempUri));

            try {
                bitmapmain = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.fromFile(finalFile));

                imageView.setImageBitmap(bitmapmain);
                layUploadimag.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }


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
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {
        dialog.setMessage(" Uploading wait");
        dialog.setCancelable(false);
        dialog.show();

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ApiFileuri.ROOT_HTTP_URL + "add-user-document",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {


                            dialog.dismiss();

                            JSONObject obj = new JSONObject(new String(response.data));

                            editTitle.setText("");
                            editDescription.setText("");
                            bitmapmain = null;

                            layUploadimag.setVisibility(View.GONE);

                            getFamilyDocument();
                            Toast.makeText(getApplicationContext(), obj.getString("user_data"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "please try after some time!..", Toast.LENGTH_SHORT).show();
                            editTitle.setText("");
                            editDescription.setText("");
                            bitmapmain = null;
                            dialog.dismiss();

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

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
               // params.put("user_id", userid);
                OneMLUserSpace oneMLUserSpace = OneMLUserSpace.getInstance(SelectPrescription.this);
                UserModel userModel = oneMLUserSpace.getUserProfile();
                if (userModel != null) {
                    if(HelperFunction.checkStringEmpty(userModel.getId())){
                        params.put("user_id", userModel.getId());
                    }
                }
                params.put("document_title", editTitle.getText().toString());
                params.put("document_description", editDescription.getText().toString());
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
                    ActivityCompat.requestPermissions(SelectPrescription.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
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
            case 100: {
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

