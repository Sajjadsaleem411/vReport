package app.vreport.com.Activities;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import app.vreport.com.Activities.Report.ReportActivity;
import app.vreport.com.Controller.GPSTracker;
import app.vreport.com.Controller.MyLocationListener;
import app.vreport.com.Controller.ServerConnection;
import app.vreport.com.Fragments.HomeScreen;
import app.vreport.com.R;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private GoogleApiClient googleApiClient;
    boolean flag = false;
    ImageView header_image;
    TextView header_name, header_email;
    public static GoogleMap mGoogleMap;
    public static Double mlog = 0.33333, mlat = 0.3333;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        try {

        //    if (SplashScreen.isOnline(this)) {
/*
                ServerConnection serverConnection = new ServerConnection(this, 1);
                serverConnection.SubmitToServer();
*/
//            }

            final String[] userdata = SplashScreen.sql.GetUserData();

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //           Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    //                 .setAction("Action", null).show();
                    try {
                       /* if (displayGpsStatus()) {

                            Intent intent = new Intent(HomeActivity.this, ReportActivity.class);
                            startActivity(intent);
                        }*/
                        if (displayGpsStatus() && SplashScreen.isOnline(HomeActivity.this)) {

                            Intent intent = new Intent(HomeActivity.this, ReportActivity.class);
                            startActivity(intent);
                        } else if (!SplashScreen.isOnline(HomeActivity.this)) {
                            HomeActivity.showMessage("Internet Off", "Please check your Internet Connection .", HomeActivity.this);

                        } else {
                            HomeActivity.showMessage("GPS Is Disable", "Please check your Device's GPS .", HomeActivity.this);
                        }
                    } catch (Exception e) {

                    }
                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();


                /*Navigator Header fields*/
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View hView = navigationView.getHeaderView(0);
            /*hView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(userdata!=null&&userdata[1] != null&&userdata[2] != null)
                    startActivity(new Intent(HomeActivity.this,Profile.class));
                }
            });*/

            header_image = (ImageView) hView.findViewById(R.id.header_image);

            header_name = (TextView) hView.findViewById(R.id.header_name);
            //    header_name.setVisibility(View.INVISIBLE);

            header_email = (TextView) hView.findViewById(R.id.header_email);
            header_email.setVisibility(View.INVISIBLE);

            if (userdata != null) {
                if (userdata[0] != null) {
                    header_name.setText(userdata[0]);
                    header_name.setVisibility(View.VISIBLE);
                }
                if (userdata[1] != null) {
                    header_email.setText(userdata[1]);
                    header_email.setVisibility(View.VISIBLE);
                }
                if (userdata[2] != null) {

                    Picasso.with(this).load(userdata[2]).into(header_image);

                }

            } else {
                Log.d("id data null", "null");
            }


            navigationView.setNavigationItemSelectedListener(this);

            Fragment fragment = new HomeScreen();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        /*    if (googleApiClient == null) {
                googleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this).build();
                googleApiClient.connect();

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
                        LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
                result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                    @Override
                    public void onResult(LocationSettingsResult result) {
                        final Status status = result.getStatus();
                        final LocationSettingsStates state = result.getLocationSettingsStates();
                        switch (status.getStatusCode()) {
                            case LocationSettingsStatusCodes.SUCCESS:
                                // Alllocation settings are satisfied. The client can initialize location
                                // requests here.
                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied. But could be fixed by showing the user
                                // a dialog.
                                try {
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    status.startResolutionForResult(HomeActivity.this, REQUEST_CHECK_SETTINGS);

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
                });             }*/


        } catch (Exception e) {
            Toast.makeText(this, "Home_Create " + e,
                    Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent;
        int id = item.getItemId();

        if (id == R.id.nav_feedback) {

            Uri uri = Uri.parse("https://goo.gl/forms/4oc5EMBvh1iQMrwP2"); // missing 'http://' will cause crashed
            intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

        }
        if (id == R.id.nav_logout) {
            try {
                SplashScreen.sql.ClearTableData("user");
                intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.putExtra("TokenExpire", true);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Home_Logout " + e,
                        Toast.LENGTH_SHORT).show();

            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /*----Method to Check GPS is enable or disable ----- */
    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }

    /*Show Dialog*/
    static public void showMessage(String title, String message, final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.setTitle(title);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText(message);


        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButton);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        dialog.show();


        /* AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
/*
        startActivity(new Intent(HomeActivity.this,HomeActivity.class));
        finish();*/
    }/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                  //               Create an Intent that will start the Menu-Activity.
                             *//*   startActivity(new Intent(HomeActivity.this,HomeActivity.class));
                                    finish();
                         *//*
                             *//*Get current Location user *//*


                                LocationManager locationManagerCt = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      *//*  if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return TODO;
        }*//*

                                *//*
                                Location locationCt = locationManagerCt
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                                MarkerOptions marker = new MarkerOptions().position(
                                        new LatLng(locationCt.getLatitude(), locationCt.getLongitude())).title("Current Location").snippet("Discription");

                                marker.icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

// Moving Camera to a Location with animation
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(new LatLng(locationCt.getLatitude(), locationCt.getLongitude())).zoom(15).build();

                                HomeActivity.mGoogleMap.animateCamera(CameraUpdateFactory
                                        .newCameraPosition(cameraPosition));*//*
                            }
                        }, 5000);
                        break;
                    case Activity.RESULT_CANCELED:

                        break;
                }
                break;
        }
    }*/

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
