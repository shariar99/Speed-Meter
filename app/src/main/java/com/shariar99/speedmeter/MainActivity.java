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
    Location previousLocation = null;
    Location currentLocation = null;
    long previousTime = 0;
    float topSpeed = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView speedTextView = findViewById(R.id.speedTextView);
        TextView avspeedTextView = findViewById(R.id.avspeedTextView);
        TextView TspeedTextView =findViewById(R.id.TspeedTextView);



        previousTime = System.currentTimeMillis();

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location;
                previousTime = System.currentTimeMillis();

                // This method is called when the location is updated
                float speed = location.getSpeed();
                // Convert speed from meters per second to km/h
                speed = speed * 3.6f;
                speedTextView.setText(String.format("Sp: %.2f km/h", speed));

                //avarage distance
                float distance = 0;
                if (previousLocation != null) {
                    distance = currentLocation.distanceTo(previousLocation);
                }
                previousLocation = currentLocation;

                // Calculate time taken since last update
                long time = currentLocation.getTime() - previousTime;
                previousTime = currentLocation.getTime();

                // Calculate average speed
                float avspeed = distance / time;

                // Convert speed from meters per second to km/h
                avspeed = speed * 3.6f;
                avspeedTextView.setText(String.format("Avg: %.2f km/h",avspeed));

                // Update top speed
                if (speed > topSpeed) {
                    topSpeed = speed;
                }

                // Display speed and top speed
                TspeedTextView.setText(String.format("Top: %.2f km/h", topSpeed));

            }

            // Other methods of the LocationListener interface
        });
    }
}