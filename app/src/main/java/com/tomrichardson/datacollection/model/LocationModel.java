package com.tomrichardson.datacollection.model;

import co.uk.rushorm.core.RushObject;

/**
 * Created by tom on 20/11/2015.
 */
public class LocationModel extends RushObject {
  public double latitude;
  public double longitude;
  public float accuracy;

  public LocationModel(double latitude, double longitude, float accuracy) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.accuracy = accuracy;
  }
}
