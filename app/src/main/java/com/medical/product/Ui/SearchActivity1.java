package com.medical.product.Ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.product.Interfaces.OfferServiceApi;
import com.medical.product.R;
import com.medical.product.adapter.SearchAdapter;
import com.medical.product.helpingFile.ApiUtils;
import com.medical.product.models.All;
import com.medical.product.models.PROFILE;
import com.medical.product.usersession.UserSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity1 extends AppCompatActivity {
    private static final String TAG = "SearchActivity1";
    private SearchAdapter searchAdapter;
    CompositeDisposable disposable = new CompositeDisposable();
    String API;
    List<PROFILE> profiles = new ArrayList<>();
    private ProgressDialog dialog;
    UserSession userSession;
    ImageButton imgbtnSearchvoice;
    int REQUEST_CODE = 1;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: started.");
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.search_toolbar);
        imgbtnSearchvoice = (ImageButton) findViewById(R.id.imgbtnSearchvoice);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userSession = new UserSession(SearchActivity1.this);
        HashMap<String, String> hashMap = userSession.getUserApiKey();
        API = hashMap.get("api_key");
        searchView = findViewById(R.id.searchView);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchAdapter.getFilter().filter(newText);
                return false;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait, Fetching response");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        getBestOffer();
        imgbtnSearchvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String language =  "uk-UK";
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,language);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, language);
                intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, language);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice searching...");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }
    private void setUpRecyclerView(List<PROFILE> all) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        searchAdapter = new SearchAdapter(all,this);
       recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(searchAdapter);
    }
    private void getBestOffer(){
        Log.d(TAG, "getBestOffer: started.");
        OfferServiceApi offerServiceApi = ApiUtils.getOfferService();
        offerServiceApi.getAll(API).subscribeOn(Schedulers.io())
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
                            profiles.addAll(all.getMASTERS().getPROFILE());
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
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
                       setUpRecyclerView(profiles);
                    }
                });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // Populate the wordsList with the String values the recognition engine thought it heard
            final ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (!matches.isEmpty()) {
                String Query = matches.get(0);
                searchView.setQuery(Query,true);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
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
