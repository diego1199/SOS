package com.certificacion.dauza.sos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.Manifest;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.AfterPermissionGranted;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, EasyPermissions.PermissionCallbacks {

    public enum EmergencyServiceType {
        AMBULANCE,
        POLICE,
        FIREFIGHTER
    }

    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;

    private Context context;
    private static final int RC_ALL_PERMISSIONS_REQUIRED = 101;

    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 5000;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private LocationManager mLocationManager;

    private String[] locationPerms;

    private double longitud;
    private double latitud;

    private FloatingActionButton changeServiceButton;
    private Button requestButton;
    private Spinner serviceTypeSpinner;

    private EmergencyServiceType selectedEmergencyType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        selectedEmergencyType = EmergencyServiceType.AMBULANCE;

        locationPerms = new String[2];
        locationPerms[0] = Manifest.permission.ACCESS_COARSE_LOCATION;
        locationPerms[1] = Manifest.permission.ACCESS_FINE_LOCATION;

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        verifyCurrentUser();

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        setContentView(R.layout.activity_maps);

        context = this;

        methodRequiresPermissions();

        requestButton = findViewById(R.id.requestButton);
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "Pedir servicio en " + longitud + ", " + latitud);
            }
        });

        changeServiceButton = findViewById(R.id.changeServiceButton);
        changeServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serviceTypeSpinner.performClick();
            }
        });

        serviceTypeSpinner = findViewById(R.id.serviceTypeSpinner);
        serviceTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position) {
                    case 0:
                        selectedEmergencyType = EmergencyServiceType.AMBULANCE;
                        requestButton.setText(R.string.pedir_ambulancia);
                        break;
                    case 1:
                        selectedEmergencyType = EmergencyServiceType.POLICE;
                        requestButton.setText(R.string.pedir_policia);
                        break;
                    case 2:
                        selectedEmergencyType = EmergencyServiceType.FIREFIGHTER;
                        requestButton.setText(R.string.pedir_bomberos);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        initMap();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurrentLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationManager.removeUpdates(mLocationListener);
    }

    private void verifyCurrentUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            goLogInScreen();
        }
    }

    private void goLogInScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    //region Map Methods

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    private void initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (EasyPermissions.hasPermissions(this, locationPerms)) {
            mMap.setMyLocationEnabled(true);
        }
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                drawMarker(location);
                latitud = location.getLatitude();
                longitud = location.getLongitude();
                //mLocationManager.removeUpdates(mLocationListener);
            } else {
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private void drawMarker(Location location) {

        if (mMap != null) {
            mMap.clear();
            LatLng gps = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(gps)
                    .title("Mi ubicación actual"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 17));
        }

    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {

        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;
        if (!(isGPSEnabled || isNetworkEnabled))
            Toast.makeText(context, R.string.error_location_provider, Toast.LENGTH_LONG).show();
        else {
            if (EasyPermissions.hasPermissions(this, locationPerms)) {
                if (isNetworkEnabled) {
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                    location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }

                if (isGPSEnabled) {
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                    location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
//                latitud = location.getLatitude();
//                longitud = location.getLongitude();
            }
            else {
                EasyPermissions.requestPermissions(this, "SOS no tiene permisos de tu ubicación.", RC_ALL_PERMISSIONS_REQUIRED, locationPerms);
            }
        }
        if (location != null) {
            drawMarker(location);
        }
    }

    //endregion

    //region App Runtime Permissions

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_ALL_PERMISSIONS_REQUIRED)
    private void methodRequiresPermissions() {
        String[] perms = {
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.SEND_SMS,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "SOS no tiene todos los permisos en el dispositivo.", RC_ALL_PERMISSIONS_REQUIRED, perms);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG, "Permission has been granted");
        if (perms.contains(Manifest.permission.ACCESS_FINE_LOCATION) && perms.contains(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            getCurrentLocation();
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
        if (EasyPermissions.checkDeniedPermissionsNeverAskAgain(this, "SOS debe tener permisos a tu ubicación actual y debe ser capaz de enviar SMS, todo esto velando por su seguridad. Será redirigido a la pantalla de ajustes para otorgar los permisos faltantes.", android.R.string.ok, android.R.string.cancel, perms));
    }

    //endregion
}
