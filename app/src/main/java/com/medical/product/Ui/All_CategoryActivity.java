package com.medical.product.Ui;

import android.content.Context;
import android.os.Bundle;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medical.product.R;
import com.medical.product.adapter.AdapterReletedProduct;
import com.medical.product.adapter.AdapterSimilarProduct;
import com.medical.product.adapter.HorizontalAdadpater;
import com.medical.product.adapter.HorizontalAllCategoryRecycleViewAdapter;
import com.medical.product.adapter.HorizontalAllbrandsRecycleViewAdapter;
import com.medical.product.adapter.HorizontalCategoryProductAdapter;
import com.medical.product.adapter.HorizontalRecycleViewAdapterBrands;
import com.medical.product.adapter.SpinnerCustomAdapter;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.models.Adf;
import com.medical.product.models.GatterGetReletedProductList;
import com.medical.product.models.GatterGetSimilorList;
import com.medical.product.models.HorizontalModelBrands;
import com.medical.product.models.HorizontalModelProducts;
import com.medical.product.models.HorizontalModelSaveCategory;
import com.medical.product.models.VerticalModel;

import java.util.ArrayList;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.medical.product.helpingFile.ReuseMethod.hideStatusbar;

public class All_CategoryActivity extends AppCompatActivity {

    String[] gendername = {"Gender", "Male", "Female", "Other"};
    int genderimage[] = {1, R.drawable.maleicon, R.drawable.femaleicon, R.drawable.othergender};
    SpinnerCustomAdapter customAdapter;
    Spinner spinnergender;
    private Keystore store;
    RecyclerView recCategory;
    Context context = All_CategoryActivity.this;
    private ArrayList<HorizontalModelSaveCategory> filelist;
    private ArrayList<HorizontalModelBrands> arrayListvertical;
    private ArrayList<HorizontalModelProducts> gatterGetReletedProductLists;
    private ArrayList<Adf> ads;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_category);
        hideStatusbar(this);
        recCategory = (RecyclerView) findViewById(R.id.recCategory);
        ///single  activity manage  all  view  all action  on the base of slug  string
        if (getIntent().getStringExtra("slug").equalsIgnoreCase("brands")) {
            getSupportActionBar().setTitle("All Brands");
            arrayListvertical = new Gson().fromJson(getIntent().getStringExtra("FILES_TO_SEND"), new TypeToken<List<HorizontalModelBrands>>() {
            }.getType());
            recCategory.setLayoutManager(new LinearLayoutManager(this));
            HorizontalAllbrandsRecycleViewAdapter horizontalRecycleViewAdapter = new HorizontalAllbrandsRecycleViewAdapter(context, arrayListvertical);
            recCategory.setAdapter(horizontalRecycleViewAdapter);
        } else if (getIntent().getStringExtra("slug").equalsIgnoreCase("products")) {
            getSupportActionBar().setTitle("All Products");
            gatterGetReletedProductLists = new Gson().fromJson(getIntent().getStringExtra("FILES_TO_SEND"), new TypeToken<List<HorizontalModelProducts>>() {
            }.getType());
            HorizontalCategoryProductAdapter horizontalRecycleViewAdapter = new HorizontalCategoryProductAdapter(All_CategoryActivity.this, gatterGetReletedProductLists);
            LinearLayoutManager recreletedmanager = new LinearLayoutManager(this);
            recCategory.setLayoutManager(recreletedmanager);
            recCategory.setHasFixedSize(true);
            recCategory.setAdapter(horizontalRecycleViewAdapter);
        }
        else if (getIntent().getStringExtra("slug").equalsIgnoreCase("ad")) {
            RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
            getSupportActionBar().setTitle("Sponsor Ad");
            ads = new Gson().fromJson(getIntent().getStringExtra("FILES_TO_SEND"), new TypeToken<List<Adf>>() {
            }.getType());
            HorizontalAdadpater horizontalRecycleViewAdapter = new HorizontalAdadpater(context,ads);
            recCategory.setHasFixedSize(true);
            recCategory.setLayoutManager(new LinearLayoutManager(recCategory.getContext(), LinearLayoutManager.HORIZONTAL, false));
            recCategory.setRecycledViewPool(viewPool);
            recCategory.setItemViewCacheSize(6);
            recCategory.setAdapter(horizontalRecycleViewAdapter);
        }else {
            getSupportActionBar().setTitle("All Category");
            filelist = (ArrayList<HorizontalModelSaveCategory>) getIntent().getSerializableExtra("FILES_TO_SEND");
            recCategory.setLayoutManager(new LinearLayoutManager(this));
            HorizontalAllCategoryRecycleViewAdapter adapter = new HorizontalAllCategoryRecycleViewAdapter(context, filelist);
            recCategory.setAdapter(adapter);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
