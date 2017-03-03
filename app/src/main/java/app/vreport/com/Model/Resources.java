package app.vreport.com.Model;

import app.vreport.com.R;

/**
 * Created by Sajjad Saleem on 12/29/2016.
 */
public class Resources {

    public Resources() {

    }

    public int[][] Images = {{R.drawable.ic_traffic, R.drawable.ic_traffic_moderate,
            R.drawable.ic_traffic_high, R.drawable.ic_traffic_deadlock},

            {R.drawable.ic_civicissue, R.drawable.ic_civic_issues___illegal_bilboard,
                    R.drawable.ic_civic_issue__parking, R.drawable.ic_civic_issue___garbage},

            {R.drawable.ic_crime_bugglery, R.drawable.ic_crime_cartheft,
                    R.drawable.ic_crime_shotsfired, R.drawable.ic_crime_snatching},
            {R.drawable.ic_hazard, R.drawable.ic_hazard_fireaccident,
                    R.drawable.ic_hazard_pothole, R.drawable.ic_hazard_onroad},

            {R.drawable.ic_weather, R.drawable.ic_weather_fog,
                    R.drawable.ic_weather_storm, R.drawable.ic_weather_rainwater}
    };

    public int[][] SubCategory = {
            {R.drawable.ic_civic_issue___parking__no_parking, R.drawable.ic_civic_issue___parking___illegar_parking_mafia},
            {R.drawable.ic_civic_issue___garbage_overflow_dumps, R.drawable.ic_civic_issue___litter},
            {R.drawable.ic_crime_bankrobbery, R.drawable.ic_crime_homerobbery,
                    R.drawable.ic_crime_snatching},
            {R.drawable.ic_hazard_onroad_construction, R.drawable.ic_hazard_roadside_electricwire, R.drawable.ic_hazard_roadside_missingsign}

    };

    public String[][] SubCatname = {
            {"Civic Issue Parking", "No Parking", "Illegal Parking"},
            {"Civic Issue Garbage", "Overflow Dumps", "Litter"},
            {"Crime Robbery", "Bank Robbery", "Home Robbery", "Snatching"},
            {"Hazard OnRoad Issue", "Construction", "Electricwire", "Missing Sign"}
    };

    public String[][] name = {
            {"Traffic", "Moderate", "High", "Deadlock"},
            {"Civic Issue", "Illegal Billboard", "Parking", "Garbage"},
            {"Crime", "Car Theft", "ShotFire", "Robbery"},
            {"Hazard", "Fire Accident", "PotHole", "Road Issue"},
            {"Weather", "Fog", "Storm", "Rain Water"}
    };

    public String[] category_dropdown = {"Traffic Problem", "Civic Issue", "Crime Accident"
            , "Hazard Issue", "Weather Condition","All Category"};

}
