package app.vreport.com.Controller;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import app.vreport.com.Activities.HomeActivity;
import app.vreport.com.Activities.Report.GeneralForm;
import app.vreport.com.Activities.Report.ReportActivity;

/**
 * Created by Sajjad Saleem on 12/29/2016.
 */
public class MyLocationListener implements LocationListener {

    Context mContex;

    public MyLocationListener(Context c){

        mContex=c;
    }
    @Override
    public void onLocationChanged(Location loc) {

     //   pb.setVisibility(View.INVISIBLE);
/*
        Toast.makeText(mContex, "Location changed : Lat: " +
                        loc.getLatitude() + " Lng: " + loc.getLongitude(),
                Toast.LENGTH_SHORT).show();
*/
        String longitude = "Longitude: " + loc.getLongitude();
        Log.d("TAG", longitude);
        String latitude = "Latitude: " + loc.getLatitude();
        Log.d("TAG", latitude);
        GeneralForm.report.setLatitude(loc.getLatitude());
        GeneralForm.report.setLongitude(loc.getLongitude());

        HomeActivity.mlat=loc.getLatitude();
        HomeActivity.mlog=loc.getLongitude();

    /*----------to get City-Name from coordinates ------------- */
            String cityName = null;
            Geocoder gcd = new Geocoder(mContex,
                    Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(), loc
                        .getLongitude(), 1);
                if (addresses.size() > 0)
                    Log.d("address",""+addresses.get(0).getAddressLine(0));
                Log.d("address subadmin",""+addresses.get(0).getSubAdminArea());

                Log.d("address loc",""+addresses.get(0).getLocality());
                GeneralForm.report.setPlace(addresses.get(0).getAddressLine(0));
                GeneralForm.report.setCity( addresses.get(0).getAddressLine(1)  +", "+addresses.get(0).getAddressLine(2));

                    System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getFeatureName() + "," + addresses.get(0).getSubAdminArea() + "," + addresses.get(0).getAdminArea() + "," + addresses.get(0).getLocality() + ","
                        + addresses.get(0).getAdminArea() +
                        "," + addresses.get(0).getCountryName();

                Log.d("City", addresses.get(0).getFeatureName() + "," + addresses.get(0).getLocality() + ","
                        + addresses.get(0).getAdminArea() +
                        "," + addresses.get(0).getCountryName());

            } catch (IOException e) {
                e.printStackTrace();
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
}