package com.tomrichardson.datacollection.service;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;

import com.tomrichardson.datacollection.model.ActivityModel;
import com.tomrichardson.datacollection.model.LocationModel;
import com.tomrichardson.datacollection.model.ScreenStateModel;
import com.tomrichardson.datacollection.model.SummaryModel;
import com.tomrichardson.datacollection.model.service.RunnableService;
import com.tomrichardson.datacollection.model.service.RunnableServiceModel;
import com.tomrichardson.datacollection.model.service.StaticServiceModel;
import com.tomrichardson.datacollection.service.activity.ActivityService;
import com.tomrichardson.datacollection.service.location.LocationService;
import com.tomrichardson.datacollection.service.phone.PhoneService;
import com.tomrichardson.datacollection.service.phonestate.ScreenStateService;
import com.tomrichardson.datacollection.service.summary.SummaryService;
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

  public static RunnableService[] getSupportedDataServices(Context context) {

    return new RunnableService[]{
        new RunnableServiceModel("Location Service", LocationService.class,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                            LocationModel.class),
        new RunnableServiceModel("Activity Service", ActivityService.class,
                            new String[]{},
                            ActivityModel.class),
        new StaticServiceModel("Text Message Service", TextMessageService.class,
            new String[]{Manifest.permission.READ_SMS}),

        new StaticServiceModel("Phone Service", PhoneService.class,
            new String[]{Manifest.permission.READ_CALL_LOG}),

        new RunnableServiceModel("Screen State Service", ScreenStateService.class,
            new String[]{},
            ScreenStateModel.class),

        new RunnableServiceModel("Summary Service", SummaryService.class,
            new String[]{}, SummaryModel.class)
    };
  }
}
