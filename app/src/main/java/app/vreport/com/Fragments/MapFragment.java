package app.vreport.com.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import app.vreport.com.Activities.HomeActivity;
import app.vreport.com.Activities.SplashScreen;
import app.vreport.com.Model.Map;
import app.vreport.com.Model.Resources;
import app.vreport.com.R;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sajjad Saleem on 12/22/2016.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener {

    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    //  private LocationRequest mLocationRequest;
    private double currentLatitude = 0;
    private double currentLongitude = 0;
    private Button spinner;
    Resources resources;

    private Spinner spinner_category;

    // Store instance variables
    private String title;
    private int page;
    final String TAG = "ExceptionCatch";

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    // GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    EditText editText;


    Location locationCt;
    Location mLastLocation;

    // newInstance constructor for creating fragment with arguments
    public static MapFragment newInstance(int page, String title) {
        MapFragment fragmentFirst = new MapFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
 /*       if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            mGoogleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            *//*************************//*
            builder.setAlwaysShow(true); //this is the key ingredient
            *//*************************//*

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result.getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can initialize location
                            // requests here.
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);

                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
*/
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        //   TextView tvLabel = (TextView) mView.findViewById(R.id.tvLabel);
        //  tvLabel.setText(page + " -- " + title);

        // Create the LocationRequest object
        try {

            getCurrentLocation();

            resources = new Resources();
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_dropdown_item, resources.category_dropdown);

            spinner = (Button) mView.findViewById(R.id.category);
            spinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Select by Category")
                            .setAdapter(adapter, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    // TODO: user specific action

                                    if (resources.category_dropdown[which].contentEquals(resources.category_dropdown[0])) {
                                        AddMarker(new boolean[]{true, false, false, false, false});

                                    } else if (resources.category_dropdown[which].contentEquals(resources.category_dropdown[1])) {
                                        AddMarker(new boolean[]{false, true, false, false, false});


                                    } else if (resources.category_dropdown[which].contentEquals(resources.category_dropdown[2])) {
                                        AddMarker(new boolean[]{false, false, true, false, false});

                                    } else if (resources.category_dropdown[which].contentEquals(resources.category_dropdown[3])) {
                                        AddMarker(new boolean[]{false, false, false, true, false});

                                    } else if (resources.category_dropdown[which].contentEquals(resources.category_dropdown[4])) {
                                        AddMarker(new boolean[]{false, false, false, false, true});

                                    } else {
                                        AddMarker(new boolean[]{true, true, true, true, true});

                                    }


                                    dialog.dismiss();
                                }
                            }).create().show();
                }
            });


            editText = (EditText) mView.findViewById(R.id.search);
            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        //      performSearch();
                        return true;
                    }
                    return false;
                }
            });

            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                    /*Search place code*/
                        Intent intent =
                                new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                        .build(getActivity());
                        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                                .build();

                        intent =
                                new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                        .setFilter(typeFilter)
                                        .build(getActivity());
                    } catch (GooglePlayServicesRepairableException e) {
                        // TODO: Handle the error.
                    } catch (GooglePlayServicesNotAvailableException e) {
                        // TODO: Handle the error.
                    }
                }
            });

        } catch (Exception e) {
            Toast.makeText(getContext(), "MapFragment_create " + e,
                    Toast.LENGTH_SHORT).show();

        }


        return mView;
    }

    public void getCurrentLocation() {

        LocationManager locationManagerCt = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
      /*  if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return TODO;
        }*/
        locationCt = locationManagerCt
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
// Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {

                    case Activity.RESULT_OK:
                        //      startActivity(new Intent(HomeActivity.this,HomeActivity.class));
                        //      finish();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                //      mGoogleMap.getMyLocation();
                                /*
                                getCurrentLocation();
                                MarkerOptions marker = new MarkerOptions().position(
                                        new LatLng(locationCt.getLatitude(), locationCt.getLongitude())).title("Current Location").snippet("Discription");

                                marker.icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

// Moving Camera to a Location with animation
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(new LatLng(locationCt.getLatitude(), locationCt.getLongitude())).zoom(15).build();
                                mGoogleMap.animateCamera(CameraUpdateFactory
                                        .newCameraPosition(cameraPosition));

                                mGoogleMap.addMarker(marker);*/
                            }
                        }, 3000);
                        break;
                    case Activity.RESULT_CANCELED:

                        break;
                }
                break;
        }


        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getContext(), data);
                Log.d("Tag", "Place: " + place.getName());
                Log.d("Tag", "Place: " + place.getAddress());
                editText.setText(place.getName());
                getLocationFromAddress((String) place.getAddress());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
                // TODO: Handle the error.
                Log.i("Tag", status.getStatusMessage());

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void getLocationFromAddress(String strAddress) {
        //Create coder with Activity context - this
        Geocoder coder = new Geocoder(getContext());
        List<Address> address;

        try {
            //Get latLng from String
            address = coder.getFromLocationName(strAddress, 5);

            //check for null
            if (address == null) {
                return;
            }

            //Lets take first possibility from the all possibilities.
            Address location = address.get(0);
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            //  LatLng loc1 = new LatLng(24.831343,67.156085);

        /*    ArrayList<Map> maps= SplashScreen.sql.GetList_map();
            if(maps!=null&&maps.size()!=0) {
                for (int i = 0; i < maps.size(); i++) {

                    LatLng loc1 = new LatLng(maps.get(i).getLatitude(), maps.get(i).getLongitude());
                    mGoogleMap.addMarker(new MarkerOptions().position(loc1).title("GeneralForm").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
                }
            }
        */    //Put marker on map on that LatLng

            AddMarker(new boolean[]{true, true, true, true, true});
            Marker srchMarker = HomeActivity.mGoogleMap.addMarker(new MarkerOptions().position(latLng).title("Search Location").icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            //Animate and Zoon on that map location
            HomeActivity.mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            HomeActivity.mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.mapView);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        HomeActivity.mGoogleMap = googleMap;
        // Add a marker in Sydney and move the camera
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        try {
            HomeActivity.mGoogleMap.setMyLocationEnabled(true);
            HomeActivity.mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            HomeActivity.mGoogleMap.setMyLocationEnabled(true);

            AddMarker(new boolean[]{true, true, true, true, true});
            //    googleMap.getUiSettings().setZoomControlsEnabled(true);
            //    googleMap.getUiSettings().setCompassEnabled(true);
//            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            //    googleMap.getUiSettings().setZoomGesturesEnabled(true);
            //    googleMap.getUiSettings().setRotateGesturesEnabled(true);

      /*      LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            List<String> providers = mLocationRequest.getProviders(true);
            Location l = null;

            for (int i = 0; i < providers.size(); i++) {
                l = lm.getLastKnownLocation(providers.get(i));
                if (l != null) {
                    currentLatitude = l.getLatitude();
                    currentLongitude = l.getLongitude();
        *//*            strAdd = getCompleteAddressString(currentLatitude, currentLongitude);
                    tvAddress.setText("Complete Address : " + strAdd);
        *//*            break;
                }
            }

*/
            if (googleMap != null) {
                //    if (currentLongitude != 0 && currentLatitude != 0) {

                LatLng latLng=new LatLng(locationCt.getLatitude(), locationCt.getLongitude());
                MarkerOptions marker = new MarkerOptions().position(latLng).title("Current Location").snippet("Discription");

                marker.icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

               // Moving Camera to a Location with animation
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng).zoom(15).build();

                HomeActivity.mGoogleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));

                HomeActivity.mGoogleMap.addMarker(marker);
            }

            //  }

            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    // The next two lines tell the new client that “this” current class will handle connection stuff
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    //fourth line adds the LocationServices API endpoint from GooglePlayServices
                    .addApi(LocationServices.API)
                    .build();

            AddMarker(new boolean[]{true, true, true, true, true});

        } catch (Exception e) {
            Log.d(TAG, "Location*** " + e);

        }
    }

/*
    @Override
    public void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }
*/

    @Override
    public void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        try {
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
            }
        } catch (Exception e) {

        }


    }

    /**
     * If connected get lat and long
     */
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            currentLatitude = mLastLocation.getLatitude();
            currentLongitude = mLastLocation.getLongitude();

        }
    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
            /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /**
     * If locationChanges change lat and long
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        Toast.makeText(getContext(), currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();

        HomeActivity.mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).title("Location").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        //Animate and Zoon on that map location
        HomeActivity.mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(currentLatitude, currentLongitude)));
        HomeActivity.mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

    }

    public void AddMarker(boolean[] check) {

        try {
            ArrayList<Map> maps = SplashScreen.sql.GetList_map();
            if (maps != null && maps.size() != 0) {
                HomeActivity.mGoogleMap.clear();
                for (int i = 0; i < maps.size(); i++) {

                    LatLng loc1 = new LatLng(maps.get(i).getLatitude(), maps.get(i).getLongitude());
                    if (maps.get(i).getCategory().contentEquals("Traffic") && check[0]) {
                        HomeActivity.mGoogleMap.addMarker(new MarkerOptions().position(loc1).title(maps.get(i).getCategory())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_traffic)));

                    } else if (maps.get(i).getCategory().contentEquals("Civic Issue") && check[1]) {

                        HomeActivity.mGoogleMap.addMarker(new MarkerOptions().position(loc1).title(maps.get(i).getCategory())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_civicissue)));

                    } else if (maps.get(i).getCategory().contentEquals("Crime") && check[2]) {

                        HomeActivity.mGoogleMap.addMarker(new MarkerOptions().position(loc1).title(maps.get(i).getCategory())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_crime_bugglery)));

                    } else if (maps.get(i).getCategory().contentEquals("Hazard") && check[3]) {

                        HomeActivity.mGoogleMap.addMarker(new MarkerOptions().position(loc1).title(maps.get(i).getCategory())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_hazard)));

                    } else if (maps.get(i).getCategory().contentEquals("Weather") && check[4]) {

                        HomeActivity.mGoogleMap.addMarker(new MarkerOptions().position(loc1).title(maps.get(i).getCategory())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_weather)));

                    }

                }
            }
        } catch (Exception e) {

        }

    }

    /*
        Bitmap marker_icon(int i){

            int height = 100;
            int width = 100;
            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(i);
            Bitmap b=bitmapdraw.getBitmap();
            return Bitmap.createScaledBitmap(b, width, height, false);


        }
    */
/*Select by category*/
    class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        Resources resources = new Resources();

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            if (parent.getItemAtPosition(pos).toString().contentEquals(resources.category_dropdown[0])) {
                AddMarker(new boolean[]{true, false, false, false, false});

            } else if (parent.getItemAtPosition(pos).toString().contentEquals(resources.category_dropdown[1])) {
                AddMarker(new boolean[]{false, true, false, false, false});


            } else if (parent.getItemAtPosition(pos).toString().contentEquals(resources.category_dropdown[2])) {
                AddMarker(new boolean[]{false, false, true, false, false});

            } else if (parent.getItemAtPosition(pos).toString().contentEquals(resources.category_dropdown[3])) {
                AddMarker(new boolean[]{false, false, false, true, false});

            } else if (parent.getItemAtPosition(pos).toString().contentEquals(resources.category_dropdown[4])) {
                AddMarker(new boolean[]{false, false, false, false, true});

            } else {
                AddMarker(new boolean[]{true, true, true, true, true});

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    }


}