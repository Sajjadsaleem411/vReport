package app.vreport.com.Activities.Report;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import app.vreport.com.R;

public class ReportComment extends AppCompatActivity {
    EditText mCommentText;
    Button ok,cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_comment);
        mCommentText=(EditText)findViewById(R.id.comment);
        ok=(Button)findViewById(R.id.ok);
        cancel=(Button)findViewById(R.id.cancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralForm.report.setDescribtion(mCommentText.getText().toString());
                finish();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

    }

    @Override
    public void onBackPressed() {

        GeneralForm.report.setDescribtion(mCommentText.getText().toString());
        finish();
    }

}
