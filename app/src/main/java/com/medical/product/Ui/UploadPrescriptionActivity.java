package com.medical.product.Ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.medical.product.R;
import com.medical.product.Utils.FilePath;
import com.medical.product.adapter.AdapterPrescriptionImage;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.UploadMultiFiles;
import com.medical.product.models.GatterGetPrescriptionImage;
import com.medical.product.usersession.UserSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class UploadPrescriptionActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private JSONObject jsonObject;
    String prescriptionImagestring = "";
    ProgressDialog dialog;
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
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.ACCESS_WIFI_STATE,
    };

    String lastid = "";
    String temp[] = null;
    private Uri imageUri;
    private AdapterPrescriptionImage recyclerAdapter;

    Button btnCamera, btnGallery, btnPrescription, btnContinue;

    private ArrayList<GatterGetPrescriptionImage> gatterprescimg;
    private File output = null;

    //multiple images
    ArrayList<Uri> mArrayUri;
    String imageURI;
    ArrayList<String> encodedImageList;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private Keystore store;
    private UserSession userSession;

    RecyclerView recyclePrescImage;

    CheckBox terms;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    public static boolean is_next = false;
    int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_prescription);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        type = getIntent().getIntExtra("type", 0);

        if (type == 0) {
            setTitle("Upload Prescription");
        } else {
            setTitle("Attach Prescription");
        }
       componentInitilization();
        is_next = false;
        store = Keystore.getInstance(getApplicationContext());
        userSession = new UserSession(getApplicationContext());

        gatterprescimg = new ArrayList<>();

        recyclerAdapter = new AdapterPrescriptionImage(gatterprescimg, this);
        LinearLayoutManager recyclerManagertable = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclePrescImage.setLayoutManager(recyclerManagertable);
        recyclePrescImage.setHasFixedSize(true);
        recyclePrescImage.setAdapter(recyclerAdapter);


        dialog = new ProgressDialog(this);

        jsonObject = new JSONObject();
        mArrayUri = new ArrayList<Uri>();
        encodedImageList = new ArrayList<>();
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);

        }

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_next = false;
                Intent cameraIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, 33);
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_next = false;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Choose application"), 100);
            }
        });

        btnPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_next = false;
                Intent i = new Intent(getApplicationContext(), SelectPrescription.class);
                startActivityForResult(i, 77);

            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (terms.isChecked()) {
//                    addDataAdvertisement();
                    if (!is_next)
                        if (type == 0) {
                            uploadBitmap(gatterprescimg);

                        } else if (type == 1) {
                            Intent intent = new Intent();
                            intent.putExtra("data", new Gson().toJson(gatterprescimg));
                            setResult(2, intent);
                            finish();
                        } else if (type == 2) {
                            uploadToWhatsapp(gatterprescimg);
                        } else {
                            Intent i = new Intent(getApplicationContext(), OrderPrescriptionInfoActivity.class);
                            i.putExtra("insert_id", lastid);
                            startActivity(i);
                        }
                }

                    else
                        Toast.makeText(UploadPrescriptionActivity.this, "Checked Terms &amp; Conditions", Toast.LENGTH_LONG).show();
            }
        });


    }

    private void uploadToWhatsapp(ArrayList<GatterGetPrescriptionImage> gatterprescimg) {
        dialog.setMessage("Uploading Prescription");
        dialog.setCancelable(false);
        dialog.show();
        if (gatterprescimg != null && gatterprescimg.size() > 0) {
//            for (int i = 0; i < gatterprescimg.size(); i++) {
//                if (!gatterprescimg.get(i).getType().equals("uriimg")) {
//                    File file = new File(FilePath.getPath(this, Uri.parse(gatterprescimg.get(i).getImage())));
//                    try {
//                        File now_file = new Compressor(this).compressToFile(file);
//                        builder.addFormDataPart("files", now_file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), now_file));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    if (TextUtils.isEmpty(other)) {
//                        other = gatterprescimg.get(i).getImage();
//                    } else {
//                        other = other + "," + gatterprescimg.get(i).getImage();
//                    }
//
//                }
//            }
            sendImageTo1ml(Uri.parse(gatterprescimg.get(0).getImage()));
        }else{
            dialog.dismiss();
            Toast.makeText(this, "Please Select image first", Toast.LENGTH_SHORT).show();
        }


    }

    private void sendImageTo1ml(Uri imageuri){

        if(whatsappInstalledOrNot()) {
            try {
                String toNumber = "+919999626489"; // contains spaces.
                toNumber = toNumber.replace("+", "").replace(" ", "");
                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.putExtra(Intent.EXTRA_STREAM, imageuri);
                sendIntent.putExtra("jid", toNumber + "@s.whatsapp.net");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Username = "+store.get("name")+"\n"+"Email = "+store.get("email")+
                        "\n"+"Phone number = "+store.get("phone")+"\nPrescription API_KEY = "+store.get("api_key"));
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setPackage("com.whatsapp");
                sendIntent.setType("*/*");
                dialog.dismiss();
                startActivity(sendIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{

            sendEmail(imageuri);
        }
    }
    private boolean whatsappInstalledOrNot() {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }if(app_installed){
            savecontact();
        }
        return app_installed;
    }
    private void savecontact(){
        String DisplayName = "1ml Pharmacy";
        String MobileNumber = "+919999626489";
        ArrayList<ContentProviderOperation> ops =
                new ArrayList<>();
        int rawContactID = ops.size();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, DisplayName)
                .build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());

        try{
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        }catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }
    }
    private void sendEmail(Uri uri) {
        Log.i("Send email", "order.recive@1ml.co.in");
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"order.recive@1ml.co.in"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Prescription");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Prescription id = {oKtp1d0l0SMZ9FtdQmwP0agrI39tKJATf0unh8fpQNYcKLWvXyFzEw==}");
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:"+uri));
        try {
            dialog.dismiss();
            startActivity(Intent.createChooser(emailIntent, "Send Presciption"));
            finish();

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(UploadPrescriptionActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
    private void componentInitilization() {
        btnCamera = (Button) findViewById(R.id.btnCamera);
        btnGallery = (Button) findViewById(R.id.btnGallery);
        btnPrescription = (Button) findViewById(R.id.btnPrescription);
        btnContinue = (Button) findViewById(R.id.btnContinue);
        recyclePrescImage = (RecyclerView) findViewById(R.id.recyclePrescImage);
        terms = (CheckBox) findViewById(R.id.terms);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            // When an Image is picked
            if (requestCode == 100 && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                //  encodedImageList.clear();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                // imagesUriList = new ArrayList<Uri>();


                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    //    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        mArrayUri.add(uri);
                        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imageURI = cursor.getString(columnIndex);
                        encodedImageList.add(imageURI);
                        cursor.close();
                    }
                    recyclerAdapter.notifyDataSetChanged();
                } else {
                    if (data.getData() != null) {
                        Uri mImageUri = data.getData();
                        encodedImageList.add(String.valueOf(mImageUri));
                        recyclerAdapter.notifyDataSetChanged();
                    }
                }
            } else if (requestCode == 33 && resultCode == RESULT_OK) {
                encodedImageList.add(String.valueOf(imageUri));
                recyclerAdapter.notifyDataSetChanged();
            } else if (requestCode == 77) {

                prescriptionImagestring = data.getStringExtra("FILES_TO_SEND");
                recyclerAdapter.notifyDataSetChanged();

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        getAddessList();
        super.onActivityResult(requestCode, resultCode, data);
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void getAddessList() {
        gatterprescimg.clear();
        int i = 0;
        if (encodedImageList.size() > 0) {
            for (int j = 0; j < encodedImageList.size(); j++) {
                gatterprescimg.add(new GatterGetPrescriptionImage(
                        "" + i,
                        "" + j,
                        encodedImageList.get(j),
                        "bitmap"
                ));
                i++;

            }
        }
        if (!prescriptionImagestring.equals("")) {
            temp = prescriptionImagestring.split("#");
            if (temp.length != 0) {
                for (int j = 0; j < temp.length; j++) {
                    gatterprescimg.add(new GatterGetPrescriptionImage(
                            "" + i,
                            "" + j,
                            temp[j],
                            "uriimg"

                    ));
                    i++;
                }

            }
        }


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
                }
                return;
            }

        }

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    private void addDataAdvertisement() {


        JSONArray jsonArray = new JSONArray();

        JSONArray jsonArrayPresc = new JSONArray();

        if (gatterprescimg.size() == 0) {
            Toast.makeText(this, "Please select Prescription images first.", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < gatterprescimg.size(); i++) {
            String type = gatterprescimg.get(i).getType();

            if (type.equals("bitmap")) {
                jsonArray.put(gatterprescimg.get(i).getImage());
            } else if (type.equals("uriimg")) {
                jsonArrayPresc.put(gatterprescimg.get(i).getImage());

            }
        }
        try {
            jsonObject.put("user_id", store.get("id"));
            jsonObject.put(UploadMultiFiles.imageList, jsonArray);
            jsonObject.put("prescription_image", jsonArrayPresc);


        } catch (JSONException e) {
            Log.e("JSONObject Here", e.toString());
        }

        dialog.setMessage(" Uploading Prescription");
        dialog.setCancelable(false);
        dialog.show();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ApiFileuri.ROOT_HTTP_URL + "add-perception", jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        String status = null;
                        try {
                            status = jsonObject.getString("status");
                            if (status.equals("true")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), OrderPrescriptionInfoActivity.class);
                                i.putExtra("insert_id", jsonObject.getString("insert_id"));
                                startActivity(i);
                            } else {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        dialog.dismiss();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplication(), "Error Occurred", Toast.LENGTH_SHORT).show();
                // dialog.dismiss();
                dialog.dismiss();


            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(200 * 30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UploadPrescriptionActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_CONTACTS},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                    return false;
                }
            }
        }
        return true;
    }

    public void setArrayPrescription(String type, String pos) {

        int rempos = Integer.parseInt(pos);

        if (type.equals("bitmap")) {
            encodedImageList.remove(rempos);
        } else if (type.equals("uriimg")) {
            temp[rempos] = null;
        }

    }

    private void uploadBitmap(final ArrayList<GatterGetPrescriptionImage> gatterprescimg) {
        dialog.setMessage("Uploading Prescription");
        dialog.setCancelable(false);
        dialog.show();
        String other = "";
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
        MultipartBody body = builder.build();
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(ApiFileuri.ROOT_HTTP_URL + "add-perception")
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
                                dialog.dismiss();
                                String data = response.body().string();
                                JSONObject obj = new JSONObject(data);
                                if (obj.getBoolean("status")) {
                                    is_next = true;
                                    lastid = obj.getString("insert_id");
                                    Intent i = new Intent(getApplicationContext(), OrderPrescriptionInfoActivity.class);
                                    i.putExtra("insert_id", obj.getString("insert_id"));
                                    startActivity(i);
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

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public Bitmap get_bitmaap(File file) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


}
