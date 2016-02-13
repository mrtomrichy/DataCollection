package com.tomrichardson.datacollection.model;

import android.provider.CallLog;

/**
 * Created by tom on 03/02/2016.
 */
public class PhoneCallModel {
  public String number;
  public int duration;
  public long time;
  public int type;

  public PhoneCallModel(String number, int duration, long time, int type) {
    this.number = number;
    this.duration = duration;
    this.time = time;
    this.type = type;
  }

  public String getCallType() {
    switch (this.type) {
      case CallLog.Calls.OUTGOING_TYPE:
        return "OUTGOING";

      case CallLog.Calls.INCOMING_TYPE:
        return "INCOMING";

      case CallLog.Calls.MISSED_TYPE:
        return "MISSED";
    }

    return "UNKNOWN";
  }
}
