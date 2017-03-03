package app.vreport.com.Controller;

/**
 * Created by Sajjad Saleem on 1/1/2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import app.vreport.com.Activities.SplashScreen;
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
    ByteArrayEntity entity ;

    public ServerConnection(Context context){
        this.context=context;
        url="http://vreportapp.com/Help/Api/POST-api-Posts";
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
                JSONObject jsonObject=SplashScreen.sql.Report(3);
                if(jsonObject!=null) {
                    try {
                        entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    makeHTTPCall();
                }
            }
        }
                .execute(null, null, null);
    }


    // Make Http call to upload Image to Php server
    public void makeHTTPCall() {
        //   String url = "http://testing.vire-news.com/issues";
        //   String url="http://asmani.pk/webservices/upload_image.php";
        //    String url="http://testing.vire-news.com/check?str=123";
        //   prgDialog.setMessage("Invoking Php");
        AsyncHttpClient client = new AsyncHttpClient();

        client.post(context,url,entity, "application/json", new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http
                    // response code '200'

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Toast.makeText(context,
                                "Success "+responseBody.toString()+"",
                                Toast.LENGTH_LONG).show();
                        Log.d("Success",responseBody.toString());
                    }
                    // When the response returned by REST has Http
                    // response code other than '200' such as '404',
                    // '500' or '403' etc

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        // When Http response code is '404'
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
                            Toast.makeText(
                                    context,
                                    "Error Occured n Most Common Error: n1. Device not connected to Internetn2. Web App is not deployed in App servern3. App server is not runningn HTTP Status code : "
                                            + statusCode, Toast.LENGTH_LONG)
                                    .show();
                        }
                    }

                });
    }



}




