import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NewpageComponent } from './components/newpage/newpage.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { RecipeinsertComponent } from './components/recipeinsert/recipeinsert.component';
import { RecipeDetailsComponent } from './components/recipe-details/recipe-details.component';
import { ProfileComponent } from './components/profile/profile.component';
import { UpdateRecipeComponent } from './components/update-recipe/update-recipe.component';



const routes: Routes = [
    {
        path: 'home',
        component: HomeComponent,
    },

    {
        path: 'test',
        component: NewpageComponent,
    },

    {
        path: 'login',
        component: LoginComponent,
    },

    {
        path: 'register',
        component: RegisterComponent,
    },

    {
        path: 'createrecipe',
        component: RecipeinsertComponent,
    },

    {
        path: 'recipedetails',
        component: RecipeDetailsComponent,
    },

    {
        path: 'profile',
        component: ProfileComponent,
    },

    {
        path: 'update',
        component: UpdateRecipeComponent,
    },


    {
        path: '**', 
        component: HomeComponent,
    }
];

@NgModule({
    imports: [
        RouterModule.forRoot(routes)
    ],
    exports: [
        RouterModule
    ],
    declarations: []
})
export class AppRoutingModule { }