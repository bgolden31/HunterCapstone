import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';

@Injectable()
export class RecipeService {
     constructor (private http: HttpClient) { }

     getRecipes(ingredients) {
        return this.http.get('https://api.edamam.com/search?q=' + ingredients + '&app_id=7aede50c&app_key=908364149d33f6c3fa9765757f65fcfb&from=0&to=3');
    }
}