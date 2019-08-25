package com.example.whereisthepotato;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class LocationService extends Service {
        private static final String TAG = "MyLocationService";
        private LocationManager mLocationManager = null;
        private static final int LOCATION_INTERVAL = 3000;
        private static final float LOCATION_DISTANCE = 10f;

        LocationListener[] mLocationListeners = new LocationListener[] {
                new LocationListener(LocationManager.GPS_PROVIDER),
                new LocationListener(LocationManager.NETWORK_PROVIDER)
        };

        @Override
        public IBinder onBind(Intent arg0)
        {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId)
        {
            Log.e(TAG, "onStartCommand");
            super.onStartCommand(intent, flags, startId);
            return START_STICKY;
        }

        @Override
        public void onCreate()
        {
            initializeLocationManager();
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[1]);
            } catch (java.lang.SecurityException ex) {
                Log.i(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "network provider does not exist, " + ex.getMessage());
            }
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[0]);
            } catch (java.lang.SecurityException ex) {
                Log.i(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "gps provider does not exist " + ex.getMessage());
            }
            Toast.makeText(getApplicationContext(), "SERVICE STARTED", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onDestroy()
        {
            Log.e(TAG, "onDestroy");
            super.onDestroy();
            if (mLocationManager != null) {
                for (int i = 0; i < mLocationListeners.length; i++) {
                    try {
                        mLocationManager.removeUpdates(mLocationListeners[i]);
                    } catch (Exception ex) {
                        Log.i(TAG, "fail to remove location listners, ignore", ex);
                    }
                }
            }
        }

        private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

        // create LocationListener class to get location updates
        private class LocationListener implements android.location.LocationListener
        {
            Location mLastLocation;

            public LocationListener(String provider)
            {
                Log.e(TAG, "LocationListener " + provider);
                mLastLocation = new Location(provider);
            }

            @Override
            public void onLocationChanged(Location location)
            {
                Log.e(TAG, "TE LA COMES");

                FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();


                Log.e(TAG, "onLocationChanged: " + location);
                mLastLocation.set(location);
               // FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                //mDatabase.child("users").child(user.getUid()).setValue(location);
                Toast.makeText(getApplicationContext(), location.toString(), Toast.LENGTH_LONG).show();

//
//
//                Map<String, String> data = new HashMap<>();
//                data.put("location", location.toString());
//                db.collection("users").document(currentUser.getUid()).set(data, SetOptions.merge());


            }

            @Override
            public void onProviderDisabled(String provider)
            {
                Log.e(TAG, "onProviderDisabled: " + provider);
            }

            @Override
            public void onProviderEnabled(String provider)
            {
                Log.e(TAG, "onProviderEnabled: " + provider);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras)
            {
                Log.e(TAG, "onStatusChanged: " + provider);
            }
        }
}
