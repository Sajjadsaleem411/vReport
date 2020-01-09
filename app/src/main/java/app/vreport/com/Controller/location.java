package app.vreport.com.Controller;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import app.vreport.com.Activities.Report.GeneralForm;

/**
 * Created by Muhammad Sajjad on 3/7/2017.
 */
public class location {
    Context context;

    public location(Context context) {
        this.context = context;
    }

    public String[] getLocationFromAddress(String strAddress) {
        //Create coder with Activity context - this


        String add[] = new String[3];
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng latLng;

        try {
            //Get latLng from String
            address = coder.getFromLocationName(strAddress, 5);

            //check for null
            if (address == null) {
                return null;
            }

            //Lets take first possibility from the all possibilities.
            Address location = address.get(0);
            latLng = new LatLng(location.getLatitude(), location.getLongitude());


            GeneralForm.report.setLatitude(location.getLatitude());
            GeneralForm.report.setLongitude(location.getLongitude());

            Log.d("Lat", "" + location.getLatitude());
            Log.d("Lon", "" + location.getLongitude());

            Geocoder gcd = new Geocoder(context,
                    Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(location.getLatitude(), location
                        .getLongitude(), 1);
                if (addresses.size() > 0)
                    Log.d("address", "" + addresses.get(0).getAddressLine(0));
                Log.d("address subadmin", "" + addresses.get(0).getSubAdminArea());

                Log.d("address loc", "" + addresses.get(0).getLocality());
                GeneralForm.report.setPlace(addresses.get(0).getAddressLine(0));
                GeneralForm.report.setCity(addresses.get(0).getAddressLine(1));
                GeneralForm.report.setCountry(addresses.get(0).getAddressLine(2));

                add[0] = addresses.get(0).getAddressLine(0);
                add[1] = addresses.get(0).getAddressLine(1);
                add[2] = addresses.get(0).getAddressLine(2);

                Log.d("City***", addresses.get(0).getAddressLine(0) + "," + addresses.get(0).getAddressLine(1) + ","
                        + addresses.get(0).getAddressLine(2));


            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return add;

    }

    public void getLocationFromAddress(double lat,double lon) {
        //Create coder with Activity context - this


        String add[] = new String[3];
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng latLng;


        Geocoder gcd = new Geocoder(context,
                Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(lat, lon, 1);
            if (addresses.size() > 0)
                Log.d("address", "" + addresses.get(0).getAddressLine(0));
            Log.d("address subadmin", "" + addresses.get(0).getSubAdminArea());

            Log.d("Get_Place","="+addresses.get(0).getAddressLine(0));
            Log.d("Get_City",""+addresses.get(0).getAddressLine(1));
            Log.d("Get_Country",""+addresses.get(0).getAddressLine(2));

            Log.d("address loc", "" + addresses.get(0).getLocality());
            GeneralForm.report.setPlace(addresses.get(0).getAddressLine(0));
            GeneralForm.report.setCity(addresses.get(0).getAddressLine(1));
            GeneralForm.report.setCountry(addresses.get(0).getAddressLine(2));


            Log.d("City", addresses.get(0).getFeatureName() + "," + addresses.get(0).getLocality() + ","
                    + addresses.get(0).getAdminArea() +
                    "," + addresses.get(0).getCountryName());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

        public String[] getAddressFromLocation(Location loc) {

        String add[] = new String[3];
        Geocoder gcd = new Geocoder(context,
                Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(loc.getLatitude(), loc
                    .getLongitude(), 1);
            if (addresses.size() > 0)
                Log.d("address", "" + addresses.get(0).getAddressLine(0));
            Log.d("address subadmin", "" + addresses.get(0).getSubAdminArea());

            Log.d("address loc", "" + addresses.get(0).getLocality());
            GeneralForm.report.setPlace(addresses.get(0).getAddressLine(0));
            GeneralForm.report.setCity(addresses.get(0).getAddressLine(1));
            GeneralForm.report.setCountry(addresses.get(0).getAddressLine(2));

            add[0] = addresses.get(0).getAddressLine(0);
            add[1] = addresses.get(0).getAddressLine(1);
            add[2] = addresses.get(0).getAddressLine(2);

            System.out.println(addresses.get(0).getLocality());

            Log.d("City", addresses.get(0).getFeatureName() + "," + addresses.get(0).getLocality() + ","
                    + addresses.get(0).getAdminArea() +
                    "," + addresses.get(0).getCountryName());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return add;

    }
}
