import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { NewpageComponent } from './components/newpage/newpage.component';

import { RecipeService } from './services/recipe.service';
import { LoginService } from './services/login.service';
import { RegisterService } from './services/register.service';
import { CreateRecipeService } from './services/createrecipe.service';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { RecipeinsertComponent } from './components/recipeinsert/recipeinsert.component';

@NgModule({
  declarations: [
    AppComponent,
    NewpageComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    RecipeinsertComponent
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
    CreateRecipeService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
