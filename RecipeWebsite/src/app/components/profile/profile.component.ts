import { Component, OnInit } from '@angular/core';
import { recipe } from '../../models/recipe.model';
import { userInfo } from '../../models/userInfo.model';
import { history } from '../../models/history.model';
import { RecipeService } from '../../services/recipe.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  username: string;
  userInfo: userInfo;
  recipeHistory: Array<history>;
  userRecipes: Array<recipe>;
  tmp: string;
  recipeId: number;

  constructor(private recipeService: RecipeService,
              private router: Router) { }

  ngOnInit() {
    this.tmp = localStorage.getItem("userInfo");
    this.userInfo = JSON.parse(this.tmp);
    this.tmp = localStorage.getItem("recipeHistory");
    this.recipeHistory = JSON.parse(this.tmp);
    this.tmp = localStorage.getItem("userRecipes");
    this.userRecipes = JSON.parse(this.tmp);
  }

/**
 * This function is called when the user clicks
 * the "delete recipe" function next to a recipe
 * on their profile. The recipe's ID is passed to the
 * service, which then calls the deletion function
 * on the server. The recipe is then deleted from the
 * database.
 * 
 * @param recipeId the ID of the recipe to be deleted.
 */

  deleteRecipe(recipeId) {
    this.recipeService.deleteRecipe(recipeId)
    .subscribe((data: Object) => {
      alert(data)
    });
  }

/**
 * This function is called when the user clicks
 * the "edit recipe" function next to a recipe
 * on their profile. The recipe itself is stored
 * in local storage, and then the user is routed
 * to the update recipe page.
 * 
 * @param recipe the recipe in question to be updated.
 */

  updateRecipe(recipe) {
    this.tmp = JSON.stringify(recipe);
    localStorage.setItem("recipe", this.tmp);
    this.router.navigate(['/update']);
  }

}
