package com.example.kchu0.test;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable {

    public Recipe(String image, String label, String source, String calories, String ingredient, String url, String fat, String sugar, String protein, String sodium, String carbs, String fiber, String cholesterol) {
        this.image = image;
        this.label = label;
        this.source = source;
        this.calories = calories;
        this.ingredient = ingredient;
        this.url = url;
        this.fat = fat;
        this.sugar = sugar;
        this.protein = protein;
        this.sodium = sodium;
        this.carbs = carbs;
        this.fiber = fiber;
        this.cholesterol = cholesterol;
    }

    protected Recipe(Parcel in) {
        image = in.readString();
        label = in.readString();
        source = in.readString();
        calories = in.readString();
        ingredient = in.readString();
        url = in.readString();
        fat = in.readString();
        sugar = in.readString();
        protein = in.readString();
        sodium = in.readString();
        carbs = in.readString();
        fiber = in.readString();
        cholesterol = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(label);
        dest.writeString(source);
        dest.writeString(calories);
        dest.writeString(ingredient);
        dest.writeString(url);
        dest.writeString(fat);
        dest.writeString(sugar);
        dest.writeString(protein);
        dest.writeString(sodium);
        dest.writeString(carbs);
        dest.writeString(fiber);
        dest.writeString(cholesterol);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getImage() {

        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getSugar() {
        return sugar;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getSodium() {
        return sodium;
    }

    public void setSodium(String sodium) {
        this.sodium = sodium;
    }

    public String getCarbs() {
        return carbs;
    }

    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }

    public String getFiber() {
        return fiber;
    }

    public void setFiber(String fiber) {
        this.fiber = fiber;
    }

    public String getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(String cholesterol) {
        this.cholesterol = cholesterol;
    }

    private String image;
    private String label;
    private String source;
    private String calories;
    private String ingredient;
    private String url; //Instructions link
    private String fat;
    private String sugar;
    private String protein;
    private String sodium;
    private String carbs;
    private String fiber;
    private String cholesterol;
}