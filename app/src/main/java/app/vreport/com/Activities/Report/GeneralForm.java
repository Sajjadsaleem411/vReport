package app.vreport.com.Activities.Report;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.ads.AdRequest;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import app.vreport.com.Activities.HomeActivity;
import app.vreport.com.Activities.SplashScreen;
import app.vreport.com.Controller.MyLocationListener;
import app.vreport.com.Model.Report;
import app.vreport.com.Model.ReportData;
import app.vreport.com.Model.Resources;
import app.vreport.com.R;
import app.vreport.com.Controller.*;

public class GeneralForm extends AppCompatActivity implements View.OnClickListener {

    static int turn = -1;
    int prevoiusTurn = -1;
    boolean category_flag = false;

    /*Popup Fields Needs*/
    location mlocation;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    Button search_area;
    EditText text_area;
    String[] Address;

    /********/


    public static Report report = new Report();

    private ImageView[] icons = new ImageView[4];
    private TextView[] text_subcat = new TextView[4];

    private int ImagesID[] = {R.id.icon, R.id.category1, R.id.category2, R.id.category3};
    private int TextViewID[] = {R.id.title, R.id.text_cat1, R.id.text_cat2, R.id.text_cat3};
    Intent mIntent;
    ImageView mPhotoicon;
    ImageView mClose;
    LinearLayout mComment;
    Button mCommenticon;
    Bitmap photo;
    TextView mCommenttext;
    int mResourceIndex;
    int mSubRecource;
    public Resources mResourceData;
    Button send, later;
    TextView title;
    int mSubCatIndex[] = new int[3];


    private final int CAMERA_REQUEST = 1888;

    String encodedString;
    String imgPath, fileName;
    Bitmap bitmap;
    private LocationManager locationMangaer = null;
    LinearLayout layout[] = new LinearLayout[3];
    int LayoutID[] = {R.id.layout1, R.id.layout2, R.id.layout3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_form);

        // The request code used in ActivityCompat.requestPermissions()
// and returned in the Activity's onRequestPermissionsResult()


        /*Get Index of feature and also sub category array*/
        mResourceIndex = getIntent().getExtras().getInt("Resource");
        mSubCatIndex[0] = getIntent().getExtras().getInt("SubCategory1");
        mSubCatIndex[1] = getIntent().getExtras().getInt("SubCategory2");
        mSubCatIndex[2] = getIntent().getExtras().getInt("SubCategory3");

        mlocation = new location(this);
        mResourceData = new Resources();
        GeneralForm.report.setCategory(mResourceData.name[mResourceIndex][0]);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }/*
        locationListener = new MyLocationListener(this);
        locationMangaer.requestLocationUpdates(LocationManager
                .GPS_PROVIDER, 5000, 10, locationListener);*/
        mInitialization();


        GPSTracker tracker = new GPSTracker(this);
        mlocation.getLocationFromAddress(tracker.getLatitude(), tracker.getLongitude());

 /*       *//*Get current Location user *//*
        locationMangaer = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        MyLocationListener locationListener = new MyLocationListener(this);

        locationMangaer.requestLocationUpdates(LocationManager
                .GPS_PROVIDER, 5000, 10, locationListener);

*/
    }

    /*Initialize All field's*/
    private void mInitialization() {

        title = (TextView) findViewById(R.id.title);
        title.setText(mResourceData.name[mResourceIndex][0]);

        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(this);

        later = (Button) findViewById(R.id.later);
        later.setOnClickListener(this);

        mPhotoicon = (ImageView) this.findViewById(R.id.image);

        mPhotoicon.setOnClickListener(this);

        mClose = (ImageView) findViewById(R.id.close);
        mClose.setOnClickListener(this);

        mComment = (LinearLayout) findViewById(R.id.commentlayout);
        mComment.setOnClickListener(this);

        mCommenticon = (Button) findViewById(R.id.commenticon);
        mCommenticon.setOnClickListener(this);

        mCommenttext = (TextView) findViewById(R.id.commenttext);

        /*Set Sub category Icons*/
        for (int i = 0; i < mResourceData.Images[mResourceIndex].length; i++) {
            icons[i] = (ImageView) findViewById(ImagesID[i]);
            icons[i].setImageDrawable(ContextCompat.getDrawable(this, mResourceData.Images[mResourceIndex][i]));
            icons[i].setOnClickListener(this);
        }
        /*Set Sub category text*/
        for (int i = 0; i < mResourceData.name[mResourceIndex].length; i++) {
            text_subcat[i] = (TextView) findViewById(TextViewID[i]);
            text_subcat[i].setText(mResourceData.name[mResourceIndex][i]);
        }
        /*Image and Text layout */
        for (int i = 0; i < LayoutID.length; i++) {
            layout[i] = (LinearLayout) findViewById(LayoutID[i]);
            layout[i].setOnClickListener(this);
        }

        /*Set Send Button click Effect*/
        send.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        send.setBackgroundColor(getResources().getColor(R.color.transBlack));
                     /*v.setBackgroundColor(getResources().getColor(R.color.transBlack));
                     v.invalidate();
                     */

                        //         GeneralForm.report.setUserImage("https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAR8AAAAJDdjZGVlYzY1LWEwYTYtNDUzOS1iMGI5LTU0Mzk3NzgxNGVjYw.jpg");
                        //         GeneralForm.report.setUserName("Sajjad");

/*
            Log.d("Report Date",GeneralForm.report.getDate());
            Log.d("Report Longitude",""+GeneralForm.report.getLongitude());
            Log.d("Report Latitude",""+GeneralForm.report.getLatitude());
//            Log.d("Report Describtion",GeneralForm.report.getDescribtion());
//            Log.d("Report ImagePath",GeneralForm.report.getReportImage());
            Log.d("Report Category",GeneralForm.report.getCategory());
            Log.d("Report SubCat",GeneralForm.report.getSubCategory());*/

                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        send.setBackgroundColor(getResources().getColor(R.color.SkyBlue));
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
        /* set Later Button click Effect*/
        later.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        later.setBackgroundColor(getResources().getColor(R.color.transBlack));
                     /*v.setBackgroundColor(getResources().getColor(R.color.transBlack));
                     v.invalidate();
                     */
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        later.setBackgroundColor(getResources().getColor(R.color.Button));
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });

    }

    /*When Image button click*/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /*For get camera image*/
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP


            Uri selectedImage = getImageUri(getApplicationContext(), photo);
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            // Get the cursor
            Cursor cursor = this.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imgPath = cursor.getString(columnIndex);
            Log.d("imgPath", imgPath);
            cursor.close();/*
                ImageView imgView = (ImageView) findViewById(R.id.imgView);
                // Set the Image in ImageView
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgPath));*/
            // Get the Image's file name
            String fileNameSegments[] = imgPath.split("/");
            Log.d("fileNameSegments.Length", "" + fileNameSegments.length);
            Log.d("fileNameSegments[0]", fileNameSegments[0]);
            fileName = fileNameSegments[fileNameSegments.length - 1];
            Log.d("fileName", fileName);
            // Put file name in Async Http Post Param which will used in Php web app

            //   params.put("filename", fileName);
            BitmapFactory.Options options = null;
            options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            bitmap = BitmapFactory.decodeFile(imgPath,
                    options);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Must compress the Image to reduce image size to make upload easy
            bitmap.compress(Bitmap.CompressFormat.JPEG, 55, stream);
            byte[] byte_arr = stream.toByteArray();
            // Encode Image to String
            encodedString = Base64.encodeToString(byte_arr, 0);
            Log.d("string", encodedString);

            File f = new File(imgPath);
            Picasso.with(this).load(f).into(mPhotoicon);
            GeneralForm.report.setReportImage(imgPath);

        }
        /*For Location search*/

        else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.d("Tag", "Place: " + place.getName());

                mlocation.getLocationFromAddress((String) place.getAddress());

                Address = ((String) place.getAddress()).split(", ");
                //    text_area.setText(place.getName());
                text_area.setText(Address[0] + ", " + Address[1]);
                report.setPlace(Address[0]);
                report.setCity(Address[1]);
                if (Address[2] != null)
                    report.setCountry(Address[2]);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("Tag", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    @Override
    public void onClick(View v) {

        if (v == mPhotoicon) {
            mIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(mIntent, CAMERA_REQUEST);

        } else if (v == mComment || v == mCommenticon) {

            mIntent = new Intent(GeneralForm.this, ReportComment.class);
            startActivity(mIntent);

        } else if (v == send) {

            if (SplashScreen.isOnline(GeneralForm.this)) {
                GPSTracker tracker = new GPSTracker(this);
                Log.d("Tracker Long", "" + tracker.getLongitude());
                Log.d("Tracker Lat", "" + tracker.getLatitude());
                report.setLongitude(tracker.getLongitude());
                report.setLatitude(tracker.getLatitude());

                mlocation.getLocationFromAddress(tracker.getLatitude(), tracker.getLongitude());

                send.setClickable(false);
                Log.d("click", "send");
                Log.d("Latitude", "" + GeneralForm.report.getLatitude());
                Log.d("Longitude", "" + GeneralForm.report.getLongitude());
                if (category_flag && GeneralForm.report.getLatitude() != null && GeneralForm.report.getLongitude() != null) {

                    Edit_PopUp();

                }
                else if (GeneralForm.report.getLatitude() == null || GeneralForm.report.getLongitude() == null) {
                    HomeActivity.showMessage("GPS Is Disable", "Please check your Device's GPS.", GeneralForm.this);
                } else if (!category_flag) {
                    HomeActivity.showMessage("Incomplete", "Please select a category", GeneralForm.this);
                }


                send.setClickable(true);
            }
            else {
                HomeActivity.showMessage("Internet Off", "Please check your Internet Connection .", GeneralForm.this);

            }


        } else if (v == later) {
            GeneralForm.report = new Report();
            ReportActivity.mData = new ReportData();
            finish();

        } else if (v == mClose) {
            GeneralForm.report = new Report();
            ReportActivity.mData = new ReportData();
            finish();
        } else if (v == layout[0] || v == icons[1]) {
            Log.d("Test", mResourceData.name[mResourceIndex][1]);
            if (mSubCatIndex[0] != -1) {
                Call_SubCategory(0);
            } else {
                ApplySelectedEffect(0);

            }


        } else if (v == layout[1] || v == icons[2]) {
            Log.d("Test", mResourceData.name[mResourceIndex][1]);
            if (mSubCatIndex[1] != -1) {
                Call_SubCategory(1);
            } else {

                ApplySelectedEffect(1);
            }
        } else if (v == layout[2] || v == icons[3]) {
            Log.d("Test", mResourceData.name[mResourceIndex][1]);
            if (mSubCatIndex[2] != -1) {
                Call_SubCategory(2);
            } else {

                ApplySelectedEffect(2);
            }
        }
    }

    public void ApplySelectedEffect(int i) {
        category_flag = true;
        ChangeTurn(i);
        layout[i].setBackgroundColor(getResources().getColor(R.color.bg));
        text_subcat[i + 1].setTextColor(getResources().getColor(R.color.white));
        GeneralForm.report.setSubCategory(mResourceData.name[mResourceIndex][i + 1]);
        report.setSubCategory1(null);
    }

    public void Call_SubCategory(int i) {
        mIntent = new Intent(GeneralForm.this, PopUp.class);
        mIntent.putExtra("SubCategory", mSubCatIndex[i]);
        mIntent.putExtra("SubCategory Index", i);
        report.setSubCategory(mResourceData.name[mResourceIndex][i]);

        startActivity(mIntent);

    }

    public static String CurrentDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss", Locale.US);
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }

    private void ChangeTurn(int t) {
        if (turn != -1) {
            prevoiusTurn = turn;
        }
        if (prevoiusTurn != -1) {
            layout[prevoiusTurn].setBackgroundColor(getResources().getColor(R.color.transBlack));
            text_subcat[prevoiusTurn + 1].setTextColor(getResources().getColor(R.color.textColor));
            icons[prevoiusTurn + 1].setImageDrawable(ContextCompat.getDrawable
                    (this, mResourceData.Images[mResourceIndex][prevoiusTurn + 1]));

            text_subcat[prevoiusTurn + 1].setText(mResourceData.name[mResourceIndex][prevoiusTurn + 1]);

            report.setSubCategory1(null);

        }
        turn = t;
    }

    @Override
    protected void onResume() {
        super.onResume();
       /* if(ReportActivity.mData.photo!=null){
            mPhotoicon.setImageBitmap(ReportActivity.mData.photo);
            //   ReportActivity.mData.photo=photo;
        }*/

        if (ReportActivity.mData != null) {
            if (GeneralForm.report.getDescribtion() != null && !GeneralForm.report.getDescribtion().isEmpty()) {

                mCommenttext.setText(GeneralForm.report.getDescribtion());
                //   ReportActivity.mData.photo=photo;
                //         mCommenttext.setText("Successfully add!");

            }
            if (ReportActivity.mData.select_subcat != -1) {
                String subcat = mResourceData.SubCatname[mSubCatIndex[PopUp.Select_SubCatIndex]][ReportActivity.mData.select_subcat + 1];

                ChangeTurn(ReportActivity.mData.Subcat_Turn);

                icons[ReportActivity.mData.Subcat_Turn + 1].setImageDrawable(ContextCompat.getDrawable
                        (this, mResourceData.SubCategory[mSubCatIndex[PopUp.Select_SubCatIndex]][ReportActivity.mData.select_subcat]));


                layout[ReportActivity.mData.Subcat_Turn].setBackgroundColor(getResources().getColor(R.color.bg));
                text_subcat[ReportActivity.mData.Subcat_Turn + 1].setTextColor(getResources().getColor(R.color.white));
                text_subcat[ReportActivity.mData.Subcat_Turn + 1].setText(subcat);
                report.setSubCategory1(subcat);
                category_flag = true;
                ReportActivity.mData.select_subcat = -1;

            }
        }

    }


    public void Edit_PopUp() {
        final long[] date = new long[1];
        final View dialogView = View.inflate(GeneralForm.this, R.layout.edit_dialog, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(GeneralForm.this).create();

        text_area = (EditText) dialogView.findViewById(R.id.search);
        text_area.setText(GeneralForm.report.getPlace() + ", " + GeneralForm.report.getCity());

        search_area = (Button) dialogView.findViewById(R.id.search_area);
        search_area.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //      performSearch();
                    return true;
                }
                return false;
            }
        });

        search_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    /*Search place code*/
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(GeneralForm.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                            .build();

                    intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .setFilter(typeFilter)
                                    .build(GeneralForm.this);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });


        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            //dd-MM-yyyy kk:mm:ss
            String date_time = "";

            @Override
            public void onClick(View view) {
            /*Location Work*/



            /*Date & Time work */
                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());

                date[0] = calendar.getTimeInMillis();
                date_time += datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear();


                //   date_time+=" "+timePicker.getCurrentHour()+":"+timePicker.getCurrentMinute()+":";

                Log.d("Date", "= " + datePicker.getDayOfMonth() + ":" + (datePicker.getMonth() + 1) + "," + datePicker.getYear());


              /*  if(timePicker.getCurrentHour()>12){
                    date_time+=" "+(timePicker.getCurrentHour()-12)+":"+timePicker.getCurrentMinute()+":PM";
                }
                else {*/
                date_time += " " + timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
                //}
                turn = -1;
                GeneralForm.report.setDate(date_time);
                GeneralForm.report.setVote(0);
                if (GeneralForm.report.getDescribtion() == null) {
                    GeneralForm.report.setDescribtion("No description provides");
                }
                SplashScreen.sql.InsertReportData(GeneralForm.report);
                SplashScreen.sql.show();

                Log.d("Date_Time=", date_time);
                JSONObject object = Report_Json(GeneralForm.report);
                Log.d("Json", "" + object);

                ServerConnection serverConnection = new ServerConnection(GeneralForm.this,0,object,null);
                serverConnection.SubmitToServer();
                Intent intent = new Intent(GeneralForm.this, HomeActivity.class);
                startActivity(intent);
                finish();
                GeneralForm.report = new Report();
                ReportActivity.mData = new ReportData();

                alertDialog.dismiss();
            }
        });
        alertDialog.setView(dialogView);
        alertDialog.show();

    }

/*

    Log.d("class", "" + r.getCategory());
    Log.d("category", "" + r.getSubCategory());
    Log.d("sub_category", "" + r.getSubCategory1());
    Log.d("longitude", "" + r.getLongitude());
    Log.d("latitude", "" + r.getLatitude());
    Log.d("city", "" + r.getCity());
    Log.d("date", "" + r.getDate());
    Log.d("report_image", "" + r.getReportImage());
    Log.d("user_name", "" + r.getUserName());
    Log.d("user_image", "" + r.getUserImage());
    Log.d("place", "" + r.getPlace());
*/

    public JSONObject Report_Json(Report r) {
        JSONObject obj = new JSONObject();

        Log.d("debug", "ReportJSON**********");
        //    Log.d("id=", "" + c.getString(0));
        Log.d("vote", "" + r.getVote());
        Log.d("description", "" + r.getDescribtion());

        try {
            obj.put("Votes", "" + r.getVote());
            obj.put("Description", "" + r.getDescribtion());
            obj.put("ClassId", "" + r.getCategory());
            obj.put("CategoryId", "" + r.getSubCategory());

            if(r.getSubCategory1()!=null)
            obj.put("SubCategoryId", "" + r.getSubCategory1());
            obj.put("Longitude", "" + r.getLongitude());
            obj.put("Latitude", "" + r.getLatitude());
            obj.put("Place", "" + r.getPlace());
            obj.put("City", "" + r.getCity());
            obj.put("StreetNumber",JSONObject.NULL );
            obj.put("Street",JSONObject.NULL );
            obj.put("State",JSONObject.NULL );
            obj.put("CountryCode",JSONObject.NULL );
            obj.put("PostCode",JSONObject.NULL );
            obj.put("District",JSONObject.NULL );
            obj.put("CreatedDate", "" +ConvertDate(r.date));


           // obj.put("ApplicationUserId", "null");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return obj;
    }

    String ConvertDate(String date) throws ParseException {
        SimpleDateFormat fromFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        fromFormat.setLenient(false);

        SimpleDateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        toFormat.setLenient(false);
        Date d = fromFormat.parse(date);
        System.out.println(""+toFormat.format(d));


        return ""+toFormat.format(d);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }
}
