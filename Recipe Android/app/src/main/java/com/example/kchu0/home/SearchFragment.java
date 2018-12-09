package com.example.kchu0.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mancj.materialsearchbar.MaterialSearchBar;

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
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static android.app.Activity.RESULT_OK;

public class SearchFragment extends Fragment implements MaterialSearchBar.OnSearchActionListener {

    private MaterialSearchBar searchBar;
    private static final int SPEECH_REQUEST_CODE = 0;

    private TextView welcome;
    private View mProgressView;
    private View mSearchView;

    private String key;
    private ArrayList<Recipe> arrayList;
    private ListView lv;

    private SearchTask mAuthTask = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        welcome = (TextView) view.findViewById(R.id.welcome);
        Bundle bundle = getArguments();
        if(bundle != null) {
            String temp = bundle.getString("name");
            welcome.setText("Welcome Back, " + temp + "!" );
        }

        searchBar = (MaterialSearchBar) view.findViewById(R.id.search_bar);
        searchBar.setHint("Enter Ingredient...");
        searchBar.setSpeechMode(true);
        searchBar.setOnSearchActionListener(this);

        mSearchView = view.findViewById(R.id.search_form);
        mProgressView = view.findViewById(R.id.search_progress);

        arrayList = new ArrayList<>();
        lv = (ListView) view.findViewById(R.id.listView);
        return view;
    }

    /**
     * Funtion created by Material Search Bar. Refer to https://github.com/mancj/MaterialSearchBar for details
     * This Does nothing.
     * @param b
     */
    @Override
    public void onSearchStateChanged(boolean b) {
        //String state = b ? "enabled" : "disabled";
        //Toast.makeText(getContext(), "Search " + state, Toast.LENGTH_SHORT).show();
    }

    /**
     * Funtion created by Material Search Bar. Refer to https://github.com/mancj/MaterialSearchBar for details
     * Enables Search functionality
     * @param b
     */
    @Override
    public void onSearchConfirmed(CharSequence charSequence) {
        key = charSequence.toString();
        //Toast.makeText(getContext(),"Searching "+ charSequence.toString()+" ......",Toast.LENGTH_SHORT).show();
        showProgress(true);
        mAuthTask = new SearchTask(20, key);
        mAuthTask.execute((String) "hello");
        key = "";
        InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); //Closes keyboard
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * Funtion created by Material Search Bar. Refer to https://github.com/mancj/MaterialSearchBar for details.
     * Initiates Voice input
     * @param b
     */
    @Override
    public void onButtonClicked(int i) {
        switch (i){
            case MaterialSearchBar.BUTTON_NAVIGATION:
                //Toast.makeText(getContext(), "Button Nav " , Toast.LENGTH_SHORT).show();
                break;
            case MaterialSearchBar.BUTTON_SPEECH:
                displaySpeechRecognizer();
                break;
        }
    }

    /**
     *
     */
    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }


    /**
     * This callback is invoked when the Speech Recognizer returns.
     * This is where you process the intent and extract the speech text from the intent.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            // Do something with spokenText
            //Add Execute here.
            mAuthTask = new SearchTask(20, spokenText);
            mAuthTask.execute((String) "hello");
        }
        super.onActivityResult(requestCode, resultCode, data);
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

            mSearchView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSearchView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSearchView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mSearchView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
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
     * Class that was used to make HTTP calls. We create a JSON Object with user input
     * and send it to the server thought HTTP call. If we get a 200ok, means we've connected
     * and accomplished what we wanted to.
     *
     * This class does a "POST" command and enters the search that the user wants. HTTP call returns
     * a JSON file that is used by display_result() to display on screen.
     **/
    public class SearchTask extends AsyncTask<String, Void, String> {
        private int mSize;
        private String mIngredient;

        SearchTask(int size, String Ingredient) {
            mSize = size;
            mIngredient = Ingredient;
        }

        @Override
        protected void onPreExecute(){
            arrayList.clear();
            showProgress(true);
        }//onPreExecute

        @Override
        protected String doInBackground(String... arg0) {
            try {
                URL url = new URL("http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/recipe/search"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                //add name pair values to the connection

                postDataParams.put("size", mSize);
                postDataParams.put("search", mIngredient);

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
            display_result(result);
            showProgress(false);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    /**
     * Takes Results from Search Task and this function takes the results and stores them in an
     * arraylist. After storing them in an array list it passes it. Function also sets up on click
     * listen so we can view the details of the recipe.
     * @param string JSON object return from remote server
     */
    private void display_result(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray APIRecipes = jsonObject.getJSONArray("APIRecipes");
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

            JSONArray DatabaseRecipes = jsonObject.getJSONArray("DatabaseRecipes");
            for (int i = 0; i < DatabaseRecipes.length(); i++) {
                JSONObject object = DatabaseRecipes.getJSONObject(i);
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



            CustomListAdapter adapter = new CustomListAdapter(
                    getContext(), R.layout.custom_list_layout, arrayList
            );
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent intent = new Intent(getContext(), DetailsActivity.class);
                    intent.putParcelableArrayListExtra("array", arrayList);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            });



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
