package com.tomrichardson.datacollection.model.service;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tom on 29/02/2016.
 */
public abstract class RunnableService implements Parcelable {

  private String name;
  private String[] permissions;
  private Class clazz;

  public RunnableService(String name, Class clazz, String[] permissions) {
    this.name = name;
    this.permissions = permissions;
    this.clazz = clazz;
  }

  protected RunnableService(Parcel in) {
    name = in.readString();
    permissions = in.createStringArray();
    clazz = (Class) in.readSerializable();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
    dest.writeStringArray(permissions);
    dest.writeSerializable(this.clazz);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public String getName() {
    return this.name;
  }

  public String[] getPermissions() {
    return this.permissions;
  }

  public Class getServiceClass() {
    return this.clazz;
  }

  public abstract void enable(Context context);
  public abstract void disable(Context context);
  public abstract boolean isRunning(Context context);
}
