package com.tomrichardson.datacollection.service.summary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tomrichardson.datacollection.Utils;

import java.util.Calendar;

/**
 * Created by tom on 17/03/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    Log.d("AlarmReceiver", "RECEIVED");
    Calendar c = Calendar.getInstance();
    c.setTimeInMillis(Utils.getTodayDate().getTime());
    c.add(Calendar.DAY_OF_MONTH, -1);

    SummaryService.summarise(context, c.getTime());
  }
}
