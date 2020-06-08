package com.medical.product.Ui;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.medical.product.R;
import com.medical.product.Utils.Utlity;
import com.medical.product.models.Sedulas;

import java.util.ArrayList;
import java.util.Calendar;


public class Reminder_view extends AppCompatActivity implements View.OnClickListener {
    ArrayList<Reminder> reminders;
    Reminder reminderdata;
    Sedulas sedulasdata;
    TextView medicanname, dose, food;
    Button btnstop;
    Ringtone ringtoneAlarm;
    String alaramid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reminder_view);

        KeyguardManager.KeyguardLock keyguardLock = keyguardLock = ((KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE)).newKeyguardLock("TAG");

        keyguardLock.disableKeyguard(); // to unlock the device
        keyguardLock.reenableKeyguard();

        wakeDevice();


        Uri alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtoneAlarm = RingtoneManager.getRingtone(this, alarmTone);


        medicanname = (TextView) findViewById(R.id.medicanname);
        food = (TextView) findViewById(R.id.food);
        dose = (TextView) findViewById(R.id.dose);
        btnstop = (Button) findViewById(R.id.btnstop);
        btnstop.setOnClickListener(this);
        reminders = Utlity.get_remider(this);
        if (getIntent() != null) {
            alaramid = getIntent().getStringExtra("id");
        }

        //alaram  db management

        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR);
        int mint = rightNow.get(Calendar.MINUTE);
        ArrayList<Reminder> list = Utlity.get_remider(this);
        for (int i = 0; i < list.size(); i++) {
            boolean is_find = false;
            for (Sedulas sedulas1 : list.get(i).getSedulas()) {
                if (sedulas1.getHour() == hour && sedulas1.getMin() == mint) {
                    sedulasdata = sedulas1;
                    is_find = true;
                    break;
                }
            }
            if (is_find) {
                reminderdata = list.get(i);
                medicanname.setText(reminderdata.getMedican_name());
                food.setText(reminderdata.getFood());
                dose.setText(sedulasdata.getDose());
                if (ringtoneAlarm != null) {
                    ringtoneAlarm.play();
                }
                break;
            }

        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnstop:
                if (ringtoneAlarm != null && ringtoneAlarm.isPlaying()) {
                    ringtoneAlarm.stop();
                }
                finish();
                break;
        }
    }

    public void wakeDevice() {
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();

        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();
        runOnUiThread(new Runnable() {
            public void run() {
                getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            }
        });
    }

}
