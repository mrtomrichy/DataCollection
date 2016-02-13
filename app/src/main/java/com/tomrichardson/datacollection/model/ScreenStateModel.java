package com.tomrichardson.datacollection.model;

import co.uk.rushorm.core.RushObject;
import co.uk.rushorm.core.annotations.RushIgnore;

/**
 * Created by tom on 03/02/2016.
 */
public class ScreenStateModel extends RushObject {

  @RushIgnore
  public static final int SCREEN_OFF = 0;
  @RushIgnore
  public static final int SCREEN_ON = 1;

  public long time;
  public int type;

  public ScreenStateModel() {
  }

  public ScreenStateModel(long time, int type) {
    this.time = time;
    this.type = type;
  }

  public String getStateType() {
    if (this.type == SCREEN_OFF) return "SCREEN_OFF";
    else return "SCREEN_ON";
  }
}
