package com.example.kchu0.home;

/**
 * Object used to store Ingredients. Used when JSONObject return has a Object that needs to store "ingredients"
 */

public class Ingredient {
    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Ingredient(int weight, String text) {
        this.weight = weight;
        this.text = text;
    }

    private int weight;
    private String text;
}
