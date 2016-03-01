package com.tomrichardson.datacollection.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.tomrichardson.datacollection.R;
import com.tomrichardson.datacollection.model.service.RunnableService;
import com.tomrichardson.datacollection.model.service.RunnableServiceModel;
import com.tomrichardson.datacollection.ui.adapter.DataServiceAdapter;

public class MainActivity extends AppCompatActivity {

  private DataServiceAdapter adapter;

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

    adapter = new DataServiceAdapter(this, new DataServiceAdapter.RowClickedListener() {
      @Override
      public void rowClicked(RunnableService service) {
        if (service instanceof RunnableServiceModel) {
          Intent i = new Intent(MainActivity.this, DataViewActivity.class);
          i.putExtra(DataViewActivity.TRACKING_SERVICE_KEY, (RunnableServiceModel) service);
          startActivity(i);
        }
      }
    });
    mRecyclerView.setAdapter(adapter);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    adapter.onPermissionUpdate(grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED,
        requestCode);
  }
}
