package com.example.sirjackovich.wguapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.example.sirjackovich.wguapp.assessments.AssessmentDetailsActivity;
import com.example.sirjackovich.wguapp.courses.CourseDetailsActivity;

public class NotificationReceiver extends BroadcastReceiver {

  static int notificationID = 0;

  @Override
  public void onReceive(Context context, Intent intent) {
    String action = intent.getStringExtra("type");
    Uri uri;
    Intent newIntent;
    PendingIntent pendingIntent;
    NotificationCompat.Builder builder;
    NotificationManager notificationmanager;

    switch (action) {
      case "Assessment":
        uri = intent.getParcelableExtra("Assessment");
        newIntent = new Intent(context, AssessmentDetailsActivity.class);
        newIntent.putExtra("Assessment", uri);

        pendingIntent = PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder = new NotificationCompat.Builder(context)
          .setSmallIcon(R.mipmap.ic_launcher)
          .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
          .setContentText("Finish your assessment today.")
          .setContentTitle("Assessment Goal Alert")
          .setContentIntent(pendingIntent)
          .setAutoCancel(true);

        notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationmanager.notify(notificationID++, builder.build());
        break;
      case "CourseStart":
        uri = intent.getParcelableExtra("Course");
        newIntent = new Intent(context, CourseDetailsActivity.class);
        newIntent.putExtra("Course", uri);

        pendingIntent = PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder = new NotificationCompat.Builder(context)
          .setSmallIcon(R.mipmap.ic_launcher)
          .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
          .setContentText("Start your course today.")
          .setContentTitle("Course Start Date Alert")
          .setContentIntent(pendingIntent)
          .setAutoCancel(true);

        notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationmanager.notify(notificationID++, builder.build());
        break;
      case "CourseEnd":
        uri = intent.getParcelableExtra("Course");
        newIntent = new Intent(context, CourseDetailsActivity.class);
        newIntent.putExtra("Course", uri);

        pendingIntent = PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder = new NotificationCompat.Builder(context)
          .setSmallIcon(R.mipmap.ic_launcher)
          .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
          .setContentText("Finish your course today.")
          .setContentTitle("Course End Date Alert")
          .setContentIntent(pendingIntent)
          .setAutoCancel(true);

        notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationmanager.notify(notificationID++, builder.build());
        break;
    }


  }
}
