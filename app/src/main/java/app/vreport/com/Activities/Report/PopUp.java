package app.vreport.com.Activities.Report;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.vreport.com.Model.Resources;
import app.vreport.com.R;

public class PopUp extends AppCompatActivity implements View.OnClickListener {

    ImageView[] subCat = new ImageView[6];//Sub Category

    /*Sub category ImageView ID's*/
    private int ImagesID[] = {R.id.subcategory1, R.id.subcategory2, R.id.subcategory3,
            R.id.subcategory4, R.id.subcategory5, R.id.subcategory6};


    LinearLayout layout[] = new LinearLayout[6];
    int LayoutID[] = {R.id.layout1, R.id.layout2, R.id.layout3, R.id.layout4, R.id.layout5, R.id.layout6};

    private TextView[] text_subcat = new TextView[7];
    private int TextViewID[] = {R.id.SubCat_title, R.id.text_subcat1, R.id.text_subcat2, R.id.text_subcat3, R.id.text_subcat4
            , R.id.text_subcat5, R.id.text_subcat6};


    int subCatIndex;
    Resources data = new Resources();
    static int Select_SubCatIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);


        /*Set window on bottom*/
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.gravity = Gravity.BOTTOM;


        subCatIndex = getIntent().getExtras().getInt("SubCategory");
        Select_SubCatIndex = getIntent().getExtras().getInt("SubCategory Index");

        /*Initialize all sub category*/
        for (int i = 0; i < data.SubCategory[subCatIndex].length; i++) {
            subCat[i] = (ImageView) findViewById(ImagesID[i]);
            subCat[i].setImageDrawable(ContextCompat.getDrawable(this, data.SubCategory[subCatIndex][i]));
            subCat[i].setOnClickListener(this);

        }
         /*Image and Text layout */
        for (int i = 0; i < LayoutID.length; i++) {
            layout[i] = (LinearLayout) findViewById(LayoutID[i]);
            layout[i].setOnClickListener(this);
        }
        /*Set Sub category text*/
        for (int j = 0; j < data.SubCatname[subCatIndex].length; j++) {
            text_subcat[j] = (TextView) findViewById(TextViewID[j]);
            text_subcat[j].setText(data.SubCatname[subCatIndex][j]);
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        /*Window Size*/
        getWindow().setLayout((int) (width), (int) (height * 0.45));

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.subcategory1 || v == layout[0]) {

            /*Set Sub category(in sub category) Index*/
            ReportActivity.mData.select_subcat = 0;

        //    GeneralForm.report.setSubCategory(data.SubCatname[subCatIndex][ReportActivity.mData.select_subcat+1]);
            /*Select Sub category Index*/
            ReportActivity.mData.Subcat_Turn = Select_SubCatIndex;

            finish();

        } else if (v.getId() == R.id.subcategory2 || v == layout[1]) {
            /*Set Sub category(in sub category) Index*/
            ReportActivity.mData.select_subcat = 1;

       //     GeneralForm.report.setSubCategory(data.SubCatname[subCatIndex][ReportActivity.mData.select_subcat+1]);
            /*Select Sub category Index*/
            ReportActivity.mData.Subcat_Turn = Select_SubCatIndex;
            finish();

        } else if (v.getId() == R.id.subcategory3 || v == layout[2]) {

             /*Set Sub category(in sub category) Index*/
            ReportActivity.mData.select_subcat = 2;

         //   GeneralForm.report.setSubCategory(data.SubCatname[subCatIndex][ReportActivity.mData.select_subcat+1]);
            /*Select Sub category Index*/
            ReportActivity.mData.Subcat_Turn = Select_SubCatIndex;
            finish();

        } else if (v.getId() == R.id.subcategory4 || v == layout[3]) {
             /*Set Sub category(in sub category) Index*/
            ReportActivity.mData.select_subcat = 3;

          //  GeneralForm.report.setSubCategory(data.SubCatname[subCatIndex][ReportActivity.mData.select_subcat+1]);
            /*Select Sub category Index*/
            ReportActivity.mData.Subcat_Turn = Select_SubCatIndex;
            finish();

        } else if (v.getId() == R.id.subcategory5 || v == layout[4]) {     /*Set Sub category(in sub category) Index*/
            ReportActivity.mData.select_subcat = 4;

        //    GeneralForm.report.setSubCategory(data.SubCatname[subCatIndex][ReportActivity.mData.select_subcat+1]);
            /*Select Sub category Index*/
            ReportActivity.mData.Subcat_Turn = Select_SubCatIndex;

            finish();

        } else if (v.getId() == R.id.subcategory6 || v == layout[5]) {     /*Set Sub category(in sub category) Index*/
            ReportActivity.mData.select_subcat = 6;

        //    GeneralForm.report.setSubCategory(data.SubCatname[subCatIndex][ReportActivity.mData.select_subcat+1]);
          /*Select Sub category Index*/
            ReportActivity.mData.Subcat_Turn = Select_SubCatIndex;

            finish();

        }
    }
}
