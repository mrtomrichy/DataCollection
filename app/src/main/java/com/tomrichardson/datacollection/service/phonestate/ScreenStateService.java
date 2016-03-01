package com.tomrichardson.datacollection.service.phonestate;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tomrichardson.datacollection.model.ScreenStateModel;

public class ScreenStateService extends Service {

  private static final String TAG = "PhoneStateService";

  private BroadcastReceiver screenOnReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      Log.d(TAG, "Screen on");

//      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//        NotificationManager nm = getSystemService(NotificationManager.class);
//
//        StatusBarNotification[] notifications = nm.getActiveNotifications();
//
//        if(notifications.length > 0) Log.d(TAG, "There are active notifications");
//      }

      new ScreenStateModel(System.currentTimeMillis(), ScreenStateModel.SCREEN_ON).save();
      context.sendBroadcast(new Intent(ScreenStateModel.class.getName()));
    }
  };

  private BroadcastReceiver screenOffReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      Log.d(TAG, "Screen off");

      new ScreenStateModel(System.currentTimeMillis(), ScreenStateModel.SCREEN_OFF).save();
      context.sendBroadcast(new Intent(ScreenStateModel.class.getName()));
    }
  };

  @Override
  public void onCreate() {
    super.onCreate();

    registerReceiver(screenOnReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
    registerReceiver(screenOffReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    unregisterReceiver(screenOnReceiver);
    unregisterReceiver(screenOffReceiver);

  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}
