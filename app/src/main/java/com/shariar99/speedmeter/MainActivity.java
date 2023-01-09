package com.shariar99.speedmeter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    // Declare variables
    Location previousLocation = null;
    long previousTime = 0;
    float topSpeed = 0;
    float totalDistance = 0;
    long totalTime = 0;

    TextView speedTextView;
    TextView avspeedTextView;
    TextView TspeedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize TextViews
        speedTextView = findViewById(R.id.speedTextView);
        avspeedTextView = findViewById(R.id.avspeedTextView);
        TspeedTextView =findViewById(R.id.TspeedTextView);

        // Get current time
        previousTime = System.currentTimeMillis();

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // This method is called when the location is updated
                float speed = location.getSpeed();
                speed = speed * 3.6f;
                // Round the speed value to the nearest integer
                int roundedSpeed = Math.round(speed);
                speedTextView.setText(String.format("Sp: %d km/h", roundedSpeed));

                // Calculate distance and time since last update
                float distance = 0;
                long time = 0;
                if (previousLocation != null) {
                    distance = location.distanceTo(previousLocation);
                    time = location.getTime() - previousTime;
                }
                previousLocation = location;
                previousTime = location.getTime();

                // Update total distance and time
                totalDistance += distance;
                totalTime += time;

                // Calculate average speed
                float avspeed = totalDistance / totalTime;
                // Convert speed from meters per second to km/h
                avspeed = speed * 3.6f;
                // Round the average speed value to the nearest integer
                int roundedAvspeed = Math.round(avspeed);
                avspeedTextView.setText(String.format("Avg: %d km/h", roundedAvspeed));
                // Update top speed
                if (speed > topSpeed) {
                    topSpeed = speed;
                    topSpeed = speed * 3.6f;
                }
                // Round the top speed value to the nearest integer
                int roundedTopSpeed = Math.round(topSpeed);
                TspeedTextView.setText(String.format("Top: %d km/h", roundedTopSpeed));
            }

            // Other methods of the LocationListener interface
        });
    }
}


