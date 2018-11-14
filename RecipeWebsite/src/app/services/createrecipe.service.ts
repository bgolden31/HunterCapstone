import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';

@Injectable()
export class CreateRecipeService {
     constructor (private http: HttpClient) { }

     createRecipe(recipe: JSON) {
        return this.http.post('http://recipe-env.3ixtdbsqwn.us-east-2.elasticbeanstalk.com/recipe/insert', recipe, {responseType: 'text'});
    }
}