package com.tomrichardson.datacollection;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.DetectedActivity;
import com.tomrichardson.datacollection.service.summary.AlarmReceiver;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by tom on 01/03/2016.
 */
public class Utils {
  public static final long DAY_LENGTH_MS = 1000 * 60 * 60 * 24;

  public static final String[] MONTH_NAMES = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

  public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
    ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
      if (serviceClass.getName().equals(service.service.getClassName())) {
        return true;
      }
    }
    return false;
  }

  public static Date getTodayDate() {
    Calendar today = Calendar.getInstance();
    today.set(Calendar.MINUTE, 0);
    today.set(Calendar.HOUR_OF_DAY, 0);
    today.set(Calendar.SECOND, 0);

    return today.getTime();
  }

  public static String getDaySuffix(int day) {
    if (day >= 11 && day <= 13) {
      return "th";
    }
    switch (day % 10) {
      case 1:
        return "st";
      case 2:
        return "nd";
      case 3:
        return "rd";
      default:
        return "th";
    }
  }

  public static boolean isSameDay(long time, Date day) {
    return time > day.getTime() && time < day.getTime() + DAY_LENGTH_MS;
  }

  public static String getActivityString(int detectedActivityType) {
    switch (detectedActivityType) {
      case DetectedActivity.IN_VEHICLE:
        return "VEHICLE";
      case DetectedActivity.ON_BICYCLE:
        return "BICYCLE";
      case DetectedActivity.ON_FOOT:
        return "FOOT";
      case DetectedActivity.RUNNING:
        return "RUNNING";
      case DetectedActivity.STILL:
        return "STILL";
      case DetectedActivity.TILTING:
        return "TILTING";
      case DetectedActivity.UNKNOWN:
        return "UNKNOWN";
      case DetectedActivity.WALKING:
        return "WALKING";
      default:
        return "UNIDENTIFIABLE";
    }
  }

  public static void setRepeatingSummaryAlarm(Context appContext) {
    if (PendingIntent.getBroadcast(appContext, 0, new Intent(appContext, AlarmReceiver.class), PendingIntent.FLAG_NO_CREATE) != null) {
      return;
    }

    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    if(calendar.get(Calendar.HOUR_OF_DAY) >= 1) {
      calendar.add(Calendar.DAY_OF_MONTH, 1);
    }
    calendar.set(Calendar.HOUR_OF_DAY, 1);
    Intent intent = new Intent(appContext, AlarmReceiver.class);
    AlarmManager am = (AlarmManager) (appContext.getSystemService(Context.ALARM_SERVICE));

    Log.d("ALARM", "SET ALARM FOR " + calendar.getTime().toString());

    PendingIntent alarmIntent = PendingIntent.getBroadcast(appContext, 0, intent, 0);
    am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
  }
}
