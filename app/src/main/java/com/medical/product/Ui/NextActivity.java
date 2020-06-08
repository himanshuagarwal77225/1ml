package com.medical.product.Ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.medical.product.R;
import com.medical.product.adapter.CustomAdapter;

public class NextActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        tv = (TextView) findViewById(R.id.tv);
        for (int i = 0; i < CustomAdapter.imageModelArrayList.size(); i++){
            if(CustomAdapter.imageModelArrayList.get(i).getSelected()) {
                Log.i("sadf","asfd====="+CustomAdapter.imageModelArrayList.get(i).getAnimal());
                tv.setText(tv.getText() + " " + CustomAdapter.imageModelArrayList.get(i).getAnimal());
            }
        }
    }
}