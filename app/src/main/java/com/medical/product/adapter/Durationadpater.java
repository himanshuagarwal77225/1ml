package com.medical.product.adapter;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.medical.product.R;
import com.medical.product.Ui.AddNew_Update_AddressActivity;
import com.medical.product.Ui.MyAddressActivity;
import com.medical.product.Ui.ReminderAddActivity;
import com.medical.product.Utils.Utlity;
import com.medical.product.models.GatterGetAddressListModel;
import com.medical.product.models.Sedulas;

import java.util.ArrayList;
import java.util.Calendar;


public class Durationadpater extends RecyclerView.Adapter<Durationadpater.MyViewHolder> {
    private ArrayList<String> duration; // this data structure carries our title and description
    Context context;
    ReminderAddActivity addActivity;

    public Durationadpater(ReminderAddActivity reminderAddActivity, ArrayList<String> duration, Context context) {
        this.duration = duration;
        this.context = context;
        this.addActivity = reminderAddActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_duration, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // update your data here
        final String duationvalue = duration.get(position);
        holder.duration.setText(duationvalue);

        holder.duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Sedulas sedulas = new Sedulas();
                final int mHour, mMinute;
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR);
                mMinute = c.get(Calendar.MINUTE);
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                String timeSet = "";
                                if (hourOfDay > 12) {
                                    hourOfDay -= 12;
                                    timeSet = "PM";
                                } else if (hourOfDay == 0) {
                                    hourOfDay += 12;
                                    timeSet = "AM";
                                } else if (hourOfDay == 12)
                                    timeSet = "PM";
                                else {
                                    timeSet = "AM";
                                }
                               ;
                                sedulas.setHour(hourOfDay);
                                sedulas.setMin(minute);
                                sedulas.setZone(timeSet);
                                sedulas.setId(String.valueOf(Utlity.getRandomNumber(1, 100)));
                                sedulas.setDay(duationvalue);
                                dose_picker(sedulas);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
    }

    private void dose_picker(final Sedulas sedulas) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.eddit_dose, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setCancelable(true);
        final EditText dose = (EditText) view.findViewById(R.id.dose);
        final Spinner type = (Spinner) view.findViewById(R.id.type);
        Button save = (Button) view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.getSelectedItemPosition() != 0 && !TextUtils.isEmpty(dose.getText().toString())) {
                    alertDialog.dismiss();
                    sedulas.setDose(dose.getText().toString() + " " + type.getSelectedItem());
                    addActivity.set_duration(sedulas);
                } else {
                    if (TextUtils.isEmpty(dose.getText().toString())) {
                        dose.setError("Required filed !");
                    } else {
                        Toast.makeText(context, "Select dose type", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
        alertDialog.setView(view);
        alertDialog.show();
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
        CheckBox duration;

        MyViewHolder(View view) {
            super(view);
            duration = (CheckBox) itemView.findViewById(R.id.duration);


        }

    }

}
