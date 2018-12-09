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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

public class EditRecipe extends AppCompatActivity {

    private ArrayList<Recipe> arrayList;
    private ArrayList<Ingredient> arrayIngredient;
    private int position;
    private UpdateRecipe mAuthUpdate = null;
    private View mProgressView;
    private View mFridgeView;
    String Username;
    CustomListIngredeint adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        mProgressView = (View) findViewById(R.id.progress_form);
        mFridgeView = (View) findViewById(R.id.shop_form);

        Intent intent = getIntent();
        arrayList = intent.getParcelableArrayListExtra("array");
        position = intent.getIntExtra("position", 0);
        Username = intent.getStringExtra("username");

        String temp;
        final EditText recipe_name = (EditText) findViewById(R.id.recipe_name);
        temp = arrayList.get(position).getLabel();
        recipe_name.setText(temp, TextView.BufferType.EDITABLE);

        final EditText recipe_description = (EditText) findViewById(R.id.recipe_description);
        temp = arrayList.get(position).getDescription();
        recipe_description.setText(temp, TextView.BufferType.EDITABLE);

        final EditText image_url = (EditText) findViewById(R.id.recipe_image);
        temp = arrayList.get(position).getImage();
        image_url.setText(temp, TextView.BufferType.EDITABLE);

        final EditText url = (EditText) findViewById(R.id.recipe_url);
        temp = arrayList.get(position).getUrl();
        url.setText(temp, TextView.BufferType.EDITABLE);

        final EditText serving = (EditText) findViewById(R.id.recipe_serving);
        temp = Integer.toString(arrayList.get(position).getServing());
        serving.setText(temp, TextView.BufferType.EDITABLE);

        final EditText calories = (EditText) findViewById(R.id.recipe_calories);
        temp = Integer.toString(arrayList.get(position).getCalories());
        calories.setText(temp, TextView.BufferType.EDITABLE);

        final EditText totaltime = (EditText) findViewById(R.id.recipe_time);
        temp = Integer.toString(arrayList.get(position).getTotaltime());
        totaltime.setText(temp, TextView.BufferType.EDITABLE);

        final EditText fat = (EditText) findViewById(R.id.recipe_fat);
        temp = Integer.toString(arrayList.get(position).getFat());
        fat.setText(temp, TextView.BufferType.EDITABLE);

        final EditText sugar = (EditText) findViewById(R.id.recipe_sugar);
        temp = Integer.toString(arrayList.get(position).getSugar());
        sugar.setText(temp, TextView.BufferType.EDITABLE);

        final EditText protein = (EditText) findViewById(R.id.recipe_protein);
        temp = Integer.toString(arrayList.get(position).getProtein());
        protein.setText(temp, TextView.BufferType.EDITABLE);

        final EditText fiber = (EditText) findViewById(R.id.recipe_fiber);
        temp = Integer.toString(arrayList.get(position).getProtein());
        fiber.setText(temp, TextView.BufferType.EDITABLE);

        final EditText sodium = (EditText) findViewById(R.id.recipe_sodium);
        temp = Integer.toString(arrayList.get(position).getSodium());
        sodium.setText(temp, TextView.BufferType.EDITABLE);

        final EditText cholestorol = (EditText) findViewById(R.id.recipe_cholesterol);
        temp = Integer.toString(arrayList.get(position).getCholesterol());
        cholestorol.setText(temp, TextView.BufferType.EDITABLE);

        final EditText carbs = (EditText) findViewById(R.id.recipe_carbohydrates);
        temp = Integer.toString(arrayList.get(position).getCarbs());
        carbs.setText(temp, TextView.BufferType.EDITABLE);

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
        adapter = new CustomListIngredeint(
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
                AlertDialog.Builder adb=new AlertDialog.Builder(EditRecipe.this);
                adb.setTitle("Are You Sure You Want to Remove the Following Item to your List?");
                final int positionToRemove = pos;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        arrayList.remove(positionToRemove);
                        Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                    }});
                adb.show();
                return true;
            }
        });

        Button add_btn = (Button) findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog =new AlertDialog.Builder(EditRecipe.this);
                View dialog_layout = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                // Create the text field in the alert dialog...
                final EditText text1 = (EditText) dialog_layout.findViewById(R.id.text1);
                final EditText text2 = (EditText) dialog_layout.findViewById(R.id.text2);
                text1.setHint("Enter Ingredient");
                text2.setHint("Enter number of Grams");
                alertDialog.setView(dialog_layout);
                alertDialog.setNegativeButton("Cancel", null);
                alertDialog.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String text = text1.getText().toString();
                        String weight = text2.getText().toString();
                        if(!text.isEmpty()) {
                            if(!weight.isEmpty()) {
                                int result = Integer.parseInt(weight);
                                arrayIngredient.add(new Ingredient(
                                        result,
                                        text
                                ));
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(getApplicationContext(), "Please Try Again. Empty Field Detected", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Try Again. Empty Field Detected", Toast.LENGTH_SHORT).show();
                        }
                    }});
                alertDialog.show();
            }
        });

        Button create_btn = findViewById(R.id.create_btn);
        create_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String input_a = recipe_name.getText().toString();
                String input_b = recipe_description.getText().toString();
                String input_c = url.getText().toString();
                String input_d = image_url.getText().toString();
                String input_e = serving.getText().toString();
                String input_f = calories.getText().toString();
                String input_g = totaltime.getText().toString();
                String input_h = fat.getText().toString();
                String input_i = sugar.getText().toString();
                String input_j = protein.getText().toString();
                String input_k = fiber.getText().toString();
                String input_l = sodium.getText().toString();
                String input_m = cholestorol.getText().toString();
                String input_n = carbs.getText().toString();
                if(arrayIngredient.size() > 0) {
                    if(!input_a.isEmpty()  && !input_b.isEmpty() && !input_c.isEmpty() && !input_d.isEmpty() && !input_e.isEmpty()
                            && !input_f.isEmpty() && !input_g.isEmpty() && !input_h.isEmpty() && !input_i.isEmpty() && !input_j.isEmpty()
                            && !input_k.isEmpty() && !input_l.isEmpty() && !input_m.isEmpty() && !input_n.isEmpty()) {
                        mAuthUpdate = new UpdateRecipe (Username, input_a, input_b, input_c, input_d, input_e, input_f, input_g,
                                input_h, input_i, input_j, input_k, input_l, input_m, input_n, arrayList.get(position).getRecipeID());
                        mAuthUpdate.execute((String) "hello");
                        Toast.makeText(getApplicationContext(),"Added", Toast.LENGTH_SHORT);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplicationContext(), "Missing Fields, Please Try Again!", Toast.LENGTH_SHORT).show();
                    }
                }

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
     * This call is a "Post" command. Takes a number of user inputs and stores them into a
     * JSONObject that we send to the remote server. Simpler function to AddRecipeTask, but
     * this holds an extra ID that is created by the server, unique to the Recipe inserted.
     *
     **/
    public class UpdateRecipe extends AsyncTask<String, Void, String> {
        private String mUsername;
        private String mRecipename;
        private String mRecipeDes;
        private String mUrl;
        private String mImage_url;
        private int mServing;
        private int mCalories;
        private int mTotaltime;
        private int mFat;
        private int mSugar;
        private int mProtein;
        private int mFiber;
        private int mSodium;
        private int mCholestoral;
        private int mCarbs;
        private int mID;

        UpdateRecipe(String username, String Recipename, String recipe_des, String url, String image_url, String serving, String calories, String totaltime,
                  String fat, String sugar, String protein, String fiber, String sodium, String cholestoral, String carbs, int id) {
            mID = id;
            mUsername = username;
            mRecipename = Recipename;
            mRecipeDes = recipe_des;
            mUrl = url;
            mImage_url = image_url;
            mServing = Integer.parseInt(serving);
            mCalories = Integer.parseInt(calories);
            mTotaltime = Integer.parseInt(totaltime);
            mFat = Integer.parseInt(fat);
            mSugar = Integer.parseInt(sugar);
            mProtein = Integer.parseInt(protein);
            mFiber = Integer.parseInt(fiber);
            mSodium = Integer.parseInt(sodium);
            mCholestoral = Integer.parseInt(cholestoral);
            mCarbs = Integer.parseInt(carbs);
        }

        @Override
        protected void onPreExecute(){
            showProgress(true);
        }//onPreExecute

        @Override
        protected String doInBackground(String... arg0) {
            try {
                URL url = new URL("http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/recipe/update/" + mID); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                //add name pair values to the connection

                JSONObject mNutrients = new JSONObject();
                mNutrients.put("fat", mFat);
                mNutrients.put("sugar", mSugar);
                mNutrients.put("protein", mProtein);
                mNutrients.put("fiber", mFiber);
                mNutrients.put("sodium", mSodium);
                mNutrients.put("cholesterol", mCholestoral);
                mNutrients.put("carbs", mCarbs);

                JSONArray mIngredients = new JSONArray();
                for(int i = 0; i < arrayIngredient.size(); i++ ){
                    JSONObject object = new JSONObject();
                    object.put("text", arrayIngredient.get(i).getText());
                    object.put("weight", arrayIngredient.get(i).getWeight());
                    mIngredients.put(object);
                }

                postDataParams.put("username", mUsername);
                postDataParams.put("label", mRecipename);
                postDataParams.put("description", mRecipeDes);
                postDataParams.put("image", mImage_url);
                postDataParams.put("url", mUrl);
                postDataParams.put("servings", mServing);
                postDataParams.put("calories", mCalories);
                postDataParams.put("totalTime", mTotaltime);
                postDataParams.put("nutrients", mNutrients);
                postDataParams.put("ingredients", mIngredients);


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
            mAuthUpdate = null;
            Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_SHORT).show();
            showProgress(false);
        }

        @Override
        protected void onCancelled() {
            mAuthUpdate = null;
            showProgress(false);
        }
    }
}
