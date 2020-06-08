package com.medical.product.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.medical.product.R;
import com.medical.product.helpingFile.ReuseMethod;

import java.util.ArrayList;

public class LabTestReport extends AppCompatActivity {
    String flat,address,state,city,pincode,amount;
    TextView finalamount;
    String actualamount,savingamount;
    String report="No";
    ArrayList<String> benName,benAge,benGender;
    Button confirm;
    RadioButton hc_no,hc_yes;
    String finallyamount;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_hard_copy_report);
        final Bundle bundle=getIntent().getExtras();
        flat=bundle.getString("flat");
        address=bundle.getString("address");
        state=bundle.getString("state");
        city=bundle.getString("city");
        pincode=bundle.getString("pincode");
        amount=bundle.getString("totalamount");
        benName=bundle.getStringArrayList("benName");
        benAge=bundle.getStringArrayList("benAge");
        benGender=bundle.getStringArrayList("benGender");
        actualamount=bundle.getString("actualamount");
        savingamount=bundle.getString("savingamount");
        finallyamount=amount;
        finalamount=findViewById(R.id.tv_subTotal);
        finalamount.setText("Total : "+amount+ " Rs/-");
        confirm=findViewById(R.id.btn_Confirm);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(LabTestReport.this,SlotActivity.class);
               Bundle bundle1 = new Bundle();
               bundle1.putString("flat",flat);
                bundle1.putString("address",address);
                bundle1.putString("state",state);
                bundle1.putString("city",city);
                bundle1.putString("pincode",pincode);
                bundle1.putString("totalamount",finallyamount);
                bundle1.putStringArrayList("benName",benName);
                bundle1.putStringArrayList("benAge",benAge);
                bundle1.putStringArrayList("benGender",benGender);
                bundle1.putString("actualamount",actualamount);
                bundle1.putString("savingamount",savingamount);
                bundle1.putString("report",report);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
        hc_no=findViewById(R.id.rd_HC_no);
        hc_yes=findViewById(R.id.rd_HC_yes);
        hc_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hc_no.isChecked()){
                    report="No";
                    setVaue(amount);
                    hc_yes.setChecked(false);
                }

            }
        });
        hc_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hc_yes.isChecked()){
                    report="Yes";
                    setVaue(String.valueOf(Float.parseFloat(amount)+75));
                    hc_no.setChecked(false);
                }

            }
        });

    }
    private void setVaue(String amount1){
        finallyamount=amount1;
        finalamount.setText("Sub-Total : "+amount1+ " Rs/-");
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }
}
