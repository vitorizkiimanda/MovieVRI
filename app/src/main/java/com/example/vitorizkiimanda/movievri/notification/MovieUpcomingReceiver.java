package com.example.vitorizkiimanda.movievri.notification;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;


import com.example.vitorizkiimanda.movievri.R;
import com.example.vitorizkiimanda.movievri.activity.MovieDetail;
import com.example.vitorizkiimanda.movievri.model.MovieItems;

import java.util.Calendar;
import java.util.List;


public class MovieUpcomingReceiver extends BroadcastReceiver {
    private static int notifId = 2000;

    @Override
    public void onReceive(Context context, Intent intent) {
        String movieTitle = intent.getStringExtra("movietitle");
        int id = intent.getIntExtra("id", 0);

        MovieItems movieResult = new MovieItems();
        String desc = context.getString(R.string.release_today_msg) + " " +movieTitle;

        sendNotification(context, context.getString(R.string.app_name), desc, id, movieResult);

    }
    private void sendNotification(Context context, String title, String desc, int id, MovieItems movieResult) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
//        Intent intent = new Intent(context, MovieDetail.class);
//        intent.putExtra("model", movieResult);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri uriTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(title)
                .setContentText(desc)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(uriTone);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("11011",
                    "NOTIFICATION_CHANNEL_NAME", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.YELLOW);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            builder.setChannelId("11011");
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(id, builder.build());
    }

    public void setAlarm(Context context, List<MovieItems> movieResults) {
        int delay = 0;

        for (MovieItems movie : movieResults) {
            cancelAlarm(context);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, MovieUpcomingReceiver.class);
            intent.putExtra("movietitle", movie.getTitle());
            intent.putExtra("movieid", movie.getId());
            intent.putExtra("movieposter", movie.getPosterUrl());
            intent.putExtra("moviedescription", movie.getDescription());
            intent.putExtra("moviedate", movie.getYear());
            intent.putExtra("id", notifId);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                alarmManager.setInexactRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis() + delay,
                        AlarmManager.INTERVAL_DAY,
                        pendingIntent
                );
            } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis() + delay, pendingIntent);
            }
            notifId += 1;
            delay += 3000;
            Log.v("title", movie.getTitle());
        }

        Toast.makeText(context, "Upcoming Notification ON", Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getPendingIntent(context));

        Toast.makeText(context, "Upcoming Notification OFF", Toast.LENGTH_SHORT).show();
    }
    private static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, MovieUpcomingReceiver.class);
        return PendingIntent.getBroadcast(context, 1011, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }



}
