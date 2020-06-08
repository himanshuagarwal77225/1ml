package com.medical.product.Ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.medical.product.R;
import com.medical.product.helpingFile.ConnectDetector;
import com.medical.product.helpingFile.Keystore;
import com.medical.product.helpingFile.OneMLUserSpace;

import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MainActivity extends Activity {
    private Keystore store;
    String phone = "";
    Thread thread;
    FirebaseAuth firebaseAuth;
    ConnectDetector connectDetector;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        connectDetector = new ConnectDetector(getApplicationContext());
        thread = new Thread() {
            public void run() {
                store = Keystore.getInstance(getApplicationContext());
                phone = store.get("phone");
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                   /* if (!TextUtils.isEmpty(phone) && connectDetector.isConnectToInternet() && currentUser!=null) {
                            Intent i = new Intent(getApplicationContext(), Dashbord.class);
                            startActivity(i);
                    } else {
                        Intent i = new Intent(getApplicationContext(), LoginPanelActivity.class);
                        startActivity(i);
                    }
                    finish();*/
                    if (OneMLUserSpace.getInstance(MainActivity.this) != null && OneMLUserSpace.getInstance(MainActivity.this).getLogInStatus() > 0) {
                        Intent dashboardIntent = new Intent(MainActivity.this, Dashbord.class);
                        if (getIntent() != null && getIntent().getExtras() != null) {
                            dashboardIntent.putExtras(getIntent());
                        }
                        dashboardIntent.addFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(dashboardIntent);
                        finish();
                    } else {
                        Intent loginIntent = new Intent(MainActivity.this, LoginPanelActivity.class);
                        loginIntent.addFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(loginIntent);
                        finish();
                    }
                }

            }
        };
        thread.start();
    }


    public void clickFunction(View view) {
    }
}
