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
import com.tomrichardson.datacollection.model.tracking.TrackingService;
import com.tomrichardson.datacollection.service.ServiceUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by tom on 22/11/2015.
 */
public class TrackingListAdapter extends RecyclerView.Adapter<TrackingListAdapter.ViewHolder> {

  private Activity activity;
  private List<TrackingService> services;

  public TrackingListAdapter(Activity activity) {
    this.activity = activity;
    this.services = Arrays.asList(ServiceUtils.getSupportedTrackingClasses());
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
    final TrackingService service = services.get(position);

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

  private void stopService(TrackingService service) {
    service.stopService(activity);
  }

  private void startService(TrackingService service) {

    for (String permission : service.getRequiredPermissions()) {
      if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {

          // Show an explanation to the user *asynchronously* -- don't block
          // this thread waiting for the user's response! After the user
          // sees the explanation, try again to request the permission.

          ActivityCompat.requestPermissions(activity,
              new String[]{permission},
              services.indexOf(service));

        } else {
          ActivityCompat.requestPermissions(activity,
              new String[]{permission},
              services.indexOf(service));
        }

        return;
      }
    }

    service.startService(activity);
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    Switch toggle;

    public ViewHolder(View itemView) {
      super(itemView);

      this.name = (TextView) itemView.findViewById(R.id.tracking_name);
      this.toggle = (Switch) itemView.findViewById(R.id.toggle_tracking);
    }
  }
}
