package com.medical.product.Ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.medical.product.R;
import com.medical.product.adapter.AdapterSearchBrandCateList;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.VolleySingleton;
import com.medical.product.models.GatterGetSearchProductCatBrand;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.medical.product.helpingFile.ReuseMethod.hideStatusbar;

public class SearchProductActivity extends AppCompatActivity {
    private static TextView textNotifycart;
    private Keystore store;
    CircleImageView CimgivThumb;
    RecyclerView recyclevieworders;
    private AdapterSearchBrandCateList recyclerAdapter;

    SearchView searchview;

    ImageButton imgbtnSearchvoice;
    int REQUEST_CODE = 1;

    String strkeyword = "";
    LinearLayout noresult;
    private ArrayList<GatterGetSearchProductCatBrand> gatterSearchProductCatBrand;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        hideStatusbar(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       /// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        componentInitilization();
        loadDataProfile();
    }

    private void loadDataProfile() {
        store = Keystore.getInstance(getApplicationContext());
        //store.get("name")
        gatterSearchProductCatBrand = new ArrayList<>();
        recyclerAdapter = new AdapterSearchBrandCateList(gatterSearchProductCatBrand, this);
        LinearLayoutManager recyclerlayoutManager = new LinearLayoutManager(this);
        recyclevieworders.setLayoutManager(recyclerlayoutManager);
        recyclevieworders.setHasFixedSize(true);
        recyclevieworders.setAdapter(recyclerAdapter);
    }

    private void componentInitilization() {
        recyclevieworders = (RecyclerView) findViewById(R.id.recyclevieworders);
        noresult = (LinearLayout) findViewById(R.id.noresult);
        searchview = (SearchView) findViewById(R.id.searchview);
        imgbtnSearchvoice = (ImageButton) findViewById(R.id.imgbtnSearchvoice);
        searchview.setQueryHint("Type something...");
        searchview.setIconified(false);
        searchview.requestFocus();

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                strkeyword = newText;
                getProductSearch(newText);
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                strkeyword = query;
                getProductSearch(query);

                return false;
            }

        });

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

    public String sendSEarchkey() {


        return strkeyword;

    }

    private void getProductSearch(final String keyword) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiFileuri.ROOT_HTTP_URL + "product/search",
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

                                gatterSearchProductCatBrand.clear();

                                Iterator<String> iter = obj.getJSONObject("product").keys();


                                while (iter.hasNext()) {
                                    String key = iter.next();

                                    JSONArray jsonArray = obj.getJSONObject("product").getJSONArray(key);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject json = null;
                                        json = jsonArray.getJSONObject(i);
                                        gatterSearchProductCatBrand.add(new GatterGetSearchProductCatBrand(
                                                json.getString("id"),
                                                json.getString("name"),
                                                key.toString()
                                        ));
                                        recyclerAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                            recyclerAdapter.notifyDataSetChanged();
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
                params.put("search_keyword", keyword);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // Populate the wordsList with the String values the recognition engine thought it heard
            final ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (!matches.isEmpty()) {
                String Query = matches.get(0);
                strkeyword = Query;
                getProductSearch(Query);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }
}
