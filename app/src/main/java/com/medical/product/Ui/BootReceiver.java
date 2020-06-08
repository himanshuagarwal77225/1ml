/*
 * Copyright 2015 Blanyal D'Souza.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.medical.product.Ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.medical.product.Utils.Utlity;
import com.medical.product.models.Sedulas;

import java.util.Calendar;
import java.util.List;


public class BootReceiver extends BroadcastReceiver {
    private Calendar mCalendar;
    private AlarmReceiver mAlarmReceiver;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            mCalendar = Calendar.getInstance();
            mAlarmReceiver = new AlarmReceiver();
            for (Reminder reminder : Utlity.get_remider(context)) {
                for (Sedulas sedulas : reminder.getSedulas()) {
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
                    mAlarmReceiver.setAlarm(context, mCalendar, Integer.parseInt(sedulas.getId()));
                }
            }

        }
    }
}