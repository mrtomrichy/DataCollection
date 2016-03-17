package com.tomrichardson.datacollection.service;

import android.Manifest;

import com.tomrichardson.datacollection.model.ActivityModel;
import com.tomrichardson.datacollection.model.LocationModel;
import com.tomrichardson.datacollection.model.ScreenStateModel;
import com.tomrichardson.datacollection.model.service.QueryServiceModel;
import com.tomrichardson.datacollection.model.service.RunnableService;
import com.tomrichardson.datacollection.model.service.RunnableServiceModel;
import com.tomrichardson.datacollection.service.activity.ActivityService;
import com.tomrichardson.datacollection.service.location.LocationService;
import com.tomrichardson.datacollection.service.phone.PhoneService;
import com.tomrichardson.datacollection.service.phonestate.ScreenStateService;
import com.tomrichardson.datacollection.service.textmessage.TextMessageService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 20/11/2015.
 */
public class Services {

  private static Services sInstance = null;

  private List<RunnableService> services = null;

  public static Services getInstance() {
    if(sInstance == null) {
      sInstance = new Services();
    }

    return sInstance;
  }

  public List<RunnableService> getSupportedDataServices() {
    if (services == null) {
      services = new ArrayList<>();

      services.add(new RunnableServiceModel("Location Service", LocationService.class,
          new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
          LocationModel.class));

      services.add(
          new RunnableServiceModel("Activity Service", ActivityService.class,
              new String[]{},
              ActivityModel.class));

      services.add(new QueryServiceModel("Text Message Service", TextMessageService.class,
          new String[]{Manifest.permission.READ_SMS}));

      services.add(new QueryServiceModel("Phone Service", PhoneService.class,
          new String[]{Manifest.permission.READ_CALL_LOG}));

      services.add(new RunnableServiceModel("Screen State Service", ScreenStateService.class,
          new String[]{},
          ScreenStateModel.class));
    }

    return services;
  }
}

