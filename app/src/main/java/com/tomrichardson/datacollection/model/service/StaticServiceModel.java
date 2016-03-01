package com.tomrichardson.datacollection.model.service;

import android.content.Context;
import android.os.Parcel;

import co.uk.rushorm.core.RushObject;
import co.uk.rushorm.core.RushSearch;

/**
 * Created by tom on 29/02/2016.
 */
public class StaticServiceModel extends RunnableService {

  public StaticServiceModel(String name, Class clazz, String[] permissions) {
    super(name, clazz, permissions);

    if(getServiceState() == null) {
      new ServiceState(getServiceClass().getName()).save();
    }
  }

  @Override
  public void enable(Context context) {
    ServiceState state = getServiceState();
    state.enabled = true;
    state.save();
  }

  @Override
  public void disable(Context context) {
    ServiceState state = getServiceState();
    state.enabled = false;
    state.save();
  }

  @Override
  public boolean isRunning(Context context) {
    return getServiceState().enabled;
  }

  private ServiceState getServiceState() {
    return new RushSearch().whereEqual("className", getServiceClass().getName()).findSingle(ServiceState.class);
  }

  public static class ServiceState extends RushObject {
    public String className;
    public boolean enabled;

    public ServiceState() {}

    public ServiceState(String className) {
      this.className = className;
      this.enabled = false;
    }
  }

  public static final Creator<RunnableServiceModel> CREATOR = new Creator<RunnableServiceModel>() {
    @Override
    public RunnableServiceModel createFromParcel(Parcel in) {
      return new RunnableServiceModel(in);
    }

    @Override
    public RunnableServiceModel[] newArray(int size) {
      return new RunnableServiceModel[size];
    }
  };
}
