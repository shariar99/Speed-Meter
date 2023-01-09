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
                // Convert speed from meters per second to km/h
                speed = speed * 3.6f;
                speedTextView.setText(String.format("Sp: %.2f km/h", speed));

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
                avspeed = avspeed * 3.6f;
                avspeedTextView.setText(String.format("Avg: %.2f km/h",avspeed));

                // Update top speed
                if (speed > topSpeed) {
                    topSpeed = speed;
                }
                // Display top speed
                TspeedTextView.setText(String.format("Top: %.2f km/h", topSpeed));
            }

            // Other methods of the LocationListener interface
        });
    }
}


