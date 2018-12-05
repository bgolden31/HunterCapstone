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
  }

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
