package com.tomrichardson.datacollection.model.summary;

import android.os.Parcel;
import android.os.Parcelable;

import co.uk.rushorm.core.RushObject;

/**
 * Created by tom on 01/03/2016.
 */
public class LocationSummary extends RushObject implements Comparable<LocationSummary>, Parcelable {
  public String name;
  public int freq;
  public double latitude;
  public double longitude;

  public LocationSummary() {
  }

  public LocationSummary(String name, int freq, double latitude, double longitude) {
    this.name = name;
    this.freq = freq;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  @Override
  public String toString() {
    return name + " ("+ latitude + "," + longitude + "): " + freq;
  }

  // Parcelable methods

  protected LocationSummary(Parcel in) {
    name = in.readString();
    freq = in.readInt();
    latitude = in.readDouble();
    longitude = in.readDouble();
  }

  public static final Creator<LocationSummary> CREATOR = new Creator<LocationSummary>() {
    @Override
    public LocationSummary createFromParcel(Parcel in) {
      return new LocationSummary(in);
    }

    @Override
    public LocationSummary[] newArray(int size) {
      return new LocationSummary[size];
    }
  };


  @Override
  public int compareTo(LocationSummary another) {
    return another.freq - this.freq;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
    dest.writeInt(freq);
    dest.writeDouble(latitude);
    dest.writeDouble(longitude);
  }
}
