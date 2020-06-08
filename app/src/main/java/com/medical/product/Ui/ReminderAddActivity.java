
package com.medical.product.Ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.medical.product.R;
import com.medical.product.Utils.Utlity;
import com.medical.product.adapter.AdapterFeedbacklist;
import com.medical.product.adapter.Durationadpater;
import com.medical.product.adapter.Sedulasadpater;
import com.medical.product.models.Sedulas;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;


public class ReminderAddActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    private ArrayList<String> duratonlist;
    Durationadpater recyclerAdapter;
    RecyclerView duration, routin;
    private TextView medname, dose;
    private TextView time;
    private int mHour, mMinute;
    private String mTime = "";
    private Reminder reminder;
    private AlarmReceiver mAlarmReceiver;
    private Calendar mCalendar;
    private int mReceivedID;
    private String duration_str = "";
    private String foodstr = "";
    private RadioGroup food;
    private ArrayList<Sedulas> sedulas;
    private Sedulasadpater routin_ad;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        // Setup Toolbar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        getSupportActionBar().setTitle(R.string.title_activity_add_reminder);
        //find ids
        find();

    }

    private void find() {

        mCalendar = Calendar.getInstance();

        duration = (RecyclerView) findViewById(R.id.duration);
        routin = (RecyclerView) findViewById(R.id.routin);
        food = (RadioGroup) findViewById(R.id.food);
        medname = (TextView) findViewById(R.id.medname);
        save = (Button) findViewById(R.id.save);

        duratonlist = new ArrayList<>();
        duratonlist.add("Morning");
        duratonlist.add("After non");
        duratonlist.add("Evening");
        recyclerAdapter = new Durationadpater(ReminderAddActivity.this, duratonlist, this);
        duration.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        duration.setHasFixedSize(true);
        duration.setAdapter(recyclerAdapter);


        medname.setOnClickListener(this);
        save.setOnClickListener(this);

        mAlarmReceiver = new AlarmReceiver();
        sedulas = new ArrayList<>();

        food.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                foodstr = ((RadioButton) findViewById(food.getCheckedRadioButtonId()))
                        .getText().toString();
            }
        });


    }

    // On clicking menu buttons
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.medname:
                Intent intent = new Intent(this, Medican_name.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.save:
                if (!TextUtils.isEmpty(medname.getText().toString()) && !TextUtils.isEmpty(foodstr) && sedulas.size() > 0) {
                    reminder = new Reminder(String.valueOf(mReceivedID), medname.getText().toString(), foodstr, true, sedulas);
                    Utlity.save_alarm(this, reminder);
                    for (Sedulas sedulas : sedulas) {
                        String am = sedulas.getZone();
                        if (am.equalsIgnoreCase("am")) {
                            am = "0";
                        } else {
                            am = "1";
                        }
                        mCalendar.set(Calendar.HOUR_OF_DAY, sedulas.getHour());
                        mCalendar.set(Calendar.MINUTE, sedulas.getMin());
                        mCalendar.set(Calendar.SECOND, 0);
                        mCalendar.set(Calendar.AM_PM, Integer.parseInt(am));
                        mAlarmReceiver.setAlarm(getApplicationContext(), mCalendar, Integer.parseInt(sedulas.getId()));
                    }
                    Toast.makeText(this, "Reminder Saved !", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(this, "All value must be fill !", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            medname.setText(data.getStringExtra("name"));
        }

    }

    public void set_duration(Sedulas sedulasss) {
        if (sedulas.size() > 0) {
            for (int i = 0; i < sedulas.size(); i++) {
                if (!is_already(sedulas.get(i).getDay())) {
                    sedulas.add(sedulasss);
                } else {
                    sedulas.add(sedulasss);
                }
                break;
            }
        } else {
            sedulas.add(sedulasss);
        }
        routin_ad = new Sedulasadpater(ReminderAddActivity.this, sedulas, this);
        routin.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        routin.setHasFixedSize(true);
        routin.setAdapter(routin_ad);
    }

    // On clicking Time picker
    public void setTime(View v) {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.setThemeDark(false);
        tpd.show(getSupportFragmentManager(), "Timepickerdialog");
    }

   /* @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        mHour = hourOfDay;
        mMinute = minute;
        if (minute < 10) {
            mTime = hourOfDay + ":" + "0" + minute;
        } else {
            mTime = hourOfDay + ":" + minute;
        }
        time.setText(mTime);
    }*/

    private boolean is_already(String days) {
        boolean is_allready = false;
        for (int i = 0; i < sedulas.size(); i++) {
            if (sedulas.get(i).getDay().equalsIgnoreCase(days)) {
                is_allready = true;
                break;
            }
        }
        return is_allready;
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        mHour = hourOfDay;
        mMinute = minute;
        if (minute < 10) {
            mTime = hourOfDay + ":" + "0" + minute;
        } else {
            mTime = hourOfDay + ":" + minute;
        }
        time.setText(mTime);
    }
}