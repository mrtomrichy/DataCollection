package com.tomrichardson.datacollection.model.service;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.tomrichardson.datacollection.service.ServiceUtils;

/**
 * Created by tom on 22/11/2015.
 */
public class DataServiceModel implements Parcelable {

  private Class serviceClass;
  private String[] permissions;
  private String name;
  private Class modelClass;

  public DataServiceModel(String name, Class serviceClass, String[] permissions, Class model) {
    this.serviceClass = serviceClass;
    this.name = name;
    this.permissions = permissions;
    this.modelClass = model;
  }

  protected DataServiceModel(Parcel in) {
    serviceClass = (Class) in.readSerializable();
    modelClass = (Class) in.readSerializable();
    name = in.readString();
    permissions = in.createStringArray();
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
    context.startService(new Intent(context, getServiceClass()));
  }

  public void stopService(Context context) {
    context.stopService(new Intent(context, getServiceClass()));
  }

  public boolean isServiceRunning(Context context) {
    return ServiceUtils.isMyServiceRunning(context, getServiceClass());
  }

  /* Parcelable methods */

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeSerializable(serviceClass);
    dest.writeSerializable(modelClass);
    dest.writeString(name);
    dest.writeStringArray(permissions);
  }

  public static final Creator<DataServiceModel> CREATOR = new Creator<DataServiceModel>() {
    @Override
    public DataServiceModel createFromParcel(Parcel in) {
      return new DataServiceModel(in);
    }

    @Override
    public DataServiceModel[] newArray(int size) {
      return new DataServiceModel[size];
    }
  };
}
