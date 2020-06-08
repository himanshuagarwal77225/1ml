package com.medical.product.Ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medical.product.R;
import com.medical.product.adapter.SpinnerCustomAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.medical.product.helpingFile.ReuseMethod.hideStatusbar;

public class ReferAndEarnActivity extends AppCompatActivity {
ImageButton imgBtnWhatsup,imgBtnShare;
    private static final int REQUEST_INVITE = 0;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referandearn);
        hideStatusbar(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

     /*   final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        ComponentInitialization();
    }

    private void ComponentInitialization() {
        imgBtnWhatsup=(ImageButton) findViewById(R.id.imgBtnWhatsup);
        imgBtnShare=(ImageButton) findViewById(R.id.imgBtnShare);
    }



    public void clickFunction(View view) {
        switch (view.getId())
        {
            case R.id.imgBtnWhatsup:
                sendWhatsAppMsg();
                break;
            case R.id.imgBtnShare:
               onInviteUsingShare();
                break;
        }
    }

    private void onInviteUsingShare() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            String strShareMessage = "\n Let me recommend you this application\n my refral code is AB1234 \n";
            strShareMessage = strShareMessage + "https://play.google.com/store/apps/details?id=" + getPackageName();
            // Uri screenshotUri = Uri.parse("android.resource://"+getPackageName()+"/drawable/googleicon");
            // i.setType("image/png");
            // i.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            i.putExtra(Intent.EXTRA_TEXT, strShareMessage);
            startActivity(Intent.createChooser(i, strShareMessage));
        } catch(Exception e) {
            //e.toString();
        }
    }


    public void sendWhatsAppMsg() {

        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.setPackage("com.whatsapp");
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            String strShareMessage = "\nLet me recommend you this application\n my refral code is AB1234\n";
            strShareMessage = strShareMessage + "https://play.google.com/store/apps/details?id=" + getPackageName();
           // Uri screenshotUri = Uri.parse("android.resource://"+getPackageName()+"/drawable/googleicon");
           // i.setType("image/png");
           // i.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            i.putExtra(Intent.EXTRA_TEXT, strShareMessage);
            startActivity(Intent.createChooser(i, strShareMessage));
        } catch(Exception e) {
            //e.toString();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       // Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

      if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                //String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // [START_EXCLUDE]
                showMessage(getString(R.string.send_failed));
                // [END_EXCLUDE]
            }
        }
    }
    private void showMessage(String msg) {
      /*  ViewGroup container = (ViewGroup) findViewById(R.id.snackbar_layout);
        Snackbar.make(container, msg, Snackbar.LENGTH_SHORT).show();*/

      Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }


}
