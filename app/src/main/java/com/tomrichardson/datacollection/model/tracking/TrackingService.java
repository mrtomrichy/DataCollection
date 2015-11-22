package com.tomrichardson.datacollection.model.tracking;

import android.content.Context;
import android.content.Intent;

import com.tomrichardson.datacollection.service.ServiceUtils;

/**
 * Created by tom on 22/11/2015.
 */
public class TrackingService {

  private Class serviceClass;
  private String[] permissions;
  private String name;
  private Class modelClass;

  public TrackingService(String name, Class serviceClass, String[] permissions, Class model) {
    this.serviceClass = serviceClass;
    this.name = name;
    this.permissions = permissions;
    this.modelClass = model;
  }

  public String getName() {
    return this.name;
  }

  public Class getServiceClass() {
    return this.serviceClass;
  }

  public Class getModelClass() {
    return this.modelClass;
  }

  public String[] getRequiredPermissions() {
    return permissions;
  }

  public void startService(Context context) {
    context.startService(new Intent(context, this.serviceClass));
  }

  public void stopService(Context context) {
    context.stopService(new Intent(context, this.serviceClass));
  }

  public boolean isServiceRunning(Context context) {
    return ServiceUtils.isMyServiceRunning(context, this.serviceClass);
  }

}
