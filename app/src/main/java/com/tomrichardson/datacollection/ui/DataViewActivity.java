package com.tomrichardson.datacollection.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tomrichardson.datacollection.R;
import com.tomrichardson.datacollection.model.service.DataServiceModel;
import com.tomrichardson.datacollection.ui.adapter.DataViewAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import co.uk.rushorm.core.RushCore;
import co.uk.rushorm.core.RushObject;
import co.uk.rushorm.core.RushSearch;
import co.uk.rushorm.core.implementation.ReflectionUtils;

public class DataViewActivity extends AppCompatActivity {

  public static final String TRACKING_SERVICE_KEY = "tracking_service";

  private DataServiceModel service;
  private DataViewAdapter adapter;
  private List<RushObject> models;

  BroadcastReceiver receiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      updateItems();
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_data_view);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    Bundle extras = getIntent().getExtras();
    if(extras != null && extras.containsKey(TRACKING_SERVICE_KEY)) {
      service = extras.getParcelable(TRACKING_SERVICE_KEY);
    } else {
      finish();
    }

    getSupportActionBar().setTitle(service.getName() + " data");

    models = new RushSearch().orderAsc("rush_created").find(service.getModelClass());

    List<Field> fields = new ArrayList<>();
    ReflectionUtils.getAllFields(fields, service.getModelClass(), true);

    RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.dataviewlist);
    mRecyclerView.setHasFixedSize(true);

    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
    mRecyclerView.setLayoutManager(mLayoutManager);

    adapter = new DataViewAdapter(this, models, fields);
    mRecyclerView.setAdapter(adapter);
  }

  private void updateItems() {
    models.clear();
    models.addAll(new RushSearch().find(service.getModelClass()));
    adapter.notifyDataSetChanged();
  }

  private void deleteAll() {
    RushCore.getInstance().delete(new RushSearch().find(service.getModelClass()));
    updateItems();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = new MenuInflater(this);
    inflater.inflate(R.menu.menu_dataview, menu);

    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem menuItem) {
    if (menuItem.getItemId() == android.R.id.home) {
      finish();
    } else if(menuItem.getItemId() == R.id.delete_data) {
      deleteAll();
    }
    return super.onOptionsItemSelected(menuItem);
  }

  @Override
  protected void onResume() {
    super.onResume();

    registerReceiver(receiver, new IntentFilter(service.getModelClass().toString()));
    updateItems();
  }

  @Override
  protected void onPause() {
    super.onPause();

    unregisterReceiver(receiver);
  }
}
