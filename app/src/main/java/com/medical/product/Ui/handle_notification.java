package com.medical.product.Ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.medical.product.R;

import org.json.JSONException;
import org.json.JSONObject;

public class handle_notification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_notification);
        if (getIntent() != null) {
            String push = getIntent().getStringExtra("data");
            try {
                JSONObject object = new JSONObject(push);
                Intent intent = null;
                int type = object.getInt("type");
                if (type == 0) {
                    intent = new Intent(this, Order_Tab_Activity.class);
                } else {
                    intent = new Intent(this, Dashbord.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
