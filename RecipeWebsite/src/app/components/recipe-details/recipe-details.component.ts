import { Component, OnInit } from '@angular/core';
import { SaveRecipeService } from '../../services/saverecipe.service';

@Component({
  selector: 'app-recipe-details',
  templateUrl: './recipe-details.component.html',
  styleUrls: ['./recipe-details.component.css']
})
export class RecipeDetailsComponent implements OnInit {
  recipe: Object;

  constructor(private saveRecipe: SaveRecipeService) { }

  ngOnInit() {
    this.recipe = this.saveRecipe.recipe;
  }

}
