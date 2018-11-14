import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { history } from "../models/history.model";
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';

@Injectable()
export class SaveHistoryService {
    public recipeHistory: Array<history>;

     constructor () { }
}