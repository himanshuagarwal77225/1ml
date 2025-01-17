package com.medical.product.Ui;

import android.annotation.SuppressLint;
import android.app.Application;

/**
 * Created by Rahul Hooda on 14/7/17.
 */
@SuppressLint("Registered")
public class BaseApplication extends Application {

    AppEnvironment appEnvironment;

    @Override
    public void onCreate() {
        super.onCreate();
        appEnvironment = AppEnvironment.SANDBOX;
    }

    public AppEnvironment getAppEnvironment() {
        return appEnvironment;
    }

    public void setAppEnvironment(AppEnvironment appEnvironment) {
        this.appEnvironment = appEnvironment;
    }
}
