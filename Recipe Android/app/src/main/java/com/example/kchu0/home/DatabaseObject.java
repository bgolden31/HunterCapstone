
package com.example.kchu0.home;


/** Class Object created based of the return values from server.
 * Mainly used in Favourite, History. Used when the JSON return file
 * has three properties that need to be saved. Class mainly consist of
 * constructors, getters, and setters.
 */

public class DatabaseObject {
    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    public DatabaseObject(String recipeName, String author, int recipeID) {
        this.recipeName = recipeName;
        this.author = author;
        this.recipeID = recipeID;
    }

    private String recipeName;
    private String author;
    private int recipeID;
}
