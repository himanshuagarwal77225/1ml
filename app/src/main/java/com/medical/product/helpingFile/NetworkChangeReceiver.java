package com.medical.product.helpingFile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import com.medical.product.Ui.No_internet;
import com.medical.product.Utils.Utlity;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())
                && WifiManager.WIFI_STATE_DISABLING == wifiState) {
            context.startActivity(new Intent(context, No_internet.class));
        } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            if (!Utlity.is_online(context)) {
                context.startActivity(new Intent(context, No_internet.class));
            }

        }

    }


}