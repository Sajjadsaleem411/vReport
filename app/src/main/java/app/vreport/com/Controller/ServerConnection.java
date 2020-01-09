package app.vreport.com.Controller;

/**
 * Created by Sajjad Saleem on 1/1/2017.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import app.vreport.com.Activities.LoadingAcitity;
import app.vreport.com.Activities.SplashScreen;
import app.vreport.com.Model.Report;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by Sajjad Saleem on 1/2/2017.
 */


public class ServerConnection {
    ProgressDialog progress_dialog;
    Context context;
    String url;
    RequestParams params = new RequestParams();
    ByteArrayEntity entity;
    int code = 0;
    JSONObject jsonObject;


    String[] urls = {"http://www.vreportapp.com/api/Posts",
            "http://www.vreportapp.com/Token",
            "http://www.vreportapp.com/api/Account/Register",
            "http://www.vreportapp.com/api/Account/RegisterExternal"};

    public ServerConnection(Context context, int code, JSONObject jsonObject,ProgressDialog progress_dialog) {
        this.context = context;
        this.code = code;
        this.jsonObject = jsonObject;
        this.progress_dialog=progress_dialog;
    }

    public ServerConnection(Context context, int code) {
        this.context = context;
        this.code = code;

    }

    public ServerConnection(Context context) {
        this.context = context;
    }


    // AsyncTask - To convert Image to String
    public void SubmitToServer() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            }

            @Override
            protected String doInBackground(Void... params) {

                return "";
            }

            @Override
            protected void onPostExecute(String msg) {

                if (jsonObject != null) {
                    try {
                        entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

                }

                makeHTTPCall();
            }
        }
                .execute(null, null, null);
    }


    // Make Http call to upload Image to Php server
    public void makeHTTPCall() {

        final AsyncHttpClient client = new AsyncHttpClient();

        client.post(context, urls[code], entity, "application/json", new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http
            // response code '200'

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseStr = new String(responseBody);

                if(code==2){

                    if(progress_dialog!=null){
                        progress_dialog.dismiss();
                    }
                    Intent intent = new Intent(context, LoadingAcitity.class);
                    context.startActivity(intent);
                    ((Activity) context).finish();

                }
                Log.d("Success", responseBody.toString());

                if (code == 0) {

                }
                Toast.makeText(context,
                        "Status code:" + statusCode + " Success!",
                        Toast.LENGTH_LONG).show();
            }
            // When the response returned by REST has Http
            // response code other than '200' such as '404',
            // '500' or '403' etc

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // When Http response code is '404'

                if(progress_dialog!=null){
                    progress_dialog.dismiss();
                }
                if (statusCode == 404) {
                    Toast.makeText(context,
                            "Status code:" + statusCode + "Requested resource not found",
                            Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(context,
                            "Status code:" + statusCode + "Something went wrong at server end",
                            Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(
                            context,
                            "Status code:" + statusCode + " Error Occured n Most Common Error: n1. Device not connected to Internetn2. Web App is not deployed in App servern3. App server is not runningn HTTP Status code : "
                                    + statusCode, Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        client.get(urls[code], new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if(code==2){

                    if(progress_dialog!=null){
                        progress_dialog.dismiss();
                    }
                    Intent intent = new Intent(context, LoadingAcitity.class);
                    context.startActivity(intent);
                    ((Activity) context).finish();

                }

                else if (code == 0) {
                    if (responseBody != null) {

                        String responseStr = new String(responseBody);
                        Log.d("Server_msg", responseStr);

                        //   JSONObject jsnobject = null;
                        try {
                            JSONArray jsonarray = new JSONArray(responseStr);

                            for (int i = 0; i < jsonarray.length(); i++) {
                                Report report=new Report();
                                JSONObject jsnobject = jsonarray.getJSONObject(i);
                                report.vote= Integer.parseInt(jsnobject.getString("Votes"));
                                Log.d("Get Report*******", "*****=" + i + "=****");
                                Log.d("Get Votes", "" + jsnobject.getString("Votes"));
                                Log.d("Get ClassId ", "" + jsnobject.getString("ClassId"));
                                Log.d("Get CategoryId", "" + jsnobject.getString("CategoryId"));
                                Log.d("Get SubCategoryId", "" + jsnobject.getString("SubCategoryId"));
                                Log.d("Get longitude ", "" + jsnobject.getString("Longitude"));
                                Log.d("Get latitude", "" + jsnobject.getString("Latitude"));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Error", "" + e);
                        }
                    }

                }
            }


            // When the response returned by REST has Http
            // response code other than '200' such as '404',
            // '500' or '403' etc

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // Hide Progress Dialog
                //    prgDialog.hide();
                // When Http response code is '404'

                if(progress_dialog!=null){
                    progress_dialog.dismiss();
                }
                if (statusCode == 404) {
                    Toast.makeText(context,
                            "Requested resource not found",
                            Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(context,
                            "Something went wrong at server end",
                            Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {

                    Log.d("", "Error Occured n Most Common Error: n1. Device not connected to Internetn2. Web App is not deployed in App servern3. App server is not runningn HTTP Status");

                }
            }

        });
    }


}




