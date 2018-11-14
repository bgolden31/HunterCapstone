import { Component, OnInit } from '@angular/core';
import { SaveHistoryService } from '../../services/saverecipehistory.service';
import { SaveUserInfoService } from '../../services/saveuserinfo.service';
import { SaveUserRecipes } from '../../services/saveuserrecipes.service';
import { recipe } from '../../models/recipe.model';
import { userInfo } from '../../models/userInfo.model';
import { history } from '../../models/history.model';
import { CookieService } from 'ngx-cookie-service';



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

  constructor() { }

  ngOnInit() {
    this.tmp = localStorage.getItem("userInfo");
    this.userInfo = JSON.parse(this.tmp);
    this.tmp = localStorage.getItem("recipeHistory");
    this.recipeHistory = JSON.parse(this.tmp);
    this.tmp = localStorage.getItem("userRecipes");
    this.userRecipes = JSON.parse(this.tmp);
  }

}
