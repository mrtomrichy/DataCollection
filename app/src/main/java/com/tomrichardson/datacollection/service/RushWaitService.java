package com.tomrichardson.datacollection.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.tomrichardson.datacollection.Application;

/**
 * Created by tom on 01/03/2016.
 * Needed because Android recreates Application.class when a new thread is started,
 * thus reinitialising the database. This will make the Service wait before doing anything.
 */
public abstract class RushWaitService extends Service {

  private boolean registered = false;

  private BroadcastReceiver receiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      unregisterReceiver(receiver);
      registered = false;
      run();
    }
  };

  @Override
  public void onCreate() {
    super.onCreate();
    if(!Application.RUSH_INITIALISED) {
      registerReceiver(receiver, new IntentFilter(Application.RUSH_INIT_COMPLETE));
      registered = true;
    } else {
      run();
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if(registered) {
      unregisterReceiver(receiver);
    }
  }

  protected abstract void run();

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}
