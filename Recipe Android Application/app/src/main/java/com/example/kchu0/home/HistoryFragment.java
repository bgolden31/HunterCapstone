package com.example.kchu0.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonObject;

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
import java.util.Collections;

import javax.net.ssl.HttpsURLConnection;

public class HistoryFragment extends Fragment {

    private ArrayList<DatabaseObject> arrayList;
    private ListView lv;

    private View mProgressView;
    private View mHistoryView;

    private LoadHistoryTask mAuthTask = null;
    private DeleteHistoryTask mAuthDelete = null;
    private GetRecipeTask mAuthGet = null;

    private String Username;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        mProgressView = view.findViewById(R.id.history_progress);
        mHistoryView = view.findViewById(R.id.history_form);

        Bundle bundle = getArguments();
        Username = bundle.getString("username");

        arrayList = new ArrayList<>();
        lv = (ListView) view.findViewById(R.id.history_list);

        mAuthTask = new LoadHistoryTask(Username);
        mAuthTask.execute((String) "hello");

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

            mHistoryView.setVisibility(show ? View.GONE : View.VISIBLE);
            mHistoryView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mHistoryView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mHistoryView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Class that was used to make HTTP calls. We create a JSON Object with user input
     * and send it to the server thought HTTP call. If we get a 200ok, means we've connected
     * and accomplished what we wanted to.
     *
     * This task does a "GET" Command. Takes username and retrevies history list that is from
     * from the user. After taking the list, we pass it into Save history.
     **/
    public class LoadHistoryTask extends AsyncTask<String, Void, String> {
        private String mUsername;

        LoadHistoryTask(String username) {
            mUsername = username;
        }

        @Override
        protected void onPreExecute(){
            showProgress(true);
        }//onPreExecute

        @Override
        protected String doInBackground(String... arg0) {
            try {
                URL url = new URL("http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/UserHistory/get/" + mUsername ); // here is your URL path

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
            mAuthTask = null;
            save_history(result);
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
     * This task does a "DELETE" Command. Takes username and Recipe, and makes a HTTP call to remove
     * things from the server.
     **/
    public class DeleteHistoryTask extends AsyncTask<String, Void, String> {
        private String mUsername;
        private String mRecipe;


        DeleteHistoryTask(String Username, String recipe) {
            mUsername = Username;
            mRecipe = recipe;
        }

        @Override
        protected void onPreExecute(){
            showProgress(true);
        }//onPreExecute

        @Override
        protected String doInBackground(String... arg0) {
            try {
                URL url = new URL("http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/UserHistory/delete/" + mUsername + "?recipe=" + mRecipe); // here is your URL path

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
            mAuthDelete = null;
            showProgress(false);
        }

        @Override
        protected void onCancelled() {
            mAuthDelete = null;
            showProgress(false);
        }
    }

    /**
     * Class that was used to make HTTP calls. We create a JSON Object with user input
     * and send it to the server thought HTTP call. If we get a 200ok, means we've connected
     * and accomplished what we wanted to.
     *
     * This call is a "GET" command. Remote Server returns a JSON file that contains the Recipe
     * we want. This only works for Recipes created by Users and not recipes created by Edamam
     * Server. We take the return JSON object and store it in an Array though read_recipe();
     *
     **/
    public class GetRecipeTask extends AsyncTask<String, Void, String> {
        private int mID;

        GetRecipeTask(int id) {
            mID = id;
        }

        @Override
        protected void onPreExecute(){
            showProgress(true);
        }//onPreExecute

        @Override
        protected String doInBackground(String... arg0) {
            try {
                URL url = new URL("http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/recipe/get/" + mID ); // here is your URL path

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
            mAuthTask = null;
            read_recipe(result);
            showProgress(false);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    /**
     * Takes a result from GetRecipeTask, stores it into an array and passes it to DetailActivity.
     * @param result
     */
    public void read_recipe(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            ArrayList<Recipe> temp_list;
            JSONArray array = jsonObject.getJSONArray("ingredients");
            JSONObject object = jsonObject.getJSONObject("nutrients");
            temp_list = new ArrayList<>();
            temp_list.add(new Recipe(
                    jsonObject.getString("image"),
                    jsonObject.getInt("servings"),
                    jsonObject.getInt("totalTime"),
                    jsonObject.getString("author"),
                    jsonObject.getInt("rating"),
                    jsonObject.getString("description"),
                    array.toString(),
                    jsonObject.getString("label"),
                    jsonObject.getInt("calories"),
                    jsonObject.getString("url"),
                    jsonObject.getInt("recipeId"),
                    object.getInt("sodium"),
                    object.getInt("fiber"),
                    object.getInt("carbs"),
                    object.getInt("protein"),
                    object.getInt("fat"),
                    object.getInt("cholesterol"),
                    object.getInt("sugar")
            ));

            Intent intent = new Intent(getContext(), DetailsActivity.class);
            intent.putParcelableArrayListExtra("array", temp_list);
            intent.putExtra("position", 0);
            startActivity(intent);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes Result, passed from Load History Task and saves it into an Arraylist.
     * We then pass it to fill_list();
     * @param result
     */
    public void save_history(String result) {
        try {
            JSONArray array = new JSONArray(result);
            for( int i = 0; i < array.length(); i++ ){
                JSONObject object = array.getJSONObject(i);
                arrayList.add(new DatabaseObject(
                        object.getString("recipeName"),
                        object.getString("author"),
                        object.getInt("recipeId")
                ));
            }
            fill_list();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Takes Arraylist, which is populated with data and fill up the listview with information.
     */
    public void fill_list() {
        Collections.reverse(arrayList);
        final CustomListServer adapter = new CustomListServer(
                getContext(), R.layout.custom_list_object, arrayList
        );
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                int temp = arrayList.get(position).getRecipeID();
                if(temp > -1 ) {
                    mAuthGet = new GetRecipeTask(temp);
                    mAuthGet.execute((String) "hello");
                } else {
                    Toast.makeText(getContext(), "Can't Access this Recipe at this Time", Toast.LENGTH_SHORT).show();
                }
            }
        });


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
                        mAuthDelete = new DeleteHistoryTask(Username, arrayList.get(positionToRemove).getRecipeName() );
                        mAuthDelete.execute((String) "hello");
                        arrayList.remove(positionToRemove);
                        adapter.notifyDataSetChanged();
                    }});
                adb.show();
                return true;
            }
        });

    }


}
