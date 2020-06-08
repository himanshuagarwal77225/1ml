package com.medical.product.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.medical.product.R;


public class Thankyouorder extends AppCompatActivity implements View.OnClickListener {

    TextView orderid;
    Button track;
    ImageView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_thankyouorder);
        orderid = (TextView) findViewById(R.id.orderid);
        track = (Button) findViewById(R.id.track);
        close = (ImageView) findViewById(R.id.close);
        track.setOnClickListener(this);
        close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.track:
                startActivity(new Intent(Thankyouorder.this, Order_Tab_Activity.class));
                finish();
                break;
            case R.id.close:
                startActivity(new Intent(Thankyouorder.this, Dashbord.class));
                finishAffinity();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Thankyouorder.this, Dashbord.class));
        finishAffinity();
    }
}
