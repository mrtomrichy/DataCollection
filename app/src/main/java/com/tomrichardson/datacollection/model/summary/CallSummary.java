package com.tomrichardson.datacollection.model.summary;

import android.os.Parcel;
import android.os.Parcelable;

import co.uk.rushorm.core.RushObject;

/**
 * Created by tom on 01/03/2016.
 */
public class CallSummary extends RushObject implements Parcelable {
  public int callCount;
  public String mostCalledNumber;
  public String timeSpentOnPhone;

  public CallSummary() {
  }

  public CallSummary(int callCount, String mostCalledNumber, String timeSpentOnPhone) {
    this.callCount = callCount;
    this.mostCalledNumber = mostCalledNumber;
    this.timeSpentOnPhone = timeSpentOnPhone;
  }

  @Override
  public String toString() {
    return "[totalCalls: " + this.callCount + ", "
        + "mostCalled: " + this.mostCalledNumber + ", "
        + "totalCallTime: " + this.timeSpentOnPhone + "]";
  }

  // Parcelable methods

  protected CallSummary(Parcel in) {
    callCount = in.readInt();
    mostCalledNumber = in.readString();
    timeSpentOnPhone = in.readString();
  }

  public static final Creator<CallSummary> CREATOR = new Creator<CallSummary>() {
    @Override
    public CallSummary createFromParcel(Parcel in) {
      return new CallSummary(in);
    }

    @Override
    public CallSummary[] newArray(int size) {
      return new CallSummary[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(callCount);
    dest.writeString(mostCalledNumber);
    dest.writeString(timeSpentOnPhone);
  }
}
