package com.tomrichardson.datacollection.service.phone;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tomrichardson.datacollection.model.PhoneCallModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PhoneService extends Service {

  private static final String TAG = "PhoneService";

  public static void getPhoneCallData(Context context) {
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

    for (PhoneCallModel call : calls) {
      Log.d(TAG, "Phone Number:--- " + call.number + " \nCall Type:--- " + call.getCallType() + " \nCall Date:--- " + call.time + " \nCall duration in sec :--- " + call.duration);
    }
  }

  @Override
  public void onCreate() {
    getPhoneCallData(this);
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}
