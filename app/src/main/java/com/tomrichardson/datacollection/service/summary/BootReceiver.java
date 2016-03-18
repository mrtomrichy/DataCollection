package com.tomrichardson.datacollection.service.summary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tomrichardson.datacollection.Utils;

/**
 * Created by tom on 17/03/2016.
 */
public class BootReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
      Utils.setRepeatingSummaryAlarm(context.getApplicationContext());
    }
  }
}
