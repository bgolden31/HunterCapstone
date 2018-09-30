import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NewpageComponent } from './components/newpage/newpage.component';

const routes: Routes = [
    {
        path: 'test',
        component: NewpageComponent,
    },
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