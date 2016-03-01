package com.tomrichardson.datacollection.model.summary;

import co.uk.rushorm.core.RushObject;

/**
 * Created by tom on 01/03/2016.
 */
public class LocationSummary extends RushObject implements Comparable<LocationSummary> {
  public String name;
  public int freq;

  public LocationSummary() {}

  public LocationSummary(String name, int freq) {
    this.name = name;
    this.freq = freq;
  }

  @Override
  public String toString() {
    return name + ": " + freq;
  }

  @Override
  public int compareTo(LocationSummary another) {
    return another.freq - this.freq;
  }
}
