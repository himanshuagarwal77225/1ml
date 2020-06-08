package com.medical.product.helpingFile;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.medical.product.R;
import com.medical.product.Ui.Dashbord;
import com.medical.product.Ui.LoginPanelActivity;
import com.medical.product.Ui.MyNotificationActivity;
import com.medical.product.Ui.Order_Tab_Activity;
import com.medical.product.Ui.Precption_requested_order;
import com.medical.product.Ui.ReferAndEarnActivity;
import com.medical.product.Utils.Utlity;

import org.json.JSONException;
import org.json.JSONObject;


public class FCMMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCMMessagingService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            JSONObject obj = null;
            String data = remoteMessage.getData().toString();
            try {
                obj = new JSONObject(data);
                JSONObject object = obj.getJSONObject("message");
                sendNotificationMultiArray(getApplicationContext(), object.getString("title"), object.getString("body"), obj.getInt("type"), obj.getString("ex_data"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // handleMessage( remoteMessage);
        }

//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//
//            sendNotificationMultiArray(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), 50);
//
//            //Calling method to generate notification
//            //sendNotification(remoteMessage.getNotification().getBody());
//        }


 /*       Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        handleMessage(remoteMessage);*/
    }

    private void handleMessage(RemoteMessage remoteMessage) {
        sendNotification(getBaseContext(), remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
    }

    private void sendNotification(Context context, String title, String messageBody) {
        Intent intent = new Intent(this, ReferAndEarnActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = context.getString(R.string.default_notification_channel_id);
            NotificationChannel channel = new NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(messageBody);
            notificationManager.createNotificationChannel(channel);
            notificationBuilder.setChannelId(channelId);
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


    private void sendNotificationMultiArray(Context context, String title, String messageBody, int type, String extra) {
        Intent intent = null;
        if (type == 0) {
            intent = new Intent(this, Order_Tab_Activity.class);
        } else if (type == 3) {
            intent = new Intent(this, MyNotificationActivity.class);
        } else if (type == 2) {
            intent = new Intent(this, Precption_requested_order.class);
        } else if (type == 4) {
            Keystore store = Keystore.getInstance(getApplicationContext());
            ReuseMethod.SharedPrefranceGetCartValueClear(getApplicationContext());
            store.clear();
            Utlity.clear_reminder(context);
            Utlity.clear_address(context);
            intent = new Intent(this, LoginPanelActivity.class);
            context.startActivity(intent);
        } else if (type == 5) {
            intent = new Intent(this, LoginPanelActivity.class);
        } else {
            intent = new Intent(this, Dashbord.class);
        }
        Utlity.save_notification(context);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "YOUR_CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(messageBody)// message for notification
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(500);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mBuilder.setChannelId(channel.getId());
            mNotificationManager.createNotificationChannel(channel);
        }
        // clear notification after click
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }

}