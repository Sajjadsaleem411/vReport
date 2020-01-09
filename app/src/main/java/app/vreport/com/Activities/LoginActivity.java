package app.vreport.com.Activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.Manifest.permission.READ_CONTACTS;

/*For Facebook*/
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;
import org.json.JSONObject;

import app.vreport.com.Controller.ServerConnection;
import app.vreport.com.Model.Report;
import app.vreport.com.R;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, OnClickListener {

    CallbackManager callbackManager;
    GraphRequest request;
    ServerConnection server;

    ProgressDialog progress_dialog;
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mPhoneNo;
    private View mProgressView;

    private LinearLayout login, register1, register2;
    private Button login_action, register_action;
    private boolean reg_flag = false;
    private EditText login_username, login_password;
    private EditText reg_username, reg_email, reg_number, reg_fname, reg_lname, reg_password, reg_Cpassword;
    private RadioButton male, female;
    private String gender = null;
//    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION};

        if (!SplashScreen.hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        if (getIntent().getExtras().getBoolean("TokenExpire")) {
            AccessToken.setCurrentAccessToken(null);
        }

        try {

            facebookSDKInitialize();

            setContentView(R.layout.activity_login);

            if (SplashScreen.sql.GetUserData() != null) {
                Intent intent = new Intent(LoginActivity.this, LoadingAcitity.class);
                startActivity(intent);
                finish();

            }
            login = (LinearLayout) findViewById(R.id.login_layout);
            register1 = (LinearLayout) findViewById(R.id.register_layout);
            register2 = (LinearLayout) findViewById(R.id.register_layout1);
            register1.setVisibility(View.GONE);
            register2.setVisibility(View.GONE);

            login_action = (Button) findViewById(R.id.login_action);
            register_action = (Button) findViewById(R.id.register_action);

            login_action.setOnClickListener(this);
            register_action.setOnClickListener(this);

            progress_dialog = new ProgressDialog(this);
            progress_dialog.setMessage("Loading....");
            progress_dialog.setCanceledOnTouchOutside(false);


            login_username = (EditText) findViewById(R.id.username);
            login_password = (EditText) findViewById(R.id.password);
            reg_email = (EditText) findViewById(R.id.email_reg);
            reg_username = (EditText) findViewById(R.id.username_reg);
            reg_number = (EditText) findViewById(R.id.phoneNo);
            reg_fname = (EditText) findViewById(R.id.Fname_reg);
            reg_lname = (EditText) findViewById(R.id.Lname_reg);
            reg_password = (EditText) findViewById(R.id.password_reg);
            reg_Cpassword = (EditText) findViewById(R.id.Cpassword_reg);
            male = (RadioButton) findViewById(R.id.male);
            female = (RadioButton) findViewById(R.id.female);
            male.setOnClickListener(this);
            female.setOnClickListener(this);
         /*   try {
                PackageInfo info = getPackageManager().getPackageInfo(
                        "app.vreport.com",
                        PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
            } catch (PackageManager.NameNotFoundException e) {

            }*/


            // Set up the login form.
      /*      mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
            populateAutoComplete();

            mPasswordView = (EditText) findViewById(R.id.password);
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.login || id == EditorInfo.IME_NULL) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });
      */
          /*  mPhoneNo = (EditText) findViewById(R.id.phoneNo);
            mPhoneNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.login || id == EditorInfo.IME_NULL) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });*/

          /*  Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
            mEmailSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    //      attemptLogin();
                    //       Toast.makeText(LoginActivity.this, "Nothing do right now!", Toast.LENGTH_LONG).show();
*//*                    SplashScreen.sql.InsertUserData("Sajjad", "muh.sajjadsaleem@gmail.com",
                            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAR8AAAAJDdjZGVlYzY1LWEwYTYtNDUzOS1iMGI5LTU0Mzk3NzgxNGVjYw.jpg");*//*

                    String number = mPhoneNo.getText().toString();
                    if (!TextUtils.isEmpty(number) && !isPhoneNoValid(number)) {
                        mPhoneNo.setError(getString(R.string.error_invalid_phoneNo));
                    } else {
                        SplashScreen.sql.InsertUserData(null, null, null, number);
                        Intent intent = new Intent(LoginActivity.this, LoadingAcitity.class);
                        startActivity(intent);
                        finish();

                    }


                }
            });*/

            //   mLoginFormView = findViewById(R.id.login_form);
            mProgressView = findViewById(R.id.login_progress);

        /*For Facebook*/
            LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
            loginButton.setReadPermissions("email");
            getLoginDetails(loginButton);
        } catch (Exception e) {
            Toast.makeText(this, "Login_Create " + e,
                    Toast.LENGTH_SHORT).show();

        }
    }

    protected void facebookSDKInitialize() {

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
 /*
  Register a callback function with LoginButton to respond to the login result.
 */

    protected void getLoginDetails(final LoginButton login_button) {

        // Callback registration
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult login_result) {

                request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {

                                String data[] = new String[3];
                                Log.d("dgraph", "login(): completed");
                                try {
                                    data[0] = object.getString("name");

                                    String name[] = object.getString("name").toString().split(" ");


                                    if (object.has("email")) {
                                        Log.d("demail", "In");
                                        Log.d("dEmail", object.getString("email"));
                                        data[1] = object.getString("email");

                                    }

                                    data[2] = object.getJSONObject("picture").getJSONObject("data").getString("url");

                                    Log.d("Data User", data[0] + " " + data[1] + " " + data[2]);

                                    SplashScreen.sql.ClearTableData("user");
                                    SplashScreen.sql.InsertUserData(data[0], data[1], data[2], null, name[0], name[1],
                                            "Gender", object.getString("gender"), login_result.getAccessToken().getToken());

                                    JSONObject obj = new JSONObject();

                                    //    Log.d("id=", "" + c.getString(0));
       /*     Log.d("vote", "" + r.getVote());
            Log.d("description", "" + r.getDescribtion());

*/
                                    Log.d("Fb_data", "" + response);
                                    try {
                                        obj.put("Email", object.getString("email"));
                                        obj.put("UserName", name[0]);
                                        obj.put("CellNumber", null);
                                        obj.put("FirstName", name[0]);
                                        obj.put("LastName", name[1]);
                                        obj.put("Gender", object.getString("gender"));
                                        obj.put("Provider", "Facebook");
                                        obj.put("ExternalAccessToken", login_result.getAccessToken().getToken());

                                        // obj.put("ApplicationUserId", "null");

                                        Log.d("Json+fb", "" + obj);

                                        progress_dialog.show();
                                        server = new ServerConnection(LoginActivity.this, 3, obj, progress_dialog);
                                        server.SubmitToServer();

                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                    /*  Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                                    intent.putExtra("UserID",SplashScreen.sql.InsertUserData(data[0],data[1],data[2]));

                                    startActivity(intent);
                                    Toast.makeText(LoginActivity.this, "Logged In!", Toast.LENGTH_LONG).show();
                                    finish();*/
                                    Log.d("graph", response.getRawResponse());
                                } catch (Exception e) {
                                    Toast.makeText(LoginActivity.this, "Login_FBSuccess " + e, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                                    intent.putExtra("UserID",SplashScreen.sql.InsertUserData(data[0],data[1],data[2]));

                                    startActivity(intent);
                                    finish();

                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "picture,name,gender,age_range,email");
                request.setParameters(parameters);
                Log.d("graph", "graphPath(): " + request.getGraphPath());
                request.executeAsync();

                AccessToken token = new AccessToken(login_result.getAccessToken().getToken(),
                        getResources().getString(R.string.facebook_app_id),
                        login_result.getAccessToken().getUserId(), null, null, null, null, null);
                AccessToken.setCurrentAccessToken(token);
                if (!AccessToken.getCurrentAccessToken().isExpired()) {
                    //     Toast.makeText(getApplicationContext(), "Token Expired", Toast.LENGTH_LONG).show();

                }

                login_button.setClickable(false);


            }

            @Override
            public void onCancel() {
                // code for cancellation

                Log.d("Error", "CAncel");
            }

            @Override
            public void onError(FacebookException exception) {
                //  code to handle error
                Log.d("fb_Error", "" + exception);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.e("data", data.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }


    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }*/


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        /*mEmailView.setError(null);
        mPasswordView.setError(null);
        */
        mPhoneNo.setError(null);

        // Store values at the time of the login attempt.
       /* String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
       */
        String number = mPhoneNo.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(number) && !isPhoneNoValid(number)) {
            mPasswordView.setError(getString(R.string.error_invalid_phoneNo));
            focusView = mPhoneNo;
            cancel = true;
        }
     /*   // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
*/
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            /*
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);*/
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isPhoneNoValid(String phoneNo) {
        //TODO: Replace this with your own logic
        return phoneNo.length() == 11 && phoneNo.length() > 0;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
/*
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });*/

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            //   mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.login_action) {
            SplashScreen.sql.InsertUserData(null, null, null, null, null, null, null, null, null);
            if (!login_username.getText().toString().isEmpty() &&
                    !login_password.getText().toString().isEmpty()) {
                if (SplashScreen.isOnline(this)) {
                    progress_dialog.show();
                    server = new ServerConnection(this, 1, Create_Json(0), progress_dialog);
                    server.SubmitToServer();
                } else {
                    SplashScreen.showMessage("Internet Off", "Please check your Internet Connection .", this);
                }

            } else if (login_username.getText().toString().isEmpty()) {
                SplashScreen.showMessage("Error", "Please enter your username", this);
            } else if (login_password.getText().toString().isEmpty()) {
                SplashScreen.showMessage("Error", "Please enter your password", this);
            }
         /*   Intent intent = new Intent(LoginActivity.this, LoadingAcitity.class);
            startActivity(intent);
            finish();*/

        } else if (view.getId() == R.id.register_action) {
            if (register_action.getText().toString().contentEquals("Register") && !reg_flag) {
                reg_flag = true;

                login_action.setVisibility(View.GONE);
                login.setVisibility(View.GONE);
                register1.setVisibility(View.VISIBLE);
                register_action.setText("Next");

            } else if (register_action.getText().toString().contentEquals("Register") && reg_flag) {
                if (!reg_lname.getText().toString().isEmpty() && !gender.isEmpty() &&
                        isPasswordValid(reg_password.getText().toString())
                        && reg_Cpassword.getText().toString().contentEquals(reg_password.getText().toString())) {

                    if (SplashScreen.isOnline(this)) {

                        progress_dialog.show();
                        server = new ServerConnection(this, 2, Create_Json(1), progress_dialog);
                        server.SubmitToServer();
                    } else {
                        SplashScreen.showMessage("Internet Off", "Please check your Internet Connection .", this);
                    }
                   /* Intent intent = new Intent(LoginActivity.this, LoadingAcitity.class);
                    startActivity(intent);
                    finish();*/
                } else if (reg_lname.getText().toString().isEmpty()) {
                    SplashScreen.showMessage("Error", "Please enter your last name", this);
                } else if (gender.isEmpty()) {
                    SplashScreen.showMessage("Error", "Please select your gender category", this);
                } else if (!isPasswordValid(reg_password.getText().toString())) {
                    SplashScreen.showMessage("Error", "Please enter your password.\nPassword length must be 6", this);
                } else if (!reg_Cpassword.getText().toString().contentEquals(reg_password.getText().toString())) {
                    SplashScreen.showMessage("Error", "Please recheck your password", this);
                }

            } else {
                if (!reg_username.getText().toString().isEmpty() &&
                        isEmailValid(reg_email.getText().toString()) &&
                        isPhoneNoValid(reg_number.getText().toString()) &&
                        !reg_fname.getText().toString().isEmpty()) {
                    login_action.setVisibility(View.GONE);
                    login.setVisibility(View.GONE);
                    register1.setVisibility(View.GONE);
                    register2.setVisibility(View.VISIBLE);
                    register_action.setText("Register");
                } else if (reg_username.getText().toString().isEmpty()) {
                    SplashScreen.showMessage("Error", "Please enter your username", this);
                } else if (!isEmailValid(reg_email.getText().toString())) {
                    SplashScreen.showMessage("Error", "Please enter your correct email", this);
                } else if (!isPhoneNoValid(reg_number.getText().toString())) {
                    SplashScreen.showMessage("Error", "Please enter your correct phone number", this);
                } else if (reg_fname.getText().toString().isEmpty()) {
                    SplashScreen.showMessage("Error", "Please enter your first name", this);
                }

            }

        } else if (view.getId() == R.id.male) {
            gender = "Male";
        } else if (view.getId() == R.id.female) {
            gender = "Female";
        }
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


    public JSONObject Create_Json(int code) {
        JSONObject obj = new JSONObject();

        if (code == 0) {
            try {
                SplashScreen.sql.ClearTableData("user");
                SplashScreen.sql.InsertUserData(login_username.getText().toString(),null,null,null,null,null,null,null,null);

                obj.put("grant_type", "password");
                obj.put("username", login_username.getText().toString());
                obj.put("password", login_password.getText().toString());

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        } else if (code == 1) {
            Log.d("debug", "Register**********");
            //    Log.d("id=", "" + c.getString(0));
       /*     Log.d("vote", "" + r.getVote());
            Log.d("description", "" + r.getDescribtion());
*/
            try {

                SplashScreen.sql.ClearTableData("user");
                SplashScreen.sql.InsertUserData(reg_username.getText().toString(), reg_email.getText().toString()
                        , null, reg_number.getText().toString(), reg_fname.getText().toString(),
                        reg_lname.getText().toString(), gender, reg_password.getText().toString(), null);

                obj.put("Email", reg_email.getText().toString());
                obj.put("Username", reg_username.getText().toString());
                obj.put("CellNumber", reg_number.getText().toString());
                obj.put("FirstName", reg_fname.getText().toString());
                obj.put("LastName", reg_lname.getText().toString());
                obj.put("Gender", gender);
                obj.put("Password", reg_password.getText().toString());
                obj.put("ConfirmPassword", reg_Cpassword.getText().toString());

                // obj.put("ApplicationUserId", "null");

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else {

        }

     /*   try {
            obj.put("Votes", "" + r.getVote());
            obj.put("Description", "" + r.getDescribtion());
            obj.put("ClassId", "" + r.getCategory());
            obj.put("CategoryId", "" + r.getSubCategory());

            // obj.put("ApplicationUserId", "null");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
*/
        Log.d("Login_json", "" + obj);
        return obj;
    }


}

