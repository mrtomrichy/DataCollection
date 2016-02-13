package com.tomrichardson.datacollection.model;

import com.google.android.gms.location.DetectedActivity;
import com.tomrichardson.datacollection.service.activity.ActivityMinTimes;

import co.uk.rushorm.core.RushObject;

/**
 * Created by tom on 06/12/2015.
 */
public class ActivityModel extends RushObject {

  public int activityType;
  public long time;
  public String name; // Adding this for debugging purposes

  public ActivityModel() {}

  public ActivityModel(int type, long time, String name) {
    this.activityType = type;
    this.time = time;
    this.name = name;
  }

  public long getMinTimeForActivity() {
    switch(activityType) {
      case DetectedActivity.IN_VEHICLE: return ActivityMinTimes.IN_VEHICLE;
      case DetectedActivity.ON_BICYCLE: return ActivityMinTimes.ON_BICYCLE;
      case DetectedActivity.WALKING: return ActivityMinTimes.WALKING;
      case DetectedActivity.RUNNING: return ActivityMinTimes.RUNNING;
      case DetectedActivity.STILL: return ActivityMinTimes.STILL;
    }

    return 0;
  }
}
