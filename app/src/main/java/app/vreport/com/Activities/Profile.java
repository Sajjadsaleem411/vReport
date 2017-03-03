package app.vreport.com.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import app.vreport.com.R;

public class Profile extends AppCompatActivity {

    TextView name ,email;
    ImageView image,close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String[] userdata = SplashScreen.sql.GetUserData();

        name=(TextView)findViewById(R.id.name);
        email=(TextView)findViewById(R.id.email);
        image=(ImageView)findViewById(R.id.image);
        close=(ImageView)findViewById(R.id.close);

        if (userdata[0] != null) {
            name.setText(userdata[0]);
        }
        if (userdata[1] != null) {
            email.setText(userdata[1]);
        }
        if (userdata[2] != null) {

            Picasso.with(this).load(userdata[2]).into(image);

        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
