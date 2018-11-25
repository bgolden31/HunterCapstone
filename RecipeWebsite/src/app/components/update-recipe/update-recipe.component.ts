import { Component, OnInit } from '@angular/core';
import { RecipeService } from '../../services/recipe.service';
import { recipe } from '../../models/recipe.model';

@Component({
  selector: 'app-update-recipe',
  templateUrl: './update-recipe.component.html',
  styleUrls: ['./update-recipe.component.css']
})
export class UpdateRecipeComponent implements OnInit {
  recipe: recipe;
  tmp: string;
  test: string;

  constructor(private recipeService: RecipeService) { }

  ngOnInit() {
    this.tmp = localStorage.getItem("recipe");
    this.recipe = JSON.parse(this.tmp);
    this.test = this.recipe.URL;
  }

/**
 * This function is called when the user wants
 * to update details for a recipe they have
 * previously created. The pre-existing details
 * are shown on the view when they go to the
 * update page, where they can update anything
 * for their recipe. This is all stored in the
 * newRecipe variable, which is then parsed
 * and passed as a JSON object to the service.
 */

  updateRecipe(recipeId) {
    this.recipe.ingredients = [{name:"x", amount:3}, {name:"y", amount:2}];
    this.tmp = JSON.stringify(this.recipe);
    var newRecipe = JSON.parse(this.tmp);
    this.recipeService.updateRecipe(recipeId, newRecipe)
    .subscribe((data: Object) => {
      alert(data)
    });
  }
}
