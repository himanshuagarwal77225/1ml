package com.medical.product.Ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.product.Interfaces.OfferServiceApi;
import com.medical.product.R;
import com.medical.product.adapter.AdapterFeedbacklist;
import com.medical.product.adapter.Blog_adpater;
import com.medical.product.helpingFile.ApiUtils;
import com.medical.product.models.Artical_model;
import com.medical.product.models.Blog;
import com.medical.product.rest.ApiClient;
import com.medical.product.rest.ApiInterface;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Artical extends AppCompatActivity {

    RecyclerView blog_list;
    ArrayList<Artical_model> list;
    private static final String TAG = "Artical";
    private CompositeDisposable disposable = new CompositeDisposable();
    ProgressDialog dialog;
    Blog_adpater adpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artical);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        getSupportActionBar().setTitle("Articles");

        blog_list = (RecyclerView) findViewById(R.id.blog_list);
        adpater = new Blog_adpater(this);
        blog_list.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        blog_list.setHasFixedSize(true);
        blog_list.setAdapter(adpater);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait, Fetching response");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        getBlog();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(menuItem));
    }
    private void getBlog(){
        ApiClient.changeConvertorToGSON(ApiUtils.BASE_URL_BLOOD);
        ApiInterface apiService = ApiClient.createService(ApiInterface.class, Artical.this);
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
                            adpater.setOffer(blog.getBlogList());
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }else{
                            showAlert("Some error has been occured");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Log.d(TAG, "onError: called"+e.toString());
                        showAlert(e.getMessage().trim());
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
}
