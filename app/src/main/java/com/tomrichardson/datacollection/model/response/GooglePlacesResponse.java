package com.tomrichardson.datacollection.model.response;

/**
 * Created by tom on 27/02/2016.
 */
public class GooglePlacesResponse {

  private Place[] results;

  public Place[] getPlaces() {
    return results;
  }

  public class Place {
    public String name;
  }
}
