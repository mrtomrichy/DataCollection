package com.tomrichardson.datacollection.service.location;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

public class LocationService extends Service {
  public LocationService() {
  }

  private static final String TAG = "LOCATIONSERVICE";
  private LocationManager mLocationManager = null;
  private static final int LOCATION_INTERVAL = 10 * 60 * 1000;
  private static final float LOCATION_DISTANCE = 10f;

  LocationListener[] mLocationListeners;

  @Override
  public IBinder onBind(Intent arg0) {
    return null;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.e(TAG, "onStartCommand");
    super.onStartCommand(intent, flags, startId);
    return START_STICKY;
  }

  @Override
  public void onCreate() {
    Log.e(TAG, "onCreate");

    mLocationListeners = new LocationListener[]{
        new LocationListener(LocationManager.GPS_PROVIDER, getApplicationContext()),
        new LocationListener(LocationManager.NETWORK_PROVIDER, getApplicationContext())
    };

    initializeLocationManager();
    try {
      mLocationManager.requestLocationUpdates(
          LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
          mLocationListeners[1]);
    } catch (java.lang.SecurityException ex) {
      Log.i(TAG, "fail to request location update, ignore", ex);
    } catch (IllegalArgumentException ex) {
      Log.d(TAG, "network provider does not exist, " + ex.getMessage());
    }
    try {
      mLocationManager.requestLocationUpdates(
          LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
          mLocationListeners[0]);
    } catch (java.lang.SecurityException ex) {
      Log.i(TAG, "fail to request location update, ignore", ex);
    } catch (IllegalArgumentException ex) {
      Log.d(TAG, "gps provider does not exist " + ex.getMessage());
    }
  }

  @Override
  public void onDestroy() {
    Log.e(TAG, "onDestroy");
    super.onDestroy();

    if (mLocationManager != null) {
      for (int i = 0; i < mLocationListeners.length; i++) {
        try {
          mLocationManager.removeUpdates(mLocationListeners[i]);
        } catch (Exception ex) {
          Log.i(TAG, "fail to remove location listeners, ignore", ex);
        }
      }
    }
  }

  private void initializeLocationManager() {
    Log.e(TAG, "initializeLocationManager");
    if (mLocationManager == null) {
      mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
    }
  }
}
