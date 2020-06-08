package com.medical.product.adapter;

import android.content.Context;
import android.os.Bundle;

import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.medical.product.R;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.ReuseMethod;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.medical.product.helpingFile.ReuseMethod.hideStatusbar;

public class PrescriptionOrderDetailActivity extends AppCompatActivity {

    private Keystore store;
    CircleImageView CimgivThumb;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        hideStatusbar(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    private void loadDataProfile() {
        store = Keystore.getInstance(getApplicationContext());

    }




    public void clickFunction(View view) {
        switch (view.getId())
        {
            case R.id.imgBtnCart:
                ReuseMethod.OpenCartActivity(this);
                break;

        }
    }


    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
