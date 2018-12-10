import { Component, OnInit } from '@angular/core';
import { SaveRecipeService } from '../../services/saverecipe.service';
import { RecipeService } from '../../services/recipe.service';
import { recipe } from '../../models/recipe.model';
import { CookieService } from 'ngx-cookie-service';


@Component({
  selector: 'app-recipe-details',
  templateUrl: './recipe-details.component.html',
  styleUrls: ['./recipe-details.component.css']
})
export class RecipeDetailsComponent implements OnInit {
  recipe: recipe;

  constructor(private saveRecipe: SaveRecipeService,
              private cookieService: CookieService,
              private recipeService: RecipeService) { }

  ngOnInit() {
    this.recipe = this.saveRecipe.recipe;
    this.recipe.calories = Math.round(this.recipe.calories);
    this.recipe.totalTime = this.recipe.totalTime / 60;
  }

  isThereImage() {
    if (this.recipe.image == "no" || this.recipe.image == "n") {
      return false;
    }
    else {
      return true;
    }
  }

  isThereUrl() {
    if (this.recipe.url == "no" || this.recipe.url == "n") {
      return false;
    }
    else {
      return true;
    }
  }

/**
 * Allows the user to add an ingredient they found
 * from a recipe to their shopping cart. This is only
 * done from the recipe details page, and upon button
 * click, the HTTP call is made to the server where the
 * ingredient is stored in the user's shopping cart.
 *
 * @param  ing      a string representing an ingredient the user wants to add to their cart.
 * @return          a string letting the user know whether the ingredient insertion was successful or not.
 */

  addIngredientToCart(ing) {
    var ingredient = {
      username: this.cookieService.get("username"),
      ingredient: ing
    }
    var tmp = JSON.stringify(ingredient);
    var addToCart = JSON.parse(tmp);
    this.recipeService.addToShoppingCart(addToCart)
      .subscribe((data: string) => {
        alert(data);
      });
  }

/**
 * Allows the user to rate a recipe from 1 to 5.
 * A JSON object is created with the numerical rating,
 * the recipe's ID, author, name, and the user's ID.
 * A call is made to the backend server, which takes the rating
 * and factors it into the recipe's current average rating, which
 * is then updated and displayed.
 *
 * @param  rating      an integer (from 1 to 5) representing the rating a user wants to give a recipe.
 * @return             a string letting the user know whether the recipe rating was successful or not.
 */

  rateRecipe(rating) {
    if (!this.cookieService.check("username")) {
      alert("You need to be logged in to be able to rate a recipe.");
      return;
    }
    var recipe = {
      username: this.cookieService.get("username"),
      recipeName: this.recipe.label,
      author: this.recipe.author,
      recipeId: this.recipe.recipeId,
      rating: rating
    }
    var tmp = JSON.stringify(recipe);
    var rating = JSON.parse(tmp);
    this.recipeService.rateRecipe(rating)
      .subscribe((data: string) => {
        alert(data);
      });
  }

}
