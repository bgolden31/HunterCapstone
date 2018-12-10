package com.example.kchu0.home;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Recipe Class is used to store responses sent by Remote Server. Recipe Class holds the objects that
 * appear in JSON Objects.
 */

public class Recipe implements Parcelable {

    public Recipe(String image, int serving, int totaltime, String author, int rating, String description, String ingredients, String label, int calories, String url, int recipeID, int sodium, int fiber, int carbs, int protein, int fat, int cholesterol, int sugar) {
        this.image = image;
        this.serving = serving;
        this.totaltime = totaltime;
        this.author = author;
        this.rating = rating;
        this.description = description;
        this.ingredients = ingredients;
        this.label = label;
        this.calories = calories;
        this.url = url;
        this.recipeID = recipeID;
        this.sodium = sodium;
        this.fiber = fiber;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
        this.cholesterol = cholesterol;
        this.sugar = sugar;
    }

    protected Recipe(Parcel in) {
        image = in.readString();
        serving = in.readInt();
        totaltime = in.readInt();
        author = in.readString();
        rating = in.readInt();
        description = in.readString();
        ingredients = in.readString();
        label = in.readString();
        calories = in.readInt();
        url = in.readString();
        recipeID = in.readInt();
        sodium = in.readInt();
        fiber = in.readInt();
        carbs = in.readInt();
        protein = in.readInt();
        fat = in.readInt();
        cholesterol = in.readInt();
        sugar = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeInt(serving);
        dest.writeInt(totaltime);
        dest.writeString(author);
        dest.writeInt(rating);
        dest.writeString(description);
        dest.writeString(ingredients);
        dest.writeString(label);
        dest.writeInt(calories);
        dest.writeString(url);
        dest.writeInt(recipeID);
        dest.writeInt(sodium);
        dest.writeInt(fiber);
        dest.writeInt(carbs);
        dest.writeInt(protein);
        dest.writeInt(fat);
        dest.writeInt(cholesterol);
        dest.writeInt(sugar);
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

    public int getServing() {
        return serving;
    }

    public void setServing(int serving) {
        this.serving = serving;
    }

    public int getTotaltime() {
        return totaltime;
    }

    public void setTotaltime(int totaltime) {
        this.totaltime = totaltime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    public int getSodium() {
        return sodium;
    }

    public void setSodium(int sodium) {
        this.sodium = sodium;
    }

    public int getFiber() {
        return fiber;
    }

    public void setFiber(int fiber) {
        this.fiber = fiber;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public int getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(int cholesterol) {
        this.cholesterol = cholesterol;
    }

    public int getSugar() {
        return sugar;
    }

    public void setSugar(int sugar) {
        this.sugar = sugar;
    }

    private String image;
    private int serving;
    private int totaltime;
    private String author;
    private int rating;
    private String description;
    private String ingredients;
    private String label;
    private int calories;
    private String url;
    private int recipeID;
    private int sodium;
    private int fiber;
    private int carbs;
    private int protein;
    private int fat;
    private int cholesterol;
    private int sugar;

}