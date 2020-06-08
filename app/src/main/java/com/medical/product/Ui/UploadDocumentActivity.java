package com.medical.product.Ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.medical.product.R;
import com.medical.product.helpingFile.FilePathHelper;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class UploadDocumentActivity extends AppCompatActivity {
    String file_1=null;
Button btnUploadDocument;
    String uploadedFileName;
    File filefound= null;
    MultipartBody.Part attachfileToUpload = null;
    RequestBody attachfilename = null;
    Uri selectedFileURI= null;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_document);
        btnUploadDocument=(Button)findViewById(R.id.btnUploadDocument);

    }


    public void clickFunction(View view) {
        switch (view.getId())
        {
            case R.id.btnUploadDocument:

                file_1 = null;
                attachfileToUpload =null;
                attachfilename = null;

                verifyStoragePermissions1(this);
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("application/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(
                            Intent.createChooser(intent, "Select a File to Upload"),
                            4);
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    public static void verifyStoragePermissions1(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case 4:
            {
                if (resultCode == Activity.RESULT_OK) {

                    Uri fileUri = data.getData();
                    FilePathHelper filePathHelper = new FilePathHelper();
                    String path = "";
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        if (filePathHelper.getPathnew(fileUri, this) != null) {
                            path = filePathHelper.getPathnew(fileUri, this).toLowerCase();
                        } else {
                            path = filePathHelper.getFilePathFromURI(fileUri, this).toLowerCase();
                        }
                    } else {
                        path = filePathHelper.getPath(fileUri, this).toLowerCase();
                    }
                }
                break;
            }

        }
        }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode)
        {
            case 100:
            {
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


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }
}
