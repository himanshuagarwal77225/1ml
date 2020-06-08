package com.medical.product.Ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.medical.product.Interfaces.OfferServiceApi;
import com.medical.product.R;
import com.medical.product.helpingFile.ApiUtils;
import com.medical.product.models.DocsRequest;
import com.medical.product.models.DocsResponse;

import java.io.File;

import im.delight.android.webview.AdvancedWebView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DocsWebview extends Activity implements AdvancedWebView.Listener {
    AdvancedWebView webView;
    private static final String TAG = "DocsWebview";
    String name,email,user_id,mobile,dob,gender;
    ProgressDialog dialog;
    public Uri imageUri;

    private static final int FILECHOOSER_RESULTCODE   = 2888;
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.docs_webview);
        webView=findViewById(R.id.webView);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait, Fetching response");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        email=bundle.getString("email");
        mobile=bundle.getString("phone");
        user_id=bundle.getString("id");
        dob="19071996";
        gender="male";
        DocsRequest docsRequest = new DocsRequest(user_id,name,mobile,email,dob,gender);
        getData(docsRequest);

    }

    private void getData(DocsRequest docsRequest) {
        OfferServiceApi offerServiceApi = ApiUtils.getDocsUrl();
        offerServiceApi.getDocsUrl(docsRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DocsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                   public void onNext(DocsResponse docsResponse) {
                        webView.setListener(DocsWebview.this, DocsWebview.this);
                        // disable third-party cookies only
                        webView.setWebViewClient(new WebViewClient() {
                                                     @Override
                                                     public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                                         Log.d(TAG, "shouldOverrideUrlLoading: url : " + url);
                                                         view.loadUrl(url);
                                                         return true;
                                                     }
                            @Override
                            public void onPageFinished(WebView view, final String url) {
                               try{
                                    // Close progressDialog
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                }catch(Exception exception){
                                    exception.printStackTrace();
                                }
                            }
                        });
                        webView.loadUrl(docsResponse.getSso());

//                                webView.getSettings().setJavaScriptEnabled(true);
//                        webView.getSettings()
//                                .setJavaScriptCanOpenWindowsAutomatically(true);
//                        webView.getSettings().setDomStorageEnabled(true);
//                        webView.setWebViewClient(new WebViewClient() {
//                            @Override
//                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                                Log.d(TAG, "shouldOverrideUrlLoading: url : "+url);
//                                view.loadUrl(url);
//                                return true;
//                            }
//
//                            @Override
//                            public void onPageFinished(WebView view, final String url) {
//                                Log.d(TAG, "onPageFinished: done");
//                                try{
//                                    // Close progressDialog
//                                    if (dialog.isShowing()) {
//                                        dialog.dismiss();
//                                    }
//                                }catch(Exception exception){
//                                    exception.printStackTrace();
//                                }
//                            }
//                        });
//                        webView.setWebChromeClient(new WebChromeClient() {
//
//                            // openFileChooser for Android 3.0+
//                            @Override
//                            public void onPermissionRequest(final PermissionRequest request) {
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                    request.grant(request.getResources());
//                                }
//                            }
//                            void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType){
//
//                                // Update message
//                                mUploadMessage = uploadMsg;
//
//                                try{
//
//                                    // Create AndroidExampleFolder at sdcard
//
//                                    File imageStorageDir = new File(
//                                            Environment.getExternalStoragePublicDirectory(
//                                                    Environment.DIRECTORY_PICTURES)
//                                            , "1ML");
//
//                                    if (!imageStorageDir.exists()) {
//                                        // Create AndroidExampleFolder at sdcard
//                                        imageStorageDir.mkdirs();
//                                    }
//
//                                    // Create camera captured image file path and name
//                                    File file = new File(
//                                            imageStorageDir + File.separator + "IMG_"
//                                                    + String.valueOf(System.currentTimeMillis())
//                                                    + ".jpg");
//
//                                    mCapturedImageURI = Uri.fromFile(file);
//
//                                    // Camera capture image intent
//                                    final Intent captureIntent = new Intent(
//                                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//
//                                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
//
//                                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                                    i.addCategory(Intent.CATEGORY_OPENABLE);
//                                    i.setType("image/*");
//
//                                    // Create file chooser intent
//                                    Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
//
//                                    // Set camera intent to file chooser
//                                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS
//                                            , new Parcelable[] { captureIntent });
//
//                                    // On select image call onActivityResult method of activity
//                                    startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
//
//                                }
//                                catch(Exception e){
//                                    Toast.makeText(getBaseContext(), "Exception:"+e,
//                                            Toast.LENGTH_LONG).show();
//                                }
//
//                            }
//
//                            // openFileChooser for Android < 3.0
//                            public void openFileChooser(ValueCallback<Uri> uploadMsg){
//                                openFileChooser(uploadMsg, "");
//                            }
//
//                            //openFileChooser for other Android versions
//                            public void openFileChooser(ValueCallback<Uri> uploadMsg,
//                                                        String acceptType,
//                                                        String capture) {
//
//                                openFileChooser(uploadMsg, acceptType);
//                            }
//
//
//
//                            // The webPage has 2 filechoosers and will send a
//                            // console message informing what action to perform,
//                            // taking a photo or updating the file
//
//                            public boolean onConsoleMessage(ConsoleMessage cm) {
//
//                                onConsoleMessage(cm.message(), cm.lineNumber(), cm.sourceId());
//                                return true;
//                            }
//
//                            public void onConsoleMessage(String message, int lineNumber, String sourceID) {
//                                //Log.d("androidruntime", "Show console messages, Used for debugging: " + message);
//
//                            }
//                        });   // End setWebChromeClient
//                        webView.getSettings().setLoadsImagesAutomatically(true);
//                        webView.setHorizontalScrollBarEnabled(false);
//                        webView.getSettings().setLoadWithOverviewMode(true);
//                        webView.setScrollbarFadingEnabled(false);
//                        webView.getSettings().setBuiltInZoomControls(true);
//                        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
//                        webView.getSettings().setAllowFileAccess(true);
//                        webView.getSettings().setSupportZoom(true);
//                        webView.loadUrl(docsResponse.getSso());

                    }

                    @Override
                    public void onError(Throwable e) {
                    dialog.dismiss();
                    showAlert(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        if(dialog.isShowing()){
                            dialog.dismiss();
                        }

                    }
                });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);
        webView.onActivityResult(requestCode, resultCode, intent);
        Log.d(TAG, "onActivityResult: called "+resultCode+" "+requestCode);
//        if (requestCode == FILECHOOSER_RESULTCODE) {
//
//            if (null == this.mUploadMessage) {
//                return;
//
//            }
//
//            Uri result = null;
//
//            try {
//                if (resultCode != RESULT_OK) {
//
//                    result = null;
//
//                } else {
//
//                    // retrieve from the private variable if the intent is null
//                    result = intent == null ? mCapturedImageURI : intent.getData();
//                }
//            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(), "activity :" + e,
//                        Toast.LENGTH_LONG).show();
//            }
//
//            mUploadMessage.onReceiveValue(result);
//            mUploadMessage = null;
//
//        }

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
    public void onBackPressed() {

        if(webView.canGoBack()) {

            webView.goBack();

        } else {
            // Let the system handle the back button
            super.onBackPressed();
        }
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }
}
