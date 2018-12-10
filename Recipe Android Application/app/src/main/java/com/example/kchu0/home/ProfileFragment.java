/**
* XML file take/refrence by 
JayDoesCode (https://www.youtube.com/watch?v=CCpY66soGuE)

*/

package com.example.kchu0.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class ProfileFragment extends Fragment {
    private TextView name;
    private TextView username;
    private TextView age;
    private TextView email;

    private View mProgressView;
    private View myProfileFormView;

    private UserUpdateTask mAuthTask = null;
    private UserDeleteTask mDeleteTask = null;
    private int response_code_g;

    private String final_username;
    private String final_password;
    private String final_age;
    private String final_email;
    private String final_name;

    SessionManager session;
    private Bundle bundle;
    HashMap<String, String> user_s;

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        name = (TextView) view.findViewById(R.id.name);
        username = (TextView) view.findViewById(R.id.username);
        age = (TextView) view.findViewById(R.id.age);
        email = (TextView) view.findViewById(R.id.email);

        myProfileFormView = view.findViewById(R.id.profile_form);
        mProgressView = view.findViewById(R.id.profile_progress);
        session = new SessionManager(getContext());

        bundle = getArguments();
        if(bundle != null) {
            name.setText(bundle.getString("name"));
            username.setText(bundle.getString("username"));
            age.setText(bundle.getString("age"));
            email.setText(bundle.getString("email"));
        }

        final_username = bundle.getString("username");
        final_password = bundle.getString("password");
        final_age = bundle.getString("age");
        final_name = bundle.getString("name");
        final_email = bundle.getString("email");

        Button update_age = (Button) view.findViewById(R.id.update_age);
        update_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText temp_age = (EditText) view.findViewById(R.id.input_age);
                String string_age = temp_age.getText().toString();
                final_age = string_age;
                mAuthTask = new UserUpdateTask(final_email, final_password, final_username, final_age, final_name);
                mAuthTask.execute((String) "hello");
                temp_age.setText("");
                InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); //Closes keyboard
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        Button update_email = (Button) view.findViewById(R.id.update_email);
        update_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText temp_email = (EditText) view.findViewById(R.id.input_email);
                String string_email = temp_email.getText().toString();
                final_email = string_email;
                mAuthTask = new UserUpdateTask(final_email, final_password, final_username, final_age, final_name);
                mAuthTask.execute((String) "hello");
                temp_email.setText("");
                InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); //Closes keyboard
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        Button update_name = (Button) view.findViewById(R.id.update_name);
        update_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText temp_name = (EditText) view.findViewById(R.id.input_name);
                String string_name = temp_name.getText().toString();
                final_name = string_name;
                mAuthTask = new UserUpdateTask(final_email, final_password, final_username, final_age, final_name);
                mAuthTask.execute((String) "hello");
                temp_name.setText("");
                InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); //Closes keyboard
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        TextView delete = (TextView) view.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                checkpassword();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        return view;
    }

    /**
     * Function hides the "Actionbar" from the top of the screen
     * @param  none
     **/
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    /**
     * Function shows the "Actionbar" from the top of the screen
     * @param  none
     **/
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
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

            myProfileFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            myProfileFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    myProfileFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            myProfileFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Class that was used to make HTTP calls. We create a JSON Object with user input
     * and send it to the server thought HTTP call. If we get a 200ok, means we've connected
     * and accomplished what we wanted to.
     *
     * Task calls a "POST" command whenever the user wants to update something different about there profile.
     * We send all 5 data that is required to change password to remote server in order to update it.
     **/
    public class UserUpdateTask extends AsyncTask<String, Void, String> {
        private final String mUsername;
        private final String mEmail;
        private final String mName;
        private final String mAge;
        private final String mPassword;

        UserUpdateTask(String email, String password, String Username, String Age, String name) {
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
                URL url = new URL("http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/user/update/" + mUsername); // here is your URL path
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
                conn.setRequestMethod("PUT");
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
                session.changeinfo(mAge, mName, mEmail, final_username, final_password);
                user_s = session.getUserDetails();
                name.setText(user_s.get(SessionManager.KEY_NAME));
                username.setText(user_s.get(SessionManager.KEY_USERNAME));
                age.setText(user_s.get(SessionManager.KEY_AGE));
                email.setText(user_s.get(SessionManager.KEY_EMAIL));
                Toast.makeText(getContext(), "Updated Complete", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

    }

    /**
     * Class that was used to make HTTP calls. We create a JSON Object with user input
     * and send it to the server thought HTTP call. If we get a 200ok, means we've connected
     * and accomplished what we wanted to.
     *
     * This task calls a "Delete" request. Whenever "Delete account" is clicked, this Task is called when
     * USer correctly matches the passwords.
     **/
    public class UserDeleteTask extends AsyncTask<String, Void, String> {
        private final String mUsername;

        UserDeleteTask( String Username) {
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
                URL url = new URL("http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/user/delete/" + mUsername); // here is your URL path
                JSONObject postDataParams = new JSONObject();


                Log.e("params",postDataParams.toString());
                Log.e("URL",url.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("DELETE");
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
            mDeleteTask = null;
            showProgress(false);
        }

        @Override
        protected void onCancelled() {
            mDeleteTask = null;
            showProgress(false);
        }

    }

    /**
     * Prompts dialog for user to enter password. Confirm the user before Deleting and calling
     * UserDeleteTask. After UserDeleteTask is complete. Signs user out with Session manager.
     */
    public void checkpassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter Your Password");
        // Set up the input
        final EditText input = new EditText(getContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_Text;
                m_Text = input.getText().toString();
                if(final_password.equals(m_Text)) {
                    mDeleteTask = new UserDeleteTask(final_username);
                    mDeleteTask.execute((String) "hello");
                    Toast.makeText(getContext(), "Account Deleted. Sorry to See You Go.", Toast.LENGTH_LONG).show();
                    session.logoutUser();
                } else {
                    Toast.makeText(getContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();

                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }
}
