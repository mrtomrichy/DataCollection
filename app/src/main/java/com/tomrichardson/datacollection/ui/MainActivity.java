package com.tomrichardson.datacollection.ui;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.tomrichardson.datacollection.R;
import com.tomrichardson.datacollection.ui.adapter.TrackingListAdapter;

public class MainActivity extends AppCompatActivity {

  TrackingListAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.tracking_list);
    mRecyclerView.setHasFixedSize(true);

    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
    mRecyclerView.setLayoutManager(mLayoutManager);

    adapter = new TrackingListAdapter(this);
    mRecyclerView.setAdapter(adapter);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    adapter.onPermissionUpdate(grantResults.length > 0
                              && grantResults[0] == PackageManager.PERMISSION_GRANTED,
                              requestCode);
  }
}
