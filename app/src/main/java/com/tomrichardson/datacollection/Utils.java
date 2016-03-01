package com.tomrichardson.datacollection;

import com.google.android.gms.location.DetectedActivity;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by tom on 01/03/2016.
 */
public class Utils {
  public static final long DAY_LENGTH_MS = 1000 * 60 * 60 * 24;

  public static Date getTodayDate() {
    Calendar today = Calendar.getInstance();
    today.set(Calendar.MINUTE, 0);
    today.set(Calendar.HOUR_OF_DAY, 0);
    today.set(Calendar.SECOND, 0);

    return today.getTime();
  }

  public static boolean isSameDay(long time, Date day) {
    return time > day.getTime() && time < day.getTime()+DAY_LENGTH_MS;
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
}
