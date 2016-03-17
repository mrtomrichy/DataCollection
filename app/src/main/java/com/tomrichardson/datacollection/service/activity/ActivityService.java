package com.tomrichardson.datacollection.service.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.tomrichardson.datacollection.service.RushWaitService;

/**
 * Created by tom on 06/12/2015.
 */
public class ActivityService extends RushWaitService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

  private GoogleApiClient mGoogleApiClient;

  @Override
  public void run() {
    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addApi(ActivityRecognition.API)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .build();

    mGoogleApiClient.connect();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(mGoogleApiClient, getActivityDetectionPendingIntent());
    mGoogleApiClient.disconnect();
  }

  @Override
  public void onConnected(Bundle bundle) {
    Log.d("GooglePlayServices", "CONNECTED");

    ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(
        mGoogleApiClient,
        0,
        getActivityDetectionPendingIntent()
    ).setResultCallback(this);
  }

  @Override
  public void onConnectionFailed(ConnectionResult result) {
    Log.i("GooglePlayServices", "CONNECTION FAILED: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    stopSelf();
  }

  @Override
  public void onConnectionSuspended(int i) {
    Log.d("GooglePlayServices", "CONNECTION SUSPENDED");
  }

  private PendingIntent getActivityDetectionPendingIntent() {
    Intent intent = new Intent(this, DetectedActivitiesIntentService.class);

    // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
    // requestActivityUpdates() and removeActivityUpdates().
    return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  @Override
  public void onResult(Status status) {
    if (status.isSuccess()) {
      Log.d("REQUESTUPDATE", "SUCCESS");
    } else {
      Log.d("REQUESTUPDATE", "FAILURE");
      stopSelf();
    }
  }
}
