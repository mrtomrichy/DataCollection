package com.tomrichardson.datacollection.service;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;

import com.tomrichardson.datacollection.model.ActivityModel;
import com.tomrichardson.datacollection.model.LocationModel;
import com.tomrichardson.datacollection.model.service.DataServiceModel;
import com.tomrichardson.datacollection.service.activity.ActivityService;
import com.tomrichardson.datacollection.service.location.LocationService;
import com.tomrichardson.datacollection.service.textmessage.TextMessageService;

/**
 * Created by tom on 20/11/2015.
 */
public class ServiceUtils {

  public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
    ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
      if (serviceClass.getName().equals(service.service.getClassName())) {
        return true;
      }
    }
    return false;
  }

  public static DataServiceModel[] getSupportedDataServices() {

    return new DataServiceModel[]{
        new DataServiceModel("Location Service", LocationService.class,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                            LocationModel.class),
        new DataServiceModel("Activity Service", ActivityService.class,
                            new String[]{},
                            ActivityModel.class),
        new DataServiceModel("Text Message Service", TextMessageService.class,
            new String[]{Manifest.permission.READ_SMS},
            null)
    };
  }

}
