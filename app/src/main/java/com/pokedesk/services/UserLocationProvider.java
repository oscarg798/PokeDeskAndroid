package com.pokedesk.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by oscargallon on 7/25/16.
 */

public class UserLocationProvider extends Service implements LocationListener {

    private static LocationManager locationManager;

    private boolean canGetLocation = false;


    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;

    private static final long MIN_TIME_BW_UPDATES = 0;

    private Location userLocation;

    private List<LocationProviderUtils.onSubscribeforLocationUpdates> onSubscribeforLocationUpdatesList;

    private boolean isGettingLocationUpdates = false;

    public static UserLocationProvider UserLocationProvider;

    private final String LOCATION_MANAGER_UNAVALAIBLE = "location manager unavalaible";

    private final String GPS_PROVIDER_DISABLE = "GPS PROVIDER ERROR";

    private final String NETWORK_PROVIDER_DISABLE = " NETWORK PROVIDER ERROR";

    private Context context;

    private static final int TIME_INTERVAL = 1000 * 60 * 4;

    private UserLocationProvider(Context context) {
        this.context = context;
        locationManager = (LocationManager) this.context
                .getSystemService(LOCATION_SERVICE);
        onSubscribeforLocationUpdatesList = new ArrayList<>();

    }

    public static UserLocationProvider getLocationProvider(Context context) {
        if (UserLocationProvider == null) {
            UserLocationProvider = new UserLocationProvider(context);
        } else if (locationManager == null) {
            locationManager = (LocationManager) context
                    .getSystemService(LOCATION_SERVICE);
        }
        return UserLocationProvider;
    }


    public void getUserLocation(String provider, LocationProviderUtils.onGetLocation onGetLocation
            , LocationProviderUtils.onSubscribeforLocationUpdates
                                        onSubscribeforLocationUpdates) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (locationManager == null) {
            onGetLocation.errorGettingLocation(LOCATION_MANAGER_UNAVALAIBLE);
        }


        getLocationUpdates(onSubscribeforLocationUpdates);


        switch (provider) {
            case LocationManager.GPS_PROVIDER: {
                if (!isGPSEnabled()) {
                    onGetLocation
                            .errorGettingLocation(GPS_PROVIDER_DISABLE);
                    isGettingLocationUpdates = true;
                    return;
                }

                break;
            }
            case LocationManager.NETWORK_PROVIDER: {
                if (!isNetworkEnabled()) {
                    onGetLocation.errorGettingLocation(NETWORK_PROVIDER_DISABLE);
                    return;
                }
                break;
            }
        }

        locationManager.requestLocationUpdates(
                provider, 60000, 0, this);

        userLocation = locationManager.getLastKnownLocation(provider);


        if (userLocation != null) {
            onGetLocation.locationGot(userLocation);
        }

    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TIME_INTERVAL;
        boolean isSignificantlyOlder = timeDelta < -TIME_INTERVAL;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


    private void getLocationUpdates(LocationProviderUtils.onSubscribeforLocationUpdates onSubscribeforLocationUpdates) {
        if (this.onSubscribeforLocationUpdatesList == null) {
            this.onSubscribeforLocationUpdatesList = new ArrayList<>();
        }
        this.onSubscribeforLocationUpdatesList.add(onSubscribeforLocationUpdates);


    }

    public void unSuscribeLocationUpdates() {
        this.onSubscribeforLocationUpdatesList = null;
        if (this.locationManager != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(this);
            locationManager = null;
        }
    }


    private boolean isGPSEnabled() {
        return locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private boolean isNetworkEnabled() {
        return locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    @Override
    public void onLocationChanged(Location location) {
        if (isBetterLocation(location, userLocation)) {
            userLocation = location;
            if (onSubscribeforLocationUpdatesList != null) {
                for (LocationProviderUtils.onSubscribeforLocationUpdates on : onSubscribeforLocationUpdatesList) {
                    on.locationUpdateGot(location);
                }
            }
        }


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public boolean isGettingLocationUpdates() {
        return isGettingLocationUpdates;
    }

    public void setGettingLocationUpdates(boolean gettingLocationUpdates) {
        isGettingLocationUpdates = gettingLocationUpdates;
    }
}
