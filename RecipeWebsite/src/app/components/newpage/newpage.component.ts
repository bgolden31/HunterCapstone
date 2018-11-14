import { Component, OnInit } from '@angular/core';
import { RecipeService } from '../../services/recipe.service';
import { SaveRecipeService } from '../../services/saverecipe.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-newpage',
  templateUrl: './newpage.component.html',
  styleUrls: ['./newpage.component.css']
})
export class NewpageComponent implements OnInit {
  stuff: Object;
  ing: string;

  constructor(private recipeService: RecipeService, 
              private saveRecipe: SaveRecipeService,
              private router: Router) {
  }

  ngOnInit() {
  }

  getRecipes(ing) {
    this.recipeService.getRecipes(ing)
      .subscribe((data: Object) => {
          this.stuff = data;
      });
  }

  goToPage(stuff) {
    this.saveRecipe.recipe = stuff;
    this.router.navigate(['/recipedetails']);
  }
}
