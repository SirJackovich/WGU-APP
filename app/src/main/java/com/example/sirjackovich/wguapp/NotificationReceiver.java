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

public class NotificationReceiver extends BroadcastReceiver {

  static int notificationID = 0;

  @Override
  public void onReceive(Context context, Intent intent) {

    String action = intent.getStringExtra("type");
    switch (action) {
      case "Assessment":
        Uri uri = intent.getParcelableExtra("Assessment");
        Intent newIntent = new Intent(context, AssessmentDetailsActivity.class);
        newIntent.putExtra("Assessment", uri);

        PendingIntent pIntent = PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
          .setSmallIcon(R.mipmap.ic_launcher)
          .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
          .setContentText("Finish your assessment today.")
          .setContentTitle("Assessment Goal Alert")
          .setContentIntent(pIntent)
          .setAutoCancel(true);

        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationmanager.notify(notificationID++, builder.build());
    }


  }
}
