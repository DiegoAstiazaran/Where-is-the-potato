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

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

public class LocationService extends Service {
        private static final String TAG = "MyLocationService";
        private LocationManager mLocationManager = null;
        private static final int LOCATION_INTERVAL = 1000;
        private static final float LOCATION_DISTANCE = 5f;

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
            FirebaseApp.initializeApp(this);
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

        private class LocationListener implements android.location.LocationListener
        {
            Location mLastLocation;

            public LocationListener(String provider)
            {
                Log.e(TAG, "LocationListener " + provider);
                mLastLocation = new Location(provider);
            }

            @Override
            public void onLocationChanged(final Location location)
            {

                final FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

                Log.e(TAG, "onLocationChanged: " + location);
                mLastLocation.set(location);

                final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                final FirebaseUser fbuser = firebaseAuth.getCurrentUser();
                final String fbid = fbuser.getUid();

                firestoreDB.collection("users")
                        .whereEqualTo("firebase_uid", fbid)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentReference userUpdate = firestoreDB.collection("users").document(fbid);
                                    userUpdate.update("location", new GeoPoint(location.getLatitude(), location.getLongitude()));
                                }
                            }
                        });
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
