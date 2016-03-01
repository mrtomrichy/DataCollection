package com.tomrichardson.datacollection.service.phone;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

import com.tomrichardson.datacollection.Utils;
import com.tomrichardson.datacollection.model.PhoneCallModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class PhoneService {

  private static final String TAG = "PhoneService";

  public static List<PhoneCallModel> getPhoneCallData(Context context, Date today) {
    List<PhoneCallModel> calls = new ArrayList<>();

    ContentResolver cr = context.getContentResolver();
    Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, null, null, null, null);
    int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
    int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
    int date = cursor.getColumnIndex(CallLog.Calls.DATE);
    int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);

    while (cursor.moveToNext()) {
      String phNumber = cursor.getString(number);
      String callType = cursor.getString(type);
      String callDate = cursor.getString(date);
      Date callDayTime = new Date(Long.valueOf(callDate));
      String callDuration = cursor.getString(duration);

      calls.add(new PhoneCallModel(phNumber, Integer.parseInt(callDuration), callDayTime.getTime(), Integer.parseInt(callType)));
    }
    cursor.close();

    Log.d(TAG, calls.size() + "");

    Iterator<PhoneCallModel> it = calls.iterator();

    while(it.hasNext()) {
      PhoneCallModel call = it.next();
      if(!Utils.isSameDay(call.time, today))
        it.remove();
    }

    return calls;
  }
}
