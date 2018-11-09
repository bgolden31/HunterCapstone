package com.example.kchu0.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Details extends AppCompatActivity {

    private ArrayList<Recipe> arrayList;
    private int position;
    private String temp;

    public String convert(String temp) {
        String new_string;
        double double_ = Double.parseDouble(temp);
        int int_ = (int) Math.round(double_);
        new_string = Integer.toString(int_);
        return new_string;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        arrayList = intent.getParcelableArrayListExtra("array");
        position = intent.getIntExtra("position", 0);

        String label = arrayList.get(position).getLabel();
        TextView label_ = (TextView) findViewById(R.id.label);
        label_.setText(label);

        ImageView imageView = (ImageView) findViewById(R.id.image);
        String img = arrayList.get(position).getImage();
        Picasso.get().load(img).into(imageView);

        String source = arrayList.get(position).getSource();
        TextView source_ = (TextView) findViewById(R.id.source);
        temp = "Source: " + source;
        source_.setText(temp);

        String calories = arrayList.get(position).getCalories();
        TextView calories_ = (TextView) findViewById(R.id.calories);
        calories = convert(calories);
        temp = "Calories: " + calories;
        calories_.setText(temp);

        String ingredient = arrayList.get(position).getIngredient();
        ingredient = ingredient.replace("[", "");
        ingredient = ingredient.replace("]", "");
        TextView ingredient_ = (TextView) findViewById(R.id.ingredient);
        temp = "Ingredients: " + ingredient;
        ingredient_.setText(temp);

        String url = arrayList.get(position).getUrl();
        TextView url_ = (TextView) findViewById(R.id.directions);
        temp = "Directions: " + url;
        url_.setText(temp);


        String fat = arrayList.get(position).getFat();
        TextView fat_ = (TextView) findViewById(R.id.fat);
        fat = convert(fat);
        temp = "Fat: " + fat + "g";
        fat_.setText(temp);

        String sugar = arrayList.get(position).getSugar();
        TextView sugar_ = (TextView) findViewById(R.id.sugar);
        sugar = convert(sugar);
        temp = "Sugar: " + sugar + "g";
        sugar_.setText(temp);

        String protein = arrayList.get(position).getProtein();
        TextView protein_ = (TextView) findViewById(R.id.protein);
        protein = convert(protein);
        temp = "Protein: " + protein + "g";
        protein_.setText(temp);

        String sodium = arrayList.get(position).getSodium();
        TextView sodium_ = (TextView) findViewById(R.id.sodium);
        sodium = convert(sodium);
        temp = "Sodium: " + sodium + "g";
        sodium_.setText(temp);

        String carbs = arrayList.get(position).getCarbs();
        TextView carbs_ = (TextView) findViewById(R.id.carbs);
        carbs = convert(carbs);
        temp = "Carbohydrate: " + carbs + "g";
        carbs_.setText(temp);

        String fiber = arrayList.get(position).getFiber();
        TextView fiber_ = (TextView) findViewById(R.id.fiber);
        fiber = convert(fiber);
        temp = "Fiber: " + fiber + "g";
        fiber_.setText(temp);

        String cholesterol = arrayList.get(position).getCholesterol();
        TextView cholesterol_ = (TextView) findViewById(R.id.cholesterol);
        cholesterol = convert(cholesterol);
        temp = "Cholesterol: " + cholesterol + "g";
        cholesterol_.setText(temp);

    }

}
