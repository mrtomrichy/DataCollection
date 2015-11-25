package com.tomrichardson.datacollection.model;

import java.util.Date;

import co.uk.rushorm.core.RushObject;

/**
 * Created by tom on 20/11/2015.
 */
public class LocationModel extends RushObject {
  public double latitude;
  public double longitude;
  public double altitude;
  public float accuracy;
  public String provider;
  public Date date;
  public String address;
  public String zipCode;
  public String city;
  public String country;

  // Default constructor needed for Rush
  public LocationModel() {}

  public LocationModel(String provider, double latitude, double longitude, double altitude, float accuracy, Date date,
                        String address, String zipCode, String city, String country) {
    this.provider = provider;
    this.latitude = latitude;
    this.altitude = altitude;
    this.longitude = longitude;
    this.accuracy = accuracy;
    this.date = date;
    this.address = address;
    this.zipCode = zipCode;
    this.city = city;
    this.country = country;
  }
}
