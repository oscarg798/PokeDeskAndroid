package com.pokedesk.presentation.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pokedesk.R;
import com.pokedesk.controllers.MapActivityController;
import com.pokedesk.presentation.activities.MapActivity;
import com.pokedesk.presentation.listerner.interfaces.IPokemonSearch;
import com.pokedesk.services.PermissionProvider;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.squareup.picasso.Picasso.with;

/**
 * Created by oscargallon on 7/25/16.
 */

public class MapFragment extends com.google.android.gms.maps.MapFragment
        implements IPokemonSearch, MapActivityController.MarkerOptionsCallback {


    private GoogleMap map;

    private double lat;

    private double lng;


    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance(double lat, double lng) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putDouble("lat", lat);
        args.putDouble("lng", lng);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lat = getArguments().getDouble("lat");
            lng = getArguments().getDouble("lng");
        }
        initComponents();
    }


    public void initComponents() {
        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                LatLng latLng = new LatLng(lat, lng);

                Marker m = googleMap.addMarker(new MarkerOptions()
                        .position(latLng));

                CameraPosition posicion = new CameraPosition.Builder().target(latLng)
                        .zoom(15).build();

                CameraUpdate camUpd = CameraUpdateFactory.newCameraPosition(posicion);
                googleMap.animateCamera(camUpd, 2000, null);

            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    private MapFragment getMapFragment() {
        android.app.FragmentManager fm = null;

        Log.d("MAP", "sdk: " + Build.VERSION.SDK_INT);
        Log.d("MAP", "release: " + Build.VERSION.RELEASE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Log.d("MAP", "using getFragmentManager");
            fm = getFragmentManager();
        } else {
            Log.d("MAP", "using getChildFragmentManager");
            fm = getChildFragmentManager();
        }

        return (MapFragment) fm.findFragmentById(R.id.map);
    }

    @Override
    public void pokemonGot(JSONObject pokemon) {
        try {
            pokemon = pokemon.getJSONObject("pokemon");
            double lat = pokemon.getDouble("lat");
            double lng = pokemon.getDouble("lng");
            String name = pokemon.getString("name");
            final String url = pokemon.getString("img");
            final String id = pokemon.getString("num");
            final MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(lat, lng));
            markerOptions.title(name);

            ((MapActivity) getActivity()).getMapActivityController()
                    .loadMarkerOptionsImage(markerOptions, id, url);



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onMarkerOptionsUpdated(MarkerOptions markerOptions) {
        if (map != null) {
            map.addMarker(markerOptions);
        }
    }
}

