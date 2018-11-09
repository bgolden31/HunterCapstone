package com.example.kchu0.test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    ArrayList<Recipe> arrayList;
    ListView lv;

    // URL to get contacts JSON
    private static String url;

    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Bundle extras = getIntent().getExtras();
        String msg = extras.getString("keyMessage");
        url = msg;

        arrayList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listView);

        new GetRecipes().execute();
    }

    private class GetRecipes extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Search.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray return_ = jsonObject.getJSONArray("hits");

                    for (int i = 0; i < return_.length(); i++) {
                        JSONObject c = return_.getJSONObject(i);
                        JSONObject recipe = c.getJSONObject("recipe");
                        JSONObject nutrient = recipe.getJSONObject("totalNutrients");
                        JSONObject fat = nutrient.getJSONObject("FAT");
                        JSONObject sugar = nutrient.getJSONObject("SUGAR");
                        JSONObject protein = nutrient.getJSONObject("PROCNT");
                        JSONObject sodium = nutrient.getJSONObject("NA");
                        JSONObject carbs = nutrient.getJSONObject("CHOCDF");
                        JSONObject fiber = nutrient.getJSONObject("FIBTG");
                        JSONObject cholesterol = nutrient.getJSONObject("CHOLE");
                        arrayList.add(new Recipe(
                                recipe.getString("image"),
                                recipe.getString("label"),
                                recipe.getString("source"),
                                recipe.getString("calories"),
                                recipe.getString("ingredientLines"),
                                recipe.getString("url"),
                                fat.getString("quantity"),
                                sugar.getString("quantity"),
                                protein.getString("quantity"),
                                sodium.getString("quantity"),
                                carbs.getString("quantity"),
                                fiber.getString("quantity"),
                                cholesterol.getString("quantity")
                        ));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /*
             * Updating parsed JSON data into ListView
             */
            CustomListAdapter adapter = new CustomListAdapter(
                    getApplicationContext(), R.layout.custom_list_layout, arrayList
            );
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent intent = new Intent(Search.this, Details.class);
                    intent.putParcelableArrayListExtra("array", arrayList);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            });
        }
    }
}