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
  url: string;

  constructor(private recipeService: RecipeService) { }

  ngOnInit() {
    this.tmp = localStorage.getItem("recipe");
    this.recipe = JSON.parse(this.tmp);
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
    this.tmp = JSON.stringify(this.recipe);
    var newRecipe = JSON.parse(this.tmp);
    this.recipeService.updateRecipe(recipeId, newRecipe)
    .subscribe((data: Object) => {
      alert(data)
    });
  }
}
