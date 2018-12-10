package com.example.kchu0.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class SignupActivity extends AppCompatActivity {

    TextView return_login;
    private EditText edit_name;
    private EditText edit_username;
    private EditText edit_password;
    private EditText edit_age;
    private EditText edit_email;
    private Button create_account;

    private UserCreateTask mAuthTask = null;
    private int response_code_g;
    private View mProgressView;
    private View mSignUpFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edit_name = (EditText) findViewById(R.id.name);
        edit_username = (EditText) findViewById(R.id.username);
        edit_password = (EditText) findViewById(R.id.password);
        edit_age = (EditText) findViewById(R.id.age);
        edit_email = (EditText) findViewById(R.id.email);
        create_account = (Button) findViewById(R.id.btn_create);

        mSignUpFormView = findViewById(R.id.signup_form);
        mProgressView = findViewById(R.id.signup_progress);

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAttempt();
            }
        });

        return_login = findViewById(R.id.return_signin);
        return_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return to sign in
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }


    /**
     * Checks if the input fields for registration form is empty or not.
     * If it is empty, prompts user to enter it. If all fields are inputted
     * function attempts to call Asynctask, UserCreateTask to register an
     * account on the server.
     * This function does not return anything. It only prompts a toast message
     * unitl the user inputs the required fields.
     * @param  none
     * @return none
     */
    public void createAttempt() {
        if(isEmpty(edit_name)) {
            if(isEmpty(edit_username)) {
                if(isEmpty(edit_email)) {
                    if(isEmpty(edit_password)) {
                        if(isEmpty(edit_age)) {
                            String email = edit_email.getText().toString();
                            String password = edit_password.getText().toString();
                            String username = edit_username.getText().toString();
                            String age = edit_age.getText().toString();
                            String name = edit_name.getText().toString();
                            showProgress(true);
                            mAuthTask = new UserCreateTask(email, password, username, age, name);
                            mAuthTask.execute((String) "hello");
                        } else {
                            toast_message("Age");
                        }
                    } else {
                        toast_message("Password");
                    }
                } else {
                    toast_message("Email");
                }
            } else {
                toast_message("Username");
            }
        } else {
            toast_message("Name");
        }
    }

    /**
     * Used to check if the edit text field is empty or has content
     * Returns true if the field is empty, returns false if there is something
     * in the field
     * @param  Edittext Used to check the specific field that are required
     * @return boolean  Returns true or false.
     */
    private boolean isEmpty(EditText text) {
        if (text.getText().toString().trim().length() > 0)
            return true;
        return false;
    }

    /**
     * Takes a string and outputs it as a Toast message. Does not return anything.
     * @param  String   Used to print out a Toast message.
     * @return None
     */
    private void toast_message(String msg) {
        Toast.makeText(getApplicationContext(), msg + " Field is missing", Toast.LENGTH_SHORT).show();
    }

    /**
     * Used to turn on and off the loading animation when a HTTP call is being
     * made to a remote server.
     * @param show true or false value. True to turn on, False to turn off
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSignUpFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Class that was used to make HTTP calls. We create a JSON Object with user input
     * and send it to the server thought HTTP call. If we get a 200ok, means we've connected
     * and accomplished what we wanted to.
     *
     * We do a "POST" command to the server. Using the HTTP call, we supply the server with
     * username, password, age, email, name to create an account for the user. If the account is created
     * it takes the user back to the login screen and prompts a message to tell the user that.
     **/
    public class UserCreateTask extends AsyncTask<String, Void, String> {
        private final String mUsername;
        private final String mEmail;
        private final String mName;
        private final String mAge;
        private final String mPassword;

        UserCreateTask(String email, String password, String Username, String Age, String name) {
            mEmail = email;
            mPassword = password;
            mName = name;
            mAge = Age;
            mUsername = Username;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {
                URL url = new URL("http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/user/register"); // here is your URL path
                JSONObject postDataParams = new JSONObject();

                postDataParams.put("username", mUsername);
                postDataParams.put("password", mPassword);
                postDataParams.put("email",mEmail);
                postDataParams.put("age",mAge);
                postDataParams.put("name", mName);

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
                response_code_g = responseCode;
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
            mAuthTask = null;
            showProgress(false);
            if(response_code_g == HttpsURLConnection.HTTP_OK) {
                Toast.makeText(getApplicationContext(), "Account Created!, Returning to Login!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

    }
}

