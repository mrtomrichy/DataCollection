package com.tomrichardson.datacollection.service.activity;

/**
 * Created by tom on 02/02/2016.
 */
public class ActivityMinTimes {
  public static long IN_VEHICLE = 300000;   // 5 minutes
  public static long WALKING = 0;           // Could walk for 15 seconds if we're that accurate
  public static long ON_BICYCLE = 60000;    // Min 1 minute for cycling
  public static long RUNNING = 5000;        // Min 5 seconds for running
  public static long STILL = 0;             // Could stop for any amount of time
}
