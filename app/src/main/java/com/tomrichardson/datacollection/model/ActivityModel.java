package com.tomrichardson.datacollection.model;

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
}
