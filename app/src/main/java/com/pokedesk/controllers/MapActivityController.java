package com.pokedesk.controllers;

import android.app.Activity;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.util.Log;

import com.core.services.ImageDownloadService;
import com.core.services.interfaces.IImageDownloadService;
import com.core.utils.Utils;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pokedesk.controllers.abstracts.AbstractController;
import com.pokedesk.presentation.activities.MapActivity;
import com.pokedesk.presentation.fragments.MapFragment;
import com.pokedesk.services.LocationProviderUtils;
import com.pokedesk.services.UserLocationProvider;

import java.util.HashMap;

/**
 * Created by oscargallon on 7/25/16.
 */

public class MapActivityController extends AbstractController implements
        LocationProviderUtils.onGetLocation, LocationProviderUtils.onSubscribeforLocationUpdates {

    private HashMap<String, MarkerOptions> markerOptionsHashMap;

    private MapFragment mapFragment;

    public interface MarkerOptionsCallback {
        void onMarkerOptionsUpdated(MarkerOptions markerOptions);
    }


    public MapActivityController(Activity activity) {
        super(activity);
        markerOptionsHashMap = new HashMap<>();
    }

    public void getUserLocation() {
        UserLocationProvider.getLocationProvider(getActivity())
                .getUserLocation(LocationManager.GPS_PROVIDER.toString(), this, this);


        UserLocationProvider.getLocationProvider(getActivity())
                .getUserLocation(LocationManager.NETWORK_PROVIDER.toString(), this, this);


    }

    public void getPokemonsNearby(String id) {

    }

    private void getMarkerImage(final String id, String url) {
        ImageDownloadService imageDownloadService = new ImageDownloadService(new IImageDownloadService() {
            @Override
            public void onImageGot(Bitmap bitmap, String id) {

                if (mapFragment != null) {
                    MarkerOptions aux = markerOptionsHashMap.get(id);
                    if (bitmap != null) {
                        aux.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                    }

                    mapFragment.onMarkerOptionsUpdated(aux);
                }

            }

            @Override
            public void onImageError(String err) {
                markerOptionsHashMap.remove(id);

            }
        }, getActivity().getApplicationContext(), id);

        imageDownloadService.execute(url);
    }

    private void sendMarkerOptionsToFragment(MarkerOptions markerOptions) {
        if (mapFragment != null) {
            mapFragment.onMarkerOptionsUpdated(markerOptions);
        }
    }

    public void loadMarkerOptionsImage(MarkerOptions markerOptions, String id, String url) {

        if (markerOptionsHashMap.get(id) != null && markerOptionsHashMap.get(id).getIcon() != null) {
            MarkerOptions aux = markerOptionsHashMap.get(id);
            double pokeDistance = Utils.calculateDistanceBetweenTwoLocations(aux.getPosition().latitude,
                    aux.getPosition().longitude, markerOptions.getPosition().latitude,
                    markerOptions.getPosition().longitude, Utils.METER);

            Log.i("POKE DISTANCE", Double.toString(pokeDistance));
            if (100 > pokeDistance) {
                return;
            }
            aux.position(markerOptions.getPosition());
            sendMarkerOptionsToFragment(aux);
        } else {
            markerOptionsHashMap.put(id, markerOptions);
            getMarkerImage(id, url);
        }


    }


    @Override
    public void locationGot(Location location) {

        ((MapActivity) getActivity()).setLocation(location.getLatitude(), location.getLongitude());

    }

    @Override
    public void errorGettingLocation(String error) {

    }

    @Override
    public void locationUpdateGot(Location location) {

        ((MapActivity) getActivity()).setLocation(location.getLatitude(), location.getLongitude());
    }

    public void setMapFragment(MapFragment mapFragment) {
        this.mapFragment = mapFragment;
    }

    public MapFragment getMapFragment() {
        return mapFragment;
    }
}
