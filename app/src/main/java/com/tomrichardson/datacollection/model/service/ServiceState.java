package com.tomrichardson.datacollection.model.service;

import co.uk.rushorm.core.RushObject;

/**
 * Created by tom on 07/02/2016.
 */
public class ServiceState extends RushObject {
  public String name;
  public boolean isRunning;

  public ServiceState() {

  }

  public ServiceState(String name, boolean isRunning) {
    this.name = name;
    this.isRunning = isRunning;
  }
}
