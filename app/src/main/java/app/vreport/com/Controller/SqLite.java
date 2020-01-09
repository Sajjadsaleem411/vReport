package app.vreport.com.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.vreport.com.Activities.Report.GeneralForm;
import app.vreport.com.Model.Map;
import app.vreport.com.Model.Report;
import app.vreport.com.Model.Resources;

/**
 * Created by Sajjad Saleem on 1/1/2017.
 */
public class SqLite {
    SQLiteDatabase db;
    Context mContext;

    int rang = 5;

    public SqLite(Context context) {
        mContext = context;
        CreateDatabase();

    }


    private void CreateDatabase() {

        db = mContext.openOrCreateDatabase("vReport", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS report(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "vote INTEGER," +
                "description VARCHAR," +
                "category VARCHAR," +
                "sub_category VARCHAR," +
                "longitude VARCHAR," +
                "latitude VARCHAR," +
                "place VARCHAR," +
                "city VARCHAR," +
                "date DATETIME," +
                "report_image VARCHAR," +
                "user_name VARCHAR," +
                "user_image VARCHAR," +
                "sub_category1 VARCHAR," +
                "country VARCHAR," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP);");

        db.execSQL("CREATE TABLE IF NOT EXISTS user(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "username VARCHAR," +
                "email VARCHAR," +
                "image VARCHAR," +
                "number VARCHAR," +
                "f_name VARCHAR," +
                "l_name VARCHAR," +
                "gender VARCHAR," +
                "password VARCHAR," +
                "token VARCHAR," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP);");

    }

    public void InsertUserData(String name, String email, String image,
                              String number, String f_name, String l_name,
                              String gender, String password, String token) {

/*
        Cursor c = db.rawQuery("SELECT * FROM user where name='" + name + "' " +
                "and email ='" + email + "' and image='" + image + "'", null);
        if (c.getCount() == 0) {*/
            //        showMessage("Error", "No records found");
            Log.d("error", "no data in survey database");
            try {

                ContentValues insertValues = new ContentValues();
                insertValues.put("username",""+name);
                insertValues.put("email",""+ email);
                insertValues.put("image",""+ image);
                insertValues.put("number",""+ number);
                insertValues.put("f_name",""+ f_name);
                insertValues.put("l_name",""+ l_name);
                insertValues.put("gender", ""+gender);
                insertValues.put("password",""+ password);
                insertValues.put("token", ""+token);


                db.insert("user", null, insertValues);
            } catch (Exception e) {

            }
       /* }


        c = db.rawQuery("SELECT id FROM user where name='" + name + "' " +
                "and email ='" + email + "' and image='" + image + "'", null);
        if (c.getCount() == 0) {
            //        showMessage("Error", "No records found");
            Log.d("error", "no data in survey database");
            return -1;
        } else {
            if (c != null && c.moveToFirst()) ;
            do {
                Log.d("id", "=" + c.getString(0) + " " + name + " " + email + " " + image);
                return Integer.parseInt(c.getString(0));

            } while (c.moveToNext());
        }*/
    }

    public void InsertReportData(Report report) {
        try {

            ContentValues insertValues = new ContentValues();
            insertValues.put("vote", report.getVote());
            insertValues.put("description", report.getDescribtion());
            insertValues.put("sub_category1", report.getSubCategory1());
            insertValues.put("category", report.getCategory());
            insertValues.put("sub_category", report.getSubCategory());
            insertValues.put("longitude", "" + report.getLongitude());
            insertValues.put("latitude", "" + report.getLatitude());
            if (report.getPlace() != null && report.getCity() != null
                    && report.getCountry() != null) {
                insertValues.put("place", "" + report.getPlace());
                insertValues.put("city", "" + report.getCity());
                insertValues.put("country", "" + report.getCountry());
            }
            insertValues.put("date", "" + report.getDate());
            if (report.getReportImage() != null) {
                insertValues.put("report_image", "" + report.getReportImage());
            }
            insertValues.put("user_name", "" + report.getUserName());

            if (report.getUserImage() != null) {
                insertValues.put("user_image", "" + report.getUserImage());
            }

            db.insert("report", null, insertValues);
        } catch (Exception e) {
            Toast.makeText(mContext, "SQL_InsertReport " + e,
                    Toast.LENGTH_SHORT).show();

        }
    }

    public String[] GetUserData() {
        try {


            Cursor c = db.rawQuery("SELECT * FROM user ", null);
            if (c.getCount() == 0) {
                //        showMessage("Error", "No records found");
                Log.d("error", "no data in survey database");
                return null;
            } else {
                if (c != null && c.moveToFirst()) ;
                do {
                    Log.d("id get data", "=" + c.getString(0) + " " + c.getString(1) + " " + c.getString(2) + " " + c.getString(3));
                    return new String[]{c.getString(1), c.getString(2), c.getString(3)};

                } while (c.moveToNext());
            }
        } catch (Exception e) {
            Toast.makeText(mContext, "SQL_getuser " + e,
                    Toast.LENGTH_SHORT).show();

            return null;

        }

    }

    public void show() {

        Cursor c = db.rawQuery("SELECT * FROM report order by created_at DESC Limit 1", null);
        if (c.getCount() == 0) {
            //        showMessage("Error", "No records found");
            Log.d("error", "no data in survey database");
            return;
        }
        while (c.moveToNext()) {
            Log.d("debug", "Report**********");
            Log.d("id=", "" + c.getString(0));
            Log.d("vote", "" + c.getString(1));
            Log.d("description", "" + c.getString(2));
            Log.d("category", "" + c.getString(3));
            Log.d("sub_category", "" + c.getString(4));
            Log.d("longitude", "" + c.getString(5));
            Log.d("latitude", "" + c.getString(6));
            Log.d("place", "" + c.getString(7));
            Log.d("city", "" + c.getString(8));
            Log.d("date", "" + c.getString(9));
            Log.d("report_image", "" + c.getString(10));
            Log.d("user_name", "" + c.getString(11));
            Log.d("user_image", "" + c.getString(12));
            Log.d("sub_category1", "" + c.getString(13));
            if (c.getString(10) != null) {
                Log.d("other_text", "" + c.getString(10));
            }

            Log.d("Country", "" + c.getString(14));


        }
    }


    public ArrayList<Report> GetList(String category, String place, String country) {

        ArrayList<Report> reports = new ArrayList<>();
        try {
            Cursor c;

            if (!category.isEmpty() && !place.isEmpty() && !country.isEmpty()) {
                c = db.rawQuery("SELECT * FROM report where category='" + category + "' " +
                        "and place ='" + place + "' and city='" + country + "' order by created_at DESC", null);
            } else if (!category.isEmpty() && (place.isEmpty() || country.isEmpty())) {
                c = db.rawQuery("SELECT * FROM report where category='" + category + "'order by created_at DESC", null);
            } else if (category.isEmpty() && !place.isEmpty() && !country.isEmpty()) {
                c = db.rawQuery("SELECT * FROM report where place ='" + place + "' " +
                        "and city='" + country + "' order by created_at DESC", null);
            } else {
                c = db.rawQuery("SELECT * FROM report order by created_at DESC", null);

            }

            if (c.getCount() == 0) {
                //        showMessage("Error", "No records found");
                Log.d("error", "no data in survey database");
                return null;
            }
            while (c.moveToNext()) {
                Report report = new Report();

                Log.d("debug", "Report**********");
                Log.d("id=", "" + c.getString(0));
                report.setVote(Integer.parseInt(c.getString(1)));
                report.setDescribtion("" + c.getString(2));
                report.setCategory("" + c.getString(3));
                report.setSubCategory("" + c.getString(4));
                if (!c.getString(5).contains("null") && !c.getString(6).contains("null")) {

                    report.setLongitude(Double.valueOf(c.getString(5)));
                    report.setLatitude(Double.valueOf(c.getString(6)));
                }
                report.setPlace("" + c.getString(7));
                report.setCity("" + c.getString(8));
                report.setDate("" + c.getString(9));
                report.setReportImage("" + c.getString(10));
                report.setUserName("" + c.getString(11));
                report.setUserImage("" + c.getString(12));


                report.setCountry("" + c.getString(14));


                reports.add(report);

            }
        } catch (Exception e) {
            Toast.makeText(mContext, "SQL_Getlist " + e,
                    Toast.LENGTH_SHORT).show();

        }

        return reports;
    }

    public ArrayList<Map> GetList_map() {
        ArrayList<Map> maps = new ArrayList<>();
        try {

            Cursor c = db.rawQuery("SELECT longitude,latitude,category FROM report ", null);
            if (c.getCount() == 0) {
                //        showMessage("Error", "No records found");
                Log.d("error", "no data in survey database");
                return null;
            }
            while (c.moveToNext()) {
                Map map = new Map();

                Log.d("debug", "map**********");
                Log.d("longitude=", "" + c.getString(0));
                Log.d("latitude=", "" + c.getString(1));
                Log.d("category=", "" + c.getString(2));
                if (!c.getString(0).contains("null") && !c.getString(1).contains("null")) {
                    map.setLongitude(Double.parseDouble(c.getString(0)));
                    map.setLatitude(Double.parseDouble(c.getString(1)));
                }
                map.setCategory(c.getString(2));


                maps.add(map);

            }
        } catch (Exception e) {
            Toast.makeText(mContext, "SQL_getlistMap " + e,
                    Toast.LENGTH_SHORT).show();

        }

        return maps;
    }

    public void ClearTableData(String TABLE_NAME) {
        db.delete(TABLE_NAME, null, null);
    }

    public boolean ClearTableData(String TABLE_NAME, int id) {

        return db.delete(TABLE_NAME, "id" + "='" + id + "'", null) > 0;
    }

    /*public JSONObject Report(Report r) {
        Cursor c = db.rawQuery("SELECT * FROM report where id='" + id + "'", null);
        JSONObject obj = new JSONObject();

        if (c.getCount() == 0) {
            //        showMessage("Error", "No records found");
            Log.d("error", "no data in survey database");
            return null;
        }

        while (c.moveToNext()) {
            Log.d("debug", "ReportJSON**********");
            Log.d("id=", "" + c.getString(0));
            Log.d("vote", "" + c.getString(1));
            Log.d("description", "" + c.getString(2));
            Log.d("category", "" + c.getString(3));
            Log.d("sub_category", "" + c.getString(4));
            Log.d("longitude", "" + c.getString(5));
            Log.d("latitude", "" + c.getString(6));
            Log.d("city", "" + c.getString(8));
            Log.d("date", "" + c.getString(9));
            Log.d("report_image", "" + c.getString(10));
            Log.d("user_name", "" + c.getString(11));
            Log.d("user_image", "" + c.getString(12));
            Log.d("place", "" + c.getString(7));
            Log.d("class", "" + c.getString(13));
            if (c.getString(10) != null) {
                Log.d("other_text", "" + c.getString(10));
            }

            try {
                obj.put("Id", "" + c.getString(0));
                obj.put("Votes", "" + c.getString(1));
                obj.put("Comments", "" + c.getString(2));
                obj.put("ClassId", "" + c.getString(13));
                obj.put("CategoryId", "" + c.getString(3));
                obj.put("SubCategoryId", "" + c.getString(4));
                obj.put("longitude", "" + c.getString(5));
                obj.put("latitude", "" + c.getString(6));
                obj.put("PlaceId", "" + c.getString(7));
                obj.put("City", "" + c.getString(8));
                obj.put("date", "" + c.getString(9));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }
        return obj;
    }*/

    public float[] getGraphData(String place) {
        float[] count = new float[rang];

        try {
            Cursor c;
            Resources res = new Resources();


            if (place != null && !place.isEmpty()) {
                for (int i = 0; i < rang; i++) {
                    c = db.rawQuery("SELECT * FROM report where category='" +
                            res.category_graph[i] + "' and place ='" + place + "'", null);
                    Log.d("Count", res.category_graph[i] + "=" + c.getCount());
                    count[i] = (float) c.getCount();
                }
            } else {

                for (int i = 0; i < rang; i++) {
                    c = db.rawQuery("SELECT * FROM report where category='" +
                            res.category_graph[i] + "'", null);
                    Log.d("Count", res.category_graph[i] + "=" + c.getCount());
                    count[i] = (float) c.getCount();
                }
            }


        } catch (Exception e) {
            Toast.makeText(mContext, "SQL_Getlist " + e,
                    Toast.LENGTH_SHORT).show();

        }


        return count;

    }

}
