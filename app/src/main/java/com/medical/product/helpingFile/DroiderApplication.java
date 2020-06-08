package com.medical.product.helpingFile;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import com.medical.product.R;

import java.util.List;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;


/**
 * Created by dr on 31/03/18.
 * On app-aami
 */

public class DroiderApplication extends Application {
    public static final String TAG = DroiderApplication.class
            .getSimpleName();
    private static DroiderApplication mInstance;
    private static SQLiteDatabase db;
    public static String phone; //09009448112

    public static DroiderApplication getInstance() {
        if (mInstance == null)
            return null;
        return mInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

       /* CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/calibri.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());*/
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/calibri.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

    }

    public void realsememeory() {
        PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(getApplicationContext(), getLauncherActivityName()); // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />
        p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            List<ActivityManager.AppTask> tasks = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tasks = am.getAppTasks();
            }
            if (tasks != null && tasks.size() > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tasks.get(0).setExcludeFromRecents(true);
                }
            }
        }

    }

    private String getLauncherActivityName() {
        String activityName = "";
        final PackageManager pm = getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(getPackageName());
        List<ResolveInfo> activityList = pm.queryIntentActivities(intent, 0);
        if (activityList != null) {
            activityName = activityList.get(0).activityInfo.name;
        }
        return activityName;
    }
}
