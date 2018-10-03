package com.example.sirjackovich.wguapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class GoalReceiver extends BroadcastReceiver {

  static int notificationID;

  @Override
  public void onReceive(Context context, Intent intent) {

//    NotificationCompat.Builder builder =
//      new NotificationCompat.Builder(this)
//        .setSmallIcon(R.mipmap.ic_launcher)
//        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
//        .setContentTitle("Notifications Example")
//        .setContentText("This is a test notification");
//
//    Intent notificationIntent = new Intent(this, MainActivity.class);
//    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
//      PendingIntent.FLAG_UPDATE_CURRENT);
//    builder.setContentIntent(contentIntent);
//
//    // Add as notification
//    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//    manager.notify(0, builder.build());


    Notification notification = new NotificationCompat.Builder(context)
      .setSmallIcon(R.mipmap.ic_launcher)
      .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
      .setContentText("Assessment Goal Alert")
      .setContentTitle("The time is now! Finish your assessment today.").build();

    NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
    notificationManager.notify(notificationID++, notification);


  }

}
