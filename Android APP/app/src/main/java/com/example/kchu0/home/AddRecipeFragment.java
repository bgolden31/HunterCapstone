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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
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

public class AddRecipeFragment extends Fragment {

    private String Username;
    private ArrayList<Ingredient> arrayIngredient;
    private AddRecipe mAuthAdd = null;
    private View mProgressView;
    private View mAddRecView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addrecipe, container, false);

        Bundle bundle = getArguments();
        Username = bundle.getString("username");

        mProgressView = view.findViewById(R.id.progress_form);
        mAddRecView = view.findViewById(R.id.shop_form);


        arrayIngredient = new ArrayList<>();
        ListView lv = (ListView) view.findViewById(R.id.ingredient_list);

        final EditText recipe_name = (EditText) view.findViewById(R.id.recipe_name);
        final EditText recipe_description = (EditText) view.findViewById(R.id.recipe_description);
        final EditText image_url = (EditText) view.findViewById(R.id.recipe_image);
        final EditText url = (EditText) view.findViewById(R.id.recipe_url);
        final EditText serving = (EditText) view.findViewById(R.id.recipe_serving);
        final EditText calories = (EditText) view.findViewById(R.id.recipe_calories);
        final EditText totaltime = (EditText) view.findViewById(R.id.recipe_time);
        final EditText fat = (EditText) view.findViewById(R.id.recipe_fat);
        final EditText sugar = (EditText) view.findViewById(R.id.recipe_sugar);
        final EditText protein = (EditText) view.findViewById(R.id.recipe_protein);
        final EditText fiber = (EditText) view.findViewById(R.id.recipe_fiber);
        final EditText sodium = (EditText) view.findViewById(R.id.recipe_sodium);
        final EditText cholestorol = (EditText) view.findViewById(R.id.recipe_cholesterol);
        final EditText carbs = (EditText) view.findViewById(R.id.recipe_carbohydrates);

        final CustomListIngredeint adapter = new CustomListIngredeint(
                getContext(), R.layout.custom_list_ingredient, arrayIngredient
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
                AlertDialog.Builder adb=new AlertDialog.Builder(getContext());
                adb.setTitle("Are You Sure You Want to Add the Following Item to your Ingredient List?");
                final int positionToRemove = pos;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        arrayIngredient.remove(positionToRemove);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Removed", Toast.LENGTH_SHORT).show();
                    }});
                adb.show();
                return true;
            }
        });


        Button add_btn = view.findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog =new AlertDialog.Builder(getActivity());
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
                                Toast.makeText(getContext(), "Please Try Again. Empty Field Detected", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Please Try Again. Empty Field Detected", Toast.LENGTH_SHORT).show();
                        }
                    }});
                alertDialog.show();
            }
        });


        Button create_btn = view.findViewById(R.id.create_btn);
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
                        mAuthAdd = new AddRecipe (Username, input_a, input_b, input_c, input_d, input_e, input_f, input_g,
                                input_h, input_i, input_j, input_k, input_l, input_m, input_n);
                        mAuthAdd.execute((String) "hello");
                        Toast.makeText(getContext(),"Added", Toast.LENGTH_SHORT);
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getContext(), "Missing Fields, Please Try Again!", Toast.LENGTH_SHORT).show();
                    }
                }

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

            mAddRecView.setVisibility(show ? View.GONE : View.VISIBLE);
            mAddRecView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mAddRecView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mAddRecView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Class that was used to make HTTP calls. We create a JSON Object with user input
     * and send it to the server thought HTTP call. If we get a 200ok, means we've connected
     * and accomplished what we wanted to.
     *
     * This specific class takes a number of inputs from the user, and creates a JSON file to
     * send to remote Server. It is used to add a recipe onto the remote server. If it successfully
     * added, we get a 200 ok, and App alerts user with a simple Toast Message
     **/
    public class AddRecipe extends AsyncTask<String, Void, String> {
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

        AddRecipe(String username, String Recipename, String recipe_des, String url, String image_url, String serving, String calories, String totaltime,
                  String fat, String sugar, String protein, String fiber, String sodium, String cholestoral, String carbs) {
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
                URL url = new URL("http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/recipe/insert"); // here is your URL path

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
            mAuthAdd = null;
            Toast.makeText(getContext(), "Added!", Toast.LENGTH_SHORT).show();
            showProgress(false);
        }

        @Override
        protected void onCancelled() {
            mAuthAdd = null;
            showProgress(false);
        }
    }


}
