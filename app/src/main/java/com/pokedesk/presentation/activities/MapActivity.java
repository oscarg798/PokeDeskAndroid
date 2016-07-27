package com.pokedesk.presentation.activities;


import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.core.model.Pokemon;
import com.core.utils.Utils;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.pokedesk.controllers.MapActivityController;
import com.pokedesk.presentation.fragments.MapFragment;
import com.pokedesk.R;
import com.pokedesk.services.PermissionProvider;
import com.pokedesk.services.SocketService;
import com.pokedesk.services.interfaces.ISocketCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URISyntaxException;

public class MapActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<LocationSettingsResult>, ISocketCallback {

    private FrameLayout frameLayout;

    private ProgressBar pbPermissions;

    private TextView tvStatusMessage;

    private Fragment fragment;

    private final String POKEMON_GOT_KEY = "pokeFound";

    private final String INIT_KEY = "init";

    private final int REQUES_CHECK_LOCATION_SETTINGS = 234;

    private String username;

    private String password;

    private String token;

    private boolean isAuthenticated;


    private int permissionRequestedLength = 0;

    private String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    private String[] permissionToRequest = new String[2];

    private LocationRequest locationRequest;

    protected GoogleApiClient googleApiClient;

    private MapActivityController mapActivityController;

    private double lat = 0;

    private double lng = 0;

    private String socketID;

    private boolean isSearching;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        initViewComponents();
        initComponents();

    }

    private void initViewComponents() {
        frameLayout = (FrameLayout) findViewById(R.id.nv_frame_layout);
        pbPermissions = (ProgressBar) findViewById(R.id.pb_permissions);
        tvStatusMessage = (TextView) findViewById(R.id.tv_status_message);
        username = getIntent().getExtras().getString("username");
        password = getIntent().getExtras().getString("password");
        token = getIntent().getExtras().getString("token");

        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }


    }

    private void initComponents() {

        mapActivityController = new MapActivityController(this);


        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        googleApiClient.connect();

        checkPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SocketService
                .getInstance().getmSocket() == null) {
            SocketService.getInstance().setContext(getApplicationContext());

            SocketService.getInstance().initSocket();

            SocketService.getInstance().registerSocketCallbacks(this);
        }
    }

    public void setLocation(double lat, double lng) {
        double userDistance =  Utils.calculateDistanceBetweenTwoLocations(this.lat, this.lng, lat, lng, Utils.METER);
        if(lat!=0 && lng!= 0
                && 50 < userDistance){
            isSearching=false;
        }
        Log.i("USER DISTANCE", Double.toString(userDistance));
        this.lat = lat;
        this.lng = lng;
        if (socketID != null) {
            findPokemons(socketID, lat, lng);
        }
    }

    public void goToMap(double lat, double lng) {

        if(getMapActivityController().getMapFragment()!=null
                && getMapActivityController().getMapFragment().isAdded()){
            return;

        }

        this.lat = lat;

        this.lng = lng;

        frameLayout.setVisibility(View.VISIBLE);

        pbPermissions.setVisibility(View.GONE);

        tvStatusMessage.setVisibility(View.GONE);

        MapFragment mapFragment = MapFragment.newInstance(lat, lng);

        mapActivityController.setMapFragment(mapFragment);

        changeFragment(mapFragment);

        findPokemons(socketID, lat, lng);

        if(getSupportActionBar()!=null){
            getSupportActionBar().show();
        }
    }

    private void checkPermissions() {

        if (!PermissionProvider.getInstance()
                .checkPermission(getApplicationContext(),
                        permissions[0])) {
            permissionToRequest[permissionRequestedLength] = permissions[0];
            permissionRequestedLength += 1;

        }

        if (!PermissionProvider.getInstance()
                .checkPermission(getApplicationContext(),
                        permissions[0])) {
            permissionToRequest[permissionRequestedLength] = permissions[1];
            permissionRequestedLength += 1;
        }

        if (permissionRequestedLength != 0) {
            PermissionProvider.getInstance()
                    .setRationalMessage("We are using your location to find pokemons near to you");

            PermissionProvider.getInstance()
                    .setRationalTitle("Permission");

            PermissionProvider.getInstance()
                    .setRationalTxtBtn("Accept");

            PermissionProvider.getInstance().askForPermissions(this,
                    permissionToRequest);
        } else {
            showLocationMessagewithGoogleApiCLient();
        }


    }

    public void changeFragment(Fragment fragment) {
        this.fragment = fragment;
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.nv_frame_layout, fragment)
                .commit();


    }

    private void socketInitialized(JSONObject data) {
        try {
            socketID = data.getString("id");
            if (lat != 0 && lng != 0) {
                findPokemons(socketID, lat, lng);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void pokemonReceived(JSONObject data) {
        if(lat!=0 && lng!=0){
            findPokemons(socketID, lat, lng);
        }
        if (fragment != null && fragment instanceof MapFragment) {
            ((MapFragment) fragment).pokemonGot(data);
        }
    }

    private void authMessageReceived(String message) {
        Log.i("SOCKET", message);
        if (message.equals(Utils.SUCCESS_KEY)) {
            goToMap(lat, lng);
            tvStatusMessage.setText(getString(R.string.getting_location_message));
            isAuthenticated = true;
        } else {
            mapActivityController.showAlertDialog(getString(R.string.alert_label),
                    getString(R.string.socket_general_error));
        }

    }


    private void onErrorReceived(String message) {
        mapActivityController.showAlertDialog(getString(R.string.alert_label),
                getString(R.string.socket_general_error), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }, "accept");
    }

    private void onErrorReceived(JSONObject data) {
        try {
            String message = data.getString("message");
            Log.e("Socket", message);
            mapActivityController.showAlertDialog(getString(R.string.alert_label),
                    getString(R.string.socket_general_error));
        } catch (JSONException e) {
            e.printStackTrace();
            onErrorReceived("");
        }
    }

    private void findPokemons(String id, double lat, double lng) {
        if (isSearching) {
            return;
        }


        isSearching = true;
        SharedPreferences.Editor editor = getSharedPreferences("poke", 0).edit();
        editor.putString("id", id);
        editor.apply();
        if (token != null) {
            Pokemon.getInstance().getPokemonsNearby(id, token, lat, lng, getApplicationContext()
                    .getString(R.string.base_url) + getApplicationContext().getString(R.string.get_nearby_pokemons_url));
        } else {
            Pokemon.getInstance()
                    .getPokemonsNearby(id, username, password, lat, lng);
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionProvider.REQUEST_PERMISSION_CODE) {
            if (grantResults.length == permissionRequestedLength && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showLocationMessagewithGoogleApiCLient();
            }


        }

    }


    private void showLocationMessagewithGoogleApiCLient() {
        if (locationRequest == null) {
            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000 * 1);
            locationRequest.setFastestInterval(5 * 1000);
        }
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        /**
                         * User has the gps enable
                         */
                        mapActivityController.getUserLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            status.startResolutionForResult(
                                    MapActivity.this, REQUES_CHECK_LOCATION_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                        break;
                    case LocationSettingsStatusCodes.CANCELED:
                        /**
                         * User cancel the request
                         */
                        Log.i("CANCELED", "USER CANCEL");

                }
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {

    }

    public MapActivityController getMapActivityController() {
        return mapActivityController;
    }

    @Override
    public void onSocketMessage(String key, JSONObject arg) {
        switch (key) {
            case SocketService.INIT_KEY: {
                socketInitialized(arg);
                tvStatusMessage.setText(getString(R.string.login_into_pokemon_message));
                break;
            }
            case SocketService.POKEMON_GOT_KEY: {
                pokemonReceived(arg);
                break;
            }
            case SocketService.ERROR_KEY: {
                onErrorReceived(arg);
                break;
            }
        }
    }

    @Override
    public void onSocketMessage(String key, String message) {
        switch (key) {
            case SocketService.AUTH_KEY: {
                authMessageReceived(message);
                break;
            }
            case SocketService.ERROR_KEY: {
                onErrorReceived(message);
                break;
            }
        }
    }

    @Override
    public void onSccketDissconnect() {

    }
}
