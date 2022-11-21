package com.example.realclient;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.realclient.databinding.ActivityMainBinding;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.Task;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private String location;
    private ActivityMainBinding mainBinding;
    final String uniqueId = UUID.randomUUID().toString();
    private Paho paho;
    private LocationRequest locationRequest;
    private Handler handler;
    long startTime;
    static boolean active = false;

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        active = true;
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View mainView = mainBinding.getRoot();
        setContentView(mainView);
        LocationRequest.Builder builder = new LocationRequest
                .Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 2000L);
        locationRequest = builder.build();
        handler = new Handler();
        AsyncUpdate update = new AsyncUpdate();
        update.start();
        mainBinding.connect.setOnClickListener(view -> {
            String ip = mainBinding.brokerIP.getText().toString();
            String port = mainBinding.brokerPort.getText().toString();
            String broker = "tcp://" + ip + ":" + port;
            Log.d("Broker", broker);
            paho = new Paho("emergency", broker, uniqueId);
            try {
                paho.connect();
            } catch (MqttException e) {
                System.out.println("reason " + e.getReasonCode());
                System.out.println("msg " + e.getMessage());
                System.out.println("loc " + e.getLocalizedMessage());
                System.out.println("cause " + e.getCause());
                System.out.println("except " + e);
                e.printStackTrace();
                Log.d("Exception", e.getMessage());
                Toast.makeText(MainActivity.this, "Could not connect", Toast.LENGTH_LONG).show();
            }
        });
        mainBinding.update.setOnClickListener(view -> publish());
    }

    public void setLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().
                    checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                if (isLocationEnable()) {
                    LocationServices.getFusedLocationProviderClient(MainActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);
                                    LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                            .removeLocationUpdates(this);
                                    if (locationResult.getLocations().size() > 0) {
                                        int index = locationResult.getLocations().size() - 1;
                                        double latitude = locationResult.getLocations()
                                                .get(index).getLatitude(),
                                                longitude = locationResult.getLocations()
                                                        .get(index).getLongitude();
                                        String location = latitude + "," + longitude;
                                        mainBinding.location.setText(location);
                                    }
                                }
                            }, Looper.getMainLooper());
                } else {
                    turnOnLocation();
                }
            }
        }
    }

    public void publish() {
        if (paho == null) return;
        String severity = "";
        for (int i = 0; i < mainBinding.severities.getChildCount(); i++) {
            View v = mainBinding.severities.getChildAt(i);
            if (v instanceof RadioButton) {
                if (((RadioButton) v).isChecked()) {
                    severity = ((RadioButton) v).getText().toString();
                    System.out.println("Severity: " + severity);
                }
            }
        }
        if (!severity.isEmpty() && paho.checkConnection()) {
            setLocation();
            location = mainBinding.location.getText().toString();
            String message = uniqueId + ":" + severity + ":" + location;
            Log.d("Message", message);
            try {
                paho.sendMessage(message);
                startTime = System.currentTimeMillis();
            } catch (MqttException e) {
                System.out.println("reason " + e.getReasonCode());
                System.out.println("msg " + e.getMessage());
                System.out.println("loc " + e.getLocalizedMessage());
                System.out.println("cause " + e.getCause());
                System.out.println("excep " + e);
                e.printStackTrace();
                Log.d("Exception", e.getMessage());
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void turnOnLocation() {
        LocationSettingsRequest.Builder settingsBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        settingsBuilder.setAlwaysShow(true);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(settingsBuilder.build());
        result.addOnCompleteListener(task -> {
            try {
                LocationSettingsResponse response = task.getResult(ApiException.class);
                Toast.makeText(MainActivity.this, "Location on", Toast.LENGTH_SHORT).show();
            } catch (ApiException e) {
                switch (e.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            resolvableApiException.startResolutionForResult(MainActivity.this, 2);
                        } catch (IntentSender.SendIntentException | ClassCastException ex) {
                            ex.printStackTrace();
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    private boolean isLocationEnable() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if(paho != null) paho.disconnect();
        super.onDestroy();
    }

    private class AsyncUpdate extends Thread {
        public AsyncUpdate(){
            super();
            startTime = System.currentTimeMillis();
        }
        @Override
        public void run() {
            long innerSleep = System.currentTimeMillis();
            System.out.println("Thread status " + active);
            while(active) {
                if(System.currentTimeMillis() >= innerSleep + 5_000) {
                    setLocation();
                    location = mainBinding.location.getText().toString();
                    handler.post(() -> {
                        if (System.currentTimeMillis() >= 20_000 + startTime) {
                            publish();
                            startTime = System.currentTimeMillis();
                            Log.d("Thread", "publish");
                        }
                    });
                    innerSleep = System.currentTimeMillis();
                }
            }
        }
    }
}