package com.medical.product.Ui;

import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.medical.product.R;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.helpingFile.Keystore;

import java.util.StringTokenizer;

public class Deeplink extends AppCompatActivity {
    private Keystore store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deeplink);
        store = Keystore.getInstance(getApplicationContext());
        if (getIntent() != null) {
            Uri data = getIntent().getData();
            String id = data.getLastPathSegment();
            String phone = store.get("phone");
            if (!TextUtils.isEmpty(phone)) {
                Intent intent = new Intent(this, Product_detailActivity.class);
                intent.putExtra("med_id", id);
                intent.putExtra("med_cat", "");
                intent.putExtra("image", "");
                intent.putExtra("image", "");
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, LoginPanelActivity.class);
                startActivity(intent);

            }

            finish();

        }

    }
}
