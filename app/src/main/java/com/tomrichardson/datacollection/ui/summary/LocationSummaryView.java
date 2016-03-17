package com.tomrichardson.datacollection.ui.summary;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tomrichardson.datacollection.R;
import com.tomrichardson.datacollection.model.summary.LocationSummary;

/**
 * Created by tom on 16/03/2016.
 */
public class LocationSummaryView extends SummaryView implements OnMapReadyCallback {

  private GoogleMap mMap;
  private TextView placeName;
  private LocationSummary location;

  public LocationSummaryView(Context context) {
    super(context);
  }

  public LocationSummaryView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public LocationSummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void init() {
    super.init();

    placeName = (TextView) findViewById(R.id.place_text);
  }

  @Override
  protected int getLayoutId() {
    return R.layout.summary_location;
  }

  public void initData(LocationSummary location, FragmentManager manager) {
    this.location = location;
    SupportMapFragment mapFragment = (SupportMapFragment) manager.findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);

    placeName.setText(location.name);
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;

    // Add a marker in Sydney, Australia, and move the camera.
    LatLng place = new LatLng(location.latitude, location.longitude);
    mMap.addMarker(new MarkerOptions().position(place).title(location.name));
    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 17));
  }
}
