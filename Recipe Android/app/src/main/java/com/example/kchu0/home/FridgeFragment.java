package com.example.kchu0.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class FridgeFragment extends Fragment {

    private ArrayList<String> arrayList;
    private ListView lv;

    private View mProgressView;
    private View mFridgeView;

    private AddFridgeTask mAuthTask = null;
    private LoadFridgeTask mLoadTask= null;
    private DeleteFridgeTask mDeleteTask = null;

    private String Username;
    private EditText editText;
    ArrayAdapter<String> allItemsAdapter;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fridge, container, false);

        mProgressView = view.findViewById(R.id.progress_form);
        mFridgeView = view.findViewById(R.id.shop_form);

        Bundle bundle = getArguments();
        Username = bundle.getString("username");

        arrayList = new ArrayList<>();
        lv = (ListView) view.findViewById(R.id.fridge_list);

        mLoadTask = new LoadFridgeTask(Username);
        mLoadTask.execute((String) "hello");

        Button button = view.findViewById(R.id.add_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText = view.findViewById(R.id.edit_ingredient);
                String temp = editText.getText().toString();
                mAuthTask = new AddFridgeTask(Username, temp);
                mAuthTask.execute((String) "hello");
                arrayList.add(temp);
                allItemsAdapter.notifyDataSetChanged();
                InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); //Closes keyboard
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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

            mFridgeView.setVisibility(show ? View.GONE : View.VISIBLE);
            mFridgeView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFridgeView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mFridgeView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Class that was used to make HTTP calls. We create a JSON Object with user input
     * and send it to the server thought HTTP call. If we get a 200ok, means we've connected
     * and accomplished what we wanted to.
     *
     * This task does a "Post" Command. Takes Username and Ingredient and adds it to the list of items
     * the user currently has in there Fridge.
     **/
    public class AddFridgeTask extends AsyncTask<String, Void, String> {
        private String mUsername;
        private String mIngredient;

        AddFridgeTask(String username, String Ingredient) {
            mUsername = username;
            mIngredient = Ingredient;
        }

        @Override
        protected void onPreExecute(){
            showProgress(true);
        }//onPreExecute

        @Override
        protected String doInBackground(String... arg0) {
            try {
                URL url = new URL("http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/UserFridge/insert"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                //add name pair values to the connection

                postDataParams.put("username", mUsername);
                postDataParams.put("ingredient", mIngredient);

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
            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            //return result;
            //call method to handle after verification
            mAuthTask = null;
            Toast.makeText(getContext(), "Added!", Toast.LENGTH_SHORT).show();
            showProgress(false);
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
     * This Task does a "GET" command. Takes the JSON Object and stores it into the array using
     * save_fridge();
     **/
    public class LoadFridgeTask extends AsyncTask<String, Void, String> {
        private String mUsername;

        LoadFridgeTask(String username) {
            mUsername = username;
        }

        @Override
        protected void onPreExecute(){
            showProgress(true);
        }//onPreExecute

        @Override
        protected String doInBackground(String... arg0) {
            try {
                URL url = new URL("http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/UserFridge/get/" + mUsername ); // here is your URL path

                Log.e("URL",url.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.connect();

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
            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            //return result;
            //call method to handle after verification
            mLoadTask = null;
            save_fridge(result);
            showProgress(false);
        }

        @Override
        protected void onCancelled() {
            mLoadTask = null;
            showProgress(false);
        }
    }

    /**
     * Class that was used to make HTTP calls. We create a JSON Object with user input
     * and send it to the server thought HTTP call. If we get a 200ok, means we've connected
     * and accomplished what we wanted to.
     *
     * This Task does a "DELETE" command. Cals a Delete command using Username and Ingredient.
     * Server takes the two object and does things accordingly. Listview is reloaded manually
     * by the App itself. Does not call LoadFridgeTask a second time.
     **/
    public class DeleteFridgeTask extends AsyncTask<String, Void, String> {
        private String mUsername;
        private String mIngredient;


        DeleteFridgeTask(String Username, String ingredient) {
            mUsername = Username;
            mIngredient = ingredient;
        }

        @Override
        protected void onPreExecute(){
            showProgress(true);
        }//onPreExecute

        @Override
        protected String doInBackground(String... arg0) {
            try {
                URL url = new URL("http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/userFridge/delete/" + mUsername + "?ingredient=" + mIngredient); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                //add name pair values to the connection

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
            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            //return result;
            //call method to handle after verification
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
     * Takes result from LoadFridgeTask and saved it into an arraylist
     * @param result String from a HTTP Call.
     */
    public void save_fridge(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray fridge = jsonObject.getJSONArray("fridge");
            for(int i = 0; i < fridge.length(); i++ ){
                JSONObject object = fridge.getJSONObject(i);
                arrayList.add( object.getString("ingredient"));
            }
            fill_list();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called from save_fridge, and takes the ArrayList and displays it on to the screen.
     */
    public void fill_list() {
        allItemsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        lv.setAdapter(allItemsAdapter);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {
                AlertDialog.Builder adb=new AlertDialog.Builder(getActivity());
                adb.setTitle("Are You Sure You Want to Delete the Following Item?");
                final int positionToRemove = pos;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String temp = arrayList.get(positionToRemove);
                        mDeleteTask = new DeleteFridgeTask(Username, temp);
                        mDeleteTask.execute((String) "hello");
                        Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                        mLoadTask = new LoadFridgeTask(Username);
                        mLoadTask.execute((String) "hello");
                    }});
                adb.show();
                return true;
            }
        });

    }

}
