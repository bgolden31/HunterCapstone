import { Component, OnInit } from '@angular/core';
import { RecipeService } from '../../services/recipe.service';
import { SaveRecipeService } from '../../services/saverecipe.service';
import { history } from '../../models/history.model';
import { APIRecipe } from '../../models/apiRecipe.model';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';

@Component({
  selector: 'app-newpage',
  templateUrl: './newpage.component.html',
  styleUrls: ['./newpage.component.css']
})
export class NewpageComponent implements OnInit {
  stuff: APIRecipe;
  ing: string;
  recipe: history = <history>{};

  constructor(private recipeService: RecipeService, 
              private saveRecipe: SaveRecipeService,
              private router: Router,
              private cookieService: CookieService) {
  }

  ngOnInit() {
  }

  getRecipes(ing) {
    this.recipeService.getRecipes(ing)
      .subscribe((data: APIRecipe) => {
          this.stuff = data;
      });
  }

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
