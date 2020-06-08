package com.medical.product.Ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.medical.product.R;
import com.medical.product.helpingFile.ReuseMethod;

import java.util.concurrent.TimeUnit;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.medical.product.helpingFile.ReuseMethod.hideStatusbar;

public class OtpPanelActivity extends AppCompatActivity {
    EditText edtphoneotp;
    Button btnProceed;

    private String mVerificationId;
    TextView txtresendCounter,txtresend;
    String mobno;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_panel);
        hideStatusbar(this);
        ComponentInitialization();

        CountDownResendOtp();
        Intent intent = getIntent();
        mobno=intent.getStringExtra("mobileno");
        //sendVerificationCode(intent.getStringExtra("mobileno"));
    }

    private void CountDownResendOtp() {
        txtresendCounter.setVisibility(View.VISIBLE);
        CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                txtresendCounter.setText("Resend in " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                txtresendCounter.setVisibility(View.GONE);
                txtresend.setVisibility(View.VISIBLE);
                //ReuseMethod.mAuth.signOut();
               // txtresendCounter.setText("done!");
            }
        }.start();
    }

    private void ComponentInitialization() {
        edtphoneotp=(EditText) findViewById(R.id.edtphoneotp);
        btnProceed=(Button) findViewById(R.id.btnProceed);
        txtresend=(TextView) findViewById(R.id.txtresend);
        txtresendCounter=(TextView) findViewById(R.id.txtresendCounter);
    }


    public void clickFunction(View view) {
        switch (view.getId())
        {
            case R.id.btnProceed:
               String strotp= edtphoneotp.getText().toString();

               if(strotp.equals("") || strotp.length()!=6)
               {
                   Toast.makeText(getApplicationContext(),"Incurrect Phone Number",Toast.LENGTH_SHORT).show();
               }
               else{
                   //sendVerificationCode("+918769847255");
//                   verifyVerificationCode(strotp);
               }
                break;

            case R.id.txtresend:
                txtresendCounter.setVisibility(View.VISIBLE);
                txtresend.setVisibility(View.GONE);
                CountDownResendOtp();
                break;
        }
    }





}
