import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { history } from "../models/history.model";
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';

@Injectable()
export class RecipeService {
     constructor (private http: HttpClient) { }

     getRecipes(ingredients) {
        return this.http.get('https://api.edamam.com/search?q=' + ingredients + '&app_id=7aede50c&app_key=908364149d33f6c3fa9765757f65fcfb&from=0&to=3');
        //return this.http.post('http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/search', );
    }

    deleteRecipe(recipeId: number) {
        return this.http.delete('http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/recipe/delete/' + recipeId, {responseType: 'text'});
    }

    updateRecipe(recipeId: number) {
        return this.http.put('http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/recipe/update/' + recipeId, {responseType: 'text'});
    }

    insertRecipeHistory(recipe: JSON) {
        return this.http.post('http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/UserHistory/insert/', recipe, {responseType: 'text'});
    }
}