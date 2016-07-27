package com.pokedesk.services;

import android.location.Location;

/**
 * Created by oscargallon on 7/25/16.
 */

public class LocationProviderUtils {

    public interface onGetLocation {
        void locationGot(Location location);

        void errorGettingLocation(String error);

    }

    public interface onSubscribeforLocationUpdates {
        void locationUpdateGot(Location location);

    }
}
