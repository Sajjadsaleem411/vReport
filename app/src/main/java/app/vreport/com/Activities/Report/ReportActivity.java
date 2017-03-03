package app.vreport.com.Activities.Report;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import app.vreport.com.R;
import app.vreport.com.Model.ReportData;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

    public static ReportData mData ;
    private ImageView[] category ;
    private int ImagesID[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        mData = new ReportData();
        category = new ImageView[5];
        ImagesID = new int[]{R.id.traffic, R.id.civic, R.id.crime, R.id.hazard, R.id.weather};

        for (int i = 0; i < ImagesID.length; i++) {
            category[i] = (ImageView) findViewById(ImagesID[i]);
            category[i].setOnClickListener(this);
        }
        /*Parent Layout*/
        LinearLayout ll = (LinearLayout) findViewById(R.id.reportLayout);
        ll.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.reportLayout) {
            finish();

        } else if (v==category[0]) {
            Intent intent = new Intent(ReportActivity.this, GeneralForm.class);
            intent.putExtra("Resource", 0);
            intent.putExtra("SubCategory1", -1);
            intent.putExtra("SubCategory2", -1);
            intent.putExtra("SubCategory3", -1);
            startActivity(intent);
            finish();

        } else if (v==category[1]) {
            Intent intent = new Intent(ReportActivity.this, GeneralForm.class);
            intent.putExtra("Resource", 1);
            intent.putExtra("SubCategory1", -1);
            intent.putExtra("SubCategory2", 0);
            intent.putExtra("SubCategory3", 1);
            startActivity(intent);
            finish();

        } else if (v==category[2]) {
            Intent intent = new Intent(ReportActivity.this, GeneralForm.class);
            intent.putExtra("Resource", 2);
            intent.putExtra("SubCategory1", -1);
            intent.putExtra("SubCategory2", -1);
            intent.putExtra("SubCategory3", 2);
            startActivity(intent);
            finish();

        } else if (v==category[3]) {

            Intent intent = new Intent(ReportActivity.this, GeneralForm.class);
            intent.putExtra("Resource", 3);
            intent.putExtra("SubCategory1", -1);
            intent.putExtra("SubCategory2", -1);
            intent.putExtra("SubCategory3", 3);
            startActivity(intent);
            finish();

        } else if (v==category[4]) {

            Intent intent = new Intent(ReportActivity.this, GeneralForm.class);
            intent.putExtra("Resource", 4);
            intent.putExtra("SubCategory1", -1);
            intent.putExtra("SubCategory2", -1);
            intent.putExtra("SubCategory3", -1);

            startActivity(intent);
            finish();
        }

    }
}
