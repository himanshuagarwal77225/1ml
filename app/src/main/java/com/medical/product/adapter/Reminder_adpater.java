package com.medical.product.adapter;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.medical.product.R;
import com.medical.product.Ui.PillReminderActivity;
import com.medical.product.Ui.Reminder;
import com.medical.product.Ui.ReminderAddActivity;

import java.util.ArrayList;


public class Reminder_adpater extends RecyclerView.Adapter<Reminder_adpater.MyViewHolder> {
    private ArrayList<Reminder> duration; // this data structure carries our title and description
    Context context;
    PillReminderActivity addActivity;
    Sedulasadpater2 routin_ad;

    public Reminder_adpater(PillReminderActivity reminderAddActivity, ArrayList<Reminder> duration, Context context) {
        this.duration = duration;
        this.context = context;
        this.addActivity = reminderAddActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_alarm, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // update your data here
        final Reminder reminder = duration.get(position);
        holder.medican.setText(reminder.getMedican_name());
        holder.other.setText(reminder.getFood());

        if (reminder.isActivie()) {
            holder.onoff.setChecked(true);
        } else {
            holder.onoff.setChecked(false);
        }

        routin_ad = new Sedulasadpater2(reminder.getSedulas(), context);
        holder.routing.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.routing.setHasFixedSize(true);
        holder.routing.setAdapter(routin_ad);

        holder.onoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    addActivity.alaram_on(reminder,position);
                } else {
                    addActivity.alaram_off(reminder,position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return duration.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView medican, time, other, other2;
        ToggleButton onoff;
        RecyclerView routing;

        MyViewHolder(View view) {
            super(view);
            medican = (TextView) itemView.findViewById(R.id.medican);
            other = (TextView) itemView.findViewById(R.id.other);
            onoff = (ToggleButton) itemView.findViewById(R.id.onoff);
            routing = (RecyclerView) itemView.findViewById(R.id.routing);


        }

    }

}
