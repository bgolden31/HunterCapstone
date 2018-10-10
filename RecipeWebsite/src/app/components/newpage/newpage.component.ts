import { Component, OnInit } from '@angular/core';
import { RecipeService } from '../../services/recipe.service';
import { recipe } from '../../models/recipe.model';

@Component({
  selector: 'app-newpage',
  templateUrl: './newpage.component.html',
  styleUrls: ['./newpage.component.css']
})
export class NewpageComponent implements OnInit {
  stuff: Object;
  ing: string;

  constructor(private recipeService: RecipeService) {
  }

  ngOnInit() {
    //this.getRecipes(ingredients);
  }

  getRecipes(ing) {
    this.recipeService.getRecipes(ing)
      .subscribe((data: Object) => {
          this.stuff = data;
      });
  }
}
