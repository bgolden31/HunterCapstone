import { Component, OnInit } from '@angular/core';
import { CreateRecipeService } from '../../services/createrecipe.service';
import { recipe } from '../../models/recipe.model';
import { nutrients } from '../../models/nutrients.model';
import { ingredients } from '../../models/ingredients.model';

@Component({
  selector: 'app-recipeinsert',
  templateUrl: './recipeinsert.component.html',
  styleUrls: ['./recipeinsert.component.css']
})
export class RecipeinsertComponent implements OnInit {
  recipe: recipe = <recipe>{};
  nutrients: nutrients = <nutrients>{};
  ing: ingredients = <ingredients>{};
  username: string;
  name: string;
  desc: string;
  time: number;
  servings: number;
  calories: number;
  link: string;
  url: string; 
  fat: number;
  sugar: number;
  protein: number;
  fiber: number;
  sodium: number;
  chol: number;
  carbs: number;
  ingredient: string;
  amount: number;


  constructor(private createRecipeService: CreateRecipeService) { }

  ngOnInit() {
    this.recipe.ingredients = [{name:"x", amount:3}, {name:"y", amount:2}];
  }

  
  createRecipe() {
    this.recipe.username = this.username
    this.recipe.label = this.name;
    this.recipe.description = this.desc;
    this.recipe.image = this.link;
    this.recipe.URL = this.url;
    this.recipe.servings = this.servings;
    this.recipe.calories = this.calories;
    this.recipe.totalTime = this.time;
    this.nutrients.fat = this.fat;
    this.nutrients.sugar = this.sugar;
    this.nutrients.protein = this.protein;
    this.nutrients.fiber = this.fiber;
    this.nutrients.sodium = this.sodium;
    this.nutrients.cholesterol = this.chol;
    this.nutrients.carbs = this.carbs;
    /*
    this.ing.amount = this.amount;
    this.ing.name = this.ingredient;*/
    this.recipe.nutrients = this.nutrients;
    //this.recipe.ingredients.push(this.ing);
    var tmp = JSON.stringify(this.recipe);
    var recipe = JSON.parse(tmp);
    this.createRecipeService.createRecipe(recipe)
      .subscribe((data: Object) => {
        alert(data)
      });
  }
}
