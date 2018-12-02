import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { NewpageComponent } from './components/newpage/newpage.component';

import { RecipeService } from './services/recipe.service';
import { SaveRecipeService } from './services/saverecipe.service';
import { LoginService } from './services/login.service';
import { RegisterService } from './services/register.service';
import { CookieService } from 'ngx-cookie-service';      // INSTALL FROM https://www.npmjs.com/package/ngx-cookie-service


import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { RecipeinsertComponent } from './components/recipeinsert/recipeinsert.component';
import { RecipeDetailsComponent } from './components/recipe-details/recipe-details.component';
import { ProfileComponent } from './components/profile/profile.component';
import { UpdateRecipeComponent } from './components/update-recipe/update-recipe.component';

@NgModule({
  declarations: [
    AppComponent,
    NewpageComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    RecipeinsertComponent,
    RecipeDetailsComponent,
    ProfileComponent,
    UpdateRecipeComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [
    RecipeService,
    LoginService,
    RegisterService,
    SaveRecipeService,
    CookieService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
