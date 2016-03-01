package com.tomrichardson.datacollection.service.location;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
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
  private GoogleApiClient mGoogleApiClient;

  public LocationListener(String provider, Context context, GoogleApiClient googleApiClient) {
    Log.e(TAG, "LocationListener " + provider);
    mLastLocation = new Location(provider);
    mGeoCoder = new Geocoder(context);
    mContext = context;
    mGoogleApiClient = googleApiClient;
  }

  @Override
  public void onLocationChanged(final Location location) {
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

    final Address finalAddress = address;

    // Now we try to get a 'place' for the location
    if(mGoogleApiClient.isConnected()) {
      PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
          .getCurrentPlace(mGoogleApiClient, null);

      result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
        @Override
        public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
          PlaceLikelihood mostLikely = null;
          for (PlaceLikelihood placeLikelihood : likelyPlaces) {
            Log.d(TAG, String.format("Place '%s' has likelihood: %g",
                placeLikelihood.getPlace().getName(),
                placeLikelihood.getLikelihood()));

            if(mostLikely == null || placeLikelihood.getLikelihood() > mostLikely.getLikelihood()) {
              mostLikely = placeLikelihood;
            }
          }

          String placeName = mostLikely == null ? null : mostLikely.getPlace().getName().toString();
          likelyPlaces.release();

          LocationModel l = new LocationModel(placeName, location.getProvider(), location.getLatitude(), location.getLongitude(), location.getAltitude(),
              location.getAccuracy(), Calendar.getInstance().getTime(),
              finalAddress.getAddressLine(0), finalAddress.getPostalCode(), finalAddress.getLocality(), finalAddress.getCountryName());

          saveLocation(l);
        }
      });
    } else {
      LocationModel l = new LocationModel(null, location.getProvider(), location.getLatitude(), location.getLongitude(), location.getAltitude(),
          location.getAccuracy(), Calendar.getInstance().getTime(),
          finalAddress.getAddressLine(0), finalAddress.getPostalCode(), finalAddress.getLocality(), finalAddress.getCountryName());

      saveLocation(l);
    }
  }

  private void saveLocation(LocationModel l) {
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
