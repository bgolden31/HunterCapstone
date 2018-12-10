import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { recipe } from '../models/recipe.model';

import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';

@Injectable()
export class SaveRecipeService {
    public recipe: recipe;

     constructor () { }
}