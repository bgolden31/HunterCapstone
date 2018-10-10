import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NewpageComponent } from './components/newpage/newpage.component';
import { HomeComponent } from './components/home/home.component';


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