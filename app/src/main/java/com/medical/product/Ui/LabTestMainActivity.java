package com.medical.product.Ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.medical.product.BuildConfig;
import com.medical.product.Interfaces.OfferServiceApi;
import com.medical.product.R;
import com.medical.product.Utils.Utlity;
import com.medical.product.adapter.BlogAdapter;
import com.medical.product.adapter.RecyclerViewAdapterBestOffer;
import com.medical.product.adapter.RecyclerViewAdapterMostBooked;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.ApiUtils;
import com.medical.product.helpingFile.FileCompressor;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.models.All;
import com.medical.product.models.Blog;
import com.medical.product.models.PROFILE;
import com.medical.product.rest.ApiClient;
import com.medical.product.rest.ApiInterface;
import com.medical.product.usersession.UserSession;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class LabTestMainActivity extends AppCompatActivity {

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY_PHOTO = 2;
    File mPhotoFile;
    FileCompressor mCompressor;
    private static final String TAG = "LabTestMainActivity";
    private ProgressDialog dialog;
    private Button bookLabTest;
    private TextView view_all_most_booked,view_all_best_offer,latest_articles_view_all;
    LinearLayout search;
    private String API_KEY = "no";
    private UserSession userSession;
    private Toolbar toolbar;
    private Button availability;
    private Keystore store;
    private CompositeDisposable disposable = new CompositeDisposable();
    //recyclerview
    LinearLayout background;
    TextView notification;
    private RecyclerView rv_most_booked,rv_best_offer,blog_rv;
    private RecyclerViewAdapterMostBooked rv_most_booked_adapter;
    private RecyclerViewAdapterBestOffer rv_best_offer_adapter;
    private BlogAdapter blogAdapter;
    ImageButton imgBtnNotif;
    ImageButton imgBtnCart;
    TextView textView;
//    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: started.");
        setContentView(R.layout.activity_labtest);

        mCompressor=new FileCompressor(this);
        userSession = new UserSession(LabTestMainActivity.this);
        userSession.createLoginSession("oKtp1d0l0SMZ9FtdQmwP0agrI39tKJATf0unh8fpQNYcKLWvXyFzEw==");
        store = Keystore.getInstance(getApplicationContext());
        availability= findViewById(R.id.availabilty_check_btn);
        search=findViewById(R.id.search);
        background=findViewById(R.id.background);
//        back=findViewById(R.id.back);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LabTestMainActivity.this,SearchActivity1.class);
                startActivity(intent);
            }
        });
        imgBtnNotif = (ImageButton) findViewById(R.id.imgBtnNotif);
        notification = (TextView) findViewById(R.id.notification);
        if (!TextUtils.isEmpty(Utlity.get_notification(this))) {
            notification.setText(Utlity.get_notification(this));
        }
        imgBtnNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyNotificationActivity.class));
            }
        });
        view_all_most_booked=findViewById(R.id.most_booked_view_all);
        view_all_best_offer=findViewById(R.id.best_offer_view_all);
        latest_articles_view_all=findViewById(R.id.latest_articles_view_all);
        view_all_most_booked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewall = new Intent(LabTestMainActivity.this,ViewAll.class);
                viewall.putExtra("toolbar","MOST BOOKED");
                startActivity(viewall);
            }
        });
        view_all_best_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewall = new Intent(LabTestMainActivity.this,ViewAll.class);
                viewall.putExtra("toolbar","BEST OFFER");
                startActivity(viewall);
            }
        });
        latest_articles_view_all.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Artical.class));
        });
        bookLabTest=findViewById(R.id.book_lab_test);
        bookLabTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UploadPrescriptionActivity.class).putExtra("type",2));
            }
        });
        availability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LabTestMainActivity.this,Availability.class);
                startActivity(intent);
            }
        });
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imgBtnCart=findViewById(R.id.imgBtnCart);
        textView = (TextView) findViewById(R.id.textNotify);
        imgBtnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LabTestMainActivity.this,LabCartActivity.class);
                startActivity(intent);
            }
        });
        HashMap<String, String> hashMap = userSession.getUserApiKey();
        API_KEY = hashMap.get("api_key");
        store.put("api_key",API_KEY);
        rv_most_booked = findViewById(R.id.most_booked_rv);
        rv_best_offer=findViewById(R.id.best_offer_rv);
        blog_rv=findViewById(R.id.blog_rv);
        rv_most_booked_adapter = new RecyclerViewAdapterMostBooked(this);
        rv_best_offer_adapter=new RecyclerViewAdapterBestOffer(this);
        blogAdapter=new BlogAdapter(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait, Fetching response");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        initRecyclerView();
        setvaluecart();
        getBestOffer();
        getBlog();
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

    }

    // Activity's overrided method used to perform click events on menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar Item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        // Display menu Item's title by using a Toast.
        if (id == R.id.cart) {
            Toast.makeText(getApplicationContext(), "Cart pressed", Toast.LENGTH_SHORT).show();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }
   private void getBlog(){
       ApiClient.changeConvertorToGSON(ApiUtils.BASE_URL_BLOOD);
       ApiInterface apiService = ApiClient.createService(ApiInterface.class, LabTestMainActivity.this);

       // OfferServiceApi offerServiceApi =ApiUtils.getBlog();
       // offerServiceApi.getBlog()
       apiService.getBlog()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Blog>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(Blog blog) {
                    if(blog.getStatus()){
                        blogAdapter.setOffer(blog.getBlogList());
                    }else{
                        showAlert("Some error has been occured");
                    }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "onError: called"+e.toString());
                        showAlert(e.getMessage().trim());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: called");
                    }
                });
   }
    private void getBestOffer(){
        Log.d(TAG, "getBestOffer: started.");
        OfferServiceApi offerServiceApi = ApiUtils.getOfferService();
        offerServiceApi.getAll(API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<All>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: called");
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(All all) {
                        Log.d(TAG, "onNext: called.");
                        if(all.getRESPONSE().equals("SUCCESS")){
                            rv_most_booked_adapter.setOffer(all.getMASTERS().getPROFILE());
                            rv_best_offer_adapter.setOffer(all.getMASTERS().getPROFILE());
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }else{
                            showAlert("Wrong api key");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "onError: called"+e.toString());
                        showAlert(e.getMessage().trim());
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: called");
                    }
                });
    }
    private void showAlert(String msg){
        try {
            new AlertDialog.Builder(this)
                    .setMessage(msg)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .setTitle("Alert")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        disposable.clear();
    }
    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        rv_most_booked.setAdapter(rv_most_booked_adapter);
        rv_best_offer.setAdapter(rv_best_offer_adapter);
        blog_rv.setAdapter(blogAdapter);
        rv_most_booked.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        rv_most_booked.setHasFixedSize(true);
        rv_best_offer.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        rv_best_offer.setHasFixedSize(true);
        blog_rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        blog_rv.setHasFixedSize(true);
    }

    private void sendImageTo1ml(Uri imageuri){

        if(whatsappInstalledOrNot()) {
            Log.d(TAG, "sendImageTo1ml: called");
            try {
                String toNumber = "+919999626489"; // contains spaces.
                toNumber = toNumber.replace("+", "").replace(" ", "");
                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.putExtra(Intent.EXTRA_STREAM, imageuri);
                sendIntent.putExtra("jid", toNumber + "@s.whatsapp.net");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Prescription id = {oKtp1d0l0SMZ9FtdQmwP0agrI39tKJATf0unh8fpQNYcKLWvXyFzEw==}");
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setPackage("com.whatsapp");
                sendIntent.setType("*/*");
                startActivity(sendIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{

            sendEmail(imageuri);
        }
    }
    private boolean whatsappInstalledOrNot() {
        Log.d(TAG, "whatsappInstalledOrNot: called");
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
        Log.d(TAG, "savecontact: called");
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
        Log.d(TAG, "sendEmail: called path : " + "file:"+uri);
        Log.i("Send email", "order.recive@1ml.co.in");
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"order.recive@1ml.co.in"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Prescription");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Prescription id = {oKtp1d0l0SMZ9FtdQmwP0agrI39tKJATf0unh8fpQNYcKLWvXyFzEw==}");
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:"+uri));
        try {
            startActivity(Intent.createChooser(emailIntent, "Send Presciption"));
            finish();

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(LabTestMainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(LabTestMainActivity.this);
        builder.setTitle("Select");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Take Photo")) {
                    requestStoragePermission(true);
                } else if (items[which].equals("Choose from Gallery")) {
                    requestStoragePermission(false);
                } else if (items[which].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /**
     * Capture image from camera
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);

                mPhotoFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

            }
        }
    }


    /**
     * Select image fro gallery
     */
    private void dispatchGalleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, REQUEST_GALLERY_PHOTO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                try {

                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                    sendImageTo1ml(Uri.parse(mPhotoFile.getAbsolutePath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_GALLERY_PHOTO) {
                Uri selectedImage = data.getData();
                try {
                    mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                    sendImageTo1ml(Uri.parse(mPhotoFile.getPath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Requesting multiple permissions (storage and camera) at once
     * This uses multiple permission model from dexter
     * On permanent denial opens settings dialog
     */
    private void requestStoragePermission(final boolean isCamera) {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_CONTACTS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if (isCamera) {
                                dispatchTakePictureIntent();
                            } else {
                                dispatchGalleryIntent();
                            }
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {
                Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
            }
        })
                .onSameThread()
                .check();
    }


    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    /**
     * Create file with current timestamp name
     *
     * @return  e
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".png", storageDir);
        return mFile;
    }

    /**
     * Get real file path from URI
     *
     * @param contentUri
     * @return
     */
    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    public void setvaluecart() {
        String value = ReuseMethod.GetTotalLabCartItem(getApplicationContext());
        if (!(value.equals("null") ||
                value.equals(""))) {
            textView.setText(value);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        setvaluecart();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

    // Replace current Fragment with the destination Fragment.

}

