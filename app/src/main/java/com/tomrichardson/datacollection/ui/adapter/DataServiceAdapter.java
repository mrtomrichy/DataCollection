package com.tomrichardson.datacollection.ui.adapter;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.tomrichardson.datacollection.R;
import com.tomrichardson.datacollection.model.service.DataServiceModel;
import com.tomrichardson.datacollection.service.ServiceUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by tom on 22/11/2015.
 */
public class DataServiceAdapter extends RecyclerView.Adapter<DataServiceAdapter.ViewHolder> {

  public interface RowClickedListener {
    void rowClicked(DataServiceModel service);
  }

  private Activity activity;
  private List<DataServiceModel> services;
  private RowClickedListener listener;

  public DataServiceAdapter(Activity activity, RowClickedListener listener) {
    this.activity = activity;
    this.services = Arrays.asList(ServiceUtils.getSupportedDataServices());
    this.listener = listener;
  }

  public void onPermissionUpdate(boolean success, int callbackNumber) {
    if (success) {
      startService(services.get(callbackNumber));
    } else {
      // We dont have permission to run this task
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(activity).inflate(R.layout.list_trackingservices, parent, false);

    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final DataServiceModel service = services.get(position);

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(listener != null) {
          listener.rowClicked(service);
        }
      }
    });

    holder.name.setText(service.getName());
    holder.toggle.setChecked(service.isServiceRunning(activity));
    holder.toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
          startService(service);
        } else {
          stopService(service);
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return services.size();
  }

  private void stopService(DataServiceModel service) {
    service.stopService(activity);
  }

  private void startService(DataServiceModel service) {

    for (String permission : service.getRequiredPermissions()) {
      if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(activity,
            new String[]{permission},
            services.indexOf(service));

        return;
      }
    }

    service.startService(activity);
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public Switch toggle;

    public ViewHolder(View itemView) {
      super(itemView);

      this.name = (TextView) itemView.findViewById(R.id.tracking_name);
      this.toggle = (Switch) itemView.findViewById(R.id.toggle_tracking);
    }
  }
}