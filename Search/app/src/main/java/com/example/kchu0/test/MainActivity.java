package com.example.kchu0.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    private String msg;
    private EditText input;
    private String diet = "&diet=";
    private String health = "&health=";

    public boolean check_diet() {
        char charAt = diet.charAt(diet.length()-1);
        if(charAt == '=') {
            return true;
        } else {
            return false;
        }
    }
    public boolean check_health() {
        char charAt = health.charAt(health.length()-1);
        if(charAt == '=') {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = (EditText) findViewById(R.id.editText);

        final Switch balanced = (Switch) findViewById(R.id.bal_switch);
        final Switch Protein = (Switch) findViewById(R.id.protein_switch);
        final Switch Carbs = (Switch) findViewById(R.id.carb_switch);

        final Switch Alcohol = (Switch) findViewById(R.id.alco_switch);
        final Switch Vegan = (Switch) findViewById(R.id.vegan_switch);
        final Switch Peanut = (Switch) findViewById(R.id.peanut_switch);
        final Switch Vegetarian = (Switch) findViewById(R.id.vegetarian_switch);

        Button test_click = (Button) findViewById(R.id.button_his);
        test_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, History.class);
                //myIntent.putExtra("key", value); //Optional parameters
                startActivity(myIntent);
            }
        });


        Button clickButton = (Button) findViewById(R.id.button);
        clickButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = input.getText().toString();

                if (balanced.isChecked())
                    if(check_diet() == true) {
                        diet = diet + "balanced";
                    } else {
                        diet = diet + "&balanced";
                    }

                if(Protein.isChecked()){
                    if(check_diet() == true) {
                        diet = diet + "high-protein";
                    } else {
                        diet = diet + "&high-protein";
                    }
                }

                if(Carbs.isChecked()) {
                    if(check_diet() == true) {
                        diet = diet + "low-carb";
                    } else {
                        diet = diet + "&low-carb";
                    }
                }


                if(check_diet() == false) {
                    msg = "https://api.edamam.com/search?q=" + key +  diet;
                } else {
                    msg = "https://api.edamam.com/search?q=" + key;
                }

                if(Alcohol.isChecked()) {
                    if(check_health() == true) {
                        health = health + "alcohol-free";
                    } else {
                        health = health + "&alcohol-free";
                    }
                }

                if(Vegan.isChecked()) {
                    if(check_health() == true) {
                        health = health + "vegan";
                    } else {
                        health = health + "&vegan";
                    }
                }

                if(Peanut.isChecked()) {
                    if(check_health() == true) {
                        health = health + "peanut-free";
                    } else {
                        health = health + "&peanut-free";
                    }
                }

                if(Vegetarian.isChecked()) {
                    if(check_health() == true) {
                        health = health + "vegetarian";
                    } else {
                        health = health + "&vegetarian";
                    }
                }

                if(check_health() == false) {
                    msg = msg + health;
                }

                //Toast.makeText(getApplicationContext(), msg , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Search.class);
                intent.putExtra("keyMessage", msg);
                msg = ""; //Resets
                diet = "&diet="; //Resets
                health = "&health="; //Resets
                startActivity(intent);
            }
        });
    }




}