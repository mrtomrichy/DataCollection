package com.tomrichardson.datacollection.service.activity;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.tomrichardson.datacollection.model.ActivityModel;

import java.util.ArrayList;

import co.uk.rushorm.core.RushCore;

/**
 * Created by tom on 07/12/2015.
 */
public class DetectedActivitiesIntentService extends IntentService {

  protected static final String TAG = "DetectedActivitiesIS";
  private ArrayList<Integer> importantActivities;

  public DetectedActivitiesIntentService() {
    super(TAG);

    importantActivities = new ArrayList<>();
    importantActivities.add(DetectedActivity.IN_VEHICLE);
    importantActivities.add(DetectedActivity.RUNNING);
    importantActivities.add(DetectedActivity.ON_BICYCLE);
    importantActivities.add(DetectedActivity.WALKING);
    importantActivities.add(DetectedActivity.STILL);
  }

  @Override
  public void onCreate() {
    super.onCreate();
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

    ArrayList<DetectedActivity> detectedActivities = (ArrayList) result.getProbableActivities();

    DetectedActivity activity = null;

    // Activities are ordered by probability
    for (DetectedActivity da : detectedActivities) {
      if (isActivityRelevant(da.getType())) {
        activity = da;
        break;
      }
    }

    if (activity != null) {
      RushCore.getInstance().save(new ActivityModel(activity.getType(), System.currentTimeMillis(),
          getActivityString(activity.getType())));
    }

    Log.d("BROADCAST", ActivityModel.class.toString());
    this.sendBroadcast(new Intent(ActivityModel.class.toString()));
  }

  private static String getActivityString(int detectedActivityType) {
    switch (detectedActivityType) {
      case DetectedActivity.IN_VEHICLE:
        return "VEHICLE";
      case DetectedActivity.ON_BICYCLE:
        return "BICYCLE";
      case DetectedActivity.ON_FOOT:
        return "FOOT";
      case DetectedActivity.RUNNING:
        return "RUNNING";
      case DetectedActivity.STILL:
        return "STILL";
      case DetectedActivity.TILTING:
        return "TILTING";
      case DetectedActivity.UNKNOWN:
        return "UNKNOWN";
      case DetectedActivity.WALKING:
        return "WALKING";
      default:
        return "UNIDENTIFIABLE";
    }
  }

  private boolean isActivityRelevant(int id) {
    return importantActivities.indexOf(id) != -1;
  }
}