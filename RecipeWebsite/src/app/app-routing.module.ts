import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NewpageComponent } from './components/newpage/newpage.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { RecipeinsertComponent } from './components/recipeinsert/recipeinsert.component';



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