package com.tomrichardson.datacollection.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tomrichardson.datacollection.Application;
import com.tomrichardson.datacollection.R;
import com.tomrichardson.datacollection.Utils;
import com.tomrichardson.datacollection.model.SummaryModel;
import com.tomrichardson.datacollection.model.service.RunnableService;
import com.tomrichardson.datacollection.model.service.RunnableServiceModel;
import com.tomrichardson.datacollection.service.Services;
import com.tomrichardson.datacollection.service.summary.SummaryService;
import com.tomrichardson.datacollection.ui.adapter.DataServiceAdapter;

import java.util.ArrayList;

public class ServiceViewActivity extends AppCompatActivity {

  private DataServiceAdapter adapter;
  private ArrayList<RunnableService> services;

  private Button summariseButton;

  private BroadcastReceiver receiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      updateServices();
      unregisterReceiver(receiver);
      summariseButton.setEnabled(true);
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.tracking_list);
    mRecyclerView.setHasFixedSize(true);

    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
    mRecyclerView.setLayoutManager(mLayoutManager);

    services = new ArrayList<>();

    adapter = new DataServiceAdapter(this, services, new DataServiceAdapter.RowClickedListener() {
      @Override
      public void rowClicked(RunnableService service) {
        if (service instanceof RunnableServiceModel && ((RunnableServiceModel) service).getModelClass() != null) {
          Intent i = new Intent(ServiceViewActivity.this, DataViewActivity.class);
          i.putExtra(DataViewActivity.TRACKING_SERVICE_KEY, ((RunnableServiceModel) service).getModelClass());
          startActivity(i);
        }
      }
    });
    mRecyclerView.setAdapter(adapter);



    summariseButton = (Button) findViewById(R.id.summarise_button);
    summariseButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (Application.RUSH_INITIALISED) {
          SummaryService.summarise(ServiceViewActivity.this, Utils.getTodayDate());
        }
      }
    });

    TextView summariseText = (TextView) findViewById(R.id.summary_text);
    summariseText.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(Application.RUSH_INITIALISED) {
          Intent i = new Intent(ServiceViewActivity.this, DataViewActivity.class);
          i.putExtra(DataViewActivity.TRACKING_SERVICE_KEY, SummaryModel.class);
          startActivity(i);
        }
      }
    });

    if (Application.RUSH_INITIALISED) {
      updateServices();
    } else {
      this.registerReceiver(receiver, new IntentFilter(Application.RUSH_INIT_COMPLETE));
      summariseButton.setEnabled(false);
    }
  }

  private void updateServices() {
    services.removeAll(services);
    services.addAll(Services.getInstance().getSupportedDataServices());
    adapter.notifyDataSetChanged();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if(item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    adapter.onPermissionUpdate(grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED,
        requestCode);
  }
}
