package com.example.realclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

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
    private String message;
    private ActivityMainBinding mainBinding;
    final String uniqueId = UUID.randomUUID().toString();
    private Paho paho;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View mainView = mainBinding.getRoot();
        setContentView(mainView);
        LocationRequest.Builder builder = new LocationRequest
                .Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 1000L);
        locationRequest = builder.build();
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
                System.out.println("excep " + e);
                e.printStackTrace();
                Log.d("Exception", e.getMessage());
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            mainBinding.update.setOnClickListener(view -> {
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
                setLocation();
                message = uniqueId + ":" + severity + ":" + mainBinding.location.getText();
                Log.d("Message", message);
            try {
                paho.sendMessage(message);
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
            });
        }

        private void setLocation () {

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

        private void turnOnLocation () {
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

        private boolean isLocationEnable () {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }

        @Override
        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){
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
        protected void onDestroy () {
            super.onDestroy();
            paho.disconnect();
        }
    }