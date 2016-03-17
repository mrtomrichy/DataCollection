package com.tomrichardson.datacollection.service.location;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Places;
import com.tomrichardson.datacollection.service.RushWaitService;

public class LocationService extends RushWaitService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {
  public LocationService() {
  }

  private static final String TAG = "LOCATIONSERVICE";
  private LocationManager mLocationManager = null;
  private static final int LOCATION_INTERVAL = 2 * 60 * 1000;
  private static final float LOCATION_DISTANCE = 10f;

  private LocationListener[] mLocationListeners;

  private GoogleApiClient mGoogleApiClient;


  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.e(TAG, "onStartCommand");
    super.onStartCommand(intent, flags, startId);
    return START_STICKY;
  }

  @Override
  public void run() {
    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addApi(Places.PLACE_DETECTION_API)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .build();

    mGoogleApiClient.connect();

    mLocationListeners = new LocationListener[]{
        new LocationListener(LocationManager.GPS_PROVIDER, getApplicationContext(), mGoogleApiClient),
        new LocationListener(LocationManager.NETWORK_PROVIDER, getApplicationContext(), mGoogleApiClient)
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

    mGoogleApiClient.disconnect();
  }

  private void initializeLocationManager() {
    Log.e(TAG, "initializeLocationManager");
    if (mLocationManager == null) {
      mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
    }
  }

  @Override
  public void onConnected(Bundle bundle) {
  }

  @Override
  public void onConnectionSuspended(int i) {
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
  }

  @Override
  public void onResult(Status status) {
  }
}
