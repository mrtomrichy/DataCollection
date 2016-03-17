package com.tomrichardson.datacollection.model.summary;

import android.os.Parcel;
import android.os.Parcelable;

import com.tomrichardson.datacollection.Utils;

import co.uk.rushorm.core.RushObject;

/**
 * Created by tom on 01/03/2016.
 */
public class ActivitySummary extends RushObject implements Comparable<ActivitySummary>, Parcelable {

  public int activityType;
  public int totalTime;

  public ActivitySummary() {
  }

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

  // Parcelable methods

  protected ActivitySummary(Parcel in) {
    activityType = in.readInt();
    totalTime = in.readInt();
  }

  public static final Creator<ActivitySummary> CREATOR = new Creator<ActivitySummary>() {
    @Override
    public ActivitySummary createFromParcel(Parcel in) {
      return new ActivitySummary(in);
    }

    @Override
    public ActivitySummary[] newArray(int size) {
      return new ActivitySummary[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(activityType);
    dest.writeInt(totalTime);
  }
}
