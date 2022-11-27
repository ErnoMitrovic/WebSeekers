package com.example.realclient;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.CursorIndexOutOfBoundsException;
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

/**
 * <h1>Main Activity</h1>
 * <p>Class that enables the main activity</p>
 *
 * @author ErnoMitrovic <a>https://github.com/ErnoMitrovic</a>
 * @version 1.0
 * @since 21/11/2022
 */
public class MainActivity extends AppCompatActivity {
    private String location;
    private ActivityMainBinding mainBinding;
    private static String UNIQUE_ID;
    private static final String TOPIC = "rescues/severity";
    private static final long UPDATE_RATE = 10_000L;
    private Paho paho;
    private LocationRequest locationRequest;
    private Handler handler;
    private DBHandler dbHandler;
    long startTime;
    static boolean active = false;
    SyncUpdate update;

    /**
     * On start method used to ser active to true.
     */
    @Override
    protected void onStart() {
        active = true;
        update = new SyncUpdate();
        update.start();
        super.onStart();
    }

    /**
     * On stop method used to set active to false.
     */
    @Override
    protected void onStop() {
        active = false;
        super.onStop();
    }

    /**
     * Method used to create and inflate the views, here, the on click listeners are also set.
     *
     * @param savedInstanceState if the user exits the app.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // active = true;
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View mainView = mainBinding.getRoot();
        setContentView(mainView);
        LocationRequest.Builder builder = new LocationRequest
                .Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 2000L);
        locationRequest = builder.build();
        handler = new Handler();
        dbHandler = new DBHandler(MainActivity.this, "user.db");
        update = new SyncUpdate();
        update.start();
        mainBinding.connect.setOnClickListener(view -> {
            String ip = mainBinding.brokerIP.getText().toString();
            String port = mainBinding.brokerPort.getText().toString();
            String broker = "tcp://" + ip + ":" + port;
            Log.d("Broker", broker);
            try {
                UNIQUE_ID = dbHandler.getUser();
                paho = new Paho(TOPIC, broker, UNIQUE_ID);
            } catch (CursorIndexOutOfBoundsException | IllegalArgumentException e) {
                UNIQUE_ID = UUID.randomUUID().toString();
                dbHandler.insertUser(UNIQUE_ID);
                paho = new Paho(TOPIC, broker, UNIQUE_ID);
            }
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

    /**
     * Used to set the location base on the user.
     */
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

    /**
     * Used to publish the messages to the broker
     */
    public void publish() {
        if (paho == null) {
            Toast.makeText(MainActivity.this, "NOT CONNECTED", Toast.LENGTH_SHORT).show();
            return;
        }
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
            String message = UNIQUE_ID + ":" + severity + ":" + location;
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

    /**
     * Used to request the user to turn his location on.
     */
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

    /**
     * Used to check the location permissions
     *
     * @return the status of the location service.
     */
    private boolean isLocationEnable() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * Request permission results callback
     *
     * @param requestCode  the requested code.
     * @param permissions  the string of permissions.
     * @param grantResults the results granted.
     */
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

    /**
     * Used to disconnect the client.
     */
    @Override
    protected void onDestroy() {
        if (paho != null) paho.disconnect();
        super.onDestroy();
    }

    /**
     * Class to instantiate a new thread to update the data.
     *
     * @author ErnoMitrovic <a>https://github.com/ErnoMitrovic</a>
     * @version 1.0
     * @see Thread
     * @since 21/11/2022
     */
    private class SyncUpdate extends Thread {

        /**
         * Constructor for the SyncUpdate class
         */
        public SyncUpdate() {
            super();
            startTime = System.currentTimeMillis();
        }

        /**
         * The method to run for the Thread
         */
        @Override
        public void run() {
            long innerSleep = System.currentTimeMillis();
            System.out.println("Thread status " + active);
            while (active) {
                if (System.currentTimeMillis() >= innerSleep + 5_000) {
                    setLocation();
                    location = mainBinding.location.getText().toString();
                    handler.post(() -> {
                        if (System.currentTimeMillis() >= UPDATE_RATE + startTime) {
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