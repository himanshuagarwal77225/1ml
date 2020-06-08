package com.medical.product.Ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.medical.product.R;
import com.medical.product.Utils.Utlity;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class No_internet extends Activity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utlity.full_screen(No_internet.this);
        setContentView(R.layout.activity_no);
    }


    public void finsh(View v) {
        if (Utlity.is_online(this)) {
            startActivity(new Intent(this,Dashbord.class));
            finishAffinity();
        } else {
            Toast.makeText(this, "Still unable connect to internet", Toast.LENGTH_LONG).show();
        }

    }
}
