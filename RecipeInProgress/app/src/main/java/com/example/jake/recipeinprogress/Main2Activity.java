package com.example.jake.recipeinprogress;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static android.Manifest.permission.READ_CONTACTS;


public class Main2Activity extends AppCompatActivity implements LoaderCallbacks<Cursor>{

    private Main2Activity.RegisterTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mUsernameView;
    private EditText mAgeView;
    private EditText mNameView;
    private View mProgressView;
    private View mRegisterFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mUsernameView = (EditText) findViewById(R.id.username);
        mAgeView = (EditText) findViewById(R.id.age);
        mNameView = (EditText) findViewById(R.id.name);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.register_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });


        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);
    }

    private void openRegister() {
        Intent in = new Intent(this, Main2Activity.class);
        startActivity(in);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String username = mUsernameView.getText().toString();
        String age = mAgeView.getText().toString();
        String name = mNameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
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

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new RegisterTask(username, password, email, age, name);
            mAuthTask.execute((String) "hello");
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.length() > 3;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
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

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

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
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
               Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), Main2Activity.ProfileQuery.PROJECTION,

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
        //List<String> emails = new ArrayList<>();
        //cursor.moveToFirst();
        //while (!cursor.isAfterLast()) {
        //    emails.add(cursor.getString(LoginActivity.ProfileQuery.ADDRESS));
        //    cursor.moveToNext();
        //}

        //addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

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
    public class RegisterTask extends AsyncTask<String, Void, String> {

        private final String mUsername;
        private final String mPassword;
        private final String mEmail;
        private final String mAge;
        private final String mName;

        RegisterTask(String username, String password, String email, String age, String name) {
            mUsername = username;
            mPassword = password;
            mEmail = email;
            mAge = age;
            mName = name;
        }

        @Override
        protected void onPreExecute(){
            showProgress(true);
        }//onPreExecute

        @Override
        protected String doInBackground(String... arg0) {
            // TODO: attempt authentication against a network service.

            try {

                URL url = new URL("http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/register"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                //add name pair values to the connection

                postDataParams.put("username", mUsername);
                postDataParams.put("password", mPassword);
                postDataParams.put("username", mEmail);
                postDataParams.put("age", mAge);
                postDataParams.put("username", mName);


                Log.e("params",postDataParams.toString());
                Log.e("URL",url.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.connect();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(postDataParams.toString());

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();
                Log.e("responseCode", "responseCode "+responseCode);
                if (responseCode == HttpsURLConnection.HTTP_OK) {//code 200 connection OK
                    //this part is to capture the server response
                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));
                    //Log.e("response",conn.getInputStream().toString());

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    do{
                        sb.append(line);
                        Log.e("MSG sb",sb.toString());
                    }while ((line = in.readLine()) != null) ;

                    in.close();
                    Log.e("response",conn.getInputStream().toString());
                    Log.e("textmessage",sb.toString());
                    return sb.toString();//server response message

                }
                else {

                    return new String("false4 : "+responseCode);
                }
            }
            catch(Exception e){
                //error on connection
                return new String("Exception: " + e.getMessage());
            }
        }//end doInBackground

        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getApplicationContext(), result,
            //Toast.LENGTH_LONG).show();
            //return result;
            //call method to handle after verification


            mAuthTask = null;
            showProgress(false);

            RegisterVerification(result);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){
            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }//end getPostDataString
        //Log.i("result",result.toString());
        return result.toString();
    }//end UserLoginTask

    private void RegisterVerification(String svrmsg){
        String savemsg="";
        //if new record successfully saved
        Toast.makeText(this, "svrmsghelp:"+svrmsg,Toast.LENGTH_LONG).show();
        if (svrmsg.length() > 4){

            savemsg="Logged in...";

        }
        else {//save failed
            savemsg="Email/password not match!";
        }

        //**********common dialog box
        //AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        //builder1.setTitle("User Login");
        //builder1.setMessage(savemsg);
        //builder1.setCancelable(false);
        //builder1.setPositiveButton("ok", new DialogInterface.OnClickListener() {

        //    public void onClick(DialogInterface dialog, int which) {
        //        dialog.cancel();
        //    }
        //}).create().show();

        //call ListUsers page if servermessage is success
        if (svrmsg.length() > 4){

            Intent in = new Intent(this, MainActivity.class);
            startActivity(in);
        }

    }//end loginVerification
}
