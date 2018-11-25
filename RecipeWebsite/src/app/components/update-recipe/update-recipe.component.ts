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

  updateRecipe(recipeId) {
    this.recipeService.updateRecipe(recipeId)
    .subscribe((data: Object) => {
      alert(data)
    });
  }

}
