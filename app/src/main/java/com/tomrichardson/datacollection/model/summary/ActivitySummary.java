package com.tomrichardson.datacollection.model.summary;

import com.tomrichardson.datacollection.Utils;

import co.uk.rushorm.core.RushObject;

/**
 * Created by tom on 01/03/2016.
 */
public class ActivitySummary extends RushObject implements Comparable<ActivitySummary> {

  public int activityType;
  public int totalTime;

  public ActivitySummary() {}

  public ActivitySummary(int activityType, int totalTime) {
    this.activityType = activityType;
    this.totalTime = totalTime;
  }

  public String getName() {
    return Utils.getActivityString(this.activityType);
  }

  @Override
  public String toString() {
    return getName() + ": " + totalTime + "s";
  }

  @Override
  public int compareTo(ActivitySummary another) {
    return another.totalTime - this.totalTime;
  }
}
