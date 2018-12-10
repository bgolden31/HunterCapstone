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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class DetailsActivity extends AppCompatActivity {

    private ArrayList<Ingredient> arrayIngredient;
    private View mProgressView;
    private View mDetailsView;
    private AddHistoryTask mAuthTask = null;
    private AddFavouriteTask mAuthFav = null;
    private AddShoppingTask mAuthShop = null;
    private AddRating mAuthAdd = null;

    SessionManager session;             //Used to access certain info regarding user, to make calls if necessary.
    HashMap<String, String> user_s;

    /** Simple Function that takes an int and converts it into a String.
     * Data is saved as an int in object and JSON return object.
     *
     * @param number takes number and converts it into a String
     * @return String returns a String that can be displayed through Textview
     */
    public String int_to_string(int number) {
        return Integer.toString(number);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mDetailsView = (View) findViewById(R.id.scroll_form);
        mProgressView = (View) findViewById(R.id.profile_progress);

        Intent intent = getIntent();
        ArrayList<Recipe> arrayList;
        arrayList = intent.getParcelableArrayListExtra("array");
        int position;
        position = intent.getIntExtra("position", 0);
        String temp;

        String label = arrayList.get(position).getLabel();
        TextView label_ = (TextView) findViewById(R.id.label);
        label_.setText(label);

        ImageView imageView = (ImageView) findViewById(R.id.image);
        String img = arrayList.get(position).getImage();
        Picasso.get().load(img).into(imageView);

        final RatingBar ratebar = (RatingBar) findViewById(R.id.rating);
        int rate = arrayList.get(position).getRating();
        int value = 1;
        final float current_rate = (float) rate / value;
        if(rate == -1) {
            ratebar.setRating(Float.parseFloat("0.0"));
        } else {
            ratebar.setRating(Float.parseFloat(Float.toString(current_rate)));
        }

        int calories = arrayList.get(position).getCalories();
        TextView calories_ = (TextView) findViewById(R.id.calories);
        temp = "Calories: " + int_to_string(calories);
        calories_.setText(temp);

        TextView ingredient_ = (TextView) findViewById(R.id.ingredient);
        ingredient_.setText("Ingredient");
        arrayIngredient = new ArrayList<>();
        ListView lv;
        lv = (ListView) findViewById(R.id.ingredient_list);

        String info_ingredient = arrayList.get(position).getIngredients();
        try {
            JSONArray array = new JSONArray(info_ingredient);
            for ( int i = 0; i < array.length(); i++ ){
                JSONObject object = array.getJSONObject(i);
                arrayIngredient.add(new Ingredient(
                        object.getInt("weight"),
                        object.getString("text")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomListIngredeint adapter = new CustomListIngredeint(
                getApplicationContext(), R.layout.custom_list_ingredient, arrayIngredient
        );
        lv.setAdapter(adapter);
        lv.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {
                AlertDialog.Builder adb=new AlertDialog.Builder(DetailsActivity.this);
                adb.setTitle("Are You Sure You Want to Add the Following Item to your Shopping List?");
                final int positionToRemove = pos;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mAuthShop = new AddShoppingTask(user_s.get(SessionManager.KEY_USERNAME), arrayIngredient.get(positionToRemove).getText());
                        mAuthShop.execute((String) "hello");
                        Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();
                    }});
                adb.show();
                return true;
            }
        });

        String url = arrayList.get(position).getUrl();
        TextView url_ = (TextView) findViewById(R.id.directions);
        temp = "Directions: " + url;
        url_.setText(temp);

        int fat = arrayList.get(position).getFat();
        TextView fat_ = (TextView) findViewById(R.id.fat);
        temp = "Fat: " + int_to_string(fat) + "g";
        fat_.setText(temp);

        int sugar = arrayList.get(position).getSugar();
        TextView sugar_ = (TextView) findViewById(R.id.sugar);
        temp = "Sugar: " + int_to_string(sugar) + "g";
        sugar_.setText(temp);

        int protein = arrayList.get(position).getProtein();
        TextView protein_ = (TextView) findViewById(R.id.protein);
        temp = "Protein: " + int_to_string(protein) + "g";
        protein_.setText(temp);

        int sodium = arrayList.get(position).getSodium();
        TextView sodium_ = (TextView) findViewById(R.id.sodium);
        temp = "Sodium: " + int_to_string(sodium) + "g";
        sodium_.setText(temp);

        int carbs = arrayList.get(position).getCarbs();
        TextView carbs_ = (TextView) findViewById(R.id.carbs);
        temp = "Carbohydrate: " + int_to_string(carbs) + "g";
        carbs_.setText(temp);

        int fiber = arrayList.get(position).getFiber();
        TextView fiber_ = (TextView) findViewById(R.id.fiber);
        temp = "Fiber: " + int_to_string(fiber) + "g";
        fiber_.setText(temp);

        int cholesterol = arrayList.get(position).getCholesterol();
        TextView cholesterol_ = (TextView) findViewById(R.id.cholesterol);
        temp = "Cholesterol: " + int_to_string(cholesterol) + "g";
        cholesterol_.setText(temp);

        session = new SessionManager(getApplicationContext());
        user_s = session.getUserDetails();

        final String username = user_s.get(SessionManager.KEY_USERNAME);
        final String recipeName = arrayList.get(position).getLabel();
        final String author = arrayList.get(position).getAuthor();
        final int recipeId = arrayList.get(position).getRecipeID();
        mAuthTask = new AddHistoryTask(username, recipeName, author, recipeId);
        mAuthTask.execute((String) "hello");

        ratebar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                int value = Math.round(rating);
                mAuthAdd = new AddRating(username, recipeName, author, recipeId, value);
                mAuthAdd.execute((String) "hello");
                String input = Float.toString(rating);
                ratebar.setRating(Float.parseFloat(input));
                //Toast.makeText(getApplicationContext(),Float.toString(current_rate), Toast.LENGTH_SHORT ).show();

            }
        });

        Button button = (Button) findViewById(R.id.button_fav);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(DetailsActivity.this);
                adb.setTitle("Add To Favourite?");
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mAuthFav = new AddFavouriteTask(username, recipeName, author, recipeId);
                        mAuthFav.execute( (String) "hello");
                    }});
                adb.show();
            }
        });

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

            mDetailsView.setVisibility(show ? View.GONE : View.VISIBLE);
            mDetailsView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mDetailsView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mDetailsView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



    /**
     * Class that was used to make HTTP calls. We create a JSON Object with user input
     * and send it to the server thought HTTP call. If we get a 200ok, means we've connected
     * and accomplished what we wanted to.
     *
     * This call is a "Post" command. Takes Username, RecipeName, Author and ID and adds it
     * user History when it is clicked on from other Task. Whenever this page is loaded, this is
     * used.
     **/
    public class AddHistoryTask extends AsyncTask<String, Void, String> {
        private String mUsername;
        private String mRecipeName;
        private String mAuthor;
        private int mRecipeId;


        AddHistoryTask(String Username, String RecipeName, String Author, int RecipeId) {
            mUsername = Username;
            mRecipeName = RecipeName;
            mAuthor = Author;
            mRecipeId = RecipeId;
        }

        @Override
        protected void onPreExecute(){
            showProgress(true);
        }//onPreExecute

        @Override
        protected String doInBackground(String... arg0) {
            try {
                URL url = new URL("http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/UserHistory/insert"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                //add name pair values to the connection

                postDataParams.put("username", mUsername);
                postDataParams.put("recipeName", mRecipeName);
                postDataParams.put("author", mAuthor);
                postDataParams.put("recipeId", mRecipeId);

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
     * This call is a "Post" command. Takes Username, RecipeName, Author and ID and adds it
     * user Favourite. This task is loaded when ever the Favourite button is clicked
     **/
    public class AddFavouriteTask extends AsyncTask<String, Void, String> {
        private String mUsername;
        private String mRecipeName;
        private String mAuthor;
        private int mRecipeId;


        AddFavouriteTask(String Username, String RecipeName, String Author, int RecipeId) {
            mUsername = Username;
            mRecipeName = RecipeName;
            mAuthor = Author;
            mRecipeId = RecipeId;
        }

        @Override
        protected void onPreExecute(){
            showProgress(true);
        }//onPreExecute

        @Override
        protected String doInBackground(String... arg0) {
            try {
                URL url = new URL("http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/list/favorites/insert"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                //add name pair values to the connection

                postDataParams.put("username", mUsername);
                postDataParams.put("recipeName", mRecipeName);
                postDataParams.put("author", mAuthor);
                postDataParams.put("recipeId", mRecipeId);

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
            mAuthFav = null;
            showProgress(false);
        }

        @Override
        protected void onCancelled() {
            mAuthFav = null;
            showProgress(false);
        }
    }

    /**
     * Class that was used to make HTTP calls. We create a JSON Object with user input
     * and send it to the server thought HTTP call. If we get a 200ok, means we've connected
     * and accomplished what we wanted to.
     *
     * This call is a "Post" command. Takes Username, and Ingredient and adds it Favourite list.
     * Task is called whenever a user does a long click on the listview of Ingredients.
     **/
    public class AddShoppingTask extends AsyncTask<String, Void, String> {
        private String mUsername;
        private String mIngredient;


        AddShoppingTask(String Username, String Ingredient) {
            mUsername = Username;
            mIngredient = Ingredient;
        }

        @Override
        protected void onPreExecute(){
            showProgress(true);
        }//onPreExecute

        @Override
        protected String doInBackground(String... arg0) {
            try {
                URL url = new URL("http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/UserShopping/insert"); // here is your URL path

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
            mAuthShop = null;
            showProgress(false);
        }

        @Override
        protected void onCancelled() {
            mAuthShop = null;
            showProgress(false);
        }
    }


    /**
     * Class that was used to make HTTP calls. We create a JSON Object with user input
     * and send it to the server thought HTTP call. If we get a 200ok, means we've connected
     * and accomplished what we wanted to.
     *
     * This call is a "Post" command. Takes Username, RecipeName, Author, ID and Rating. Takes user
     * input from RatingBar and add its to the remote server.
     *
     **/
    public class AddRating extends AsyncTask<String, Void, String> {
        private String mUsername;
        private String mRecipeName;
        private String mAuthor;
        private int mRecipeId;
        private int mRating;


        AddRating(String Username, String RecipeName, String Author, int RecipeId, int Rating) {
            mUsername = Username;
            mRecipeName = RecipeName;
            mAuthor = Author;
            mRecipeId = RecipeId;
            mRating = Rating;
        }

        @Override
        protected void onPreExecute(){
            showProgress(true);
        }//onPreExecute

        @Override
        protected String doInBackground(String... arg0) {
            try {
                URL url = new URL("http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/recipe/update/rating"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                //add name pair values to the connection

                postDataParams.put("username", mUsername);
                postDataParams.put("recipeName", mRecipeName);
                postDataParams.put("author", mAuthor);
                postDataParams.put("recipeId", mRecipeId);
                postDataParams.put("rating", mRating);

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
            mAuthFav = null;
            showProgress(false);
        }

        @Override
        protected void onCancelled() {
            mAuthFav = null;
            showProgress(false);
        }
    }



}
