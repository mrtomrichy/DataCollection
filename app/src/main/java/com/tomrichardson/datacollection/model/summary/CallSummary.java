package com.tomrichardson.datacollection.model.summary;

import co.uk.rushorm.core.RushObject;

/**
 * Created by tom on 01/03/2016.
 */
public class CallSummary extends RushObject {
  public int callCount;
  public String mostCalledNumber;
  public String timeSpentOnPhone;

  public CallSummary() {}

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
}
