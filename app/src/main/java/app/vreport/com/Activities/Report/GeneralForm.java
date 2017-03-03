package app.vreport.com.Activities.Report;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import app.vreport.com.Activities.HomeActivity;
import app.vreport.com.Activities.SplashScreen;
import app.vreport.com.Controller.MyLocationListener;
import app.vreport.com.Model.Report;
import app.vreport.com.Model.ReportData;
import app.vreport.com.Model.Resources;
import app.vreport.com.R;


public class GeneralForm extends AppCompatActivity implements View.OnClickListener {

    static int turn = -1;
    int prevoiusTurn = -1;
    boolean category_flag=false;

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


        /*Get current Location user */
        locationMangaer = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        MyLocationListener locationListener = new MyLocationListener(this);

        locationMangaer.requestLocationUpdates(LocationManager
                .GPS_PROVIDER, 5000, 10, locationListener);


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
                send.setClickable(false);
                Log.d("click","send");
                if (category_flag&&GeneralForm.report.getLatitude()!=null&&GeneralForm.report.getLongitude()!=null) {
                    try {
                        ProgressDialog dialog = ProgressDialog.show(GeneralForm.this, "",
                                "Sending. Please wait...", true);
                        dialog.setCanceledOnTouchOutside(false);
                    }
                    catch (Exception e){}
                    turn=-1;
                    GeneralForm.report.setDate(CurrentDate());
                    GeneralForm.report.setVote(0);
                    if (GeneralForm.report.getDescribtion() == null) {
                        GeneralForm.report.setDescribtion("No description provides");
                    }
                    SplashScreen.sql.InsertReportData();
                    SplashScreen.sql.show();
                    GeneralForm.report = new Report();
                    ReportActivity.mData = new ReportData();
                    Intent intent = new Intent(GeneralForm.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    if (GeneralForm.report.getLatitude()!=null&&GeneralForm.report.getLongitude()!=null){
                        HomeActivity.showMessage("GPS Is Disable", "Please check your Device's GPS.", GeneralForm.this);
                    }

                    else if(!category_flag) {
                        HomeActivity.showMessage("Incomplete", "Please select a category", GeneralForm.this);
                    }

                    send.setClickable(true);
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
        category_flag=true;
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
                String subcat=mResourceData.SubCatname[mSubCatIndex[PopUp.Select_SubCatIndex]][ReportActivity.mData.select_subcat + 1];

                ChangeTurn(ReportActivity.mData.Subcat_Turn);

                icons[ReportActivity.mData.Subcat_Turn + 1].setImageDrawable(ContextCompat.getDrawable
                        (this, mResourceData.SubCategory[mSubCatIndex[PopUp.Select_SubCatIndex]][ReportActivity.mData.select_subcat]));


                layout[ReportActivity.mData.Subcat_Turn].setBackgroundColor(getResources().getColor(R.color.bg));
                text_subcat[ReportActivity.mData.Subcat_Turn + 1].setTextColor(getResources().getColor(R.color.white));
                text_subcat[ReportActivity.mData.Subcat_Turn + 1].setText(subcat);
                report.setSubCategory1(subcat);
                category_flag=true;
                ReportActivity.mData.select_subcat = -1;

            }
        }

    }

}
