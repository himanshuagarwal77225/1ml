package com.medical.product.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.medical.product.R;
import com.medical.product.Utils.Utlity;
import com.medical.product.adapter.AdapterFeedbacklist;
import com.medical.product.adapter.Reminder_adpater;
import com.medical.product.models.Sedulas;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;


public class PillReminderActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mList;
    private Toolbar mToolbar;
    private AlarmReceiver mAlarmReceiver;
    private FloatingActionButton add_reminder;
    private TextView no_reminder_text;
    private RecyclerView reminder_list;
    private Reminder_adpater recyclerAdapter;
    private ArrayList<Reminder> reminders;
    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill_reminder);

        // Initialize views
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        add_reminder = (FloatingActionButton) findViewById(R.id.add_reminder);
        no_reminder_text = (TextView) findViewById(R.id.no_reminder_text);
        reminder_list = (RecyclerView) findViewById(R.id.reminder_list);
        // Setup toolbar
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle("Fill  Reminder ");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);


        add_reminder.setOnClickListener(this);

        mAlarmReceiver = new AlarmReceiver();
        mCalendar = Calendar.getInstance();
        ;


        reminders = Utlity.get_remider(this);
        if (reminders.size() == 0) {
            no_reminder_text.setVisibility(View.VISIBLE);
        } else {
            reminders = Utlity.get_remider(this);
            recyclerAdapter = new Reminder_adpater(PillReminderActivity.this, reminders, this);
            LinearLayoutManager recyclerlayoutManager = new LinearLayoutManager(getApplicationContext());
            reminder_list.setLayoutManager(recyclerlayoutManager);
            reminder_list.setHasFixedSize(true);
            reminder_list.setAdapter(recyclerAdapter);
        }


    }

    // Create context menu for long press actions
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_add_reminder, menu);
    }

    // Setup menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // start licenses activity
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_reminder:
                startActivity(new Intent(this, ReminderAddActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        reminders = Utlity.get_remider(this);
        recyclerAdapter = new Reminder_adpater(PillReminderActivity.this, reminders, this);
        LinearLayoutManager recyclerlayoutManager = new LinearLayoutManager(getApplicationContext());
        reminder_list.setLayoutManager(recyclerlayoutManager);
        reminder_list.setHasFixedSize(true);
        reminder_list.setAdapter(recyclerAdapter);

        if (reminders.size() == 0) {
            no_reminder_text.setVisibility(View.VISIBLE);
        } else {
            no_reminder_text.setVisibility(View.GONE);
        }
    }

    public void alaram_off(Reminder reminder, int pos) {
        for (Sedulas sedulas : reminder.getSedulas()) {
            mAlarmReceiver.cancelAlarm(this, Integer.parseInt(sedulas.getId()));
        }
        reminder.setActivie(false);
        reminders.set(pos, reminder);
        Utlity.save_alarm2(this, reminders);
        Toast.makeText(this, "Reminder Off", Toast.LENGTH_LONG).show();
    }


    public void alaram_on(Reminder reminder, int pos) {
        for (Sedulas sedulas : reminder.getSedulas()) {
            String am = sedulas.getZone();
            if (am.equalsIgnoreCase("am")) {
                am = "0";
            } else {
                am = "1";
            }
            mCalendar.set(Calendar.HOUR, sedulas.getHour());
            mCalendar.set(Calendar.MINUTE, sedulas.getMin());
            mCalendar.set(Calendar.SECOND, 0);
            mCalendar.set(Calendar.AM_PM, Integer.parseInt(am));
            mAlarmReceiver.setAlarm(getApplicationContext(), mCalendar, Integer.parseInt(sedulas.getId()));
        }
        reminder.setActivie(true);
        reminders.set(pos, reminder);
        Utlity.save_alarm2(this, reminders);
        Toast.makeText(this, "Reminder On", Toast.LENGTH_LONG).show();
    }
}
