import { Component, OnInit } from '@angular/core';
import { RecipeService } from '../../services/recipe.service';
import { SaveRecipeService } from '../../services/saverecipe.service';
import { history } from '../../models/history.model';
import { APIRecipe } from '../../models/apiRecipe.model';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { recipe } from '../../models/recipe.model';


@Component({
  selector: 'app-newpage',
  templateUrl: './newpage.component.html',
  styleUrls: ['./newpage.component.css']
})
export class NewpageComponent implements OnInit {
  stuff: APIRecipe = <APIRecipe>{};
  ing: string;
  recipe: history = <history>{};

  constructor(private recipeService: RecipeService, 
              private saveRecipe: SaveRecipeService,
              private router: Router,
              private cookieService: CookieService) {
  }

  ngOnInit() {
    this.stuff.APIRecipes = [];
    this.stuff.DatabaseRecipes = [];
  }

/**
 * This function is called when the user enters
 * ingredients in the search bar (such as "chicken")
 * and want to retrieve recipes relevant to their search
 * terms. An array of recipes are returned, and are stored
 * in the 'stuff' variable.
 * 
 * @param ing A string of ingredients the user wants to search for in recipes.
 * @param size An integer representing how many recipes the user wants returned. Maaxes out at 50.
 */

  getRecipes(ing, size) {
    if (size > 50) {
      size = 50;
    }
    var search = {
      search: ing,
      size: size
    }
    var tmp = JSON.stringify(search);
    var temp = JSON.parse(tmp);
    this.recipeService.getRecipes(temp)
      .subscribe((data: APIRecipe) => {
          this.stuff = data;
      });
  }

/**
 * This function is called when the user clicks on the
 * "see details" button for a particular recipe. First,
 * the function checks if the user is logged in. If they are,
 * then the relevant information is parsed into a JSON object,
 * and the insertRecipeHistory() function is called to store the
 * recipe in the user's search history. After, the recipe is stored
 * in a temporary function, and the user is routed to a new page
 * where they see the specific details about the recipe in question.
 * 
 * @param stuff A particular recipe the user wants to know more details about.
 */

  goToPage(stuff) {
    if (this.cookieService.get('username') != null) {
      this.recipe.author = stuff.source;
      this.recipe.recipeId = -1;
      this.recipe.recipeName = stuff.label;
      this.recipe.username = this.cookieService.get('username');
      var tmp = JSON.stringify(this.recipe);
      var recTmp = JSON.parse(tmp);
      this.recipeService.insertRecipeHistory(recTmp)
      .subscribe((data: Object) => {
        alert(data)
      });
    }
    this.saveRecipe.recipe = stuff;
    this.router.navigate(['/recipedetails']);
  }
}
