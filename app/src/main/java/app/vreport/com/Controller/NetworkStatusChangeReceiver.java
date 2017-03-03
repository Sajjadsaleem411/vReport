package app.vreport.com.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.facebook.internal.Utility;

/**
 * Created by Muhammad Sajjad on 2/24/2017.
 */
public class NetworkStatusChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {/*
        boolean status = Utility.isInternetAvailable(context);
        if (!status)
        {*/
            Toast.makeText(context, "Network Error !", Toast.LENGTH_SHORT).show();
      //  }
    }


}