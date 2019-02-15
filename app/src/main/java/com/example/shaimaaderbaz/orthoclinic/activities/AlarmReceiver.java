package com.example.shaimaaderbaz.orthoclinic.activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.example.shaimaaderbaz.orthoclinic.R;
import com.example.shaimaaderbaz.orthoclinic.models.Surgery;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = (Bundle) intent.getBundleExtra(AddSurgeryActivity.SURGERY_EXTRA);
        Surgery surgery = (Surgery) bundle.getSerializable(AddSurgeryActivity.SURGERY_EXTRA);


        if (surgery == null) System.out.println("null");
        else if (surgery.getImages()==null)System.out.println("NULL");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("1", "fvf", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);

        }
        Intent intent1 = new Intent(context, SurgeryActivity.class);
        intent1.putExtra(AddSurgeryActivity.SURGERY_EXTRA, bundle);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1")

                .setSmallIcon(R.drawable.logo5)
                .setContentTitle(surgery.getTitle())
                .setDefaults(Notification.DEFAULT_SOUND)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentText("You have a surgical operation after an hour")
                .setContentIntent(PendingIntent.getActivity(context, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT));

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            builder.setPriority(NotificationManager.IMPORTANCE_HIGH);

        notificationManager.notify(1, builder.build());


    }
}
