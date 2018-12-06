import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';

@Injectable()
export class RecipeService {
     constructor (private http: HttpClient) { }

/**
 * Returns recipes derived from both the Edamam API, as well
 * as our own database. The amount of recipes are divided in half,
 * with half deriving from our database, and half deriving from the
 * Edamam API. The recipes are not returned in any particular order,
 * but the recipes returned are the most relevant in terms of the
 * user's search parameters.
 *
 * @param  ingredients a JSON object containing the ingredients and how many recipes the user wants to search for.
 * @return             recipes most relevant to the user's search parameters.
 */

     getRecipes(ingredients: JSON) {
        return this.http.post('http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/recipe/search', ingredients);
    }

/**
 * Returns a string to present to the user to indicate whether or not the
 * recipe deletion was successful. The user is only to delete their own recipes,
 * and can only do so from their profile page. The deletion is thorough and erases
 * all trace of the recipe within our database based on the recipe ID.
 *
 * @param  recipeId a number representing the ID number of the recipe to delete.
 * @return          a string indicating whether or not the deletion was successful.
 */

    deleteRecipe(recipeId: number) {
        return this.http.delete('http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/recipe/delete/' + recipeId, {responseType: 'text'});
    }

/**
 * Returns a string to present to the user to indicate whether or not the
 * recipe edit was successful. The user is only to edit their own recipes,
 * and can only do so from their profile page. The edit page is populated
 * with the pre-existing values that were given at the time the recipe was
 * added into the database. From there, the user can pick and choose which
 * fields they would like to change.
 *
 * @param  recipeId a number representing the ID number of the recipe to edit.
 * @param  recipe a JSON object containing the new values of the recipe.
 * @return      a string indicating whether or not the edit was successful.
 */

    updateRecipe(recipeId: number, recipe: JSON) {
        return this.http.put('http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/recipe/update/' + recipeId, recipe, {responseType: 'text'});
    }

/**
 * Returns a string to present to the user to indicate whether or not the
 * recipe was successfully added into the user's viewing history. This method
 * is called when a user searches for recipes based on certain parameters (using
 * the getRecipes function above), and clicks the "more details" button. Once the
 * button is clicked, this function is called, and the recipe is inserted into their
 * viewing history.
 *
 * @param  recipe a JSON object containing the recipe to be inserted into the user's history.
 * @return        a string indicating whether or not the insertion was successful.
 */

    insertRecipeHistory(recipe: JSON) {
        return this.http.post('http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/UserHistory/insert/', recipe, {responseType: 'text'});
    }

/**
 * Returns a string to present to the user to indicate whether or not the
 * recipe creation was successful. The user has the chance to insert values
 * for everything relevant to the recipe, including nutrients and ingredients.
 * This function is then called, and the user is notified whether the creation
 * was successful or not.
 *
 * @param  recipe a JSON object containing the values of the recipe to be created.
 * @return        a string indicating whether or not the recipe creation was successful.
 */

    createRecipe(recipe: JSON) {
        return this.http.post('http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/recipe/insert', recipe, {responseType: 'text'});
    }

/**
 * Returns a string to present to the user to indicate whether or not the
 * recipe rating update was successful. The user has the chance, once logged in,
 * to rate a recipe. The rating is then factored into the average, which is then
 * displayed on the recipe details page. The user can only rate only if they're
 * logged in, and while their rating can change, it will only be factored into
 * the average of the recipe's rating once.
 *
 * @param  recipe a JSON object containing the recipe, recipeId, and rating.
 * @return        a string indicating whether or not the recipe creation was successful.
 */

    rateRecipe(recipe: JSON) {
        return this.http.post('http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/recipe/update/rating', recipe, {responseType: 'text'});
    }

/**
 * Returns a string to present to the user to indicate whether or not the
 * ingredient was added to their shopping cart or not. The user has the chance,
 * once logged in, to be able to add ingredients from a recipe's details page to
 * their shopping cart. 
 *
 * @param  ingredient a JSON object containing the ingredient and the user's username.
 * @return            a string indicating whether or not the ingredient was added.
 */

    addToShoppingCart(ingredient: JSON) {
        return this.http.post('http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/UserShopping/insert', ingredient, {responseType: 'text'});
    }

    
/**
 * Returns a string to present to the user to indicate whether or not the
 * ingredient was added to their shopping cart or not. The user has the chance,
 * once logged in, to be able to add ingredients from a recipe's details page to
 * their shopping cart. 
 *
 * @param  username   a string containing the user's username.
 * @return            a JSON object containing the user's shopping list represented as an array of strings.
 */

    getShoppingCart(username: string) {
        return this.http.get('http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/UserShopping/get/' + username);
    }
}