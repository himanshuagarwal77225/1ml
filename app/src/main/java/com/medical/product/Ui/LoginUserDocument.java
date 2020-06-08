package com.medical.product.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.medical.product.R;

public class LoginUserDocument extends AppCompatActivity {

    LinearLayout btnPrescription, btnLab, btnXray, btnOther, btntrement, btnpre;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_family_document);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("My Health files");

        btnPrescription = (LinearLayout) findViewById(R.id.btnPrescription);
        btnLab = (LinearLayout) findViewById(R.id.btnLab);
        btnXray = (LinearLayout) findViewById(R.id.btnXray);
        btnOther = (LinearLayout) findViewById(R.id.btnOther);

        btntrement = (LinearLayout) findViewById(R.id.btntrement);
        btnpre = (LinearLayout) findViewById(R.id.btnPr);

        btnPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UploadMyDocument.class);
                i.putExtra("documenttype", "3".toString());

                startActivity(i);

            }
        });
        btntrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), UploadMyDocument.class);
                i.putExtra("documenttype", "4".toString());

                startActivity(i);

            }
        });
        btnpre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UploadMyDocument.class);
                i.putExtra("documenttype", "5".toString());

                startActivity(i);


            }
        });

        btnLab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), UploadMyDocument.class);
                i.putExtra("documenttype", "0".toString());

                startActivity(i);

            }
        });


        btnXray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), UploadMyDocument.class);
                i.putExtra("documenttype", "1".toString());

                startActivity(i);


            }
        });


        btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), UploadMyDocument.class);
                i.putExtra("documenttype", "2".toString());

                startActivity(i);


            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
