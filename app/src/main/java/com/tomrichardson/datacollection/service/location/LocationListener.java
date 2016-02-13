package com.tomrichardson.datacollection.service.location;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.tomrichardson.datacollection.model.LocationModel;

import java.io.IOException;
import java.util.Calendar;

import co.uk.rushorm.core.RushCore;

/**
 * Created by tom on 25/11/2015.
 */
public class LocationListener implements android.location.LocationListener {
  private static final String TAG = "LOCATIONLISTENER";

  private Location mLastLocation;
  private Geocoder mGeoCoder;
  private Context mContext;

  public LocationListener(String provider, Context context) {
    Log.e(TAG, "LocationListener " + provider);
    mLastLocation = new Location(provider);
    mGeoCoder = new Geocoder(context);
    mContext = context;
  }

  @Override
  public void onLocationChanged(Location location) {
    Log.e(TAG, "onLocationChanged: " + location);
    mLastLocation.set(location);

    Address address = null;
    try {
      address = mGeoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0);
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    Log.d("LOCATION", "Address: " + address.getAddressLine(0));


    LocationModel l = new LocationModel(location.getProvider(), location.getLatitude(), location.getLongitude(), location.getAltitude(),
        location.getAccuracy(), Calendar.getInstance().getTime(),
        address.getAddressLine(0), address.getPostalCode(), address.getLocality(), address.getCountryName());


    RushCore.getInstance().save(l);
    Log.d("BROADCAST", LocationModel.class.toString());
    mContext.sendBroadcast(new Intent(LocationModel.class.toString()));

  }

  @Override
  public void onProviderDisabled(String provider) {
    Log.e(TAG, "onProviderDisabled: " + provider);
  }

  @Override
  public void onProviderEnabled(String provider) {
    Log.e(TAG, "onProviderEnabled: " + provider);
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {
    Log.e(TAG, "onStatusChanged: " + provider);
  }
}
