package com.example.sandhu.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.Fragment;
///import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.sandhu.Student.FetchURL;
import com.example.sandhu.Student.GpsTracker;
import com.example.sandhu.Student.MapsActivity;
import com.example.sandhu.Student.MyProvider;
import com.example.sandhu.Student.R;
import com.example.sandhu.Student.TaskLoadedCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.example.sandhu.R;
//import com.example.sandhu.R.id;
import com.example.sandhu.collection2.MarkerCollection;
import com.example.sandhu.helpers2.FirebaseEventListenerHelper;
import com.example.sandhu.helpers2.GoogleMapHelper;
import com.example.sandhu.helpers2.MarkerAnimationHelper;
import com.example.sandhu.helpers2.UiHelper;
import com.example.sandhu.interfaces2.FirebaseDriverListener;
import com.example.sandhu.interfaces2.IPositiveNegativeListener;
import com.example.sandhu.interfaces2.IPositiveNegativeListener.DefaultImpls;
import com.example.sandhu.interfaces2.LatLngInterpolator;
import com.example.sandhu.interfaces2.LatLngInterpolator.Spherical;
import com.example.sandhu.model2.Driver;
import com.example.sandhu.Student.MyProvider;
import java.util.HashMap;
//import kotlin.TypeCastException;
//import kotlin.jvm.internal.DefaultConstructorMarker;

public final class MainActivity extends AppCompatActivity implements FirebaseDriverListener, TaskLoadedCallback {
    private GoogleMap googleMap;
    private FusedLocationProviderClient locationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private boolean locationFlag = true;
    private FirebaseEventListenerHelper valueEventListener;
    private final UiHelper uiHelper = new UiHelper();
    private final GoogleMapHelper googleMapHelper = new GoogleMapHelper();
    private final DatabaseReference databaseReference;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 6161;
    private static final String ONLINE_DRIVERS = "online_drivers";
  //  public static final com.spartons.passengerapp.MainActivity.Companion Companion = new com.spartons.passengerapp.MainActivity.Companion((DefaultConstructorMarker)null);
    private HashMap _$_findViewCache;
    GpsTracker gpsTracker;
    private MarkerOptions place1, place2;
    private Polyline currentPolyline;
    Driver driver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main3);
//inserting content provider
        gpsTracker = new GpsTracker(getApplication());
        driver = new Driver();
        ContentValues values = new ContentValues();
        values.put(MyProvider.name, "Karan");
        values.put(MyProvider.sourcce, 43.6532);
        values.put(MyProvider.destinationn, 79.3832);
        Uri uri = getContentResolver().insert(MyProvider.CONTENT_URI, values);
        Toast.makeText(getBaseContext(), "New record inserted", Toast.LENGTH_LONG)
                .show();

////////////////////////////  1
        place1 = new MarkerOptions().position(new LatLng(gpsTracker.latitude, gpsTracker.longitude)).title("Location 1");
        place2 = new MarkerOptions().position(new LatLng(driver.getLat(), driver.getLng())).title("Driver");
        ////////////////////////////////////////////
        Fragment var10000 = this.getSupportFragmentManager().findFragmentById(R.id.supportMap);

            SupportMapFragment mapFragment = (SupportMapFragment)var10000;
            mapFragment.getMapAsync((OnMapReadyCallback)(new OnMapReadyCallback() {
                public final void onMapReady(GoogleMap it) {
                    MainActivity var10000 = MainActivity.this;
                    var10000.googleMap = it;
                    ////////////////////////////////////////////3
                    googleMap.addMarker(place1).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    new FetchURL(MainActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
////////////////////////////////////////
                }
            }));
            this.createLocationCallback();
            FusedLocationProviderClient var10001 = LocationServices.getFusedLocationProviderClient((Activity)this);
            this.locationProviderClient = var10001;
            this.locationRequest = this.uiHelper.getLocationRequest();
            if (!this.uiHelper.isPlayServicesAvailable((Context)this)) {
                Toast.makeText((Context)this, (CharSequence)"Play Services did not installed!", Toast.LENGTH_SHORT).show();
                this.finish();
            } else {
                this.requestLocationUpdate();
            }

            this.valueEventListener = new FirebaseEventListenerHelper((FirebaseDriverListener)this);
            DatabaseReference var3 = this.databaseReference;
            FirebaseEventListenerHelper var4 = this.valueEventListener;
            if (var4 == null) {
            }

            var3.addChildEventListener((ChildEventListener)var4);

    }

    @SuppressLint({"MissingPermission"})
    private final void requestLocationUpdate() {
        if (!this.uiHelper.isHaveLocationPermission((Context)this)) {
            ActivityCompat.requestPermissions((Activity)this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 6161);
        } else {
            if (this.uiHelper.isLocationProviderEnabled((Context)this)) {
                UiHelper var10000 = this.uiHelper;
                Context var10001 = (Context)this;
                String var10002 = this.getResources().getString(R.string.need_location);
                String var10003 = this.getResources().getString(R.string.location_content);
                var10000.showPositiveDialogWithListener(var10001, var10002, var10003, (IPositiveNegativeListener)(new IPositiveNegativeListener() {
                    public void onPositive() {
                        MainActivity.this.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
                    }

                    public void onNegative() {
                        DefaultImpls.onNegative(this);
                    }
                }), "Turn On", false);
            }

            FusedLocationProviderClient var1 = this.locationProviderClient;
            if (var1 == null) {
            }

            LocationRequest var2 = this.locationRequest;
            if (var2 == null) {
            }

            LocationCallback var3 = this.locationCallback;
            if (var3 == null) {
            }

            var1.requestLocationUpdates(var2, var3, Looper.myLooper());
        }
    }

    private final void createLocationCallback() {
        this.locationCallback = (LocationCallback)(new LocationCallback() {
            public void onLocationResult( LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                }

                if (locationResult.getLastLocation() != null) {
                    Location var10002 = locationResult.getLastLocation();
                    double var3 = var10002.getLatitude();
                    Location var10003 = locationResult.getLastLocation();
                    LatLng latLng = new LatLng(var3, var10003.getLongitude());
                    Log.e("Locationn", latLng.latitude + " , " + latLng.longitude);
                    if (MainActivity.this.locationFlag) {
                        MainActivity.this.locationFlag = false;
                        MainActivity.this.animateCamera(latLng);
                    }

                }
            }
        });
    }

    private final void animateCamera(LatLng latLng) {
        CameraUpdate cameraUpdate = this.googleMapHelper.buildCameraUpdate(latLng);
        GoogleMap var10000 = this.googleMap;
        if (var10000 == null) {
        }

        var10000.animateCamera(cameraUpdate, 10, (CancelableCallback)null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 6161) {
            int value = grantResults[0];
            if (value == -1) {
                Toast.makeText((Context)this, (CharSequence)"Location Permission denied", Toast.LENGTH_SHORT).show();
                this.finish();
            } else if (value == 0) {
                this.requestLocationUpdate();
            }
        }
    }
    ///////////////////////////////////2
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = googleMap.addPolyline((PolylineOptions) values[0]);
        currentPolyline.setColor(getApplicationContext().getResources().getColor(R.color.colorBlue));

    }
    //////////////////////////////////////////////
    @Override
    public void onDriverAdded(Driver var1) {
        MarkerOptions markerOptions = this.googleMapHelper.getDriverMarkerOptions(new LatLng(var1.getLat(), var1.getLng()));
        GoogleMap var10000 = this.googleMap;


        Marker marker = var10000.addMarker(markerOptions);
        marker.setTag(var1.getDriverId());
        MarkerCollection.INSTANCE.insertMarker(marker);
        TextView var4 = (TextView)this._$_findCachedViewById(R.id.totalOnlineDrivers);
        var4.setText((CharSequence)(this.getResources().getString(R.string.total_online_drivers) + " " + MarkerCollection.INSTANCE.allMarkers().size()));

    }

    @Override
    public void onDriverRemoved(Driver var1) {
        MarkerCollection.INSTANCE.removeMarker(var1.getDriverId());
        TextView var10000 = (TextView)this._$_findCachedViewById(R.id.totalOnlineDrivers);
        var10000.setText((CharSequence)(this.getResources().getString(R.string.total_online_drivers) + " " + MarkerCollection.INSTANCE.allMarkers().size()));
    }

    @Override
    public void onDriverUpdated(Driver var1) {
                Marker marker = MarkerCollection.INSTANCE.getMarker(var1.getDriverId());
        MarkerAnimationHelper var10000 = MarkerAnimationHelper.INSTANCE;

        var10000.animateMarkerToGB(marker, new LatLng(var1.getLat(), var1.getLng()), (LatLngInterpolator)(new Spherical()));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


        DatabaseReference var10000 = this.databaseReference;
        FirebaseEventListenerHelper var10001 = this.valueEventListener;
        if (var10001 == null) {
        }

        var10000.removeEventListener((ChildEventListener)var10001);
        FusedLocationProviderClient var1 = this.locationProviderClient;
        if (var1 == null) {
        }

        LocationCallback var2 = this.locationCallback;
        if (var2 == null) {
        }

        var1.removeLocationUpdates(var2);
        MarkerCollection.INSTANCE.clearMarkers();
    }

    public MainActivity() {
        FirebaseDatabase var10001 = FirebaseDatabase.getInstance();
        this.databaseReference = var10001.getReference().child("online_drivers");
    }

    // $FF: synthetic method
    public static final GoogleMap access$getGoogleMap$p(MainActivity $this) {
        GoogleMap var10000 = $this.googleMap;
        if (var10000 == null) {
        }

        return var10000;
    }

    public View _$_findCachedViewById(int var1) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }

        View var2 = (View)this._$_findViewCache.get(var1);
        if (var2 == null) {
            var2 = this.findViewById(var1);
            this._$_findViewCache.put(var1, var2);
        }

        return var2;
    }

    public void _$_clearFindViewByIdCache() {
        if (this._$_findViewCache != null) {
            this._$_findViewCache.clear();
        }

    }

    public static final class Companion {
        private Companion() {
        }

        // $FF: synthetic method
//        public Companion(DefaultConstructorMarker $constructor_marker) {
//            this();
//        }
    }
}

