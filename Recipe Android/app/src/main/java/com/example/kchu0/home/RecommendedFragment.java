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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class RecommendedFragment extends Fragment {
    private ArrayList<Recipe> arrayList;
    private ListView lv;

    private LoadReccTask mAuthTask = null;
    private GetRecipeTask mAuthGet = null;

    private View mProgressView;
    private View mHistoryView;

    String Username;
    CustomListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommended, container, false);

        mProgressView = view.findViewById(R.id.rec_progress);
        mHistoryView = view.findViewById(R.id.rec_form);

        Bundle bundle = getArguments();
        Username = bundle.getString("username");

        arrayList = new ArrayList<>();
        lv = (ListView) view.findViewById(R.id.recommend_list);

        mAuthTask = new LoadReccTask(Username);
        mAuthTask.execute((String) "hello");

        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

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
     * This task does a "POST" command and tells the Server to send info. We take the JSON object returned
     * and display it to the user for viewing.
     **/
    public class LoadReccTask extends AsyncTask<String, Void, String> {
        private String mUsername;

        LoadReccTask(String username) {
            mUsername = username;
        }

        @Override
        protected void onPreExecute(){
            showProgress(true);
        }//onPreExecute

        @Override
        protected String doInBackground(String... arg0) {
            try {
                URL url = new URL("http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/recipe/recommended/" + mUsername + "?size="  + 20); // here is your URL path

                Log.e("URL",url.toString());

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("username", mUsername);
                postDataParams.put("size", 20);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
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
            display_result(result);
            showProgress(false);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

        private void display_result(String string) {
            try {
                JSONObject jsonObject = new JSONObject(string);
                JSONArray APIRecipes = jsonObject.getJSONArray("DatabaseRecipes");
                for (int i = 0; i < APIRecipes.length(); i++) {
                    JSONObject object = APIRecipes.getJSONObject(i);
                    String temp = object.getJSONArray("ingredients").toString();
                    JSONObject nutrients = object.getJSONObject("nutrients");
                    arrayList.add(new Recipe(
                            object.getString("image"),
                            object.getInt("servings"),
                            object.getInt("totalTime"),
                            object.getString("author"),
                            object.getInt("rating"),
                            object.getString("description"),
                            temp,
                            object.getString("label"),
                            object.getInt("calories"),
                            object.getString("url"),
                            object.getInt("recipeId"),
                            nutrients.getInt("sodium"),
                            nutrients.getInt("fiber"),
                            nutrients.getInt("carbs"),
                            nutrients.getInt("protein"),
                            nutrients.getInt("fat"),
                            nutrients.getInt("cholesterol"),
                            nutrients.getInt("sugar")
                    ));
                }

                adapter = new CustomListAdapter(
                        getContext(), R.layout.custom_list_layout, arrayList
                );
                lv.setAdapter(adapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        final int positionTo = position;
                        Intent intent = new Intent(getContext(), DetailsActivity.class);
                        intent.putParcelableArrayListExtra("array", arrayList);
                        intent.putExtra("position", positionTo);
                        startActivity(intent);

                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
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

    /***
     * Takes result passed from GetRecipeTask puts it into an ArrayList. The function takes
     * the information put into the temporary array and passes it into Detail activity for displaying.
     * @param result JSON object passed from Get Recipe Task
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

}
